package com.gs.common.sms.twilio;

import com.gs.bean.AdminTelephonyAccountBean;
import com.gs.bean.sms.SmsQueueBean;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.sms.SmsSender;
import com.gs.common.sms.SmsServiceData;
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
                AdminTelephonyAccountMeta adminAccountMeta = new AdminTelephonyAccountMeta();
                adminAccountMeta.setAdminId( smsQueueBean.getAdminId() );

                AdminTelephonyAccountManager adminTelephonyAccountManager = new AdminTelephonyAccountManager() ;
                ArrayList<AdminTelephonyAccountBean> arrAdminTelAccount = adminTelephonyAccountManager.getAdminTelephonyAccount(adminAccountMeta);

                if(arrAdminTelAccount!=null && !arrAdminTelAccount.isEmpty())
                {
                    for(AdminTelephonyAccountBean adminTelephonyAccountBean : arrAdminTelAccount )
                    {
                        TwilioRestClient client = new TwilioRestClient(  adminTelephonyAccountBean.getAccountSid(), adminTelephonyAccountBean.getAuthToken() );

                        // Build a filter for the SmsList
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Body", smsQueueBean.getSmsMessage());
                        params.put("To", "+"+smsQueueBean.getToTelNumber());
                        params.put("From", "+"+smsQueueBean.getFromTelNumber());

                        smsLogging.info("Sms Body : " + params.get("Body") + "\nFrom : " + params.get("From") + "\nTo : " + params.get("To")  );

                        SmsFactory messageFactory = client.getAccount().getSmsFactory();
                        Sms message = messageFactory.create(params);
                        smsLogging.info("SMS Message SID : " + message.getBody() );
                        iNumOfRows = 1;

                        break;
                    }
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
}
