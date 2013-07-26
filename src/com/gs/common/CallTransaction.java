package com.gs.common;

import com.gs.bean.CallTransactionBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/26/13
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class CallTransaction {
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String SELECTED_CALL_SERVICE = applicationConfig.get(Constants.PROP_CALL_SERVICE);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);
    private String sourceFile = "CallTransaction.java";
    Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    private static String QUERY_INSERT_TRANSACTION_CALLS = "INSERT INTO GTCALLTRANSACTION ( CALLTRANSACTIONID ,TELCOM_SERVICE_ACC_SID  ," +
            " TELCOM_SERVICE_CALL_SID  ,TELCOM_SERVICE_BILL_DURATION  , TELCOM_SERVICE_ACTUAL_DURATION  , GUEST_TELNUMBER  , " +
            " TO_TELNUMBER, CALL_TYPE  , SECRET_EVENT_NUMBER , SECRET_EVENT_KEY  , FK_GUESTID , FK_EVENTID  , FK_ADMINID ," +
            " CALL_CREATEDATE  , HUMAN_CALL_CREATEDATE  , CALL_ENDDATE  , HUMAN_CALL_ENDDATE , CALL_STATUS ) " +
            " VALUES(?,?,? ,?,?,? ,?,?,? ,?,?,? ,?,?,? ,?,?,?)";

    private static String QUERY_UPDATE_TRANSACTION_CALLS = "UPDATE GTCALLTRANSACTION SET " +
            " TELCOM_SERVICE_BILL_DURATION = ? , TELCOM_SERVICE_ACTUAL_DURATION  = ? , GUEST_TELNUMBER  = ? , " +
            " TO_TELNUMBER  = ? , CALL_TYPE  = ? , SECRET_EVENT_NUMBER  = ? , SECRET_EVENT_KEY  = ? , FK_GUESTID  = ? ," +
            " FK_EVENTID  = ? , FK_ADMINID  = ? , CALL_CREATEDATE  = ? , HUMAN_CALL_CREATEDATE  = ? , CALL_ENDDATE  = ? ," +
            " HUMAN_CALL_ENDDATE  = ? , CALL_STATUS  = ? ) " +
            "  WHERE TELCOM_SERVICE_ACC_SID = ? AND TELCOM_SERVICE_CALL_SID = ? ";

    private static CallTransaction callTransaction = null;
    private CallTransaction()
    {

    }

    public static CallTransaction getInstance()
    {
        if( callTransaction == null )
        {
            callTransaction = new CallTransaction();
        }
        return  callTransaction;
    }

    public void createTransaction(IncomingCallBean incomingCallBean)
    {
        if (incomingCallBean != null) {
            if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(SELECTED_CALL_SERVICE))
            {
                TwilioIncomingCallBean twilioIncominCallBean = (TwilioIncomingCallBean) incomingCallBean;
                createTwilioTransaction(twilioIncominCallBean);
            }
        }

    }

    public void updateTransaction(IncomingCallBean incomingCallBean, CallTransactionBean callTransactionBean)
    {
        if (incomingCallBean != null) {
            if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(SELECTED_CALL_SERVICE))
            {
                TwilioIncomingCallBean twilioIncominCallBean = (TwilioIncomingCallBean) incomingCallBean;
                updateTwilioTransaction(twilioIncominCallBean, callTransactionBean);
            }
        }

    }

    private Integer updateTwilioTransaction(TwilioIncomingCallBean twilioIncominCallBean, CallTransactionBean callTransactionBean)
    {
        Integer iNumberOfRows = -1;
        if(twilioIncominCallBean!=null)
        {
            String sQueryParameters = "UPDATE GTCALLTRANSACTION SET ";
            ArrayList<Object> aParams = new ArrayList<Object>();
            if(twilioIncominCallBean!=null)
            {
                if(twilioIncominCallBean.getBillDuration()!=null && !"".equalsIgnoreCase(twilioIncominCallBean.getBillDuration()))
                {
                    sQueryParameters = sQueryParameters + " TELCOM_SERVICE_BILL_DURATION = ?, ";
                    aParams.add(twilioIncominCallBean.getBillDuration());
                }
                if(twilioIncominCallBean.getActualCallDuration()!=null && !"".equalsIgnoreCase(twilioIncominCallBean.getActualCallDuration()))
                {
                    sQueryParameters = sQueryParameters + " TELCOM_SERVICE_ACTUAL_DURATION = ?, ";
                    aParams.add(twilioIncominCallBean.getActualCallDuration());
                }
                if(twilioIncominCallBean.getCallerInputEventId()!=null && !"".equalsIgnoreCase(twilioIncominCallBean.getCallerInputEventId()))
                {
                    sQueryParameters = sQueryParameters + " SECRET_EVENT_NUMBER = ?, ";
                    aParams.add(twilioIncominCallBean.getCallerInputEventId());
                }
                if(twilioIncominCallBean.getCallerInputSecretKey()!=null && !"".equalsIgnoreCase(twilioIncominCallBean.getCallerInputSecretKey()))
                {
                    sQueryParameters = sQueryParameters + " SECRET_EVENT_KEY = ?, ";
                    aParams.add(twilioIncominCallBean.getCallerInputSecretKey());
                }
                if(twilioIncominCallBean.getCallStatus()!=null &&  !"".equalsIgnoreCase(twilioIncominCallBean.getCallStatus()))
                {
                    sQueryParameters = sQueryParameters + " CALL_STATUS = ?,";
                    aParams.add(twilioIncominCallBean.getCallStatus());
                }
                if(twilioIncominCallBean.getCallStatus()!=null &&  "completed".equalsIgnoreCase(twilioIncominCallBean.getCallStatus()))
                {
                    sQueryParameters = sQueryParameters + " CALL_ENDDATE = ?, HUMAN_CALL_ENDDATE = ?, ";
                    Long currentTime = DateSupport.getEpochMillis();
                    String sUTCDateTime = DateSupport.getUTCDateTime();

                    aParams.add(currentTime);
                    aParams.add(sUTCDateTime);
                }
            }
            if(callTransactionBean!=null)
            {
                if(callTransactionBean.getGuestId()!=null && !"".equalsIgnoreCase(callTransactionBean.getGuestId()))
                {
                    sQueryParameters = sQueryParameters + " FK_GUESTID = ?,  ";
                    aParams.add(callTransactionBean.getGuestId());
                }
                if(callTransactionBean.getEventId()!=null && !"".equalsIgnoreCase(callTransactionBean.getEventId()))
                {
                    sQueryParameters = sQueryParameters + " FK_EVENTID = ?, ";
                    aParams.add(callTransactionBean.getEventId());
                }
                if(callTransactionBean.getAdminId()!=null && !"".equalsIgnoreCase(callTransactionBean.getAdminId()))
                {
                    sQueryParameters = sQueryParameters + " FK_ADMINID = ?, ";
                    aParams.add(callTransactionBean.getAdminId());
                }
                if(callTransactionBean.getCallPlanType()!=null && !"".equalsIgnoreCase(callTransactionBean.getCallPlanType()))
                {
                    sQueryParameters = sQueryParameters + " CALL_TYPE = ?, ";
                    aParams.add(callTransactionBean.getCallPlanType());
                }
            }

            sQueryParameters = sQueryParameters +  " TELCOM_SERVICE_CALL_SID = ? ";
            aParams.add(twilioIncominCallBean.getCallid());

                    sQueryParameters = sQueryParameters +" WHERE TELCOM_SERVICE_ACC_SID = ? and TELCOM_SERVICE_CALL_SID = ? ";
            aParams.add(twilioIncominCallBean.getAccountid() );
            aParams.add(twilioIncominCallBean.getCallid() );
            appLogging.info("Update TWilio Transaction Query  : " + sQueryParameters + " aParams : " + aParams );
            iNumberOfRows = DBDAO.putRowsQuery(sQueryParameters,aParams,ADMIN_DB,sourceFile,"updateTwilioTransaction()" );
        }
        return iNumberOfRows;
    }

    private Integer createTwilioTransaction(TwilioIncomingCallBean twilioIncominCallBean)
    {
        Integer iNumberOfRows = -1;
        if(twilioIncominCallBean!=null)
        {
            Long currentTime = DateSupport.getEpochMillis();
            String sUTCDateTime = DateSupport.getUTCDateTime();
            ArrayList<Object> aParams = DBDAO.createConstraint(Utility.getNewGuid(),twilioIncominCallBean.getAccountid(), twilioIncominCallBean.getCallid(),
                    twilioIncominCallBean.getBillDuration(),twilioIncominCallBean.getActualCallDuration(),
                    twilioIncominCallBean.getFrom(), twilioIncominCallBean.getTo(),
                    twilioIncominCallBean.getToNumberType(),
                    ParseUtil.checkNull(twilioIncominCallBean.getCallerInputEventId()), ParseUtil.checkNull(twilioIncominCallBean.getCallerInputSecretKey()),
                    "","","",currentTime,sUTCDateTime,-1,"",ParseUtil.checkNull(twilioIncominCallBean.getCallStatus()));

            iNumberOfRows = DBDAO.putRowsQuery(QUERY_INSERT_TRANSACTION_CALLS,aParams,ADMIN_DB,sourceFile,"createTwilioTransaction()" );
        }
        return iNumberOfRows;
    }

}
