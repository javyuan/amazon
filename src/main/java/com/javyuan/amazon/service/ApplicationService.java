package com.javyuan.amazon.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.javyuan.amazon.model.bean.User;
import com.javyuan.amazon.model.bean.UserProduct;
import com.javyuan.amazon.model.dao.PriceHistoryDao;
import com.javyuan.amazon.model.dao.UserDao;
import com.javyuan.amazon.model.dao.UserProductDao;
import com.javyuan.amazon.service.thread.AmazonScanner;

/**
 * @author javyuan
 * 2016年7月25日
 */

@Repository
public class ApplicationService {
	
	private static final Log log = LogFactory.getLog(ApplicationService.class);
	
	@Autowired
	UserProductDao userProductDao;
	@Autowired
	UserDao userDao;
	@Autowired
	PriceHistoryDao priceHistoryDao;

	/**
	 * 定时任务抓取amazon商品信息，每12个小时运行一次
	 * TODO 使用线程池管理AmazonScanner
	 */
	@Scheduled(fixedRate = 12 * 60 * 60 * 1000)
	public void scanTask(){
		log.debug("start task");
		UserProduct up = new UserProduct();
		up.setDelFlag(UserProduct.DEL_FLAG_NORMAL);
		List<UserProduct> upList = userProductDao.queryList(up);
		Map<String, String> pidMap = new HashMap<String, String>();
		for (UserProduct userProduct : upList) {
			// 去重复 (upList数据量不超过10W条可以这么搞搞)
			if (pidMap.containsKey(userProduct.getProductId())) {
				continue;
			}
			pidMap.put(userProduct.getProductId(), null);
			new Thread(new AmazonScanner(userProduct,userDao.queryOne(new User(userProduct.getUserId())))).start();
		}
	}
}
