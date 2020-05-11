package com.bokesoft.ecomm.estore.mid.connection.dbmanager;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import com.bokesoft.ecomm.estore.mid.connection.struc.DBType;
import com.bokesoft.ecomm.estore.mid.connection.struc.PrepareSQL;

public class OracleDBManager extends GeneralDBManager {
	private static final String[] keyWord = new String[] { "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AT", "BEGIN",
			"BETWEEN", "BY", "CASE", "CHECK", "CLUSTERS", "CLUSTER", "COLAUTH", "COLUMNS", "COMPRESS", "CONNECT",
			"CRASH", "CREATE", "CURSOR", "DATE", "DECLARE", "DEFAULT", "DESC", "DISTINCT", "DROP", "ELSE", "END",
			"EXCEPTION", "EXCLUSIVE", "FETCH", "FOR", "FROM", "FUNCTION", "GOTO", "GRANT", "GROUP", "HAVING",
			"IDENTIFIED", "IF", "IN", "INDEX", "INDEXES", "INSERT", "INTERSECT", "INTO", "IS", "LIKE", "LOCK", "LEVEL",
			"MINUS", "MODE", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "OF", "ON", "OPTION", "OR", "ORDER", "OVERLAPS",
			"PROCEDURE", "PUBLIC", "RESOURCE", "REVOKE", "SELECT", "SHARE", "SIZE", "SQL", "START", "SUBTYPE",
			"TABAUTH", "TABLE", "THEN", "TO", "TYPE", "UNION", "UNIQUE", "UPDATE", "VALUES", "VIEW", "VIEWS", "WHEN",
			"WHERE", "WITH", "LONG", "NUMBER", "INTEGER" };
	private Set<String> keySet = null;

	public OracleDBManager(Connection connection) {
		super(connection);
	}

	@Override
	public String getTableExistCheckSql() {
		return "select count(TABLE_NAME) from user_tables where UPPER(table_name)=?";
	}

	@Override
	public String keyWordEscape(String key) {
		if (this.keySet == null) {
			this.keySet = new HashSet<>();
			for (byte b = 0; b < keyWord.length; b++)
				this.keySet.add(keyWord[b].toUpperCase());
		}
		return this.keySet.contains(key.toUpperCase()) ? ("\"" + key + "\"") : key;
	}

	@Override
	public int getDBType() {
		return DBType.ORACLE;
	}

	@Override
	public String getIndexSearchSql() {
		return "select table_name from user_indexes where UPPER(index_name) =?";
	}

	@Override
	public String getColumnCheckSql() {
		return "select column_name from user_TAB_COLUMNS where UPPER(table_name) =?";
	}

	@Override
	public String getIndexCheckSql() {
		return "select index_name from user_indexes where UPPER(table_name) =?";
	}

	@Override
	public PrepareSQL getLimitString(String query, String orderBy, boolean hasOffset, int startRow, int endRow) {
		if ((orderBy != null && orderBy.length() != 0))
			query = query + " order by " + orderBy;
		StringBuilder stringBuilder = new StringBuilder(query.length() + 100);
		PrepareSQL prepareSQL = new PrepareSQL();
		if (hasOffset) {
			stringBuilder.append("select * from ( select row_.*, rownum rownum_ from ( ");
		} else {
			stringBuilder.append("select * from ( ");
		}
		stringBuilder.append(query);
		if (hasOffset) {
			stringBuilder.append(" ) row_ ) where rownum_ > ? and rownum_ <= ? ");
			prepareSQL.addValue(Integer.valueOf(startRow));
			prepareSQL.addValue(Integer.valueOf(endRow));
		} else {
			stringBuilder.append(" ) where rownum <= ?");
			prepareSQL.addValue(Integer.valueOf(endRow));
		}
		prepareSQL.setSQL(stringBuilder.toString());
		return prepareSQL;
	}

}
