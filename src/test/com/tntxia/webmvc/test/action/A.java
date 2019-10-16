package com.tntxia.webmvc.test.action;

import java.util.Map;

public abstract class A {
	
	public abstract void test();
	
	public Map<String,Object> execute(String name){
		test();
		System.out.println(name);
		return null;
	}

}
