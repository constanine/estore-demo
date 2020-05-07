package com.bokesoft.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.struc.MessageBean;
import com.bokesoft.threadPackge.JobUtil;
import com.bokesoft.utils.FileLogUtils;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2020/1/9
 * @description:刷新状态
 * */
@Controller
public class CheckMissionProgressController {

	private Logger logger = Logger.getLogger(this.getClass());

	@RequestMapping("/checkmissionprogress")
    public String checkMissionProgress(HttpServletRequest request) {

        logger.info("check mission.....");
        HttpSession session = request.getSession();
        String userCode = (String) session.getAttribute(SerachFileController.USERCODE);
        String missionid = (String) session.getAttribute(SerachFileController.MISSIONID);
        String missionType = (String) session.getAttribute(SerachFileController.MISSIONTYPE);
        List<String> copyfilelist = new ArrayList<String>();
        List<String> result = new ArrayList<String>();

        //获取消息
        List<MessageBean> messages = new ArrayList<MessageBean>();
        messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        request.setAttribute(SerachFileController.MESSAGES, messages);


        if (missionid != null) {
            logger.info(" missionid:" + missionid);
            if ("Import".equals(missionType)) {

                copyfilelist = FileLogUtils.serachImFileListFromLog(userCode,
                        missionid);
                Map<String,Object> fileData = JobUtil.getDataImp(missionid);
                //这步操作主要是文件特别多的时候，此时还在执行Import线程，这时候任务还没结束，我们进行刷新操作，页面需要显示的数据从Import线程中去获取
                // （如果Import操作结束了，移除jobCache_IMP中的missionid,这是就执行后面的finish的操作）
                if(null != fileData){
                    String finishedFileNumber = String.valueOf(fileData.get("finishedFileNumber"));
                    String currentFile = String.valueOf(fileData.get("currentFile"));
                    String finishedFileSize = String.valueOf(fileData.get("finishedfilesize"));

                    result.add(currentFile);
                    result.add(finishedFileNumber);
                    result.add(finishedFileSize);
                }
            } else if ("Export".equals(missionType)) {
                copyfilelist = FileLogUtils.serachExportFileListFromLog(userCode,
                        missionid);
                Map<String,Object> fileData = JobUtil.getDataTrasport(missionid);
                if(null != fileData){
                    String finishedFileNumber = String.valueOf(fileData.get("finishedFileNumber"));
                    String currentFile = String.valueOf(fileData.get("currentFile"));
                    String finishedFileSize = String.valueOf(fileData.get("finishedfilesize"));
                    result.add(currentFile);
                    result.add(finishedFileNumber);
                    result.add(finishedFileSize);
                }
            }
            logger.info("User: " + userCode + " check mission: " + missionid
                    + " ,copyfilelist size:" + copyfilelist.size() + "," + missionType);

            if (result != null && result.size() > 0) {
                request.setAttribute("result", result);
            } else {
                long sumfilesize = ((Long) session.getAttribute(SerachFileController.SUMFILESIZE)).longValue();
                request.setAttribute("misfinish", "finished");
                request.setAttribute(SerachFileController.MISSIONID, missionid);
                session.removeAttribute(SerachFileController.SUMFILESIZE);
                session.setAttribute(SerachFileController.SUMFILESIZE, sumfilesize);
                session.removeAttribute(SerachFileController.MISSIONID);
            }
            request.setAttribute(SerachFileController.COPYFILELIST, copyfilelist);
            request.setAttribute(SerachFileController.FILENUMBER,
                    String.valueOf(copyfilelist.size()));

            return "excuating";

        } else {
            logger.info(">>>There is no mission");
            return "redirect:/authority";

        }
    }

}
