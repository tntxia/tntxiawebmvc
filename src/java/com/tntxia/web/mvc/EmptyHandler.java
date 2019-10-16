package com.tntxia.web.mvc;

import java.util.Map;

public class EmptyHandler extends MVCPageHandler {

	@Override
	public Map<String, Object> execute(WebRuntime runtime) throws Exception {
		
		return null;
	}

}
