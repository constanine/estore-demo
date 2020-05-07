package com.bokesoft.service;

import com.bokesoft.dao.ScanmissionDao;
import com.bokesoft.struc.ExportMissionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @author:gushiming
 * @date:2020/1/3
 * @description:
 * */
@Service
public class ScanmissionService {

    @Autowired
    private ScanmissionDao scanmissionDao;

    public List<ExportMissionBean> find() {
        return scanmissionDao.find();
    }

    public void updatedata1(String auto_do, String missionid) {
        scanmissionDao.updatedata1(auto_do,missionid);
    }

    public void updatedata2(String value, String format, String aNew, String missionid) {
        scanmissionDao.updatedata2(value,format,aNew,missionid);
    }

    public void updatedata3(String wait_auth, String wait_auth1, String missionid) {
        scanmissionDao.updatedata3(wait_auth,wait_auth1,missionid);
    }
}
