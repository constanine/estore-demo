package com.bokesoft.dao;/*
 * @author:gushiming
 * @date:2019/12/30
 * @description:
 * */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bokesoft.struc.ExportMissionBean;

import java.util.List;

@Repository
public class ToAuthorizePageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ExportMissionBean> find(String wait_auth, String wait_auth1) {
        String scansql = "select * from exmissiontable where judgement= ? and authorizeState = ?";
        List<ExportMissionBean> missions = jdbcTemplate.query(scansql, new BeanPropertyRowMapper<>(ExportMissionBean.class), wait_auth, wait_auth1);
        return missions;
    }
}
