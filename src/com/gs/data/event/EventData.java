package com.gs.data.event;

import java.util.ArrayList;

import com.gs.bean.EventBean;
import com.gs.bean.EventTableBean;
import com.gs.common.db.DBDAO;

public class EventData
{
	// private ConfigBean config =
	// Configuration.getConfig(Constants.APPLICATION_PROP);

	public Integer insertEvent(EventBean eventBean)
	{
		int numOfRowsInserted = 0;
		if (eventBean.getEventId() != null && !"".equalsIgnoreCase(eventBean.getEventId()))
		{
			String sQuery = "INSERT INTO GTEVENT ( EVENTID, EVENTNAME, FK_FOLDERID , CREATEDATE , FK_ADMINID , IS_TMP , DEL_ROW ) "
					+ " VALUES ( ? , ? , ? , ? , ? , ? , ? ) ";
			ArrayList<Object> aParams = DBDAO.createConstraint(eventBean.getEventId(),
					eventBean.getEventName(), eventBean.getEventFolderId(),
					eventBean.getEventCreateDate(), eventBean.getEventAdminId(),
					eventBean.getIsTmp(), eventBean.getDelRow());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin", "EventData.java",
					"insertEvent() ");
		}

		return numOfRowsInserted;
	}

	public Integer insertEventTable(EventTableBean eventTableBean)
	{
		int numOfRowsInserted = 0;
		if (eventTableBean != null && eventTableBean.getEventTableId() != null)
		{
			String sQuery = "INSERT INTO GTEVENTTABLES ( EVENTTABLEID , FK_EVENTID , FK_TABLEID , "
					+ " ASSIGN_TO_EVENT , IS_TMP , DEL_ROW ) VALUES ( ? , ? , ? , ? , ? , ? )";
			ArrayList<Object> aParams = DBDAO.createConstraint(eventTableBean.getEventTableId(),
					eventTableBean.getEventId(), eventTableBean.getTableId(),
					eventTableBean.getAssignToEvent(), eventTableBean.getIsTmp(),
					eventTableBean.getDelRow());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, "admin", "EventData.java",
					"insertEventTable() ");
		}
		return numOfRowsInserted;
	}
}
