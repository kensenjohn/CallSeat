package com.gs.manager.event;

import com.gs.bean.EventFeatureBean;
import com.gs.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/23/13
 * Time: 1:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventFeatureManager {

    Logger appLogging = LoggerFactory.getLogger("AppLogging");

    public EventFeatureBean getEventFeatures(String sEventId)
    {
        EventFeatureBean eventFeatureBean = new  EventFeatureBean();
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId))
        {
            EventFeatureData eventFeatureData = new EventFeatureData();
            eventFeatureBean = eventFeatureData.getAllEventFeaturesValues(sEventId);
        }
        return eventFeatureBean;
    }

    public EventFeatureBean getEventFeatures(String sEventId, Constants.EVENT_FEATURES eventFeatures)
    {
        EventFeatureBean eventFeatureBean = new  EventFeatureBean();
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId) && eventFeatures!=null)
        {
            EventFeatureData eventFeatureData = new EventFeatureData();
            eventFeatureBean = eventFeatureData.getEventFeaturesValues(sEventId, eventFeatures);
        }
        return eventFeatureBean;
    }

    public Integer createEventFeatures(String sEventId, Constants.EVENT_FEATURES eventFeatures, String sValue)
    {
        Integer iNumOfRows = -1;
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId) && eventFeatures!=null && sValue!=null && !"".equalsIgnoreCase(sValue))
        {

            EventFeatureBean eventFeaturesBean = new EventFeatureBean();
            eventFeaturesBean.setEventId(sEventId);

            HashMap<String,String> hmFeatureValue = new HashMap<String, String>();
            hmFeatureValue.put(eventFeatures.getEventFeature(),sValue);

            EventFeatureData eventFeatureData = new EventFeatureData();
            EventFeatureBean tmpEventFeaturesBean = eventFeatureData.getAllEventFeatures();

            eventFeaturesBean.setHmFeatureId(tmpEventFeaturesBean.getHmFeatureId());
            eventFeaturesBean.setHmFeatureValue(hmFeatureValue);
            iNumOfRows = eventFeatureData.createEventFeatures(eventFeaturesBean);
        }
        return iNumOfRows;
    }

    public Integer updateEventFeatures(String sEventId, Constants.EVENT_FEATURES eventFeatures, String sValue)
    {
        Integer iNumOfRows = -1;
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId) && eventFeatures!=null && sValue!=null)
        {
            EventFeatureBean eventFeaturesBean = new EventFeatureBean();
            eventFeaturesBean.setEventId(sEventId);

            HashMap<String,String> hmFeatureValue = new HashMap<String, String>();
            hmFeatureValue.put(eventFeatures.getEventFeature(),sValue);

            EventFeatureData eventFeatureData = new EventFeatureData();
            EventFeatureBean tmpEventFeaturesBean = eventFeatureData.getEventFeatures(eventFeatures);

            eventFeaturesBean.setHmFeatureId(tmpEventFeaturesBean.getHmFeatureId());
            eventFeaturesBean.setHmFeatureValue(hmFeatureValue);
            iNumOfRows = eventFeatureData.updateEventFeatures(eventFeaturesBean);

        }
        return iNumOfRows;
    }
}
