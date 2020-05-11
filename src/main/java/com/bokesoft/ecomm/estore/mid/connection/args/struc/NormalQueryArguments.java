package com.bokesoft.ecomm.estore.mid.connection.args.struc;

import java.util.ArrayList;

public class NormalQueryArguments implements QueryArguments {
	private Object[] arguments = null;

	public NormalQueryArguments(Object[] paramArrayOfObject) {
		this.arguments = paramArrayOfObject;
	}

	public NormalQueryArguments(ArrayList<Object> paramArrayList) {
		byte b = 0;
		this.arguments = new Object[paramArrayList.size()];
		for (Object object : paramArrayList)
			this.arguments[b++] = object;
	}

	public int size() {
		return this.arguments.length;
	}

	public Object get(int paramInt) {
		return this.arguments[paramInt];
	}

	public boolean supportType() {
		return false;
	}

	public int getType(int paramInt) {
		return -1;
	}
}