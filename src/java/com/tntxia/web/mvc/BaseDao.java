package com.tntxia.web.mvc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.sqlexecutor.Transaction;
import com.tntxia.web.mvc.entity.PagingResult;
import com.tntxia.web.util.DatasourceStore;

public class BaseDao {
	
	public DBManager getDBManager(String name){
		DBManager res = new DBManager(DatasourceStore.getDatasource(name));
		return res;
	}
	
	public DBManager getDBManager(){
		return getDBManager("default");
	}
	
	public Transaction getTransaction(String name) throws SQLException{
		DataSource dataSource = DatasourceStore.getDatasource(name);
		Connection conn = dataSource.getConnection();
		return Transaction.createTrans(conn);
	}
	
	public Transaction getTransaction() throws SQLException{
		return getTransaction("default");
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List getRows(List list,PageBean pageBean){
		
		List rows = new ArrayList();
		int page = pageBean.getPage();
		int pageSize = pageBean.getPageSize();
		for(int i=(page-1)*pageSize;i<page*pageSize&&i<list.size();i++){
			rows.add(list.get(i));
		}
		return rows;
	}
	
	/**
	 * 返回分页的结果
	 * @param list
	 * @param pageBean
	 * @return
	 */
	public PagingResult getPagingResult(List<Object> list, PageBean pageBean) {
		List<Object> rows = this.getRows(list, pageBean);
		PagingResult res = new PagingResult();
		res.setPage(pageBean.getPage());
		res.setPageSize(pageBean.getPageSize());
		res.setTotalAmount(pageBean.getTotalAmount());
		res.setTotalPage(pageBean.getTotalPage());
		res.setRows(rows);
		return res;
	}

}
