package com.bokesoft.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bokesoft.struc.ExportMissionBean;

import java.util.List;

/*
 * @author:gushiming
 * @date:2020/1/3
 * @description:
 * */
@Repository
public class ToDoExportStep2Dao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ExportMissionBean> find(String auto_do) {
        String scansql = "select * from exmissiontable where judgement=?";
        List<ExportMissionBean> missions = jdbcTemplate.query(scansql, new BeanPropertyRowMapper<>(ExportMissionBean.class), auto_do);
        return missions;
    }
}
