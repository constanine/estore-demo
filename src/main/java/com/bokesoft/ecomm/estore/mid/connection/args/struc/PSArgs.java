package com.bokesoft.ecomm.estore.mid.connection.args.struc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PSArgs implements QueryArguments {
	private List<Integer> types = new ArrayList<>();

	private List<Object> arguments = new ArrayList<Object>();

	public void addArg(Integer paramInteger, Object paramObject) {
		this.types.add(paramInteger);
		this.arguments.add(paramObject);
	}

	public void addStringArg(String paramString) {
		this.types.add(Integer.valueOf(1002));
		this.arguments.add(paramString);
	}

	public void addIntArg(Integer paramInteger) {
		this.types.add(Integer.valueOf(1001));
		this.arguments.add(paramInteger);
	}

	public void addLongArg(Long paramLong) {
		this.types.add(Integer.valueOf(1010));
		this.arguments.add(paramLong);
	}

	public void addDateArg(Date paramDate) {
		this.types.add(Integer.valueOf(1003));
		this.arguments.add(paramDate);
	}

	public void addTimestampArg(Timestamp paramTimestamp) {
		this.types.add(Integer.valueOf(1101));
		this.arguments.add(paramTimestamp);
	}

	public boolean supportType() {
		return true;
	}

	public int size() {
		return this.arguments.size();
	}

	public int getType(int paramInt) {
		return ((Integer) this.types.get(paramInt)).intValue();
	}

	public Object get(int paramInt) {
		return this.arguments.get(paramInt);
	}
}
