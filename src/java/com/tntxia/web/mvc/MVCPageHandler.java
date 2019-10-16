package com.tntxia.web.mvc;

import java.util.HashMap;
import java.util.Map;

public abstract class MVCPageHandler extends BaseAction{
	
	private Map<String,Object> root = new HashMap<String,Object>();

	public Map<String, Object> getRoot() {
		return root;
	}

	public void setRoot(Map<String, Object> root) {
		this.root = root;
	}
	
	public void setRootValue(String key,Object value){
		
		this.root.put(key, value);
	}
	
	public void putAllRootValue(Map<String,Object> map){
		this.root.putAll(map);
	}
	
	public abstract Map<String,Object> execute(WebRuntime runtime) throws Exception;

}
