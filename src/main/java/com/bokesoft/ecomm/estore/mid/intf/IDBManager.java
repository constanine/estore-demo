package com.bokesoft.ecomm.estore.mid.intf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.bokesoft.ecomm.estore.mid.connection.args.struc.QueryArguments;
import com.bokesoft.ecomm.estore.mid.connection.struc.DBType;
import com.bokesoft.ecomm.estore.mid.connection.struc.PageResult;
import com.bokesoft.ecomm.estore.mid.connection.struc.PrepareSQL;

public interface IDBManager {
	/**
	 * 检查表是否存在
	 * 
	 * @param tableKey 表标识
	 * @return 如果tableKey存在则返回true，否则返回false
	 * @throws Throwable 处理异常
	 */
	public boolean checkTableExist(String tableKey) throws Throwable;

	/**
	 * 查询指定表现有索引（不包含主键的索引）
	 * 
	 * @param tableKey 表标识
	 * @return 索引集合
	 * @throws Throwable 处理异常
	 */
	public HashSet<String> getIndexSet(String tableKey) throws Throwable;

	/**
	 * 给定索引的名称，查询其所在的表名
	 * 
	 * @param index 索引名称
	 * @return 索引index所在的表名
	 * @throws Throwable 处理异常
	 */
	public String searchIndex(String index) throws Throwable;

	/**
	 * 查询指定表现有的列，将所有列的KEY转为大写放在一个哈希集合中返回。
	 * 
	 * @param tableKey 表标识
	 * @return tableKey中的列集合
	 * @throws Throwable 处理异常
	 */
	public HashSet<String> getTableColumnSet(String tableKey) throws Throwable;

	/**
	 * 数据库的保留关键字的转义处理
	 * 
	 * @param key 表名或者字段名等
	 * @return 处理过的数据库可以识别的字符串
	 */
	public String keyWordEscape(String key);

	/**
	 * 获取当前数据库类型
	 * 
	 * @return 数据库类型
	 * @see DBType
	 */
	public int getDBType();

	/**
	 * 获取当前时间
	 * 
	 * @return 当前时间，类型为long，即从1970-01-01 00:00:01到该时间的毫秒数 可使用java.util.Date dt = new
	 *         Date(longtime)将long类型转换为date类型
	 * @throws Throwable 处理异常
	 */
	public long getCurTime() throws Throwable;

	/**
	 * 
	 * @return 返回时区偏移量UTC+xxx
	 * @throws Throwable 处理异常
	 */
	public int getTimezoneOffset() throws Throwable;

	// 数据库辅助查询的方法
	/**
	 * 使用PrepareStatement查询数据
	 * 
	 * @param sql       原始sql语句，其中参数中的变量以?表示
	 * @param arguments 用于替换?的参数列表
	 * @return 数据集合
	 * @throws Throwable 处理异常
	 */
	public List<Map<String, Object>> execPrepareQuery(String sql, Object... arguments) throws Throwable;

	public List<Map<String, Object>> execPrepareQuery(String sql, List<Object> arguments) throws Throwable;

	public List<Map<String, Object>> execPrepareQuery(String sql, List<Integer> types, List<Object> arguments)
			throws Throwable;

	public List<Map<String, Object>> execQuery(String sql) throws Throwable;

	public PageResult execPageQueryByRowIdx(String sql, String orderBy, int start, int end) throws Throwable;

	public PageResult execPageQueryByPageSize(String sql, String orderBy, int pageSize, int pageNo) throws Throwable;

	public ResultSet executeQuery(PreparedStatement preparedStatement, String str, QueryArguments pSArgs)
			throws Throwable;

	// 数据库辅助更新的方法
	/**
	 * 使用PrepareStatement更新数据
	 * 
	 * @param sql       原始sql语句，其中参数中的变量以?表示
	 * @param arguments 用于替换?的参数列表
	 * @return 影响的数据行数
	 * @throws Throwable
	 */
	public int execPrepareUpdate(String sql, Object... arguments) throws Throwable;

	public int execPrepareUpdate(String sql, List<Object> arguments) throws Throwable;

	public int execPrepareUpdate(String sql, List<Integer> types, List<Object> arguments) throws Throwable;

	public int executeUpdate(PreparedStatement ps, String insertSql, QueryArguments args) throws Throwable;

	public int execUpdate(String sql) throws Throwable;

	/**
	 * 生成Statement对象,由具体的DBManager实现
	 * 
	 * @return Statement对象
	 * @throws Throwable 处理异常
	 */
	public Statement createStatement() throws Throwable;

	/**
	 * 生成PreparedStatement对象,由具体的DBManager实现
	 * 
	 * @return PreparedStatement对象
	 * @throws Throwable 处理异常
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException;

	/**
	 * 返回查询限定的SQL语句
	 * 
	 * 
	 * @param query     查询语句
	 * @param orderBy   排序字段
	 * @param hasOffset 偏差标志
	 * @param startRow  查询的起始行
	 * @param endRow    查询的结束行
	 * @return PrepareSQL对象
	 */
	public PrepareSQL getLimitString(String query, String orderBy, boolean hasOffset, int startRow, int endRow);

	/**
	 * 回滚当前事务
	 * 
	 * @throws SQLException 数据库异常
	 */
	public void rollback() throws SQLException;

	/**
	 * 提交事务
	 * 
	 * @throws SQLException 数据库异常
	 */
	public void commit() throws SQLException;

	/**
	 * 关闭当前事务
	 * 
	 * @throws SQLException 数据库异常
	 */
	public void close() throws SQLException;

	/**
	 * 为预处理语句赋值
	 * 
	 * @param ps             预编译的执行语句
	 * @param parameterIndex 参数序号
	 * @param value          参数值
	 * @param dataType       数据类型
	 * @throws SQLException     数据库异常
	 * @throws MidCoreException 中间层异常
	 */
	public void setParameter(PreparedStatement ps, int parameterIndex, Object value, int dataType) throws SQLException;

	/**
	 * 开启事务
	 * 
	 * @throws Throwable
	 */
	default public void begin() throws Throwable {
	}

	/**
	 * 返回数据库主版本号
	 * 
	 * @return 数据库主版本号
	 * @throws Throwable 处理异常
	 */
	public default int getMainVersion() throws Throwable {
		return -1;
	}


	/**
	 * 锁住数据库指定数据，条件为OID
	 * 
	 * @param tableKey 表标识
	 * @param columnKey 列标识
	 * @param OID 数据对象数据标识
	 * @throws Throwable 处理异常
	 */
	public void setRowLock(String tableKey, String columnKey, Long OID) throws Throwable;

	/**
	 * 根据条件和参数锁住数据库指定数据
	 * 
	 * @param tableKey 表标识
	 * @param condition 条件
	 * @param args 参数
	 * @throws Throwable 处理异常
	 */
	public void setRowLock(String tableKey, String condition, QueryArguments args) throws Throwable;
}
