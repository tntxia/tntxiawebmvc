package com.tntxia.web.login;

import java.util.Map;

import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.WebRuntime;


public class LogoutAction extends BaseAction {

	
	public Map<String,Object> execute(WebRuntime runtime) throws Exception {
		runtime.invalideSession();
		return this.success();
	}

}
