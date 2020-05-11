package com.bokesoft.ecomm.estore.mid.connection.dbmanager;

import java.sql.Connection;

import com.bokesoft.ecomm.estore.mid.connection.struc.DBType;
import com.bokesoft.ecomm.estore.mid.connection.struc.PageResult;
import com.bokesoft.ecomm.estore.mid.connection.struc.PrepareSQL;

public class MysqlDBManager extends GeneralDBManager {

	public MysqlDBManager(Connection connection) {
		super(connection);
	}

	@Override
	public String keyWordEscape(String key) {
		return "`" + key + "`";
	}

	@Override
	public int getDBType() {
		return DBType.MYSQL;
	}

	@Override
	public String getTableExistCheckSql() {
		return "select count(TABLE_NAME)count from information_schema.`TABLES` WHERE UPPER(TABLE_NAME) = ?  and TABLE_SCHEMA = ?";
	}

	@Override
	public String getIndexSearchSql() {
		return "select TABLE_NAME from information_schema.STATISTICS where UPPER(INDEX_NAME)=?   and TABLE_SCHEMA = ?";
	}

	@Override
	public String getColumnCheckSql() {
		return "select COLUMN_NAME from information_schema.COLUMNS where UPPER(TABLE_NAME)=?   and TABLE_SCHEMA = ?";
	}

	@Override
	public String getIndexCheckSql() {
		return "select INDEX_NAME from information_schema.STATISTICS where UPPER(TABLE_NAME)=?   and TABLE_SCHEMA = ?";
	}

	@Override
	public PrepareSQL getLimitString(String query, String orderBy, boolean hasOffset, int startRow, int endRow) {

		boolean bool = (orderBy != null && orderBy.length() != 0) ? true : false;
		StringBuilder stringBuilder;
		(stringBuilder = new StringBuilder(query.length() + 50)).append(query);
		if (bool) {
			stringBuilder.append(" order by ");
			stringBuilder.append(orderBy);
		}
		stringBuilder.append(hasOffset ? " limit ?, ?" : " limit ?");
		PrepareSQL prepareSQL;
		(prepareSQL = new PrepareSQL()).setSQL(stringBuilder.toString());
		if (hasOffset) {
			prepareSQL.addValue(Integer.valueOf(startRow));
			prepareSQL.addValue(Integer.valueOf(endRow - startRow));
		} else {
			prepareSQL.addValue(Integer.valueOf(endRow));
		}
		return prepareSQL;
	}
}
