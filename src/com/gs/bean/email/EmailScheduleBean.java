package com.gs.bean.email;

import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/26/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailScheduleBean {
    public EmailScheduleBean()
    {

    }
    public EmailScheduleBean( HashMap<String,String> hmEmailScheduleBean)
    {
        this.emailScheduleId = ParseUtil.checkNull(hmEmailScheduleBean.get("EMAILSCHEDULEID"));
        this.emailTemplateId = ParseUtil.checkNull( hmEmailScheduleBean.get("FK_EMAILTEMPLATEID") );
        this.eventId = ParseUtil.checkNull( hmEmailScheduleBean.get("FK_EVENTID") );
        this.adminId = ParseUtil.checkNull( hmEmailScheduleBean.get("FK_ADMINID") );
        this.guestId = ParseUtil.checkNull( hmEmailScheduleBean.get("FK_GUESTID") );
        this.createDate = ParseUtil.sToL( hmEmailScheduleBean.get("CREATEDATE") );
        this.humanCreateDate =  ParseUtil.checkNull( hmEmailScheduleBean.get("HUMANCREATEDATE") );
        this.scheduledSendDate = ParseUtil.sToL( hmEmailScheduleBean.get("SCHEDULEDSENDDATE") );
        this.humanScheduledSendDate =  ParseUtil.checkNull( hmEmailScheduleBean.get("HUMANSCHEDULEDSENDDATE") );
        this.scheduleStatus = ParseUtil.checkNull( hmEmailScheduleBean.get("SCHEDULE_STATUS") );
    }
    private String emailScheduleId = "";
    private String emailTemplateId = "";
    private String eventId = "";
    private String adminId = "";
    private String guestId = "";
    private Long createDate = 0L;
    private String humanCreateDate = "";
    private Long scheduledSendDate = 0L;
    private String humanScheduledSendDate = "";
    private String scheduleStatus = "";

    public String getEmailScheduleId() {
        return emailScheduleId;
    }

    public void setEmailScheduleId(String emailScheduleId) {
        this.emailScheduleId = emailScheduleId;
    }

    public String getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(String emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

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
        return "EmailScheduleBean{" +
                "emailScheduleId='" + emailScheduleId + '\'' +
                ", emailTemplateId='" + emailTemplateId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", createDate=" + createDate +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", scheduledSendDate=" + scheduledSendDate +
                ", humanScheduledSendDate='" + humanScheduledSendDate + '\'' +
                ", scheduleStatus='" + scheduleStatus + '\'' +
                '}';
    }
}
