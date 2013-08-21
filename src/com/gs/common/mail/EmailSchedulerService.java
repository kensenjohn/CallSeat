package com.gs.common.mail;

import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.email.EmailSchedulerRequest;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/18/13
 * Time: 11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailSchedulerService {

    private static Configuration emailConfig = Configuration.getInstance(Constants.EMAILER_PROP);
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    public Integer createEmailSchedule(EmailSchedulerRequest emailSchedulerRequest) {
        Integer iNumOfRecords = 0;
        if(emailSchedulerRequest!=null && emailSchedulerRequest.getEmailTemplate()!=null ) {

            MailingServiceData emailServiceData = new MailingServiceData();
            EmailTemplateBean emailTemplateBean = emailServiceData.getEmailTemplate( emailSchedulerRequest.getEmailTemplate() );

            EmailScheduleBean requestEmailSchedulerBean = new EmailScheduleBean();
            if(!"".equalsIgnoreCase(ParseUtil.checkNull(emailSchedulerRequest.getEventId()))){
                requestEmailSchedulerBean.setEventId( ParseUtil.checkNull(emailSchedulerRequest.getEventId()) );
            }

            if(!"".equalsIgnoreCase(ParseUtil.checkNull(emailSchedulerRequest.getAdminId()))){
                requestEmailSchedulerBean.setAdminId( ParseUtil.checkNull(emailSchedulerRequest.getAdminId()) );
            }

            if(!"".equalsIgnoreCase(ParseUtil.checkNull(emailSchedulerRequest.getGuestId()))){
                requestEmailSchedulerBean.setGuestId( ParseUtil.checkNull(emailSchedulerRequest.getGuestId()) );
            }

            requestEmailSchedulerBean.setEmailTemplateId( emailTemplateBean.getEmailTemplateId() );

            String sScheduleTime = Constants.EMPTY;
            Long lScheduleTime = 0L;
            Long currentTime = DateSupport.getEpochMillis();
            if(emailSchedulerRequest.getScheduleTime() > 0 ) {
                lScheduleTime =  emailSchedulerRequest.getScheduleTime();
                sScheduleTime =  emailSchedulerRequest.getHumanScheduleTime();
            } else {
                lScheduleTime = DateSupport.addTime(currentTime,  ParseUtil.sToI(emailConfig.get(Constants.PROP_EMAIL_SCHEDULE_EMAIL_DELAY,"5")), Constants.TIME_UNIT.MINUTES);
                sScheduleTime = DateSupport.getTimeByZone(lScheduleTime, Constants.DEFAULT_TIMEZONE);
            }


            requestEmailSchedulerBean.setScheduledSendDate( lScheduleTime );
            requestEmailSchedulerBean.setHumanScheduledSendDate( sScheduleTime );
            requestEmailSchedulerBean.setScheduleStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

            requestEmailSchedulerBean.setEmailTemplateId( emailTemplateBean.getEmailTemplateId() );

            EmailScheduleBean emailScheduleBean = new EmailScheduleBean();
            EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
            if(emailSchedulerRequest.isUpdateScheduleIfExists()) {
                emailScheduleBean = emailSchedulerData.getEmailScheduler( requestEmailSchedulerBean , Constants.SCHEDULER_STATUS.NEW_SCHEDULE );
            }

            appLogging.info("Upodating emailScheduleBean : " + emailScheduleBean);
            if(emailScheduleBean!=null && emailScheduleBean.getEmailScheduleId()!=null && !"".equalsIgnoreCase(emailScheduleBean.getEmailScheduleId())
                    && emailSchedulerRequest.isUpdateScheduleIfExists())
            {
                requestEmailSchedulerBean.setEmailScheduleId(emailScheduleBean.getEmailScheduleId());
                iNumOfRecords =emailSchedulerData.updateSchedule( requestEmailSchedulerBean );
            } else {

                requestEmailSchedulerBean.setEmailScheduleId(Utility.getNewGuid() );
                requestEmailSchedulerBean.setCreateDate( currentTime );
                requestEmailSchedulerBean.setHumanCreateDate( DateSupport.getUTCDateTime() );

                iNumOfRecords =emailSchedulerData.createSchedule( requestEmailSchedulerBean );
            }
        }
        return iNumOfRecords;
    }
}
