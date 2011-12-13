package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TelNumberBean {
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
	private boolean isactive = false;
	private boolean isPurchased = false;
	private boolean isNew = false;

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isTelNumBeanSet() {
		if (telNumberId != null && !"".equalsIgnoreCase(telNumberId)
				&& telNumber != null && !"".equalsIgnoreCase(telNumber)
				&& eventId != null && !"".equalsIgnoreCase(eventId)) {
			return true;
		}
		return false;
	}

	public String getTelNumberId() {
		return telNumberId;
	}

	public void setTelNumberId(String telNumberId) {
		this.telNumberId = telNumberId;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getTelNumberTypeId() {
		return telNumberTypeId;
	}

	public void setTelNumberTypeId(String telNumberTypeId) {
		this.telNumberTypeId = telNumberTypeId;
	}

	public String getTelNumberType() {
		return telNumberType;
	}

	public void setTelNumberType(String telNumberType) {
		this.telNumberType = telNumberType;
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

	public String getDelRow() {
		return delRow;
	}

	public void setDelRow(String delRow) {
		this.delRow = delRow;
	}

	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public boolean isPurchased() {
		return isPurchased;
	}

	public void setPurchased(boolean isPurchased) {
		this.isPurchased = isPurchased;
	}

	public JSONObject toJson() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("telnum_id", telNumberId);
			jsonObject.put("telnum", telNumber);
			jsonObject.put("telnum_type_id", telNumberTypeId);
			jsonObject.put("telnum_type", telNumberType);
			jsonObject.put("event_id", eventId);
			jsonObject.put("admin_id", adminId);
			jsonObject.put("is_purchased", isPurchased);
			jsonObject.put("is_active", isactive);
			jsonObject.put("del_row", delRow);
			jsonObject.put("is_n ew", isNew);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
