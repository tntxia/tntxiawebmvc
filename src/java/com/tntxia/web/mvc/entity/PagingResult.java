package com.tntxia.web.mvc.entity;

import java.util.List;

public class PagingResult {
	
	private int page;
	
	private int pageSize;
	
	private int totalPage;
	
	private int totalAmount;
	
	private List<Object> rows;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Object> getRows() {
		return rows;
	}

	public void setRows(List<Object> rows) {
		this.rows = rows;
	}

}
