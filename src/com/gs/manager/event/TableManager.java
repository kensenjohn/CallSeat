package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventTableBean;
import com.gs.bean.TableBean;
import com.gs.common.Utility;
import com.gs.data.event.GuestTableData;
import com.gs.data.event.TableData;

public class TableManager {
	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");

	public TableBean createNewTable(String sAdminId) {
		TableBean tableBean = new TableBean();
		tableBean.setTableId(Utility.getNewGuid());
		// tableBean.set
		return null;
	}

	public TableBean createNewTable(String sAdminId, String sEventId) {
		return null;
	}

	public Integer updateTable(TableBean tableBean) {

		Integer iNumOfRows = 0;
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId())) {
			TableData tableData = new TableData();
			iNumOfRows = tableData.updateTable(tableBean);
		}
		return iNumOfRows;
	}

	public TableBean createNewTable(TableBean tableBean) {
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId())) {
			TableData tableData = new TableData();
			Integer iNumOfRecord = tableData.insertTable(tableBean);

			if (iNumOfRecord > 0) {
				appLogging.info("Create Table : " + tableBean.getTableId());
			} else {
				appLogging.error("Error creating Table : " + tableBean);
				tableBean = null; // so that this will not be used again.
			}
		}

		return tableBean;

	}

    public  ArrayList<EventTableBean> getTableByNum(String sTableNum, String sEventId) {
        TableData tableData = new TableData();

        ArrayList<EventTableBean> arrEventTableBean = tableData.getTableByNumber( sTableNum, sEventId );

        return arrEventTableBean;
    }

	public TableBean getTable(String sTableId) {
		TableData tableData = new TableData();

		TableBean tableBean = tableData.getTableById(sTableId);

		return tableBean;
	}

	public HashMap<Integer, TableBean> retrieveEventTables(String sEventId) {
		TableData tableData = new TableData();
		HashMap<Integer, TableBean> hmTables = tableData
				.getEventTables(sEventId);

		return hmTables;
	}

	public Integer deleteTable(TableBean tableBean) {
		Integer numOfTablesDel = 0;
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId())) {
			TableData tableData = new TableData();

			numOfTablesDel = tableData.deleteTables(tableBean.getTableId());
		}
		return numOfTablesDel;
	}

	public Integer deleteEventTable(EventTableBean eventtableBean) {
		Integer numOfEventTablesDel = 0;
		if (eventtableBean != null
				&& !"".equalsIgnoreCase(eventtableBean.getTableId())
				&& !"".equalsIgnoreCase(eventtableBean.getEventId())) {
			TableData tableData = new TableData();

			numOfEventTablesDel = tableData.deleteEventTable(
					eventtableBean.getTableId(), eventtableBean.getEventId());

		}
		return numOfEventTablesDel;
	}
	
	public Integer deleteGuestFromTables(String sGuestId) {
		
		Integer numOfGuestTablesDel = 0;
		if(sGuestId != null
				&& !"".equalsIgnoreCase(sGuestId))
		{
			GuestTableData guestTableData = new GuestTableData();
			numOfGuestTablesDel =  guestTableData.deleteGuestFromAllTables(sGuestId);
		}
		return numOfGuestTablesDel;
	}

	public Integer deleteGuestEventTable(ArrayList<String> arrTableId,
			String sGuestId) {
		Integer numOfEventTablesDel = 0;

		if (arrTableId != null && !arrTableId.isEmpty() && sGuestId != null
				&& !"".equalsIgnoreCase(sGuestId)) {
			GuestTableData guestTableData = new GuestTableData();
			numOfEventTablesDel = guestTableData.deleteGuestEventTable(
					arrTableId, sGuestId);
		}

		return numOfEventTablesDel;
	}

	public Integer deleteGuestEventTable(HashMap<Integer, TableBean> hmTables,
			EventGuestMetaData eventGuestMeta) {

		Integer numOfEventTablesDel = 0;
		if (hmTables != null && !hmTables.isEmpty()
				&& eventGuestMeta.getGuestId() != null
				&& !"".equalsIgnoreCase(eventGuestMeta.getGuestId())) {
			ArrayList<String> arrTables = new ArrayList<String>();

			Set<Integer> setTableKey = hmTables.keySet();
			for (Integer iTable : setTableKey) {
				TableBean tableBean = hmTables.get(iTable);

				arrTables.add(tableBean.getTableId());

			}
			GuestTableData guestTableData = new GuestTableData();
			numOfEventTablesDel = deleteGuestEventTable(arrTables,
					eventGuestMeta.getGuestId());
		}
		return numOfEventTablesDel;
	}
}
