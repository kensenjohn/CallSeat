package com.gs.manager.event;

import com.gs.bean.EventFeatureBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.Utility;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/23/13
 * Time: 1:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventFeatureData {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    Configuration applicationConfig = Configuration
            .getInstance(Constants.APPLICATION_PROP);

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    private String sourceFile = "EventFeatureData.java";

    public EventFeatureBean getAllEventFeaturesValues(String sEventId)
    {
        EventFeatureBean eventFeatureBean = new EventFeatureBean();
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId))
        {
            eventFeatureBean.setEventId(sEventId);
            String sQuery = "SELECT * FROM GTEVENTFEATUREVALUES GTEFV, GTEVENTFEATURES GTF WHERE GTF.FEATUREID = GTEFV.FK_FEATUREID AND " +
                    " GTEFV.FK_EVENTID = ? ";
            ArrayList<Object> aParams = DBDAO.createConstraint(sEventId);

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getAllEventFeatures()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                HashMap<String,String> hmEventFeature = new HashMap<String, String>();
                for(HashMap<String, String> hmResult : arrResult )
                {
                    String sFeatureNameKey = ParseUtil.checkNull(hmResult.get("FEATURE_NAME"));
                    String sFeatureValue = ParseUtil.checkNull(hmResult.get("FEATUREVALUE"));
                    hmEventFeature.put(sFeatureNameKey,sFeatureValue )
                    ;
                    if( Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER.getEventFeature().equalsIgnoreCase( sFeatureNameKey )
                            || Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER.getEventFeature().equalsIgnoreCase( sFeatureNameKey ) )
                    {
                        hmEventFeature.put(sFeatureNameKey+"_HUMAN", Utility.getHumanFormattedNumber(sFeatureValue));
                    }
                }
                eventFeatureBean.setHmFeatureValue(hmEventFeature);
            }
        }
        return eventFeatureBean;
    }

    public EventFeatureBean getEventFeaturesValues(String sEventId,Constants.EVENT_FEATURES eventFeatures)
    {
        EventFeatureBean eventFeatureBean = new EventFeatureBean();
        if(sEventId!=null && !"".equalsIgnoreCase(sEventId) && eventFeatures!=null)
        {
            eventFeatureBean.setEventId(sEventId);
            String sQuery = "SELECT * FROM GTEVENTFEATUREVALUES GTEFV, GTEVENTFEATURES GTF WHERE GTF.FEATUREID = GTEFV.FK_FEATUREID AND " +
                    " GTEFV.FK_EVENTID = ?  AND GTF.FEATURE_NAME = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint(sEventId, eventFeatures.getEventFeature() );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getEventFeaturesValues()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                HashMap<String,String> hmEventFeature = new HashMap<String, String>();
                for(HashMap<String, String> hmResult : arrResult ) {
                    hmEventFeature.put(hmResult.get("FEATURE_NAME"), hmResult.get("FEATUREVALUE"));
                }
                eventFeatureBean.setHmFeatureValue(hmEventFeature);
            }
        }
        return eventFeatureBean;
    }

    public EventFeatureBean getAllEventFeatures( )
    {
        EventFeatureBean eventFeatureBean = new EventFeatureBean();

            String sQuery = "SELECT * FROM  GTEVENTFEATURES GTF";

            ArrayList<Object> aParams = new ArrayList<Object>();

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getAllEventFeatures()");

        if(arrResult!=null && !arrResult.isEmpty())
        {
            HashMap<String,String> hmEventFeatureID = new HashMap<String, String>();
            for(HashMap<String, String> hmResult : arrResult )
            {
                hmEventFeatureID.put(hmResult.get("FEATURE_NAME"), hmResult.get("FEATUREID"));
            }
            eventFeatureBean.setHmFeatureId(hmEventFeatureID);
        }
        return eventFeatureBean;
    }
    public EventFeatureBean getEventFeatures( Constants.EVENT_FEATURES eventFeatures )
    {
        EventFeatureBean eventFeatureBean = new EventFeatureBean();

        if(eventFeatures!=null)
        {
            String sQuery = "SELECT * FROM  GTEVENTFEATURES WHERE FEATURE_NAME = ?";

            ArrayList<Object> aParams = DBDAO.createConstraint(eventFeatures.getEventFeature());

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getEventFeatures()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                HashMap<String,String> hmEventFeatureID = new HashMap<String, String>();
                for(HashMap<String, String> hmResult : arrResult )
                {
                    hmEventFeatureID.put(hmResult.get("FEATURE_NAME"), hmResult.get("FEATUREID"));
                }
                eventFeatureBean.setHmFeatureId(hmEventFeatureID);
            }
        }

        return eventFeatureBean;
    }


    public Integer createEventFeatures(EventFeatureBean eventFeatureBean)
    {
        Integer iNumOfRows = -1;
        if(eventFeatureBean!=null && eventFeatureBean.getHmFeatureValue()!=null && !eventFeatureBean.getHmFeatureValue().isEmpty()
                && eventFeatureBean.getHmFeatureId()!=null && !eventFeatureBean.getHmFeatureId().isEmpty())
        {
            HashMap<String,String> hmFeatureValue = eventFeatureBean.getHmFeatureValue();
            HashMap<String,String> hmFeatureId = eventFeatureBean.getHmFeatureId();
            String sEventId = ParseUtil.checkNull(eventFeatureBean.getEventId());
            if(hmFeatureValue!=null && hmFeatureId!=null)
            {
                String sQuery = "INSERT INTO GTEVENTFEATUREVALUES (FEATUREVALUEID, FK_FEATUREID , FK_EVENTID , FEATUREVALUE) VALUES(?,?,?,?)";
                for(Map.Entry<String,String> mapFeatureValue : hmFeatureValue.entrySet() )
                {
                    String sFeatureName = ParseUtil.checkNull(mapFeatureValue.getKey());

                    String sFeatureId = ParseUtil.checkNull(hmFeatureId.get(sFeatureName));

                    if(sFeatureId!=null && !"".equalsIgnoreCase(sFeatureId))
                    {
                        ArrayList<Object> aParams = DBDAO.createConstraint(Utility.getNewGuid(),sFeatureId,sEventId,ParseUtil.checkNull(mapFeatureValue.getValue()) );

                        iNumOfRows = DBDAO.putRowsQuery(sQuery,aParams,ADMIN_DB,sourceFile,"createEventFeatures()");
                    }
                }
            }
        }
        return iNumOfRows;
    }

    public Integer updateEventFeatures(EventFeatureBean eventFeatureBean)
    {
        Integer iNumOfRows = -1;
        if(eventFeatureBean!=null && eventFeatureBean.getHmFeatureValue()!=null && !eventFeatureBean.getHmFeatureValue().isEmpty()
                && eventFeatureBean.getHmFeatureId()!=null && !eventFeatureBean.getHmFeatureId().isEmpty())
        {
            HashMap<String,String> hmFeatureValue = eventFeatureBean.getHmFeatureValue();
            HashMap<String,String> hmFeatureId = eventFeatureBean.getHmFeatureId();
            String sEventId = ParseUtil.checkNull(eventFeatureBean.getEventId());
            if(hmFeatureValue!=null && hmFeatureId!=null)
            {
                String sQuery = "UPDATE GTEVENTFEATUREVALUES SET FEATUREVALUE = ? WHERE FK_FEATUREID = ? AND FK_EVENTID = ? ";
                for(Map.Entry<String,String> mapFeatureValue : hmFeatureValue.entrySet() )
                {
                    String sFeatureName = ParseUtil.checkNull(mapFeatureValue.getKey());

                    String sFeatureId = ParseUtil.checkNull(hmFeatureId.get(sFeatureName));

                    ArrayList<Object> aParams = DBDAO.createConstraint(ParseUtil.checkNull(mapFeatureValue.getValue()) , sFeatureId,sEventId  );

                    iNumOfRows = DBDAO.putRowsQuery(sQuery,aParams,ADMIN_DB,sourceFile,"updateEventFeatures()");
                }


            }
        }
        return iNumOfRows;
    }
}
