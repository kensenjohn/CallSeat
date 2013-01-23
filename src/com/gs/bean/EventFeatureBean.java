package com.gs.bean;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/23/13
 * Time: 1:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventFeatureBean {
    private String eventId = "";
    private HashMap<String,String> hmFeatureValue = new HashMap<String, String>();
    private HashMap<String,String> hmFeatureId = new HashMap<String, String>();


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public HashMap<String, String> getHmFeatureValue() {
        return hmFeatureValue;
    }

    public void setHmFeatureValue(HashMap<String, String> hmFeatureValue) {
        this.hmFeatureValue = hmFeatureValue;
    }

    public HashMap<String, String> getHmFeatureId() {
        return hmFeatureId;
    }

    public void setHmFeatureId(HashMap<String, String> hmFeatureId) {
        this.hmFeatureId = hmFeatureId;
    }

    @Override
    public String toString() {
        return "EventFeatureBean{" +
                "eventId='" + eventId + '\'' +
                ", hmFeatureValue=" + hmFeatureValue +
                ", hmFeatureId=" + hmFeatureId +
                '}';
    }

}
