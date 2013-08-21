package com.gs.common.mail;

import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.sms.SmsScheduleBean;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/26/13
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailSchedulerData {

    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String ADMIN_DB = applicationConfig.get(Constants.ADMIN_DB);
    private String sourceFile = "EmailSchedulerData.java";

    private static final Logger emailLogging = LoggerFactory.getLogger(Constants.EMAILER_LOGS);

    public Integer createSchedule( EmailScheduleBean emailRequestSchedulerBean )
    {
        Integer iNumberOfRows = 0;
        if(emailRequestSchedulerBean!=null)
        {
            String sQuery = "INSERT INTO GTEMAILSCHEDULE (EMAILSCHEDULEID,FK_EMAILTEMPLATEID,FK_EVENTID, " +
                    " FK_ADMINID,FK_GUESTID, CREATEDATE, HUMANCREATEDATE,SCHEDULEDSENDDATE,HUMANSCHEDULEDSENDDATE," +
                    " SCHEDULE_STATUS ) VALUES(?,?,?, ?,?,?, ?,?,?, ?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(emailRequestSchedulerBean.getEmailScheduleId(), emailRequestSchedulerBean.getEmailTemplateId(),
                    emailRequestSchedulerBean.getEventId(), emailRequestSchedulerBean.getAdminId(), ParseUtil.checkNull(emailRequestSchedulerBean.getGuestId()),
                    emailRequestSchedulerBean.getCreateDate(), emailRequestSchedulerBean.getHumanCreateDate(), emailRequestSchedulerBean.getScheduledSendDate(),
                    emailRequestSchedulerBean.getHumanScheduledSendDate(), emailRequestSchedulerBean.getScheduleStatus());

            iNumberOfRows = DBDAO.putRowsQuery( sQuery,aParams,ADMIN_DB,sourceFile,"createSchedule()" );
        }
        return iNumberOfRows;
    }

    public Integer updateSchedule( EmailScheduleBean emailRequestSchedulerBean )
    {
        Integer iNumberOfRows = 0;
        if(emailRequestSchedulerBean!=null)
        {
            String sQuery = "UPDATE GTEMAILSCHEDULE SET SCHEDULEDSENDDATE = ? , HUMANSCHEDULEDSENDDATE = ? , SCHEDULE_STATUS = ? , " +
                    "  FK_EMAILTEMPLATEID = ? WHERE EMAILSCHEDULEID = ? AND FK_EVENTID = ? AND FK_ADMINID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint( emailRequestSchedulerBean.getScheduledSendDate(), emailRequestSchedulerBean.getHumanScheduledSendDate() ,
                    emailRequestSchedulerBean.getScheduleStatus(), emailRequestSchedulerBean.getEmailTemplateId(), emailRequestSchedulerBean.getEmailScheduleId(),
                    emailRequestSchedulerBean.getEventId() , emailRequestSchedulerBean.getAdminId() );
            if(emailRequestSchedulerBean.getGuestId() == null || "".equalsIgnoreCase(ParseUtil.checkNull(emailRequestSchedulerBean.getGuestId()) )) {
                sQuery = sQuery + "  AND FK_GUESTID = ? ";
                aParams.add( emailRequestSchedulerBean.getGuestId() );
            }
            iNumberOfRows = DBDAO.putRowsQuery( sQuery,aParams,ADMIN_DB,sourceFile,"updateSchedule()" );
        }
        return iNumberOfRows;
    }

    public ArrayList<EmailScheduleBean> getArrSchedule(Long lStartTime, Long lEndTime, Constants.SCHEDULER_STATUS scheduleStatus,
                                                     Constants.SCHEDULE_PICKUP_TYPE schedulePickupType )
    {
        ArrayList<EmailScheduleBean> arrSchedulerBean = new ArrayList<EmailScheduleBean>();
        if(schedulePickupType!=null && scheduleStatus!=null && lStartTime>0)
        {
            String sQuery = "SELECT * FROM GTEMAILSCHEDULE WHERE ";
            ArrayList<Object> aParams = new ArrayList<Object>();

            sQuery = sQuery + " SCHEDULE_STATUS = ? ";
            aParams.add( scheduleStatus.getSchedulerStatus() );

            if(Constants.SCHEDULE_PICKUP_TYPE.NEW_RECORDS.equals( schedulePickupType ))
            {
                sQuery = sQuery + " AND SCHEDULEDSENDDATE >= ? AND SCHEDULEDSENDDATE <= ? ";
                aParams.add(lStartTime);
                aParams.add(lEndTime);
            }
            else if(Constants.SCHEDULE_PICKUP_TYPE.OLD_RECORDS.equals( schedulePickupType ))
            {
                sQuery = sQuery + " AND SCHEDULEDSENDDATE < ? ";
                aParams.add(lStartTime);
            }
            else if(Constants.SCHEDULE_PICKUP_TYPE.CURRENT_RECORD.equals( schedulePickupType ))
            {
                sQuery = sQuery + " AND SCHEDULEDSENDDATE = ? ";
                aParams.add(lStartTime);
            }



            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getArrSchedule()");
            emailLogging.info("Query : " + sQuery + " Params : " + aParams  + " result : " + arrResult);
            if(arrResult!=null && !arrResult.isEmpty() )
            {
                for( HashMap<String, String> hmResult : arrResult )
                {
                    EmailScheduleBean  emailScheduleBean = new EmailScheduleBean(hmResult);
                    arrSchedulerBean.add( emailScheduleBean );
                }
            }
        }


        return arrSchedulerBean;
    }

    public EmailScheduleBean getEmailScheduler( EmailScheduleBean emailRequestSchedulerBean , Constants.SCHEDULER_STATUS scheduleStatus )
    {
        String sQuery = Constants.EMPTY;
        ArrayList<Object> aParams = new ArrayList<Object>();

        if(emailRequestSchedulerBean != null && emailRequestSchedulerBean.getEventId()!=null && !"".equalsIgnoreCase(emailRequestSchedulerBean.getEventId())
                && emailRequestSchedulerBean.getAdminId()!=null && !"".equalsIgnoreCase(emailRequestSchedulerBean.getAdminId())
                && emailRequestSchedulerBean.getGuestId()!=null && !"".equalsIgnoreCase(emailRequestSchedulerBean.getGuestId())
                && scheduleStatus !=null)
        {
            sQuery = "SELECT * FROM GTEMAILSCHEDULE WHERE FK_EVENTID = ? AND FK_ADMINID = ? AND FK_GUESTID = ? AND SCHEDULE_STATUS = ?" +
                    " AND FK_EMAILTEMPLATEID = ?";
            aParams = DBDAO.createConstraint( emailRequestSchedulerBean.getEventId() , emailRequestSchedulerBean.getAdminId(),
                    emailRequestSchedulerBean.getGuestId() , scheduleStatus.getSchedulerStatus(), emailRequestSchedulerBean.getEmailTemplateId() );
        } else if( emailRequestSchedulerBean != null && emailRequestSchedulerBean.getEventId()!=null && !"".equalsIgnoreCase(emailRequestSchedulerBean.getEventId())
                && emailRequestSchedulerBean.getAdminId()!=null && !"".equalsIgnoreCase(emailRequestSchedulerBean.getAdminId())
                && scheduleStatus !=null ) {
            sQuery = "SELECT * FROM GTEMAILSCHEDULE WHERE FK_EVENTID = ? AND FK_ADMINID = ? AND SCHEDULE_STATUS = ?" +
                    " AND FK_EMAILTEMPLATEID = ?";
            aParams = DBDAO.createConstraint( emailRequestSchedulerBean.getEventId() , emailRequestSchedulerBean.getAdminId(),
                    scheduleStatus.getSchedulerStatus(), emailRequestSchedulerBean.getEmailTemplateId() );
        }

        EmailScheduleBean emailScheduleBean = new EmailScheduleBean();
        if(emailRequestSchedulerBean != null)
        {

            ArrayList<HashMap<String, String>> arrResult = DBDAO.getDBData(ADMIN_DB,sQuery,aParams,true,sourceFile,"getEmailScheduler()");

            if(arrResult!=null && !arrResult.isEmpty())
            {
                for( HashMap<String, String> hmResult : arrResult )
                {
                    emailScheduleBean = new EmailScheduleBean(hmResult);
                }
            }
        }
        return emailScheduleBean;
    }


}
