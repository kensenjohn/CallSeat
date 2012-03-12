package com.gs.bean;

import java.util.HashMap;

import com.gs.common.ParseUtil;

public class AdminTelephonyAccountBean {
	/*
	 * ADMINTELEPHONYACCOUNT | varchar(45) | NO | PRI | NULL | | | FK_ADMINID |
	 * varchar(45) | NO | | NULL | | | ACCOUNTSID | varchar(45) | NO | | NULL |
	 * | | AUTH_TOKEN | varchar(45) | NO | | NULL | | | DEL_ROW | int(1) | NO |
	 * | 0 | | | CREATEDATE | bigint(20) | NO | | 0 | | | HUMANCREATEDATE
	 */

	public AdminTelephonyAccountBean(HashMap<String, String> hmResult) {
		this.adminTelephonyAcId = ParseUtil.checkNull(hmResult
				.get("ADMINTELEPHONYACCOUNT"));
		this.adminId = ParseUtil.checkNull(hmResult.get("FK_ADMINID"));
		this.accountSid = ParseUtil.checkNull(hmResult.get("ACCOUNTSID"));
		this.authToken = ParseUtil.checkNull(hmResult.get("AUTH_TOKEN"));
		this.isDeleteRow = ParseUtil.sTob(hmResult.get("isDeleteRow"));
		this.createDate = ParseUtil.checkNull(hmResult.get("CREATEDATE"));
		this.humanCreateDate = ParseUtil.checkNull(hmResult
				.get("HUMANCREATEDATE"));
	}

	public AdminTelephonyAccountBean() {

	}

	private String adminTelephonyAcId = "";
	private String adminId = "";
	private String accountSid = "";
	private String authToken = "";
	private boolean isDeleteRow = false;
	private String createDate = "";
	private String humanCreateDate = "";

	public String getAdminTelephonyAcId() {
		return adminTelephonyAcId;
	}

	public void setAdminTelephonyAcId(String adminTelephonyAcId) {
		this.adminTelephonyAcId = adminTelephonyAcId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isDeleteRow() {
		return isDeleteRow;
	}

	public void setDeleteRow(boolean isDeleteRow) {
		this.isDeleteRow = isDeleteRow;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getHumanCreateDate() {
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate) {
		this.humanCreateDate = humanCreateDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminTelephonyAccountBean [adminTelephonyAcId=")
				.append(adminTelephonyAcId).append(", adminId=")
				.append(adminId).append(", accountSid=").append(accountSid)
				.append(", authToken=").append(authToken)
				.append(", isDeleteRow=").append(isDeleteRow)
				.append(", createDate=").append(createDate)
				.append(", humanCreateDate=").append(humanCreateDate)
				.append("]");
		return builder.toString();
	}

}
