package com.gs.bean;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.gs.common.ParseUtil;

public class AdminBean {
	private String adminId = "";
	private String userInfoId = "";
	private Long createDate = 0L;
	private String isTemporary = "1";
	private String deleteRow = "0";
	private String humanCreateDate = "";
	private UserInfoBean adminUserInfoBean = new UserInfoBean();
	private boolean isAdminExists = false;

	public AdminBean(HashMap<String, String> hmAdminRes) {

		this.adminId = ParseUtil.checkNull(hmAdminRes.get("ADMINID"));
		this.userInfoId = ParseUtil.checkNull(hmAdminRes.get("FK_USERINFOID"));
		this.createDate = ParseUtil.sToL(hmAdminRes.get("ADMIN_CREATEDATE"));
		this.isTemporary = ParseUtil.checkNull(hmAdminRes.get("IS_TMP"));
		this.deleteRow = ParseUtil.checkNull(hmAdminRes.get("ADMIN_DEL_ROW"));

		if (this.userInfoId != null && !"".equalsIgnoreCase(this.userInfoId)) {
			this.adminUserInfoBean = new UserInfoBean(hmAdminRes);

			if (this.adminUserInfoBean.isUserInfoExists()) {
				isAdminExists = true;
			}
		}
	}

	public AdminBean() {
		// this.adminId = Utility.getNewGuid();
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getIsTemporary() {
		return isTemporary;
	}

	public void setIsTemporary(String isTemporary) {
		this.isTemporary = isTemporary;
	}

	public String getDeleteRow() {
		return deleteRow;
	}

	public void setDeleteRow(String deleteRow) {
		this.deleteRow = deleteRow;
	}

	public UserInfoBean getAdminUserInfoBean() {
		return adminUserInfoBean;
	}

	public void setAdminUserInfoBean(UserInfoBean adminUserInfoBean) {
		this.adminUserInfoBean = adminUserInfoBean;
	}

	public boolean isAdminExists() {
		return isAdminExists;
	}

	public String getHumanCreateDate() {
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate) {
		this.humanCreateDate = humanCreateDate;
	}

	@Override
	public String toString() {
		return "AdminBean [adminId=" + adminId + ", userInfoId=" + userInfoId
				+ ", createDate=" + createDate + ", isTemporary=" + isTemporary
				+ ", deleteRow=" + deleteRow + "]";
	}

	public JSONObject toJson() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("admin_id", this.adminId);
			jsonObject.put("create_date", this.createDate);
			jsonObject.put("is_tmp", this.isTemporary);
			jsonObject.put("del_row", this.deleteRow);
			jsonObject.put("user_info_id", this.userInfoId);

			if (adminUserInfoBean != null) {

				jsonObject.put("user_info_bean",
						this.adminUserInfoBean.toJson());
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
