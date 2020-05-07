package com.bokesoft.service;

import com.bokesoft.dao.AuthorizeMissionDao;
import com.bokesoft.struc.ExportMissionBean;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * @author:gushiming
 * @date:2020/1/6
 * @description:
 * */
@Service
public class AuthorizeMissionService {

    @Autowired
    private AuthorizeMissionDao authorizeMissionDao;

    public void update1(Object[] params) {
        authorizeMissionDao.update1(params);
    }

    public void update2(Object[] msgparamsstop) {
        authorizeMissionDao.update2(msgparamsstop);

    }

	public ExportMissionBean findExportMission(String missionid) {
		return authorizeMissionDao.findExportMission(new Object[]{missionid});
	}
}
