package com.bokesoft.ecomm.estore.mid.connection;

import javax.sql.DataSource;

import com.bokesoft.ecomm.estore.StartApplication;
import com.bokesoft.ecomm.estore.mid.connection.dbmanager.MysqlDBManager;
import com.bokesoft.ecomm.estore.mid.connection.dbmanager.OracleDBManager;
import com.bokesoft.ecomm.estore.mid.connection.dbmanager.SqlserverDBManager;
import com.bokesoft.ecomm.estore.mid.connection.struc.DBType;
import com.bokesoft.ecomm.estore.mid.intf.IDBManager;

public class DBManagerFactory {
	public static DataSource getDataSource() {
		DataSource dataSource = StartApplication.getBean(DataSource.class);
		return dataSource;
	}
	public IDBManager getDBManager(int dbType) throws Throwable {
		IDBManager result;
		switch (dbType) {
		case DBType.MSSQL:
			result = new SqlserverDBManager(getDataSource().getConnection());
			break;
		case DBType.MYSQL:
			result = new MysqlDBManager(getDataSource().getConnection());
			break;
		case DBType.ORACLE:
			result = new OracleDBManager(getDataSource().getConnection());
			break;
		default:
			throw new RuntimeException("dbtype no matched!,please use correct dbtype");
		}
		return result;
	}
}
