package com.tntxia.web.mvc;

import java.util.HashMap;
import java.util.Map;

public abstract class RedirectViewHandler extends MVCPageHandler {

	protected abstract String init(WebRuntime runtime) throws Exception;
	
	@Override
	public Map<String, Object> execute(WebRuntime runtime) throws Exception {
		Map<String,Object> res = new HashMap<String,Object>();
		String forward = this.init(runtime);
		res.put("forward", forward);
		return res;
	}

}
