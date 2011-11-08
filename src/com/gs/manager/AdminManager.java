package com.gs.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.DateSupport;
import com.gs.common.Utility;
import com.gs.data.AdminData;

public class AdminManager
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	/**
	 * This will create an Admin record. If admin is null or does not have a
	 * matching User Info it will throw an error.
	 * 
	 * @param adminBean
	 */
	public void createAdmin(AdminBean adminBean)
	{
		if (adminBean == null || adminBean.getAdminId() == null
				|| "".equalsIgnoreCase(adminBean.getAdminId()) || adminBean.getUserInfoId() == null
				|| "".equalsIgnoreCase(adminBean.getUserInfoId()))
		{
			appLogging.error("There is no user info to create.");
		} else
		{
			AdminData adminData = new AdminData();

			int iNumOfRecs = adminData.insertAdmin(adminBean);

			if (iNumOfRecs > 0)
			{
				appLogging.info("Admin creation successful for  : " + adminBean.getAdminId());
			} else
			{
				appLogging.error("Error creating Admin " + adminBean.getAdminId());
			}
		}

	}

	public AdminBean createAdmin()
	{
		AdminBean adminBean = getTemporaryAdmin();
		createAdmin(adminBean);

		return adminBean;
	}

	private AdminBean getTemporaryAdmin()
	{
		AdminBean adminBean = new AdminBean();

		UserInfoManager userInfoManager = new UserInfoManager();
		UserInfoBean userInfoBean = userInfoManager.createUserInfoBean();

		if (userInfoBean != null && !"".equalsIgnoreCase(userInfoBean.getUserInfoId()))
		{
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

	public UserInfoBean getAminUserInfo(String sAdminId)
	{
		UserInfoBean adminUserInfo = new UserInfoBean();
		if (sAdminId != null && !"".equals(sAdminId))
		{
			AdminData adminData = new AdminData();
			adminUserInfo = adminData.getAdminUserInfo(sAdminId);

		}
		return adminUserInfo;
	}

	public AdminBean getAdmin(String sAdminId)
	{
		AdminBean adminBean = new AdminBean();
		if (sAdminId != null && !"".equals(sAdminId))
		{
			AdminData adminData = new AdminData();
			adminBean = adminData.getAdmin(sAdminId);
		}
		return adminBean;
	}
}
