package com.gs.common.sms;

import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.sms.SmsQueueBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/14/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsServiceData {

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    private static final Logger smsLogging = LoggerFactory.getLogger(Constants.SMS_LOGS);
    private String sourceFile = "SmsServiceData.java";

    public SmsTemplateBean getSmsTemplate( Constants.SMS_TEMPLATE smsTemplate ) {
        SmsTemplateBean smsTemplateBean = new SmsTemplateBean();
        if (smsTemplate != null)
        {
            String querySmsTemplate = "SELECT * FROM GTSMSTEMPLATE WHERE SMSTEMPLATENAME = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint(smsTemplate.getSmsTemplate());

            ArrayList<HashMap<String, String>> arrHmEmailTemplate = DBDAO.getDBData(ADMIN_DB, querySmsTemplate, aParams, false, sourceFile , "getSmsTemplate()");
            if (arrHmEmailTemplate != null && !arrHmEmailTemplate.isEmpty()) {
                for (HashMap<String, String> hmEmailTemplate : arrHmEmailTemplate) {
                    smsTemplateBean = new SmsTemplateBean(hmEmailTemplate);
                }
            }
        }
        return smsTemplateBean;
    }

    public SmsTemplateBean getSmsTemplateById( String sTemplateId ) {
        SmsTemplateBean smsTemplateBean = new SmsTemplateBean();
        if (sTemplateId != null && !"".equalsIgnoreCase(sTemplateId))
        {
            String querySmsTemplateById = "SELECT * FROM GTSMSTEMPLATE WHERE SMSTEMPLATEID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint( sTemplateId );

            ArrayList<HashMap<String, String>> arrHmEmailTemplate = DBDAO.getDBData(ADMIN_DB, querySmsTemplateById, aParams,
                    false, sourceFile , "getSmsTemplateById()");

            if (arrHmEmailTemplate != null && !arrHmEmailTemplate.isEmpty()) {
                for (HashMap<String, String> hmEmailTemplate : arrHmEmailTemplate) {
                    smsTemplateBean = new SmsTemplateBean(hmEmailTemplate);
                }
            }
        }
        return smsTemplateBean;
    }

    public Integer createSmsQueue( SmsQueueBean smsQueueBean )
    {
        Integer iNumOfRows = 0;
        if(smsQueueBean!=null && !"".equalsIgnoreCase(smsQueueBean.getSmsQueueId()))
        {
            String sQuery = "INSERT INTO GTSMSQUEUE (SMSQUEUEID, FROM_TELNUMBER, TO_TELNUMBER, FK_SMSTEMPLATEID, SMS_MESSAGE, STATUS, " +
                    " FK_EVENTID, FK_ADMINID, FK_GUESTID, CREATEDATE, HUMANCREATEDATE, MODIFIEDDATE, HUMANMODIFIEDDATE, " +
                    " EVENT_TYPE ) " +
                    " VALUES ( ?,?,?  ,?,?,?  ,?,?,?  ,?,?,?  ,?,?)";
            ArrayList<Object> aParams = DBDAO.createConstraint( smsQueueBean.getSmsQueueId(),smsQueueBean.getFromTelNumber(), smsQueueBean.getToTelNumber(),
                    smsQueueBean.getTemplateId(), smsQueueBean.getSmsMessage(), smsQueueBean.getStatus(),
                    smsQueueBean.getEventId(), smsQueueBean.getAdminId(), smsQueueBean.getGuestId(),
                    smsQueueBean.getCreateDate(), smsQueueBean.getHumanCreateDate(), smsQueueBean.getModifiedDate(),
                    smsQueueBean.getHumanModifiedDate(), smsQueueBean.getEventType() );

            iNumOfRows = DBDAO.putRowsQuery(sQuery, aParams,ADMIN_DB,sourceFile, "createSmsQueue()");
        }
        return iNumOfRows;
    }

    public ArrayList<SmsQueueBean> getOldSmsQueueBean( SmsQueueBean requestSmsQueueBean , Long startTime)
    {
        ArrayList<SmsQueueBean> arrSmsQueuBean = new ArrayList<SmsQueueBean>();
        if( requestSmsQueueBean!=null )
        {
            String sQuery = "SELECT * FROM GTSMSQUEUE WHERE STATUS = ? AND  MODIFIEDDATE < ? ";
            ArrayList<Object> aParams = DBDAO.createConstraint( requestSmsQueueBean.getStatus() , startTime  );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true ,sourceFile, "getOldSmsQueueBean()");
            smsLogging.debug("Old Sms Queue Bean Query : " + sQuery + " aParams :" + aParams + " " + arrResult);
            if(arrResult!=null && !arrResult.isEmpty())
            {
                for( HashMap<String, String> hmResult : arrResult )
                {
                    SmsQueueBean smsQueueBean = new SmsQueueBean( hmResult );
                    arrSmsQueuBean.add( smsQueueBean );
                }
            }
        }
        return arrSmsQueuBean;
    }

    public ArrayList<SmsQueueBean> getSmsQueueBean( SmsQueueBean requestSmsQueueBean , Long startTime , Long endTime)
    {
        ArrayList<SmsQueueBean> arrSmsQueuBean = new ArrayList<SmsQueueBean>();
        if( requestSmsQueueBean!=null )
        {
            String sQuery = "SELECT * FROM GTSMSQUEUE WHERE STATUS = ? AND ( MODIFIEDDATE >= ? AND MODIFIEDDATE <= ? )";
            ArrayList<Object> aParams = DBDAO.createConstraint( requestSmsQueueBean.getStatus() , startTime , endTime );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB, sQuery, aParams, true ,sourceFile, "getSmsQueueBean()");
            smsLogging.debug("New Sms Queue Bean Query : " + sQuery + " aParams :" + aParams + " " + arrResult);
            if(arrResult!=null && !arrResult.isEmpty())
            {
                for( HashMap<String, String> hmResult : arrResult )
                {
                    SmsQueueBean smsQueueBean = new SmsQueueBean( hmResult );
                    arrSmsQueuBean.add( smsQueueBean );
                }
            }
        }
        return arrSmsQueuBean;
    }

    public Integer updateSmsQueueBean( SmsQueueBean requestSmsQueueBean)
    {
        Integer iNumOfRows = 0;
        if( requestSmsQueueBean!=null )
        {
            String sQuery = "UPDATE GTSMSQUEUE SET STATUS = ?, MODIFIEDDATE = ?, HUMANMODIFIEDDATE = ?  WHERE SMSQUEUEID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint( requestSmsQueueBean.getStatus(), requestSmsQueueBean.getModifiedDate(),
                    requestSmsQueueBean.getHumanModifiedDate(), requestSmsQueueBean.getSmsQueueId() );
            iNumOfRows = DBDAO.putRowsQuery( sQuery, aParams,ADMIN_DB,sourceFile, "updateSmsQueueBean()" );

        }
        return iNumOfRows;
    }
}
