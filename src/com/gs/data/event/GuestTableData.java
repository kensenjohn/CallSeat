package com.gs.data.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AssignedGuestBean;
import com.gs.bean.TableGuestsBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;

public class GuestTableData
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public HashMap<Integer, AssignedGuestBean> getUnAssignedGuest(String sEventId)
	{
		HashMap<Integer, AssignedGuestBean> hmTableGuests = new HashMap<Integer, AssignedGuestBean>();

		String sQuery = "SELECT GG.GUESTID ,  GU.FIRST_NAME, GU.LAST_NAME ,GE.EVENTID "
				+ " FROM GTGUESTS GG "
				+ " INNER JOIN GTUSERINFO GU ON GG.FK_USERINFOID = GU.USERINFOID "
				+ " INNER JOIN GTEVENTGUESTS GEG ON GG.GUESTID = GEG.FK_GUESTID "
				+ " INNER JOIN  GTEVENT GE ON GEG.FK_EVENTID = GE.EVENTID "
				+ " INNER JOIN GTEVENTTABLES GET ON  GET.FK_EVENTID = GE.EVENTID "
				+ " LEFT OUTER JOIN GTTABLEGUESTS GTG ON  GTG.FK_GUESTID = GG.GUESTID "
				+ " WHERE  GE.EVENTID = ?";

		ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

		ArrayList<HashMap<String, String>> arrAssignedGuestsRes = DBDAO.getDBData(ADMIN_DB, sQuery,
				aParams, true, "GuestTableData.java", "getUnAssignedGuest()");

		if (arrAssignedGuestsRes != null && !arrAssignedGuestsRes.isEmpty())
		{
			Integer iNumOfRows = 0;
			for (HashMap<String, String> hmAssignedGuests : arrAssignedGuestsRes)
			{
				AssignedGuestBean assignedGuestBean = new AssignedGuestBean(hmAssignedGuests);

				hmTableGuests.put(iNumOfRows, assignedGuestBean);
			}
		}
		return hmTableGuests;
	}

	public HashMap<Integer, AssignedGuestBean> getTableGuest(String sEventId, String sTableId)
	{
		HashMap<Integer, AssignedGuestBean> hmTableGuests = new HashMap<Integer, AssignedGuestBean>();

		String sQuery = "SELECT GG.GUESTID , GU.FIRST_NAME, GU.LAST_NAME, GU.CELL_PHONE, GU.PHONE_NUM, "
				+ " GEG.TOTAL_INVITED_SEATS, GEG.RSVP_SEATS, GTG.ASSIGNED_SEATS, GT.NUMOFSEATS FROM GTGUESTS GG , "
				+ "GTUSERINFO GU ,  GTEVENT GE , GTEVENTGUESTS GEG , GTEVENTTABLES GET, GTTABLE GT , "
				+ "GTTABLEGUESTS GTG "
				+ " WHERE "
				+ " GG.FK_USERINFOID = GU.USERINFOID AND GE.EVENTID = GEG.FK_EVENTID AND "
				+ " GEG.FK_GUESTID = GG.GUESTID AND GE.EVENTID = ?  AND GET.FK_TABLEID = GT.TABLEID AND "
				+ " GET.FK_EVENTID = GE.EVENTID AND GT.TABLEID = ? AND GTG.FK_TABLEID = GT.TABLEID AND "
				+ " GTG.FK_GUESTID = GG.GUESTID ";

		ArrayList<Object> aParams = DBDAO.createConstraint(sEventId, sTableId);

		ArrayList<HashMap<String, String>> arrAssignedGuestsRes = DBDAO.getDBData(ADMIN_DB, sQuery,
				aParams, true, "GuestTableData.java", "getAllTablesGuest()");

		if (arrAssignedGuestsRes != null && !arrAssignedGuestsRes.isEmpty())
		{
			Integer iNumOfRows = 0;
			for (HashMap<String, String> hmAssignedGuests : arrAssignedGuestsRes)
			{
				AssignedGuestBean assignedGuestBean = new AssignedGuestBean(hmAssignedGuests);

				hmTableGuests.put(iNumOfRows, assignedGuestBean);
			}
		}
		return hmTableGuests;
	}

	public HashMap<Integer, TableGuestsBean> getAllTablesGuest(String sEventId)
	{
		HashMap<Integer, TableGuestsBean> hmTableGuests = new HashMap<Integer, TableGuestsBean>();

		String sQuery = "SELECT  "
				+ " GT.TABLEID, GT.TABLENAME, GT.TABLENUM, GT.NUMOFSEATS, GT.IS_TMP, GT.DEL_ROW, GT.FK_ADMINID, "
				+ " GT.MODIFYDATE, GT.MODIFIEDBY, GTG.TABLEGUESTID, GTG.FK_GUESTID, "
				+ " GTG.IS_TMP AS GUEST_IS_TMP , GTG.DEL_ROW AS GUEST_DEL_ROW, GTG.ASSIGNED_SEATS, "
				+ " GG.GUESTID, GU.FIRST_NAME, GU.LAST_NAME, GU.CELL_PHONE, GU.PHONE_NUM , GEG.RSVP_SEATS, "
				+ " GEG.TOTAL_INVITED_SEATS "
				+ " FROM GTEVENT GE "
				+ " LEFT OUTER JOIN GTEVENTTABLES GEVT ON GE.EVENTID = GEVT.FK_EVENTID "
				+ " LEFT OUTER JOIN  GTTABLE GT ON GEVT.FK_TABLEID = GT.TABLEID "
				+ " LEFT OUTER JOIN GTTABLEGUESTS GTG ON GT.TABLEID = GTG.FK_TABLEID "
				+ " LEFT OUTER JOIN ( GTGUESTS GG INNER JOIN GTEVENTGUESTS GEG ON GEG.FK_GUESTID = GG.GUESTID )"
				+ " ON GTG.FK_GUESTID = GG.GUESTID "
				+ " LEFT OUTER JOIN GTUSERINFO GU ON GG.FK_USERINFOID = GU.USERINFOID "
				+ " WHERE GE.EVENTID=? ORDER BY GT.CREATEDATE DESC";

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
				tableGuestsBean.setTableNum(ParseUtil.checkNull(hmTableGuest.get("TABLENUM")));
				tableGuestsBean.setNumOfSeats(ParseUtil.checkNull(hmTableGuest.get("NUMOFSEATS")));
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

				/*
				 * GG.GUESTID, GU.FIRST_NAME, GU.LAST_NAME, GU.CELL_PHONE,
				 * GU.PHONE_NUM , GEG.RSVP_SEATS, " + " GEG.TOTAL_INVITED_SEATS
				 */
				tableGuestsBean.setFirstName(ParseUtil.checkNull(hmTableGuest.get("FIRST_NAME")));
				tableGuestsBean.setLastName(ParseUtil.checkNull(hmTableGuest.get("LAST_NAME")));
				tableGuestsBean.setCellPhone(ParseUtil.checkNull(hmTableGuest.get("CELL_PHONE")));
				tableGuestsBean.setPhoneNum(ParseUtil.checkNull(hmTableGuest.get("PHONE_NUM")));
				tableGuestsBean.setRsvpSeats(ParseUtil.checkNull(hmTableGuest.get("RSVP_SEATS")));
				tableGuestsBean.setTotalInvitedSeats(ParseUtil.checkNull(hmTableGuest
						.get("TOTAL_INVITED_SEATS")));

				hmTableGuests.put(iTableGuestNum, tableGuestsBean);
				iTableGuestNum++;

			}
		}

		return hmTableGuests;
	}

	public Integer deleteGuestTable(TableGuestsBean tableGuestBean)
	{
		Integer numOfRows = 0;
		if (tableGuestBean != null && tableGuestBean.getTableId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getTableId()))
		{

			String sQuery = "DELETE FROM GTTABLEGUESTS WHERE FK_TABLEID = ? ";

			ArrayList<Object> aParams = DBDAO.createConstraint(tableGuestBean.getTableId());

			numOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "GuestTableData.java",
					"deleteGuestTable()");
		}
		return numOfRows;
	}

	public Integer insertGuestTableAssignment(TableGuestsBean tableGuestBean)
	{
		appLogging.info("tableGuestBean = " + tableGuestBean);
		Integer iNumOfRows = 0;
		if (tableGuestBean != null && tableGuestBean.getTableId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getTableId())
				&& tableGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestId())
				&& tableGuestBean.getGuestAssignedSeats() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestAssignedSeats()))
		{
			appLogging.info("Going to insert data");

			String sQuery = "INSERT INTO GTTABLEGUESTS (TABLEGUESTID,FK_TABLEID,FK_GUESTID, IS_TMP , DEL_ROW, ASSIGNED_SEATS ) "
					+ " VALUES (?,?,?,?,?, ?) ";

			ArrayList<Object> aParams = DBDAO.createConstraint(tableGuestBean.getTableGuestId(),
					tableGuestBean.getTableId(), tableGuestBean.getGuestId(),
					tableGuestBean.getIsTemporary(), tableGuestBean.getDelelteRow(),
					tableGuestBean.getGuestAssignedSeats());
			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "GuestTableData.java",
					"insertGuestTable()");
		}

		return iNumOfRows;
	}

	public Integer updateGuestTableAssignment(TableGuestsBean tableGuestBean)
	{
		Integer iNumOfRows = 0;
		if (tableGuestBean != null && tableGuestBean.getTableId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getTableId())
				&& tableGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestId())
				&& tableGuestBean.getGuestAssignedSeats() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestAssignedSeats()))
		{
			String sQuery = "UPDATE GTTABLEGUESTS SET  ASSIGNED_SEATS = ? "
					+ " WHERE  FK_TABLEID = ? and FK_GUESTID = ? ";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					tableGuestBean.getGuestAssignedSeats(), tableGuestBean.getTableId(),
					tableGuestBean.getGuestId());
			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "GuestTableData.java",
					"insertGuestTable()");
		}

		return iNumOfRows;
	}

	public ArrayList<TableGuestsBean> getGuestAssgnments(String sGuestId)
	{
		ArrayList<TableGuestsBean> arrTableGuestBean = new ArrayList<TableGuestsBean>();
		if (sGuestId != null && !"".equalsIgnoreCase(sGuestId))
		{
			String sQuery = "select GTB.TABLEID,GTB.TABLENAME,GTB.TABLENUM,GTB.NUMOFSEATS,GTB.IS_TMP,GTB.DEL_ROW, "
					+ " GTB.CREATEDATE,GTB.FK_ADMINID,GTB.MODIFYDATE,GTB.MODIFIEDBY,GTB.HUMAN_CREATEDATE,GTB.HUMAN_MODIFYDATE, "
					+ " GTG.TABLEGUESTID,GTG.FK_TABLEID,GTG.FK_GUESTID,GTG.IS_TMP,GTG.DEL_ROW,GTG.ASSIGNED_SEATS FROM GTTABLE GTB, "
					+ " GTTABLEGUESTS GTG WHERE GTG.FK_GUESTID=? "
					+ " and GTG.FK_TABLEID = GTB.TABLEID";

			ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);

			ArrayList<HashMap<String, String>> arrGuestAssignment = DBDAO.getDBData(ADMIN_DB,
					sQuery, aParams, false, "GuestTableData.java", "getGuestAssgnments()");

			if (arrGuestAssignment != null && !arrGuestAssignment.isEmpty())
			{
				for (HashMap<String, String> hmGuestAssignment : arrGuestAssignment)
				{
					TableGuestsBean tableGuestsBean = new TableGuestsBean();
					tableGuestsBean.setGuestAssignedSeats(ParseUtil.checkNull(hmGuestAssignment
							.get("ASSIGNED_SEATS")));

					tableGuestsBean.setGuestId(ParseUtil.checkNull(hmGuestAssignment
							.get("FK_GUESTID")));

					tableGuestsBean.setAdminId(ParseUtil.checkNull(hmGuestAssignment
							.get("FK_ADMINID")));

					tableGuestsBean.setCreateDate(ParseUtil.sToL(hmGuestAssignment
							.get("CREATEDATE")));
					tableGuestsBean
							.setTableId(ParseUtil.checkNull(hmGuestAssignment.get("TABLEID")));
					tableGuestsBean.setTableName(ParseUtil.checkNull(hmGuestAssignment
							.get("TABLENAME")));
					tableGuestsBean.setTableNum(ParseUtil.checkNull(hmGuestAssignment
							.get("TABLENUM")));
					tableGuestsBean.setNumOfSeats(ParseUtil.checkNull(hmGuestAssignment
							.get("NUMOFSEATS")));
					tableGuestsBean.setIsTemporary(ParseUtil.checkNull(hmGuestAssignment
							.get("IS_TMP")));
					tableGuestsBean.setDelelteRow(ParseUtil.checkNull(hmGuestAssignment
							.get("DEL_ROW")));

					arrTableGuestBean.add(tableGuestsBean);
				}
			}

		}
		return arrTableGuestBean;
	}
}
