package com.gs.bean;

import java.util.HashMap;

import com.gs.common.Constants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.exception.ExceptionHandler;
import com.gs.common.ParseUtil;

public class EventBean {
	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");
	private String eventId = "";
	private String eventNum = "";
	private String eventName = "";
	private String eventFolderId = "";
	private Long eventCreateDate = 0L;
	private String eventAdminId = "";
	private Long eventDate = 0L;

	private String isTmp = "";
	private String delRow = "";

	private String humanEventDate = "";
	private String humanCreateDate = "";

    private Long rsvpDeadlineDate = 0L;
    private String humanRsvpDeadlineDate = Constants.EMPTY;
    private String eventTimeZone = Constants.EMPTY;

	public EventBean() {

	}

	public EventBean(HashMap<String, String> hmEventBean) {
		this.eventId = ParseUtil.checkNull(hmEventBean.get("EVENTID"));
		this.eventNum = ParseUtil.checkNull(hmEventBean.get("EVENTNUM"));
		this.eventName = ParseUtil.checkNull(hmEventBean.get("EVENTNAME"));
		this.eventFolderId = ParseUtil.checkNull(hmEventBean.get("FK_FOLDERID"));
		this.eventCreateDate = ParseUtil.sToL(hmEventBean.get("CREATEDATE"));
		this.eventAdminId = ParseUtil.checkNull(hmEventBean.get("FK_ADMINID"));
		this.isTmp = ParseUtil.checkNull(hmEventBean.get("IS_TMP"));
		this.delRow = ParseUtil.checkNull(hmEventBean.get("DEL_ROW"));
		this.eventDate = ParseUtil.sToL(hmEventBean.get("EVENTDATE"));
		this.humanEventDate = ParseUtil.checkNull(hmEventBean.get("HUMANEVENTDATE"));
		this.humanCreateDate = ParseUtil.checkNull(hmEventBean.get("HUMANCREATEDATE"));
        this.rsvpDeadlineDate = ParseUtil.sToL(hmEventBean.get("RSVPDEADLINEDATE"));
        this.humanRsvpDeadlineDate = ParseUtil.checkNull(hmEventBean.get("HUMANRSVPDEADLINEDATE"));
        this.eventTimeZone = ParseUtil.checkNull(hmEventBean.get("EVENTTIMEZONE"));
	}

	public String getEventNum() {
		return eventNum;
	}

	public void setEventNum(String eventNum) {
		this.eventNum = eventNum;
	}

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

	public String getEventFolderId() {
		return eventFolderId;
	}

	public void setEventFolderId(String eventFolderId) {
		this.eventFolderId = eventFolderId;
	}

	public Long getEventCreateDate() {
		return eventCreateDate;
	}

	public void setEventCreateDate(Long eventCreateDate) {
		this.eventCreateDate = eventCreateDate;
	}

	public String getEventAdminId() {
		return eventAdminId;
	}

	public void setEventAdminId(String eventAdminId) {
		this.eventAdminId = eventAdminId;
	}

	public String getIsTmp() {
		return isTmp;
	}

	public void setIsTmp(String isTmp) {
		this.isTmp = isTmp;
	}

	public String getDelRow() {
		return delRow;
	}

	public void setDelRow(String delRow) {
		this.delRow = delRow;
	}

	public Long getEventDate() {
		return eventDate;
	}

	public void setEventDate(Long eventDate) {
		this.eventDate = eventDate;
	}

	public String getHumanEventDate() {
		return humanEventDate;
	}

	public void setHumanEventDate(String humanEventDate) {
		this.humanEventDate = humanEventDate;
	}

	public String getHumanCreateDate() {
		return humanCreateDate;
	}

	public void setHumanCreateDate(String humanCreateDate) {
		this.humanCreateDate = humanCreateDate;
	}

    public Long getRsvpDeadlineDate() {
        return rsvpDeadlineDate;
    }

    public void setRsvpDeadlineDate(Long rsvpDeadlineDate) {
        this.rsvpDeadlineDate = rsvpDeadlineDate;
    }

    public String getHumanRsvpDeadlineDate() {
        return humanRsvpDeadlineDate;
    }

    public void setHumanRsvpDeadlineDate(String humanRsvpDeadlineDate) {
        this.humanRsvpDeadlineDate = humanRsvpDeadlineDate;
    }

    public String getEventTimeZone() {
        return eventTimeZone;
    }

    public void setEventTimeZone(String eventTimeZone) {
        this.eventTimeZone = eventTimeZone;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "eventId='" + eventId + '\'' +
                ", eventNum='" + eventNum + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventFolderId='" + eventFolderId + '\'' +
                ", eventCreateDate=" + eventCreateDate +
                ", eventAdminId='" + eventAdminId + '\'' +
                ", eventDate=" + eventDate +
                ", isTmp='" + isTmp + '\'' +
                ", delRow='" + delRow + '\'' +
                ", humanEventDate='" + humanEventDate + '\'' +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", rsvpDeadlineDate=" + rsvpDeadlineDate +
                ", humanRsvpDeadlineDate='" + humanRsvpDeadlineDate + '\'' +
                ", eventTimeZone='" + eventTimeZone + '\'' +
                '}';
    }

	public JSONObject toJson() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("event_id", eventId);
			jsonObject.put("event_num", eventNum);
			jsonObject.put("event_name", eventName);
			jsonObject.put("event_folder_id", eventFolderId);
			jsonObject.put("event_create_date", eventCreateDate);
			jsonObject.put("event_admin_id", eventAdminId);
			jsonObject.put("event_date", eventDate);

			jsonObject.put("is_tmp", isTmp);
			jsonObject.put("del_row", delRow);

			jsonObject.put("human_event_date", humanEventDate);
			jsonObject.put("human_create_date", humanCreateDate);

            jsonObject.put("rsvp_deadline_date", rsvpDeadlineDate);
            jsonObject.put("human_rsvp_deadline_date", humanRsvpDeadlineDate);
            jsonObject.put("event_time_zone", eventTimeZone);
		} catch (Exception e) {
			appLogging.error("Error creating EventBean Object \n"
					+ ExceptionHandler.getStackTrace(e));
		}
		return jsonObject;
	}
}
