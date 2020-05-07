package com.bokesoft.threadPackge;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bokesoft.StartApplication;
import com.bokesoft.struc.StatusBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.RunLogUtils;

/**
 * 
 * @class 当审批驳回时，中转区导回至内部的文件迁移程序
 *
 */
public class Tran_BackThread implements Runnable {

	/**
	 * @param fileList         ，经查询传过来的文件列表
	 * @param targetUrl        有界面传入的目标地址
	 * @param mission          当前复制任务编号
	 * @param hasError         用来当复制异常时，装入异常内容，传入runlog中
	 * @param currentFile      当前进行迁移的文件
	 * @param currentfilesize  当前迁移完的文件大小
	 * @param finishedfilesize 已完成迁移的文件总大小
	 * @param canDoMove        根据这个boolean 值判断是否能够继续迁移
	 */
	private static JdbcTemplate getJdbcTemplate() {
		return StartApplication.getBean(JdbcTemplate.class);
	}

	private boolean hasError = false;
	private String currentFile;
	private int finishedFileNumber = 0;
	private long currentfilesize = 0;
	private long finishedfilesize = 0;

	private JobContext jobcontext;

	public Tran_BackThread(JobContext jobcontext) {
		this.jobcontext = jobcontext;
	}

	public String getCurrentFile() {
		return currentFile;
	}

	public long getFinishedfilesize() {
		return finishedfilesize;
	}

	public int getFinishedFileNumber() {
		return finishedFileNumber;
	}

	public void run() {

		jobcontext.setStatus(StatusBean.RUNNING);

		Logger runlog = RunLogUtils.prepareLogsFolder(
				jobcontext.getUserCode(), EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/"
						+ JobUtil.DFM.format(new Date()) + "/" + jobcontext.getUserCode() + "/",
				jobcontext.getMissionid(), "Tran_Back");
		runlog.info(">>>>>>>>>>>" + jobcontext.getMissionid() + " start...<<<<<<<<<<<<<");
		String targetUrl;

		String sourceFolderurl = EnvUtils.getTransferDisk() + "/" + jobcontext.getUserCode();
		targetUrl = EnvUtils.getI2OSourceDir(jobcontext.getUserCode());
		runlog.info("sourceurl is " + sourceFolderurl + ",targetUrl is " + targetUrl);
		// log同步
		FileWirterTool fw = new FileWirterTool();
		fw.setLogger(runlog);

		// start先将基础数据写入数据库
		runlog.info(">>>>>no write insert sql");
		String sql = " update exmissiontable set exportstep2runlogUrl=? , step2state=? where missionid like ?";
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Object[] params = {
				EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/" + JobUtil.DFM.format(new Date())
						+ "/" + jobcontext.getUserCode() + "/" + jobcontext.getMissionid() + "-Tran_Back-runlog.txt",
				"running", jobcontext.getMissionid() };

		getJdbcTemplate().update(sql, params);
		// 记录任务开始消息
		String msgsql2 = "update messagetable set MissionState=? , reportTime=?, MessageState=? where missionid =?";

		for (int i = 0; i < jobcontext.getFileList().size(); i++) {

			if (JobUtil.jobCache_TBT.get(jobcontext.getMissionid()).getStop()) {
				// 如果借测到不能isCanDoMove为false,则停止线程循环

				break;
			}
			// 判断其任务文件类型
			String[] fileinfos = jobcontext.getFileList().get(i).split("\t");

			// 如果是file类型
			if ("file".equals(fileinfos[0])) {
				// 每次循环都重置下targetUrl
				targetUrl = EnvUtils.getI2OSourceDir(jobcontext.getUserCode());
				currentFile = fileinfos[1];
				runlog.info(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + "folder...");
				targetUrl = targetUrl + File.separator + currentFile;

				// 直接将文件从source移动到target,即复制与删除同时进行
				fw.moveFileAndFolder(EnvUtils.getTransferDisk() + "/" + jobcontext.getUserCode() + "/"
						+ jobcontext.getMissionid() + "/" + fileinfos[1], targetUrl);
			}

			// 如果表错则写入runlog里
			if (!fw.getIsFinish()) {
				runlog.error(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + " is unfinished");
				runlog.error(fw.getWhy());
				hasError = true;
				break;
			}
			finishedFileNumber = i + 1;
			currentfilesize = fw.getFilesize();
			finishedfilesize = finishedfilesize + currentfilesize;
			runlog.info(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + " is finished");
		}

		// 防止空文件夹。最后清除sourceURl下所有空文件夹
		// 如果有文件夹有文件则停止删除

		if (!hasError) {
			// 如果hasError为0，说明全部都完成了，没有错误

			// 同时sql记录停止时间，状态改为stopped
			if (JobUtil.jobCache_TBT.get(jobcontext.getMissionid()).getStop()) {
				runlog.info(jobcontext.getUserCode() + " has stop it" + new Date());
				sql = "update  exmissiontable set endTime=? , step2state=? where missionid like ?";
				String endTime = df.format(new Date());
				Object[] stopparams = { endTime, "stopped", jobcontext.getMissionid() };
				getJdbcTemplate().update(sql, stopparams);
				// msgsql记录停止消息
				Object[] msgparamsstop = { "step2stopped", df.format(new Date()), "new", jobcontext.getMissionid() };
				getJdbcTemplate().update(msgsql2, msgparamsstop);
				jobcontext.setStatus(StatusBean.STOPPED);
			} else {
				// runlog.info("all is finish");
				// 且最后sql中记录完成时间，状态改为finished
				runlog.info(">>> Move missions are all finished");
				sql = "update exmissiontable set endTime=? , step2state=? where missionid like ?";
				String endTime = df.format(new Date());
				Object[] finparams = { endTime, "finished", jobcontext.getMissionid() };
				getJdbcTemplate().update(sql, finparams);
				// msgsql记录完成消息
				Object[] msgparamsfinish = { "step2finished", df.format(new Date()), "new", jobcontext.getMissionid() };
				getJdbcTemplate().update(msgsql2, msgparamsfinish);
				jobcontext.setStatus(StatusBean.FINISHED);
			}
		} else {
			sql = "update  exmissiontable set endTime=? , step2state=? where missionid = ?";
			String endTime = df.format(new Date());
			Object[] errorparams = { endTime, "error", jobcontext.getMissionid() };
			getJdbcTemplate().update(sql, errorparams);
			// msgsql记录错误消息
			Object[] msgparamserror = { "step2error", df.format(new Date()), "new", jobcontext.getMissionid() };
			getJdbcTemplate().update(msgsql2, msgparamserror);
			jobcontext.setStatus(StatusBean.ERROR);
		}
		JobUtil.jobCache_TBT.remove(jobcontext.getMissionid());
	}
}
