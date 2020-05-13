package com.bokesoft.ecomm.estore.mid.connection.struc;

public class DBType {
	public static final int MYSQL = 10;
	public static final int ORACLE = 20;
	public static final int MSSQL = 30;
	public static final String MYSQL_STR = "MYSQL";
	public static final String MSSQL_STR = "SQLSERVER";
	public static final String ORACLE_STR = "ORACLE";
	public static int parseCode(String dbType) {
		switch (dbType.toUpperCase()) {
		case MYSQL_STR:
			return MYSQL;
		case MSSQL_STR:
			return MSSQL;
		case ORACLE_STR:
			return ORACLE;
		default:
			throw new RuntimeException("数据库类型不匹配");
		}
	}
}
