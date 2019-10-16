package com.tntxia.web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.tntxia.dbmanager.datasource.DefaultDataSource;
import com.tntxia.xml.util.Dom4jUtil;

public class DatasourceStore {
	
	private static boolean isInit = false;
	
	private static Map<String,DataSource> store = new HashMap<String,DataSource>();
	
	/**
	 * 
	 * @param xmlPath
	 * @throws DocumentException
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("rawtypes")
	public static void init(String xmlPath) throws DocumentException, ClassNotFoundException{
		
		System.out.println("System is going to init");
		
		if(isInit){
			return;
		}
		
		Document doc = Dom4jUtil.getDoc(xmlPath);
		List list = doc.selectNodes("/db/datasource");
		for(int i=0;i<list.size();i++){
			Element node = (Element) list.get(i);
			
			String name = node.attributeValue("name");
			System.out.println("datasource name :"+name);
			String jndi = Dom4jUtil.getText(node, "jndi");
			if(StringUtils.isNotEmpty(jndi)){
			    try
			    {
			        // 初始化查找命名空间
			        Context ctx = new InitialContext();
			        // 找到DataSource
			        DataSource ds = (DataSource)ctx.lookup(jndi);
			        store.put(name, ds);
			    }
			    catch(Exception e)
			    {
			        System.out.println(e);
			    }
			}else{
				DefaultDataSource defaultdatasource = new DefaultDataSource();
				String url = Dom4jUtil.getText(node, "url");
				String user = Dom4jUtil.getText(node, "user");
				String password = Dom4jUtil.getText(node, "password");
				String driver = Dom4jUtil.getText(node, "driver");
				defaultdatasource.setDriverClassName(driver);
				defaultdatasource.setUsername(user);
				defaultdatasource.setPassword(password);
				defaultdatasource.setUrl(url);
				store.put(name, defaultdatasource);
			}
		}
		isInit = true;
	}
	
	public static DataSource getDatasource(String name){
		return store.get(name);
	}

}
