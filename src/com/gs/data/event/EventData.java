package com.gs.data.event;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.bean.EventBean;
import com.gs.bean.EventTableBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;

public class EventData
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

	public Integer insertEvent(EventBean eventBean)
	{
		int numOfRowsInserted = 0;
		if (eventBean.getEventId() != null && !"".equalsIgnoreCase(eventBean.getEventId()))
		{
			String sQuery = "INSERT INTO GTEVENT ( EVENTID, EVENTNAME, FK_FOLDERID , CREATEDATE ,"
					+ " FK_ADMINID , IS_TMP , DEL_ROW, EVENTDATE, HUMANCREATEDATE, HUMANEVENTDATE ) "
					+ " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ?) ";
			ArrayList<Object> aParams = DBDAO.createConstraint(eventBean.getEventId(),
					eventBean.getEventName(), eventBean.getEventFolderId(),
					eventBean.getEventCreateDate(), eventBean.getEventAdminId(),
					eventBean.getIsTmp(), eventBean.getDelRow(), eventBean.getEventDate(),
					eventBean.getHumanCreateDate(), eventBean.getHumanEventDate());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "EventData.java",
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
					+ " ASSIGN_TO_EVENT , IS_TMP , DEL_ROW ) VALUES ( ? , ? , ? , ? , ? , ?  )";
			ArrayList<Object> aParams = DBDAO.createConstraint(eventTableBean.getEventTableId(),
					eventTableBean.getEventId(), eventTableBean.getTableId(),
					eventTableBean.getAssignToEvent(), eventTableBean.getIsTmp(),
					eventTableBean.getDelRow());

			numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, ADMIN_DB, "EventData.java",
					"insertEventTable() ");
		}
		return numOfRowsInserted;
	}

	public ArrayList<EventBean> getAllEventsByAdmin(String sAmindId)
	{
		ArrayList<EventBean> arrEventBean = new ArrayList<EventBean>();
		if (sAmindId != null && !"".equalsIgnoreCase(sAmindId))
		{
			String sQuery = "SELECT  EVENTID, EVENTNUM, EVENTNAME, FK_FOLDERID , CREATEDATE , FK_ADMINID , "
					+ " IS_TMP , DEL_ROW , EVENTDATE FROM GTEVENT WHERE FK_ADMINID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(sAmindId);

			ArrayList<HashMap<String, String>> arrAllEvents = DBDAO.getDBData(ADMIN_DB, sQuery,
					aParams, true, "EventData.java", "getAllEventsByAdmin()");

			if (arrAllEvents != null && !arrAllEvents.isEmpty())
			{
				for (HashMap<String, String> hmEvent : arrAllEvents)
				{
					EventBean eventBean = new EventBean(hmEvent);
					arrEventBean.add(eventBean);
				}
			}
		}

		return arrEventBean;
	}

	public EventBean getEvent(String sEventId)
	{
		EventBean eventBean = new EventBean();
		if (sEventId != null && !"".equalsIgnoreCase(sEventId))
		{
			String sQuery = "SELECT  EVENTID, EVENTNUM, EVENTNAME, FK_FOLDERID , CREATEDATE , FK_ADMINID , "
					+ " IS_TMP , DEL_ROW , EVENTDATE FROM GTEVENT WHERE EVENTID = ?";

			ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

			ArrayList<HashMap<String, String>> arrEvent = DBDAO.getDBData(ADMIN_DB, sQuery,
					aParams, true, "EventData.java", "getAllEventsByAdmin()");

			if (arrEvent != null && !arrEvent.isEmpty())
			{
				for (HashMap<String, String> hmEvent : arrEvent)
				{
					eventBean = new EventBean(hmEvent);
					// arrEventBean.add(eventBean);
				}
			}
		}
		return eventBean;
	}
}
