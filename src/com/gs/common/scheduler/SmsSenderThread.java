package com.gs.common.scheduler;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.sms.SmsSender;
import com.gs.common.sms.SmsSenderService;
import com.gs.common.sms.twilio.SmsTwiml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/22/13
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsSenderThread implements Runnable {
    private static final Logger schedulerLogging = LoggerFactory.getLogger(Constants.SCHEDULER_LOGS);
    private Configuration smsConfig = Configuration.getInstance(Constants.SMS_PROP);


    @Override
    public void run() {
        boolean isSmsSenderEnabled = ParseUtil.sTob(smsConfig.get(Constants.PROP_ENABLE_SMS_SENDER));
        try {
            if(isSmsSenderEnabled)
            {
                SmsSender smsSender = new SmsTwiml();
                SmsSenderService smsSenderService = new SmsSenderService(smsSender);
                smsSenderService.invokeSmsSender();
                schedulerLogging.info("Invoking sms sender complete");
            }
            else
            {
                schedulerLogging.info("Sms sender not enabled");
            }
        }
        catch (Exception e) {
            schedulerLogging.error("Error while invoking the SmsSenderThread service "
                    + ExceptionHandler.getStackTrace(e));
        }
    }
}
