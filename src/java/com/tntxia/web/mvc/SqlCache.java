package com.tntxia.web.mvc;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SqlCache {
	
	private static Map<String,String> map = new HashMap<String,String>();
	
	public static void put(String key,String sql){
		map.put(key, sql);
	}
	
	public static String get(String key) throws IOException, TemplateException{
		return get(key,null);
	}
	
	public static String get(String key,Map<String,Object> paramMap) throws IOException, TemplateException{
		
		if(paramMap!=null){
			List<String> emptyKeys = new ArrayList<String>();
			for(Map.Entry<String, Object> entry : paramMap.entrySet()){
				if(entry.getValue() instanceof String){
					if(StringUtils.isEmpty((String)entry.getValue())){
						emptyKeys.add(entry.getKey());
					}
				}
			}
			for(String ek : emptyKeys){
				paramMap.remove(ek);
			}
		}
		
		String sql = map.get(key);
		if(sql==null){
			return null;
		}
        Template t = new Template("name", new StringReader(sql),
                    new Configuration());
        StringWriter writer = new StringWriter();      
        t.process(paramMap, writer);      
        String res = writer.toString();   
		
		return res;
	}

}
