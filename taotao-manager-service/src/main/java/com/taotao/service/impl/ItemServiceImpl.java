package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.service.ItemService;

/**
 * @author Administrator
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService{
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Override
	public TbItem getItemById(long itemId) {
		//TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// TODO Auto-generated method stub
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null && list.size()>0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}

	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
//		PageHelper.startPage(page,rows);
//		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByPageRows(page, rows);
		int total = itemMapper.selectTotal();
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		//total取值
		result.setTotal(total);
		return result;
	}

	@Override
	public TaotaoResult createItem(TbItem item,String desc) throws Exception {
		//补全商品信息
		item.setId(IDUtils.genItemId());
		//商品状态
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		//添加商品描述信息
		TaotaoResult result = insertItemDesc(IDUtils.genItemId(), desc);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		return TaotaoResult.ok();
	}
	/**
	 * 添加商品描述
	 * <p>Title: insertItemDesc</p>
	 * <p>Description: </p>
	 * @param desc
	 */
	private TaotaoResult insertItemDesc(Long itemId, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
}
