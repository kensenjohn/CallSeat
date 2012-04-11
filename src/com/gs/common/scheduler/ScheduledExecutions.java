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

	private static final Logger schedulerLogging = LoggerFactory
			.getLogger(Constants.SCHEDULER_LOGS);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		mailScheduler = Executors.newSingleThreadScheduledExecutor();
		mailScheduler.scheduleWithFixedDelay(new SingleMailmanThread(),
				ParseUtil.sToL(processSchedulerConfig
						.get(Constants.PROP_STANNDARD_MAIL_INIT_DELAY)),
				ParseUtil.sToL(processSchedulerConfig
						.get(Constants.PROP_STANNDARD_MAIL_PROC_DELAY)),
				TimeUnit.SECONDS);
		schedulerLogging.info("mailScheduler  : startup context");

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (mailScheduler != null) {
			mailScheduler.shutdownNow();
			schedulerLogging.info("mailScheduler  : shut down ");
		}

	}
}
