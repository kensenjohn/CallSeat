package com.gs.manager.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventBean;
import com.gs.bean.EventTableBean;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.Utility;
import com.gs.data.event.EventData;

public class EventManager
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public EventBean createEvent(EventBean eventBean)
	{
		if (eventBean == null || eventBean.getEventAdminId() == null
				|| "".equalsIgnoreCase(eventBean.getEventAdminId()))
		{
			appLogging.error("There Admin to create an event for." + eventBean);
			eventBean = null;
		} else if (eventBean != null && eventBean.getEventAdminId() != null
				&& !"".equalsIgnoreCase(eventBean.getEventAdminId()))
		{
			EventData eventData = new EventData();
			int iNumOfRecords = eventData.insertEvent(eventBean);

			if (iNumOfRecords > 0)
			{
				appLogging.info("Create Event : " + eventBean.getEventId());
			} else
			{
				appLogging.error("Error creating Event : " + eventBean);
				eventBean = null; // so that this will not be used again.
			}

		}
		return eventBean;
	}

	public EventBean createEvent(String sAdminId)
	{
		EventBean eventBean = createTemporaryEvent(sAdminId);

		eventBean = createEvent(eventBean);

		appLogging.info(" Create Event Bean : " + eventBean);
		return eventBean;
	}

	private EventBean createTemporaryEvent(String sAdminId)
	{
		EventBean eventBean = new EventBean();

		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId))
		{
			eventBean.setEventId(Utility.getNewGuid());
			eventBean.setEventFolderId(Constants.ROOT_FOLDER);
			eventBean.setEventAdminId(sAdminId);
			eventBean.setEventName(Constants.DEFAULT_EVENT_NAME);
			eventBean.setEventCreateDate(DateSupport.getEpochMillis());
			eventBean.setIsTmp("1");
			eventBean.setDelRow("0");
		}

		return eventBean;

	}

	public EventTableBean assignTableToEvent(String sEventId, String sTableId)
	{
		EventTableBean eventTableBean = new EventTableBean();
		if (sEventId != null && sTableId != null)
		{
			eventTableBean.setEventTableId(Utility.getNewGuid());
			eventTableBean.setTableId(sTableId);
			eventTableBean.setEventId(sEventId);
			eventTableBean.setIsTmp("1");
			eventTableBean.setDelRow("0");
			eventTableBean.setAssignToEvent("1");

			eventTableBean = assignTableToEvent(eventTableBean);

		}

		return eventTableBean;
	}

	public EventTableBean assignTableToEvent(EventTableBean eventTableBean)
	{
		EventData eventData = new EventData();
		int iNumOfRecords = eventData.insertEventTable(eventTableBean);

		if (iNumOfRecords > 0)
		{
			appLogging.info("Create Event : " + eventTableBean.getEventTableId());
		} else
		{
			appLogging.error("Error creating Event Table  : " + eventTableBean);
			eventTableBean = null; // so that this will not be used again.
		}
		return eventTableBean;
	}
}
