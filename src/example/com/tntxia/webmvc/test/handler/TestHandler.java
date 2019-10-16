package com.tntxia.webmvc.test.handler;

import java.util.Map;

import com.tntxia.web.mvc.MVCPageHandler;
import com.tntxia.web.mvc.WebRuntime;

public class TestHandler extends MVCPageHandler {

	@Override
	public Map<String, Object> execute(WebRuntime runtime) throws Exception {
		Map<String,Object> initParam = runtime.getInitParamMap();
		return initParam;
	}

}
