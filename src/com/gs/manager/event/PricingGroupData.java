package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.PricingGroupBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;

public class PricingGroupData {

	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public PricingGroupBean getPricingGroup(String sPricingGroupId) {

		PricingGroupBean pricingGroupBean = new PricingGroupBean();
		if (sPricingGroupId != null && !"".equalsIgnoreCase(sPricingGroupId)) {
			String sQuery = "select PRICEGROUPID, PRICEGROUPNAME,  MIN_GUEST_NUM,  MAX_GUEST_NUM , PRICE "
					+ " from GTPRICEGROUP GPG WHERE PRICEGROUPID = ?";

			ArrayList<Object> aParams = new ArrayList<Object>();
			aParams.add(sPricingGroupId);

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "PricingGroupData.java",
					"getPricingGroups()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {

					pricingGroupBean = new PricingGroupBean(hmResult);
					break;
				}
			}
		}
		return pricingGroupBean;
	}

	public ArrayList<PricingGroupBean> getAllPricingGroups() {

		ArrayList<PricingGroupBean> arrPricingGroupBean = new ArrayList<PricingGroupBean>();

		String sQuery = "select PRICEGROUPID, PRICEGROUPNAME,  MIN_GUEST_NUM,  MAX_GUEST_NUM , "
				+ " PRICE, MAX_MINUTES, MIN_MINUTES,SMS_COUNT from GTPRICEGROUP GPG";

		ArrayList<Object> aParams = new ArrayList<Object>();

		ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
				ADMIN_DB, sQuery, aParams, false, "PricingGroupData.java",
				"getPricingGroups()");

		if (arrResult != null && !arrResult.isEmpty()) {
			for (HashMap<String, String> hmResult : arrResult) {
				PricingGroupBean pricingGroupBean = new PricingGroupBean();
				pricingGroupBean = getPricingGroupBean(hmResult);

				arrPricingGroupBean.add(pricingGroupBean);
			}
		}
		return arrPricingGroupBean;
	}

	private PricingGroupBean getPricingGroupBean(
			HashMap<String, String> hmResult) {
		PricingGroupBean pricingGroupBean = new PricingGroupBean(hmResult);

		return pricingGroupBean;
	}
}
