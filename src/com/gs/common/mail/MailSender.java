package com.gs.common.mail;

import com.gs.bean.email.EmailObject;

public interface MailSender {
	public boolean send(EmailObject emailObject);
}
