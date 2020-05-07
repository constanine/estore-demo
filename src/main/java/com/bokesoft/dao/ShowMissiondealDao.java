package com.bokesoft.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.ImportMissionBean;

import java.util.List;
import java.util.Map;

/*
 * @author:gushiming
 * @date:2020/1/2
 * @description:
 * */
@Repository
public class ShowMissiondealDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ImportMissionBean> findIm(String userCode, int startnum, int limitnum) {
        String imsql = "select * from immissiontable where userCode=? order by  startTime desc,missionid desc limit ?,?";

        List<ImportMissionBean> immissions = jdbcTemplate.query(imsql, new BeanPropertyRowMapper<>(ImportMissionBean.class), userCode, startnum, limitnum);
        return immissions;
    }

    public List<Map<String, Object>> findImPage(String userCode) {
        String impagessql = "select count(*) as pages from immissiontable where userCode=?";
        List<Map<String, Object>> pageslist = jdbcTemplate.queryForList(impagessql, userCode);
        return pageslist;
    }

    public List<ExportMissionBean> findEx(String userCode, int startnum, int limitnum) {
        String exsql = "select * from exmissiontable where userCode=? order by  startTime desc,missionid desc limit ?,?";
        List<ExportMissionBean> exmissions = jdbcTemplate.query(exsql, new BeanPropertyRowMapper<>(ExportMissionBean.class), userCode, startnum, limitnum);
        return exmissions;
    }

    public List<Map<String, Object>> findExPage(String userCode) {
        String expagesql = "select count(*) as pages from exmissiontable where userCode=? ";
        List<Map<String, Object>> pageslist = jdbcTemplate.queryForList(expagesql, userCode);
        return pageslist;
    }
}
