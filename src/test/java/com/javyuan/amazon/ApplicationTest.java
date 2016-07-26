package com.javyuan.amazon;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.javyuan.amazon.model.bean.UserProduct;
import com.javyuan.amazon.model.dao.UserProductDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTest {
	
	@Autowired
	UserProductDao userProductDao; 
	
	@Test
	public void userProductDaoTest(){
		UserProduct up = new UserProduct();
		up.setId("1");
		List<UserProduct> list = userProductDao.queryList(up);
		Assert.notEmpty(list, "");
	}
	
}
