package com.bokesoft.ecomm.estore.mid.util;

import java.sql.SQLException;

import com.bokesoft.ecomm.estore.mid.connection.DBManagerFactory;

public class EstoreContextBuilder {
	private static final ThreadLocal<EstoreContext> CACHE_CONTEXT = new ThreadLocal<EstoreContext>();

	public static EstoreContext get() throws SQLException {
		if(null == CACHE_CONTEXT.get()) {
			CACHE_CONTEXT.set(create());
		}
		return CACHE_CONTEXT.get();
	}

	public static EstoreContext create() throws SQLException {
		return new EstoreContext(DBManagerFactory.getDBManager());
	}
	
	public static void clear() throws SQLException {
		if(null != CACHE_CONTEXT.get()) {
			CACHE_CONTEXT.get().getDBManager().rollback();
			CACHE_CONTEXT.get().getDBManager().close();
		}
		CACHE_CONTEXT.remove();
	}
}
