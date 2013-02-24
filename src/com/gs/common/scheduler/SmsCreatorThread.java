package com.gs.common.scheduler;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.sms.SmsCreator;
import com.gs.common.sms.SmsCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/16/13
 * Time: 1:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsCreatorThread implements Runnable {
    private static final Logger schedulerLogging = LoggerFactory.getLogger(Constants.SCHEDULER_LOGS);
    private Configuration smsConfig = Configuration.getInstance(Constants.SMS_PROP);


    @Override
    public void run() {
        try {
            boolean isSmsCreatorEnabled = ParseUtil.sTob(smsConfig.get(Constants.PROP_ENABLE_SMS_CREATOR));
            if(isSmsCreatorEnabled)
            {
                SmsCreator smsCreator = new SmsCreator();
                SmsCreatorService smsCreatorService = new SmsCreatorService( smsCreator );
                smsCreatorService.invokeSmsCreator();
                schedulerLogging.info("Invoking sms creator complete");
            }
            else
            {
                schedulerLogging.info("Sms creator not enabled");
            }
        } catch (Exception e) {
            schedulerLogging.error("Error while invoking the SmsCreatorThread service "
                    + ExceptionHandler.getStackTrace(e));
        }
    }
}
