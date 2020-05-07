package com.bokesoft.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.ImportMissionBean;

@Repository
public class ToPersonPageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //导入任务集合
    public List<ImportMissionBean> findImport(String userCode) {
        String imsql = "select *  from immissiontable where userCode=? order by  startTime desc,missionid desc limit 3";
        return jdbcTemplate.query(imsql, new BeanPropertyRowMapper<>(ImportMissionBean.class), userCode);
    }

    //导出任务集合
    public List<ExportMissionBean> findExport(String userCode) {
        String exsql = "select missionid, startTime, step1state, sumFileNumber ,sumFileSize, judgement, step2state, endTime   from exmissiontable where userCode=? order by startTime desc,missionid desc limit 3";
        return jdbcTemplate.query(exsql,new BeanPropertyRowMapper<>(ExportMissionBean.class),userCode);
    }

}
