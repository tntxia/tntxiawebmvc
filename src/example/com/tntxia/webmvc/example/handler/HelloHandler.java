package com.tntxia.webmvc.example.handler;

import java.util.Map;

import com.tntxia.web.mvc.MVCPageHandler;
import com.tntxia.web.mvc.WebRuntime;

public class HelloHandler extends MVCPageHandler {

	@Override
	public Map<String, Object> execute(WebRuntime runtime) throws Exception {
		this.setRootValue("hello", "Hello,World!!");
		return this.getRoot();
	}

}
