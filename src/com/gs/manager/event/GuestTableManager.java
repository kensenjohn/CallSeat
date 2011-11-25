package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AssignedGuestBean;
import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.TableBean;
import com.gs.bean.TableGuestsBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.data.GuestData;
import com.gs.data.event.GuestTableData;
import com.gs.data.event.TableData;

public class GuestTableManager
{
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public HashMap<Integer, AssignedGuestBean> getUnAssignedGuest(
			GuestTableMetaData guestTableMetaData)
	{
		HashMap<Integer, AssignedGuestBean> hmTableGuests = new HashMap<Integer, AssignedGuestBean>();
		if (guestTableMetaData != null && guestTableMetaData.getEventId() != null
				&& !"".equalsIgnoreCase(guestTableMetaData.getEventId()))
		{
			EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
			eventGuestMetaData.setEventId(guestTableMetaData.getEventId());

			EventGuestManager eventGuestManager = new EventGuestManager();
			ArrayList<EventGuestBean> arrEventGuestBean = eventGuestManager
					.getGuestsByEvent(eventGuestMetaData);

			appLogging.info("Arr Event Guests: " + arrEventGuestBean);

			HashMap<Integer, TableGuestsBean> hmAllAssignedGuests = getTablesAndGuest(guestTableMetaData
					.getEventId());

			appLogging.info("Arr Event Guests: " + hmAllAssignedGuests);

			Integer iNumOfGuests = 0;
			if (arrEventGuestBean != null && !arrEventGuestBean.isEmpty())
			{
				for (EventGuestBean eventGuestBean : arrEventGuestBean)
				{
					String sGuestId = eventGuestBean.getGuestId();
					Integer iRsvp = ParseUtil.sToI(eventGuestBean.getRsvpSeats());

					int iAssignedSeats = 0;
					if (hmAllAssignedGuests != null && !hmAllAssignedGuests.isEmpty())
					{
						Set<Integer> setTableGuestNum = hmAllAssignedGuests.keySet();

						for (Integer iTableGuestBean : setTableGuestNum)
						{
							TableGuestsBean tableGuestBean = hmAllAssignedGuests
									.get(iTableGuestBean);

							String assignedGuestId = tableGuestBean.getGuestId();
							if (assignedGuestId != null
									&& assignedGuestId.equalsIgnoreCase(sGuestId))
							{
								iAssignedSeats = iAssignedSeats
										+ ParseUtil.sToI(tableGuestBean.getGuestAssignedSeats());
							}

						}
					}

					int numRemaininAssignment = iRsvp - iAssignedSeats;

					if (numRemaininAssignment > 0)
					{
						// This indicates that this guest has at least 1 seat to
						// be assigned.

						AssignedGuestBean unassignedBean = new AssignedGuestBean();
						unassignedBean.setGuestId(sGuestId);

						GuestData guestData = new GuestData();
						GuestBean guestBean = guestData.getGuest(sGuestId);

						if (guestBean != null && guestBean.getUserInfoBean() != null)
						{
							UserInfoBean userInfoBean = guestBean.getUserInfoBean();

							unassignedBean.setCellNumber(userInfoBean.getCellPhone());
							unassignedBean.setHomeNumber(userInfoBean.getPhoneNum());
							unassignedBean.setFirstName(userInfoBean.getFirstName());
							unassignedBean.setLastName(userInfoBean.getLastName());
						}
						unassignedBean.setRsvpSeats(eventGuestBean.getRsvpSeats());
						unassignedBean.setUnAssignedSeats(ParseUtil.iToS(numRemaininAssignment));

						hmTableGuests.put(iNumOfGuests, unassignedBean);
						iNumOfGuests++;
					}

				}
			}
		}

		return hmTableGuests;
	}

	public HashMap<Integer, AssignedGuestBean> getAssignedGuest(String sEventId, String sTableId)
	{
		GuestTableData guestTableData = new GuestTableData();
		HashMap<Integer, AssignedGuestBean> hmTableGuests = guestTableData.getTableGuest(sEventId,
				sTableId);

		return hmTableGuests;
	}

	public void getGuestWithNoTable(String sEventId)
	{

	}

	public HashMap<Integer, TableGuestsBean> getTablesAndGuest(String sEventId)
	{
		GuestTableData guestTableData = new GuestTableData();

		HashMap<Integer, TableGuestsBean> hmTableGuests = guestTableData
				.getAllTablesGuest(sEventId);

		return hmTableGuests;
	}

	public HashMap<String, TableGuestsBean> consolidateTableAndGuest(
			HashMap<Integer, TableGuestsBean> hmTableGuests)
	{
		HashMap<String, TableGuestsBean> hmConsTableGuestBean = new HashMap<String, TableGuestsBean>();
		if (hmTableGuests != null && !hmTableGuests.isEmpty())
		{
			Set<Integer> setIndexResRows = hmTableGuests.keySet();
			if (setIndexResRows != null && !setIndexResRows.isEmpty())
			{

				for (Integer indexRows : setIndexResRows)
				{
					boolean isNewTable = false;
					TableGuestsBean tableGuestBean = hmTableGuests.get(indexRows);

					TableGuestsBean tmpTableGuestBean = hmConsTableGuestBean.get(tableGuestBean
							.getTableId());

					if (tmpTableGuestBean == null)
					{
						tmpTableGuestBean = copyOfTableGuestsBean(tableGuestBean);
						isNewTable = true;
						hmConsTableGuestBean.put(tableGuestBean.getTableId(), tmpTableGuestBean);
					}

					if (!isNewTable)
					{
						Integer totalSeatsAssigned = ParseUtil.sToI(tmpTableGuestBean
								.getGuestAssignedSeats());

						Integer indivSeatAssigned = ParseUtil.sToI(tableGuestBean
								.getGuestAssignedSeats());

						tmpTableGuestBean.setGuestAssignedSeats(ParseUtil.iToS(totalSeatsAssigned
								+ indivSeatAssigned));

					}

				}
			}
		}
		return hmConsTableGuestBean;
	}

	private TableGuestsBean copyOfTableGuestsBean(TableGuestsBean srcTableGuestBean)
	{
		TableGuestsBean tmpTableGuestBean = new TableGuestsBean();
		tmpTableGuestBean.setTableId(srcTableGuestBean.getTableId());
		tmpTableGuestBean.setTableName(srcTableGuestBean.getTableName());
		tmpTableGuestBean.setTableNum(srcTableGuestBean.getTableNum());
		tmpTableGuestBean.setNumOfSeats(srcTableGuestBean.getNumOfSeats());
		tmpTableGuestBean.setGuestAssignedSeats(srcTableGuestBean.getGuestAssignedSeats());
		/*
		 * private String tableId = ""; private String tableName = ""; private
		 * String tableNum = ""; private String numOfSeats = ""; private String
		 * isTemporary = ""; private String delelteRow = ""; private String
		 * adminId = ""; private Long createDate = 0L; private Long modifyDate =
		 * 0L; private String modifiedBy = ""; private String tableGuestId = "";
		 * private String guestId = ""; private String guestTableIsTmp = "";
		 * private String guestTableDelRow = ""; private String
		 * guestAssignedSeats = "";
		 */
		return tmpTableGuestBean;
	}

	public JSONObject getAssignedGuestBeanJson(HashMap<Integer, AssignedGuestBean> hmTableGuests)
	{
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonTableArray = new JSONArray();
		try
		{

			if (hmTableGuests != null && !hmTableGuests.isEmpty())
			{
				Set<Integer> setTableGuest = hmTableGuests.keySet();

				int iIndex = 0;
				for (Integer keyTableGuest : setTableGuest)
				{
					AssignedGuestBean assignGuestBean = hmTableGuests.get(keyTableGuest);

					jsonTableArray.put(iIndex, assignGuestBean.toJson());
					iIndex++;
				}

				jsonObject.put("num_of_rows", ParseUtil.iToI(hmTableGuests.size()));
				jsonObject.put("guests", jsonTableArray);

			}
		} catch (JSONException e)
		{
			appLogging.error(ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}

	public JSONObject getTablesAndGuestJson(HashMap<String, TableGuestsBean> hmTableGuests)
	{
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonTableArray = new JSONArray();
		try
		{

			if (hmTableGuests != null && !hmTableGuests.isEmpty())
			{
				Set<String> setTableGuest = hmTableGuests.keySet();

				int iIndex = 0;
				for (String keyTableGuest : setTableGuest)
				{
					TableGuestsBean tableGuestBean = hmTableGuests.get(keyTableGuest);

					jsonTableArray.put(iIndex, tableGuestBean.toJson());
					iIndex++;
				}

				jsonObject.put("num_of_rows", ParseUtil.iToI(hmTableGuests.size()));
				jsonObject.put("tables", jsonTableArray);

			}
		} catch (JSONException e)
		{
			appLogging.error(ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}

	public JSONObject getTablesAndGuestJson(String sEventId)
	{
		HashMap<Integer, TableGuestsBean> hmTableGuests = getTablesAndGuest(sEventId);
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonTableArray = new JSONArray();
		try
		{

			if (hmTableGuests != null && !hmTableGuests.isEmpty())
			{
				Set<Integer> setTableGuest = hmTableGuests.keySet();

				for (Integer keyTableGuest : setTableGuest)
				{
					TableGuestsBean tableGuestBean = hmTableGuests.get(keyTableGuest);

					jsonTableArray.put(keyTableGuest, tableGuestBean.toJson());

				}

				jsonObject.put("num_of_rows", ParseUtil.iToI(hmTableGuests.size()));
				jsonObject.put("tables", jsonTableArray);

			}
		} catch (JSONException e)
		{
			appLogging.error(ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;

	}

	public Integer deleteGuestTables(TableGuestsBean tableGuestBean)
	{
		Integer numOfGuestTablesDel = 0;
		if (tableGuestBean != null && !"".equalsIgnoreCase(tableGuestBean.getTableId()))
		{
			GuestTableData guestTableData = new GuestTableData();

			numOfGuestTablesDel = guestTableData.deleteGuestTable(tableGuestBean);

		}
		return numOfGuestTablesDel;
	}

	public GuestTableResponse assignSeatsForGuest(GuestTableMetaData guestTableMetaData)
	{
		GuestTableResponse guestTableResponse = new GuestTableResponse();
		String sEventId = guestTableMetaData.getEventId();
		String sAdminId = guestTableMetaData.getAdminId();
		String sTableId = guestTableMetaData.getTableId();
		String sGuestId = guestTableMetaData.getGuestId();
		Integer iNumOfSeats = guestTableMetaData.getNumOfSeats();

		HashMap<Integer, AssignedGuestBean> hmAssignedGuests = getAssignedGuest(sEventId, sTableId);

		// AssignedGuestBean existingGuestAssigned = new AssignedGuestBean();

		boolean isGuestAlreadySeatedAtTable = false;
		int sameGuestAlreadyOccupySeats = 0;
		int countAssigned = 0;
		if (hmAssignedGuests != null && !hmAssignedGuests.isEmpty())
		{
			Set<Integer> setNumOfGuest = hmAssignedGuests.keySet();
			for (Integer iNumOfGuest : setNumOfGuest)
			{
				AssignedGuestBean assignedGuests = hmAssignedGuests.get(iNumOfGuest);

				countAssigned = countAssigned + ParseUtil.sToI(assignedGuests.getAssignedSeats());

				if (sGuestId.equalsIgnoreCase(assignedGuests.getGuestId()))
				{
					isGuestAlreadySeatedAtTable = true;
					sameGuestAlreadyOccupySeats = sameGuestAlreadyOccupySeats
							+ ParseUtil.sToI(assignedGuests.getAssignedSeats());
				}

			}
		}

		iNumOfSeats = iNumOfSeats + sameGuestAlreadyOccupySeats;

		TableData tableData = new TableData();
		TableBean tableBean = tableData.getTableById(sTableId);
		int totalSeatsAtTable = ParseUtil.sToI(tableBean.getNumOfSeats());

		appLogging.info("iNumOfSeats " + iNumOfSeats + " totalSeatsAtTable = " + totalSeatsAtTable
				+ " countAssigned = " + countAssigned);

		if (iNumOfSeats <= (totalSeatsAtTable - countAssigned))
		{
			TableGuestsBean tableGuestBean = assignGuestToTable(sGuestId, sTableId, iNumOfSeats,
					isGuestAlreadySeatedAtTable);

			if (tableGuestBean != null && tableGuestBean.getTableGuestId() != null
					&& !"".equalsIgnoreCase(tableGuestBean.getTableGuestId()))
			{
				guestTableResponse.setTableGuestsBean(tableGuestBean);
				guestTableResponse.setSuccess(true);
				guestTableResponse.setMessage("Guest was successfully assigned to table.");
			} else
			{
				guestTableResponse.setSuccess(false);
				guestTableResponse
						.setMessage("Guest was not assigned a seat. Please try again later.");
			}
		} else
		{
			guestTableResponse.setSuccess(false);
			guestTableResponse
					.setMessage("Guest was not assigned a seat. Number of guests exceeds number of seats at table.");
		}

		return guestTableResponse;
	}

	private TableGuestsBean assignGuestToTable(String sGuestId, String sTableId,
			Integer iNumOfSeats, boolean isGuestAlreadyPresented)
	{
		TableGuestsBean tableGuestBean = new TableGuestsBean();

		tableGuestBean.setTableGuestId(Utility.getNewGuid());
		tableGuestBean.setTableId(sTableId);
		tableGuestBean.setGuestId(sGuestId);
		tableGuestBean.setIsTemporary("1");
		tableGuestBean.setDelelteRow("0");
		tableGuestBean.setGuestAssignedSeats(ParseUtil.iToS(iNumOfSeats));

		GuestTableData guestTableData = new GuestTableData();
		Integer iNumOfRows = 0;
		appLogging.info("Guest already assigned to table ? " + isGuestAlreadyPresented);
		if (isGuestAlreadyPresented)
		{
			iNumOfRows = guestTableData.updateGuestTableAssignment(tableGuestBean);
		} else
		{
			iNumOfRows = guestTableData.insertGuestTableAssignment(tableGuestBean);
		}

		if (iNumOfRows <= 0)
		{
			appLogging.error("Error assigning seats to guests");
			tableGuestBean.setTableGuestId("");
		}

		return tableGuestBean;

	}

	public ArrayList<TableGuestsBean> getGuestsAssignments(GuestTableMetaData guestTableMetaData)
	{
		ArrayList<TableGuestsBean> arrTableGuestBean = new ArrayList<TableGuestsBean>();
		if (guestTableMetaData != null && guestTableMetaData.getGuestId() != null
				&& !"".equalsIgnoreCase(guestTableMetaData.getGuestId()))
		{
			GuestTableData guestTableData = new GuestTableData();
			arrTableGuestBean = guestTableData.getGuestAssgnments(guestTableMetaData.getGuestId());
		}
		return arrTableGuestBean;
	}
}
