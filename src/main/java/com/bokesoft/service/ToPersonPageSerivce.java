package com.bokesoft.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bokesoft.dao.ToPersonPageDao;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.ImportMissionBean;
import com.bokesoft.struc.MessageBean;

@Service
public class ToPersonPageSerivce {

    List<MessageBean>  messages = new ArrayList<MessageBean>();

    @Autowired
    private ToPersonPageDao personPageDao;

    //导入任务集合
    public List<ImportMissionBean> findImport(String userCode) {
        return personPageDao.findImport(userCode);
    }

    //导出任务集合
    public List<ExportMissionBean> findExport(String userCode) {
        return personPageDao.findExport(userCode);
    }

}
