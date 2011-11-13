package com.gs.bean;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.gs.common.ParseUtil;

public class AssignedGuestBean
{

	private String guestId = "";
	private String firstName = "";
	private String lastName = "";

	private String cellNumber = "";
	private String homeNumber = "";

	private String rsvpSeats = "";
	private String assignedSeats = "";
	private String unAssignedSeats = "";

	public String getUnAssignedSeats()
	{
		return unAssignedSeats;
	}

	public void setUnAssignedSeats(String unAssignedSeats)
	{
		this.unAssignedSeats = unAssignedSeats;
	}

	private String totalTableSeats = "";

	public AssignedGuestBean()
	{

	}

	public AssignedGuestBean(HashMap<String, String> hmAssignedGuests)
	{
		this.guestId = ParseUtil.checkNull(hmAssignedGuests.get("GUESTID"));
		this.firstName = ParseUtil.checkNull(hmAssignedGuests.get("FIRST_NAME"));
		this.lastName = ParseUtil.checkNull(hmAssignedGuests.get("LAST_NAME"));

		this.cellNumber = ParseUtil.checkNull(hmAssignedGuests.get("CELL_NUMBER"));
		this.homeNumber = ParseUtil.checkNull(hmAssignedGuests.get("PHONE_NUMBER"));

		this.rsvpSeats = ParseUtil.checkNull(hmAssignedGuests.get("RSVP_SEATS"));
		this.assignedSeats = ParseUtil.checkNull(hmAssignedGuests.get("ASSIGNED_SEATS"));

		this.totalTableSeats = ParseUtil.checkNull(hmAssignedGuests.get("NUMOFSEATS"));
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getCellNumber()
	{
		return cellNumber;
	}

	public void setCellNumber(String cellNumber)
	{
		this.cellNumber = cellNumber;
	}

	public String getHomeNumber()
	{
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber)
	{
		this.homeNumber = homeNumber;
	}

	public String getRsvpSeats()
	{
		return rsvpSeats;
	}

	public void setRsvpSeats(String rsvpSeats)
	{
		this.rsvpSeats = rsvpSeats;
	}

	public String getAssignedSeats()
	{
		return assignedSeats;
	}

	public void setAssignedSeats(String assignedSeats)
	{
		this.assignedSeats = assignedSeats;
	}

	public String getTotalTableSeats()
	{
		return totalTableSeats;
	}

	public void setTotalTableSeats(String totalTableSeats)
	{
		this.totalTableSeats = totalTableSeats;
	}

	public String getGuestId()
	{
		return guestId;
	}

	public void setGuestId(String guestId)
	{
		this.guestId = guestId;
	}

	public JSONObject toJson()
	{
		JSONObject jsonObject = new JSONObject();

		/*
		 * private String guestId = ""; private String firstName = ""; private
		 * String lastName = "";
		 * 
		 * private String cellNumber = ""; private String homeNumber = "";
		 * 
		 * private String rsvpSeats = ""; private String assignedSeats = "";
		 * 
		 * private String totalTableSeats = "";
		 */

		try
		{
			jsonObject.put("guest_id", this.guestId);
			jsonObject.put("first_name", this.firstName);
			jsonObject.put("last_name", this.lastName);
			jsonObject.put("cell_number", this.cellNumber);
			jsonObject.put("home_number", this.homeNumber);
			jsonObject.put("rsvp_seats", this.rsvpSeats);
			jsonObject.put("assigned_seats", this.assignedSeats);
			jsonObject.put("total_table_seats", this.totalTableSeats);

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;

	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("AssignedGuestBean [firstName=").append(firstName).append(", lastName=")
				.append(lastName).append(", cellNumber=").append(cellNumber)
				.append(", homeNumber=").append(homeNumber).append(", rsvpSeats=")
				.append(rsvpSeats).append(", assignedSeats=").append(assignedSeats)
				.append(", totalTableSeats=").append(totalTableSeats).append("]");
		return builder.toString();
	}
}
