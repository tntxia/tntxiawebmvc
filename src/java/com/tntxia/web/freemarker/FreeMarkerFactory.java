package com.tntxia.web.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerFactory {
	
	private static boolean initFreeMarker;
	
	private static Configuration cfg = null;
	
	public static void init(HttpServletRequest request,String charset){
		
		ServletContext context = request.getServletContext();
		
		if(!initFreeMarker){
			// 创建一个FreeMarker实例
			cfg = new Configuration();
			// 指定FreeMarker模板文件的位置
			cfg.setServletContextForTemplateLoading(context, "/");
			cfg.setDefaultEncoding(charset);
			cfg.setClassicCompatible(true);
			cfg.setNumberFormat("0.####");
			initFreeMarker = true;
		}
	}
	
	public static String getTemplateResult(String url, Map<String, Object> root,
			String charset) {
		
		StringWriter out = new StringWriter();

		try {
			
			Template t = cfg.getTemplate(url);
			try {
				if (root == null) {
					root = new HashMap<String, Object>();
				}
				
				t.process(root, out); // 往模板里写数据
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		
		return out.toString();
	}

}
