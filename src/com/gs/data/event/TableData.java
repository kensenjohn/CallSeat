package com.gs.data.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TableBean;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;

public class TableData
{
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public TableBean getTableById(String sTableId)
	{
		TableBean tableBean = new TableBean();

		if (sTableId != null && !"".equalsIgnoreCase(sTableId))
		{
			String sQuery = "select * from GTTABLE GTT where GTT.TABLEID = ? ";

			ArrayList<Object> arrParams = DBDAO.createConstraint(sTableId);

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData("admin", sQuery,
					arrParams, false, "TableData.java", "getTableData()");

			for (HashMap<String, String> hmResult : arrResult)
			{
				tableBean.setTableId(hmResult.get("TABLEID"));
				tableBean.setTableName(hmResult.get("TABLENAME"));

			}
		}

		return tableBean;
	}

	public Integer insertTable(TableBean tableBean)
	{
		int numOfRowsInserted = 0;
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId()))
		{
			String sQuery = "INSERT INTO GTTABLE (TABLEID, TABLENAME, TABLENUM, NUMOFSEATS , IS_TMP , DEL_ROW , "
					+ " CREATEDATE, FK_ADMINID, MODIFYDATE, MODIFIEDBY) VALUES ( ?,?,? ,?,?,? ,?,?,? ,? )";

			ArrayList<Object> aParams = DBDAO.createConstraint(tableBean.getTableId(),
					tableBean.getTableName(), tableBean.getTableNum(), tableBean.getNumOfSeats(),
					tableBean.getIsTmp(), tableBean.getDelRow(), tableBean.getCreateDate(),
					tableBean.getAdminId(), tableBean.getModifyDate(), tableBean.getModifyBy());
			appLogging.error("Table Bean aParmas : " + aParams);
			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin", "TableData.java",
					"insertTable() ");
		}
		return numOfRowsInserted;
	}

	public HashMap<Integer, TableBean> getEventTables(String sEventId)
	{
		HashMap<Integer, TableBean> hmTables = new HashMap<Integer, TableBean>();
		if (sEventId != null && !"".equalsIgnoreCase(sEventId))
		{
			String sQuery = " SELECT ET.EVENTTABLEID, ET.FK_EVENTID, ET.ASSIGN_TO_EVENT, ET.IS_TMP, T.TABLEID, "
					+ "  T.TABLENAME,  T.TABLENUM,  T.NUMOFSEATS, T.IS_TMP, T.DEL_ROW, T.CREATEDATE, T.OWNER , "
					+ " T.MODIFIEDDATE, T.MODIFIEDBY "
					+ " FROM  GTEVENTTABLES ET, GTTABLE T WHERE  ET.FK_EVENTID =? AND ET.FK_TABLEID = T.TABLEID  ";

			ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

			ArrayList<HashMap<String, String>> arrEventTableRes = DBDAO.getDBData("admin", sQuery,
					aParams, true, "TableData.java", "getEventTables()");

			if (arrEventTableRes != null && !arrEventTableRes.isEmpty())
			{
				Integer iTableNum = 0;
				for (HashMap<String, String> hmEventTable : arrEventTableRes)
				{
					TableBean tableBean = new TableBean();

					tableBean.setTableId(ParseUtil.checkNull(hmEventTable.get("TABLEID")));
					tableBean.setTableName(ParseUtil.checkNull(hmEventTable.get("TABLENAME")));
					tableBean.setTableNum(ParseUtil.checkNull(hmEventTable.get("TABLENUM")));
					tableBean.setNumOfSeats(ParseUtil.checkNull(hmEventTable.get("NUMOFSEATS")));
					tableBean.setIsTmp(ParseUtil.checkNull(hmEventTable.get("IS_TMP")));
					tableBean.setDelRow(ParseUtil.checkNull(hmEventTable.get("DEL_ROW")));
					tableBean.setCreateDate(ParseUtil.sToL(hmEventTable.get("CREATEDATE")));
					tableBean.setAdminId(ParseUtil.checkNull(hmEventTable.get("FK_ADMINID")));
					tableBean.setModifyDate(ParseUtil.sToL(hmEventTable.get("MODIFIEDDATE")));
					tableBean.setModifyBy(ParseUtil.checkNull(hmEventTable.get("MODIFIEDBY")));
				}
			}

		}
		return hmTables;
	}
}
