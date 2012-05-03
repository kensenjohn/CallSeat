package com.gs.bean;

import java.util.HashMap;

public class TelNumberTypeBean {

	public TelNumberTypeBean() {

	}

	public TelNumberTypeBean(HashMap<String, String> hmResult) {
		this.telNumberTypeId = hmResult.get("TELNUMBERTYPEID");
		this.description = hmResult.get("DESCRIPTION");
		this.telNumType = hmResult.get("TELNUMTYPE");

	}

	private String telNumberTypeId = "";
	private String description = "";
	private String telNumType = "";

	public String getTelNumberTypeId() {
		return telNumberTypeId;
	}

	public void setTelNumberTypeId(String telNumberTypeId) {
		this.telNumberTypeId = telNumberTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTelNumType() {
		return telNumType;
	}

	public void setTelNumType(String telNumType) {
		this.telNumType = telNumType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TelNumberTypeBean [telNumberTypeId=")
				.append(telNumberTypeId).append(", description=")
				.append(description).append(", telNumType=").append(telNumType)
				.append("]");
		return builder.toString();
	}

}
