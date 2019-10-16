package com.tntxia.web.util;

import javax.servlet.http.HttpServletRequest;

public class EscapeUnescape {
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
	
	public static String unescape(HttpServletRequest request,String name){
		String src = request.getParameter(name);
		return unescape(src);
	}

	public static String unescape(String src) {
		
		if(src==null){
			return null;
		}
		
		try{
			
			StringBuffer tmp = new StringBuffer();
			tmp.ensureCapacity(src.length());
			int lastPos = 0, pos = 0;
			char ch;
			while (lastPos < src.length()) {
				pos = src.indexOf("%", lastPos);
				if (pos == lastPos) {
					if (src.charAt(pos + 1) == 'u') {
						ch = (char) Integer.parseInt(src
								.substring(pos + 2, pos + 6), 16);
						tmp.append(ch);
						lastPos = pos + 6;
					} else {
						ch = (char) Integer.parseInt(src
								.substring(pos + 1, pos + 3), 16);
						tmp.append(ch);
						lastPos = pos + 3;
					}
				} else {
					if (pos == -1) {
						tmp.append(src.substring(lastPos));
						lastPos = src.length();
					} else {
						tmp.append(src.substring(lastPos, pos));
						lastPos = pos;
					}
				}
			}
			return tmp.toString();
		}catch(Exception e){
			e.printStackTrace();
			return src;
		}
		
		
	}

	public static void main(String[] args) {
		String tmp = "%u73E0%u6D77%u5E02%u521B%u65B0%u56DB%u8DEF19%u53F7%u4ED3%u5E93201%u5BA4";
		
		System.out.println("testing unescape :" + tmp);
		System.out.println(unescape(tmp));
	}
}
