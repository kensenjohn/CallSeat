package com.gs.common.sms;

import com.gs.bean.sms.SmsQueueBean;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/22/13
 * Time: 1:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsSenderService {
    private SmsSender smsSender;

    private static final Logger smsLogging = LoggerFactory.getLogger(Constants.SMS_LOGS);
    private Configuration smsConfig = Configuration.getInstance(Constants.SMS_PROP);

    public SmsSenderService( SmsSender smsSender )
    {
        this.smsSender = smsSender;
    }

    public void invokeSmsSender() {
        if( this.smsSender!=null )
        {
            smsLogging.debug("Start execution of sms sender.");
            Long lCurrentTime =  DateSupport.getEpochMillis();
            ArrayList<SmsQueueBean> arrOldSmsQueueBean = getOldSmsQueueBean(lCurrentTime);
            if(arrOldSmsQueueBean!=null && !arrOldSmsQueueBean.isEmpty())
            {
                smsLogging.debug( " Old Sms Queue Bean " + arrOldSmsQueueBean);
                for(SmsQueueBean oldSmsQueueBean : arrOldSmsQueueBean )
                {
                    oldSmsQueueBean.setStatus( Constants.SCHEDULER_STATUS.ERROR.getSchedulerStatus()  );
                    this.smsSender.update( oldSmsQueueBean );
                }
            }

            ArrayList<SmsQueueBean> arrNewSmsQueueBean = getNewSmsQueueBean(lCurrentTime);
            if(arrNewSmsQueueBean!=null && !arrNewSmsQueueBean.isEmpty())
            {
                smsLogging.debug( " New Sms Queue Bean " + arrNewSmsQueueBean);
                for(SmsQueueBean newSmsQueueBean : arrNewSmsQueueBean )
                {
                    newSmsQueueBean.setStatus( Constants.SCHEDULER_STATUS.PICKED_TO_PROCESS.getSchedulerStatus()  );
                    newSmsQueueBean.setModifiedDate( lCurrentTime );
                    newSmsQueueBean.setHumanModifiedDate( DateSupport.getTimeByZone(lCurrentTime,Constants.DEFAULT_TIMEZONE,Constants.DATE_PATTERN_TZ));
                    this.smsSender.update( newSmsQueueBean );
                }
                for(SmsQueueBean newSmsQueueBean : arrNewSmsQueueBean )
                {
                    smsLogging.debug( " Before send SMS Send " + newSmsQueueBean + " - " + ParseUtil.checkNull( newSmsQueueBean.getSmsMessage() ));
                    Integer iNumOfRows = this.smsSender.send( newSmsQueueBean );

                    if(iNumOfRows>0)
                    {
                        newSmsQueueBean.setStatus( Constants.SCHEDULER_STATUS.COMPLETE.getSchedulerStatus()  );
                    }
                    else
                    {
                        newSmsQueueBean.setStatus( Constants.SCHEDULER_STATUS.ERROR.getSchedulerStatus()  );
                    }
                    Long modifiedTime = DateSupport.getEpochMillis();
                    newSmsQueueBean.setModifiedDate( modifiedTime );
                    newSmsQueueBean.setHumanModifiedDate( DateSupport.getTimeByZone(modifiedTime,Constants.DEFAULT_TIMEZONE,Constants.DATE_PATTERN_TZ));
                    this.smsSender.update( newSmsQueueBean );
                }
            }
            smsLogging.debug("End execution of sms sender.");
        }
    }

    public ArrayList<SmsQueueBean> getOldSmsQueueBean(Long lCurrentTime )
    {
        SmsServiceData smsServiceData = new SmsServiceData();
        Long lStartTime = DateSupport.subtractTime( lCurrentTime , ParseUtil.sToI(smsConfig.get(Constants.PROP_SMS_SENDER_DELAY)) , Constants.TIME_UNIT.MINUTES );

        SmsQueueBean requestSmsQueueBean = new SmsQueueBean();
        requestSmsQueueBean.setStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

        ArrayList<SmsQueueBean> arrSmsQueueBean = smsServiceData.getOldSmsQueueBean(requestSmsQueueBean, lStartTime );
        return arrSmsQueueBean;
    }

    public ArrayList<SmsQueueBean> getNewSmsQueueBean( Long lCurrentTime )
    {
        SmsServiceData smsServiceData = new SmsServiceData();
        Long lStartTime = DateSupport.subtractTime( lCurrentTime , ParseUtil.sToI(smsConfig.get(Constants.PROP_SMS_SENDER_DELAY)) , Constants.TIME_UNIT.MINUTES );

        SmsQueueBean requestSmsQueueBean = new SmsQueueBean();
        requestSmsQueueBean.setStatus( Constants.SCHEDULER_STATUS.NEW_SCHEDULE.getSchedulerStatus() );

        ArrayList<SmsQueueBean> arrSmsQueueBean = smsServiceData.getSmsQueueBean(requestSmsQueueBean, lStartTime, lCurrentTime );
        return arrSmsQueueBean;
    }
}
