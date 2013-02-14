package com.gs.common.sms;

import com.gs.bean.sms.SmsScheduleBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.db.DBDAO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/13/13
 * Time: 11:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmsSchedulerData {

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);
    private String sourceFile = "SmsSchedulerData.java";
    public Integer createSchedule( SmsScheduleBean smsRequestSchedulerBean )
    {
        Integer iNumberOfRows = 0;
        if(smsRequestSchedulerBean!=null)
        {
            String sQuery = "INSERT INTO GTSMSSCHEDULE (SMSSCHEDULEID,FK_SMSTEMPLATEID,FK_EVENTID, " +
                    " FK_ADMINID,FK_GUESTID, CREATEDATE, HUMANCREATEDATE,SCHEDULEDSENDDATE,HUMANSCHEDULEDSENDDATE," +
                    " SCHEDULE_STATUS ) VALUES(?,?,?, ?,?,?, ?,?,?, ?)";
            ArrayList<Object> aParams = DBDAO.createConstraint( smsRequestSchedulerBean.getSmsScheduleId(), smsRequestSchedulerBean.getSmsTemplateId(),
                    smsRequestSchedulerBean.getEventId() , smsRequestSchedulerBean.getAdminId() , smsRequestSchedulerBean.getGuestId() ,
                    smsRequestSchedulerBean.getCreateDate() , smsRequestSchedulerBean.getHumanCreateDate() , smsRequestSchedulerBean.getScheduledSendDate(),
                    smsRequestSchedulerBean.getHumanScheduledSendDate(), smsRequestSchedulerBean.getScheduleStatus() );

            iNumberOfRows = DBDAO.putRowsQuery( sQuery,aParams,ADMIN_DB,sourceFile,"createSchedule()" );
        }
        return iNumberOfRows;
    }

    public Integer updateSchedule( SmsScheduleBean smsRequestSchedulerBean )
    {
        Integer iNumberOfRows = 0;
        if(smsRequestSchedulerBean!=null)
        {
            String sQuery = "UPDATE GTSMSSCHEDULE SET SCHEDULEDSENDDATE = ? , HUMANSCHEDULEDSENDDATE = ? , SCHEDULE_STATUS = ? , " +
                    "  FK_SMSTEMPLATEID = ? WHERE SMSSCHEDULEID = ? AND FK_EVENTID = ? AND FK_ADMINID = ? AND FK_GUESTID = ? ";
            ArrayList<Object> aParams = DBDAO.createConstraint( smsRequestSchedulerBean.getScheduledSendDate(), smsRequestSchedulerBean.getHumanScheduledSendDate() ,
                    smsRequestSchedulerBean.getScheduleStatus(), smsRequestSchedulerBean.getSmsTemplateId(), smsRequestSchedulerBean.getSmsScheduleId(),
                    smsRequestSchedulerBean.getEventId() , smsRequestSchedulerBean.getAdminId(), smsRequestSchedulerBean.getGuestId() );

            iNumberOfRows = DBDAO.putRowsQuery( sQuery,aParams,ADMIN_DB,sourceFile,"updateSchedule()" );
        }
        return iNumberOfRows;
    }

    public SmsScheduleBean getGuestScheduler( SmsScheduleBean smsRequestSchedulerBean , Constants.SCHEDULER_STATUS scheduleStatus )
    {
        /*
        CREATE TABLE GTSMSSCHEDULE( SMSSCHEDULEID VARCHAR(45) NOT NULL, FK_SMSTEMPLATEID VARCHAR(45) NOT NULL,  FK_EVENTID VARCHAR(45),
         FK_ADMINID VARCHAR(45), FK_GUESTID VARCHAR(45), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), SCHEDULEDSENDDATE BIGINT(20)
          NOT NULL DEFAULT 0, HUMANSCHEDULEDSENDDATE VARCHAR(45), SCHEDULE_STATUS VARCHAR(45) NOT NULL,PRIMARY KEY (SMSSCHEDULEID) )
           ENGINE = MyISAM DEFAULT CHARSET = utf8;
         */
        SmsScheduleBean smsScheduleBean = new SmsScheduleBean();
        if(smsRequestSchedulerBean != null && smsRequestSchedulerBean.getEventId()!=null && !"".equalsIgnoreCase(smsRequestSchedulerBean.getEventId())
                && smsRequestSchedulerBean.getAdminId()!=null && !"".equalsIgnoreCase(smsRequestSchedulerBean.getAdminId())
                && smsRequestSchedulerBean.getGuestId()!=null && !"".equalsIgnoreCase(smsRequestSchedulerBean.getGuestId())
                && scheduleStatus !=null)
        {
            String sQuery = "SELECT * FROM GTSMSSCHEDULE WHERE FK_EVENTID = ? AND FK_ADMINID = ? AND FK_GUESTID = ? AND SCHEDULE_STATUS = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint( smsRequestSchedulerBean.getEventId() , smsRequestSchedulerBean.getAdminId(),
                    smsRequestSchedulerBean.getGuestId() , scheduleStatus.getSchedulerStatus() );

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getGuestScheduler()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                for( HashMap<String, String> hmResult : arrResult )
                {
                    smsScheduleBean = new SmsScheduleBean(hmResult);
                }
            }
        }
        return smsScheduleBean;
    }
}
