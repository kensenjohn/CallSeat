package com.gs.data.event;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.bean.EventTableBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TableBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;

public class TableData {
	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public ArrayList<EventTableBean>  getTableByNumber(String sTableNum, String sEventId)
    {

        ArrayList<EventTableBean>  arrEventTableBean = new ArrayList<EventTableBean>();

        if (sTableNum != null && !"".equalsIgnoreCase(sTableNum) && ParseUtil.sToI(sTableNum)>0
                && sEventId != null && !"".equalsIgnoreCase(sEventId)) {
            String sQuery = "select * from GTEVENTTABLES GTET, GTTABLE GTT where GTET.FK_TABLEID=GTT.TABLEID and GTET.FK_EVENTID=? and GTT.TABLENUM=?";

            ArrayList<Object> arrParams = DBDAO.createConstraint(sEventId,sTableNum);

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB, sQuery, arrParams, false, "TableData.java", "getTableByNumber()");

            for (HashMap<String, String> hmResult : arrResult) {
                EventTableBean eventTableGuestBean = new EventTableBean( hmResult );

                arrEventTableBean.add( eventTableGuestBean );
            }
        }
        return arrEventTableBean;
    }

	public TableBean getTableById(String sTableId) {
		TableBean tableBean = new TableBean();

		if (sTableId != null && !"".equalsIgnoreCase(sTableId)) {
			String sQuery = "select * from GTTABLE GTT where GTT.TABLEID = ? ";

			ArrayList<Object> arrParams = DBDAO.createConstraint(sTableId);

			ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(
					ADMIN_DB, sQuery, arrParams, false, "TableData.java",
					"getTableData()");

			for (HashMap<String, String> hmResult : arrResult) {
				tableBean.setTableId(hmResult.get("TABLEID"));
				tableBean.setTableName(hmResult.get("TABLENAME"));
				tableBean.setTableNum(ParseUtil.checkNull(hmResult
						.get("TABLENUM")));
				tableBean.setNumOfSeats(ParseUtil.checkNull(hmResult
						.get("NUMOFSEATS")));
				tableBean.setIsTmp(ParseUtil.checkNull(hmResult.get("IS_TMP")));
				tableBean
						.setDelRow(ParseUtil.checkNull(hmResult.get("DEL_ROW")));
				tableBean.setCreateDate(ParseUtil.sToL(hmResult
						.get("CREATEDATE")));
				tableBean.setAdminId(ParseUtil.checkNull(hmResult
						.get("FK_ADMINID")));

				tableBean.setModifyBy(ParseUtil.checkNull(hmResult
						.get("MODIFIEDBY")));
				tableBean.setModifyDate(ParseUtil.sToL(hmResult
						.get("MODIFYDATE")));

			}
		}

		return tableBean;
	}

	public Integer updateTable(TableBean tableBean) {
		int numOfRowsInserted = 0;
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId())) {
			String sQuery = "UPDATE GTTABLE SET TABLENAME = ? , TABLENUM = ?, NUMOFSEATS = ?, MODIFYDATE = ?, MODIFIEDBY = ? WHERE TABLEID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(
					tableBean.getTableName(), tableBean.getTableNum(),
					tableBean.getNumOfSeats(), tableBean.getModifyDate(),
					tableBean.getModifyBy(), tableBean.getTableId());

			appLogging.error("Table Bean aParmas : " + aParams);
			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin",
					"TableData.java", "updateTable() ");
		}
		return numOfRowsInserted;
	}

	public Integer insertTable(TableBean tableBean) {
		int numOfRowsInserted = 0;
		if (tableBean != null && !"".equalsIgnoreCase(tableBean.getTableId())) {
			String sQuery = "INSERT INTO GTTABLE (TABLEID, TABLENAME, TABLENUM, NUMOFSEATS , IS_TMP , DEL_ROW , "
					+ " CREATEDATE, FK_ADMINID, MODIFYDATE, MODIFIEDBY) VALUES ( ?,?,? ,?,?,? ,?,?,? ,? )";

			ArrayList<Object> aParams = DBDAO.createConstraint(
					tableBean.getTableId(), tableBean.getTableName(),
					tableBean.getTableNum(), tableBean.getNumOfSeats(),
					tableBean.getIsTmp(), tableBean.getDelRow(),
					tableBean.getCreateDate(), tableBean.getAdminId(),
					tableBean.getModifyDate(), tableBean.getModifyBy());
			appLogging.error("Table Bean aParmas : " + aParams);
			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin",
					"TableData.java", "insertTable() ");
		}
		return numOfRowsInserted;
	}

	public HashMap<Integer, TableBean> getEventTables(String sEventId) {
		HashMap<Integer, TableBean> hmTables = new HashMap<Integer, TableBean>();
		if (sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			String sQuery = " SELECT ET.EVENTTABLEID, ET.FK_EVENTID, ET.ASSIGN_TO_EVENT, ET.IS_TMP, T.TABLEID, "
					+ "  T.TABLENAME,  T.TABLENUM,  T.NUMOFSEATS, T.IS_TMP, T.DEL_ROW, T.CREATEDATE , "
					+ " T.MODIFYDATE, T.MODIFIEDBY "
					+ " FROM  GTEVENTTABLES ET, GTTABLE T WHERE  ET.FK_EVENTID =? AND ET.FK_TABLEID = T.TABLEID  ";

			ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

			ArrayList<HashMap<String, String>> arrEventTableRes = DBDAO
					.getDBData("admin", sQuery, aParams, true,
							"TableData.java", "getEventTables()");

			if (arrEventTableRes != null && !arrEventTableRes.isEmpty()) {
				Integer iTableNum = 0;
				for (HashMap<String, String> hmEventTable : arrEventTableRes) {
					TableBean tableBean = new TableBean();

					tableBean.setTableId(ParseUtil.checkNull(hmEventTable
							.get("TABLEID")));
					tableBean.setTableName(ParseUtil.checkNull(hmEventTable
							.get("TABLENAME")));
					tableBean.setTableNum(ParseUtil.checkNull(hmEventTable
							.get("TABLENUM")));
					tableBean.setNumOfSeats(ParseUtil.checkNull(hmEventTable
							.get("NUMOFSEATS")));
					tableBean.setIsTmp(ParseUtil.checkNull(hmEventTable
							.get("IS_TMP")));
					tableBean.setDelRow(ParseUtil.checkNull(hmEventTable
							.get("DEL_ROW")));
					tableBean.setCreateDate(ParseUtil.sToL(hmEventTable
							.get("CREATEDATE")));
					tableBean.setAdminId(ParseUtil.checkNull(hmEventTable
							.get("FK_ADMINID")));
					tableBean.setModifyDate(ParseUtil.sToL(hmEventTable
							.get("MODIFIEDDATE")));
					tableBean.setModifyBy(ParseUtil.checkNull(hmEventTable
							.get("MODIFIEDBY")));
					hmTables.put(iTableNum, tableBean);
					iTableNum++;
				}
			}

		}
		return hmTables;
	}

	public Integer deleteTables(String sTableId) {
		Integer numOfRows = 0;
		if (sTableId != null && !"".equalsIgnoreCase(sTableId)) {
			String sQuery = "DELETE FROM GTTABLE WHERE TABLEID = ?";
			ArrayList<Object> aParams = DBDAO.createConstraint(sTableId);

			numOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"TableData.java", "deleteTables()");
		}
		return numOfRows;
	}

	public Integer deleteEventTable(String sTableId, String sEventId) {
		Integer numOfRows = 0;
		if (sTableId != null && !"".equalsIgnoreCase(sTableId)
				&& sEventId != null && !"".equalsIgnoreCase(sEventId)) {
			String sQuery = " DELETE FROM  GTEVENTTABLES  WHERE FK_EVENTID =? and FK_TABLEID =  ? ";

			ArrayList<Object> aParams = DBDAO.createConstraint(sEventId,
					sTableId);

			numOfRows = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB,
					"TableData.java", "deleteEventTable()");
		}
		return numOfRows;
	}
}
