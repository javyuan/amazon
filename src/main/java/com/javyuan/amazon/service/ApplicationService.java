package com.javyuan.amazon.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.javyuan.amazon.model.bean.PriceHistory;
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

@Service
public class ApplicationService {
	
	private static final Log log = LogFactory.getLog(ApplicationService.class);
	
	@Autowired
	UserProductDao userProductDao;
	@Autowired
	UserDao userDao;
	@Autowired
	PriceHistoryDao priceHistoryDao;
	@Autowired
	ThreadPoolTaskExecutor myExecutor;
	@Autowired
	EmailService emailService;

	/**
	 * 抓取amazon商品信息定时任务，每1个小时运行一次
	 */
	@Scheduled(fixedRate = 1 * 60 * 60 * 1000)
	public void scanAmazon(){
		UserProduct up = new UserProduct();
		up.setDelFlag(UserProduct.DEL_FLAG_NORMAL);
		List<UserProduct> upList = userProductDao.queryList(up);
		log.debug("total task :" + upList.size());
		Map<String, String> pidMap = new HashMap<String, String>();
		for (UserProduct userProduct : upList) {
			// 去重复 (upList数据量不超过10W条可以这么搞搞)
			if (pidMap.containsKey(userProduct.getProductId())) {
				continue;
			}
			pidMap.put(userProduct.getProductId(), null);
			myExecutor.execute(new AmazonScanner(userProduct,userDao.queryOne(new User(userProduct.getUserId()))));
		}
	}
	
	/**
	 * 发送每日报告定时任务，每天早晨9点执行
	 */
	@Scheduled(cron="0 0 9 * * MON-FRI")
	public void dailyReport(){
		List<User> userList = userDao.queryList(new User());
		for (User user : userList) {
			UserProduct bean = new UserProduct();
			bean.setUserId(user.getId());
			List<UserProduct> upList = userProductDao.queryList(bean);
			List<Map<String,Object>> reportList = new ArrayList<Map<String,Object>>();
			for (UserProduct up : upList) {
				PriceHistory ph = new PriceHistory();
				ph.setCreateDate(getDate());
				ph.setProductId(up.getProductId());
				List<PriceHistory> list = priceHistoryDao.queryList(ph);
				if (list.isEmpty()) {
					continue;
				}
				Map<String,Object> product = new HashMap<String,Object>();
				product.put("productId", up.getProductId());
				product.put("productName", list.get(0).getProductName());
				product.put("currentPrice", list.get(list.size()-1).getPrice());
				product.put("leastPrice", getLeastPrice(list));
				product.put("globalShip", list.get(list.size()-1).getGlobalShip());
				product.put("shipFee", list.get(list.size()-1).getShipFee());
				reportList.add(product);
			}
			try {
				Map<String,Object> model = new HashMap<String,Object>();
				model.put("productList", reportList);
				emailService.sendHtmlMail(user.getEmail(), "日报", "dailyReport", model);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 获取当日最低价格
	 */
	private BigDecimal getLeastPrice(List<PriceHistory> list) {
		BigDecimal leastPrice = new BigDecimal(0);
		for (PriceHistory priceHistory : list) {
			if (leastPrice.compareTo(priceHistory.getPrice()) < 0) {
				leastPrice = priceHistory.getPrice();
			}
		}
		return leastPrice;
	}

	/**
	 * 一天前的日期
	 */
	private Date getDate() {
		Date now = new Date();
		return new Date(now.getTime() - 24*60*60*1000/*一天前*/);
	}
}
