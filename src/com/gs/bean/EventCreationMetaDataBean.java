package com.gs.bean;

public class EventCreationMetaDataBean {
	private AdminBean adminBean = null;
	private String eventId = "";
	private String eventName = "";
	private String eventDate = "";
	private String eventDatePattern = "";
	private String eventTimeZone = "";
    private String rsvpDeadlineDate = "";
    private String rsvpDeadlineDateDatePattern = "";
    private boolean isCreateEvent = false;
    private boolean isUpdateEvent = false;

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

    public String getRsvpDeadlineDate() {
        return rsvpDeadlineDate;
    }

    public void setRsvpDeadlineDate(String rsvpDeadlineDate) {
        this.rsvpDeadlineDate = rsvpDeadlineDate;
    }

    public String getRsvpDeadlineDateDatePattern() {
        return rsvpDeadlineDateDatePattern;
    }

    public void setRsvpDeadlineDateDatePattern(String rsvpDeadlineDateDatePattern) {
        this.rsvpDeadlineDateDatePattern = rsvpDeadlineDateDatePattern;
    }

    public boolean isCreateEvent() {
        return isCreateEvent;
    }

    public void setCreateEvent(boolean createEvent) {
        isCreateEvent = createEvent;
    }

    public boolean isUpdateEvent() {
        return isUpdateEvent;
    }

    public void setUpdateEvent(boolean updateEvent) {
        isUpdateEvent = updateEvent;
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
