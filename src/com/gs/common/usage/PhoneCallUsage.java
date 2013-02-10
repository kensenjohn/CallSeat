package com.gs.common.usage;

import com.gs.bean.EventFeatureBean;
import com.gs.bean.usage.PhoneCallUsageBean;
import com.gs.bean.usage.UsageBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;
import com.gs.manager.event.EventFeatureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/8/13
 * Time: 5:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class PhoneCallUsage implements Usage {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    private String sourceFile = "PhoneCallUsage.java";

    @Override
    public PhoneCallUsageBean getUsage(UsageMetaData usageMetaData) {
        PhoneCallUsageBean phoneCallUsageBean = new PhoneCallUsageBean();

        // Total Minutes Allocated
        phoneCallUsageBean.setNumOfDemoMinutesAllocated( getTotalMinutesAllocated(usageMetaData,Constants.EVENT_FEATURES.DEMO_TOTAL_CALL_MINUTES) );
        phoneCallUsageBean.setNumOfPremiumMinutesAllocated( getTotalMinutesAllocated(usageMetaData,Constants.EVENT_FEATURES.PREMIUM_TOTAL_CALL_MINUTES) );

        // Minutes Used
        phoneCallUsageBean.setNumOfDemoMinutesUsed( getMinutesUsed(usageMetaData, USAGE_CALL_TYPE.DEMO ) );
        phoneCallUsageBean.setNumOfPremiumMinutesUsed( getMinutesUsed(usageMetaData, USAGE_CALL_TYPE.PREMIUM ) );

        // Minutes Remaining
        phoneCallUsageBean.setNumOfDemoMinutesRemaining( (phoneCallUsageBean.getNumOfDemoMinutesAllocated() - phoneCallUsageBean.getNumOfDemoMinutesUsed()) );
        phoneCallUsageBean.setNumOfPremiumMinutesRemaining( (phoneCallUsageBean.getNumOfPremiumMinutesAllocated() - phoneCallUsageBean.getNumOfPremiumMinutesUsed()) );

        return phoneCallUsageBean;
    }

    @Override
    public UsageBean getDemoUsage(UsageMetaData usageMetaData) {
        PhoneCallUsageBean phoneCallUsageBean = new PhoneCallUsageBean();
        phoneCallUsageBean.setNumOfDemoMinutesAllocated( getTotalMinutesAllocated(usageMetaData,Constants.EVENT_FEATURES.DEMO_TOTAL_CALL_MINUTES) );
        return phoneCallUsageBean;
    }

    @Override
    public UsageBean getPremiumUsage(UsageMetaData usageMetaData) {
        PhoneCallUsageBean phoneCallUsageBean = new PhoneCallUsageBean();
        phoneCallUsageBean.setNumOfPremiumMinutesAllocated( getTotalMinutesAllocated(usageMetaData,Constants.EVENT_FEATURES.PREMIUM_TOTAL_CALL_MINUTES) );
        return phoneCallUsageBean;
    }

    private Integer getTotalMinutesAllocated(UsageMetaData usageMetaData,  Constants.EVENT_FEATURES eventFeatures)
    {
        Integer iTotalMinutesAllocated = 0;
        if(usageMetaData!=null)
        {
            EventFeatureManager eventFeatureManager = new EventFeatureManager();
            EventFeatureBean eventFeatureBean = eventFeatureManager.getEventFeatures(usageMetaData.getEventId(),eventFeatures);
            if(eventFeatureBean!=null)
            {
                HashMap<String,String> hmFeatureValue = eventFeatureBean.getHmFeatureValue();
                if(hmFeatureValue!=null && !hmFeatureValue.isEmpty())
                {
                    for(Map.Entry<String,String> mapFeatureValue : hmFeatureValue.entrySet())
                    {
                        if( eventFeatures.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey()))
                        {
                            iTotalMinutesAllocated = ParseUtil.sToI(mapFeatureValue.getValue());
                        }
                    }
                }
            }

        }
        return iTotalMinutesAllocated;
    }

    private Integer getMinutesUsed(UsageMetaData usageMetaData,  USAGE_CALL_TYPE usageCallType )
    {
        Integer iMinutesUsed = 0;
        if(usageMetaData!=null && usageMetaData.getEventId()!=null && !"".equalsIgnoreCase(usageMetaData.getEventId()))
        {
            if(usageCallType!=null && usageCallType.getUsageCallType().equalsIgnoreCase(USAGE_CALL_TYPE.DEMO.getUsageCallType()))
            {
                iMinutesUsed = EventFeatureManager.getIntegerValueFromEventFeature( usageMetaData.getEventId() , Constants.EVENT_FEATURES.DEMO_FINAL_CALL_MINUTES_USED );
                if(iMinutesUsed<=0)
                {
                    iMinutesUsed = getDemoMinutesUsed(usageMetaData);
                }
            }
            else if(usageCallType!=null && usageCallType.getUsageCallType().equalsIgnoreCase(USAGE_CALL_TYPE.PREMIUM.getUsageCallType()))
            {
                iMinutesUsed = EventFeatureManager.getIntegerValueFromEventFeature( usageMetaData.getEventId() , Constants.EVENT_FEATURES.PREMIUM_FINAL_CALL_MINUTES_USED );
                if(iMinutesUsed<=0)
                {
                    iMinutesUsed = getPremiumMinutesUsed(usageMetaData);
                }
            }
        }
        return iMinutesUsed;

    }

    private Integer getDemoMinutesUsed(UsageMetaData usageMetaData)
    {
        Integer iDemoMinutesUsed = 0;

        if(usageMetaData!=null && usageMetaData.getEventId()!=null && !"".equalsIgnoreCase(usageMetaData.getEventId()))
        {
            String sQuery = "select * from GTCALLTRANSACTION GTCT , GTTELNUMBERTYPE GTTNT, GTTELNUMBERS GTT WHERE " +
                    " GTCT.FK_EVENTID=? and GTCT.CALL_STATUS='completed' and GTCT.SECRET_EVENT_NUMBER = GTT.SECRET_EVENT_NUMBER " +
                    " AND GTCT.FK_EVENTID=GTT.FK_EVENTID and GTT.TELNUMBER=GTCT.TO_TELNUMBER AND GTT.FK_TELNUMBERTYPEID=GTTNT.TELNUMBERTYPEID " +
                    " AND GTTNT.TELNUMTYPE IN ('"+Constants.EVENT_TASK.DEMO_SEATING.getTask()+"','"+Constants.EVENT_TASK.DEMO_RSVP.getTask()+"')";

            ArrayList<Object> aParams = DBDAO.createConstraint(usageMetaData.getEventId());

            iDemoMinutesUsed = getMinutesUsedData(sQuery , aParams , "getDemoMinutesUsed()" );
        }

        return iDemoMinutesUsed;
    }

    private Integer getPremiumMinutesUsed(UsageMetaData usageMetaData)
    {
        Integer iDemoMinutesUsed = 0;
        if(usageMetaData!=null && usageMetaData.getEventId()!=null && !"".equalsIgnoreCase(usageMetaData.getEventId()))
        {
            String sQuery = "select * from GTCALLTRANSACTION GTCT , GTTELNUMBERTYPE GTTNT, GTTELNUMBERS GTT WHERE " +
                    " GTCT.FK_EVENTID=? and GTCT.CALL_STATUS='completed' and (GTCT.SECRET_EVENT_NUMBER IS NULL OR GTCT.SECRET_EVENT_NUMBER='') " +
                    " AND GTCT.FK_EVENTID=GTT.FK_EVENTID and GTT.TELNUMBER=GTCT.TO_TELNUMBER AND GTT.FK_TELNUMBERTYPEID=GTTNT.TELNUMBERTYPEID " +
                    " AND GTTNT.TELNUMTYPE IN ('"+Constants.EVENT_TASK.SEATING.getTask()+"','"+Constants.EVENT_TASK.RSVP.getTask()+"')";

            ArrayList<Object> aParams = DBDAO.createConstraint(usageMetaData.getEventId());

            iDemoMinutesUsed = getMinutesUsedData(sQuery , aParams , "getPremiumMinutesUsed()" );
        }
        return iDemoMinutesUsed;
    }

    private Integer getMinutesUsedData(String sQuery, ArrayList<Object> aParams, String invokingMethodName)
    {
        Integer iBillingMinutesUsed = 0;
        if(sQuery!=null && !"".equalsIgnoreCase(sQuery) && aParams!=null && invokingMethodName!=null && !"".equalsIgnoreCase(invokingMethodName))
        {
            ArrayList<HashMap<String, String>> arrDemoMinsResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,invokingMethodName );

            if(arrDemoMinsResult!=null && !arrDemoMinsResult.isEmpty())
            {
                for(HashMap<String,String> hmDemoMinsResult : arrDemoMinsResult )
                {
                    Integer iTmpTelecomBillDuration = ParseUtil.sToI(hmDemoMinsResult.get("TELCOM_SERVICE_BILL_DURATION"));

                    iBillingMinutesUsed = iBillingMinutesUsed + iTmpTelecomBillDuration;
                }
            }
        }
        else
        {
            appLogging.info("Invalid parameters use to access call minutes used : " + ParseUtil.checkNull(sQuery) + " params : " +
                    ParseUtil.checkNullObject(aParams) + " Invoking method : " + ParseUtil.checkNull(invokingMethodName));
        }
        return iBillingMinutesUsed;
    }

    private Integer getDemoRSVPMinutesUsed()
    {
        Integer iDemoMinutesUsed = 0;
        return iDemoMinutesUsed;
    }

    private Integer getDemoSeatingMinutesUsed()
    {
        Integer iDemoMinutesUsed = 0;
        return iDemoMinutesUsed;
    }

    private Integer getPremiumRSVPMinutesUsed()
    {
        Integer iDemoMinutesUsed = 0;
        return iDemoMinutesUsed;
    }

    private Integer getPremiumSeatingMinutesUsed()
    {
        Integer iDemoMinutesUsed = 0;
        return iDemoMinutesUsed;
    }


    private Integer getMinutesRemaining(UsageMetaData usageMetaData,  USAGE_CALL_TYPE usageCallType )
    {
        Integer iMinutesRemaining = 0;
        return iMinutesRemaining;
    }
}
