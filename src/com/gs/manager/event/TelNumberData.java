package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.DemoTelNumber;
import com.gs.bean.TelNumberBean;
import com.gs.bean.TelNumberTypeBean;
import com.gs.common.db.DBDAO;

public class TelNumberData {
	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public TelNumberBean getTelNumber(TelNumberMetaData telNumberMetaData) {
		TelNumberBean telNumberBean = new TelNumberBean();
		if (telNumberMetaData != null) {
			String sQuery = "select TELNUMBERID, TELNUMBER, FK_TELNUMBERTYPEID, FK_EVENTID ,FK_ADMINID ,DEL_ROW, "
					+ " TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE from GTTELNUMBERS GTT, GTTELNUMBERTYPE GTN WHERE "
					+ " GTT.FK_TELNUMBERTYPEID = GTN.TELNUMBERTYPEID AND GTT.TELNUMBER = ? ";

			ArrayList<Object> aParams = DBDAO.createConstraint(telNumberMetaData.getEventTaskTelNumber());
            appLogging.debug("sQuery : " + sQuery + " Params : " + aParams);
			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData( ADMIN_DB, sQuery, aParams, false, "TelNumberData.java", "getTelNumber()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					telNumberBean = getTelNumberBean(hmResult);
				}
			}

		}
		return telNumberBean;
	}

	public ArrayList<TelNumberTypeBean> getTelNumberTypes() {
		ArrayList<TelNumberTypeBean> arrTelNumTypeBean = getTelNumberTypes(Constants.EVENT_TASK.ALL);

		return arrTelNumTypeBean;
	}

	public ArrayList<TelNumberTypeBean> getTelNumberTypes(Constants.EVENT_TASK telNumberType) {

		ArrayList<TelNumberTypeBean> arrTelNumTypeBean = new ArrayList<TelNumberTypeBean>();

		ArrayList<Object> aParams = new ArrayList<Object>();
		String sQuery = "SELECT * FROM GTTELNUMBERTYPE";

		if (telNumberType != null&& !Constants.EVENT_TASK.ALL.getTask().equalsIgnoreCase(telNumberType.getTask())) {
			sQuery = "SELECT * FROM GTTELNUMBERTYPE WHERE TELNUMTYPE = ?";
            aParams.add(telNumberType.getTask());
		}

		ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
				"getTelNumberTypes()");

		if (arrResult != null && !arrResult.isEmpty()) {
			for (HashMap<String, String> hmResult : arrResult) {
				TelNumberTypeBean telNumTypeBean = new TelNumberTypeBean(hmResult);
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
		telNumberBean.setTelNumberId(ParseUtil.checkNull(hmResult.get("TELNUMBERID")));
		telNumberBean.setTelNumber(ParseUtil.checkNull(hmResult.get("TELNUMBER")));
		telNumberBean.setTelNumberTypeId(ParseUtil.checkNull(hmResult.get("FK_TELNUMBERTYPEID")));
		telNumberBean.setEventId(ParseUtil.checkNull(hmResult.get("FK_EVENTID")));
		telNumberBean.setAdminId(ParseUtil.checkNull(hmResult.get("FK_ADMINID")));
		telNumberBean.setDelRow(ParseUtil.checkNull(hmResult.get("DEL_ROW")));
		telNumberBean.setTelNumberType(ParseUtil.checkNull(hmResult.get("TELNUMTYPE")));

		telNumberBean.setIsactive(ParseUtil.sTob(hmResult.get("IS_ACTIVE")));
		telNumberBean.setPurchased(ParseUtil.sTob(hmResult.get("IS_PURCHASED")));

		telNumberBean.setSecretEventIdentity(ParseUtil.checkNull(hmResult.get("SECRET_EVENT_NUMBER")));
		telNumberBean.setSecretEventKey(ParseUtil.checkNull(hmResult.get("SECRET_KEY")));
		telNumberBean.setHumanTelNumber(ParseUtil.checkNull(hmResult.get("HUMAN_TELNUMBER")));

		return telNumberBean;
	}

	public int updateTelNumber(TelNumberMetaData telNumMetaData,
			TelNumberTypeBean demoTelNumTypeBean) {

		Integer iNumOfRows = 0;
		if (telNumMetaData != null && demoTelNumTypeBean != null) {
			String sQuery = "UPDATE GTTELNUMBERS SET TELNUMBER = ?,FK_TELNUMBERTYPEID = ? , HUMAN_TELNUMBER = ? WHERE FK_ADMINID = ? AND FK_EVENTID = ? "
					+ " AND FK_TELNUMBERTYPEID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					telNumMetaData.getDigits(),
					telNumMetaData.getTelNumberTypeId(),
					telNumMetaData.getHumanTelNumber(),
					telNumMetaData.getAdminId(), telNumMetaData.getEventId(),
					demoTelNumTypeBean.getTelNumberTypeId());

			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "TelNumberData.java", "updateTelNumber()");

		}

		return iNumOfRows;
	}

	/**
	 * Will list out all event tel numbers using the secret event num.
	 * 
	 * @param telNumMetaData
	 * @return ArrayList<TelNumberBean>
	 */
	public ArrayList<TelNumberBean> getTelNumbersFromSecretEventNumAndKey(
			TelNumberMetaData telNumMetaData) {
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		if (telNumMetaData != null
				&& !"".equalsIgnoreCase(telNumMetaData
						.getSecretEventIdentifier())
				&& !"".equalsIgnoreCase(telNumMetaData
						.getSecretEventSecretKey())) {
			String sQuery = "SELECT * FROM  GTTELNUMBERS GTT, GTTELNUMBERTYPE GTN WHERE  GTT.SECRET_EVENT_NUMBER = ? AND GTT.SECRET_KEY = ?"
					+ " AND GTT.FK_TELNUMBERTYPEID = GTN.TELNUMBERTYPEID ";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					telNumMetaData.getSecretEventIdentifier(),
					telNumMetaData.getSecretEventSecretKey());

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
					"getTelNumbersFromSecretEventNumAndKey()");
			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					arrTelNumBean.add(getTelNumberBean(hmResult));
				}
			}
		}
		return arrTelNumBean;
	}

	/**
	 * Will list out all event tel numbers using the secret event num.
	 * 
	 * @param telNumMetaData
	 * @return ArrayList<TelNumberBean>
	 */
	public ArrayList<TelNumberBean> getTelNumbersFromSecretEventNum(
			TelNumberMetaData telNumMetaData) {
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		if (telNumMetaData != null
				&& !"".equalsIgnoreCase(telNumMetaData
						.getSecretEventIdentifier())) {
			String sQuery = "SELECT * FROM GTTELNUMBERS WHERE  SECRET_EVENT_NUMBER = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(telNumMetaData
					.getSecretEventIdentifier());

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, aParams, false, "TelNumberData.java",
					"getTelNumbersFromSecretEventNum()");

			if (arrResult != null && !arrResult.isEmpty()) {
				for (HashMap<String, String> hmResult : arrResult) {
					arrTelNumBean.add(getTelNumberBean(hmResult));
				}
			}
		}
		return arrTelNumBean;
	}

	public int createTelNumber(TelNumberMetaData telNumMetaData) {
		String sQuery = "INSERT INTO GTTELNUMBERS (TELNUMBERID,TELNUMBER,FK_TELNUMBERTYPEID,FK_EVENTID,"
				+ " FK_ADMINID,DEL_ROW,IS_ACTIVE,IS_PURCHASED, SECRET_EVENT_NUMBER, SECRET_KEY, HUMAN_TELNUMBER,CREATEDATE, " +
                "  HUMANCREATEDATE ) "
				+ " VALUES(?,?,?,   ?,?,?,	 ?,?,?,  ?,?,?,  ?)";

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
						(telNumMetaData.getSecretEventIdentifier() != null && !"".equalsIgnoreCase(telNumMetaData.getSecretEventIdentifier())) ? telNumMetaData.getSecretEventIdentifier() : "-",
						(telNumMetaData.getSecretEventSecretKey() != null && !"".equalsIgnoreCase(telNumMetaData.getSecretEventSecretKey())) ? telNumMetaData.getSecretEventSecretKey() : "-",
						(telNumMetaData.getHumanTelNumber() != null && !"".equalsIgnoreCase(telNumMetaData.getHumanTelNumber())) ? telNumMetaData.getHumanTelNumber() : telNumMetaData.getDigits(),
                        telNumMetaData.getCurrentTime() , telNumMetaData.getCurrentHumanTime() );

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
