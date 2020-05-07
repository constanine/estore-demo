package com.bokesoft.ecomm.estore.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.bokesoft.ecomm.estore.StartApplication;

public class DBManagerTool {
	private static ThreadLocal<Connection> CONN_CACHE = new ThreadLocal<Connection>();

	public Connection getDBConnection() throws SQLException {	
		if(null == CONN_CACHE.get() && CONN_CACHE.get().isClosed()) {
			DataSource dataSource = StartApplication.getBean(DataSource.class);
			CONN_CACHE.set(dataSource.getConnection());
			CONN_CACHE.get().setAutoCommit(false);
		}		
		return CONN_CACHE.get();
	}
	
	public void commit() throws SQLException {
		if(null != CONN_CACHE.get() && !CONN_CACHE.get().isClosed()) {			
			CONN_CACHE.get().commit();
		}
	}
	
	public void rollback() throws SQLException {
		if(null != CONN_CACHE.get() && !CONN_CACHE.get().isClosed()) {			
			CONN_CACHE.get().rollback();
		}
	}
	
	public void close() throws SQLException {
		if(null != CONN_CACHE.get() && !CONN_CACHE.get().isClosed()) {			
			CONN_CACHE.get().close();
		}
	}
}
