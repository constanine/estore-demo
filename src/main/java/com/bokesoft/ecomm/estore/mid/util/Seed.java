package com.bokesoft.ecomm.estore.mid.util;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.bokesoft.ecomm.estore.mid.connection.args.struc.PSArgs;
import com.bokesoft.ecomm.estore.mid.connection.struc.DataType;
import com.bokesoft.ecomm.estore.mid.intf.IDBManager;

/**
 * Seed根据种子类型生成不重复的数值，0~1000为系统保留的种子类型
 * 
 * @author 刘翔翔
 *
 */
public class Seed {

	public static final String SYS_SEED_TABLE = "Sys_IDSeed";

	private static final String SELECT_SQL = "select SeedMark from " + SYS_SEED_TABLE + " where SeedID=? ";

	private static final String UPDATE_SQL = "update " + SYS_SEED_TABLE + " set SeedMark=? where SeedID=?";

	private static final String AUTO_PLUS_UPDATE_SQL = "update " + SYS_SEED_TABLE + " set SeedMark=SeedMark+? where SeedID=?";

	private static final String INSERT_SQL = "insert into " + SYS_SEED_TABLE + "(SeedID,SeedMark,Description,CreateTime) values(?,?,?,?)";

	/** 申请的区间宽度 */
	public static Long IntervalWidth = 256L;

	/** 种子发生器的类型 */
	private int seedType = -1;

	/** 种子类型的描述 */
	private String description = "";

	/** 当前编号可用区间的最小值 */
	private Long begin = -1L;

	/** 当前编号可用区间的最大值 */
	private Long end = -1L;

	/** 种子是否初始化过 */
	private boolean init = false;

	/** 种子分配的起始点 */
	private Integer startIndex = 0;

	public Seed(int seedType, String description) {
		this.seedType = seedType;
		this.description = description;
	}

	public Seed(int seedType, String description, Integer startIndex) {
		this.seedType = seedType;
		this.description = description;
		this.startIndex = startIndex;
	}

	public Long applyID(EstoreContext context) throws Throwable {
		IDBManager dbManager = null;
		try {
			if (!init) {
				dbManager = context.getDBManager();
				init(dbManager);
			}

			if (begin >= end) {
				if (dbManager == null) {
					dbManager = context.getDBManager();
				}
				applyNewInterval(dbManager);
			}
			if (dbManager != null ) {
				dbManager.commit();
			}
		} catch (Throwable e) {
			if (dbManager != null) {
				dbManager.rollback();
			}
			throw e;
		} finally {
			if (dbManager != null ) {
				dbManager.close();
			}
		}

		return ++begin;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @throws Throwable
	 */
	private void init(IDBManager dbManager) throws Throwable {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			PSArgs args = new PSArgs();
			args.addArg(DataType.INT, seedType);
			dbManager.setRowLock(SYS_SEED_TABLE, "SeedID=?", args);

			ps = dbManager.prepareStatement(SELECT_SQL);
			rs = dbManager.executeQuery(ps, SELECT_SQL, args);
			boolean hasData = rs.next();
			if (hasData) {
				begin = rs.getLong(1);
				end = begin + IntervalWidth;
			}
			rs.close();
			ps.close();

			if (hasData) {
				ps = dbManager.prepareStatement(UPDATE_SQL);
				args = new PSArgs();
				args.addArg(DataType.LONG, end);
				args.addArg(DataType.INT, seedType);
				dbManager.executeUpdate(ps, UPDATE_SQL, args);
			} else {
				ps = dbManager.prepareStatement(INSERT_SQL);
				args = new PSArgs();
				args.addArg(DataType.INT, seedType);
				args.addArg(DataType.LONG, startIndex + IntervalWidth);
				args.addArg(DataType.STRING, description);
				args.addArg(DataType.DATE, new Date(System.currentTimeMillis()));
				dbManager.executeUpdate(ps, INSERT_SQL, args);

				begin = startIndex.longValue();
				end = startIndex + IntervalWidth;
			}
			init = true;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
	}

	/**
	 * 申请新的ID区间
	 * 
	 * @param context
	 * @throws Throwable
	 */
	private void applyNewInterval(IDBManager dbManager) throws Throwable {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// 每次申请时，如果区间放大两倍，一直放大到8192
			if (IntervalWidth != 8192)
				IntervalWidth = IntervalWidth * 2;

			PSArgs args = new PSArgs();
			args.addLongArg(IntervalWidth);
			args.addIntArg(seedType);

			ps = dbManager.prepareStatement(AUTO_PLUS_UPDATE_SQL);
			dbManager.executeUpdate(ps, AUTO_PLUS_UPDATE_SQL, args);
			ps.close();

			ps = dbManager.prepareStatement(SELECT_SQL);
			args = new PSArgs();
			args.addIntArg(seedType);
			rs = dbManager.executeQuery(ps, SELECT_SQL, args);
			rs.next();

			end = rs.getLong(1);
			begin = end - IntervalWidth;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}

	}

}

