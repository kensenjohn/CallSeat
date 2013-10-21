package com.gs.common.sms.twilio;

import com.gs.bean.AdminTelephonyAccountBean;
import com.gs.bean.TelNumberBean;
import com.gs.bean.sms.SmsQueueBean;
import com.gs.bean.sms.SmsTransactionBean;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ParseUtil;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.Utility;
import com.gs.common.sms.SmsSender;
import com.gs.common.sms.SmsServiceData;
import com.gs.common.sms.SmsTransaction;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import com.gs.phone.account.AdminTelephonyAccountManager;
import com.gs.phone.account.AdminTelephonyAccountMeta;
import com.gs.phone.account.TwilioAccount;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/23/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsTwiml  implements SmsSender {
    Logger smsLogging = LoggerFactory.getLogger(Constants.SMS_LOGS);
    @Override
    public Integer send(SmsQueueBean smsQueueBean) {
        Integer iNumOfRows = 0;
        if(smsQueueBean!=null)
        {
            try {
                // To send SMS we need the Account SID and Auth Token of the number(in our case a Sub account)

                SmsAccountDetails smsAccountDetail = getAccountDetails( smsQueueBean );
                smsLogging.info("Sms Account telnum type : " + smsAccountDetail.getTelnumType() );
                if(smsAccountDetail!=null && !"".equalsIgnoreCase(smsAccountDetail.getAccountSid()) && !"".equalsIgnoreCase(smsAccountDetail.getAuthToken()))
                {
                    TwilioRestClient client = new TwilioRestClient(  smsAccountDetail.getAccountSid(), smsAccountDetail.getAuthToken() );

                    // Build a filter for the SmsList
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Body", smsQueueBean.getSmsMessage());
                    params.put("To", "+"+smsQueueBean.getToTelNumber());
                    params.put("From", "+"+smsQueueBean.getFromTelNumber());

                    smsLogging.info("Sms Body : " + params.get("Body") + "\nFrom : " + params.get("From") + "\nTo : " + params.get("To")  );

                    SmsFactory messageFactory = client.getAccount().getSmsFactory();
                    Sms message = messageFactory.create(params);

                    if(message!=null && !"".equalsIgnoreCase(message.getSid()))
                    {
                        iNumOfRows = 1; // Success sending SMS
                        SmsTransactionBean smsTransactionBean = getTmpSmsTransactionBean( smsQueueBean ,
                                message.getSid(), Constants.SCHEDULER_STATUS.COMPLETE.getSchedulerStatus() );
                        SmsTransaction smsTransaction = SmsTransaction.getInstance();
                        smsTransaction.createTransaction( smsTransactionBean );
                    }
                }  else {
                    smsLogging.error("Could not find a valid Sms Account  : " + ParseUtil.checkNullObject(smsQueueBean));
                }
            } catch (TwilioRestException e) {
                smsLogging.error("Sms send failure\n" + ExceptionHandler.getStackTrace(e));
            }
        }
        return iNumOfRows;
    }

    @Override
    public void update(SmsQueueBean smsQueueBean) {
        if(smsQueueBean!=null && smsQueueBean.getSmsQueueId()!=null && !"".equalsIgnoreCase(smsQueueBean.getSmsQueueId()))
        {
            SmsServiceData smsServiceData = new SmsServiceData();
            smsServiceData.updateSmsQueueBean(smsQueueBean);
        }
    }

    private SmsTransactionBean getTmpSmsTransactionBean ( SmsQueueBean smsQueueBean , String smsSid , String status)
    {
        SmsTransactionBean smsTransactionBean = new SmsTransactionBean();

        smsTransactionBean.setSmsTransactionId( Utility.getNewGuid() );
        smsTransactionBean.setAdminId(smsQueueBean.getAdminId());
        smsTransactionBean.setCreateDate(DateSupport.getEpochMillis());
        smsTransactionBean.setEventId(smsQueueBean.getEventId());
        smsTransactionBean.setFromNumber(smsQueueBean.getFromTelNumber());
        smsTransactionBean.setToNumber(smsQueueBean.getToTelNumber());
        smsTransactionBean.setHumanCreateDate( DateSupport.getUTCDateTime() );
        smsTransactionBean.setGuestId( smsQueueBean.getGuestId() );
        smsTransactionBean.setSmsMessage( smsQueueBean.getSmsMessage() );

        SmsAccountDetails smsAccountDetail = getAccountDetails( smsQueueBean );

        if( smsAccountDetail!=null)
        {
            smsTransactionBean.setEventType( smsAccountDetail.getTelnumType() );
            smsTransactionBean.setTelecomServiceAccSID( smsAccountDetail.getAccountSid() );
        }

        smsTransactionBean.setTelecomServiceSmsSID( smsSid );
        smsTransactionBean.setSmsStatus(status);

        return smsTransactionBean;
    }

    private  SmsAccountDetails getAccountDetails(  SmsQueueBean smsQueueBean  )
    {

        SmsAccountDetails smsAccountDetail = new SmsAccountDetails();
        if(smsQueueBean!=null)
        {
            TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
            telNumberMetaData.setEventId(smsQueueBean.getEventId());
            telNumberMetaData.setAdminId(smsQueueBean.getAdminId());
            TelNumberManager telNumManager = new TelNumberManager();
            ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumbersByEvent(telNumberMetaData);

            if(arrTelNumberBean!=null && !arrTelNumberBean.isEmpty())
            {
                for( TelNumberBean telNumberBean : arrTelNumberBean )
                {
                    if( telNumberBean.getTelNumber().equalsIgnoreCase( smsQueueBean.getFromTelNumber() ))
                    {
                        //smsTransactionBean.setEventType( telNumberBean.getTelNumberType() );
                        //break ;

                        smsAccountDetail.setTelnumType( telNumberBean.getTelNumberType() );

                        if (Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask().equalsIgnoreCase( telNumberBean.getTelNumberType() ))
                        {

                            AdminTelephonyAccountMeta adminAccountMeta = new AdminTelephonyAccountMeta();
                            adminAccountMeta.setAdminId( smsQueueBean.getAdminId() );

                            AdminTelephonyAccountManager adminTelephonyAccountManager = new AdminTelephonyAccountManager() ;
                            ArrayList<AdminTelephonyAccountBean> arrAdminTelAccount = adminTelephonyAccountManager.getAdminTelephonyAccount(adminAccountMeta);

                            if(arrAdminTelAccount!=null && !arrAdminTelAccount.isEmpty())
                            {
                                for(AdminTelephonyAccountBean adminTelephonyAccountBean : arrAdminTelAccount )
                                {
                                    smsAccountDetail.setAccountSid( adminTelephonyAccountBean.getAccountSid() );
                                    smsAccountDetail.setAuthToken( adminTelephonyAccountBean.getAuthToken() );
                                    break;
                                }
                            }

                        }  else if (Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask().equalsIgnoreCase( telNumberBean.getTelNumberType()  )) {

                            smsAccountDetail.setAccountSid( TwilioAccount.getAccountSid() );
                            smsAccountDetail.setAuthToken( TwilioAccount.getAccountToken() );
                        }
                    } else {
                        smsLogging.info("The From Number does not match the number for the event in Data base : " + ParseUtil.checkNull(telNumberBean.getTelNumber()) + " - " + ParseUtil.checkNull(smsQueueBean.getFromTelNumber()));
                    }
                }
            }
        }
        return smsAccountDetail;
    }


    public class SmsAccountDetails
    {
        private String accountSid = "";
        private String authToken = "";
        private String telnumType = "";

        public String getAccountSid() {
            return accountSid;
        }

        public void setAccountSid(String accountSid) {
            this.accountSid = accountSid;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getTelnumType() {
            return telnumType;
        }

        public void setTelnumType(String telnumType) {
            this.telnumType = telnumType;
        }
    }

}
