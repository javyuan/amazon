package com.javyuan.amazon.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javyuan.amazon.service.ApplicationService;

@Controller
public class GreetingController {
	
	private Log log = LogFactory.getLog(GreetingController.class);
	
	@Autowired
	ApplicationService service;
	
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(Model m) {
        return "greeting";
    }
}
