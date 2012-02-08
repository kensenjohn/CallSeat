package com.gs.bean;

public class EventCreationMetaDataBean {
	private AdminBean adminBean = null;
	private String eventId = "";
	private String eventName = "";
	private String eventDate = "";
	private String eventDatePattern = "";
	private String eventTimeZone = "";

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

	public String getEventTimeZone() {
		return eventTimeZone;
	}

	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}

	public String getEventDate() {
		return eventDate;
	}

	public AdminBean getAdminBean() {
		return adminBean;
	}

	public void setAdminBean(AdminBean adminBean) {
		this.adminBean = adminBean;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventDatePattern() {
		return eventDatePattern;
	}

	public void setEventDatePattern(String eventDatePattern) {
		this.eventDatePattern = eventDatePattern;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventCreationMetaDataBean [adminBean=")
				.append(adminBean).append(", eventDate=").append(eventDate)
				.append(", eventDatePattern=").append(eventDatePattern)
				.append("]");
		return builder.toString();
	}

}
