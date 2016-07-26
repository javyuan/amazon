package com.javyuan.amazon.model.bean;

import com.javyuan.amazon.model.base.BaseBean;

/**
 * @author javyuan 
 * 2016年7月22日
 */
public class Product extends BaseBean {
	private String title;
	private String image;

	public Product() {
		super();
	}

	public Product(String id) {
		super(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
