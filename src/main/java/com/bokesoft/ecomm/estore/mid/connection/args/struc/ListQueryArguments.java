package com.bokesoft.ecomm.estore.mid.connection.args.struc;

import java.util.List;

public class ListQueryArguments implements QueryArguments {
	private List<Integer> types = null;

	private List<Object> arguments = null;

	public ListQueryArguments(List<Integer> paramList, List<Object> paramList1) {
		this.types = paramList;
		this.arguments = paramList1;
	}

	public int size() {
		return (this.arguments != null) ? this.arguments.size() : 0;
	}

	public Object get(int paramInt) {
		return this.arguments.get(paramInt);
	}

	public boolean supportType() {
		return (this.types != null);
	}

	public int getType(int paramInt) {
		Integer integer;
		return ((integer = (Integer) ((this.types == null) ? null : this.types.get(paramInt))) != null)
				? integer.intValue()
				: -1;
	}
}