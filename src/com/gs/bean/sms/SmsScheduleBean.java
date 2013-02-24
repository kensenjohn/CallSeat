package com.gs.bean.sms;

import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/14/13
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsScheduleBean {

    public SmsScheduleBean()
    {

    }
    public SmsScheduleBean(HashMap<String,String> hmSmsScheduleBean)
    {
        this.smsScheduleId = ParseUtil.checkNull( hmSmsScheduleBean.get("SMSSCHEDULEID") );
        this.smsTemplateId = ParseUtil.checkNull( hmSmsScheduleBean.get("FK_SMSTEMPLATEID") );
        this.eventId = ParseUtil.checkNull( hmSmsScheduleBean.get("FK_EVENTID") );
        this.adminId = ParseUtil.checkNull( hmSmsScheduleBean.get("FK_ADMINID") );
        this.guestId = ParseUtil.checkNull( hmSmsScheduleBean.get("FK_GUESTID") );
        this.createDate = ParseUtil.sToL( hmSmsScheduleBean.get("CREATEDATE") );
        this.humanCreateDate =  ParseUtil.checkNull( hmSmsScheduleBean.get("HUMANCREATEDATE") );
        this.scheduledSendDate = ParseUtil.sToL( hmSmsScheduleBean.get("SCHEDULEDSENDDATE") );
        this.humanScheduledSendDate =  ParseUtil.checkNull( hmSmsScheduleBean.get("HUMANSCHEDULEDSENDDATE") );
        this.scheduleStatus = ParseUtil.checkNull( hmSmsScheduleBean.get("SCHEDULE_STATUS") );

    }
    private String smsScheduleId = "";
    private String smsTemplateId = "";
    private String eventId = "";
    private String adminId = "";
    private String guestId = "";
    private Long createDate = 0L;
    private String humanCreateDate = "";
    private Long scheduledSendDate = 0L;
    private String humanScheduledSendDate = "";
    private String scheduleStatus = "";

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getSmsScheduleId() {
        return smsScheduleId;
    }

    public void setSmsScheduleId(String smsScheduleId) {
        this.smsScheduleId = smsScheduleId;
    }

    public String getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(String smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getHumanCreateDate() {
        return humanCreateDate;
    }

    public void setHumanCreateDate(String humanCreateDate) {
        this.humanCreateDate = humanCreateDate;
    }

    public Long getScheduledSendDate() {
        return scheduledSendDate;
    }

    public void setScheduledSendDate(Long scheduledSendDate) {
        this.scheduledSendDate = scheduledSendDate;
    }

    public String getHumanScheduledSendDate() {
        return humanScheduledSendDate;
    }

    public void setHumanScheduledSendDate(String humanScheduledSendDate) {
        this.humanScheduledSendDate = humanScheduledSendDate;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    @Override
    public String toString() {
        return "SmsScheduleBean{" +
                "smsScheduleId='" + smsScheduleId + '\'' +
                ", smsTemplateId='" + smsTemplateId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", humanScheduledSendDate='" + humanScheduledSendDate + '\'' +
                ", scheduleStatus='" + scheduleStatus + '\'' +
                ", scheduledSendDate=" + scheduledSendDate +
                ", createDate=" + createDate +
                '}';
    }
}
