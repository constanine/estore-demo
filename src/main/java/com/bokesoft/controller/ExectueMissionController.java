package com.bokesoft.controller;



import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bokesoft.threadPackge.JobContext;
import com.bokesoft.threadPackge.JobUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.ExectueMissionService;
import com.bokesoft.struc.FileBean;
import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.FileLogUtils;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2020/1/9
 * @description:复制到内网/外网(存储文件)
 * */
@Controller
public class ExectueMissionController {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ExectueMissionService exectueMissionService;

    @SuppressWarnings("unchecked")
	@RequestMapping("/exectuemission")
    public String exectueMission(HttpServletRequest request) {

        //从网页上先获取参数
        //获取用户
        String userCode = String.valueOf(request.getSession().getAttribute(SerachFileController.USERCODE));
        List<String> fileinfos = new ArrayList<String>();
        List<String> copyfilelist = new ArrayList<String>();

        //判断是否需要后台迁移文件至中转区
        boolean needmovethread = false;

        //获取任务信息
        HttpSession session = request.getSession();
        long sumFileSize = 0;

        //获取消息
        List<MessageBean> messages = new ArrayList<MessageBean>();
        messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        request.setAttribute(SerachFileController.MESSAGES, messages);

        //开始分流
        //获取分页标项
        String missionType = (String) session.getAttribute(SerachFileController.MISSIONTYPE);
        if (missionType != null) {
            logger.info("MissionType:" + missionType + " to start");
            if("Import".equals(missionType)){
                //获取导入文件信息
                sumFileSize = ((Long) session.getAttribute(SerachFileController.SUMFILESIZE)).longValue();
                logger.info("=============>>filesize:" + sumFileSize);
                List<FileBean> fileStateList = new ArrayList<FileBean>();
                fileStateList = (List<FileBean>) session.getAttribute(SerachFileController.FILESTATELIST);
                int filenumber = fileStateList.size();
                if (filenumber > 0) {
                    //准备任务id
                    String missionid = userCode + "-Im-"
                            + JobUtil.DF.format(new Date());
                    logger.info("write filelistLog..." + missionid
                            + "filelistLog");
                    //判断filelistLog完成，完成之后再进行下一步，以防显示的filelist有损失
                    boolean canNext = false;
                    canNext = FileLogUtils.addImFileList(userCode, missionid,
                            fileStateList);
                    if (canNext) {
                        for (FileBean filestate : fileStateList) {
                            copyfilelist.add(filestate.getPath());
                        }
                        logger.info("acquire copyfilelist:" + copyfilelist);
                        request.setAttribute(SerachFileController.COPYFILELIST, copyfilelist);
                        session.removeAttribute(SerachFileController.FILESTATELIST);
                        request.setAttribute(SerachFileController.FILENUMBER, String.valueOf(copyfilelist.size()));
                        logger.info("=============>>filesize:"
                                + fileStateList.size()
                                + ",copyfilelist.size():" + copyfilelist.size());
                        logger.info("this mission:" + missionid + "starting");

                        //插入数据
                        logger.info("Starting mission:" + missionid
                                + " copying...");
                        Object[] params = {
                                userCode,
                                missionid,
                                EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/" + JobUtil.DFM.format(new Date()) + "/"
                                        + userCode + "/" + missionid
                                        + "-im-filelist.txt",
                                EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/" + JobUtil.DFM.format(new Date()) + "/"
                                        + userCode + "/" + missionid
                                        + "-Import-runlog.txt", filenumber,
                                sumFileSize};
                        logger.info(">>>>writting into sql...");
                        exectueMissionService.insertIm(params);
                        session.setAttribute(SerachFileController.SUMFILESIZE, sumFileSize);
                        session.setAttribute(SerachFileController.MISSIONID, missionid);
                        session.setAttribute(SerachFileController.MISSIONTYPE, missionType);

                        //启动线程执行迁移任务
                        if (canNext
                                && (!JobUtil.isContainSameImpJob(missionid))) {
                            doImport(userCode, missionid, copyfilelist);
                            logger.info("Misssion: " + missionid
                                    + " Copy is completed!");
                        }

                        return "excuating";


                    } else {
                        /*
                         * 如果canNext = FileLogUtils.addFileList(userCode, missionid,
                         * filestatelist);报错，就得考虑第2个页面显示期错误
                         */
                        session.removeAttribute(SerachFileController.SUMFILESIZE);
                        return "redirect:/authority";
                    }
                }

            } else if ("Export".equals(missionType)) {
                // 准备任务id
                String missionid = userCode + "-Ex-" + JobUtil.DF.format(new Date());
                logger.info("write filelistLog..." + missionid + "filelistLog");
                sumFileSize = ((Long) session.getAttribute(SerachFileController.SUMFILESIZE)).longValue();
                // 获取页面信息参数
                String[] filetypes = request.getParameterValues("filetype");
                List<String> filenames = (List<String>) session.getAttribute("filenames");
                List<String> uris = (List<String>) session.getAttribute("uris");

                if (filetypes != null) {
                    for (int i = 0; i < filetypes.length; i++) {
                        fileinfos.add(filetypes[i] + "\t" + filenames.get(i) + "\t"
                                + uris.get(i));
                        if ("file".equals(filetypes[i])) {
                            needmovethread = true;
                        }
                    }

                    //启动线程执行迁移任务
                    logger.info("Starting mission:" + missionid + " copying...");

                    FileLogUtils.addExportFileListMethod(userCode, missionid,
                            fileinfos);
                    copyfilelist = fileinfos;

                    //写入数据库
                    if (!needmovethread) {
                        Object[] params = {
                                userCode,
                                missionid,
                                new Date(),
                                sumFileSize,
                                "finished",
                                EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/" + JobUtil.DFM.format(new Date()) + "/"
                                        + userCode + "/" + missionid
                                        + "-ex-filelist.txt",
                                "just url list ,no log",
                                fileinfos.size(),
                                "ready_scan"};
                        exectueMissionService.insertEx(params);
                    } else {
                        Object[] params = {
                                userCode,
                                missionid,
                                new Date(),
                                sumFileSize,
                                EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/" + JobUtil.DFM.format(new Date()) + "/"
                                        + userCode + "/" + missionid
                                        + "-ex-filelist.txt",
                                fileinfos.size(),
                                "ready_scan"};
                        exectueMissionService.insertEx2(params);
                    }
                }
                request.setAttribute(SerachFileController.FILENUMBER,
                        String.valueOf(copyfilelist.size()));
                session.setAttribute(SerachFileController.SUMFILESIZE, sumFileSize);
                session.setAttribute(SerachFileController.MISSIONID, missionid);
                session.setAttribute(SerachFileController.MISSIONTYPE, missionType);
                request.setAttribute(SerachFileController.COPYFILELIST, copyfilelist);

                if (needmovethread) {
                    doTransport(userCode, missionid, fileinfos);
                }

                return "excuating";

            } else {
                return "redirect:/authority";
            }
        }
        return null;
    }


    private void doImport(String userCode, String missionid,
                          List<String> copyfilelist) {
        JobContext jobContext = new JobContext();
        if(!JobUtil.isContainSameImpJob(missionid)) {
            jobContext.setMissionid(missionid);
            jobContext.setUserCode(userCode);
            jobContext.setStop(false);
            jobContext.setFileList(copyfilelist);
            JobUtil.createImpJob(jobContext);
        }
    }

    private void doTransport(String userCode, String missionid,
                             List<String> copyfilelist) {
        JobContext jobContext = new JobContext();
        if(!JobUtil.isContainSameTransportJob(missionid)) {
            jobContext.setMissionid(missionid);
            jobContext.setUserCode(userCode);
            jobContext.setStop(false);
            jobContext.setFileList(copyfilelist);
            JobUtil.createTransportJob(jobContext);
        }
    }
}

























