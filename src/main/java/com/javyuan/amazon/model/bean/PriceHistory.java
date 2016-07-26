package com.javyuan.amazon.model.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.javyuan.amazon.model.base.BaseBean;

/**
 * @author javyuan 2016年7月22日
 */
public class PriceHistory extends BaseBean {
	private String productId;
	private Date createDate;
	private BigDecimal price;
	private String globalShip;
	private BigDecimal shipFee;

	public PriceHistory() {
		super();
	}

	public PriceHistory(String id) {
		super(id);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getGlobalShip() {
		return globalShip;
	}

	public void setGlobalShip(String globalShip) {
		this.globalShip = globalShip;
	}

	public BigDecimal getShipFee() {
		return shipFee;
	}

	public void setShipFee(BigDecimal shipFee) {
		this.shipFee = shipFee;
	}

	public static final String GLOBAL_SHIP_YES = "Y";
	public static final String GLOBAL_SHIP_NO = "N";
}
