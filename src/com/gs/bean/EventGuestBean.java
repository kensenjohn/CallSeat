package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class EventGuestBean
{
	private String eventGuestId = "";
	private String eventId = "";
	private String guestId = "";
	private String isTemporary = "";
	private String deleteRow = "";
	private String totalNumberOfSeats = "";
	private String rsvpSeats = "";

	public String getEventGuestId()
	{
		return eventGuestId;
	}

	public void setEventGuestId(String eventGuestId)
	{
		this.eventGuestId = eventGuestId;
	}

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public String getGuestId()
	{
		return guestId;
	}

	public void setGuestId(String guestId)
	{
		this.guestId = guestId;
	}

	public String getIsTemporary()
	{
		return isTemporary;
	}

	public void setIsTemporary(String isTemporary)
	{
		this.isTemporary = isTemporary;
	}

	public String getDeleteRow()
	{
		return deleteRow;
	}

	public void setDeleteRow(String deleteRow)
	{
		this.deleteRow = deleteRow;
	}

	public String getTotalNumberOfSeats()
	{
		return totalNumberOfSeats;
	}

	public void setTotalNumberOfSeats(String totalNumberOfSeats)
	{
		this.totalNumberOfSeats = totalNumberOfSeats;
	}

	public String getRsvpSeats()
	{
		return rsvpSeats;
	}

	public void setRsvpSeats(String rsvpSeats)
	{
		this.rsvpSeats = rsvpSeats;
	}

	public JSONObject toJson()
	{

		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("event_guest_id", this.eventGuestId);
			jsonObject.put("event_id", this.eventId);
			jsonObject.put("guest_id", this.guestId);
			jsonObject.put("is_tmp", this.isTemporary);
			jsonObject.put("del_row", this.deleteRow);
			jsonObject.put("total_seats", this.totalNumberOfSeats);
			jsonObject.put("rsvp_seats", this.rsvpSeats);

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
