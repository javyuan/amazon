package com.javyuan.amazon.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GreetingController {
	
	private Log log = LogFactory.getLog(GreetingController.class);
	
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(Model m) {
        return "greeting";
    }
}
