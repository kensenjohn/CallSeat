package com.gs.bean.sms;

import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/20/13
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmsQueueBean {

    public SmsQueueBean()
    {

    }

    private String smsQueueId = "";
    private String fromTelNumber = "";
    private String toTelNumber = "";
    private String templateId = "";
    private String smsMessage = "";
    private String status = "";
    private String eventId = "";
    private String adminId = "";
    private String guestId = "";
    private Long createDate = 0L;
    private String humanCreateDate = "";
    private Long modifiedDate = 0L;
    private String humanModifiedDate = "";
    private String eventType  = "";
    public SmsQueueBean(HashMap<String,String> hmResult)
    {
        this.smsQueueId  = ParseUtil.checkNull( hmResult.get("SMSQUEUEID"));
        this.fromTelNumber = ParseUtil.checkNull( hmResult.get("FROM_TELNUMBER"));
        this.toTelNumber = ParseUtil.checkNull( hmResult.get("TO_TELNUMBER"));
        this.templateId = ParseUtil.checkNull( hmResult.get("FK_SMSTEMPLATEID"));
        this.smsMessage = ParseUtil.checkNull( hmResult.get("SMS_MESSAGE"));
        this.status = ParseUtil.checkNull( hmResult.get("STATUS"));
        this.eventId = ParseUtil.checkNull( hmResult.get("FK_EVENTID"));
        this.adminId = ParseUtil.checkNull( hmResult.get("FK_ADMINID"));
        this.guestId = ParseUtil.checkNull( hmResult.get("FK_GUESTID"));
        this.createDate = ParseUtil.sToL(hmResult.get("CREATEDATE"));
        this.humanCreateDate = ParseUtil.checkNull(hmResult.get("HUMANCREATEDATE"));
        this.modifiedDate = ParseUtil.sToL(hmResult.get("MODIFIEDDATE"));
        this.humanModifiedDate = ParseUtil.checkNull(hmResult.get("HUMANMODIFIEDDATE"));
        this.eventType =  ParseUtil.checkNull( hmResult.get("EVENT_TYPE"));
    }

    public String getSmsQueueId() {
        return smsQueueId;
    }

    public void setSmsQueueId(String smsQueueId) {
        this.smsQueueId = smsQueueId;
    }

    public String getFromTelNumber() {
        return fromTelNumber;
    }

    public void setFromTelNumber(String fromTelNumber) {
        this.fromTelNumber = fromTelNumber;
    }

    public String getToTelNumber() {
        return toTelNumber;
    }

    public void setToTelNumber(String toTelNumber) {
        this.toTelNumber = toTelNumber;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getHumanModifiedDate() {
        return humanModifiedDate;
    }

    public void setHumanModifiedDate(String humanModifiedDate) {
        this.humanModifiedDate = humanModifiedDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "SmsQueueBean{" +
                "smsQueueId='" + smsQueueId + '\'' +
                ", fromTelNumber='" + fromTelNumber + '\'' +
                ", toTelNumber='" + toTelNumber + '\'' +
                ", templateId='" + templateId + '\'' +
                ", smsMessage='" + smsMessage + '\'' +
                ", status='" + status + '\'' +
                ", eventId='" + eventId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", createDate=" + createDate +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", modifiedDate=" + modifiedDate +
                ", humanModifiedDate='" + humanModifiedDate + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
