package com.tntxia.webmvc.example.handler;

import java.util.HashMap;
import java.util.Map;

import com.tntxia.web.mvc.MVCPageHandler;
import com.tntxia.web.mvc.WebRuntime;

public class TestRedirectHandler extends MVCPageHandler {

	@Override
	public Map<String, Object> execute(WebRuntime runtime) throws Exception {
		Map<String,Object> res = new HashMap<String,Object>();
		String redirect = runtime.getParam("redirect");
		res.put("forward", redirect);
		return res;
	}

}
