package com.gs.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.UserInfoBean;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.Utility;
import com.gs.data.UserInfoData;

import java.util.ArrayList;

public class UserInfoManager {

	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");

	// private static final Logger logger2 = Logger.g

	public UserInfoBean createUserInfoBean(UserInfoBean userInfoBean) {
		if (userInfoBean == null || userInfoBean.getUserInfoId() == null
				|| "".equalsIgnoreCase(userInfoBean.getUserInfoId())) {
			appLogging.error("There is no user info to create." + userInfoBean);
			userInfoBean = null;
		} else if (userInfoBean != null && userInfoBean.getUserInfoId() != null
				&& !"".equalsIgnoreCase(userInfoBean.getUserInfoId())) {
			UserInfoData userinfoData = new UserInfoData();
			int iNumOfRecords = userinfoData.insertUserInfo(userInfoBean);

			if (iNumOfRecords > 0) {
				appLogging.info("Create User Info Bean : "
						+ userInfoBean.getUserInfoId());
			} else {
				appLogging.error("Error creating User Info Bean : "
						+ userInfoBean);
				userInfoBean = null; // so that this will not be used again.
			}

		}
		return userInfoBean;
	}

	public UserInfoBean createUserInfoBean() {
		UserInfoBean userInfoBean = getTemporaryUserInfo();

		userInfoBean = createUserInfoBean(userInfoBean);
		appLogging.info(" User Info Bean : " + userInfoBean);
		return userInfoBean;
	}

	private UserInfoBean getTemporaryUserInfo() {
		UserInfoBean userInfoBean = new UserInfoBean();

		userInfoBean.setUserInfoId(Utility.getNewGuid());
		userInfoBean.setFirstName("Guest Admin");
		userInfoBean.setIsTemporary("1");
		userInfoBean.setDeleteRow("0");
		// change this to a valid date
		userInfoBean.setCreateDate(DateSupport.getEpochMillis());
		userInfoBean.setHumanCreateDate(DateSupport.getUTCDateTime());
		userInfoBean.setTimezone(Constants.DEFAULT_TIMEZONE);

		return userInfoBean;
	}

	public Integer updateGuestUserInfo(UserInfoBean userInfoBean) {
		Integer iNumOfRecs = 0;
		if (userInfoBean != null && userInfoBean.getUserInfoId() != null
				&& !"".equalsIgnoreCase(userInfoBean.getUserInfoId())) {
			UserInfoData userinfoData = new UserInfoData();
			iNumOfRecs = userinfoData.updateGuestUserInfo(userInfoBean);
		}
		return iNumOfRecs;
	}

    public ArrayList<UserInfoBean> getUserInfoBeanByCellPhone(String sCellPhone , String sAdminId)
    {
        ArrayList<UserInfoBean> arrUserInfoBean = new ArrayList<UserInfoBean>();
        if(sCellPhone!=null && !"".equalsIgnoreCase(sCellPhone))
        {
            UserInfoData userinfoData = new UserInfoData();
            arrUserInfoBean = userinfoData.getGuestUserInfoByByCellPhone(sCellPhone, sAdminId );
        }
        return arrUserInfoBean;
    }

    public ArrayList<UserInfoBean> getUserInfoBeanByHomePhone(String sHomePhone , String sAdminId)
    {
        ArrayList<UserInfoBean> arrUserInfoBean = new ArrayList<UserInfoBean>();
        if(sHomePhone!=null && !"".equalsIgnoreCase(sHomePhone))
        {
            UserInfoData userinfoData = new UserInfoData();
            arrUserInfoBean = userinfoData.getGuestUserInfoByByHomePhone(sHomePhone, sAdminId);
        }
        return arrUserInfoBean;
    }

}
