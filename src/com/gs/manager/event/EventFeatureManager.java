package com.gs.manager.event;

import com.gs.bean.EventFeatureBean;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

    public static Integer getIntegerValueFromEventFeature(String sEventId , Constants.EVENT_FEATURES eventFeature )
    {
        return ParseUtil.sToI( getValueFromEventFeature(sEventId, eventFeature ) );
    }

    public static String getStringValueFromEventFeature(String sEventId , Constants.EVENT_FEATURES eventFeature )
    {
        return ParseUtil.checkNull( getValueFromEventFeature(sEventId, eventFeature ) );
    }

    private static String getValueFromEventFeature(String sEventId , Constants.EVENT_FEATURES eventFeature )
    {
        String sDataValue = "";
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId ) && eventFeature!=null && !"".equalsIgnoreCase(eventFeature.getEventFeature() ))
        {
            EventFeatureManager eventFeatureManager = new EventFeatureManager();
            EventFeatureBean eventFeatureBean = eventFeatureManager.getEventFeatures(sEventId , eventFeature );
            if(eventFeatureBean!=null)
            {
                HashMap<String, String> hmFeatureValue = eventFeatureBean.getHmFeatureValue();
                if( hmFeatureValue!=null && !hmFeatureValue.isEmpty() )
                {
                    for( Map.Entry<String,String> mapFeatureValue : hmFeatureValue.entrySet() )
                    {
                        if( eventFeature.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey()) )
                        {
                            sDataValue = ParseUtil.checkNull(mapFeatureValue.getValue());
                        }
                    }
                }
            }
        }

        return sDataValue;
    }
}
