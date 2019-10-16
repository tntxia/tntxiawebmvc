package com.tntxia.webmvc.test.action;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class TestAction2 {

	@Test
	public void test() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Object obj = B.class.newInstance();
		Method method = null;
		
		try{
			method = obj.getClass().getMethod("execute");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		method.invoke(obj, "test1");
	}

}
