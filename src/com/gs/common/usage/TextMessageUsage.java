package com.gs.common.usage;

import com.gs.bean.EventFeatureBean;
import com.gs.bean.usage.TextMessageUsageBean;
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
 * Time: 5:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextMessageUsage  implements Usage {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    private String sourceFile = "TextMessageUsage.java";

    @Override
    public TextMessageUsageBean getUsage(UsageMetaData usageMetaData) {
        TextMessageUsageBean textMessageUsageBean = new TextMessageUsageBean();

        // Total Text Messages Allocated
        textMessageUsageBean.setNumOfDemoTextAllocated( getTotalTextMessagesAllocated( usageMetaData , Constants.EVENT_FEATURES.DEMO_TOTAL_TEXT_MESSAGES ) );
        textMessageUsageBean.setNumOfPremiumTextAllocated(  getTotalTextMessagesAllocated( usageMetaData , Constants.EVENT_FEATURES.PREMIUM_TOTAL_TEXT_MESSAGES )  );

        //Total Text Messages Sent
        textMessageUsageBean.setNumOfDemoTextSent(  getTextMessagesSent(usageMetaData, USAGE_CALL_TYPE.DEMO ) );
        textMessageUsageBean.setNumOfPremiumTextSent( getTextMessagesSent(usageMetaData, USAGE_CALL_TYPE.PREMIUM ) );

        //Total TextMessages Remaining
        textMessageUsageBean.setNumOfDemoTextRemaining( textMessageUsageBean.getNumOfDemoTextAllocated() - textMessageUsageBean.getNumOfDemoTextSent() );
        textMessageUsageBean.setNumOfPremiumTextRemaining( textMessageUsageBean.getNumOfPremiumTextAllocated() - textMessageUsageBean.getNumOfPremiumTextSent() );


        return textMessageUsageBean;
    }

    @Override
    public UsageBean getDemoUsage(UsageMetaData usageMetaData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UsageBean getPremiumUsage(UsageMetaData usageMetaData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Integer getTotalTextMessagesAllocated(UsageMetaData usageMetaData,  Constants.EVENT_FEATURES eventFeatures)
    {
        Integer iTotalTextMessagesAllocated = 0 ;
        if( usageMetaData!=null && eventFeatures!=null )
        {
            iTotalTextMessagesAllocated = EventFeatureManager.getIntegerValueFromEventFeature( usageMetaData.getEventId() , eventFeatures);
        }
        return iTotalTextMessagesAllocated;
    }

    private Integer getDemoTextMessagesSent(UsageMetaData usageMetaData)
    {
        Integer iDemoMinutesUsed = 0;

        if(usageMetaData!=null && usageMetaData.getEventId()!=null && !"".equalsIgnoreCase(usageMetaData.getEventId()))
        {
            String sQuery = "SELECT * from GTSMSTRANSACTION GTST, GTTELNUMBERTYPE GTTNT,  GTTELNUMBERS GTT " +
                    " WHERE GTST.FK_EVENTID=? and GTST.SMS_STATUS='COMPLETE'   AND GTST.FK_EVENTID=GTT.FK_EVENTID  and "  +
                    " GTT.TELNUMBER=GTST.FROM_TELNUMBER AND  GTT.FK_TELNUMBERTYPEID=GTTNT.TELNUMBERTYPEID   AND " +
                    " GTTNT.TELNUMTYPE IN ('"+Constants.EVENT_TASK.DEMO_SEATING.getTask()+"','"+Constants.EVENT_TASK.DEMO_RSVP.getTask()+"') " +
                    " AND GTTNT.TELNUMTYPE = GTST.EVENT_TYPE";

            ArrayList<Object> aParams = DBDAO.createConstraint(usageMetaData.getEventId());

            iDemoMinutesUsed = getTextMessagesSentData(sQuery , aParams , "getDemoTextMessagesSent()" );
        }

        return iDemoMinutesUsed;
    }

    private Integer getPremiumTextMessagesSent(UsageMetaData usageMetaData)
    {
        Integer iDemoMinutesUsed = 0;
        if(usageMetaData!=null && usageMetaData.getEventId()!=null && !"".equalsIgnoreCase(usageMetaData.getEventId()))
        {
            String sQuery = "SELECT * from GTSMSTRANSACTION GTST, GTTELNUMBERTYPE GTTNT,  GTTELNUMBERS GTT " +
                    " WHERE GTST.FK_EVENTID=? and GTST.SMS_STATUS='COMPLETE'   AND GTST.FK_EVENTID=GTT.FK_EVENTID  and "  +
                    " GTT.TELNUMBER=GTST.FROM_TELNUMBER AND  GTT.FK_TELNUMBERTYPEID=GTTNT.TELNUMBERTYPEID   AND " +
                    " GTTNT.TELNUMTYPE IN ('"+Constants.EVENT_TASK.SEATING.getTask()+"','"+Constants.EVENT_TASK.RSVP.getTask()+"') " +
                    " AND GTTNT.TELNUMTYPE = GTST.EVENT_TYPE";


            ArrayList<Object> aParams = DBDAO.createConstraint(usageMetaData.getEventId());

            iDemoMinutesUsed = getTextMessagesSentData(sQuery , aParams , "getPremiumTextMessagesSent()" );
        }
        return iDemoMinutesUsed;
    }

    private Integer getTextMessagesSent(UsageMetaData usageMetaData,  USAGE_CALL_TYPE usageCallType )
    {
        Integer iTextMessagesSent = 0;
        if(usageMetaData!=null && usageMetaData.getEventId()!=null && !"".equalsIgnoreCase(usageMetaData.getEventId()))
        {
            if(usageCallType!=null && usageCallType.getUsageCallType().equalsIgnoreCase(USAGE_CALL_TYPE.DEMO.getUsageCallType()))
            {
                iTextMessagesSent = EventFeatureManager.getIntegerValueFromEventFeature( usageMetaData.getEventId() , Constants.EVENT_FEATURES.DEMO_FINAL_TEXT_MESSAGES_SENT );
                if(iTextMessagesSent<=0)
                {
                    iTextMessagesSent = getDemoTextMessagesSent(usageMetaData);
                }
            }
            else if(usageCallType!=null && usageCallType.getUsageCallType().equalsIgnoreCase(USAGE_CALL_TYPE.PREMIUM.getUsageCallType()))
            {
                iTextMessagesSent = EventFeatureManager.getIntegerValueFromEventFeature( usageMetaData.getEventId() , Constants.EVENT_FEATURES.PREMIUM_FINAL_TEXT_MESSAGES_SENT );
                if(iTextMessagesSent<=0)
                {
                    iTextMessagesSent = getPremiumTextMessagesSent(usageMetaData);
                }
            }
        }
        return iTextMessagesSent;

    }

    private Integer getTextMessagesSentData(String sQuery, ArrayList<Object> aParams, String invokingMethodName)
    {
        Integer iBillingTextMessagesSent = 0;
        if(sQuery!=null && !"".equalsIgnoreCase(sQuery) && aParams!=null && invokingMethodName!=null && !"".equalsIgnoreCase(invokingMethodName))
        {
            ArrayList<HashMap<String, String>> arrTextMessageResult = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true, sourceFile, invokingMethodName);

            if(arrTextMessageResult!=null && !arrTextMessageResult.isEmpty())
            {
                iBillingTextMessagesSent = arrTextMessageResult.size();
            }
        }
        else
        {
            appLogging.info("Invalid parameters use to access call minutes used : " + ParseUtil.checkNull(sQuery) + " params : " +
                    ParseUtil.checkNullObject(aParams) + " Invoking method : " + ParseUtil.checkNull(invokingMethodName));
        }
        return iBillingTextMessagesSent;
    }
}
