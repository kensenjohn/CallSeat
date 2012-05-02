package com.gs.bean;

import java.util.HashMap;

public class DemoTelNumber {
	/*
	 * DEMOTELNUMBERID | varchar(45) | NO | PRI | NULL | | | FK_TELNUMBERTYPEID
	 * | varchar(45) | NO | | NULL | | | TELNUMBER
	 */
	public DemoTelNumber() {

	}

	public DemoTelNumber(HashMap<String, String> hmResult) {
		this.demoTelNumberId = hmResult.get("DEMOTELNUMBERID");
		this.demoTelNumberTypeId = hmResult.get("FK_TELNUMBERTYPEID");
		this.demoTelNumber = hmResult.get("TELNUMBER");
		this.demoHumanTelNumber = hmResult.get("HUMAN_TELNUMBER");
	}

	private String demoTelNumberId = "";
	private String demoTelNumberTypeId = "";
	private String demoTelNumber = "";
	private String demoHumanTelNumber = "";

	public String getDemoTelNumberId() {
		return demoTelNumberId;
	}

	public void setDemoTelNumberId(String demoTelNumberId) {
		this.demoTelNumberId = demoTelNumberId;
	}

	public String getDemoTelNumberTypeId() {
		return demoTelNumberTypeId;
	}

	public void setDemoTelNumberTypeId(String demoTelNumberTypeId) {
		this.demoTelNumberTypeId = demoTelNumberTypeId;
	}

	public String getDemoTelNumber() {
		return demoTelNumber;
	}

	public void setDemoTelNumber(String demoTelNumber) {
		this.demoTelNumber = demoTelNumber;
	}

	public String getDemoHumanTelNumber() {
		return demoHumanTelNumber;
	}

	public void setDemoHumanTelNumber(String demoHumanTelNumber) {
		this.demoHumanTelNumber = demoHumanTelNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DemoTelNumber [demoTelNumberId=")
				.append(demoTelNumberId).append(", demoTelNumberTypeId=")
				.append(demoTelNumberTypeId).append(", demoTelNumber=")
				.append(demoTelNumber).append(", demoHumanTelNumber=")
				.append(demoHumanTelNumber).append("]");
		return builder.toString();
	}

}
