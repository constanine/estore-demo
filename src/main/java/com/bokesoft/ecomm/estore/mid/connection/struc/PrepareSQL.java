package com.bokesoft.ecomm.estore.mid.connection.struc;

import java.util.ArrayList;
import java.util.List;

public class PrepareSQL {
	private String sql;

	private List<Object> values = new ArrayList<Object>();

	public void setSQL(String s) {
		this.sql = s;
	}

	public String getSQL() {
		return sql;
	}

	public void addValue(int index, Object o) {
		values.add(index, o);
	}

	public void addValue(Object o) {
		values.add(o);
	}

	public void addAllValue(List<Object> list) {
		if (list != null && list.size() > 0) {
			values.addAll(list);
		}
	}

	public List<Object> getPrepareValues() {
		return values;
	}
}
