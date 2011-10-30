package com.gs.bean;

public class EventBean
{
	private String eventId = "";
	private String eventNum = "";
	private String eventName = "";
	private String eventFolderId = "";
	private Long eventCreateDate = 0L;
	private String eventAdminId = "";

	private String isTmp = "";
	private String delRow = "";

	public String getEventNum()
	{
		return eventNum;
	}

	public void setEventNum(String eventNum)
	{
		this.eventNum = eventNum;
	}

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public String getEventFolderId()
	{
		return eventFolderId;
	}

	public void setEventFolderId(String eventFolderId)
	{
		this.eventFolderId = eventFolderId;
	}

	public Long getEventCreateDate()
	{
		return eventCreateDate;
	}

	public void setEventCreateDate(Long eventCreateDate)
	{
		this.eventCreateDate = eventCreateDate;
	}

	public String getEventAdminId()
	{
		return eventAdminId;
	}

	public void setEventAdminId(String eventAdminId)
	{
		this.eventAdminId = eventAdminId;
	}

	public String getIsTmp()
	{
		return isTmp;
	}

	public void setIsTmp(String isTmp)
	{
		this.isTmp = isTmp;
	}

	public String getDelRow()
	{
		return delRow;
	}

	public void setDelRow(String delRow)
	{
		this.delRow = delRow;
	}

	@Override
	public String toString()
	{
		return "EventBean [eventId=" + eventId + ", eventName=" + eventName + ", eventFolderId="
				+ eventFolderId + ", eventCreateDate=" + eventCreateDate + ", eventAdminId="
				+ eventAdminId + ", isTmp=" + isTmp + ", delRow=" + delRow + "]";
	}

	/*
	 * EVENTID INT(11) NOT NULL, EVENTNAME VARCHAR(2048) NOT NULL DEFAULT 'New
	 * Event', FK_FOLDERID VARCHAR(45) NOT NULL, CREATEDATE TIMESTAMP NOT NULL
	 * DEFAULT '1980-01-01 00:00:00', FK_ADMINID VARCHAR(45) NOT NULL, IS_TMP
	 * INT(1) NOT NULL DEFAULT 1 , DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY
	 * KEY (EVENTID)
	 */
}
