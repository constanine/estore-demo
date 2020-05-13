package com.bokesoft.ecomm.estore.mid.connection;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.bokesoft.ecomm.estore.StartApplication;
import com.bokesoft.ecomm.estore.config.DataSourceConfig;
import com.bokesoft.ecomm.estore.mid.connection.dbmanager.MysqlDBManager;
import com.bokesoft.ecomm.estore.mid.connection.dbmanager.OracleDBManager;
import com.bokesoft.ecomm.estore.mid.connection.dbmanager.SqlserverDBManager;
import com.bokesoft.ecomm.estore.mid.connection.struc.DBType;
import com.bokesoft.ecomm.estore.mid.intf.IDBManager;

public class DBManagerFactory {
	private static DataSource _getDataSource() {
		DataSource dataSource = StartApplication.getBean(DataSource.class);
		return dataSource;
	}
	
	private static int _getDBType() {
		return DBType.parseCode(DataSourceConfig.getDBType());
	}

	public static IDBManager getDBManager() throws SQLException {
		IDBManager result;
		int dbType = _getDBType();
		switch (dbType) {
		case DBType.MSSQL:
			result = new SqlserverDBManager(_getDataSource().getConnection());
			break;
		case DBType.MYSQL:
			result = new MysqlDBManager(_getDataSource().getConnection());
			break;
		case DBType.ORACLE:
			result = new OracleDBManager(_getDataSource().getConnection());
			break;
		default:
			throw new RuntimeException("dbtype no matched!,please use correct dbtype");
		}
		return result;
	}
}
