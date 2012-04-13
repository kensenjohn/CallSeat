package com.gs.common.mail;

import com.gs.bean.email.EmailObject;

public class QuickMailSendThread implements Runnable {

	private EmailObject emailObject;

	public QuickMailSendThread(EmailObject emailObject) {
		this.emailObject = emailObject;
	}

	@Override
	public void run() {

		if (emailObject != null) {
			MailSender mailSender = new AmazonEmailSend();
			mailSender.send(emailObject);
		}

	}

}
