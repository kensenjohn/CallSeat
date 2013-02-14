package com.gs.task;

import com.gs.bean.EventFeatureBean;
import com.gs.bean.InformGuestBean;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.bean.sms.SmsTemplateBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.common.*;
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

    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);

    public static void sendRSVPConfirmation(InformGuestBean informGuestBean)
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
                        if( Constants.EVENT_FEATURES.RSVP_SMS_CONFIRMATION.getEventFeature().equalsIgnoreCase(mapFeatureValue.getKey())
                                && ParseUtil.sTob(mapFeatureValue.getValue()) )
                        {
                            SmsScheduleBean requestSmsSchedulerBean = new SmsScheduleBean();
                            requestSmsSchedulerBean.setEventId( informGuestBean.getEventId() );
                            requestSmsSchedulerBean.setAdminId( informGuestBean.getAdminId() );
                            requestSmsSchedulerBean.setGuestId( informGuestBean.getGuestId() );


                            SmsSchedulerData smsSchedulerData = new SmsSchedulerData();
                            SmsScheduleBean smsScheduleBean = smsSchedulerData.getGuestScheduler( requestSmsSchedulerBean , Constants.SCHEDULER_STATUS.NEW_SCHEDULE );


                            SmsServiceData smsServiceData = new SmsServiceData();
                            SmsTemplateBean smsTemplateBean = smsServiceData.getSmsTemplate( Constants.SMS_TEMPLATE.SMS_RSVP_CONFIRMATION );
                            if(smsScheduleBean!=null && smsScheduleBean.getSmsScheduleId()!=null && !"".equalsIgnoreCase(smsScheduleBean.getSmsScheduleId()))
                            {
                                Long currentTime = DateSupport.getEpochMillis();
                                Long futureTime = DateSupport.addTime(currentTime, 15, Constants.TIME_UNIT.MINUTES);
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
                                Long futureTime = DateSupport.addTime(currentTime, 15, Constants.TIME_UNIT.MINUTES);
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

                        }
                    }
                }
            }
        }
    }

    public static void sendSeatingInformation(InformGuestBean informGuestBean)
    {
        if(informGuestBean!=null)
        {

        }
    }
}
