package com.javyuan.amazon.model.base;

/**
 * @author javyuan
 *
 *         2016年7月22日
 */
public abstract class BaseBean {

	protected String delFlag;
	protected String id;

	public BaseBean() {
	}

	public BaseBean(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DEL = "1";
}
