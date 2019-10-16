package com.tntxia.web.util;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.jdbc.PageBean;

public class DBManagerUtil {
	
	public static PageBean toDBPageBean(com.tntxia.web.mvc.PageBean pageBean){
		
		PageBean res = new PageBean();
		res.setPage(pageBean.getPage());
		res.setPageSize(pageBean.getPageSize());
		return res;
		
	}
	
	public static DBManager getDBManager(){
		return new DBManager(DatasourceStore.getDatasource("default"));
	}

}
