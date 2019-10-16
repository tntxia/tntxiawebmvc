package com.tntxia.webmvc.test.json;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.tntxia.dbmanager.DBManager;
import com.tntxia.dbmanager.datasource.FilepathDataSource;

public class JSONTest {
	
	@Test
	public void testJSON() throws Exception{
		
		SerializeConfig mapping = new SerializeConfig(); 
		mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
    	mapping.put(java.util.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		
		DBManager db = new DBManager(new FilepathDataSource("c:\\jdbc.properties"));
		List list = db.queryForList("select * from in_warehouse", true);
		System.out.println(JSON.toJSONString(list, SerializerFeature.WriteDateUseDateFormat));
	}

}
