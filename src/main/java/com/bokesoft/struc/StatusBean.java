package com.bokesoft.struc;

public enum StatusBean {

	RUNNING("running"), STOPPED("stopped"), FINISHED("finished"), ERROR("error");

	private String value;

	private StatusBean(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
