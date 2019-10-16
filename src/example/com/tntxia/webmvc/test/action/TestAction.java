package com.tntxia.webmvc.test.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.WebRuntime;
import com.tntxia.web.mvc.entity.UserAgent;
import com.tntxia.web.mvc.view.FileView;
import com.tntxia.web.mvc.view.ImageView;
import com.tntxia.web.mvc.view.PagingDataView;

public class TestAction extends BaseAction{
	
	private DBManager dbManager = this.getDBManager();
	
	private DBManager oaDBManager = this.getDBManager("oa");
	
	public Map<String, Object> execute(WebRuntime runtime, List list) throws Exception{
		
		String ipHeader = "WL-Proxy-Client-IP";
		
		Map<String,Object> res = new HashMap<String,Object>();
		
		return res;
	}
	
	public PagingDataView getPagingData(WebRuntime runtime) {
		PagingDataView view = new PagingDataView(runtime);
		view.setTotalAmount(100);
		List<Object> list = new ArrayList<Object>();
		for(int i=0;i<99;i++) {
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", i);
			item.put("name", "item"+i);
			list.add(item);
		}
		view.setList(list);
		return view;
		
	}
	
	public FileView download(WebRuntime runtime) {
		FileView fileView = new FileView();
		fileView.setName("测试的文件");
		fileView.setFilePath("D:\\oa_upload\\0e4ba124fb544ef7ba147958c668e518.gif");
		return fileView;
	}
	
	public ImageView showImage(WebRuntime runtime) {
		ImageView view = new ImageView();
		view.setName("测试的文件");
		view.setFilePath("D:\\oa_upload\\7de3af4675c845cb920a8bb72bf310ad.jpg");
		return view;
	}
	
	public String testHeader(WebRuntime runtime) {
		return runtime.getHeader("User-Agent");
	}
	
	public UserAgent testAgent(WebRuntime runtime) {
		return runtime.getUserAgent();
	}

}
