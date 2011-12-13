package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TelNumberBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;

public class TelNumberData {
	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public TelNumberBean getTelNumber(TelNumberMetaData telNumberMetaData) {
		TelNumberBean telNumberBean = new TelNumberBean();
		if (telNumberMetaData != null) {
			String sQuery = "select TELNUMBERID, TELNUMBER, FK_TELNUMBERTYPEID, FK_EVENTID ,FK_ADMINID ,DEL_ROW, "
					+ " TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE from GTTELNUMBERS GTT, GTTELNUMBERTYPE GTN WHERE "
					+ " GTT.FK_TELNUMBERTYPEID = GTN.TELNUMBERTYPEID AND GTT.TELNUMBER = ? ";

			ArrayList<Object> aParams = DBDAO
					.createConstraint(telNumberMetaData.getEventTaskTelNumber());

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
					"getTelNumber()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					telNumberBean = getTelNumberBean(hmResult);
				}
			}

		}
		return telNumberBean;
	}

	public ArrayList<TelNumberBean> getEventTelNumbers(
			TelNumberMetaData telNumberMetaData) {
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();
		if (telNumberMetaData != null) {
			String sQuery = "SELECT TELNUMBERID, TELNUMBER, FK_TELNUMBERTYPEID, FK_EVENTID ,FK_ADMINID ,DEL_ROW, "
					+ " TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE from GTTELNUMBERS GTT, GTTELNUMBERTYPE GTN WHERE "
					+ " GTT.FK_TELNUMBERTYPEID = GTN.TELNUMBERTYPEID AND GTT.FK_EVENTID = ? AND  GTT.FK_ADMINID = ? ";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					telNumberMetaData.getEventId(),
					telNumberMetaData.getAdminId());

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
					"getEventTelNumbers()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					arrTelNumberBean.add(getTelNumberBean(hmResult));
				}
			}

		}
		return arrTelNumberBean;
	}

	private TelNumberBean getTelNumberBean(HashMap<String, String> hmResult) {
		TelNumberBean telNumberBean = new TelNumberBean();
		telNumberBean.setTelNumberId(ParseUtil.checkNull(hmResult
				.get("TELNUMBERID")));
		telNumberBean.setTelNumber(ParseUtil.checkNull(hmResult
				.get("TELNUMBER")));
		telNumberBean.setTelNumberTypeId(ParseUtil.checkNull(hmResult
				.get("FK_TELNUMBERTYPEID")));
		telNumberBean
				.setEventId(ParseUtil.checkNull(hmResult.get("FK_EVENTID")));
		telNumberBean
				.setAdminId(ParseUtil.checkNull(hmResult.get("FK_ADMINID")));
		telNumberBean.setDelRow(ParseUtil.checkNull(hmResult.get("DEL_ROW")));
		telNumberBean.setTelNumberType(ParseUtil.checkNull(hmResult
				.get("TELNUMTYPE")));

		telNumberBean.setIsactive(ParseUtil.sTob(hmResult.get("IS_ACTIVE")));
		telNumberBean
				.setPurchased(ParseUtil.sTob(hmResult.get("IS_PURCHASED")));

		return telNumberBean;
	}
}
