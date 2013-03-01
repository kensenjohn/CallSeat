package com.gs.common.mail;

import com.gs.bean.email.EmailObject;
import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.common.Utility;

public class SingleEmailCreator implements MailCreator {

	@Override
	public void create(EmailObject emailObject, EmailScheduleBean emailScheduleBean) {
		MailingServiceData mailingServiceData = new MailingServiceData();

		EmailQueueBean emailQueueBean = (EmailQueueBean) emailObject;
		emailQueueBean.setEmailQueueId(Utility.getNewGuid());
		mailingServiceData.insertEmailQueue(emailQueueBean);
	}

}
