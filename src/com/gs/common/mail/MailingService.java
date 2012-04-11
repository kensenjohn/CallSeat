package com.gs.common.mail;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.email.EmailQueueBean;
import com.gs.common.Constants;

public class MailingService {
	private MailSender mailSender;
	private MailingServiceData mailServiceData = new MailingServiceData();

	private static final Logger emailerLogging = LoggerFactory
			.getLogger(Constants.EMAILER_LOGS);

	public MailingService(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void invokeMailSender() {
		emailerLogging.debug("Start execution of mail send service.");
		ArrayList<EmailQueueBean> arrEmailQueueBean = getAllNewEmails();
		if (arrEmailQueueBean != null && !arrEmailQueueBean.isEmpty()) {
			emailerLogging
					.debug("Number of emails " + arrEmailQueueBean.size());
			for (EmailQueueBean emailQueueBean : arrEmailQueueBean) {
				emailerLogging.debug("EmailQueueBean going to email : "
						+ emailQueueBean);
				Integer iNumOfRecs = setEmailStatus(emailQueueBean,
						Constants.EMAIL_STATUS.PICKED_TO_SEND);
				emailerLogging.debug("Picked to send records : " + iNumOfRecs);
				boolean isSucess = false;
				if (iNumOfRecs > 0) {
					emailerLogging.debug("Sending email to  " + emailQueueBean);
					isSucess = this.mailSender.send(emailQueueBean);
				}

				if (isSucess) {
					setEmailStatus(emailQueueBean, Constants.EMAIL_STATUS.SENT);
				} else {
					setEmailStatus(emailQueueBean, Constants.EMAIL_STATUS.ERROR);
				}
			}
		}

	}

	private ArrayList<EmailQueueBean> getAllNewEmails() {

		ArrayList<EmailQueueBean> arrEmailQueueBean = mailServiceData
				.getEmailsFromQueue(Constants.EMAIL_STATUS.NEW);
		return arrEmailQueueBean;

	}

	private Integer setEmailStatus(ArrayList<EmailQueueBean> arrEmailQueueBean,
			Constants.EMAIL_STATUS emailStatus) {
		Integer iNumOfRows = 0;
		if (arrEmailQueueBean != null && !arrEmailQueueBean.isEmpty()) {
			for (EmailQueueBean emailQueueBean : arrEmailQueueBean) {
				iNumOfRows = setEmailStatus(emailQueueBean, emailStatus);
			}
		}
		return iNumOfRows;
	}

	private Integer setEmailStatus(EmailQueueBean emailQueueBean,
			Constants.EMAIL_STATUS emailStatus) {
		Integer iNumOfRows = mailServiceData.updateEmailStatus(emailQueueBean,
				emailStatus);
		return iNumOfRows;
	}
}
