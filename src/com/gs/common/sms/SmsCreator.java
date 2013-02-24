package com.gs.common.sms;

import com.gs.bean.email.EmailObject;
import com.gs.bean.sms.SmsObject;
import com.gs.bean.sms.SmsQueueBean;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/16/13
 * Time: 1:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsCreator {
    private static final Logger smsLogging = LoggerFactory.getLogger(Constants.SMS_LOGS);

    public void create(SmsObject smsObject , SmsScheduleBean smsScheduleBean )
    {
        if(smsObject!=null && smsScheduleBean!=null)
        {
            Long lCurrentTime = DateSupport.getEpochMillis();
            String sCurrentTime = DateSupport.getUTCDateTime();
            SmsQueueBean smsQueueBean = new SmsQueueBean();
            smsQueueBean.setSmsQueueId(Utility.getNewGuid());
            smsQueueBean.setFromTelNumber( smsObject.getFromPhoneNumber() );
            smsQueueBean.setToTelNumber( smsObject.getToPhoneNumber() );
            smsQueueBean.setTemplateId( smsScheduleBean.getSmsTemplateId() );
            smsQueueBean.setSmsMessage( smsObject.getMessage() );
            smsQueueBean.setStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );
            smsQueueBean.setEventId( smsScheduleBean.getEventId() );
            smsQueueBean.setAdminId( smsScheduleBean.getAdminId() );
            smsQueueBean.setGuestId( smsScheduleBean.getGuestId() );
            smsQueueBean.setCreateDate( lCurrentTime );
            smsQueueBean.setHumanCreateDate( sCurrentTime );
            smsQueueBean.setModifiedDate( lCurrentTime );
            smsQueueBean.setHumanModifiedDate( sCurrentTime );
            smsQueueBean.setEventType( "" );

            SmsServiceData smsServiceData = new SmsServiceData();
            Integer iNumOfRows = smsServiceData.createSmsQueue( smsQueueBean );


            // updating Schedule so this record will not get picked up again
            if(iNumOfRows>0)
            {
                smsLogging.info("Successfully created entry in SMS Queue : " + smsScheduleBean );
                // Successfully created entry in Queue.
                smsScheduleBean.setScheduleStatus( Constants.SCHEDULER_STATUS.COMPLETE.getSchedulerStatus()  );
            }
            else
            {
                smsLogging.info("There was an error creating entry in Sms : " + smsScheduleBean );
                // There as an error creating entry in Queue
                smsScheduleBean.setScheduleStatus(Constants.SCHEDULER_STATUS.ERROR.getSchedulerStatus());
            }
            SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
            smsSchedulerData.updateSchedule(smsScheduleBean);
        }
        else
        {
            smsLogging.info("Invalid data, unable to create Sms Queue");
        }
    }

    public void update( SmsScheduleBean smsScheduleBean )
    {
        if(smsScheduleBean!=null)
        {
            SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
            smsSchedulerData.updateSchedule(smsScheduleBean);
        }
    }
}
