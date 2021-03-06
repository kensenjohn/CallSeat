package com.gs.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.UserInfoBean;
import com.gs.common.db.DBDAO;

public class UserInfoData {
	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");

	public Integer insertUserInfo(UserInfoBean userInfoBean) {
		appLogging.debug("Invoking insert User Info Bean");
		String sQuery = "INSERT INTO GTUSERINFO (USERINFOID, FIRST_NAME, LAST_NAME, ADDRESS_1, ADDRESS_2, CITY, STATE, COUNTRY,IP_ADDRESS,IS_TMP, "
				+ " DEL_ROW, CREATEDATE, CELL_PHONE, PHONE_NUM, EMAIL , HUMAN_CREATEDATE,TIMEZONE ) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?) ";

		ArrayList<Object> aParams = DBDAO.createConstraint(
				userInfoBean.getUserInfoId(), userInfoBean.getFirstName(),
				userInfoBean.getLastName(), userInfoBean.getAddress1(),
				userInfoBean.getAddress2(), userInfoBean.getCity(),
				userInfoBean.getState(), userInfoBean.getCountry(),
				userInfoBean.getIpAddress(), userInfoBean.getIsTemporary(),
				userInfoBean.getDeleteRow(), userInfoBean.getCreateDate(),
				userInfoBean.getCellPhone(), userInfoBean.getPhoneNum(),
				userInfoBean.getEmail(), userInfoBean.getHumanCreateDate(),
				userInfoBean.getTimezone());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin",
				"UserInfoData.java", "insertUserInfo() ");

		return numOfRowsInserted;

	}

	public Integer updateGuestUserInfo(UserInfoBean userInfoBean) {
		Integer iNumOfRecs = 0;
		if (userInfoBean != null && userInfoBean.getUserInfoId() != null
				&& !"".equalsIgnoreCase(userInfoBean.getUserInfoId())) {
			String sQuery = "UPDATE GTUSERINFO SET FIRST_NAME = ?, LAST_NAME = ? ,  CELL_PHONE = ? , PHONE_NUM = ? , EMAIL = ? "
					+ " WHERE USERINFOID = ? ";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					userInfoBean.getFirstName(), userInfoBean.getLastName(),
					userInfoBean.getCellPhone(), userInfoBean.getPhoneNum(),
					userInfoBean.getEmail(), userInfoBean.getUserInfoId());

			iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, "admin",
					"UserInfoData.java", "updateGuestUserInfo() ");

		}
		return iNumOfRecs;
	}

    public ArrayList<UserInfoBean> getGuestUserInfoByByCellPhone(String sCellphoneNumber, String sAdminId)
    {
        ArrayList<UserInfoBean> arrUserInfoBean = new ArrayList<UserInfoBean>();
        if(sCellphoneNumber!=null && !"".equalsIgnoreCase(sCellphoneNumber) && sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
        {
            String sQuery = "SELECT GTU.* FROM GTUSERINFO GTU, GTGUESTS GTG WHERE GTU.CELL_PHONE = ? and GTG.FK_USERINFOID = GTU.USERINFOID " +
                    " AND GTG.FK_ADMINID= ? ";

            ArrayList<Object> aParams = DBDAO.createConstraint(sCellphoneNumber, sAdminId );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData("admin",sQuery,aParams,true,"UserInfoData.java", "updateGuestUserInfo()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                for(HashMap<String, String> hmResult : arrResult )
                {
                    UserInfoBean userInfoBean = new UserInfoBean(hmResult) ;
                    arrUserInfoBean.add(userInfoBean);
                }
            }
        }
        return arrUserInfoBean;
    }

    public ArrayList<UserInfoBean> getGuestUserInfoByByHomePhone(String sCellphoneNumber, String sAdminId)
    {
        ArrayList<UserInfoBean> arrUserInfoBean = new ArrayList<UserInfoBean>();
        if(sCellphoneNumber!=null && !"".equalsIgnoreCase(sCellphoneNumber) && sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
        {
            String sQuery = "SELECT * FROM GTUSERINFO GTU, GTGUESTS GTG WHERE GTU.PHONE_NUM = ? and GTG.FK_USERINFOID = GTU.USERINFOID " +
                    " AND GTG.FK_ADMINID= ? ";

            ArrayList<Object> aParams = DBDAO.createConstraint(sCellphoneNumber, sAdminId );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData("admin",sQuery,aParams,true,"UserInfoData.java", "updateGuestUserInfo()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                for(HashMap<String, String> hmResult : arrResult )
                {
                    UserInfoBean userInfoBean = new UserInfoBean(hmResult) ;
                    arrUserInfoBean.add(userInfoBean);
                }
            }
        }
        return arrUserInfoBean;
    }
}
