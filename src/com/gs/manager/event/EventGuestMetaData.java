package com.gs.manager.event;

import java.util.ArrayList;

public class EventGuestMetaData
{
	private String eventId = "";
	private ArrayList<String> arrGuestId = new ArrayList<String>();

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public ArrayList<String> getArrGuestId()
	{
		return arrGuestId;
	}

	public void setArrGuestId(ArrayList<String> arrGuestId)
	{
		this.arrGuestId = arrGuestId;
	}

}
