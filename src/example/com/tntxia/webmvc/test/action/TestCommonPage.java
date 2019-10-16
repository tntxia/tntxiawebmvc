package com.tntxia.webmvc.test.action;

import java.util.HashMap;
import java.util.Map;

import com.tntxia.web.mvc.WebRuntime;

public class TestCommonPage {
	
	public Map<String,Object> execute(WebRuntime runtime){
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("goodItem", "it's good");
		return res;
	}

}
