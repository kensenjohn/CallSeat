package com.gs.data.event;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.bean.TableGuestsBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;

public class GuestTableData
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public HashMap<Integer, TableGuestsBean> getAllTablesGuest(String sEventId)
	{
		HashMap<Integer, TableGuestsBean> hmTableGuests = new HashMap<Integer, TableGuestsBean>();

		String sQuery = "SELECT  "
				+ " GT.TABLEID, GT.TABLENAME, GT.TABLENUM, GT.NUMOFSEATS, GT.IS_TMP, GT.DEL_ROW, GT.FK_ADMINID, "
				+ " GT.MODIFYDATE, GT.MODIFIEDBY, GTG.TABLEGUESTID, GTG.FK_GUESTID, "
				+ " GTG.IS_TMP AS GUEST_IS_TMP , GTG.DEL_ROW AS GUEST_DEL_ROW, GTG.ASSIGNED_SEATS "
				+ " FROM GTEVENT GE "
				+ " LEFT OUTER JOIN GTEVENTTABLES GEVT ON GE.EVENTID = GEVT.FK_EVENTID "
				+ " LEFT OUTER JOIN  GTTABLE GT ON GEVT.FK_TABLEID = GT.TABLEID "
				+ " LEFT OUTER JOIN GTTABLEGUESTS GTG ON GT.TABLEID = GTG.FK_TABLEID "
				+ " LEFT OUTER JOIN GTGUESTS GG ON GTG.FK_GUESTID = GG.GUESTID "
				+ " LEFT OUTER JOIN GTUSERINFO GU ON GG.FK_USERINFOID = GU.USERINFOID "
				+ " WHERE GE.EVENTID=? ";

		ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

		ArrayList<HashMap<String, String>> arrTableGuestsRes = DBDAO.getDBData(ADMIN_DB, sQuery,
				aParams, true, "GuestTableData.java", "getAllTablesGuest()");

		if (arrTableGuestsRes != null && !arrTableGuestsRes.isEmpty())
		{
			Integer iTableGuestNum = 0;
			for (HashMap<String, String> hmTableGuest : arrTableGuestsRes)
			{
				TableGuestsBean tableGuestsBean = new TableGuestsBean();

				tableGuestsBean.setTableId(ParseUtil.checkNull(hmTableGuest.get("TABLEID")));
				tableGuestsBean.setTableName(ParseUtil.checkNull(hmTableGuest.get("TABLENAME")));
				tableGuestsBean.setTableNum(ParseUtil.checkNull(hmTableGuest.get("NUMOFSEATS")));
				tableGuestsBean.setIsTemporary(ParseUtil.checkNull(hmTableGuest.get("IS_TMP")));
				tableGuestsBean.setDelelteRow(ParseUtil.checkNull(hmTableGuest.get("DEL_ROW")));
				tableGuestsBean.setAdminId(ParseUtil.checkNull(hmTableGuest.get("FK_ADMINID")));
				tableGuestsBean.setCreateDate(ParseUtil.sToL(hmTableGuest.get("CREATEDATE")));
				tableGuestsBean.setModifyDate(ParseUtil.sToL(hmTableGuest.get("MODIFYDATE")));
				tableGuestsBean.setModifiedBy(ParseUtil.checkNull(hmTableGuest.get("MODIFIEDBY")));
				tableGuestsBean.setTableGuestId(ParseUtil.checkNull(hmTableGuest
						.get("TABLEGUESTID")));
				tableGuestsBean.setGuestId(ParseUtil.checkNull(hmTableGuest.get("FK_GUESTID")));
				tableGuestsBean.setGuestTableIsTmp(ParseUtil.checkNull(hmTableGuest
						.get("GUEST_IS_TMP")));
				tableGuestsBean.setGuestTableDelRow(ParseUtil.checkNull(hmTableGuest
						.get("GUEST_DEL_ROW")));
				tableGuestsBean.setGuestAssignedSeats(ParseUtil.checkNull(hmTableGuest
						.get("ASSIGNED_SEATS")));

				hmTableGuests.put(iTableGuestNum, tableGuestsBean);
				iTableGuestNum++;

			}
		}

		return hmTableGuests;
	}
}
