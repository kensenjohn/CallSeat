package com.gs.manager.event;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TableGuestsBean;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.data.event.GuestTableData;

public class GuestTableManager
{
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

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
}
