package com.bokesoft.service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bokesoft.controller.SerachFileController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bokesoft.dao.LoginDao;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.NetEnvUtils;


@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    public boolean check(String userCode, String password, HttpServletRequest request) {
        //判断查询的用户是否存在
        if (loginDao.check(userCode, password)) {
        	//用户存在
            userCode.toLowerCase();
            HttpSession session = request.getSession();
            session.invalidate();
            session = request.getSession();
            session.setAttribute(SerachFileController.USERCODE, userCode);
            if(NetEnvUtils.isInnerNetReq(request)) {
            	session.setAttribute("netEnv", "inner");
            	logger.info("user login for inner,request url:"+request.getRequestURL().toString());                 
            }
            if(NetEnvUtils.isOutterNetReq(request)) {
            	session.setAttribute("netEnv", "outter");
            	logger.info("user login for outter,request url:"+request.getRequestURL().toString());                 
            }
            checkandMakePersonalFolder(userCode);
            //判断是否是审批人员
            String isauthorizer = String.valueOf(EnvUtils.getAuthorizorIds().contains(userCode));
            if(EnvUtils.isAuthorizer(request)) {
                session.setAttribute(SerachFileController.ISAUTHORIZER, String.valueOf(EnvUtils.isAuthorizer(request)));
            }
            return true;
        } else {
            //用户不存在
            request.setAttribute("error", "usererror");
            logger.error("this login is not user");
            return false;
        }
    }


    //登录的时候，在内外网中创建文件夹，用于存储文件
    private void checkandMakePersonalFolder(String userCode) {
        String O2ISourceDir = EnvUtils.getO2ISourceDir(userCode);
        String I2OSourceDir = EnvUtils.getI2OSourceDir(userCode);
        String O2IDourceDir = EnvUtils.getO2IDestDir(userCode);
        String I2ODourceDir = EnvUtils.getI2ODestDir(userCode);
        File O2ISfile = new File(O2ISourceDir);
        if(!O2ISfile.exists()){
            O2ISfile.mkdirs();
        }
        File I2OSfile = new File(I2OSourceDir);
        if(!I2OSfile.exists()){
            I2OSfile.mkdirs();
        }
        File O2IDfile = new File(O2IDourceDir);
        if(!O2IDfile.exists()){
            O2IDfile.mkdirs();
        }
        File I2ODfile = new File(I2ODourceDir);
        if(!I2ODfile.exists()){
            I2ODfile.mkdirs();
        }
    }
}
