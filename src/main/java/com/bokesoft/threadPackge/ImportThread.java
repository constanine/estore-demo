package com.bokesoft.threadPackge;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bokesoft.StartApplication;
import com.bokesoft.struc.StatusBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.RunLogUtils;

/**
 * 
 * @class 用于外部导入时用的文件迁移线程
 */
public class ImportThread implements Runnable {

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

	public ImportThread(JobContext jobcontext) {
		this.jobcontext = jobcontext;
	}

	public void run() {
		jobcontext.setStatus(StatusBean.RUNNING);
		// mission 调用
		Logger runlog = RunLogUtils.prepareLogsFolder(
				jobcontext.getUserCode(), EnvUtils.getTaskDisk() + "/" + JobUtil.DFY.format(new Date()) + "/"
						+ JobUtil.DFM.format(new Date()) + "/" + jobcontext.getUserCode(),
				jobcontext.getMissionid(), "Import");
		runlog.info(">>>>>>>>>>>" + jobcontext.getMissionid() + " start...<<<<<<<<<<<<<");
		String targetUrl;

		String sourceFolderurl = EnvUtils.getO2ISourceDir(jobcontext.getUserCode());
		targetUrl = EnvUtils.getO2IDestDir(jobcontext.getUserCode());

		// log同步
		FileWirterTool fw = new FileWirterTool();
		fw.setLogger(runlog);

		// start先将基础数据写入数据库
		runlog.info(">>>>>no write insert sql");
		String sql = " update immissiontable set startTime=? , state=? where missionid like ?";
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String startTime = df.format(new Date());
		Object[] params = { startTime, "running", jobcontext.getMissionid() };
		runlog.info("sourceurl is " + sourceFolderurl + ",targetUrl is " + targetUrl);
		getJdbcTemplate().update(sql, params);
		// msgsql记录开始消息
		String msgsql = "insert into messagetable (missionid,MissionState,reportTime,MessageState,userCode) values (?,?,?,?,?)";
		Object[] msgparams1 = { jobcontext.getMissionid(), df.format(new Date()), "running", "new",
				jobcontext.getUserCode() };
		getJdbcTemplate().update(msgsql, msgparams1);
		String msgsql2 = "update messagetable set MissionState=? , reportTime=?, MessageState=? where missionid =?";

		for (int i = 0; i < jobcontext.getFileList().size(); i++) {
			runlog.info(">>>now loop for moving file...");
			if (JobUtil.jobCache_IMP.get(jobcontext.getMissionid()).getStop()) {
				// 如果借测到不能isCanDoMove为false,则停止线程循环

				break;
			}

			// 每次循环都重置下targetUrl
			targetUrl = EnvUtils.getO2IDestDir(jobcontext.getUserCode());
			currentFile = jobcontext.getFileList().get(i);
			runlog.info(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + "folder...");
			targetUrl = targetUrl + File.separator
					+ (jobcontext.getFileList().get(i).substring(sourceFolderurl.length()));

			// 直接将文件从source移动到target,即复制与删除同时进行
			fw.moveFileAndFolder(jobcontext.getFileList().get(i), targetUrl);

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
			Map<String, Object> map = new HashMap<>();
			map.put("currentFile", currentFile);
			map.put("finishedFileNumber", finishedFileNumber);
			map.put("finishedfilesize", finishedfilesize);
			JobUtil.jobCache_IMP.get(jobcontext.getMissionid()).setData(map);

			runlog.info(">>> Move " + jobcontext.getFileList().get(i) + " to " + targetUrl + " is finished");
		}

		// 防止空文件夹。最后清除sourceURl下所有空文件夹
		// 如果有文件夹有文件则停止删除

		if (!hasError) {
			// 如果hasError为0，说明全部都完成了，没有错误

			// 同时sql记录停止时间，状态改为stopped
			if (JobUtil.jobCache_IMP.get(jobcontext.getMissionid()).getStop()) {
				runlog.info(jobcontext.getUserCode() + " has stop it" + new Date());
				sql = "update  immissiontable set endTime=? , state=? where missionid = ?";
				String endTime = df.format(new Date());
				Object[] stopparams = { endTime, "stopped", jobcontext.getMissionid() };
				getJdbcTemplate().update(sql, stopparams);
				// msgsql记录停止消息
				Object[] msgparamsstop = { "stopped", df.format(new Date()), "new", jobcontext.getMissionid() };
				getJdbcTemplate().update(msgsql2, msgparamsstop);
				jobcontext.setStatus(StatusBean.STOPPED);

			} else {
				// runlog.info("all is finish");
				// 且最后sql中记录完成时间，状态改为finished
				runlog.info(">>> Move missions are all finished");
				sql = "update  immissiontable set endTime=? , state=?  where missionid = ?";
				String endTime = df.format(new Date());
				Object[] finparams = { endTime, "finished", jobcontext.getMissionid() };
				getJdbcTemplate().update(sql, finparams);
				// msgsql记录完成消息
				Object[] msgparamsfinish = { "finished", df.format(new Date()), "new", jobcontext.getMissionid() };
				getJdbcTemplate().update(msgsql2, msgparamsfinish);
				jobcontext.setStatus(StatusBean.FINISHED);

			}
		} else {
			sql = "update  immissiontable set endTime=? , state=? where missionid = ?";
			String endTime = df.format(new Date());
			Object[] errorparams = { endTime, "error", jobcontext.getMissionid() };
			getJdbcTemplate().update(sql, errorparams);
			// msgsql记录错误消息
			Object[] msgparamserror = { "error", df.format(new Date()), "new", jobcontext.getMissionid() };
			getJdbcTemplate().update(msgsql2, msgparamserror);
			jobcontext.setStatus(StatusBean.ERROR);
		}
		JobUtil.jobCache_IMP.remove(jobcontext.getMissionid());

	}

}
