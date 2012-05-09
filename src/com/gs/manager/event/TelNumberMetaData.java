package com.gs.manager.event;

public class TelNumberMetaData {
	private String guestTelNumber = "";
	private String eventTaskTelNumber = "";
	private String eventId = "";
	private String adminId = "";
	private String digits = "";
	private String seatingTelNumDigit = "";
	private String rsvpTelNumDigit = "";
	private boolean isActive = false;
	private boolean isPurchased = false;
	private String telNumberTypeId = "";
	private boolean isDelRow = false;

	private String secretEventIdentifier = "";
	private String secretEventSecretKey = "";
	private String humanTelNumber = "";

	public String getSeatingTelNumDigit() {
		return seatingTelNumDigit;
	}

	public void setSeatingTelNumDigit(String seatingTelNumDigit) {
		this.seatingTelNumDigit = seatingTelNumDigit;
	}

	public String getRsvpTelNumDigit() {
		return rsvpTelNumDigit;
	}

	public void setRsvpTelNumDigit(String rsvpTelNumDigit) {
		this.rsvpTelNumDigit = rsvpTelNumDigit;
	}

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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isPurchased() {
		return isPurchased;
	}

	public void setPurchased(boolean isPurchased) {
		this.isPurchased = isPurchased;
	}

	public String getTelNumberTypeId() {
		return telNumberTypeId;
	}

	public void setTelNumberTypeId(String telNumberTypeId) {
		this.telNumberTypeId = telNumberTypeId;
	}

	public boolean isDelRow() {
		return isDelRow;
	}

	public void setDelRow(boolean isDelRow) {
		this.isDelRow = isDelRow;
	}

	public String getSecretEventIdentifier() {
		return secretEventIdentifier;
	}

	public void setSecretEventIdentifier(String secretEventIdentifier) {
		this.secretEventIdentifier = secretEventIdentifier;
	}

	public String getSecretEventSecretKey() {
		return secretEventSecretKey;
	}

	public void setSecretEventSecretKey(String secretEventSecretKey) {
		this.secretEventSecretKey = secretEventSecretKey;
	}

	public String getHumanTelNumber() {
		return humanTelNumber;
	}

	public void setHumanTelNumber(String humanTelNumber) {
		this.humanTelNumber = humanTelNumber;
	}

}
