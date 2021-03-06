package com.gs.manager.event;

import java.util.ArrayList;

public class EventGuestMetaData {
	private String eventId = "";
	private ArrayList<String> arrGuestId = new ArrayList<String>();
	private String rsvpDigits = "";
	private String eventGuestId = "";
	private String guestId = "";

	public String getEventGuestId() {
		return eventGuestId;
	}

	public void setEventGuestId(String eventGuestId) {
		this.eventGuestId = eventGuestId;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public ArrayList<String> getArrGuestId() {
		return arrGuestId;
	}

	public void setArrGuestId(ArrayList<String> arrGuestId) {
		this.arrGuestId = arrGuestId;
	}

	public String getRsvpDigits() {
		return rsvpDigits;
	}

	public void setRsvpDigits(String rsvpDigits) {
		this.rsvpDigits = rsvpDigits;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventGuestMetaData [eventId=").append(eventId)
				.append(", arrGuestId=").append(arrGuestId)
				.append(", rsvpDigits=").append(rsvpDigits)
				.append(", eventGuestId=").append(eventGuestId)
				.append(", guestId=").append(guestId).append("]");
		return builder.toString();
	}
}
