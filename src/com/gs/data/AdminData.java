package com.gs.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;

public class AdminData
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public Integer insertAdmin(AdminBean adminBean)
	{
		appLogging.debug("Invoking insert Admin Data");
		String sQuery = "INSERT INTO GTADMIN (ADMINID, FK_USERINFOID,CREATEDATE,IS_TMP,DEL_ROW, HUMANCREATEDATE)"
				+ " VALUES ( ? , ? , ? , ? , ? , ? )";
		ArrayList<Object> aParams = DBDAO.createConstraint(adminBean.getAdminId(),
				adminBean.getUserInfoId(), adminBean.getCreateDate(), adminBean.getIsTemporary(),
				adminBean.getDeleteRow(), adminBean.getHumanCreateDate());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "AdminData.java",
				"insertAdmin() ");

		return numOfRowsInserted;
	}

	public void updateAdmin()
	{

	}

	public UserInfoBean getAdminUserInfo(String sAdminId)
	{
		UserInfoBean adminUserInfoBean = new UserInfoBean();
		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId))
		{
			String sQuery = "SELECT GTU.USERINFOID , GTU.FIRST_NAME , GTU.LAST_NAME , GTU.ADDRESS_1 , "
					+ " GTU.ADDRESS_2,  GTU.CITY, GTU.STATE , GTU.COUNTRY , GTU.CELL_PHONE, GTU.IP_ADDRESS, "
					+ " GTU.PHONE_NUM, GTU.EMAIL, GTU.TIMEZONE,  GTU.IS_TMP , GTU.DEL_ROW, GTU.CREATEDATE "
					+ " FROM GTUSERINFO GTU , GTADMIN GTA WHERE GTA.ADMINID = ? AND "
					+ " GTA.FK_USERINFOID = GTU.USERINFOID";

			ArrayList<Object> aParams = DBDAO.createConstraint(sAdminId);

			ArrayList<HashMap<String, String>> arrAdminUserInfo = DBDAO.getDBData(ADMIN_DB, sQuery,
					aParams, true, "AdminData.java", "getAdminUserInfo()");

			if (arrAdminUserInfo != null && !arrAdminUserInfo.isEmpty())
			{
				for (HashMap<String, String> hmAdminUserInfo : arrAdminUserInfo)
				{
					adminUserInfoBean = new UserInfoBean(hmAdminUserInfo);
					break;
				}
			}

		}

		return adminUserInfoBean;

	}

	public AdminBean getAdmin(String sAdminId)
	{
		AdminBean adminBean = new AdminBean();

		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId))
		{
			String sQuery = "SELECT GTA.ADMINID, GTA.FK_USERINFOID, GTA.CREATEDATE AS ADMIN_CREATEDATE, "
					+ " GTA.IS_TMP AS ADMIN_IS_TMP, GTA.DEL_ROW  AS ADMIN_DEL_ROW,"
					+ " GTU.USERINFOID , GTU.FIRST_NAME , GTU.LAST_NAME , GTU.ADDRESS_1 , "
					+ " GTU.ADDRESS_2,  GTU.CITY, GTU.STATE , GTU.COUNTRY , GTU.CELL_PHONE, GTU.IP_ADDRESS, "
					+ " GTU.PHONE_NUM, GTU.EMAIL, GTU.TIMEZONE,  GTU.IS_TMP , GTU.DEL_ROW, GTU.CREATEDATE "
					+ " FROM GTUSERINFO GTU , GTADMIN GTA WHERE GTA.ADMINID = ? AND "
					+ " GTA.FK_USERINFOID = GTU.USERINFOID";

			ArrayList<Object> aParams = DBDAO.createConstraint(sAdminId);

			ArrayList<HashMap<String, String>> arrAdminRes = DBDAO.getDBData(ADMIN_DB, sQuery,
					aParams, true, "AdminData.java", "getAdmin()");

			if (arrAdminRes != null && !arrAdminRes.isEmpty())
			{
				for (HashMap<String, String> hmAdminRes : arrAdminRes)
				{
					adminBean = new AdminBean(hmAdminRes);
					break;
				}
			}
		}
		return adminBean;
	}

}
