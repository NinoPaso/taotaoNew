package com.taotao.pojo;

import java.util.Date;

public class TbItemParamPage {
	private Long id;

    private TbItemCat tbItemCat;

    private Date created;

    private Date updated;

    private String paramData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TbItemCat getTbItemCat() {
		return tbItemCat;
	}

	public void setTbItemCat(TbItemCat tbItemCat) {
		this.tbItemCat = tbItemCat;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getParamData() {
		return paramData;
	}

	public void setParamData(String paramData) {
		this.paramData = paramData;
	}
    
    
}
