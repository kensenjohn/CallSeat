package com.gs.manager.event;

public class GuestTableMetaData
{
	private String eventId = "";
	private String adminId = "";
	private String tableId = "";
	private String guestId = "";

	public String getGuestId()
	{
		return guestId;
	}

	public void setGuestId(String guestId)
	{
		this.guestId = guestId;
	}

	private Integer numOfSeats = 0;

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

	public String getTableId()
	{
		return tableId;
	}

	public void setTableId(String tableId)
	{
		this.tableId = tableId;
	}

	public Integer getNumOfSeats()
	{
		return numOfSeats;
	}

	public void setNumOfSeats(Integer numOfSeats)
	{
		this.numOfSeats = numOfSeats;
	}

}
