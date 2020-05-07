package com.bokesoft.service;

import com.bokesoft.dao.ToDoExportStep2Dao;
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
public class ToDoExportStep2Service {

    @Autowired
    private ToDoExportStep2Dao exportStep2Dao;

    public List<ExportMissionBean> find(String auto_do) {
        List<ExportMissionBean> missions = exportStep2Dao.find(auto_do);
        return missions;
    }
}
