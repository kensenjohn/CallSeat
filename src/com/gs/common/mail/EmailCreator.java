package com.gs.common.mail;

import com.gs.bean.email.EmailObject;
import com.gs.bean.email.EmailQueueBean;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.common.Constants;
import com.gs.common.Utility;
import com.gs.common.sms.SmsSchedulerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/26/13
 * Time: 11:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailCreator implements MailCreator {
    private static final Logger emailLogging = LoggerFactory.getLogger(Constants.EMAILER_LOGS);
    @Override
    public void create(EmailObject emailObject, EmailScheduleBean emailScheduleBean) {
        MailingServiceData mailingServiceData = new MailingServiceData();

        EmailQueueBean emailQueueBean = (EmailQueueBean) emailObject;
        emailQueueBean.setEmailQueueId(Utility.getNewGuid());
        Integer iNumOfRows = mailingServiceData.insertEmailQueue(emailQueueBean);

        if(emailScheduleBean!=null && !"".equalsIgnoreCase(emailScheduleBean.getEmailScheduleId()))
        {
            // updating Schedule so this record will not get picked up again
            if(iNumOfRows>0)
            {
                emailLogging.info("Successfully created entry in Email Queue : " + emailScheduleBean );
                // Successfully created entry in Queue.
                emailScheduleBean.setScheduleStatus( Constants.SCHEDULER_STATUS.COMPLETE.getSchedulerStatus()  );
            }
            else
            {
                emailLogging.info("There was an error creating entry in Email : " + emailScheduleBean );
                // There as an error creating entry in Queue
                emailScheduleBean.setScheduleStatus(Constants.SCHEDULER_STATUS.ERROR.getSchedulerStatus());
            }
            EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
            emailSchedulerData.updateSchedule( emailScheduleBean );
        }

    }

    public void update( EmailScheduleBean emailScheduleBean )
    {
        if(emailScheduleBean!=null)
        {
            EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
            emailSchedulerData.updateSchedule( emailScheduleBean );
        }
    }
}
