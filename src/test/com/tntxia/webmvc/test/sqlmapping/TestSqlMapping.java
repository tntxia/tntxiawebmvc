package com.tntxia.webmvc.test.sqlmapping;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.tntxia.web.mvc.SqlCache;

import freemarker.template.TemplateException;

public class TestSqlMapping {

	@Test
	public void test() throws IOException, TemplateException {
		String sql = "select * from user";
		SqlCache.put("test", sql);
		assertEquals(sql, SqlCache.get("test", null));
	}
	
	@Test
	public void test1() throws IOException, TemplateException {
		String sql = "select * from user <#if test>hello</#if>";
		SqlCache.put("test", sql);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("test", true);
		String sql2 = SqlCache.get("test", paramMap);
		System.out.println(sql2);
		assertNotSame(sql, sql2);
		
	}

}
