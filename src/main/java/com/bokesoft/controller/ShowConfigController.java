package com.bokesoft.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2019/12/27
 * @description:用来显示当前用户的配置
 * */
@Controller
public class ShowConfigController {

    @RequestMapping("/showconfig")
    public String showConfig(HttpServletRequest request) {

        List<MessageBean> messages = new ArrayList<>();
        Map<String, String> configmap = new HashMap<>();
        HttpSession session = request.getSession();
        String userCode = String.valueOf(session.getAttribute(SerachFileController.USERCODE));

        //判断是否为审批人员，如果是审批人员，则加入到list中
        if (EnvUtils.isAuthorizer(request)) {
            messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        }
        //外网的ip
        String outDiskIp = EnvUtils.getOutDiskIp();
        //内网的ip
        String inDiskIp = EnvUtils.getInDiskIp();
        String o2ISourceDir = EnvUtils.getO2ISourceDir(userCode); // 外网 to-内网
        String i2OSourceDir = EnvUtils.getI2OSourceDir(userCode); //内网  to-外网
        String i2ODestDir = EnvUtils.getI2ODestDir(userCode); //外网 from-内网
        String o2IDestDir = EnvUtils.getO2IDestDir(userCode); //内网 from-外网

        String transferDisk = EnvUtils.getTransferDisk();
        String taskDisk = EnvUtils.getTaskDisk();


        configmap.put("o2ISourceDir", outDiskIp + "/" + o2ISourceDir); //外网 to-内网
        configmap.put("i2OSourceDir", inDiskIp + "/" + i2OSourceDir);  //内网 to-外网
        configmap.put("i2ODestDir", outDiskIp + "/" + i2ODestDir);     //外网  from-内网
        configmap.put("o2IDestDir", inDiskIp + "/" + o2IDestDir);      //内网  from-外网

        configmap.put("transferDisk", transferDisk);
        request.setAttribute("taskDisk", taskDisk);
        request.setAttribute("configmap", configmap);
        request.setAttribute(SerachFileController.MESSAGES, messages);

        return "configPage";
    }
}


























