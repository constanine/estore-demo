package com.bokesoft.threadPackge;

import com.bokesoft.StartApplication;
import com.bokesoft.struc.StatusBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.RunLogUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

//迁移中转线程

/**
 * @class 有内部导出至中转区的文件迁移程序
 */
public class TransportThread implements Runnable {

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
	private static JdbcTemplate getJdbctemplate() {
		return StartApplication.getBean(JdbcTemplate.class);
	}

	private boolean hasError = false;
	private String currentFile;
	private int finishedFileNumber = 0;
	private long currentfilesize = 0;
	private long finishedfilesize = 0;

	private JobContext jobcontext;

	public TransportThread(JobContext jobcontext) {
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
		// mission 调用
		Logger runlog = RunLogUtils.prepareLogsFolder(
				jobcontext.getUserCode(), EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/"
						+ JobUtil.DFM.format(new Date()) + "/" + jobcontext.getUserCode(),
				jobcontext.getMissionid(), "To_Tran");
		runlog.info(">>>>>>>>>>>" + jobcontext.getMissionid() + " start...<<<<<<<<<<<<<");
		String targetUrl;

		String sourceFolderurl = EnvUtils.getI2OSourceDir(jobcontext.getUserCode());
		targetUrl = EnvUtils.getTransferDisk() + File.separator + jobcontext.getUserCode();
		runlog.info("sourceurl is " + sourceFolderurl + ",targetUrl is " + targetUrl);
		// log同步
		FileWirterTool fw = new FileWirterTool();
		fw.setLogger(runlog);

		// start先将基础数据写入数据库
		runlog.info(">>>>>no write insert sql");
		String sql = " update exmissiontable set step1state=? where missionid like ?";
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Object[] params = { "running", jobcontext.getMissionid() };
		getJdbctemplate().update(sql, params);
		// 记录任务开始消息
		String msgsql = "insert into messagetable (missionid,MissionState,reportTime,MessageState,userCode) values (?,?,?,?,?)";
		Object[] msgparams1 = { jobcontext.getMissionid(), "step1running", df.format(new Date()), "new",
				jobcontext.getUserCode() };
		getJdbctemplate().update(msgsql, msgparams1);
		String msgsql2 = "update messagetable set MissionState=? , reportTime=? , MessageState=? where missionid =? ";

		for (int i = 0; i < jobcontext.getFileList().size(); i++) {

			if (JobUtil.jobCache_TRANSPORT.get(jobcontext.getMissionid()).getStop()) {
				// 如果检测到isCanDoMove为false,则停止线程循环

				break;
			}

			// 每次循环都重置下targetUrl
			targetUrl = EnvUtils.getTransferDisk() + File.separator + jobcontext.getUserCode() + "/"
					+ jobcontext.getMissionid();

			String[] fileinfos = jobcontext.getFileList().get(i).split("\t");
			// 如果是file类型
			if ("file".equals(fileinfos[0]) || "folder".equals(fileinfos[0])) {
				currentFile = fileinfos[1];
				runlog.info(">>> Move " + fileinfos[1] + " to " + targetUrl + "folder...");
				targetUrl = targetUrl + File.separator + fileinfos[1];

				// 直接将文件从source移动到target,即复制与删除同时进行
				fw.moveFileAndFolder(fileinfos[2], targetUrl);

				// 如果表错则写入runlog里
				if (!fw.getIsFinish()) {
					runlog.error(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + " is unfinished");
					runlog.error(fw.getWhy());
					hasError=true;
					break;
				}
				finishedFileNumber = i + 1;
				currentfilesize = fw.getFilesize();
				finishedfilesize = finishedfilesize + currentfilesize;
				Map<String, Object> map = new HashMap<>();
				map.put("currentFile", currentFile);
				map.put("finishedFileNumber", finishedFileNumber);
				map.put("finishedfilesize", finishedfilesize);
				JobUtil.jobCache_TRANSPORT.get(jobcontext.getMissionid()).setData(map);
				runlog.info(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + " is finished");
			}
		}
		// 防止空文件夹。最后清除sourceURl下所有空文件夹
		// 如果有文件夹有文件则停止删除

		if (!hasError) {
			// 如果hasError为0，说明全部都完成了，没有错误

			// 同时sql记录停止时间，状态改为stopped
			if (JobUtil.jobCache_TRANSPORT.get(jobcontext.getMissionid()).getStop()) {
				runlog.info(jobcontext.getUserCode() + " has stop it" + new Date());
				sql = "update  exmissiontable set  step1_state=?, exportstep1runlogUrl=? where missionid = ?";
				Object[] stopparams = { "stopped",
						EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/"
								+ JobUtil.DFM.format(new Date()) + "/" + jobcontext.getUserCode() + "/"
								+ jobcontext.getMissionid() + "-To_Tran-runlog.txt",
						jobcontext.getMissionid() };
				getJdbctemplate().update(sql, stopparams);
				// msgsql记录停止消息
				Object[] msgparamsstop = { "step1stopped", df.format(new Date()), "new", jobcontext.getMissionid() };
				getJdbctemplate().update(msgsql2, msgparamsstop);
				jobcontext.setStatus(StatusBean.STOPPED);
			} else {
				// runlog.info("all is finish");
				// 且最后sql中记录完成时间，状态改为finished
				runlog.info(">>> Move missions are all finished.ready for scanner");
				sql = "update  exmissiontable set step1state=? ,exportstep1runlogUrl =?  , judgement=? where missionid = ?";
				Object[] finparams = { "finished",
						EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/"
								+ JobUtil.DFM.format(new Date()) + "/" + jobcontext.getUserCode() + "/"
								+ jobcontext.getMissionid() + "-To_Tran-runlog.txt",
						"ready_scan", jobcontext.getMissionid() };
				getJdbctemplate().update(sql, finparams);
				// msgsql记录完成消息
				Object[] msgparamsfinish = { "step1finished", df.format(new Date()), "new", jobcontext.getMissionid() };
				getJdbctemplate().update(msgsql2, msgparamsfinish);
				jobcontext.setStatus(StatusBean.FINISHED);
			}
		} else {
			sql = "update  exmissiontable set step1state=? ,exportstep1runlogUrl=? where missionid = ?";
			Object[] errorparams = { "error",
					EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/" + JobUtil.DFM.format(new Date())
							+ "/" + jobcontext.getUserCode() + "/" + jobcontext.getMissionid() + "-To_Tran-runlog.txt",
					jobcontext.getMissionid() };
			getJdbctemplate().update(sql, errorparams);
			// msgsql记录错误消息
			Object[] msgparamserror = { "step1error", df.format(new Date()), "new", jobcontext.getMissionid() };
			getJdbctemplate().update(msgsql2, msgparamserror);
			jobcontext.setStatus(StatusBean.ERROR);
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// do nothing
		}
		JobUtil.jobCache_TRANSPORT.remove(jobcontext.getMissionid());
	}

}
