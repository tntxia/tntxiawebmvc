package com.tntxia.webmvc.test.dom4j;

import static org.junit.Assert.*;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.tntxia.xml.util.Dom4jUtil;


public class Test {

	@org.junit.Test
	public void test() throws DocumentException {
		
		Document doc = Dom4jUtil.getDoc("WebContent/WEB-INF/config/mvc.xml");
		
		List nodelist = doc.selectNodes("mvc/mvcList/mvcView");
		
		for(int i=0;i<nodelist.size();i++){
			Element el = (Element) nodelist.get(i);
			
			Element angularEle = (Element)el.selectSingleNode("angular");
			
			String appName = Dom4jUtil.getProp(angularEle, "appName");
			System.out.println(appName);
		}
		
	}

}
