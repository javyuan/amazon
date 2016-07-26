package com.javyuan.amazon.model.base;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.javyuan.amazon.model.common.MapperBuilder;
import com.javyuan.amazon.model.common.SQLBuilder;
import com.javyuan.amazon.model.common.SQLBuilderException;



/**
 * Dao基类，提供基本的增删改查操作
 * @author javyuan
 */
public abstract class BaseDao<T extends BaseBean> {

	public static final Log log = LogFactory.getLog(BaseDao.class);
	
	protected NamedParameterJdbcTemplate jdbcTemplate;
	protected MapperBuilder<T> mapperBuilder = new MapperBuilder<T>();
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
	
	/**
	 * 列表查询
	 * @return List<T>
	 */
	public List<T> queryList(T bean) {
		List<T> list;
		try {
			list = jdbcTemplate.query(new SQLBuilder<T>(bean).buildQuerySQL(), new BeanPropertySqlParameterSource(bean), mapperBuilder.getMapper(bean));
		} catch (SQLBuilderException e) {
			log.error(e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询一个
	 * @return T
	 */
	public T queryOne(T bean) {
		List<T> list = queryList(bean);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 插入
	 * @return int
	 *
	 */
	public int insert(T bean) {
		try {
			return jdbcTemplate.update(new SQLBuilder<T>(bean).buildInsertSQL(), new BeanPropertySqlParameterSource(bean));
		} catch (SQLBuilderException e) {
			log.error(e);
			return 0;
		}
	}
	
	/**
	 * 批量插入
	 * @return int[]
	 *
	 */
	public int[] batchInsert(T[] beans) {
		try {
			return jdbcTemplate.batchUpdate(new SQLBuilder<T>(beans[0]).buildInsertSQL(), SqlParameterSourceUtils.createBatch(beans));
		} catch (SQLBuilderException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * 更新
	 * @return int
	 *
	 */
	public int update(T bean) {
		try {
			return jdbcTemplate.update(new SQLBuilder<T>(bean).buildUpdateSQL(), new BeanPropertySqlParameterSource(bean));
		} catch (SQLBuilderException e) {
			log.error(e);
			return 0;
		}
	}
	
	/**
	 * 批量更新
	 * @return int[]
	 *
	 */
	public int[] batchUpdate(T[] beans) {
		try {
			return jdbcTemplate.batchUpdate(new SQLBuilder<T>(beans[0]).buildUpdateSQL(), SqlParameterSourceUtils.createBatch(beans));
		} catch (SQLBuilderException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * 删除
	 * @return int
	 *
	 */
	public int delete(T bean) {
		try {
			return jdbcTemplate.update(new SQLBuilder<T>(bean).buildDeleteSQL(), new BeanPropertySqlParameterSource(bean));
		} catch (SQLBuilderException e) {
			log.error(e);
			return 0;
		}
	}
	
	/**
	 * 批量删除
	 * @return int[]
	 *
	 */
	public int[] batchDelete(T[] beans) {
		try {
			return jdbcTemplate.batchUpdate(new SQLBuilder<T>(beans[0]).buildDeleteSQL(), SqlParameterSourceUtils.createBatch(beans));
		} catch (SQLBuilderException e) {
			log.error(e);
			return null;
		}
	}
}
