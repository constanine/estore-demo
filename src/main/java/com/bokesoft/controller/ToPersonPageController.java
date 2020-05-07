package com.bokesoft.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.ToPersonPageSerivce;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.ImportMissionBean;
import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @description:用户存在，校验用户是否有登陆权限
 * */
@Controller
public class ToPersonPageController {

    @Autowired
    private ToPersonPageSerivce personPageSerivce;

    @RequestMapping("/authority")
    public String canAuthorized(HttpServletRequest request) {
        //用户存在，从session中取出用户信息
        HttpSession session = request.getSession();
        String userCode = (String) session.getAttribute(SerachFileController.USERCODE);

        String canAuthorized = "no";

        //搜索导入任务、导出任务集合
        List<ImportMissionBean> imMissions = personPageSerivce.findImport(userCode);
        List<ExportMissionBean> exMissions = personPageSerivce.findExport(userCode);


        //如果有审批资格，则启动搜索所有带审批的集合，放入messages中
        if (EnvUtils.isAuthorizer(request)) {
            canAuthorized = "yes";
        }

        List<MessageBean> messages = MIssionMessageUtils.getMissionMessage(userCode,request);

        request.setAttribute("immissions", imMissions);
        request.setAttribute("exmissions", exMissions);
        request.setAttribute(SerachFileController.MESSAGES, messages);
        request.setAttribute("canAuthorized", canAuthorized);

        MIssionMessageUtils.dealMissionMessage(userCode);

        return "main";
    }
}
