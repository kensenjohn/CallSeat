package com.gs.manager.forgot;

import com.gs.bean.SecurityForgotInfoBean;

public interface ForgotInfoManager {
	public abstract boolean createUserRequest();
	public abstract SecurityForgotInfoBean identifyUserResponse(String sSecureTokenId);
	public abstract boolean processUserResponse(SecurityForgotInfoBean securityForgotInfoBean);
}
