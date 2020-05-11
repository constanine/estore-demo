package com.bokesoft.ecomm.estore.mid.connection.args.struc;

public interface QueryArguments {
	  boolean supportType();
	  
	  int size();
	  
	  int getType(int paramInt);
	  
	  Object get(int paramInt);
}
