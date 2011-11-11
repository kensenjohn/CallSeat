package com.gs.manager.event;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AssignedGuestBean;
import com.gs.bean.TableBean;
import com.gs.bean.TableGuestsBean;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.data.event.GuestTableData;
import com.gs.data.event.TableData;

public class GuestTableManager
{
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public HashMap<Integer, AssignedGuestBean> getUnAssignedGuest(String sEventId)
	{
		GuestTableData guestTableData = new GuestTableData();
		HashMap<Integer, AssignedGuestBean> hmTableGuests = guestTableData
				.getUnAssignedGuest(sEventId);

		return hmTableGuests;
	}

	public HashMap<Integer, AssignedGuestBean> getAssignedGuest(String sEventId, String sTableId)
	{
		GuestTableData guestTableData = new GuestTableData();
		HashMap<Integer, AssignedGuestBean> hmTableGuests = guestTableData.getTableGuest(sEventId,
				sTableId);

		return hmTableGuests;
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

				boolean isNewTable = false;
				for (Integer indexRows : setIndexResRows)
				{
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

	public void assignSeatsForGuest(GuestTableMetaData guestTableMetaData)
	{
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

		if (iNumOfSeats < (totalSeatsAtTable - countAssigned))
		{
			TableGuestsBean tableGuestBean = assignGuestToTable(sGuestId, sTableId, iNumOfSeats,
					isGuestAlreadySeatedAtTable);
		}

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
}
