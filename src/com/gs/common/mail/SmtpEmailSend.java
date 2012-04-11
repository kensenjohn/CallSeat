package com.gs.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.email.EmailObject;
import com.gs.common.Configuration;
import com.gs.common.Constants;

public class SmtpEmailSend implements MailSender {

	private Logger emailerLogging = LoggerFactory
			.getLogger(Constants.EMAILER_LOGS);
	private Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	@Override
	public boolean send(EmailObject emailObject) {
		boolean isSuccess = true;

		emailerLogging.info("Complete Simple send of email");

		return isSuccess;
	}

}
