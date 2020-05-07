package com.bokesoft.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.ScanmissionService;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.utils.FileLogUtils;
import com.bokesoft.utils.ScanUtils;

/*
 * @author:gushiming
 * @date:2020/1/2
 * @description: 触发任务扫描
 * */
@Controller
public class ScanmissionController {
	private static final DateFormat DATA_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ScanmissionService scanmissionService;

	@RequestMapping("/scanmission")
	public String scanmission(HttpServletRequest request) {
		// 初始化元素
		logger.info("Sacnning missions....");
		String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);

		// 先获得扫描Mission的信息
		List<ExportMissionBean> missions = scanmissionService.find();
		List<String> missionIds = new ArrayList<String>();
		// 执行扫描并更新
		for (ExportMissionBean mission : missions) {
			missionIds.add(mission.getMissionid());
			String missionUser = mission.getUserCode();
			if (StringUtils.isNotBlank(userCode)) {
				missionUser = userCode;
			}
			try {
				scanMissionCore(missionUser, mission);
			}catch (Exception e){
				logger.error("mission"+mission.getMissionid()+",can't been scanned!");
			}
		}
		logger.info("mission need auth:" + missionIds);
		logger.info("scan has completed!");

		if (StringUtils.isNotBlank(userCode)) {
			return "redirect:/authority";
		} else {
			request.setAttribute("missions", missions);
			return "scanResult";
		}
	}

	private void scanMissionCore(String missionUser, ExportMissionBean mission) {
		List<String> copyfilelist;
		boolean canAutoMove;
		String missionid = mission.getMissionid();
		copyfilelist = FileLogUtils.serachExportFileListFromLog(missionUser, missionid);
		canAutoMove = true;
		String value = "auto_move";
		canAutoMove = ScanUtils.decideToAutoMove(copyfilelist, missionUser, missionid);
		if (canAutoMove) {
			scanmissionService.updatedata1("auto_do", missionid);
			mission.setJudgement("auto_do");
			scanmissionService.updatedata2(value, DATA_FORMAT.format(new Date()), "new", missionid);
		} else {
			value = "wait_authorized";
			scanmissionService.updatedata3("wait_auth", "wait_auth", missionid);
			// 记录待审批
			mission.setJudgement("wait_auth");
			scanmissionService.updatedata2(value, DATA_FORMAT.format(new Date()), "new", missionid);
		}
	}
}
