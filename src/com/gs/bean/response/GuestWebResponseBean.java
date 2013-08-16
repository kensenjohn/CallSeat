package com.gs.bean.response;

import com.gs.common.Constants;
import com.gs.common.ParseUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class GuestWebResponseBean {
    private String guestWebResponseId = Constants.EMPTY;
    private Constants.GUEST_WEB_RESPONSE_TYPE guestWebResponseType = Constants.GUEST_WEB_RESPONSE_TYPE.NONE;
    private String linkId = Constants.EMPTY;
    private String linkDomain = Constants.EMPTY;
    private String guestId = Constants.EMPTY;
    private String eventId = Constants.EMPTY;
    private long createDate = 0L;
    private String humanCreateDate = Constants.EMPTY;
    private String responseStatus = Constants.EMPTY;
    private String adminId = Constants.EMPTY;
    private long responseDate = 0L;
    private String humanResponseDate = Constants.EMPTY;

    public GuestWebResponseBean() {

    }

    public GuestWebResponseBean(HashMap<String,String> hmWebResponseResult) {
        this.guestWebResponseId = ParseUtil.checkNull(hmWebResponseResult.get("GUESTWEBRESPONSEID")) ;
        String sGuestWebResponseType  = ParseUtil.checkNull(hmWebResponseResult.get("WEB_RESPONSE_TYPE")) ;
        if(Constants.GUEST_WEB_RESPONSE_TYPE.RSVP.name().equalsIgnoreCase(sGuestWebResponseType)) {
            this.guestWebResponseType = Constants.GUEST_WEB_RESPONSE_TYPE.RSVP;
        }
        this.linkId = ParseUtil.checkNull(hmWebResponseResult.get("LINKID")) ;
        this.linkDomain = ParseUtil.checkNull(hmWebResponseResult.get("LINK_DOMAIN")) ;
        this.guestId = ParseUtil.checkNull(hmWebResponseResult.get("FK_GUESTID")) ;
        this.eventId = ParseUtil.checkNull(hmWebResponseResult.get("FK_EVENTID")) ;
        this.createDate = ParseUtil.sToL(hmWebResponseResult.get("CREATEDATE"));
        this.humanCreateDate = ParseUtil.checkNull(hmWebResponseResult.get("HUMANCREATEDATE")) ;
        this.responseStatus = ParseUtil.checkNull(hmWebResponseResult.get("RESPONSE_STATUS")) ;
        this.adminId = ParseUtil.checkNull(hmWebResponseResult.get("FK_ADMINID")) ;
        this.responseDate = ParseUtil.sToL(hmWebResponseResult.get("RESPONSEDATE"));
        this.humanResponseDate = ParseUtil.checkNull(hmWebResponseResult.get("HUMANRESPONSEDATE")) ;
    }
    public String getGuestWebResponseId() {
        return guestWebResponseId;
    }

    public void setGuestWebResponseId(String guestWebResponseId) {
        this.guestWebResponseId = guestWebResponseId;
    }

    public Constants.GUEST_WEB_RESPONSE_TYPE getGuestWebResponseType() {
        return guestWebResponseType;
    }

    public void setGuestWebResponseType(Constants.GUEST_WEB_RESPONSE_TYPE guestWebResponseType) {
        this.guestWebResponseType = guestWebResponseType;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkDomain() {
        return linkDomain;
    }

    public void setLinkDomain(String linkDomain) {
        this.linkDomain = linkDomain;
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getHumanCreateDate() {
        return humanCreateDate;
    }

    public void setHumanCreateDate(String humanCreateDate) {
        this.humanCreateDate = humanCreateDate;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public long getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(long responseDate) {
        this.responseDate = responseDate;
    }

    public String getHumanResponseDate() {
        return humanResponseDate;
    }

    public void setHumanResponseDate(String humanResponseDate) {
        this.humanResponseDate = humanResponseDate;
    }

    @Override
    public String toString() {
        return "GuestWebResponseBean{" +
                "guestWebResponseId='" + guestWebResponseId + '\'' +
                ", guestWebResponseType=" + guestWebResponseType +
                ", linkId='" + linkId + '\'' +
                ", linkDomain='" + linkDomain + '\'' +
                ", guestId='" + guestId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", createDate=" + createDate +
                ", humanCreateDate='" + humanCreateDate + '\'' +
                ", responseStatus='" + responseStatus + '\'' +
                ", adminId='" + adminId + '\'' +
                ", responseDate=" + responseDate +
                ", humanResponseDate='" + humanResponseDate + '\'' +
                '}';
    }
}
