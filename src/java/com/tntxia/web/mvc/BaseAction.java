package com.tntxia.web.mvc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.sqlexecutor.SQLExecutorSingleConn;
import com.tntxia.sqlexecutor.Transaction;
import com.tntxia.web.mvc.consts.Consts;
import com.tntxia.web.util.DatasourceStore;

public class BaseAction {
	
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
	
	public DBManager getDBManager(String name){
		return new BaseDao().getDBManager(name);
	}
	
	public DBManager getDBManager(){
		return getDBManager("default");
	}
	
	public Transaction getTransaction(String name) throws SQLException{
		return new BaseDao().getTransaction(name);
	}
	
	public Transaction getTransaction() throws SQLException{
		return getTransaction("default");
	}
	
	public SQLExecutorSingleConn getSQLExecutorSingleConn() throws SQLException{
		return this.getSQLExecutorSingleConn("default");
	}
	
	public SQLExecutorSingleConn getSQLExecutorSingleConn(String name) throws SQLException{
		DataSource dataSource = DatasourceStore.getDatasource(name);
		System.out.println(dataSource);
		return new SQLExecutorSingleConn(dataSource);
	}
	
	public PageBean getPageBean(HttpServletRequest request){
		return this.getPageBean(request,20);
	}
	
	public PageBean getPageBean(HttpServletRequest request,int defaultPageSize){
		PageBean pageBean = new PageBean();
		String pageStr = request.getParameter("page");
		String pageSizeStr = request.getParameter("pageSize");
		int pageSize = defaultPageSize;
		if(StringUtils.isNumericSpace(pageStr)){
			pageBean.setPage((Integer) Integer.valueOf(pageStr));
		}
		if(StringUtils.isNumericSpace(pageSizeStr)){
			pageSize = (Integer) Integer.valueOf(pageSizeStr);
		}
		pageBean.setPageSize(pageSize);
		return pageBean;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Map<String,Object> getPagingResult(List list,HttpServletRequest request,int totalAmount){
		PageBean pageBean = this.getPageBean(request);
		int page = pageBean.getPage();
		int pageSize = pageBean.getPageSize();
		List rows = new ArrayList();
		if((page-1)*pageSize<list.size()){
			int start = (page-1)*pageSize;
			for(int i = start;i<list.size();i++){
				rows.add(list.get(i));
			}
		}
		
		Map<String,Object> res = new HashMap<String,Object>();
		pageBean.setTotalAmount(totalAmount);
		res.put("rows", rows);
		res.put("page", pageBean.getPage());
		res.put("totalAmount", totalAmount);
		res.put("totalPage", pageBean.getTotalPage());
		res.put("pageSize", pageBean.getPageSize());
		return res;
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Map<String,Object> getPagingResult(List list,HttpServletRequest request,int totalAmount,int defaultPageSize){
		PageBean pageBean = this.getPageBean(request,defaultPageSize);
		int page = pageBean.getPage();
		int pageSize = pageBean.getPageSize();
		List rows = new ArrayList();
		if((page-1)*pageSize<list.size()){
			int start = (page-1)*pageSize;
			for(int i = start;i<list.size();i++){
				rows.add(list.get(i));
			}
		}
		
		Map<String,Object> res = new HashMap<String,Object>();
		pageBean.setTotalAmount(totalAmount);
		res.put("rows", rows);
		res.put("page", pageBean.getPage());
		res.put("totalAmount", totalAmount);
		res.put("pageSize", pageBean.getPageSize());
		res.put("totalPage", pageBean.getTotalPage());
		return res;
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Map<String,Object> getPagingResult(List list,PageBean pageBean,int totalAmount){
		
		int page = pageBean.getPage();
		int pageSize = pageBean.getPageSize();
		List rows = new ArrayList();
		if((page-1)*pageSize<list.size()){
			int start = (page-1)*pageSize;
			for(int i = start;i<start+pageSize && i<list.size();i++){
				rows.add(list.get(i));
			}
		}
		
		Map<String,Object> res = new HashMap<String,Object>();
		pageBean.setTotalAmount(totalAmount);
		res.put("rows", rows);
		res.put("page", pageBean.getPage());
		res.put("totalAmount", totalAmount);
		res.put("pageSize", pageBean.getPageSize());
		res.put("totalPage", pageBean.getTotalPage());
		return res;
		
	}
	
	@SuppressWarnings({ "rawtypes"})
	public Map<String,Object> getPagingResult(List rows,PageBean pageBean){
		
		
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("rows", rows);

		res.put("page", pageBean.getPage());
		res.put("totalAmount", pageBean.getTotalAmount());
		res.put("totalPage", pageBean.getTotalPage());
		res.put("pageSize", pageBean.getPageSize());
		res.put("totalPage", pageBean.getTotalPage());
		return res;
		
	}
	
	/**
	 * 
	 * @param list 所有的数据列表
	 * @param runtime Web运行对象
	 * @param totalAmount 总数据
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	public Map<String,Object> getPagingResult(List list,WebRuntime runtime,int totalAmount){
		PageBean pageBean = runtime.getPageBean();
		return this.getPagingResult(list, pageBean, totalAmount);
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
	
	
	
	
}
