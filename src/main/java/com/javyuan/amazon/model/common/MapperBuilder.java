package com.javyuan.amazon.model.common;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.javyuan.amazon.model.utils.CamelCaseUtils;

/**
 * @author javyuan 
 * 2016年7月22日
 */
public class MapperBuilder<T> {

	/**
	 * 自动生成RowMapper 
	 * TODO 支持Bean的嵌套
	 */
	public RowMapper<T> getMapper(T bean) {
		RowMapper<T> mapper = new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				T resultBean = null;
				try {
					resultBean = (T) bean.getClass().newInstance();
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				Field[] fields = bean.getClass().getDeclaredFields();
				for (Field field : fields) {
					String name = field.getName();
					if (name != null && name.indexOf("_") < 0) {
						try {
							// 相当于调用rs.getXxx("xxx");
							Object value = rs.getClass().getMethod("get" + field.getType().getSimpleName(), String.class).invoke(rs, CamelCaseUtils.toUnderlineName(name));
							// 相当于调用bean.setXxx(xxx);
							bean.getClass().getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), field.getType()).invoke(resultBean, value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return resultBean;
			}
		};
		return mapper;
	}

}
