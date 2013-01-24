package com.gs.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event_id", this.eventId);

            JSONObject jsonMapFeatureValue = new JSONObject();
            if(hmFeatureValue!=null && !hmFeatureValue.isEmpty())
            {
                for(Map.Entry<String,String> mapHmFeatureValue : hmFeatureValue.entrySet() )
                {
                    jsonMapFeatureValue.put(mapHmFeatureValue.getKey(), mapHmFeatureValue.getValue());
                }
            }

            JSONObject jsonMapFeatureId = new JSONObject();
            if(hmFeatureId!=null && !hmFeatureId.isEmpty())
            {
                for(Map.Entry<String,String> mapHmFeatureId : hmFeatureId.entrySet() )
                {
                    jsonMapFeatureId.put(mapHmFeatureId.getKey(), mapHmFeatureId.getValue());
                }
            }

            jsonObject.put("map_feature_value", jsonMapFeatureValue);
            jsonObject.put("map_feature_id", jsonMapFeatureId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
