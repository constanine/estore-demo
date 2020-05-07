package com.bokesoft.threadPackge;/*
 * @author:gushiming
 * @date:2020/3/13
 * @description:
 * */

import com.bokesoft.struc.StatusBean;

import java.util.List;
import java.util.Map;

public class JobContext {

    private StatusBean status;
    private String missionid;
    private Map<String,Object> data;
    private Boolean stop;
    private String userCode;
    private List<String> fileList;

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public String getMissionid() {
        return missionid;
    }

    public void setMissionid(String missionid) {
        this.missionid = missionid;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
