package com.tntxia.web.mvc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.tntxia.web.freemarker.FreeMarkerFactory;

public class CommonDoAction extends Action {


	private String charset;

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	// 通过类名，新增一个对象
	private Object createObject(String clazz) throws ClassNotFoundException {

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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String basePath = request.getContextPath();

		String className = this.getClassName();

		String methodName = null;
		
		String uri = request.getRequestURI();
		
		String url = uri.substring(basePath.length() + 1);

		if (url != null && url.indexOf("!") != -1) {
			String[] actionSplit = url.split("\\.");
			if (actionSplit.length != 2) {
				throw new Exception("非法路径");
			}
			String[] actionNameSplit = actionSplit[0].split("!");
			url = actionNameSplit[0] + "." + actionSplit[1];
			methodName = actionNameSplit[1];

		}

		if (methodName == null) {
			if (request.getParameter("method") != null) {
				methodName = request.getParameter("method");
			} else {
				methodName = "execute";
			}
		}

		Object res = null;

		try {

			Class<?> clazz = Class.forName(className);

			Method[] methods = clazz.getMethods();

			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					
					Class<?> para[] = method.getParameterTypes();
					List params = new ArrayList();

					for (Class<?> p : para) {
						if (p.getName().equals("com.tntxia.web.mvc.WebRuntime")) {
							params.add(new WebRuntime(request, response));
						}else if(p.getName().equals("javax.servlet.http.HttpServletRequest")){
							params.add(request);
						}else if(p.getName().equals("javax.servlet.http.HttpServletResponse")){
							params.add(response);
						}else{
							
							BufferedReader br = new BufferedReader(new InputStreamReader(  
				                    (ServletInputStream) request.getInputStream(), "utf-8"));
				            StringBuffer sb = new StringBuffer("");  
				            String temp;
				            while ((temp = br.readLine()) != null) {  
				                sb.append(temp);  
				            }  
				            br.close();  
				            String acceptjson = sb.toString();  
				            Object obj = null;
				            try {
				            	obj = JSON.parseObject(acceptjson,p);
				            }catch(Exception ex) {
				            	System.out.println("参数："+ p + "转化失败");
				            }
				            
							params.add(obj);
						}
					}
					Object obj = createObject(className);
					res = method.invoke(obj, params.toArray());

				}
			}
			
			FreeMarkerFactory.init(request, charset);
			
			DoActionResultResolver resultResolver = new DoActionResultResolver(request,response,res,charset);
			resultResolver.resolve();

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setCharacterEncoding(charset);
			PrintWriter writer = response.getWriter();
			writer.write("类：" + className + "不存在！");
			writer.flush();
			writer.close();
			e.printStackTrace();
			return;

		}

	}

}
