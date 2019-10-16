package com.tntxia.web.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tntxia.web.mvc.view.FTLView;

public abstract class MVCAction extends BaseAction {
	
	private Map<String, Object> root = new HashMap<String, Object>();
	
	private String toPage;

	public Map<String, Object> getRoot() {
		return root;
	}

	public void setRoot(Map<String, Object> root) {
		this.root = root;
	}

	public String getToPage() {
		return toPage;
	}

	public void setToPage(String toPage) {
		this.toPage = toPage;
	}
	
	public abstract void init(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	public void putAll(Map<String,Object> map){
		if(map==null){
			return;
		}
		this.root.putAll(map);
	}
	
	public void put(String key,Object value){
		this.root.put(key, value);
	}
	
	public FTLView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		this.put("basePath", request.getContextPath());
		
		init(request,response);
		FTLView view = new FTLView(toPage,root);
		return view;
	}

}
