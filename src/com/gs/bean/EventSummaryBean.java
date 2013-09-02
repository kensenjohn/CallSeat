package com.gs.bean;

import com.gs.bean.usage.PhoneCallUsageBean;
import com.gs.bean.usage.TextMessageUsageBean;
import org.json.JSONException;
import org.json.JSONObject;

public class EventSummaryBean {
    private String eventId = "";
    private String eventName = "";
    private String eventDate = "";

    private Integer totalTable = 0;
    private Integer totalSeats = 0;
    private Integer assignedSeats = 0;
    private Integer totalGuestParty = 0;
    private Integer totalGuestsInvited = 0;
    private Integer totalGuestRsvp = 0;

    private String rsvpNumber = "";
    private String seatingNumber = "";
    private String telephonyEventNumber = "";
    private String telephonyRSVPSecretKey = "";
    private String telephonySeatingSecretKey = "";
    private boolean isDemoMode = true;

    private PhoneCallUsageBean phoneCallUsageBean = new PhoneCallUsageBean();
    private TextMessageUsageBean textMessageUsageBean = new TextMessageUsageBean();

    private EventDateObj eventDateObj = new EventDateObj();
    private String rsvpDeadLineDate = "";

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

    public String getTelephonyEventNumber() {
        return telephonyEventNumber;
    }

    public void setTelephonyEventNumber(String telephonyEventNumber) {
        this.telephonyEventNumber = telephonyEventNumber;
    }

    public String getTelephonyRSVPSecretKey() {
        return telephonyRSVPSecretKey;
    }

    public void setTelephonyRSVPSecretKey(String telephonyRSVPSecretKey) {
        this.telephonyRSVPSecretKey = telephonyRSVPSecretKey;
    }

    public String getTelephonySeatingSecretKey() {
        return telephonySeatingSecretKey;
    }

    public void setTelephonySeatingSecretKey(String telephonySeatingSecretKey) {
        this.telephonySeatingSecretKey = telephonySeatingSecretKey;
    }

    public boolean isDemoMode() {
        return isDemoMode;
    }

    public void setDemoMode(boolean demoMode) {
        isDemoMode = demoMode;
    }

    public PhoneCallUsageBean getPhoneCallUsageBean() {
        return phoneCallUsageBean;
    }

    public void setPhoneCallUsageBean(PhoneCallUsageBean phoneCallUsageBean) {
        this.phoneCallUsageBean = phoneCallUsageBean;
    }

    public TextMessageUsageBean getTextMessageUsageBean() {
        return textMessageUsageBean;
    }

    public void setTextMessageUsageBean(TextMessageUsageBean textMessageUsageBean) {
        this.textMessageUsageBean = textMessageUsageBean;
    }

    public EventDateObj getEventDateObj() {
        return eventDateObj;
    }

    public void setEventDateObj(EventDateObj eventDateObj) {
        this.eventDateObj = eventDateObj;
    }

    public String getRsvpDeadLineDate() {
        return rsvpDeadLineDate;
    }

    public void setRsvpDeadLineDate(String rsvpDeadLineDate) {
        this.rsvpDeadLineDate = rsvpDeadLineDate;
    }

    @Override
    public String toString() {
        return "EventSummaryBean{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", totalTable=" + totalTable +
                ", totalSeats=" + totalSeats +
                ", assignedSeats=" + assignedSeats +
                ", totalGuestParty=" + totalGuestParty +
                ", totalGuestsInvited=" + totalGuestsInvited +
                ", totalGuestRsvp=" + totalGuestRsvp +
                ", rsvpNumber='" + rsvpNumber + '\'' +
                ", seatingNumber='" + seatingNumber + '\'' +
                ", telephonyEventNumber='" + telephonyEventNumber + '\'' +
                ", telephonyRSVPSecretKey='" + telephonyRSVPSecretKey + '\'' +
                ", telephonySeatingSecretKey='" + telephonySeatingSecretKey + '\'' +
                ", isDemoMode=" + isDemoMode +
                ", phoneCallUsageBean=" + phoneCallUsageBean +
                ", textMessageUsageBean=" + textMessageUsageBean +
                ", eventDateObj=" + eventDateObj +
                ", rsvpDeadLineDate='" + rsvpDeadLineDate + '\'' +
                '}';
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
            jsonObject.put("telephony_event_number", this.telephonyEventNumber);
            jsonObject.put("telephony_rsvp_secret_key", this.telephonyRSVPSecretKey);
            jsonObject.put("telephony_seating_secret_key", this.telephonySeatingSecretKey);
            jsonObject.put("is_demo_numbers", this.isDemoMode);

            jsonObject.put("phone_call_usage", this.phoneCallUsageBean.toJson());
            jsonObject.put("text_message_usage", this.textMessageUsageBean.toJson());
            jsonObject.put("event_date_obj", this.eventDateObj.toJson());
            jsonObject.put("rsvp_deadline_date", this.rsvpDeadLineDate );

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
