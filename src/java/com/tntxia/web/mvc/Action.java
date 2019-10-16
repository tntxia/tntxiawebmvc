package com.tntxia.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Action {
	
	private String name;
	
	private String className;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public abstract void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception ;
	
}
