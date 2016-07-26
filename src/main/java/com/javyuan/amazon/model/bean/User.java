package com.javyuan.amazon.model.bean;

import com.javyuan.amazon.model.base.BaseBean;

/**
 * @author javyuan 
 * 2016年7月25日
 */
public class User extends BaseBean {

	public User() {
		super();
	}

	public User(String id) {
		super(id);
	}

	private String userName;
	private String password;
	private String email;
	private String phone;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
