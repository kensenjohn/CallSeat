package com.gs.phone.account;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminTelephonyAccountBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;

public class AdminTelephonyAccountData {

	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public ArrayList<AdminTelephonyAccountBean> getAdminAccount(
			AdminTelephonyAccountMeta adminAccountMeta) {

		ArrayList<AdminTelephonyAccountBean> arrAdminTelAccount = new ArrayList<AdminTelephonyAccountBean>();
		if (adminAccountMeta != null
				&& !"".equalsIgnoreCase(adminAccountMeta.getAdminId())) {
			String sQuery = "SELECT * FROM GTADMINTELEPHONYACCOUNT WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(adminAccountMeta
					.getAdminId());

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, true,
					"AdminTelephonyAccountManager.java",
					"getAdminTelephonyAccount()");
			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					AdminTelephonyAccountBean adminTelAccount = new AdminTelephonyAccountBean(
							hmResult);
					arrAdminTelAccount.add(adminTelAccount);
				}
			}
		} else {
			appLogging.info("Invalid data in input.");
		}
		return arrAdminTelAccount;

	}

	public Integer insertAdminAccount(AdminTelephonyAccountMeta adminAccountMeta) {

		Integer iNumOfRows = 0;
		if (adminAccountMeta != null
				&& !"".equalsIgnoreCase(adminAccountMeta.getAdminId())) {
			String sQuery = "INSERT INTO GTADMINTELEPHONYACCOUNT (ADMINTELEPHONYACCOUNT , FK_ADMINID,"
					+ "  ACCOUNTSID, AUTH_TOKEN, DEL_ROW , CREATEDATE , HUMANCREATEDATE ) VALUES (?,?,?, ?,?,?, ?)";

			String sTmpAdminTelAcountId = Utility.getNewGuid();
			Long currentDate = DateSupport.getEpochMillis();
			String sHumanDate = DateSupport.getUTCDateTime();

			ArrayList<Object> aParams = DBDAO.createConstraint(
					sTmpAdminTelAcountId, adminAccountMeta.getAdminId(),
					adminAccountMeta.getAccountSid(),
					adminAccountMeta.getAuthToken(), "0", currentDate,
					sHumanDate);

			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"AdminTelephonyAccountData.java", "insertAdminAccount()");
		} else {
			appLogging.info("Invalid data in input.");
		}
		return iNumOfRows;
	}
}
