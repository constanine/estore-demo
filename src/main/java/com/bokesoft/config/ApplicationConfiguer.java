package com.bokesoft.config;


import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
* @description:自动加载yml中fileex.cfg下的元素
* */
@ConfigurationProperties(prefix = "fileex.cfg")
@Component //将配置类注入容器
public class ApplicationConfiguer {
	private List<String> no_auth_url_list;
	private String supervisors;
	private String inner_net_disk_path;
	private String inner_net_disk_ip;
	private String inner_net_ip;
	private String outter_net_disk_path;
	private String outter_net_disk_ip;
	private String outter_net_ip;
	private String transfer_disk_path;
	private String task_data_disk;
	private String history_data_disk;  //未用
	private String adAddress;
	private boolean use_ldap;
	
	public List<String> getNo_auth_url_list() {
		return no_auth_url_list;
	}
	public void setNo_auth_url_list(List<String> no_auth_url_list) {
		this.no_auth_url_list = no_auth_url_list;
	}
	public String getSupervisors() {
		return supervisors;
	}
	public void setSupervisors(String supervisors) {
		this.supervisors = supervisors;
	}
	public String getInner_net_disk_path() {
		return inner_net_disk_path;
	}
	public void setInner_net_disk_path(String inner_net_disk_path) {
		this.inner_net_disk_path = inner_net_disk_path;
	}
	public String getInner_net_disk_ip() {
		return inner_net_disk_ip;
	}
	public void setInner_net_disk_ip(String inner_net_disk_ip) {
		this.inner_net_disk_ip = inner_net_disk_ip;
	}
	public String getInner_net_ip() {
		return inner_net_ip;
	}
	public void setInner_net_ip(String inner_net_ip) {
		this.inner_net_ip = inner_net_ip;
	}
	public String getOutter_net_disk_path() {
		return outter_net_disk_path;
	}
	public void setOutter_net_disk_path(String outter_net_disk_path) {
		this.outter_net_disk_path = outter_net_disk_path;
	}
	public String getOutter_net_disk_ip() {
		return outter_net_disk_ip;
	}
	public void setOutter_net_disk_ip(String outter_net_disk_ip) {
		this.outter_net_disk_ip = outter_net_disk_ip;
	}
	public String getOutter_net_ip() {
		return outter_net_ip;
	}
	public void setOutter_net_ip(String outter_net_ip) {
		this.outter_net_ip = outter_net_ip;
	}
	public String getTransfer_disk_path() {
		return transfer_disk_path;
	}
	public void setTransfer_disk_path(String transfer_disk_path) {
		this.transfer_disk_path = transfer_disk_path;
	}
	public String getTask_data_disk() {
		return task_data_disk;
	}
	public void setTask_data_disk(String task_data_disk) {
		this.task_data_disk = task_data_disk;
	}
	public String getHistory_data_disk() {
		return history_data_disk;
	}
	public void setHistory_data_disk(String history_data_disk) {
		this.history_data_disk = history_data_disk;
	}
	public String getAdAddress() {
		return adAddress;
	}
	public void setAdAddress(String adAddress) {
		this.adAddress = adAddress;
	}
	public boolean isUse_ldap() {
		return use_ldap;
	}
	public void setUse_ldap(boolean use_ldap) {
		this.use_ldap = use_ldap;
	}
}
