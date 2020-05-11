package com.bokesoft.ecomm.estore.mid.connection.args.struc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PsPara {
	private PreparedStatement ps;

	private String sql;

	public PsPara(PreparedStatement paramPreparedStatement, String paramString) {
		this.ps = paramPreparedStatement;
		this.sql = paramString;
	}

	public PreparedStatement getPs() {
		return this.ps;
	}

	public String getSql() {
		return this.sql;
	}

	public void close() throws SQLException {
		this.ps.close();
	}
}
