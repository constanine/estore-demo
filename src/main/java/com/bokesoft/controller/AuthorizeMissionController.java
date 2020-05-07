package com.bokesoft.controller;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bokesoft.threadPackge.JobUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.AuthorizeMissionService;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.RunLogUtils;

/*
 * @author:gushiming
 * @date:2020/1/6
 * @description:审批通过/驳回
 * */
@Controller
public class AuthorizeMissionController {

    @Autowired
    private AuthorizeMissionService authorizeMissionService;

    @RequestMapping("/authorizemission")
    public String authorizeMission(HttpServletRequest request) throws SQLException {
        //获取页面元素信息
        HttpSession session = request.getSession();
        String userCode = String.valueOf(session.getAttribute(SerachFileController.USERCODE));
        if (!EnvUtils.isAuthorizer(request)) {
            throw new RuntimeException("userCode:" + userCode + " no rights to authorizeMission!");
        }
        String result = request.getParameter("result");
        String missionid = request.getParameter(SerachFileController.MISSIONID);
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String authorizdTime = df.format(new Date());
        //准备日志
        String folderUrl = EnvUtils.getTaskDisk() + "/" + JobUtil.DFYMD.format(new Date()) + "/" + userCode;
        Logger logger = RunLogUtils.prepareLogsFolder(userCode, folderUrl, missionid, "Authorized");

        Object[] params = null;
        Object[] msgparamsstop = null;

        if ("yes".equals(result)) {
            logger.info("mission:" + missionid + " is authorized by " + userCode);
            params = new Object[]{userCode, "yes", authorizdTime, "auto_do", folderUrl + "/" + missionid + "-Authorized-log.txt", missionid};
            //msgsql记录审批通过消息
            msgparamsstop = new Object[]{"passed", df.format(new Date()), "new", missionid};
        } else if ("no".equals(result)) {
            logger.info("mission:" + missionid + " is rejected by " + userCode);
            params = new Object[]{userCode, "no", authorizdTime, "delete", folderUrl + "/" + missionid + "-Authorized-log.txt", missionid};
            //msgsql记录审批驳回消息
            msgparamsstop = new Object[]{"rejected", df.format(new Date()), "new", missionid};
        }

        //审批之后，启动导出或导回的任务
        authorizeMissionService.update1(params);
        authorizeMissionService.update2(msgparamsstop);
        ExportMissionBean mission = authorizeMissionService.findExportMission(missionid);
        ToDoExportStep2Controller.exportMissionStep2Core(mission.getUserCode(), mission);
        return "redirect:/toauthorizepage";
    }

}
















