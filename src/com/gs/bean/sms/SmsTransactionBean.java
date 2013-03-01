package com.gs.bean.sms;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/25/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmsTransactionBean {
    /*
    +------------------------+--------------+------+-----+---------+-------+
    | Field                  | Type         | Null | Key | Default | Extra |
    +------------------------+--------------+------+-----+---------+-------+
    | SMSTRANSACTIONID       | varchar(45)  | NO   | PRI | NULL    |       |
    | TELCOM_SERVICE_ACC_SID | varchar(45)  | YES  |     | NULL    |       |
    | TELCOM_SERVICE_SMS_SID | varchar(45)  | YES  |     | NULL    |       |
    | FROM_TELNUMBER         | varchar(45)  | NO   |     | NULL    |       |
    | TO_TELNUMBER           | varchar(45)  | NO   |     | NULL    |       |
    | SMS_MESSAGE            | varchar(160) | YES  |     | NULL    |       |
    | SMS_STATUS             | varchar(45)  | YES  |     | NULL    |       |
    | SECRET_EVENT_NUMBER    | varchar(20)  | YES  |     | NULL    |       |
    | SECRET_EVENT_KEY       | varchar(20)  | YES  |     | NULL    |       |
    | FK_EVENTID             | varchar(45)  | YES  |     | NULL    |       |
    | FK_ADMINID             | varchar(45)  | YES  |     | NULL    |       |
    | FK_GUESTID             | varchar(45)  | YES  |     | NULL    |       |
    | CREATEDATE             | bigint(20)   | NO   |     | 0       |       |
    | HUMANCREATEDATE        | varchar(45)  | YES  |     | NULL    |       |
    | EVENT_TYPE             | varchar(45)  | YES  |     | NULL    |       |
    +------------------------+--------------+------+-----+---------+-------+
     */

    private String smsTransactionId = "";
    private String telecomServiceAccSID = "";
    private String telecomServiceSmsSID = "";
    private String fromNumber = "";
    private String toNumber = "";
    private String smsMessage = "";
    private String secretEventNumber = "";
    private String secretEventKey = "";
    private String smsStatus = "";
    private String adminId = "";
    private String eventId = "";
    private String guestId = "";
    private Long createDate = 0L;
    private String humanCreateDate = "";
    private String eventType = "";

    public String getSmsTransactionId() {
        return smsTransactionId;
    }

    public void setSmsTransactionId(String smsTransactionId) {
        this.smsTransactionId = smsTransactionId;
    }

    public String getTelecomServiceAccSID() {
        return telecomServiceAccSID;
    }

    public void setTelecomServiceAccSID(String telecomServiceAccSID) {
        this.telecomServiceAccSID = telecomServiceAccSID;
    }

    public String getTelecomServiceSmsSID() {
        return telecomServiceSmsSID;
    }

    public void setTelecomServiceSmsSID(String telecomServiceSmsSID) {
        this.telecomServiceSmsSID = telecomServiceSmsSID;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getSecretEventNumber() {
        return secretEventNumber;
    }

    public void setSecretEventNumber(String secretEventNumber) {
        this.secretEventNumber = secretEventNumber;
    }

    public String getSecretEventKey() {
        return secretEventKey;
    }

    public void setSecretEventKey(String secretEventKey) {
        this.secretEventKey = secretEventKey;
    }

    @Override
    public String toString() {
        return "SmsTransactionBean{" +
                "smsTransactionId='" + smsTransactionId + '\'' +
                ", telecomServiceAccSID='" + telecomServiceAccSID + '\'' +
                ", telecomServiceSmsSID='" + telecomServiceSmsSID + '\'' +
                ", fromNumber='" + fromNumber + '\'' +
                ", toNumber='" + toNumber + '\'' +
                ", smsMessage='" + smsMessage + '\'' +
                ", smsStatus='" + smsStatus + '\'' +
                ", secretEventNumber='" + secretEventNumber + '\'' +
                ", secretEventKey='" + secretEventKey + '\'' +
                ", smsStatus='" + smsStatus + '\'' +
                ", adminId='" + adminId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", createDate=" + createDate +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
