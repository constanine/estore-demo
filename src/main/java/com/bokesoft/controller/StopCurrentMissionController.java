package com.bokesoft.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bokesoft.threadPackge.JobUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.FileLogUtils;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2020/1/9
 * @description:停止当前任务
 * */
@Controller
public class StopCurrentMissionController {

    private Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/stopcurrentmission")
    public String stopCurrentMission(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String missionid = (String) session.getAttribute(SerachFileController.MISSIONID);
        String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);
        String missionType = (String) session.getAttribute(SerachFileController.MISSIONTYPE);

        //获取消息
        List<MessageBean> messages = new ArrayList<MessageBean>();
        messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        request.setAttribute(SerachFileController.MESSAGES, messages);

        if (missionid != null) {
            logger.info("missionid:" + missionid);

            // 每次请求任务列表时，应该通过当前的任务号找到filelog文件提取filelist
            List<String> copyfileList = new ArrayList<String>();

            if ("import".equalsIgnoreCase(missionType)) {//如果是导入任务
                copyfileList = FileLogUtils.serachImFileListFromLog(userCode, missionid);
                if (JobUtil.isContainSameImpJob(missionid)) {
                    JobUtil.setStopImp(missionid);
                    session.removeAttribute(SerachFileController.MISSIONID);
                    request.setAttribute(SerachFileController.MISSIONID, missionid);
                    //如果线程再说明会等待当前导入任务，然后停止整个导入任务
                    request.setAttribute(SerachFileController.MISFINISH, "stopping");
                } else {
                    //如果线程被killed了。说明当前的整个导入任务已经结束
                    request.setAttribute(SerachFileController.MISFINISH, "stopped");
                    session.removeAttribute(SerachFileController.SUMFILESIZE);
                }
            } else if ("export".equalsIgnoreCase(missionType)) {//如果是到外部
                copyfileList = FileLogUtils.serachExportFileListFromLog(userCode, missionid);
                if (JobUtil.isContainSameTransportJob(missionid)) {
                    JobUtil.setStopTransport(missionid);
                    session.removeAttribute(SerachFileController.MISSIONID);
                    request.setAttribute(SerachFileController.MISSIONID, missionid);
                    //如果线程再说明会等待当前导入任务，然后停止整个导入任务
                    request.setAttribute(SerachFileController.MISFINISH, "stopping");
                } else {
                    //如果线程被killed了。说明当前的整个导入任务已经结束
                    request.setAttribute(SerachFileController.MISFINISH, "stopped");
                    session.removeAttribute("needmovenumber");
                    session.removeAttribute(SerachFileController.SUMFILESIZE);
                }
            }


            request.setAttribute(SerachFileController.COPYFILELIST, copyfileList);
            request.setAttribute(SerachFileController.FILENUMBER, String.valueOf(copyfileList.size()));
            return "excuating";
        } else {
            return "redirect:/authority";

        }
    }

}


