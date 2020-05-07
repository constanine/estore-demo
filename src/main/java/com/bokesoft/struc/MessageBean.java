package com.bokesoft.struc;
/**
 * @id 自身id
 * @param MissionState 消息中的任务状态
 * @param reportTime   消息报告时间
 * @param Missionid		任务id
 * @param MessageState 消息状态：new ,readed
 *
 */
public class MessageBean {
	private String Missionid;
	private String MissionState;
	private String reportTime;	
	private String MessageState;	
	private String userCode;

	public String getMissionState() {
		return MissionState;
	}
	public void setMissionState(String missionState) {
		MissionState = missionState;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getMissionid() {
		return Missionid;
	}
	public void setMissionid(String missionid) {
		Missionid = missionid;
	}
	public String getMessageState() {
		return MessageState;
	}
	public void setMessageState(String messageState) {
		MessageState = messageState;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}
