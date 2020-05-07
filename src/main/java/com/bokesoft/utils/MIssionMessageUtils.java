package com.bokesoft.utils;

import com.bokesoft.StartApplication;
import com.bokesoft.struc.MessageBean;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class MIssionMessageUtils {
	static List<MessageBean> messages = new ArrayList<MessageBean>();
	static Object[] params;
	static List<String> authorizors = new ArrayList<String>();

	private static JdbcTemplate getJdbcTemplate() {
		return StartApplication.getBean(JdbcTemplate.class);
	}

	// 启动搜索所有带审批的集合，放到messages中
	public static List<MessageBean> getMissionMessage(String userCode, HttpServletRequest request) {
		if (EnvUtils.isAuthorizer(request)) {
			Object[] params = { "wait_authorized", "waitting", userCode };
			String msgsql = "select * from messagetable where MissionState= ? or MissionState= ? or userCode=? order by reportTime desc";
			messages = getJdbcTemplate().query(msgsql, new BeanPropertyRowMapper<>(MessageBean.class), params);
			return messages;
		} else {
			Object[] params = { userCode };
			String msgsql = "select *  from messagetable where userCode=? order by reportTime desc";
			messages = getJdbcTemplate().query(msgsql, new BeanPropertyRowMapper<>(MessageBean.class), params);
			return messages;
		}
	}

	public static void dealMissionMessage(String userCode) {
		String msgreadsql = "update messagetable set MessageState= ? where userCode=? and MissionState not in(?)";
		getJdbcTemplate().update(msgreadsql, "read", userCode, "wait_authorized");

		String msgreadsql2 = "update messagetable set MessageState= ? where userCode=? and MissionState =?";
		getJdbcTemplate().update(msgreadsql2, "waitting", userCode, "wait_authorized");

		if (messages.size() > 5) {
			String msgdeletesql = "delete from  messagetable  where MessageState= ? and userCode=?";
			getJdbcTemplate().update(msgdeletesql, "read", userCode);
		}
	}
}
