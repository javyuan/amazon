package com.javyuan.amazon.service.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.javyuan.amazon.model.bean.PriceHistory;
import com.javyuan.amazon.model.common.BusinessException;
import com.javyuan.amazon.model.dao.PriceHistoryDao;
import com.javyuan.amazon.service.utils.ParseUtils;

/**
 * @author javyuan
 * 2016年7月25日
 */
public class AmazonScanner implements Runnable {

	private static final Log log = LogFactory.getLog(AmazonScanner.class);
	
	private static final String AMAZON_URL = "https://www.amazon.com/gp/aw/d/";
//	private static final String AMAZON_URL = "https://www.amazon.com/View-Master-Virtual-Reality-Starter-Pack/dp/";
	private static final String AMAZON_PARAM = "?ie=UTF8&*Version*=1&*entries*=0";
	private String productId;
	
	PriceHistoryDao priceHistoryDao;
	
	public AmazonScanner(String productId, PriceHistoryDao priceHistoryDao) {
		this.productId = productId;
		this.priceHistoryDao = priceHistoryDao;
	}
	
	@Override
	public void run() {
		log.debug("AmazonScanner start run");
		// 开始扫描
		Map<String,Object> map = scan(productId);
		if (map == null) {
			return;
		}
		// 保存结果
		PriceHistory bean = new PriceHistory();
		bean.setCreateDate(new Date());
		bean.setProductId(productId);
		bean.setPrice((BigDecimal)map.get("price"));
		bean.setShipFee((BigDecimal)map.get("shipFee"));
		bean.setGlobalShip((String)map.get("globalShip"));
		priceHistoryDao.insert(bean);
	}
	
	public static Map<String, Object> scan(String productId){
		if (StringUtils.isEmpty(productId)) {
			log.error("productId is Empty");
			return null;
		}
		try {
			// 请求Amazon.com
			URL url = new URL(AMAZON_URL + productId + AMAZON_PARAM);
			log.info(url);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			connection.setRequestProperty("Accept-Encoding", "gzip");
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.setRequestProperty("Cache-Control", "max-age=0");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Cookie", "s_vnum=1879376024569%26vn%3D1; s_nr=1447377451185-New; s_dslv=1447377451187; session-token=\"YJ6YLQpJKnNbAlhuXCJ56A6DdM9mY/zOPkZ+I0oe0HztMM6IlU76Sml6t1OqOKBCDLNeZl8K5uGzWHPYTVuSViWu04IKTyaH6AGjG+T/o0MTbXywJGnjT5qBxTcZrOBABxHtuXxeHYFcHuG54cKXyZ9TaJvK5soFAC73mDgun3pROoiXQzg+Fy697hReD30m+Hvrhp7a5gdxKsp2WvbJuZivp8bCkUtJY5of1oLN4Oo+mNwfRqLx/EwNkAmnV9nbbk5XPa71TBFUcGHON9IPmA==\"; x-main=Lu0Xvik6Q25A7ulaKkIVpjKhftemjH7opaZf6NgykHONuKBRG0B0ekgfqc1YDFWt; at-main=\"5|seqqoaG1z6Q+i0dGBSUFYXMvXt2+wItLDaiRxV7a4as/REoH373QDVOxvDPYDRGg1rqZAjYRvTFQE+bq1yTJrOVSu/rq7FOV4NDBKaBYGjA0AvFHeVCxc1ox8wguvBCrjSBf8KywFfwNVgKprq2m5gCbxcvwhMi1V5UStk0KBRy10TbfTf2P3ZH6T+Cjokl+bVZRtyty+GDKnuHar452ILBLxy6/4au6yeFsSwuIfvo75v5wL556CkEQf5HACE/u930FnD+cbtNvW6UlpcFGGLMxsBPiq5S5YhB2IiA9XQA=\"; x-wl-uid=17JmxGixIxLMsMW7zDu8lz7ugeEbzvXuj+iuKe0sUlZQLtKGVKiSf0xHxkAyaqnsUZ2URcujJa0GlJ0+hrgUUWk5roxxKt7cLI8Z7gbe8tmKAQKJO+WIiB2hzC8tKK338iOVDeu18f+g=; ubid-main=191-8467550-9526103; session-id-time=2082787201l; session-id=184-8511036-4523332; csm-hit=41JTAPC99YF7MTES88RM+s-41JTAPC99YF7MTES88RM|1469439833701");
			connection.setRequestProperty("Host", "www.amazon.com");
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
			connection.connect();
			// 打印Http请求结果
			log.info(connection.getHeaderField(null));
			// 接收HTML
			String html = readHTML(connection.getInputStream());
			if (StringUtils.isEmpty(html)) {
				throw new BusinessException("html is empty");
			}
			// 解析HTML（substring出自己需要的那部分HTML）
			return parseHTML(html.substring(html.indexOf("<span id=\"productTitle\" class=\"a-size-large\""), html.indexOf("<div id=\"hqpWrapper\" class=\"centerColAlign\">")));
		} catch (BusinessException e) {
			return null;
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Map<String, Object> parseHTML(String html) throws BusinessException{
		// 商品not available的时候停止解析
		if (ParseUtils.isNotAvailable(html)) {
			throw new BusinessException("product is not available now");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", ParseUtils.parseName(html));
		map.put("price", ParseUtils.parsePrice(html));
		map.put("shipFee", ParseUtils.parseShipFee(html));
		map.put("globalShip", ParseUtils.parseGlobalShip(html));
		return map;
	}

	private static String readHTML(InputStream inputStream) {
		try {
			// 请求头中设置了Accept-Encoding gzip，需要使用GZIPInputStream进行解压
			BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream),"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
