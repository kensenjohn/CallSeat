package com.gs.common.scheduler;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.mail.EmailCreator;
import com.gs.common.mail.EmailCreatorService;
import com.gs.common.mail.MailCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/26/13
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailCreatorThread implements Runnable {
    private static final Logger schedulerLogging = LoggerFactory.getLogger(Constants.SCHEDULER_LOGS);
    private Configuration emailConfig = Configuration.getInstance(Constants.EMAILER_PROP);
    @Override
    public void run() {
        try {
            boolean isEmailCreatorEnabled = ParseUtil.sTob(emailConfig.get(Constants.PROP_ENABLE_EMAIL_CREATOR));
            if(isEmailCreatorEnabled)
            {
                MailCreator emailCreator = new EmailCreator();
                EmailCreatorService emailCreatorService = new EmailCreatorService( emailCreator );
                emailCreatorService.invokeEmailCreator();
                schedulerLogging.info("Invoking email creator complete");
            }
            else
            {
                schedulerLogging.info("Email creator not enabled");
            }
        } catch (Exception e) {
            schedulerLogging.error("Error while invoking the SmsCreatorThread service "
                    + ExceptionHandler.getStackTrace(e));
        }
    }
}
