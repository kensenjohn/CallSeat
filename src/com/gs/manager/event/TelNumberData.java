package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.DemoTelNumber;
import com.gs.bean.TelNumberBean;
import com.gs.bean.TelNumberTypeBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
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

	public ArrayList<TelNumberTypeBean> getTelNumberTypes() {
		ArrayList<TelNumberTypeBean> arrTelNumTypeBean = getTelNumberTypes(Constants.EVENT_TASK.ALL
				.getTask());

		return arrTelNumTypeBean;
	}

	public ArrayList<TelNumberTypeBean> getTelNumberTypes(String sTelNumberType) {

		ArrayList<TelNumberTypeBean> arrTelNumTypeBean = new ArrayList<TelNumberTypeBean>();

		ArrayList<Object> aParams = new ArrayList<Object>();
		String sQuery = "SELECT * FROM GTTELNUMBERTYPE";

		if (sTelNumberType != null && !"".equalsIgnoreCase(sTelNumberType)) {
			sQuery = "SELECT * FROM GTTELNUMBERTYPE WHERE TELNUMTYPE = ?";

			if (Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(
					sTelNumberType)) {
				aParams.add(Constants.EVENT_TASK.RSVP.getTask());
			} else if (Constants.EVENT_TASK.SEATING.getTask().equalsIgnoreCase(
					sTelNumberType)) {
				aParams.add(Constants.EVENT_TASK.SEATING.getTask());
			} else if (Constants.EVENT_TASK.DEMO_RSVP.getTask()
					.equalsIgnoreCase(sTelNumberType)) {
				aParams.add(Constants.EVENT_TASK.DEMO_RSVP.getTask());
			} else if (Constants.EVENT_TASK.DEMO_SEATING.getTask()
					.equalsIgnoreCase(sTelNumberType)) {
				aParams.add(Constants.EVENT_TASK.DEMO_SEATING.getTask());
			}
		}

		ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
				ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
				"getTelNumberTypes()");

		if (arrResult != null && !arrResult.isEmpty()) {
			for (HashMap<String, String> hmResult : arrResult) {
				TelNumberTypeBean telNumTypeBean = new TelNumberTypeBean(
						hmResult);
				arrTelNumTypeBean.add(telNumTypeBean);
			}
		}
		return arrTelNumTypeBean;
	}

	public ArrayList<TelNumberBean> getEventTelNumbers(
			TelNumberMetaData telNumberMetaData) {
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();
		if (telNumberMetaData != null) {
			String sQuery = "SELECT GTT.TELNUMBERID,  GTT.TELNUMBER,  GTT.FK_TELNUMBERTYPEID,  "
					+ " GTT.FK_EVENTID , GTT.FK_ADMINID , GTT.DEL_ROW, GTT.SECRET_EVENT_NUMBER, "
					+ " GTT.SECRET_KEY , GTT.HUMAN_TELNUMBER, "
					+ "  GTN.TELNUMBERTYPEID,GTN.DESCRIPTION,GTN.TELNUMTYPE from GTTELNUMBERS GTT, GTTELNUMBERTYPE GTN WHERE "
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

		telNumberBean.setSecretEventIdentity(ParseUtil.checkNull(hmResult
				.get("SECRET_EVENT_NUMBER")));
		telNumberBean.setSecretEventKey(ParseUtil.checkNull(hmResult
				.get("SECRET_KEY")));
		telNumberBean.setHumanTelNumber(ParseUtil.checkNull(hmResult
				.get("HUMAN_TELNUMBER")));

		return telNumberBean;
	}

	public int createTelNumber(TelNumberMetaData telNumMetaData) {
		/*
		 * -------------------+-------------+------+-----+---------+-------+ |
		 * TELNUMBERID | varchar(45) | NO | PRI | NULL | | | TELNUMBER |
		 * varchar(45) | NO | | NULL | | | FK_TELNUMBERTYPEID | varchar(45) | NO
		 * | | NULL | | | FK_EVENTID | varchar(45) | NO | | NULL | | |
		 * FK_ADMINID | varchar(45) | NO | | NULL | | | DEL_ROW | int(1) | NO |
		 * | 0 | | | IS_ACTIVE | int(1) | YES | | 0 | | | IS_PURCHASED
		 */
		String sQuery = "INSERT INTO GTTELNUMBERS (TELNUMBERID,TELNUMBER,FK_TELNUMBERTYPEID,FK_EVENTID,"
				+ " FK_ADMINID,DEL_ROW,IS_ACTIVE,IS_PURCHASED, SECRET_EVENT_NUMBER, SECRET_KEY, HUMAN_TELNUMBER ) "
				+ " VALUES(?,?,?,   ?,?,?,	 ?,?,?,  ?,?)";

		String sTelNumberID = Utility.getNewGuid();
		ArrayList<Object> aParams = DBDAO
				.createConstraint(
						sTelNumberID,
						telNumMetaData.getDigits(),
						telNumMetaData.getTelNumberTypeId(),
						telNumMetaData.getEventId(),
						telNumMetaData.getAdminId(),
						telNumMetaData.isDelRow() ? "1" : "0",
						telNumMetaData.isActive() ? "1" : "0",
						telNumMetaData.isPurchased() ? "1" : "0",
						(telNumMetaData.getSecretEventIdentifier() != null && !""
								.equalsIgnoreCase(telNumMetaData
										.getSecretEventIdentifier())) ? telNumMetaData
								.getSecretEventIdentifier() : "-",
						(telNumMetaData.getSecretEventSecretKey() != null && !""
								.equalsIgnoreCase(telNumMetaData
										.getSecretEventSecretKey())) ? telNumMetaData
								.getSecretEventSecretKey() : "-",
						(telNumMetaData.getHumanTelNumber() != null && !""
								.equalsIgnoreCase(telNumMetaData
										.getHumanTelNumber())) ? telNumMetaData
								.getHumanTelNumber() : telNumMetaData
								.getDigits());

		int iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
				"TelNumberData.java", "createTelNumber()");

		return iNumOfRows;
	}

	public ArrayList<TelNumberBean> updateTelNumber() {
		return null;
	}

	public ArrayList<DemoTelNumber> getDemoTelNumberList() {

		ArrayList<DemoTelNumber> arrDemoTelNumber = new ArrayList<DemoTelNumber>();

		String sQuery = "SELECT * FROM GTDEMOTELNUMBERS";
		ArrayList<Object> aParams = new ArrayList<Object>();

		ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
				ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
				"getDemoTelNumberList()");

		if (arrResult != null && !arrResult.isEmpty()) {
			for (HashMap<String, String> hmResult : arrResult) {
				DemoTelNumber demoTelNumber = new DemoTelNumber(hmResult);

				arrDemoTelNumber.add(demoTelNumber);
			}
		}
		return arrDemoTelNumber;
	}
}
