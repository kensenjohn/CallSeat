package com.gs.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.bean.RegisterAdminBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;

public class AdminData {
	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");

	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public Integer insertAdmin(AdminBean adminBean) {
		appLogging.debug("Invoking insert Admin Data");
		String sQuery = "INSERT INTO GTADMIN (ADMINID, FK_USERINFOID,CREATEDATE,IS_TMP,DEL_ROW, HUMANCREATEDATE)"
				+ " VALUES ( ? , ? , ? , ? , ? , ? )";
		ArrayList<Object> aParams = DBDAO.createConstraint(
				adminBean.getAdminId(), adminBean.getUserInfoId(),
				adminBean.getCreateDate(), adminBean.getIsTemporary(),
				adminBean.getDeleteRow(), adminBean.getHumanCreateDate());

		int numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
				"AdminData.java", "insertAdmin() ");

		return numOfRowsInserted;
	}

	public void updateAdmin() {

	}

	public UserInfoBean getAdminUserInfo(String sAdminId) {
		UserInfoBean adminUserInfoBean = new UserInfoBean();
		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)) {
			String sQuery = "SELECT GTU.USERINFOID , GTU.FIRST_NAME , GTU.LAST_NAME , GTU.ADDRESS_1 , "
					+ " GTU.ADDRESS_2,  GTU.CITY, GTU.STATE , GTU.COUNTRY , GTU.CELL_PHONE, GTU.IP_ADDRESS, "
					+ " GTU.PHONE_NUM, GTU.EMAIL, GTU.TIMEZONE,  GTU.IS_TMP , GTU.DEL_ROW, GTU.CREATEDATE "
					+ " FROM GTUSERINFO GTU , GTADMIN GTA WHERE GTA.ADMINID = ? AND "
					+ " GTA.FK_USERINFOID = GTU.USERINFOID";

			ArrayList<Object> aParams = DBDAO.createConstraint(sAdminId);

			ArrayList<HashMap<String, String>> arrAdminUserInfo = DBDAO
					.getDBData(ADMIN_DB, sQuery, aParams, true,
							"AdminData.java", "getAdminUserInfo()");

			if (arrAdminUserInfo != null && !arrAdminUserInfo.isEmpty()) {
				for (HashMap<String, String> hmAdminUserInfo : arrAdminUserInfo) {
					adminUserInfoBean = new UserInfoBean(hmAdminUserInfo);
					break;
				}
			}

		}

		return adminUserInfoBean;

	}

	public AdminBean getAdmin(String sAdminId) {
		AdminBean adminBean = new AdminBean();

		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)) {
			String sQuery = "SELECT GTA.ADMINID, GTA.FK_USERINFOID, GTA.CREATEDATE AS ADMIN_CREATEDATE, "
					+ " GTA.IS_TMP AS ADMIN_IS_TMP, GTA.DEL_ROW  AS ADMIN_DEL_ROW,"
					+ " GTU.USERINFOID , GTU.FIRST_NAME , GTU.LAST_NAME , GTU.ADDRESS_1 , "
					+ " GTU.ADDRESS_2,  GTU.CITY, GTU.STATE , GTU.COUNTRY , GTU.CELL_PHONE, GTU.IP_ADDRESS, "
					+ " GTU.PHONE_NUM, GTU.EMAIL, GTU.TIMEZONE,  GTU.IS_TMP , GTU.DEL_ROW, GTU.CREATEDATE "
					+ " FROM GTUSERINFO GTU , GTADMIN GTA WHERE GTA.ADMINID = ? AND "
					+ " GTA.FK_USERINFOID = GTU.USERINFOID";

			ArrayList<Object> aParams = DBDAO.createConstraint(sAdminId);

			ArrayList<HashMap<String, String>> arrAdminRes = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, true, "AdminData.java",
					"getAdmin()");

			if (arrAdminRes != null && !arrAdminRes.isEmpty()) {
				for (HashMap<String, String> hmAdminRes : arrAdminRes) {
					adminBean = new AdminBean(hmAdminRes);
					break;
				}
			}
		}
		return adminBean;
	}

	public AdminBean isAdminEmailExists(RegisterAdminBean regAdminBean) {

		AdminBean adminBean = new AdminBean();

		if (regAdminBean != null
				&& !"".equalsIgnoreCase(regAdminBean.getEmail())) {

			String sQuery = "SELECT GTA.ADMINID, GTA.FK_USERINFOID, GTA.CREATEDATE AS ADMIN_CREATEDATE, "
					+ " GTA.IS_TMP AS ADMIN_IS_TMP, GTA.DEL_ROW  AS ADMIN_DEL_ROW,"
					+ " GTU.USERINFOID , GTU.FIRST_NAME , GTU.LAST_NAME , GTU.ADDRESS_1 , "
					+ " GTU.ADDRESS_2,  GTU.CITY, GTU.STATE , GTU.COUNTRY , GTU.CELL_PHONE, GTU.IP_ADDRESS, "
					+ " GTU.PHONE_NUM, GTU.EMAIL, GTU.TIMEZONE,  GTU.IS_TMP , GTU.DEL_ROW, GTU.CREATEDATE "
					+ " FROM GTUSERINFO GTU , GTADMIN GTA WHERE GTU.EMAIL = ?  AND "
					+ " GTA.FK_USERINFOID = GTU.USERINFOID";

			ArrayList<Object> aParams = DBDAO.createConstraint(regAdminBean
					.getEmail());

			ArrayList<HashMap<String, String>> arrAdminRes = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, true, "AdminData.java",
					"isAdminEmailExists()");

			if (arrAdminRes != null && !arrAdminRes.isEmpty()) {
				for (HashMap<String, String> hmAdminRes : arrAdminRes) {
					adminBean = new AdminBean(hmAdminRes);
					break;
				}
			}
		}
		return adminBean;
	}

	public Integer registerUser(RegisterAdminBean regAdminBean,
			AdminBean adminBean) {
		int numOfAdminInserted = 0;
		if (regAdminBean != null && regAdminBean.getEmail() != null
				&& !"".equalsIgnoreCase(regAdminBean.getEmail())
				&& regAdminBean.getPassword() != null
				&& !"".equalsIgnoreCase(regAdminBean.getPassword())) {
			String sQuery = "UPDATE GTUSERINFO SET EMAIL = ?, FIRST_NAME = ?, LAST_NAME = ? "
					+ " WHERE USERINFOID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					regAdminBean.getEmail(), regAdminBean.getFirstName(),
					regAdminBean.getLastName(), adminBean.getUserInfoId());

			numOfAdminInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"AdminData.java", "registerUser() ");

			if (numOfAdminInserted > 0) {
				numOfAdminInserted = createPassword(regAdminBean);
			} else {
				appLogging.error("Error registring user.");
			}
		}
		return numOfAdminInserted;
	}

	public String getPasswordHash(RegisterAdminBean regAdminBean,
			Constants.PASSWORD_STATUS passwordStatus) {
		String sPasswordHash = "";
		if (regAdminBean != null && regAdminBean.getAdminId() != null
				&& !"".equalsIgnoreCase(regAdminBean.getAdminId())
				&& passwordStatus != null) {

			String sQuery = "SELECT PASSWORD FROM GTPASSWORD WHERE FK_ADMINID = ? AND PASSWORD_STATUS = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					regAdminBean.getAdminId(), passwordStatus.getStatus());

			ArrayList<HashMap<String, String>> arrPasswordRes = DBDAO
					.getDBData(ADMIN_DB, sQuery, aParams, true,
							"AdminData.java", "getPasswordHash()");

			if (arrPasswordRes != null && !arrPasswordRes.isEmpty()) {
				for (HashMap<String, String> hmPasswordRes : arrPasswordRes) {
					sPasswordHash = ParseUtil.checkNull(hmPasswordRes
							.get("PASSWORD"));
					// adminBean = new AdminBean(hmAdminRes);
					break;
				}
			}

		}
		return sPasswordHash;
	}

	public Integer deactivateOldPassword(AdminBean adminBean) {
		int numOfRowsInserted = 0;
		if (adminBean != null && adminBean.getAdminId() != null
				&& !"".equalsIgnoreCase(adminBean.getAdminId())) {
			String sQuery = "UPDATE GTPASSWORD SET PASSWORD_STATUS = ? WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					Constants.PASSWORD_STATUS.OLD.getStatus(),
					adminBean.getAdminId());
			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"AdminData.java", "deactivateOldPassword()");
		}
		return numOfRowsInserted;
	}

	public Integer createPassword(RegisterAdminBean regAdminBean) {

		appLogging.debug("Create password invoked.");
		int numOfRowsInserted = 0;
		if (regAdminBean != null && regAdminBean.getPasswordHash() != null
				&& !"".equalsIgnoreCase(regAdminBean.getPasswordHash())) {
			String sQuery = "INSERT INTO GTPASSWORD ( PASSWORDID , PASSWORD , FK_ADMINID , CREATEDATE , "
					+ " HUMANCREATEDATE ,  PASSWORD_STATUS ) VALUES ( ?,?,?  ,?,?,?)";

			String sPasswordId = Utility.getNewGuid();
			ArrayList<Object> aParams = DBDAO.createConstraint(sPasswordId,
					regAdminBean.getPasswordHash(), regAdminBean.getAdminId(),
					regAdminBean.getCreateDate(),
					regAdminBean.getHumanCreateDate(),
					Constants.PASSWORD_STATUS.ACTIVE.getStatus());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"AdminData.java", "createPassword() ");

		}

		return numOfRowsInserted;

	}

	public Integer toggleAdminTemp(boolean isTmp, String sAdminId) {

		int numOfRowsUpdated = 0;
		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId)) {
			// Code here will make admin permanent by setting the "is_tmp" flag
			// to
			// permanent.
			String sQuery = "UPDATE GTADMIN SET IS_TMP = ? WHERE ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(isTmp ? "1"
					: "0", sAdminId);
			numOfRowsUpdated = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"AdminData.java", "toggleAdminTemp() ");
		}
		return numOfRowsUpdated;

	}

	public void assignEventToPermAdmin(String sTmpAdminId, String sPermAdminId) {
		if (sTmpAdminId != null && !"".equalsIgnoreCase(sTmpAdminId)
				&& sPermAdminId != null && !"".equalsIgnoreCase(sPermAdminId)) {
			String sQuery = "UPDATE GTEVENT SET FK_ADMINID = ? ,IS_TMP=? WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(sPermAdminId,
					"0", sTmpAdminId);

			DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "AdminData.java",
					"assignEventToPermAdmin()");
		}
	}

	public void assignGuestToPermAdmin(String sTmpAdminId, String sPermAdminId) {

		if (sTmpAdminId != null && !"".equalsIgnoreCase(sTmpAdminId)
				&& sPermAdminId != null && !"".equalsIgnoreCase(sPermAdminId)) {
			String sQuery = "UPDATE GTGUESTS SET FK_ADMINID = ? ,IS_TMP=? WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(sPermAdminId,
					"0", sTmpAdminId);

			DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "AdminData.java",
					"assignGuestToPermAdmin()");
		}
	}

	public void assignTableToPermAdmin(String sTmpAdminId, String sPermAdminId) {

		if (sTmpAdminId != null && !"".equalsIgnoreCase(sTmpAdminId)
				&& sPermAdminId != null && !"".equalsIgnoreCase(sPermAdminId)) {
			String sQuery = "UPDATE GTTABLE SET FK_ADMINID = ? ,IS_TMP=? WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(sPermAdminId,
					"0", sTmpAdminId);

			DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "AdminData.java",
					"assignTableToPermAdmin()");
		}
	}

	public void assignTelNumberToPermAdmin(String sTmpAdminId,
			String sPermAdminId) {
		if (sTmpAdminId != null && !"".equalsIgnoreCase(sTmpAdminId)
				&& sPermAdminId != null && !"".equalsIgnoreCase(sPermAdminId)) {
			String sQuery = "UPDATE GTTELNUMBERS SET FK_ADMINID = ? WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(sPermAdminId,
					sTmpAdminId);

			DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "AdminData.java",
					"assignTelNumberToPermAdmin()");
		}
	}
}
