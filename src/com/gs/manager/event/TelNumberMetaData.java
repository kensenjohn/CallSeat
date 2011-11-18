package com.gs.manager.event;

public class TelNumberMetaData
{
	private String guestTelNumber = "";
	private String eventTaskTelNumber = "";
	private String eventId = "";
	private String adminId = "";

	public String getGuestTelNumber()
	{
		return guestTelNumber;
	}

	public void setGuestTelNumber(String guestTelNumber)
	{
		this.guestTelNumber = guestTelNumber;
	}

	public String getEventTaskTelNumber()
	{
		return eventTaskTelNumber;
	}

	public void setEventTaskTelNumber(String eventTaskTelNumber)
	{
		this.eventTaskTelNumber = eventTaskTelNumber;
	}

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public String getAdminId()
	{
		return adminId;
	}

	public void setAdminId(String adminId)
	{
		this.adminId = adminId;
	}

}
