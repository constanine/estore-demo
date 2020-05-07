package com.bokesoft.service;

import com.bokesoft.dao.ToAuthorizePageDao;
import com.bokesoft.struc.ExportMissionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @author:gushiming
 * @date:2019/12/30
 * @description:
 * */
@Service
public class ToAuthorizePageService {

    @Autowired
    private ToAuthorizePageDao authorizePageDao;

    public List<ExportMissionBean> find(String wait_auth, String wait_auth1) {
        List<ExportMissionBean> missions = authorizePageDao.find(wait_auth,wait_auth1);
        return missions;
    }
}
