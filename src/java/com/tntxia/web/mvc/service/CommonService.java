package com.tntxia.web.mvc.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.sqlexecutor.SQLExecutorSingleConn;
import com.tntxia.sqlexecutor.Transaction;
import com.tntxia.web.mvc.PageBean;
import com.tntxia.web.mvc.consts.Consts;
import com.tntxia.web.util.DatasourceStore;

public class CommonService {
	
	public Map<String,Object> errorMsg(String msg){
		Map<String,Object> res = new HashMap<String,Object>();
		res.put(Consts.SUCCESS_KEY, false);
		res.put(Consts.MSG_KEY, msg);
		return res;
	}
	
	public Map<String,Object> success(){
		Map<String,Object> res = new HashMap<String,Object>();
		res.put(Consts.SUCCESS_KEY, true);
		return res;
	}
	
	public Map<String,Object> success(String key,Object obj){
		Map<String,Object> res = new HashMap<String,Object>();
		res.put(Consts.SUCCESS_KEY, true);
		if(key!=null){
			res.put(key, obj);
		}
		return res;
	}
	
	public DBManager getDBManager(){
		return this.getDBManager("default");
	}
	
	public DBManager getDBManager(String name){
		DBManager res = new DBManager(DatasourceStore.getDatasource(name));
		return res;
	}
	
	public Transaction getTransaction(String name) throws SQLException{
		DataSource dataSource = DatasourceStore.getDatasource(name);
		Connection conn = dataSource.getConnection();
		return Transaction.createTrans(conn);
	}
	
	public Transaction getTransaction() throws SQLException{
		return getTransaction("default");
	}
	
	public SQLExecutorSingleConn getSqlExecutorSingleConn() throws SQLException{
		return new SQLExecutorSingleConn(DatasourceStore.getDatasource("default"));
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List getRows(List list,PageBean pageBean){
		
		List rows = new ArrayList();
		int page = pageBean.getPage();
		int pageSize = pageBean.getPageSize();
		for(int i=(page-1)*pageSize;i<page*pageSize;i++){
			if(i<list.size()){
				rows.add(list.get(i));
			}
		}
		return rows;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Map<String,Object> getPagingResult(List list, PageBean pageBean, int totalAmount)
    {
        int page = pageBean.getPage();
        int pageSize = pageBean.getPageSize();
        List rows = new ArrayList();
        if((page - 1) * pageSize < list.size())
        {
            int start = (page - 1) * pageSize;
            for(int i = start; i < list.size(); i++)
                rows.add(list.get(i));

        }
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("rows", rows);
        res.put("page", Integer.valueOf(pageBean.getPage()));
        res.put("totalAmount", Integer.valueOf(totalAmount));
        res.put("totalPage", pageBean.getTotalPage());
        res.put("pageSize", Integer.valueOf(pageBean.getPageSize()));
        return res;
    }

}
