package com.gs.data;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.UserInfoBean;
import com.gs.common.db.DBDAO;

public class UserInfoData
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public Integer insertUserInfo(UserInfoBean userInfoBean)
	{
		appLogging.debug("Invoking insert User Info Bean");
		String sQuery = "INSERT INTO GTUSERINFO (USERINFOID, FIRST_NAME, LAST_NAME, ADDRESS_1, ADDRESS_2, CITY, STATE, COUNTRY,IP_ADDRESS,IS_TMP, "
				+ " DEL_ROW, CREATEDATE, CELL_PHONE, PHONE_NUM, EMAIL , HUMAN_CREATEDATE,TIMEZONE ) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?) ";

		ArrayList<Object> aParams = DBDAO.createConstraint(userInfoBean.getUserInfoId(),
				userInfoBean.getFirstName(), userInfoBean.getLastName(),
				userInfoBean.getAddress1(), userInfoBean.getAddress2(), userInfoBean.getCity(),
				userInfoBean.getState(), userInfoBean.getCountry(), userInfoBean.getIpAddress(),
				userInfoBean.getIsTemporary(), userInfoBean.getDeleteRow(),
				userInfoBean.getCreateDate(), userInfoBean.getCellPhone(),
				userInfoBean.getPhoneNum(), userInfoBean.getEmail(),
				userInfoBean.getHumanCreateDate(), userInfoBean.getTimezone());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin", "UserInfoData.java",
				"insertUserInfo() ");

		return numOfRowsInserted;

	}
}
