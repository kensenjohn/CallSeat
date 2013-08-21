package com.gs.bean.response;

import com.gs.common.Constants;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 7:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebRespResponse {
    private String responseId = Constants.EMPTY;
    private String guestId = Constants.EMPTY;
    private String eventId = Constants.EMPTY;
    private boolean isSuccess = false;
    private String message =  Constants.EMPTY;
    private ArrayList<GuestWebResponseBean> arrGuestWebResponse = new ArrayList<GuestWebResponseBean>();

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

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

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<GuestWebResponseBean> getArrGuestWebResponse() {
        return arrGuestWebResponse;
    }

    public void setArrGuestWebResponse(ArrayList<GuestWebResponseBean> arrGuestWebResponse) {
        this.arrGuestWebResponse = arrGuestWebResponse;
    }

    @Override
    public String toString() {
        return "WebRsvpResponseBean{" +
                "responseId='" + responseId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", isSuccess=" + isSuccess +
                ", message='" + message + '\'' +
                '}';
    }
}
