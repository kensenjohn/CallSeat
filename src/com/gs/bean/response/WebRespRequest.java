package com.gs.bean.response;

import com.gs.common.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 7:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebRespRequest {
    private String linkId = Constants.EMPTY;
    private String guestId = Constants.EMPTY;
    private String eventId = Constants.EMPTY;
    private Constants.GUEST_WEB_RESPONSE_TYPE guestWebResponseType = Constants.GUEST_WEB_RESPONSE_TYPE.NONE;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public Constants.GUEST_WEB_RESPONSE_TYPE getGuestWebResponseType() {
        return guestWebResponseType;
    }

    public void setGuestWebResponseType(Constants.GUEST_WEB_RESPONSE_TYPE guestWebResponseType) {
        this.guestWebResponseType = guestWebResponseType;
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

    @Override
    public String toString() {
        return "WebRespRequest{" +
                "linkId='" + linkId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", guestWebResponseType=" + guestWebResponseType +
                '}';
    }
}
