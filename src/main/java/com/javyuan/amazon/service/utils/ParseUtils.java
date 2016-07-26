package com.javyuan.amazon.service.utils;

import java.math.BigDecimal;

import com.javyuan.amazon.model.bean.PriceHistory;

/**
 * @author javyuan
 * 2016年7月26日
 */
public class ParseUtils {
	
	private static final String NAME_KEY = "<span id=\"productTitle\" class=\"a-size-large\">";
	private static final String PRICE_KEY = "<span id=\"priceblock_ourprice\" class=\"a-size-medium a-color-price\">$";
	private static final String SHIP_FEE_KEY = "<span id=\"ourprice_shippingmessage\">";
	private static final String GLOBAL_SHIP_KEY = "AmazonGlobal Priority Shipping";
	private static final String END_KEY = "</span>";
	private static final String AVAIABLE_KEY = "Available from";

	/**
	 * 解析商品名称
	 */
	public static String parseName(String html) {
		String temp = html.substring(html.indexOf(NAME_KEY)+ NAME_KEY.length());
		String name = temp.substring(0, temp.indexOf(END_KEY)).trim();
		return name;
	}
	/**
	 * 解析商品价格
	 */
	public static BigDecimal parsePrice(String html) {
		String temp = html.substring(html.indexOf(PRICE_KEY) + PRICE_KEY.length());
		String price = temp.substring(0, temp.indexOf(END_KEY)).trim();
		return new BigDecimal(price);
	}
	/**
	 * 解析商品运费（一般支持global ship的商品才显示运费）
	 */
	public static BigDecimal parseShipFee(String html) {
		try {
			String temp = html.substring(html.indexOf(SHIP_FEE_KEY));
			String temp2 = temp.substring(temp.indexOf("<span class=\"a-size-base a-color-secondary\">"));
			String temp3 = temp2.substring(temp2.indexOf("$") + 1);
			String shipFee = temp3.substring(0, temp3.indexOf(" ")).trim();
			return new BigDecimal(shipFee);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	/**
	 * 解析商品是否支持global ship
	 */
	public static String parseGlobalShip(String html) {
		int temp = html.indexOf(GLOBAL_SHIP_KEY);
		if (temp == -1) {
			return PriceHistory.GLOBAL_SHIP_NO;
		} else {
			return PriceHistory.GLOBAL_SHIP_YES;
		}
	}
	/**
	 * 解析商品是否可以购买
	 */
	public static boolean isNotAvailable(String html) {
		int temp = html.indexOf(AVAIABLE_KEY);
		return temp != -1;
	}

}
