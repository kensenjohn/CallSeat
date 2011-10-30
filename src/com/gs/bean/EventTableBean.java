package com.gs.bean;

public class EventTableBean
{
	// EVENTTABLEID VARCHAR(45) NOT NULL,
	// FK_EVENTID INT(11) NOT NULL, FK_TABLEID VARCHAR(45) NOT NULL, IS_TMP
	// INT(1) NOT NULL DEFAULT 1, DEL_ROW INT(1) NOT NULL DEFAULT 0

	private String eventTableId = "";
	private String eventId = "";
	private String tableId = "";
	private String isTmp = "";
	private String delRow = "";
	private String assignToEvent = "";

	public String getEventTableId()
	{
		return eventTableId;
	}

	public void setEventTableId(String eventTableId)
	{
		this.eventTableId = eventTableId;
	}

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public String getTableId()
	{
		return tableId;
	}

	public void setTableId(String tableId)
	{
		this.tableId = tableId;
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

	public String getAssignToEvent()
	{
		return assignToEvent;
	}

	public void setAssignToEvent(String assignToEvent)
	{
		this.assignToEvent = assignToEvent;
	}

	@Override
	public String toString()
	{
		return "EventTableBean [eventTableId=" + eventTableId + ", eventId=" + eventId
				+ ", tableId=" + tableId + ", isTmp=" + isTmp + ", delRow=" + delRow
				+ ", assignToEvent=" + assignToEvent + "]";
	}

}
