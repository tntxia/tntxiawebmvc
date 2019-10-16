package com.tntxia.web.mvc;

public class PageBean {
	
	private int page = 1;
	
	private int pageSize = 50;
	
	private int totalAmount;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTop(){
		return page * pageSize;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public int getTotalPage(){
		return totalAmount%pageSize==0?totalAmount/pageSize:totalAmount/pageSize+1;
	}

}
