package com.bokesoft.service;

import com.bokesoft.dao.ExectueMissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * @author:gushiming
 * @date:2020/1/9
 * @description:
 * */
@Service
public class ExectueMissionService {

    @Autowired
    private ExectueMissionDao exectueMissionDao;

    public void insertIm(Object[] params) {
        exectueMissionDao.insertIm(params);

    }

    public void insertEx(Object[] params) {
        exectueMissionDao.insertEx(params);
    }

    public void insertEx2(Object[] params) {
        exectueMissionDao.insertEx2(params);
    }
}
