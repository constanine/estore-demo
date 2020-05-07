package com.bokesoft.dao;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bokesoft.struc.ExportMissionBean;

/*
 * @author:gushiming
 * @date:2020/1/6
 * @description:
 * */
@Repository
public class AuthorizeMissionDao {
	private static Logger log = LoggerFactory.getLogger(AuthorizeMissionDao.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void update1(Object[] params) {
        String sql = " update exmissiontable set authorizer=? , authorizeState=? , authorizeTime=? , judgement=?, authorityLogUrl =? where missionid like ?";
        jdbcTemplate.update(sql,params);
    }

    public void update2(Object[] msgparamsstop) {
        String msgsql="update messagetable set MissionState=? , reportTime=?, MessageState=? where missionid =?";
        jdbcTemplate.update(msgsql,msgparamsstop);
    }

    public ExportMissionBean findExportMission(Object[] params) {
        String scansql = "select * from exmissiontable where missionid=?";
        List<ExportMissionBean> missions = jdbcTemplate.query(scansql, new BeanPropertyRowMapper<>(ExportMissionBean.class), params);
        return missions.get(0);
    }
}
