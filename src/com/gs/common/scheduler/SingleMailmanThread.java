package com.gs.common.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.common.mail.AmazonEmailSend;
import com.gs.common.mail.MailSender;
import com.gs.common.mail.MailingService;

public class SingleMailmanThread implements Runnable {

	private static final Logger schedulerLogging = LoggerFactory
			.getLogger(Constants.SCHEDULER_LOGS);

	private Configuration processSchedulerConfig = Configuration
			.getInstance(Constants.EMAILER_PROP);

	@Override
	public void run() {
		try {
			boolean isSingleEmailSendEnabled = ParseUtil
					.sTob(processSchedulerConfig
							.get(Constants.PROP_ENABLE_SINGLE_EMAIL_SEND));
			if (isSingleEmailSendEnabled) {
				MailSender mailSender = new AmazonEmailSend();
				MailingService mailingService = new MailingService(mailSender);
				mailingService.invokeMailSender();
				schedulerLogging.info("Invoking single mail complete");
			} else {
				schedulerLogging.info("Single email Sender disabled");
			}

		} catch (Exception e) {
			schedulerLogging.error("Error while invoking the mailman service "
					+ ExceptionHandler.getStackTrace(e));
		}

	}
}
