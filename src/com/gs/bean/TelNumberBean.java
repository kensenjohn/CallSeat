package com.gs.bean;

public class TelNumberBean
{
	/*
	 * TELNUMBERID, TELNUMBER, FK_EVENTID ,FK_ADMINID ,
	 * DEL_ROW,TELNUMBERTYPEID,DESCRIPTION,TELNUMTYPE
	 */
	private String telNumberId = "";
	private String telNumber = "";
	private String telNumberTypeId = "";
	private String telNumberType = "";
	private String eventId = "";
	private String adminId = "";
	private String delRow = "";

	public boolean isTelNumBeanSet()
	{
		if (telNumberId != null && !"".equalsIgnoreCase(telNumberId) && telNumber != null
				&& !"".equalsIgnoreCase(telNumber) && eventId != null
				&& !"".equalsIgnoreCase(eventId))
		{
			return true;
		}
		return false;
	}

	public String getTelNumberId()
	{
		return telNumberId;
	}

	public void setTelNumberId(String telNumberId)
	{
		this.telNumberId = telNumberId;
	}

	public String getTelNumber()
	{
		return telNumber;
	}

	public void setTelNumber(String telNumber)
	{
		this.telNumber = telNumber;
	}

	public String getTelNumberTypeId()
	{
		return telNumberTypeId;
	}

	public void setTelNumberTypeId(String telNumberTypeId)
	{
		this.telNumberTypeId = telNumberTypeId;
	}

	public String getTelNumberType()
	{
		return telNumberType;
	}

	public void setTelNumberType(String telNumberType)
	{
		this.telNumberType = telNumberType;
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

	public String getDelRow()
	{
		return delRow;
	}

	public void setDelRow(String delRow)
	{
		this.delRow = delRow;
	}

}
