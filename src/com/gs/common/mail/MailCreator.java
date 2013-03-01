package com.gs.common.mail;

import com.gs.bean.email.EmailObject;
import com.gs.bean.email.EmailScheduleBean;

public interface MailCreator {
	public void create(EmailObject emailObject, EmailScheduleBean emailScheduleBean);
}
