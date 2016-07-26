package com.javyuan.amazon.model.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.javyuan.amazon.model.base.BaseBean;

/**
 * @author javyuan
 * 
 *        2016年7月22日
 */
public class UserProduct extends BaseBean {

	private String userId;
	private String productId;
	private BigDecimal remindPrice;
	private Date createDate;

	public UserProduct() {
		super();
	}

	public UserProduct(String id) {
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public BigDecimal getRemindPrice() {
		return remindPrice;
	}

	public void setRemindPrice(BigDecimal remindPrice) {
		this.remindPrice = remindPrice;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
