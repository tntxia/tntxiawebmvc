package com.tntxia.webmvc.test.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.SqlCache;
import com.tntxia.web.mvc.view.FTLView;

public class HelloAction extends BaseAction{
	
	private DBManager dbManager = this.getDBManager("default");
	
	public FTLView hello(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, Object> res = new HashMap<String, Object>();
		
		String ip = request.getRemoteAddr();
		
		
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("top", 10);
		SqlCache.get("saleAssay", paramMap);
		res.put("rows", dbManager.queryForList("select * from menu", true));
		res.put("hello", "hello");
		FTLView view = new FTLView("hello.ftl",res);
		return view;
	}

}
