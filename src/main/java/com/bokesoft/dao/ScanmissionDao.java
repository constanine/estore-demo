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
public class ScanmissionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ExportMissionBean> find() {
        String scansql = "select missionid,userCode from exmissiontable where judgement='ready_scan' "
        		+ " AND step1state='finished' AND (step2state = ''  OR step2state is Null) ";
        return jdbcTemplate.query(scansql,new BeanPropertyRowMapper<>(ExportMissionBean.class));
    }

    public void updatedata1(String auto_do, String missionid) {
        String exsql = " update exmissiontable set judgement=? where missionid like ?";
        jdbcTemplate.update(exsql,auto_do,missionid);
    }

    public void updatedata2(String value, String format, String aNew, String missionid) {
        String msgsql2="update messagetable set MissionState=? , reportTime=?, MessageState=? where missionid =?";
        jdbcTemplate.update(msgsql2,value,format,aNew,missionid);

    }

    public void updatedata3(String wait_auth, String wait_auth1, String missionid) {
        String amsql = " update exmissiontable set judgement=? , authorizeState=? where missionid = ?";
        jdbcTemplate.update(amsql,wait_auth,wait_auth1,missionid);

    }
}
