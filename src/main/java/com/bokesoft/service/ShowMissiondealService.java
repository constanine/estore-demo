package com.bokesoft.service;

import com.bokesoft.dao.ShowMissiondealDao;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.ImportMissionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * @author:gushiming
 * @date:2020/1/2
 * @description:
 * */
@Service
public class ShowMissiondealService {

    @Autowired
    private ShowMissiondealDao missiondealDao;

    public List<ImportMissionBean> findIm(String userCode, int startnum, int limitnum) {

        List<ImportMissionBean> immissions = missiondealDao.findIm(userCode, startnum, limitnum);

        return immissions;
    }

    public List<Map<String, Object>> findImPage(String userCode) {
        List<Map<String, Object>> pageslist = missiondealDao.findImPage(userCode);

        return pageslist;
    }


    public List<ExportMissionBean> findEx(String userCode, int startnum, int limitnum) {
        List<ExportMissionBean> exmissions = missiondealDao.findEx(userCode, startnum, limitnum);
        return exmissions;
    }

    public List<Map<String, Object>> finExPage(String userCode) {
        List<Map<String, Object>> pageslist = missiondealDao.findExPage(userCode);
        return pageslist;
    }
}
