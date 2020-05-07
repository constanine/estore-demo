package com.bokesoft.struc;


/** 
 * @OperatedBean 为历史的操作记录bean
 * @param id
 * @param user 提交者
 * @param missionid 提交的任务编号
 * @param startTime 提交任务的时间
 * @param state 任务状态：runnable,finished,stoped
 * @param endTime 任务结束的时间：完成或停止
 * @param filelogUrl filelistlog-文件存放路径
 * @param runlogUrl runlog-文件存放路径
 * @param sumFileNumber 任务中文件总数
 * @param sumFileSize 任务中文件总大小
 */

public class ImportMissionBean {
	private long id;
	private String userCode;
	private String missionid;
	private String startTime;
	private String state;
	private String endTime;
	private String filelogUrl;
	private String runlogUrl;
	private long sumFileNumber;
	private long sumFileSize;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getMissionid() {
		return missionid;
	}
	public void setMissionid(String missionid) {
		this.missionid = missionid;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFilelogUrl() {
		return filelogUrl;
	}
	public void setFilelogUrl(String filelogUrl) {
		this.filelogUrl = filelogUrl;
	}
	public String getRunlogUrl() {
		return runlogUrl;
	}
	public void setRunlogUrl(String runlogUrl) {
		this.runlogUrl = runlogUrl;
	}
	public long getSumFileNumber() {
		return sumFileNumber;
	}
	public void setSumFileNumber(long sumFileNumber) {
		this.sumFileNumber = sumFileNumber;
	}
	public long getSumFileSize() {
		return sumFileSize;
	}
	public void setSumFileSize(long sumFileSize) {
		this.sumFileSize = sumFileSize;
	}

}
