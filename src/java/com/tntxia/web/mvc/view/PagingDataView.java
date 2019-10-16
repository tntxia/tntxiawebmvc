package com.tntxia.web.mvc.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tntxia.web.mvc.PageBean;
import com.tntxia.web.mvc.WebRuntime;

public class PagingDataView  implements ResultView{
	
	private PageBean pageBean;
	
	private List<Object> list;
	
	private List<Object> rows;
	
	public PagingDataView() {
		
	}
	
	public PagingDataView(WebRuntime runtime) {
		this.setPageBean(runtime.getPageBean());
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<Object> getRows() {
		if(this.rows!=null) {
			return rows;
		}
		
		List<Object> res = new ArrayList<Object>();
		int page = this.pageBean.getPage();
		int pageSize = this.getPageBean().getPageSize();
		for(int i=(page-1)*pageSize;i<page*pageSize&&i<list.size();i++){
			res.add(list.get(i));
		}
		
		return res;
	}
	
	public void setList(List<Object> list) {
		this.list = list;
		this.rows = null;
	}

	public void setRows(List<Object> rows) {
		this.rows = rows;
	}
	
	public void setTotalAmount(int totalAmount) {
		pageBean.setTotalAmount(totalAmount);
	}

	@Override
	public void resolve(HttpServletRequest request, HttpServletResponse response, String charset) throws IOException {

		response.setCharacterEncoding("utf-8");
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("page", pageBean.getPage());
		result.put("pageSize", pageBean.getPageSize());
		result.put("totalPage", pageBean.getTotalPage());
		result.put("totalAmount", pageBean.getTotalAmount());
		result.put("rows", this.getRows());
		
		response.setContentType("application/json");
		response.getWriter().print(
				JSON.toJSONString(result,
						SerializerFeature.WriteDateUseDateFormat));
		
	}
	
	

}
