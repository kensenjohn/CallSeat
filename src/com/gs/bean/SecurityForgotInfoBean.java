package com.gs.bean;

import java.util.HashMap;

import com.gs.common.Constants;
import com.gs.common.ParseUtil;

public class SecurityForgotInfoBean {
	
	private String securityForgotInfoId = "";
	private String adminId = "";
	private String tokenAdminIdHash = "";
	private String secureTokenId = "";
	private String tokenAdminId = "";
	private Long createDate = 0L;
	private String humanCreateDate = "";
	private boolean isUsable = false;
	private Constants.FORGOT_INFO_ACTION actionType = null;
	
	public SecurityForgotInfoBean()
	{
		
	}
	
	public SecurityForgotInfoBean(HashMap<String, String> hmForgotInfoRes)
	{
		this.securityForgotInfoId = ParseUtil.checkNull(hmForgotInfoRes.get("SECURITYFORGOTINFOID"));
		this.adminId = ParseUtil.checkNull(hmForgotInfoRes.get("FK_ADMINID"));
		this.secureTokenId = ParseUtil.checkNull(hmForgotInfoRes.get("SECURE_TOKEN_ID"));
		this.tokenAdminIdHash = ParseUtil.checkNull(hmForgotInfoRes.get("TOKEN_ADMIN_ID_COMBO"));
		this.createDate = ParseUtil.sToL(hmForgotInfoRes.get("CREATEDATE"));
		this.humanCreateDate = ParseUtil.checkNull(hmForgotInfoRes.get("HUMANCREATEDATE"));
		this.isUsable = ParseUtil.sTob(hmForgotInfoRes.get("IS_USABLE"));
		
		String sActionType = ParseUtil.checkNull(hmForgotInfoRes.get("ACTION_TYPE"));
		if(Constants.FORGOT_INFO_ACTION.PASSWORD.getAction().equalsIgnoreCase(sActionType))
		{
			this.actionType = Constants.FORGOT_INFO_ACTION.PASSWORD;
		}
		else if(Constants.FORGOT_INFO_ACTION.USERNAME.getAction().equalsIgnoreCase(sActionType))
		{
			this.actionType = Constants.FORGOT_INFO_ACTION.USERNAME;
		}
	}
	
	public String getSecurityForgotInfoId() {
		return securityForgotInfoId;
	}
	public void setSecurityForgotInfoId(String securityForgotInfoId) {
		this.securityForgotInfoId = securityForgotInfoId;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	
	public String getTokenAdminIdHash() {
		return tokenAdminIdHash;
	}

	public void setTokenAdminIdHash(String tokenAdminIdHash) {
		this.tokenAdminIdHash = tokenAdminIdHash;
	}

	public String getSecureTokenId() {
		return secureTokenId;
	}

	public void setSecureTokenId(String secureTokenId) {
		this.secureTokenId = secureTokenId;
	}

	public String getTokenAdminId() {
		return tokenAdminId;
	}

	public void setTokenAdminId(String tokenAdminId) {
		this.tokenAdminId = tokenAdminId;
	}

	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public String getHumanCreateDate() {
		return humanCreateDate;
	}
	public void setHumanCreateDate(String humanCreateDate) {
		this.humanCreateDate = humanCreateDate;
	}
	public boolean isUsable() {
		return isUsable;
	}
	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}
	
	public Constants.FORGOT_INFO_ACTION getActionType() {
		return actionType;
	}
	public void setActionType(Constants.FORGOT_INFO_ACTION actionType) {
		this.actionType = actionType;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SecurityForgotInfoBean [securityForgotInfoId=")
				.append(securityForgotInfoId).append(", adminId=")
				.append(adminId).append(", createDate=")
				.append(createDate).append(", humanCreateDate=")
				.append(humanCreateDate).append(", isUsable=").append(isUsable)
				.append(", actionType=").append(actionType).append("]");
		return builder.toString();
	}
	
	
}
