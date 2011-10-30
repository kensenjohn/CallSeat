package com.gs.manager.event;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TableBean;
import com.gs.common.Utility;
import com.gs.data.event.TableData;

public class TableManager
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public TableBean createNewTable(String sAdminId)
	{
		TableBean tableBean = new TableBean();
		tableBean.setTableId(Utility.getNewGuid());
		// tableBean.set
		return null;
	}

	public TableBean createNewTable(String sAdminId, String sEventId)
	{
		return null;
	}

	public TableBean createNewTable(TableBean tableBean)
	{
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId()))
		{
			TableData tableData = new TableData();
			Integer iNumOfRecord = tableData.insertTable(tableBean);

			if (iNumOfRecord > 0)
			{
				appLogging.info("Create Table : " + tableBean.getTableId());
			} else
			{
				appLogging.error("Error creating Table : " + tableBean);
				tableBean = null; // so that this will not be used again.
			}
		}

		return tableBean;

	}

	public HashMap<Integer, TableBean> retrieveEventTables(String sEventId)
	{
		TableData tableData = new TableData();
		HashMap<Integer, TableBean> hmTables = tableData.getEventTables(sEventId);

		return hmTables;
	}

	public void assignTableToEvent()
	{

	}

}
