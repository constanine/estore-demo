package com.bokesoft.ecomm.estore.mid.connection.dbmanager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bokesoft.ecomm.estore.mid.connection.args.struc.ListQueryArguments;
import com.bokesoft.ecomm.estore.mid.connection.args.struc.NormalQueryArguments;
import com.bokesoft.ecomm.estore.mid.connection.args.struc.PSArgs;
import com.bokesoft.ecomm.estore.mid.connection.args.struc.PsPara;
import com.bokesoft.ecomm.estore.mid.connection.args.struc.QueryArguments;
import com.bokesoft.ecomm.estore.mid.connection.struc.DataType;
import com.bokesoft.ecomm.estore.mid.connection.struc.PageResult;
import com.bokesoft.ecomm.estore.mid.connection.struc.PrepareSQL;
import com.bokesoft.ecomm.estore.mid.intf.IDBManager;
import com.bokesoft.ecomm.estore.utils.DBTools;

public abstract class GeneralDBManager implements IDBManager {
	private Logger logger = Logger.getLogger(this.getClass());
	protected Connection connection = null;

	public GeneralDBManager(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void rollback() throws SQLException {
		this.connection.rollback();
	}

	@Override
	public void commit() throws SQLException {
		this.connection.commit();
	}

	@Override
	public void close() throws SQLException {
		if (this.connection == null)
			return;
		if (!this.connection.isClosed()) {
			this.connection.rollback();
			this.connection.close();
		}
	}

	@Override
	public boolean checkTableExist(String tableKey) throws Throwable {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			logger.debug(getTableExistCheckSql());
			statement = createStatement();
			resultSet = statement.executeQuery(getTableExistCheckSql());
			return (resultSet.getInt(1) == 1);
		} catch (Throwable throwable) {
			logger.error(throwable.getMessage(), throwable);
			throw throwable;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
	}

	public abstract String getTableExistCheckSql();

	@Override
	public String searchIndex(String index) throws Throwable {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		if (StringUtils.isNotBlank(index)) {
			try {
				String str = getIndexSearchSql();
				preparedStatement = prepareStatement(str);
				PSArgs pSArgs = new PSArgs();
				pSArgs.addArg(DataType.STRING, index);
				resultSet = executeQuery(preparedStatement, str, (QueryArguments) pSArgs);
				if (resultSet.next()) {
					return resultSet.getString(1);
				}
				return null;
			} catch (Throwable throwable) {
				logger.error(throwable.getMessage(), throwable);
				throw throwable;
			} finally {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		}
		return null;
	}

	public abstract String getIndexSearchSql();

	public HashSet<String> getTableColumnSet(String tableKey) throws Throwable {
		String str = getColumnCheckSql();
		HashSet<String> hashSet = new HashSet<String>();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		if (StringUtils.isNotBlank(tableKey)) {
			try {
				preparedStatement = prepareStatement(str);
				PSArgs pSArgs = new PSArgs();
				pSArgs.addArg(DataType.STRING, tableKey.toUpperCase());
				resultSet = executeQuery(preparedStatement, str, (QueryArguments) pSArgs);
				while (resultSet.next()) {
					hashSet.add(resultSet.getString(1).toUpperCase());
				}
			} catch (Throwable throwable) {
				logger.error("getTableColumnSet tableKey=" + tableKey, throwable);
				throw throwable;
			} finally {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		}
		return hashSet;
	}

	public abstract String getColumnCheckSql();

	public HashSet<String> getIndexSet(String tableKey) throws Throwable {
		String sql = getIndexCheckSql();
		HashSet<String> hashSet = new HashSet<String>();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		if (StringUtils.isNotBlank(tableKey)) {
			try {
				preparedStatement = prepareStatement(sql);
				PSArgs pSArgs = new PSArgs();
				pSArgs.addArg(DataType.STRING, tableKey.toUpperCase());
				resultSet = executeQuery(preparedStatement, sql, (QueryArguments) pSArgs);
				while (resultSet.next()) {
					if (null != resultSet.getString(1)) {
						hashSet.add(resultSet.getString(1).toUpperCase());
					}
				}
			} catch (Throwable throwable) {
				logger.error("getIndexSet tableKey=" + tableKey, throwable);
				throw throwable;
			} finally {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		}
		return hashSet;
	}

	public abstract String getIndexCheckSql();

	public ResultSet executeQuery(PreparedStatement preparedStatement, String sql, QueryArguments pSArgs)
			throws Throwable {
		fillPrepareParameters(preparedStatement, pSArgs);
		logger.info("execute sql:" + sql + ",args:" + pSArgs);
		try {
			return preparedStatement.executeQuery();
		} catch (Throwable throwable) {
			logger.error("execute sql:" + sql + ",args:" + pSArgs + " error", throwable);
			throw throwable;
		}
	}

	private void fillPrepareParameters(PreparedStatement preparedStatement, QueryArguments pSArgs) throws SQLException {
		if (pSArgs == null)
			return;
		if (pSArgs.supportType()) {
			int j = pSArgs.size();
			for (byte b1 = 0; b1 < j; b1++) {
				int k = pSArgs.getType(b1);
				Object object = pSArgs.get(b1);
				setParameter(preparedStatement, b1 + 1, object, k);
			}
			return;
		}
		int i = pSArgs.size();
		for (byte b = 0; b < i; b++) {
			Object object = pSArgs.get(b);
			if (!(object instanceof Timestamp) && !(object instanceof Date) && object instanceof Date) {
				object = new Timestamp(((Date) object).getTime());
			}
			preparedStatement.setObject(b + 1, object);
		}
	}

	@Override
	public long getCurTime() throws Throwable {
		return System.currentTimeMillis();
	}

	@Override
	public int getTimezoneOffset() throws Throwable {
		Calendar.getInstance().getTimeZone();
		return 0;
	}

	@Override
	public List<Map<String, Object>> execPrepareQuery(String sql, Object... arguments) throws Throwable {
		return _execPrepareQuery(sql, (QueryArguments) new NormalQueryArguments(arguments));
	}

	@Override
	public List<Map<String, Object>> execPrepareQuery(String sql, List<Object> arguments) throws Throwable {
		return _execPrepareQuery(sql, (QueryArguments) new ListQueryArguments(null, arguments));
	}

	@Override
	public List<Map<String, Object>> execPrepareQuery(String sql, List<Integer> types, List<Object> arguments)
			throws Throwable {

		return _execPrepareQuery(sql, (QueryArguments) new ListQueryArguments(types, arguments));
	}

	private List<Map<String, Object>> _execPrepareQuery(String sql, QueryArguments queryArguments) throws Throwable {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = prepareStatement(sql);
			resultSet = executeQuery(preparedStatement, sql, queryArguments);
			return DBTools.wrapDBResult(resultSet);
		} catch (Throwable throwable) {
			throw throwable;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}

	@Override
	public PageResult execPageQueryByRowIdx(String sql, String orderBy, int start, int end) throws Throwable {
		PrepareSQL prepareSQL = getLimitString(sql, orderBy, start > 0 ? true : false, start, end);
		List<Map<String, Object>> data = _execPrepareQuery(prepareSQL.getSQL(), new NormalQueryArguments(prepareSQL.getPrepareValues().toArray()));
		PageResult result = new PageResult();
		result.setData(data);
		result.setPageSize(start-end);
		result.setPageNo(end/(start-end));
		return result;
	}

	@Override
	public PageResult execPageQueryByPageSize(String sql, String orderBy, int pageNo, int pageSize) throws Throwable {
		int start = pageNo * pageSize;
		int end = (pageNo + 1) * pageSize + pageSize;
		PrepareSQL prepareSQL = getLimitString(sql, orderBy, start > 0 ? true : false, start, end);
		List<Map<String, Object>> data = _execPrepareQuery(prepareSQL.getSQL(),
				new NormalQueryArguments(prepareSQL.getPrepareValues().toArray()));
		PageResult result = new PageResult();
		result.setData(data);
		result.setPageSize(pageSize);
		result.setPageNo(pageNo);
		return result;
	}

	@Override
	public List<Map<String, Object>> execQuery(String sql) throws Throwable {
		Statement statement = null;
		ResultSet resultSet = null;
		List<Map<String, Object>> result;
		try {
			logger.debug(sql);
			statement = createStatement();
			resultSet = statement.executeQuery(sql);
			result = DBTools.wrapDBResult(resultSet);
		} catch (Throwable throwable) {
			logger.error(throwable.getMessage(), throwable);
			throw throwable;
		} finally {
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
		}
		return result;
	}

	@Override
	public int execPrepareUpdate(String sql, Object... arguments) throws Throwable {
		return execPrepareUpdate(sql, (QueryArguments) new NormalQueryArguments(arguments));
	}

	@Override
	public int execPrepareUpdate(String sql, List<Object> arguments) throws Throwable {
		return execPrepareUpdate(sql, (QueryArguments) new ListQueryArguments(null, arguments));
	}

	@Override
	public int execPrepareUpdate(String sql, List<Integer> types, List<Object> arguments) throws Throwable {
		return execPrepareUpdate(sql, (QueryArguments) new ListQueryArguments(types, arguments));
	}

	@Override
	public int execUpdate(String sql) throws Throwable {
		Statement statement = null;
		try {
			logger.info("execute sql:" + sql);
			statement = createStatement();
			return statement.executeUpdate(sql);
		} catch (Throwable throwable) {
			logger.error("execute sql:" + sql + " error", throwable);
			throw throwable;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	public int executeUpdate(PreparedStatement preparedStatement, String sql, QueryArguments psArgs) throws Throwable {
		int result = 0;
		try {
			fillPrepareParameters(preparedStatement, psArgs);
			result = preparedStatement.executeUpdate();
			logger.info("execute sql:" + sql + ",args:" + psArgs);
		} catch (Throwable throwable) {
			logger.error("execute sql:" + sql + ",args:" + psArgs + " error", throwable);
			throw throwable;
		}
		return result;
	}

	@Override
	public Statement createStatement() throws Throwable {
		return this.connection.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.connection.prepareStatement(sql);
	}

	@Override
	public void setParameter(PreparedStatement ps, int parameterIndex, Object value, int dataType) throws SQLException {
		switch (dataType) {
		case DataType.INT:
			if (value == null) {
				ps.setNull(parameterIndex, 4);
				return;
			}
			ps.setInt(parameterIndex, ((Integer) value).intValue());
			return;
		case DataType.STRING:
		case DataType.FIXED_STRING:
		case DataType.TEXT:
			ps.setString(parameterIndex, (value == null) ? null : value.toString());
			return;
		case DataType.NUMERIC:
			ps.setBigDecimal(parameterIndex, (value == null) ? null : (BigDecimal) value);
			return;
		case DataType.BINARY:
			ps.setBytes(parameterIndex, (value == null) ? null : (byte[]) value);
			return;
		case DataType.LONG:
			if (value == null) {
				ps.setNull(parameterIndex, -5);
				return;
			}
			ps.setLong(parameterIndex, ((Long) value).longValue());
			return;
		case DataType.DATETIME:
			if (value == null) {
				ps.setNull(parameterIndex, 93);
				return;
			}
			ps.setTimestamp(parameterIndex, new Timestamp(((Date) value).getTime()));
			return;
		case DataType.DATE:
			if (value == null) {
				ps.setNull(parameterIndex, 91);
				return;
			}
			ps.setDate(parameterIndex, new Date(((Date) value).getTime()));
			return;
		case DataType.DOUBLE:
			if (value == null) {
				ps.setNull(parameterIndex, 8);
				return;
			}
			ps.setDouble(parameterIndex, ((BigDecimal) value).doubleValue());
			return;
		case DataType.FLOAT:
			if (value == null) {
				ps.setNull(parameterIndex, 6);
				return;
			}
			ps.setFloat(parameterIndex, ((BigDecimal) value).floatValue());
			return;
		case DataType.BOOLEAN:
			ps.setInt(parameterIndex,
					((value == null) ? null : Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0)).intValue());
			return;
		}
		throw new RuntimeException("参数格式不匹配");

	}

	@Override
	public void setRowLock(String tableKey, String columnKey, Long OID) throws Throwable {
		// TODO Auto-generated method stub

	}
}
