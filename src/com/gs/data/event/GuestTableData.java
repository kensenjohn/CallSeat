package com.gs.data.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AssignedGuestBean;
import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.TableBean;
import com.gs.bean.TableGuestsBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;
import com.gs.data.GuestData;

public class GuestTableData {
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public HashMap<Integer, AssignedGuestBean> getUnAssignedGuest(
			String sEventId) {
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

		ArrayList<HashMap<String, String>> arrAssignedGuestsRes = DBDAO
				.getDBData(ADMIN_DB, sQuery, aParams, true,
						"GuestTableData.java", "getUnAssignedGuest()");

		if (arrAssignedGuestsRes != null && !arrAssignedGuestsRes.isEmpty()) {
			Integer iNumOfRows = 0;
			for (HashMap<String, String> hmAssignedGuests : arrAssignedGuestsRes) {
				AssignedGuestBean assignedGuestBean = new AssignedGuestBean(
						hmAssignedGuests);

				hmTableGuests.put(iNumOfRows, assignedGuestBean);
			}
		}
		return hmTableGuests;
	}

	/**
	 * @param sEventId
	 * @param sTableId
	 * @return
	 */
	/**
	 * @param sEventId
	 * @param sTableId
	 * @return
	 */
	public HashMap<Integer, AssignedGuestBean> getTableGuest(String sEventId,
			String sTableId) {
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

		String sTableGuestQuery = "select * from GTTABLEGUESTS GTG, GTEVENTTABLES GTE, GTTABLE GT WHERE "
				+ " GT.TABLEID=GTG.FK_TABLEID AND GTG.FK_TABLEID=? "
				+ " AND GTE.FK_TABLEID = GTG.FK_TABLEID AND GTE.FK_EVENTID = ? ";

		ArrayList<Object> aTableGuestParams = DBDAO.createConstraint(sTableId,
				sEventId);

		ArrayList<HashMap<String, String>> arrTableGuestsRes = DBDAO.getDBData(
				ADMIN_DB, sTableGuestQuery, aTableGuestParams, true,
				"GuestTableData.java", "getTableGuest()");

		if (arrTableGuestsRes != null && !arrTableGuestsRes.isEmpty()) {
			ArrayList<String> arrGuestId = new ArrayList<String>();

			HashMap<String, AssignedGuestBean> hmGuestAssignedSeats = new HashMap<String, AssignedGuestBean>();
			for (HashMap<String, String> hmTableGuest : arrTableGuestsRes) {

				AssignedGuestBean assigneGuestBean = new AssignedGuestBean();

				String sGuestId = ParseUtil.checkNull(hmTableGuest
						.get("FK_GUESTID"));
				String sAssignedSeatAtTable = ParseUtil.checkNull(hmTableGuest
						.get("ASSIGNED_SEATS"));

				assigneGuestBean.setGuestId(sGuestId);
				assigneGuestBean.setAssignedSeats(sAssignedSeatAtTable);
				hmGuestAssignedSeats.put(sGuestId, assigneGuestBean);

				arrGuestId.add(sGuestId);
			}
			if (arrGuestId != null && !arrGuestId.isEmpty()) {
				GuestData guestData = new GuestData();
				ArrayList<EventGuestBean> arrEventGuestBean = guestData
						.getEventGuests(sEventId, arrGuestId);

				if (arrEventGuestBean != null && !arrEventGuestBean.isEmpty()
						&& hmGuestAssignedSeats != null
						&& !hmGuestAssignedSeats.isEmpty()) {
					Set<String> setGuestId = hmGuestAssignedSeats.keySet();

					Integer iNumOfRows = 0;
					for (String sGuestID : setGuestId) {
						for (EventGuestBean eventGuestBean : arrEventGuestBean) {
							if (eventGuestBean.getGuestId().equalsIgnoreCase(
									sGuestID)) {

								GuestBean guestBean = eventGuestBean
										.getGuestBean();
								UserInfoBean userInfoBean = guestBean
										.getUserInfoBean();

								AssignedGuestBean assigneGuestBean = hmGuestAssignedSeats
										.get(sGuestID);

								assigneGuestBean.setFirstName(userInfoBean
										.getFirstName());
								assigneGuestBean.setLastName(userInfoBean
										.getLastName());
								assigneGuestBean.setCellNumber(userInfoBean
										.getCellPhone());
								assigneGuestBean.setHomeNumber(userInfoBean
										.getPhoneNum());

								assigneGuestBean.setRsvpSeats(eventGuestBean
										.getRsvpSeats());
								hmTableGuests.put(iNumOfRows, assigneGuestBean);
								iNumOfRows++;
							}
						}
					}
				}

			}

		}

		/*
		 * ArrayList<Object> aParams = DBDAO.createConstraint(sEventId,
		 * sTableId);
		 * 
		 * ArrayList<HashMap<String, String>> arrAssignedGuestsRes = DBDAO
		 * .getDBData(ADMIN_DB, sQuery, aParams, true, "GuestTableData.java",
		 * "getAllTablesGuest()");
		 * 
		 * if (arrAssignedGuestsRes != null && !arrAssignedGuestsRes.isEmpty())
		 * { Integer iNumOfRows = 0; for (HashMap<String, String>
		 * hmAssignedGuests : arrAssignedGuestsRes) { AssignedGuestBean
		 * assignedGuestBean = new AssignedGuestBean( hmAssignedGuests);
		 * 
		 * hmTableGuests.put(iNumOfRows, assignedGuestBean); } }
		 */
		return hmTableGuests;
	}

	public HashMap<Integer, TableGuestsBean> getAllTablesGuest(String sEventId) {
		HashMap<Integer, TableGuestsBean> hmTableGuests = new HashMap<Integer, TableGuestsBean>();

		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			TableData tableData = new TableData();
			// get all tables created for this event.
			HashMap<Integer, TableBean> hmTables = tableData.getEventTables(sEventId);
			if (hmTables != null && !hmTables.isEmpty()) 
			{
				Set<Integer> setTableNumber = hmTables.keySet();
				// create an arraylist of table ids of this event.
				ArrayList<String> arrTableId = new ArrayList<String>();
				for (Integer iNumber : setTableNumber) {
					TableBean tableBean = hmTables.get(iNumber);
					arrTableId.add(tableBean.getTableId());
				}
				
				//get all guests who are assigned to this event's tables
				hmTableGuests = getTableGuest(arrTableId);

				//ArrayList<String> arrTmpGuestTableId = new ArrayList<String>();
				HashMap<String,String> hmTmpGuestTableId = new HashMap<String,String>();
				if (hmTableGuests != null && !hmTableGuests.isEmpty()) 
				{
					Set<Integer> setTableGuestNumber = hmTableGuests.keySet();

					// create an arraylist of guests who were assigned to this event.
					ArrayList<String> arrGuestId = new ArrayList<String>();
					for (Integer tableGuestNumber : setTableGuestNumber) {
						TableGuestsBean tableGuestsBean = hmTableGuests.get(tableGuestNumber);
						arrGuestId.add(tableGuestsBean.getGuestId());
					}
					GuestData guestData = new GuestData();
					// getting the guest details such as RSVP, Invite number and userinfo such as , first name, last name etc.
					ArrayList<EventGuestBean> arrEventGuestList = guestData.getEventGuestList(sEventId, arrGuestId);
					
					if (arrEventGuestList != null && !arrEventGuestList.isEmpty()) 
					{
						for (Integer tableGuestNumber : setTableGuestNumber) // iterating through guests assigned in a table
						{
							TableGuestsBean tableGuestsBean = hmTableGuests.get(tableGuestNumber);
							for (EventGuestBean eventGuestBean : arrEventGuestList) // iterating through guest userinfo details
							{
								if (tableGuestsBean.getGuestId().equalsIgnoreCase(eventGuestBean.getGuestId())) 
								{
									tableGuestsBean.setRsvpSeats(eventGuestBean.getRsvpSeats());
									tableGuestsBean.setTotalInvitedSeats(eventGuestBean.getTotalNumberOfSeats());
									for (Integer iNumber : setTableNumber) 
									{
										TableBean tableBean = hmTables.get(iNumber);

										if (tableBean.getTableId().equalsIgnoreCase(tableGuestsBean.getTableId())) // assign table details to the Guest data.
										{
											hmTmpGuestTableId.put(tableGuestsBean.getTableId(), tableGuestsBean.getTableId()); // creating a list of tables which are being used by guests.
											//arrTmpGuestTableId.add(tableGuestsBean.getTableId()); 
											tableGuestsBean.setNumOfSeats(tableBean.getNumOfSeats());
											tableGuestsBean.setTableId(tableBean.getTableId());
											tableGuestsBean.setTableName(tableBean.getTableName());
											tableGuestsBean.setTableNum(tableBean.getTableNum());
										}
									}

								}
							}

						}

					}
				}

				// A mismatch in total number of tables and number of tables which are occupied.
				// This indicates that there are tables which are empty.
				if (hmTmpGuestTableId != null && arrTableId != null && arrTableId.size() != hmTmpGuestTableId.size()) 
				{
					ArrayList<String> arrEmptyTables = new ArrayList<String>();
					for (String sTableId : arrTableId) 
					{
						boolean isFound = false;
						hmTmpGuestTableId.entrySet();
						for( Map.Entry<String,String> guestTableData : hmTmpGuestTableId.entrySet() )
						{
							String sTmpTableId = guestTableData.getKey();
							if (sTableId.equalsIgnoreCase(sTmpTableId)) 
							{
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							arrEmptyTables.add(sTableId);
						}
					}
					if (arrEmptyTables != null && !arrEmptyTables.isEmpty()) 
					{
						for (String sEmptyTableId : arrEmptyTables) 
						{
							for (Integer iNumber : setTableNumber) 
							{
								TableBean tableBean = hmTables.get(iNumber);
								if (tableBean.getTableId().equalsIgnoreCase(sEmptyTableId)) 
								{
									TableGuestsBean tableGuestBean = new TableGuestsBean();
									tableGuestBean.setTableId(tableBean.getTableId());
									tableGuestBean.setTableName(tableBean.getTableName());
									tableGuestBean.setTableNum(tableBean.getTableNum());
									tableGuestBean.setRsvpSeats("0");
									tableGuestBean.setTotalInvitedSeats("0");
									tableGuestBean.setNumOfSeats(tableBean.getNumOfSeats());
									tableGuestBean.setAdminId(tableBean.getAdminId());

									hmTableGuests.put(hmTableGuests.size() + 1,tableGuestBean);
								}
							}
						}
					}

				}

			}
		}
		// Str

		return hmTableGuests;
	}

	public Integer deleteGuestTable(TableGuestsBean tableGuestBean) {
		Integer numOfRows = 0;
		if (tableGuestBean != null && tableGuestBean.getTableId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getTableId())) {

			String sQuery = "DELETE FROM GTTABLEGUESTS WHERE FK_TABLEID = ? ";

			ArrayList<Object> aParams = DBDAO.createConstraint(tableGuestBean
					.getTableId());

			numOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestTableData.java", "deleteGuestTable()");
		}
		return numOfRows;
	}

	public Integer insertGuestTableAssignment(TableGuestsBean tableGuestBean) {
		appLogging.info("tableGuestBean = " + tableGuestBean);
		Integer iNumOfRows = 0;
		if (tableGuestBean != null && tableGuestBean.getTableId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getTableId())
				&& tableGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestId())
				&& tableGuestBean.getGuestAssignedSeats() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestAssignedSeats())) {
			appLogging.info("Going to insert data");

			String sQuery = "INSERT INTO GTTABLEGUESTS (TABLEGUESTID,FK_TABLEID,FK_GUESTID, IS_TMP , DEL_ROW, ASSIGNED_SEATS ) "
					+ " VALUES (?,?,?,?,?, ?) ";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					tableGuestBean.getTableGuestId(),
					tableGuestBean.getTableId(), tableGuestBean.getGuestId(),
					tableGuestBean.getIsTemporary(),
					tableGuestBean.getDelelteRow(),
					tableGuestBean.getGuestAssignedSeats());
			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestTableData.java", "insertGuestTable()");
		}

		return iNumOfRows;
	}	

	public Integer updateGuestTableAssignment(TableGuestsBean tableGuestBean) {
		Integer iNumOfRows = 0;
		if (tableGuestBean != null && tableGuestBean.getTableId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getTableId())
				&& tableGuestBean.getGuestId() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestId())
				&& tableGuestBean.getGuestAssignedSeats() != null
				&& !"".equalsIgnoreCase(tableGuestBean.getGuestAssignedSeats())) {
			String sQuery = "UPDATE GTTABLEGUESTS SET  ASSIGNED_SEATS = ? "
					+ " WHERE  FK_TABLEID = ? and FK_GUESTID = ? ";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					tableGuestBean.getGuestAssignedSeats(),
					tableGuestBean.getTableId(), tableGuestBean.getGuestId());
			iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"GuestTableData.java", "insertGuestTable()");
		}

		return iNumOfRows;
	}

	public ArrayList<TableGuestsBean> getGuestAssgnments(String sGuestId) {
		ArrayList<TableGuestsBean> arrTableGuestBean = new ArrayList<TableGuestsBean>();
		if (sGuestId != null && !"".equalsIgnoreCase(sGuestId)) {
			String sQuery = "select GTB.TABLEID,GTB.TABLENAME,GTB.TABLENUM,GTB.NUMOFSEATS,GTB.IS_TMP,GTB.DEL_ROW, "
					+ " GTB.CREATEDATE,GTB.FK_ADMINID,GTB.MODIFYDATE,GTB.MODIFIEDBY,GTB.HUMAN_CREATEDATE,GTB.HUMAN_MODIFYDATE, "
					+ " GTG.TABLEGUESTID,GTG.FK_TABLEID,GTG.FK_GUESTID,GTG.IS_TMP,GTG.DEL_ROW,GTG.ASSIGNED_SEATS FROM GTTABLE GTB, "
					+ " GTTABLEGUESTS GTG WHERE GTG.FK_GUESTID=? "
					+ " and GTG.FK_TABLEID = GTB.TABLEID";

			ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);

			ArrayList<HashMap<String, String>> arrGuestAssignment = DBDAO
					.getDBData(ADMIN_DB, sQuery, aParams, false,
							"GuestTableData.java", "getGuestAssgnments()");

			if (arrGuestAssignment != null && !arrGuestAssignment.isEmpty()) {
				for (HashMap<String, String> hmGuestAssignment : arrGuestAssignment) {
					TableGuestsBean tableGuestsBean = new TableGuestsBean();
					tableGuestsBean
							.setGuestAssignedSeats(ParseUtil
									.checkNull(hmGuestAssignment
											.get("ASSIGNED_SEATS")));

					tableGuestsBean.setGuestId(ParseUtil
							.checkNull(hmGuestAssignment.get("FK_GUESTID")));

					tableGuestsBean.setAdminId(ParseUtil
							.checkNull(hmGuestAssignment.get("FK_ADMINID")));

					tableGuestsBean.setCreateDate(ParseUtil
							.sToL(hmGuestAssignment.get("CREATEDATE")));
					tableGuestsBean.setTableId(ParseUtil
							.checkNull(hmGuestAssignment.get("TABLEID")));
					tableGuestsBean.setTableName(ParseUtil
							.checkNull(hmGuestAssignment.get("TABLENAME")));
					tableGuestsBean.setTableNum(ParseUtil
							.checkNull(hmGuestAssignment.get("TABLENUM")));
					tableGuestsBean.setNumOfSeats(ParseUtil
							.checkNull(hmGuestAssignment.get("NUMOFSEATS")));
					tableGuestsBean.setIsTemporary(ParseUtil
							.checkNull(hmGuestAssignment.get("IS_TMP")));
					tableGuestsBean.setDelelteRow(ParseUtil
							.checkNull(hmGuestAssignment.get("DEL_ROW")));

					arrTableGuestBean.add(tableGuestsBean);
				}
			}

		}
		return arrTableGuestBean;
	}
	
	public Integer deleteGuestFromAllTables(String sGuestId) {
		Integer iNumOfRecs = 0;
		if (sGuestId != null && !"".equalsIgnoreCase(sGuestId) )
		{
			String sQuery = " DELETE FROM GTTABLEGUESTS WHERE FK_GUESTID=?";
			ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId);
			
			if (aParams != null && aParams.size() > 0) {
				iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
						"GuestTableData.java", "deleteGuestEventTable()");
			}
		}
		return iNumOfRecs;
	}

	public Integer deleteGuestEventTable(ArrayList<String> arrTableId,
			String sGuestId) {
		Integer iNumOfRecs = 0;

		if (sGuestId != null && !"".equalsIgnoreCase(sGuestId)
				&& arrTableId != null && !arrTableId.isEmpty()) {
			String sQuery = " DELETE FROM GTTABLEGUESTS WHERE FK_GUESTID=? AND FK_TABLEID IN (__TABLE_LIST__)";

			ArrayList<Object> aParams = new ArrayList<Object>();
			aParams.add(sGuestId);

			String sTableParams = Utility.getMultipleParamsList(arrTableId
					.size());
			sQuery = sQuery.replace("__TABLE_LIST__", sTableParams);

			for (String sTableId : arrTableId) {

				aParams.add(sTableId);
			}

			if (aParams != null && aParams.size() > 0) {
				iNumOfRecs = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
						"GuestTableData.java", "deleteGuestEventTable()");
			}

		}
		return iNumOfRecs;
	}

    public ArrayList<TableGuestsBean> getGuestsEventTable(String sGuestId,
                                                 String sEventId) {

        ArrayList<TableGuestsBean> arrTableGuestBean = new ArrayList<TableGuestsBean>();
        if (sGuestId != null && !"".equalsIgnoreCase(sGuestId)) {
            String sQuery = "select GTB.TABLEID,GTB.TABLENAME,GTB.TABLENUM,GTB.NUMOFSEATS,GTB.IS_TMP,GTB.DEL_ROW, "
                    + " GTB.CREATEDATE,GTB.FK_ADMINID,GTB.MODIFYDATE,GTB.MODIFIEDBY,GTB.HUMAN_CREATEDATE,GTB.HUMAN_MODIFYDATE, "
                    + " GTG.TABLEGUESTID,GTG.FK_TABLEID,GTG.FK_GUESTID,GTG.IS_TMP,GTG.DEL_ROW,GTG.ASSIGNED_SEATS FROM GTTABLE GTB, "
                    + " GTTABLEGUESTS GTG,  GTEVENTTABLES ET WHERE GTG.FK_GUESTID=? "
                    + " and GTG.FK_TABLEID = GTB.TABLEID AND  ET.FK_EVENTID = ?  AND GTG.FK_TABLEID = ET.FK_TABLEID ";

            ArrayList<Object> aParams = DBDAO.createConstraint(sGuestId, sEventId );

            ArrayList<HashMap<String, String>> arrGuestTables = DBDAO
                    .getDBData(ADMIN_DB, sQuery, aParams, false,
                            "GuestTableData.java", "getEventGuestTables()");

            ArrayList<HashMap<String, String>> arrGuestAssignment = DBDAO
                    .getDBData(ADMIN_DB, sQuery, aParams, false,
                            "GuestTableData.java", "getGuestAssgnments()");

            if (arrGuestAssignment != null && !arrGuestAssignment.isEmpty()) {
                for (HashMap<String, String> hmGuestAssignment : arrGuestAssignment) {
                    TableGuestsBean tableGuestsBean = new TableGuestsBean();
                    tableGuestsBean
                            .setGuestAssignedSeats(ParseUtil
                                    .checkNull(hmGuestAssignment
                                            .get("ASSIGNED_SEATS")));

                    tableGuestsBean.setGuestId(ParseUtil
                            .checkNull(hmGuestAssignment.get("FK_GUESTID")));

                    tableGuestsBean.setAdminId(ParseUtil
                            .checkNull(hmGuestAssignment.get("FK_ADMINID")));

                    tableGuestsBean.setCreateDate(ParseUtil
                            .sToL(hmGuestAssignment.get("CREATEDATE")));
                    tableGuestsBean.setTableId(ParseUtil
                            .checkNull(hmGuestAssignment.get("TABLEID")));
                    tableGuestsBean.setTableName(ParseUtil
                            .checkNull(hmGuestAssignment.get("TABLENAME")));
                    tableGuestsBean.setTableNum(ParseUtil
                            .checkNull(hmGuestAssignment.get("TABLENUM")));
                    tableGuestsBean.setNumOfSeats(ParseUtil
                            .checkNull(hmGuestAssignment.get("NUMOFSEATS")));
                    tableGuestsBean.setIsTemporary(ParseUtil
                            .checkNull(hmGuestAssignment.get("IS_TMP")));
                    tableGuestsBean.setDelelteRow(ParseUtil
                            .checkNull(hmGuestAssignment.get("DEL_ROW")));

                    arrTableGuestBean.add(tableGuestsBean);
                }
            }

        }

        return arrTableGuestBean;

    }

	public ArrayList<String> getEventGuestTables(String sGuestId,
			String sEventId) {

		ArrayList<String> arrTableId = new ArrayList<String>();
		if (sGuestId != null && !"".equalsIgnoreCase(sGuestId)
				&& sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			String sQuery = "select * from GTEVENTTABLES ET, GTTABLEGUESTS TG WHERE ET.FK_EVENTID = ? "
					+ " AND TG.FK_TABLEID = ET.FK_TABLEID AND TG.FK_GUESTID=? ";
			ArrayList<Object> aParams = DBDAO.createConstraint(sEventId,
					sGuestId);

			ArrayList<HashMap<String, String>> arrGuestTables = DBDAO
					.getDBData(ADMIN_DB, sQuery, aParams, false,
							"GuestTableData.java", "getEventGuestTables()");

			if (arrGuestTables != null && !arrGuestTables.isEmpty()) {
				for (HashMap<String, String> hmResult : arrGuestTables) {
					arrTableId.add(ParseUtil.checkNull(hmResult
							.get("FK_TABLEID")));
				}
			}
		}
		return arrTableId;

	}

	public HashMap<Integer, TableGuestsBean> getTableGuest(
			ArrayList<String> arrTableId) {

		HashMap<Integer, TableGuestsBean> hmTableGuests = new HashMap<Integer, TableGuestsBean>();
		if (arrTableId != null && !arrTableId.isEmpty()) {
			String sQuery = "SELECT * FROM GTTABLEGUESTS GTG, GTGUESTS GG, GTUSERINFO GU WHERE "
					+ " GTG.FK_TABLEID in ( __TABLE_ID_LIST__ ) and GTG.FK_GUESTID=GG.GUESTID AND"
					+ " GG.FK_USERINFOID = GU.USERINFOID";
			String sTableParams = Utility.getMultipleParamsList(arrTableId
					.size());
			sQuery = sQuery.replace("__TABLE_ID_LIST__", sTableParams);

			ArrayList<Object> aParams = new ArrayList<Object>();
			for (String sTableId : arrTableId) {
				aParams.add(sTableId);
			}

			ArrayList<HashMap<String, String>> arrTableGuestsRes = DBDAO
					.getDBData(ADMIN_DB, sQuery, aParams, false,
							"GuestTableData.java", "getTableGuests()");
			//appLogging.info(sQuery + ": " + aParams);

			if (arrTableGuestsRes != null && !arrTableGuestsRes.isEmpty()) {
				Integer iTableGuestNum = 0;
				for (HashMap<String, String> hmTableGuest : arrTableGuestsRes) {
					TableGuestsBean tableGuestsBean = new TableGuestsBean();

					tableGuestsBean.setTableGuestId(ParseUtil
							.checkNull(hmTableGuest.get("TABLEGUESTID")));
					tableGuestsBean.setTableId(ParseUtil.checkNull(hmTableGuest
							.get("FK_TABLEID")));
					tableGuestsBean.setGuestId(ParseUtil.checkNull(hmTableGuest
							.get("FK_GUESTID")));
					tableGuestsBean.setGuestAssignedSeats(ParseUtil
							.checkNull(hmTableGuest.get("ASSIGNED_SEATS")));

					tableGuestsBean.setFirstName(ParseUtil
							.checkNull(hmTableGuest.get("FIRST_NAME")));
					tableGuestsBean.setLastName(ParseUtil
							.checkNull(hmTableGuest.get("LAST_NAME")));
					tableGuestsBean.setCellPhone(ParseUtil
							.checkNull(hmTableGuest.get("CELL_PHONE")));
					tableGuestsBean.setPhoneNum(ParseUtil
							.checkNull(hmTableGuest.get("PHONE_NUM")));

					hmTableGuests.put(iTableGuestNum, tableGuestsBean);
					iTableGuestNum++;
				}
			}
		}
		return hmTableGuests;
	}
}
