package com.bokesoft.dao;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bokesoft.config.ApplicationConfiguer;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

@Repository
public class LoginDao {
	private static Logger logger = LoggerFactory.getLogger(LoginDao.class);

	@Autowired
	private ApplicationConfiguer config;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean check(String userCode, String password) {
		if (config.isUse_ldap()) {
			if (ldapCheck(userCode, password)) {
				return true;
			}
		} else {

			String sql = "select userCode from logintable where userCode = ? and password = ?";

			// 使用jdbcTemplate的queryForObject方法，如果查询不到数据，就会抛异常
			try {
				jdbcTemplate.queryForObject(sql, String.class, userCode, password);
				return true;
			} catch (DataAccessException e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		}
		return false;
	}

	private boolean ldapCheck(String userCode, String password) {
		// 获取AD域服务器的ip地址
		String adAddr = config.getAdAddress();
		if (null == adAddr || adAddr.trim().length() <= 0) {
			logger.warn("Windows AD Server Address is not configurated, disable AD User Authentication.");
			return false;
		}

		logger.info("Begin to check user [" + userCode + "] from Windows AD Server: " + adAddr);
		UniAddress dc;
		try {
			//
			dc = UniAddress.getByName(adAddr);
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException: " + adAddr + ", " + e.getMessage(), e);
			return false;
		}
		NtlmPasswordAuthentication na = new NtlmPasswordAuthentication(null, userCode, password);
		try {
			SmbSession.logon(dc, na);
			logger.info("User [" + userCode + "] check from Windows AD Server: " + adAddr + " success");
			return true;
		} catch (SmbException e) {
			logger.error("SmbException: " + e.getMessage(), e);
			return false;
		}
	}
}
