package com.tntxia.web.mvc.xml.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dom4j.Element;

import com.tntxia.web.mvc.Action;
import com.tntxia.web.mvc.WebRuntime;

public abstract class CommonView extends Action {

	private Element el;

	private String handler;

	public Element getEl() {
		return el;
	}

	public void setEl(Element el) {
		this.el = el;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	// 通过类名，新增一个对象
	public Object createObject(String clazz) throws ClassNotFoundException {

		if (clazz == null) {
			return null;
		}
		Object obj = null;
		try {
			obj = Class.forName(clazz).newInstance();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public Object executeMethod(Object obj, String methodName,
			WebRuntime runtime) {

		if (obj == null) {
			return null;
		}

		Object res = null;
		try {
			Method method = obj.getClass().getMethod(methodName,
					WebRuntime.class);
			res = method.invoke(obj, runtime);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return res;

	}

}
