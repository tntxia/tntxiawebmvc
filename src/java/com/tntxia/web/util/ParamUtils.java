package com.tntxia.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ParamUtils {
	
	public static String unescape(HttpServletRequest request,String paramName){
		String res = request.getParameter(paramName);
		if(res==null){
			return null;
		}
		return EscapeUnescape.unescape(res);
	}
	
	public static int getInt(HttpServletRequest request,String paramName){
		String res = request.getParameter(paramName);
		if(StringUtils.isEmpty(res)){
			return 0;
		}
		return Integer.parseInt(res);
		
	}
	
	public static String getString(HttpServletRequest request,String paramName){
		String res = request.getParameter(paramName);
		if(res!=null){
			res = res.trim();
		}
		return res;
		
		
	}
	
	public static boolean getBoolean(HttpServletRequest request,String paramName){
		String res = request.getParameter(paramName);
		if(StringUtils.isEmpty(res)){
			return false;
		}
		return Boolean.parseBoolean(res);
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String,Object> getParamMap(HttpServletRequest request){
		Map<String,Object> res = new HashMap<String,Object>();
		Map<String,String[]> paramMap = request.getParameterMap();
		for(Map.Entry entry : paramMap.entrySet() ){
			String[] value = (String[])entry.getValue();
			if(value.length==1){
				res.put((String) entry.getKey(), value[0]);
			}else{
				res.put((String) entry.getKey(), value);
			}
		}
		return res;
		
	}
	

}
