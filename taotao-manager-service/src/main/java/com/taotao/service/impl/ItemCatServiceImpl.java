package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EUTreeNode> getCatList(long parentId) {
		// TODO Auto-generated method stub
		TbItemCatExample example = new TbItemCatExample();
		TbItemCatExample.Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//根据条件查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//查询列表转换成TreeNode
		List<EUTreeNode> resultList = new ArrayList<>();
		for(TbItemCat tbItemCat:list) {
			EUTreeNode node = new EUTreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}	
		return resultList;
	}

}
