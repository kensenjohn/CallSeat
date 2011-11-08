package com.gs.manager.event;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.AdminBean;
import com.gs.bean.EventBean;
import com.gs.bean.EventCreationMetaDataBean;
import com.gs.bean.EventTableBean;
import com.gs.bean.UserInfoBean;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.data.event.EventData;
import com.gs.manager.AdminManager;

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

	public EventBean createEvent(EventCreationMetaDataBean eventMeta)
	{
		EventBean eventBean = createTemporaryEvent(eventMeta);

		eventBean = createEvent(eventBean);

		appLogging.info(" Create Event Bean : " + eventBean);
		return eventBean;
	}

	private EventBean createTemporaryEvent(EventCreationMetaDataBean eventMeta)
	{
		EventBean eventBean = new EventBean();

		if (eventMeta != null && eventMeta.getAdminBean() != null
				&& !"".equalsIgnoreCase(eventMeta.getAdminBean().getAdminId()))
		{
			eventBean.setEventId(Utility.getNewGuid());
			eventBean.setEventFolderId(Constants.ROOT_FOLDER);
			eventBean.setEventAdminId(eventMeta.getAdminBean().getAdminId());
			eventBean.setEventName(Constants.DEFAULT_EVENT_NAME);
			eventBean.setEventCreateDate(DateSupport.getEpochMillis());
			eventBean.setHumanCreateDate(DateSupport.getUTCDateTime());
			eventBean.setIsTmp("1");
			eventBean.setDelRow("0");

			eventBean.setEventDate(DateSupport.getMillis(eventMeta.getEventDate(),
					eventMeta.getEventDatePattern(), eventMeta.getEventTimeZone()));
			eventBean.setHumanEventDate(eventMeta.getEventDate());
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

	public EventBean getEvent(String sEventId)
	{
		EventBean eventBean = new EventBean();

		if (sEventId != null && !"".equalsIgnoreCase(sEventId))
		{
			EventData eventData = new EventData();
			eventBean = eventData.getEvent(sEventId);
		}
		return eventBean;
	}

	public ArrayList<EventBean> getAllEvents(String sAdminId)
	{
		ArrayList<EventBean> arrEventBean = new ArrayList<EventBean>();
		if (sAdminId != null && !"".equalsIgnoreCase(sAdminId))
		{
			AdminManager adminManager = new AdminManager();

			AdminBean adminBean = adminManager.getAdmin(sAdminId);

			if (adminBean != null && adminBean.isAdminExists())
			{
				UserInfoBean adminUserInfoBean = adminBean.getAdminUserInfoBean();

				if (adminUserInfoBean.isUserInfoExists())
				{
					EventData eventData = new EventData();
					ArrayList<EventBean> tmpArrEventBean = eventData.getAllEventsByAdmin(sAdminId);

					if (tmpArrEventBean != null && !tmpArrEventBean.isEmpty())
					{
						for (EventBean eventBean : tmpArrEventBean)
						{
							eventBean.setHumanEventDate(DateSupport.getTimeByZone(
									eventBean.getEventDate(), adminUserInfoBean.getTimezone()));

							arrEventBean.add(eventBean);
						}
					}
				}
			}
		}
		return arrEventBean;
	}

	public JSONObject getEventJson(ArrayList<EventBean> arrEventBean)
	{
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonEventArray = new JSONArray();
		try
		{
			if (arrEventBean != null && !arrEventBean.isEmpty())
			{
				Integer numOfEvent = 0;
				for (EventBean eventBean : arrEventBean)
				{
					jsonEventArray.put(numOfEvent, eventBean.toJson());
					numOfEvent++;
				}

				jsonObject.put("num_of_rows", ParseUtil.iToI(arrEventBean.size()));
				jsonObject.put("events", jsonEventArray);
			}
		} catch (JSONException e)
		{
			appLogging.error("Error converting EventBean to JSON "
					+ ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}
}
