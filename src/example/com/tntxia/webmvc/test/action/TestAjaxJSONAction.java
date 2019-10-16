package com.tntxia.webmvc.test.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tntxia.web.mvc.WebRuntime;
import com.tntxia.webmvc.test.form.UserForm;

public class TestAjaxJSONAction {
	
	public Map<String,Object> execute(UserForm user,WebRuntime runtime,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("id", user.getId());
		res.put("name", user.getName());
		return res;
		
	}

}
