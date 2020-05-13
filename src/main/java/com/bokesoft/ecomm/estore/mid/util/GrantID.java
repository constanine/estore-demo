package com.bokesoft.ecomm.estore.mid.util;

public class GrantID {

	public static final int OID = 0;
	/** 迁移增量表的主键ID */
	public static final int MIGRATION_DELTA_ID = 4;

	private static final Seed OIDSeed = new Seed(OID, "OID", 10000);
	private static final Seed Migration_Delta_ID = new Seed(MIGRATION_DELTA_ID, "Migration_Delta_ID");

	public static synchronized Long applyID(EstoreContext context, int seedType) throws Throwable {
		switch (seedType) {
		case OID:
			return OIDSeed.applyID(context);
		case MIGRATION_DELTA_ID:
			return Migration_Delta_ID.applyID(context);
		}
		return 1L;
	}
}
