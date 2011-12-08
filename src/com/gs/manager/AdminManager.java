package com.gs.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.bean.RegisterAdminBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.BCrypt;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.Utility;
import com.gs.data.AdminData;

public class AdminManager {
	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");

	/**
	 * This will create an Admin record. If admin is null or does not have a
	 * matching User Info it will throw an error.
	 * 
	 * @param adminBean
	 */
	public void createAdmin(AdminBean adminBean) {
		if (adminBean == null || adminBean.getAdminId() == null
				|| "".equalsIgnoreCase(adminBean.getAdminId())
				|| adminBean.getUserInfoId() == null
				|| "".equalsIgnoreCase(adminBean.getUserInfoId())) {
			appLogging.error("There is no user info to create.");
		} else {
			AdminData adminData = new AdminData();

			int iNumOfRecs = adminData.insertAdmin(adminBean);

			if (iNumOfRecs > 0) {
				appLogging.info("Admin creation successful for  : "
						+ adminBean.getAdminId());
			} else {
				appLogging.error("Error creating Admin "
						+ adminBean.getAdminId());
			}
		}

	}

	public AdminBean createAdmin() {
		AdminBean adminBean = getTemporaryAdmin();
		createAdmin(adminBean);

		return adminBean;
	}

	private AdminBean getTemporaryAdmin() {
		AdminBean adminBean = new AdminBean();

		UserInfoManager userInfoManager = new UserInfoManager();
		UserInfoBean userInfoBean = userInfoManager.createUserInfoBean();

		if (userInfoBean != null
				&& !"".equalsIgnoreCase(userInfoBean.getUserInfoId())) {
			adminBean.setUserInfoId(userInfoBean.getUserInfoId());

			adminBean.setAdminId(Utility.getNewGuid());
			adminBean.setIsTemporary("1");
			adminBean.setDeleteRow("0");
			adminBean.setCreateDate(DateSupport.getEpochMillis()); // change
			// this to a
			// valid date
			adminBean.setHumanCreateDate(DateSupport.getUTCDateTime());
		}

		// adminBean.

		return adminBean;
	}

	public UserInfoBean getAminUserInfo(String sAdminId) {
		UserInfoBean adminUserInfo = new UserInfoBean();
		if (sAdminId != null && !"".equals(sAdminId)) {
			AdminData adminData = new AdminData();
			adminUserInfo = adminData.getAdminUserInfo(sAdminId);

		}
		return adminUserInfo;
	}

	public AdminBean getAdmin(String sAdminId) {
		AdminBean adminBean = new AdminBean();
		if (sAdminId != null && !"".equals(sAdminId)) {
			AdminData adminData = new AdminData();
			adminBean = adminData.getAdmin(sAdminId);
		}
		return adminBean;
	}

	public boolean isUserExists(RegisterAdminBean regAdminBean) {

		boolean isAdminExists = false;
		if (regAdminBean != null) {
			AdminData adminData = new AdminData();
			AdminBean adminBean = adminData.isAdminEmailExists(regAdminBean);

			String sAdminId = adminBean.getAdminId();

			if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)) {
				isAdminExists = true;
			}
		}

		return isAdminExists;
	}

	public Integer registerUser(RegisterAdminBean regAdminBean) {
		int numOfAdminRegs = 0;
		if (regAdminBean != null) {

			String sEmail = regAdminBean.getEmail();
			String sPassword = regAdminBean.getPassword();

			String sPasswordHash = BCrypt.hashpw(sPassword, BCrypt.gensalt(5));

			regAdminBean.setPasswordHash(sPasswordHash);
			regAdminBean.setCreateDate(DateSupport.getEpochMillis());
			regAdminBean.setHumanCreateDate(DateSupport.getUTCDateTime());

			AdminData adminData = new AdminData();

			AdminBean adminBean = adminData.getAdmin(regAdminBean.getAdminId());

			if (adminBean == null
					|| (adminBean != null && !adminBean.isAdminExists())) {
				adminBean = createAdmin();
			}

			adminData.registerUser(regAdminBean, adminBean);

			if (adminBean != null && adminBean.isAdminExists()) {
				numOfAdminRegs = adminData.toggleAdminTemp(true,
						adminBean.getAdminId());
			} else {
				appLogging.error("Error  registering user.");
			}

		}
		return numOfAdminRegs;
	}

	public AdminBean getAdminBeanFromEmail(RegisterAdminBean loginAdminBean) {
		AdminData adminData = new AdminData();
		AdminBean adminBean = adminData.isAdminEmailExists(loginAdminBean);

		return adminBean;
	}

	public AdminBean authenticateUser(RegisterAdminBean loginAdminBean) {

		AdminBean adminBean = new AdminBean();
		if (loginAdminBean != null && loginAdminBean.getEmail() != null
				&& !"".equalsIgnoreCase(loginAdminBean.getEmail())
				&& loginAdminBean.getPassword() != null
				&& !"".equalsIgnoreCase(loginAdminBean.getPassword())) {
			String sEmail = loginAdminBean.getEmail();
			String sPasswordInput = loginAdminBean.getPassword();

			AdminData adminData = new AdminData();
			adminBean = adminData.isAdminEmailExists(loginAdminBean);

			if (adminBean != null) {
				loginAdminBean.setAdminId(adminBean.getAdminId());

				String sPasswordHashFromDB = adminData.getPasswordHash(
						loginAdminBean, Constants.PASSWORD_STATUS.ACTIVE);
				if (sPasswordHashFromDB != null
						&& !"".equalsIgnoreCase(sPasswordHashFromDB)) {
					if (BCrypt.checkpw(sPasswordInput, sPasswordHashFromDB)) {
						// logged in.
					} else {
						adminBean = new AdminBean();
						appLogging.info("User was not authenticated - Email : "
								+ sEmail);
					}
				} else {
					appLogging.error("Password Hash does not exist - Email : "
							+ sEmail);
				}
			} else {
				appLogging.error("Admin Info does not exist for email."
						+ sEmail);
			}

		} else {
			appLogging.error("Invalid data for credentials. ");
		}
		return adminBean;
	}

	public void assignTmpToPermAdmin(String sTmpAdminId, String sPermAdminId) {

		if (sTmpAdminId != null && !"".equalsIgnoreCase(sTmpAdminId)
				&& sPermAdminId != null && !"".equalsIgnoreCase(sPermAdminId)) {

			AdminData adminData = new AdminData();
			adminData.assignEventToPermAdmin(sTmpAdminId, sPermAdminId);
			adminData.assignGuestToPermAdmin(sTmpAdminId, sPermAdminId);
			adminData.assignTableToPermAdmin(sTmpAdminId, sPermAdminId);

		}

	}
}
