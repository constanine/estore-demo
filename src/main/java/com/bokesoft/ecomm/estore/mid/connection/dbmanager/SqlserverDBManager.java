package com.bokesoft.ecomm.estore.mid.connection.dbmanager;

import java.sql.Connection;
import java.sql.SQLException;

import com.bokesoft.ecomm.estore.mid.connection.struc.DBType;
import com.bokesoft.ecomm.estore.mid.connection.struc.PrepareSQL;

public class SqlserverDBManager extends GeneralDBManager {

	public SqlserverDBManager(Connection connection) throws SQLException {
		super(connection);
	}

	@Override
	public String keyWordEscape(String key) {
		return "[" + key + "]";
	}

	@Override
	public int getDBType() {
		return DBType.MSSQL;
	}

	@Override
	public String getTableExistCheckSql() {
		return "select count(name) count from sys.tables where name=? and type='u'";
	}

	@Override
	public String getColumnCheckSql() {
		return "select name from syscolumns where id=(select id from sysobjects where xtype='U' and name=?)";
	}

	@Override
	public String getIndexCheckSql() {
		return "select name from sysindexes where id=(select id from sysobjects where xtype='U' and name=?)";
	}

	@Override
	public String getIndexSearchSql() {
		return "select name from sysobjects where id=(select id from sysindexes where name=?)";
	}

	@Override
	public PrepareSQL getLimitString(String query, String orderBy, boolean hasOffset, int startRow, int endRow) {
		StringBuilder stringBuilder = new StringBuilder(query.length() + 100);
		boolean bool = (orderBy != null && orderBy.length() != 0) ? true : false;
		PrepareSQL prepareSQL = new PrepareSQL();
		if (hasOffset) {
			stringBuilder.append("select * from ( select tb1_.* , ROW_NUMBER() over( ");
			stringBuilder.append("order by ");
			stringBuilder.append(bool ? orderBy : "(select 1)");
			stringBuilder.append(") as rownumber_ from (");
			stringBuilder.append(query);
			stringBuilder.append(" ) tb1_ ) tb2_ where rownumber_ > ? and rownumber_ <= ? ");
			prepareSQL.addValue(Integer.valueOf(startRow));
			prepareSQL.addValue(Integer.valueOf(endRow));
		} else {
			stringBuilder.append("select * from ( select tb1_.* , ROW_NUMBER() over( ");
			stringBuilder.append("order by ");
			stringBuilder.append(bool ? orderBy : "(select 1)");
			stringBuilder.append(") as rownumber_ from (");
			stringBuilder.append(query);
			stringBuilder.append(" ) tb1_ ) tb2_ where rownumber_ <= ? ");
			prepareSQL.addValue(Integer.valueOf(endRow));
		}
		prepareSQL.setSQL(stringBuilder.toString());
		return prepareSQL;
	}
}
