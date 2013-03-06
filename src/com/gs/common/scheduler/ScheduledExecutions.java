package com.gs.common.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;

public class ScheduledExecutions implements ServletContextListener {

	Configuration processSchedulerConfig = Configuration
			.getInstance(Constants.PROCESS_SCHEDULER_PROP);
	private ScheduledExecutorService mailScheduler;
    private ScheduledExecutorService smsCreatorScheduler;
    private ScheduledExecutorService smsSenderScheduler;
    private ScheduledExecutorService mailCreator;

	private static final Logger schedulerLogging = LoggerFactory
			.getLogger(Constants.SCHEDULER_LOGS);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
        smsCreatorScheduler = Executors.newSingleThreadScheduledExecutor();
        smsCreatorScheduler.scheduleWithFixedDelay(new SmsCreatorThread(),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_SMS_CREATORL_INIT_DELAY)),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_SMS_CREATOR_PROC_DELAY)),
                TimeUnit.SECONDS  );
        schedulerLogging.info("smsCreatorScheduler  : startup context");

        smsSenderScheduler = Executors.newSingleThreadScheduledExecutor();
        smsSenderScheduler.scheduleWithFixedDelay(new SmsSenderThread(),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_SMS_SENDER_INIT_DELAY)),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_SMS_SENDER_PROC_DELAY)),
                TimeUnit.MINUTES  );
        schedulerLogging.info("smsSenderScheduler  : startup context");


		mailScheduler = Executors.newSingleThreadScheduledExecutor();
		mailScheduler.scheduleWithFixedDelay(new SingleMailmanThread(),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_STANNDARD_MAIL_INIT_DELAY)),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_STANNDARD_MAIL_PROC_DELAY)),
                TimeUnit.SECONDS);
        schedulerLogging.info("mailScheduler  : startup context");

        mailCreator = Executors.newSingleThreadScheduledExecutor();
        mailCreator.scheduleWithFixedDelay(new EmailCreatorThread(),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_EMAIL_CREATORL_INIT_DELAY)),
                ParseUtil.sToL(processSchedulerConfig.get(Constants.PROP_EMAIL_CREATOR_PROC_DELAY)),
                TimeUnit.SECONDS  );
        schedulerLogging.info("mailCreator  : startup context");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (mailScheduler != null) {
			mailScheduler.shutdownNow();
			schedulerLogging.info("mailScheduler  : shut down ");
		}

        if(smsCreatorScheduler!=null)
        {
            smsCreatorScheduler.shutdownNow();
            schedulerLogging.info("smsCreatorScheduler  : shut down ");
        }

        if(smsSenderScheduler!=null)
        {
            smsSenderScheduler.shutdownNow();
            schedulerLogging.info("smsSenderScheduler  : shut down ");
        }

        if( mailCreator != null )
        {
            mailCreator.shutdownNow();
            schedulerLogging.info("mailCreator  : shut down ");
        }

	}
}
