package com.bokesoft.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/*
 * @author:gushiming
 * @date:2020/1/9
 * @description:
 * */
@Repository
public class ExectueMissionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertIm(Object[] params) {
        String sql = "insert into immissiontable (userCode,missionid,filelogUrl,runlogUrl,sumFileNumber,sumFileSize)values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, params);
    }

    public void insertEx(Object[] params) {
        String sql = "insert into exmissiontable (userCode, missionid, starttime, sumfilesize,step1state, exportfilelogurl, exportstep1runlogUrl, sumFileNumber,judgement) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,params);
    }

    public void insertEx2(Object[] params) {
        String sql = "insert into exmissiontable (userCode, missionid, starttime,  sumfilesize, exportfilelogurl, sumFileNumber,judgement) values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,params);
    }
}
