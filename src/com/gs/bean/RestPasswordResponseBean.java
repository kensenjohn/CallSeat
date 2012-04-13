package com.gs.bean;

public class RestPasswordResponseBean {
	private AdminBean adminBean = new AdminBean();
	private String newPassword = "";

	public AdminBean getAdminBean() {
		return adminBean;
	}

	public void setAdminBean(AdminBean adminBean) {
		this.adminBean = adminBean;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
