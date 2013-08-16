package com.gs.call.task;

import com.gs.bean.EventFeatureBean;
import com.gs.bean.InformGuestBean;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.common.*;
import com.gs.common.mail.EmailSchedulerData;
import com.gs.common.mail.MailingServiceData;
import com.gs.common.sms.SmsSchedulerData;
import com.gs.common.sms.SmsServiceData;
import com.gs.manager.event.EventFeatureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/13/13
 * Time: 6:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class InformGuestTask {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private static Configuration smsConfig = Configuration.getInstance(Constants.SMS_PROP);
    private static Configuration emailConfig = Configuration.getInstance(Constants.EMAILER_PROP);

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public static void sendRSVPConfirmation(InformGuestBean informGuestBean)
    {
        if(informGuestBean!=null)
        {
            EventFeatureManager eventFeatureManager = new EventFeatureManager();
            EventFeatureBean allEventFeatureBean =  eventFeatureManager.getEventFeatures(informGuestBean.getEventId());

            appLogging.info("All Event features bean  : " + allEventFeatureBean  );
            if(allEventFeatureBean!=null)
            {
                HashMap<String, String> hmAllEventFeature = allEventFeatureBean.getHmFeatureValue();

                if(hmAllEventFeature!=null && !hmAllEventFeature.isEmpty())
                {
                    for(Map.Entry<String,String> mapFeatureValue : hmAllEventFeature.entrySet() )
                    {
                        if( Constants.EVENT_FEATURES.RSVP_SMS_CONFIRMATION.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey())
                                && ParseUtil.sTob(mapFeatureValue.getValue()) )
                        {
                            SmsServiceData smsServiceData = new SmsServiceData();
                            SmsTemplateBean smsTemplateBean = smsServiceData.getSmsTemplate( Constants.SMS_TEMPLATE.SMS_RSVP_CONFIRMATION );

                            SmsScheduleBean requestSmsSchedulerBean = new SmsScheduleBean();
                            requestSmsSchedulerBean.setEventId( informGuestBean.getEventId() );
                            requestSmsSchedulerBean.setAdminId( informGuestBean.getAdminId() );
                            requestSmsSchedulerBean.setGuestId( informGuestBean.getGuestId() );
                            requestSmsSchedulerBean.setSmsTemplateId( smsTemplateBean.getSmsTemplateId() );


                            SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
                            SmsScheduleBean smsScheduleBean = smsSchedulerData.getGuestScheduler( requestSmsSchedulerBean , Constants.SCHEDULER_STATUS.NEW_SCHEDULE );

                            // appLogging.info("RSVP SMSSChedule bean  : " + smsScheduleBean  );
                            if(smsScheduleBean!=null && smsScheduleBean.getSmsScheduleId()!=null && !"".equalsIgnoreCase(smsScheduleBean.getSmsScheduleId()))
                            {
                                Long currentTime = DateSupport.getEpochMillis();
                                Long futureTime = DateSupport.addTime(currentTime, ParseUtil.sToI(smsConfig.get(Constants.PROP_SMS_SCHEDULE_SMS_DELAY)), Constants.TIME_UNIT.MINUTES);
                                String sFutureTime = DateSupport.getTimeByZone(futureTime, Constants.DEFAULT_TIMEZONE);

                                requestSmsSchedulerBean.setSmsScheduleId(smsScheduleBean.getSmsScheduleId());

                                requestSmsSchedulerBean.setScheduledSendDate( futureTime );
                                requestSmsSchedulerBean.setHumanScheduledSendDate( sFutureTime );
                                requestSmsSchedulerBean.setScheduleStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

                                requestSmsSchedulerBean.setSmsTemplateId( smsTemplateBean.getSmsTemplateId() );

                                smsSchedulerData.updateSchedule( requestSmsSchedulerBean );
                            }
                            else
                            {
                                Long currentTime = DateSupport.getEpochMillis();
                                Long futureTime = DateSupport.addTime(currentTime,  ParseUtil.sToI(smsConfig.get(Constants.PROP_SMS_SCHEDULE_SMS_DELAY)), Constants.TIME_UNIT.MINUTES);
                                String sFutureTime = DateSupport.getTimeByZone(futureTime, Constants.DEFAULT_TIMEZONE);

                                requestSmsSchedulerBean.setSmsScheduleId(Utility.getNewGuid() );
                                requestSmsSchedulerBean.setCreateDate( currentTime );
                                requestSmsSchedulerBean.setHumanCreateDate( DateSupport.getUTCDateTime() );


                                requestSmsSchedulerBean.setScheduledSendDate( futureTime );
                                requestSmsSchedulerBean.setHumanScheduledSendDate( sFutureTime );
                                requestSmsSchedulerBean.setScheduleStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

                                requestSmsSchedulerBean.setSmsTemplateId( smsTemplateBean.getSmsTemplateId() );

                                smsSchedulerData.createSchedule( requestSmsSchedulerBean );
                            }

                        }

                        if( Constants.EVENT_FEATURES.RSVP_EMAIL_CONFIRMATION.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey())
                                && ParseUtil.sTob(mapFeatureValue.getValue()) )
                        {
                            MailingServiceData emailServiceData = new MailingServiceData();
                            EmailTemplateBean emailTemplateBean = emailServiceData.getEmailTemplate( Constants.EMAIL_TEMPLATE.RSVP_CONFIRMATION_EMAIL );

                            EmailScheduleBean requestEmailSchedulerBean = new EmailScheduleBean();
                            requestEmailSchedulerBean.setEventId( informGuestBean.getEventId() );
                            requestEmailSchedulerBean.setAdminId( informGuestBean.getAdminId() );
                            requestEmailSchedulerBean.setGuestId( informGuestBean.getGuestId() );
                            requestEmailSchedulerBean.setEmailTemplateId( emailTemplateBean.getEmailTemplateId() );


                            EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
                            EmailScheduleBean emailScheduleBean = emailSchedulerData.getGuestScheduler( requestEmailSchedulerBean , Constants.SCHEDULER_STATUS.NEW_SCHEDULE );

                            Long currentTime = DateSupport.getEpochMillis();
                            Long futureTime = DateSupport.addTime(currentTime,  ParseUtil.sToI(emailConfig.get(Constants.PROP_EMAIL_SCHEDULE_EMAIL_DELAY)), Constants.TIME_UNIT.MINUTES);
                            String sFutureTime = DateSupport.getTimeByZone(futureTime, Constants.DEFAULT_TIMEZONE);

                            requestEmailSchedulerBean.setScheduledSendDate( futureTime );
                            requestEmailSchedulerBean.setHumanScheduledSendDate( sFutureTime );
                            requestEmailSchedulerBean.setScheduleStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

                            requestEmailSchedulerBean.setEmailTemplateId( emailTemplateBean.getEmailTemplateId() );


                            // appLogging.info("RSVP SMSSChedule bean  : " + smsScheduleBean  );
                            if(emailScheduleBean!=null && emailScheduleBean.getEmailScheduleId()!=null && !"".equalsIgnoreCase(emailScheduleBean.getEmailScheduleId()))
                            {
                                requestEmailSchedulerBean.setEmailScheduleId(emailScheduleBean.getEmailScheduleId());


                                emailSchedulerData.updateSchedule( requestEmailSchedulerBean );
                            }
                            else
                            {

                                requestEmailSchedulerBean.setEmailScheduleId(Utility.getNewGuid() );
                                requestEmailSchedulerBean.setCreateDate( currentTime );
                                requestEmailSchedulerBean.setHumanCreateDate( DateSupport.getUTCDateTime() );

                                emailSchedulerData.createSchedule( requestEmailSchedulerBean );
                            }

                        }
                    }
                }
                else
                {
                    appLogging.info("Event Feature HashMap"  );
                }
            }
            else
            {
                appLogging.info("There were no Event Features found for : " + informGuestBean.getEventId()  );
            }
        }
        else
        {
            appLogging.info("Invalid informGuest Bean"  );
        }
    }

    public static void sendSeatingConfirmation(InformGuestBean informGuestBean)
    {
        if(informGuestBean!=null)
        {
            EventFeatureManager eventFeatureManager = new EventFeatureManager();
            EventFeatureBean allEventFeatureBean =  eventFeatureManager.getEventFeatures(informGuestBean.getEventId());

            if(allEventFeatureBean!=null)
            {
                HashMap<String, String> hmAllEventFeature = allEventFeatureBean.getHmFeatureValue();

                if(hmAllEventFeature!=null && !hmAllEventFeature.isEmpty())
                {
                    for(Map.Entry<String,String> mapFeatureValue : hmAllEventFeature.entrySet() )
                    {
                        if( Constants.EVENT_FEATURES.SEATING_SMS_GUEST_AFTER_CALL.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey())
                                && ParseUtil.sTob(mapFeatureValue.getValue()) )
                        {
                            SmsServiceData smsServiceData = new SmsServiceData();
                            SmsTemplateBean smsTemplateBean = smsServiceData.getSmsTemplate( Constants.SMS_TEMPLATE.SMS_SEATING_CONFIRMATION );

                            SmsScheduleBean requestSmsSchedulerBean = new SmsScheduleBean();
                            requestSmsSchedulerBean.setEventId( informGuestBean.getEventId() );
                            requestSmsSchedulerBean.setAdminId( informGuestBean.getAdminId() );
                            requestSmsSchedulerBean.setGuestId( informGuestBean.getGuestId() );
                            requestSmsSchedulerBean.setSmsTemplateId( smsTemplateBean.getSmsTemplateId() );

                            SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
                            SmsScheduleBean smsScheduleBean = smsSchedulerData.getGuestScheduler( requestSmsSchedulerBean , Constants.SCHEDULER_STATUS.NEW_SCHEDULE );

                            Long currentTime = DateSupport.getEpochMillis();
                            Long futureTime = DateSupport.addTime(currentTime, ParseUtil.sToI(smsConfig.get(Constants.PROP_SMS_SCHEDULE_SMS_DELAY)), Constants.TIME_UNIT.MINUTES);
                            String sFutureTime = DateSupport.getTimeByZone(futureTime, Constants.DEFAULT_TIMEZONE);
                            //appLogging.info("Current Time : " + currentTime + " future time : " + futureTime +" - + "  + sFutureTime );

                            requestSmsSchedulerBean.setScheduledSendDate( futureTime );
                            requestSmsSchedulerBean.setHumanScheduledSendDate( sFutureTime );
                            requestSmsSchedulerBean.setScheduleStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

                            if(smsScheduleBean!=null && smsScheduleBean.getSmsScheduleId()!=null && !"".equalsIgnoreCase(smsScheduleBean.getSmsScheduleId()))
                            {
                                requestSmsSchedulerBean.setSmsScheduleId(smsScheduleBean.getSmsScheduleId());

                                smsSchedulerData.updateSchedule( requestSmsSchedulerBean );
                            }
                            else
                            {
                                requestSmsSchedulerBean.setSmsScheduleId(Utility.getNewGuid() );
                                requestSmsSchedulerBean.setCreateDate( currentTime );
                                requestSmsSchedulerBean.setHumanCreateDate( DateSupport.getUTCDateTime() );

                                smsSchedulerData.createSchedule( requestSmsSchedulerBean );
                            }
                        }

                        appLogging.info("Event features  : " + mapFeatureValue.getKey() + " - " + ParseUtil.sTob(mapFeatureValue.getValue())   );
                        if( Constants.EVENT_FEATURES.SEATING_EMAIL_GUEST_AFTER_CALL.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey())
                                && ParseUtil.sTob(mapFeatureValue.getValue()) )
                        {
                            MailingServiceData mailingServiceData = new MailingServiceData();
                            EmailTemplateBean emailTemplateBean = mailingServiceData.getEmailTemplate(Constants.EMAIL_TEMPLATE.SEATING_CONFIRMATION_EMAIL);

                            appLogging.info(" EmailTemplateBean  : " + emailTemplateBean   );

                            EmailScheduleBean requestEmailSchedulerBean = new EmailScheduleBean();
                            requestEmailSchedulerBean.setEventId( informGuestBean.getEventId() );
                            requestEmailSchedulerBean.setAdminId( informGuestBean.getAdminId() );
                            requestEmailSchedulerBean.setGuestId( informGuestBean.getGuestId() );
                            requestEmailSchedulerBean.setEmailTemplateId(emailTemplateBean.getEmailTemplateId());

                            EmailSchedulerData emailSchedulerData = new EmailSchedulerData();
                            EmailScheduleBean emailScheduleBean = emailSchedulerData.getGuestScheduler( requestEmailSchedulerBean , Constants.SCHEDULER_STATUS.NEW_SCHEDULE );

                            appLogging.info(" EmailTemplateBean  : email schedule bean : " + emailScheduleBean   );
                            Long currentTime = DateSupport.getEpochMillis();
                            Long futureTime = DateSupport.addTime(currentTime, ParseUtil.sToI(emailConfig.get(Constants.PROP_EMAIL_SCHEDULE_EMAIL_DELAY)), Constants.TIME_UNIT.MINUTES);
                            String sFutureTime = DateSupport.getTimeByZone(futureTime, Constants.DEFAULT_TIMEZONE);

                            requestEmailSchedulerBean.setScheduledSendDate( futureTime );
                            requestEmailSchedulerBean.setHumanScheduledSendDate( sFutureTime );
                            requestEmailSchedulerBean.setScheduleStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

                            if(emailScheduleBean!=null && emailScheduleBean.getEmailScheduleId()!=null && !"".equalsIgnoreCase(emailScheduleBean.getEmailScheduleId()))
                            {
                                requestEmailSchedulerBean.setEmailScheduleId( emailScheduleBean.getEmailScheduleId() );

                                emailSchedulerData.updateSchedule( requestEmailSchedulerBean );
                            }
                            else
                            {
                                requestEmailSchedulerBean.setEmailScheduleId(Utility.getNewGuid() );
                                requestEmailSchedulerBean.setCreateDate( currentTime );
                                requestEmailSchedulerBean.setHumanCreateDate( DateSupport.getUTCDateTime() );

                                emailSchedulerData.createSchedule( requestEmailSchedulerBean );
                            }
                        }
                    }
                }
            }
        }
    }
}
