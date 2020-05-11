package com.bokesoft.ecomm.estore.mid.connection.struc;

import java.util.List;
import java.util.Map;

public class PageResult {
	private int pageSize;
	private int pageNo;
	private List<Map<String,Object>> data;
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	public int getPageSize() {
		return pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public List<Map<String, Object>> getData() {
		return data;
	}
	
	
}
