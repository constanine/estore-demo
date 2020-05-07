package com.bokesoft.struc;


/** 
 * @OperatedBean 为历史的操作记录bean
 * @param id
 * @param user 提交者
 * @param missionid 提交的任务编号
 * @param startTime 提交任务的时间
 * @param exportilelogUrl 任务filelistlog-文件存放路径
 * @param exportrunlogUrl 任务runlog-文件存放路径
 * @param sumFileNumber 任务中文件总数
 * @param isScaned 在中转区时，是否已被扫描  已扫描,准备扫描，以完成
 * @param judgement 扫描后的判断，通过自动导出(1)，还是待审批(0) 
 * @param endTime 导出任务结束时的时间
 * @param authorizer 审批人 
 * @param authorizeState 审批状态：流程中，通过，不通过
 * @param authorizeTime 审批时间
 * @param authorityLogUrl 审批日志
 */

public class ExportMissionBean {
	private long id;
	private String userCode;
	private String missionid;
	private String startTime;
	private String exportfilelogUrl;
	private String step1state;
	private long sumFileNumber;
	private long sumfilesize;
	private String judgement;
	private String exportstep1runlogUrl;
	private String exportstep2runlogUrl;
	private String step2state;
	private String endTime;
	private String authorizer;
	private String authorizeState;	
	private String authorizeTime;
	private String authorityLogUrl;
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
	public String getExportfilelogUrl() {
		return exportfilelogUrl;
	}
	public void setExportfilelogUrl(String exportfilelogUrl) {
		this.exportfilelogUrl = exportfilelogUrl;
	}

	public long getSumfilesize() {
		return sumfilesize;
	}
	public void setSumfilesize(long sumfilesize) {
		this.sumfilesize = sumfilesize;
	}
	public String getExportstep1runlogUrl() {
		return exportstep1runlogUrl;
	}
	public void setExportstep1runlogUrl(String exportstep1runlogUrl) {
		this.exportstep1runlogUrl = exportstep1runlogUrl;
	}
	public String getExportstep2runlogUrl() {
		return exportstep2runlogUrl;
	}
	public void setExportstep2runlogUrl(String exportstep2runlogUrl) {
		this.exportstep2runlogUrl = exportstep2runlogUrl;
	}
	public long getSumFileNumber() {
		return sumFileNumber;
	}
	public void setSumFileNumber(long sumFileNumber) {
		this.sumFileNumber = sumFileNumber;
	}

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getAuthorizer() {
		return authorizer;
	}
	public void setAuthorizer(String authorizer) {
		this.authorizer = authorizer;
	}
	public String getAuthorizeState() {
		return authorizeState;
	}
	public void setAuthorizeState(String authorizeState) {
		this.authorizeState = authorizeState;
	}

	public String getJudgement() {
		return judgement;
	}
	public void setJudgement(String judgement) {
		this.judgement = judgement;
	}
	public String getAuthorizeTime() {
		return authorizeTime;
	}
	public void setAuthorizeTime(String authorizeTime) {
		this.authorizeTime = authorizeTime;
	}
	public String getAuthorityLogUrl() {
		return authorityLogUrl;
	}
	public void setAuthorityLogUrl(String authorityLogUrl) {
		this.authorityLogUrl = authorityLogUrl;
	}
	public String getStep2state() {
		return step2state;
	}
	public void setStep2state(String step2state) {
		this.step2state = step2state;
	}
	public String getStep1state() {
		return step1state;
	}
	public void setStep1state(String step1state) {
		this.step1state = step1state;
	}

	
}
