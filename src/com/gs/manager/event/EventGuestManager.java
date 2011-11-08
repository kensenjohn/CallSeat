package com.gs.manager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.data.GuestData;

public class EventGuestManager
{
	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public Integer assignGuestToEvent(EventGuestBean eventGuestBean)
	{
		int iNumOfRecs = 0;
		if (eventGuestBean != null && eventGuestBean.getGuestId() != null
				&& eventGuestBean.getEventId() != null
				&& !"".equalsIgnoreCase(eventGuestBean.getGuestId())
				&& !"".equalsIgnoreCase(eventGuestBean.getEventId()))
		{

			GuestData guestData = new GuestData();

			iNumOfRecs = guestData.assignGuestToEvent(eventGuestBean);

			if (iNumOfRecs > 0)
			{
				appLogging.info("Event Guest assignment successful for  : "
						+ eventGuestBean.getGuestId() + " Event Id = "
						+ eventGuestBean.getEventId());
			} else
			{
				appLogging.error("Error assigning Event Guest " + eventGuestBean.getGuestId()
						+ " Event Id = " + eventGuestBean.getEventId());
				eventGuestBean = null;
			}
		} else
		{
			appLogging.error("There was an error in input : " + eventGuestBean);
		}

		return iNumOfRecs;
	}

	public HashMap<String, ArrayList<EventGuestBean>> getEventGuests(
			ArrayList<GuestBean> arrGuestBean)
	{
		HashMap<String, ArrayList<EventGuestBean>> hmEventGuestBean = new HashMap<String, ArrayList<EventGuestBean>>();
		if (arrGuestBean != null && !arrGuestBean.isEmpty())
		{
			GuestData guestData = new GuestData();
			for (GuestBean guestBean : arrGuestBean)
			{
				ArrayList<EventGuestBean> arrEventGuestsBean = guestData.getEventGuests(guestBean
						.getGuestId());

				hmEventGuestBean.put(guestBean.getGuestId(), arrEventGuestsBean);
			}
		}
		return hmEventGuestBean;
	}

	public JSONObject getEventGuestsJson(HashMap<String, ArrayList<EventGuestBean>> hmEventGuestBean)
	{
		JSONObject jsonObject = new JSONObject();
		try
		{

			if (hmEventGuestBean != null && !hmEventGuestBean.isEmpty())
			{
				Set<String> setGuestId = hmEventGuestBean.keySet();
				int numOfGuests = 0;
				for (String sGuestId : setGuestId)
				{
					ArrayList<EventGuestBean> arrEventGuestBean = hmEventGuestBean.get(sGuestId);
					JSONArray jsonEventGuestArray = new JSONArray();
					int numOfRows = 0;
					if (arrEventGuestBean != null && !arrEventGuestBean.isEmpty())
					{
						int iIndex = 0;
						for (EventGuestBean eventGuestBean : arrEventGuestBean)
						{
							jsonEventGuestArray.put(iIndex, eventGuestBean.toJson());
							iIndex++;
						}
						numOfRows++;
					}
					JSONObject jsonGuestObject = new JSONObject();
					jsonGuestObject.put("num_of_event_guest_rows", ParseUtil.iToI(numOfRows));
					jsonGuestObject.put("event_guests", jsonEventGuestArray);

					jsonObject.put(sGuestId, jsonGuestObject);
					numOfGuests++;
				}

			}

		} catch (JSONException e)
		{
			appLogging.error(ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}
}
