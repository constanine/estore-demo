package com.bokesoft.ecomm.estore.mid.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bokesoft.ecomm.estore.mid.intf.IDBManager;

public class EstoreContext {
	private IDBManager dbmanager;
	private Map<String, Object> paras = null;

	public EstoreContext(IDBManager dbmanager) {
		this.dbmanager = dbmanager;
		this.paras = new HashMap<String, Object>();
	}

	public IDBManager getDBManager() {
		return this.dbmanager;
	}

	public Long applyNewOID() throws Throwable {
		return GrantID.applyID(this, GrantID.OID);
	}

	public Long applyNewMigrationDeltaID() throws Throwable {
		return GrantID.applyID(this, GrantID.MIGRATION_DELTA_ID);
	}

	/**
	 * 取得参数集
	 * 
	 * @return 参数集对象
	 */
	public Map<String, Object> getParas() {
		return this.paras;
	}

	/**
	 * 设置参数集合
	 * 
	 * @param paras 参数集
	 */
	public void setParas(Map<String, Object> paras) {
		this.paras = paras;
	}

	/**
	 * 取得参数的值
	 * 
	 * @param key 参数标识
	 * @return 如果key存在则返回其值，否则返回null
	 */
	public Object getPara(String key) {
		return this.paras != null ? this.paras.get(key) : null;
	}

	/**
	 * 设置参数值
	 * 
	 * @param key   参数标识
	 * @param value 参数值
	 */
	public void setPara(String key, Object value) {
		this.paras.put(key, value);
	}
}
