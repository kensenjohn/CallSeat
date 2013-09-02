package com.gs.bean;

import com.gs.common.Constants;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/31/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventDateObj {
    private String eventDate = Constants.EMPTY;
    private String eventHr = Constants.EMPTY;
    private String eventMin = Constants.EMPTY;
    private String eventAmPm = Constants.EMPTY;
    private String eventTimeZone = Constants.EMPTY;

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventHr() {
        return eventHr;
    }

    public void setEventHr(String eventHr) {
        this.eventHr = eventHr;
    }

    public String getEventMin() {
        return eventMin;
    }

    public void setEventMin(String eventMin) {
        this.eventMin = eventMin;
    }

    public String getEventTimeZone() {
        return eventTimeZone;
    }

    public void setEventTimeZone(String eventTimeZone) {
        this.eventTimeZone = eventTimeZone;
    }

    public String getEventAmPm() {
        return eventAmPm;
    }

    public void setEventAmPm(String eventAmPm) {
        this.eventAmPm = eventAmPm;
    }

    @Override
    public String toString() {
        return "EventDateObj{" +
                "eventDate='" + eventDate + '\'' +
                ", eventHr='" + eventHr + '\'' +
                ", eventMin='" + eventMin + '\'' +
                ", eventAmPm='" + eventAmPm + '\'' +
                ", eventTimeZone='" + eventTimeZone + '\'' +
                '}';
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event_date", this.eventDate);
            jsonObject.put("event_hr", this.eventHr);
            jsonObject.put("event_min", this.eventMin);
            jsonObject.put("event_ampm", this.eventAmPm);
            jsonObject.put("event_timezone", this.eventTimeZone);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
