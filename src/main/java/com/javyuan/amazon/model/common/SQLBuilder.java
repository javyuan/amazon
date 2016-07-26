package com.javyuan.amazon.model.common;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.javyuan.amazon.model.bean.UserProduct;
import com.javyuan.amazon.model.utils.CamelCaseUtils;

/**
 * @author javyuan
 * 2016年7月22日
 * 自动生成符合JDBCTemplate语法的增删改查、批量增删改的sql语句
 */
public class SQLBuilder<T> {
	
	private static final Log log = LogFactory.getLog(SQLBuilder.class);

	private T bean;
	private HashMap<String,Object> beanMap = new HashMap<String,Object>();
	
	public SQLBuilder(T bean) throws SQLBuilderException{
		if (bean == null) {
			throw new SQLBuilderException("bean can not be null");
		}
		this.bean = bean;
		initBeanMap(bean);
	}
	
	public String buildDeleteSQL() throws SQLBuilderException {
		checkBean();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(getTableName(bean));
		sb.append(getWhere());
		log.debug(sb.toString());
		return sb.toString();
	}

	public String buildUpdateSQL() throws SQLBuilderException {
		checkBean();
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(getTableName(bean));
		sb.append(" SET ");
		Iterator<String> it = beanMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			sb.append(CamelCaseUtils.toUnderlineName(key).toUpperCase());
			sb.append(" = ");
			sb.append(":");
			sb.append(key);
			sb.append(", ");
		}
		sb = sb.deleteCharAt(sb.lastIndexOf(","));
		log.debug(sb.toString());
		return sb.toString();
	}

	public String buildInsertSQL() throws SQLBuilderException {
		checkBean();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("INSERT INTO ");
		sb1.append(getTableName(bean));
		sb1.append("(");
		Iterator<String> it = beanMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			sb1.append(CamelCaseUtils.toUnderlineName(key).toUpperCase());
			sb1.append(", ");
			sb2.append(":");
			sb2.append(key);
			sb2.append(", ");
		}
		sb1 = sb1.deleteCharAt(sb1.lastIndexOf(","));
		sb2 = sb2.deleteCharAt(sb2.lastIndexOf(","));
		sb1.append(")");
		sb1.append(" VALUES (");
		sb2.append(")");
		log.debug(sb1.toString()+sb2.toString());
		return sb1.toString()+sb2.toString();
	}

	private void checkBean() throws SQLBuilderException {
		if (beanMap.isEmpty()) {
			throw new SQLBuilderException("empty bean");
		}
	}

	public String buildQuerySQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(getTableName(bean));
		sb.append(getWhere());
		log.debug(sb.toString());
		return sb.toString();
	}
	
	private void initBeanMap(T bean) {
		try {
			// bean类中的变量存入map
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				Object value = null;
				if (name != null && name.indexOf("_") < 0) {
					value = bean.getClass().getMethod("get"+name.substring(0, 1).toUpperCase() + name.substring(1), null).invoke(bean, null);
				}
				if (value != null) {
					beanMap.put(name, value);
				}
			}
			// bean父类中的变量存入map
			Field[] suerfields = bean.getClass().getSuperclass().getDeclaredFields();
			for (Field field : suerfields) {
				String name = field.getName();
				Object value = null;
				if (name != null && name.indexOf("_") < 0) {
					value = bean.getClass().getSuperclass().getMethod("get"+name.substring(0, 1).toUpperCase() + name.substring(1), null).invoke(bean, null);
				}
				if (value != null) {
					beanMap.put(name, value);
				}
			}
			log.debug(beanMap);
		} catch (Exception e) {
			log.error("init beanMap error", e);
		}
	}


	
	private String getTableName(T bean) {
		return CamelCaseUtils.toUnderlineName(bean.getClass().getSimpleName()).toUpperCase();
	}

	private String getWhere() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = beanMap.keySet().iterator();
		if (!it.hasNext()) {
			return "";
		}
		sb.append(" WHERE ");
		while(it.hasNext()){
			String key = it.next();
			sb.append(CamelCaseUtils.toUnderlineName(key).toUpperCase());
			sb.append(" = ");
			sb.append(":");
			sb.append(key);
			sb.append(" AND ");
		}
		return sb.substring(0, sb.lastIndexOf("AND"));
	}
	
	public static void main(String[] args) throws Exception {
//		SQLBuilder<UserProduct> builder = new SQLBuilder<UserProduct>();
		UserProduct p = new UserProduct();
		p.setId("0");
		p.setRemindPrice(new BigDecimal(100));
		p.setCreateDate(new Date());
		new SQLBuilder<UserProduct>(new UserProduct()).buildQuerySQL();
		new SQLBuilder<UserProduct>(p).buildInsertSQL();
		new SQLBuilder<UserProduct>(p).buildUpdateSQL();
		new SQLBuilder<UserProduct>(p).buildDeleteSQL();
	}
}
