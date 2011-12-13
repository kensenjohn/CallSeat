package com.gs.manager.event;

public class TelNumberMetaData {
	private String guestTelNumber = "";
	private String eventTaskTelNumber = "";
	private String eventId = "";
	private String adminId = "";
	private String digits = "";

	private String areaCodeSearch = "";
	private String textPatternSearch = "";

	public String getGuestTelNumber() {
		return guestTelNumber;
	}

	public void setGuestTelNumber(String guestTelNumber) {
		this.guestTelNumber = guestTelNumber;
	}

	public String getEventTaskTelNumber() {
		return eventTaskTelNumber;
	}

	public void setEventTaskTelNumber(String eventTaskTelNumber) {
		this.eventTaskTelNumber = eventTaskTelNumber;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getDigits() {
		return digits;
	}

	public void setDigits(String digits) {
		this.digits = digits;
	}

	public String getAreaCodeSearch() {
		return areaCodeSearch;
	}

	public void setAreaCodeSearch(String areaCodeSearch) {
		this.areaCodeSearch = areaCodeSearch;
	}

	public String getTextPatternSearch() {
		return textPatternSearch;
	}

	public void setTextPatternSearch(String textPatternSearch) {
		this.textPatternSearch = textPatternSearch;
	}

}
