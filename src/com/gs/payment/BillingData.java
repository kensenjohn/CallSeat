package com.gs.payment;

import java.util.ArrayList;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;

public class BillingData {
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public String insertBillingAddress(BillingMetaData billingMetaData) {

		String sBillingAddressID = "";
		if (billingMetaData != null) {
			String sQuery = "INSERT INTO GTBILLADDRESS ( BILLADDRESSID , FK_ADMINID , FK_EVENTID , "
					+ " FIRSTNAME , MIDDLENAME , LASTNAME , ADDRESS1, ADDRESS2, CITY, STATE, COUNTRY, ZIP ) "
					+ " VALUES ( ?,?,? ,?,?,? ,?,?,? ,?,?,?)";

			String sTmpUID = Utility.getNewGuid();
			ArrayList<Object> aParam = new ArrayList<Object>();

			aParam.add(sTmpUID);
			aParam.add(billingMetaData.getAdminId());
			aParam.add(billingMetaData.getEventId());
			aParam.add(billingMetaData.getFirstName());
			aParam.add(billingMetaData.getLastName());
			aParam.add(billingMetaData.getMiddletName());
			aParam.add(billingMetaData.getAddress1());
			aParam.add(billingMetaData.getAddress2());
			aParam.add(billingMetaData.getCity());
			aParam.add(billingMetaData.getState());
			aParam.add(billingMetaData.getCountry());
			aParam.add(billingMetaData.getZip());

			DBDAO.putRowsQuery(sQuery, aParam, ADMIN_DB, "BillingData.java",
					"insertBillingAddress()");

			sBillingAddressID = sTmpUID;
		}
		return sBillingAddressID;
	}

	public Integer insertBillingCreditCard(BillingMetaData billingMetaData,
			String sBillingAddressId) {
		Integer iNumOfRecs = 0;
		if (billingMetaData != null && sBillingAddressId != null
				&& !"".equalsIgnoreCase(sBillingAddressId)) {
			String sQuery = "INSERT INTO GTBILLCREDITCARD ( BILLCREDITCARDID  , FK_BILLADDRESSID  , FK_ADMINID , FK_EVENTID , "
					+ "   CREDITCARDNUM , SECURITYCODE, AMOUNT ) "
					+ " VALUES ( ?,?,? ,?,?,? ,? )";

			String sTmpUID = Utility.getNewGuid();
			ArrayList<Object> aParam = new ArrayList<Object>();

			aParam.add(sTmpUID);
			aParam.add(sBillingAddressId);
			aParam.add(billingMetaData.getAdminId());
			aParam.add(billingMetaData.getEventId());
			aParam.add(billingMetaData.getCreditCardNum());
			aParam.add(billingMetaData.getSecureNum());
			aParam.add(billingMetaData.getPrice());

			iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParam, ADMIN_DB,
					"BillingData.java", "insertBillingCreditCard()");
		}
		return iNumOfRecs;
	}
}
