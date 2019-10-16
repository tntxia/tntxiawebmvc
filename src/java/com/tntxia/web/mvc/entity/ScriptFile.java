package com.tntxia.web.mvc.entity;

import java.util.HashMap;
import java.util.Map;

public class ScriptFile {
	
	private String version;
	
	private Map<String, String> attributes = new HashMap<String,String>();
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
