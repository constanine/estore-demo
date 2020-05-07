package com.bokesoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.ToDoExportStep2Service;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.threadPackge.JobContext;
import com.bokesoft.threadPackge.JobUtil;
import com.bokesoft.utils.FileLogUtils;

/*
 * @author:gushiming
 * @date:2020/1/3
 * @description:触发文件复制功能
 * */
@Controller
public class ToDoExportStep2Controller {

	private static Logger logger = Logger.getLogger(ToDoExportStep2Controller.class);
	@Autowired
	private ToDoExportStep2Service exportStep2Service;

	@RequestMapping("/toexportstep2")
	public String todoExportStep2(HttpServletRequest request) {
		String userCode = String.valueOf(request.getSession().getAttribute(SerachFileController.USERCODE));

		logger.info(">>>auto moving file...");
		// 获得要自动运行的Mission信息
		Object[] params = { "auto_do" };
		logger.info("sql params:" + String.valueOf(params[0]));
		List<ExportMissionBean> missions = exportStep2Service.find("auto_do");

		// 循环执行所有可运行的任务
		logger.info("Need execute mssions number:" + missions.size());
		for (ExportMissionBean mission : missions) {
			String missionUser = mission.getUserCode();
			if (StringUtils.isNotBlank(userCode)) {
				missionUser = userCode;
			}
			exportMissionStep2Core(missionUser, mission);
		}

		if (userCode != null) {
			return "redirect:/authority";

		} else {
			request.setAttribute(SerachFileController.MISSIONS, missions);
			return "showExport2page";
		}
	}

	//复制到外网的第二步
	static void exportMissionStep2Core(String missionUser, ExportMissionBean mission) {
		if (!missionUser.equals(mission.getUserCode())) {
			return;
		}
		List<String> copyfilelist;
		String missionid = mission.getMissionid();
		String authorizedstate = mission.getAuthorizeState();
		// 被驳回的任务，执行back线程
		if ("no".equals(authorizedstate)) {
			copyfilelist = FileLogUtils.serachExportFileListFromLog(missionUser, missionid);
			JobContext jobContext = new JobContext();
			if (!(JobUtil.isContainSameTBTJob(missionid))) {
				jobContext.setMissionid(missionid);
				jobContext.setUserCode(missionUser);
				jobContext.setStop(false);
				jobContext.setFileList(copyfilelist);
				JobUtil.createTBTJob(jobContext);
			}
		} else {// 通过审批和自动迁移的任务执行toOutExport线程
			copyfilelist = FileLogUtils.serachExportFileListFromLog(missionUser, missionid);
			if (!(JobUtil.isContainSame2outJob(missionid))) {
				JobContext jobContext = new JobContext();
				jobContext.setMissionid(missionid);
				jobContext.setUserCode(missionUser);
                jobContext.setStop(false);
                jobContext.setFileList(copyfilelist);
			    JobUtil.create2outJob(jobContext);
			}else {
				logger.info("Mission:"+missionid+" is running");
			}
		}
	}
}
