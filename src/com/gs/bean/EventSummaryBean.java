package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class EventSummaryBean {
	String eventId = "";
	String eventName = "";
	String eventDate = "";

	Integer totalTable = 0;
	Integer totalSeats = 0;
	Integer assignedSeats = 0;
	Integer totalGuestParty = 0;
	Integer totalGuestsInvited = 0;
	Integer totalGuestRsvp = 0;

	String rsvpNumber = "";
	String seatingNumber = "";

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public Integer getTotalTable() {
		return totalTable;
	}

	public void setTotalTable(Integer totalTable) {
		this.totalTable = totalTable;
	}

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}

	public Integer getAssignedSeats() {
		return assignedSeats;
	}

	public void setAssignedSeats(Integer assignedSeats) {
		this.assignedSeats = assignedSeats;
	}

	public Integer getTotalGuestParty() {
		return totalGuestParty;
	}

	public void setTotalGuestParty(Integer totalGuestParty) {
		this.totalGuestParty = totalGuestParty;
	}

	public Integer getTotalGuestsInvited() {
		return totalGuestsInvited;
	}

	public void setTotalGuestsInvited(Integer totalGuestsInvited) {
		this.totalGuestsInvited = totalGuestsInvited;
	}

	public Integer getTotalGuestRsvp() {
		return totalGuestRsvp;
	}

	public void setTotalGuestRsvp(Integer totalGuestRsvp) {
		this.totalGuestRsvp = totalGuestRsvp;
	}

	public String getRsvpNumber() {
		return rsvpNumber;
	}

	public void setRsvpNumber(String rsvpNumber) {
		this.rsvpNumber = rsvpNumber;
	}

	public String getSeatingNumber() {
		return seatingNumber;
	}

	public void setSeatingNumber(String seatingNumber) {
		this.seatingNumber = seatingNumber;
	}

	public JSONObject toJson() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("event_id", this.eventId);
			jsonObject.put("event_name", this.eventName);
			jsonObject.put("event_date", this.eventDate);
			jsonObject.put("total_table", this.totalTable);
			jsonObject.put("total_seats", this.totalSeats);
			jsonObject.put("assigned_seats", this.assignedSeats);
			jsonObject.put("total_guest_party", this.totalGuestParty);
			jsonObject.put("total_guest_invited", this.totalGuestsInvited);
			jsonObject.put("total_guest_rsvp", this.totalGuestRsvp);
			jsonObject.put("rsvp_tel_number", this.rsvpNumber);
			jsonObject.put("seating_tel_number", this.seatingNumber);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
