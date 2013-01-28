package com.gs.bean;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/26/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CallTransactionBean {
    private static String QUERY_UPDATE_TRANSACTION_CALLS = "UPDATE GTCALLTRANSACTION SET " +
            " TELCOM_SERVICE_BILL_DURATION = ? , TELCOM_SERVICE_ACTUAL_DURATION  = ? , GUEST_TELNUMBER  = ? , " +
            " TO_TELNUMBER  = ? , CALL_TYPE  = ? , SECRET_EVENT_NUMBER  = ? , SECRET_EVENT_KEY  = ? , FK_GUESTID  = ? ," +
            " FK_EVENTID  = ? , FK_ADMINID  = ? , CALL_CREATEDATE  = ? , HUMAN_CALL_CREATEDATE  = ? , CALL_ENDDATE  = ? ," +
            " HUMAN_CALL_ENDDATE  = ? , CALL_STATUS  = ? ) " +
            "  WHERE TELCOM_SERVICE_ACC_SID = ? AND TELCOM_SERVICE_CALL_SID = ? ";

    private String guestId = "";
    private String eventId = "";
    private String adminId = "";
    private String callPlanType = "";

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
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

    public String getCallPlanType() {
        return callPlanType;
    }

    public void setCallPlanType(String callPlanType) {
        this.callPlanType = callPlanType;
    }
}
