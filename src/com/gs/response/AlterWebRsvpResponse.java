package com.gs.response;

import com.gs.bean.EventGuestBean;
import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.response.GuestWebResponseBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.bean.response.WebRespResponse;
import com.gs.common.*;
import com.gs.common.mail.EmailSchedulerData;
import com.gs.common.mail.MailingServiceData;
import com.gs.manager.event.EventGuestManager;
import com.gs.manager.event.EventGuestMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 7:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlterWebRsvpResponse {
    private static final Logger applLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    private static final Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    public WebRespResponse generateGuestWebResponseBean( WebRespRequest webRespRequest ) {
        WebRespResponse webRespResponse = new WebRespResponse();
        if(webRespRequest!=null && !Utility.isNullOrEmpty(webRespRequest.getEventId())) {
            ArrayList<EventGuestBean>  arrEventGuestWithNoRSVP = getGuestWhoDidNotRSVP( webRespRequest ) ;
            if(arrEventGuestWithNoRSVP!=null && !arrEventGuestWithNoRSVP.isEmpty()) {
                ArrayList<GuestWebResponseBean>  arrGuestWebResponse = new ArrayList<GuestWebResponseBean>();
                WebResponseData webResponseData = new WebResponseData();
                for(EventGuestBean eventGuestBean : arrEventGuestWithNoRSVP ) {
                    GuestWebResponseBean guestWebResponseBean = new GuestWebResponseBean();
                    guestWebResponseBean.setGuestWebResponseId( Utility.getNewGuid() );
                    guestWebResponseBean.setGuestWebResponseType(webRespRequest.getGuestWebResponseType());
                    guestWebResponseBean.setLinkId(Utility.getLinkId(Constants.SIZE_OF_RSVP_LINK_ID));
                    guestWebResponseBean.setLinkDomain( applicationConfig.get(Constants.DOMAIN) );
                    guestWebResponseBean.setGuestId(eventGuestBean.getGuestId());
                    guestWebResponseBean.setEventId( webRespRequest.getEventId() );
                    guestWebResponseBean.setCreateDate( DateSupport.getEpochMillis() );
                    guestWebResponseBean.setHumanCreateDate( DateSupport.getUTCDateTime() );
                    guestWebResponseBean.setResponseStatus( Constants.GUEST_WEB_RESPONSE_STATUS.NEW.name() );
                    guestWebResponseBean.setAdminId( webRespRequest.getAdminId() );
                    guestWebResponseBean.setResponseDate( 0L );
                    guestWebResponseBean.setHumanResponseDate( "" );
                    if(webResponseData.createGuestWebResponse(guestWebResponseBean) > 0 ) {
                        arrGuestWebResponse.add( guestWebResponseBean );
                    }
                }
                if(arrGuestWebResponse!=null && !arrGuestWebResponse.isEmpty()) {
                    webRespResponse.setSuccess(true);
                    webRespResponse.setArrGuestWebResponse( arrGuestWebResponse );

                }
            }
        }
        return webRespResponse;
    }

    public Integer updateGuestResponseStatus(  WebRespRequest webRespRequest , Constants.GUEST_WEB_RESPONSE_STATUS sResponseStatus ) {
        Integer iNumOfRows = 0;
        if(webRespRequest!=null && !Utility.isNullOrEmpty(webRespRequest.getEventId())
                && !Utility.isNullOrEmpty(webRespRequest.getGuestId())  && !Utility.isNullOrEmpty(webRespRequest.getLinkId())
                && sResponseStatus!=null) {
            GuestWebResponseBean guestWebResponseBean = new GuestWebResponseBean();
            guestWebResponseBean.setGuestId( webRespRequest.getGuestId() );
            guestWebResponseBean.setEventId( webRespRequest.getEventId() );
            guestWebResponseBean.setLinkId( webRespRequest.getLinkId() );
            guestWebResponseBean.setResponseStatus( sResponseStatus.name() );
            guestWebResponseBean.setResponseDate( DateSupport.getEpochMillis() );
            guestWebResponseBean.setHumanResponseDate( DateSupport.getUTCDateTime() );


            WebResponseData webResponseData = new WebResponseData();
            iNumOfRows = webResponseData.updateGuestWebResponse(guestWebResponseBean);
        } else {
            applLogging.info("Guest Response status has no data or invalid request sent. " );
        }
        return iNumOfRows;
    }

    private ArrayList<EventGuestBean>  getGuestWhoDidNotRSVP( WebRespRequest webRespRequest) {
        ArrayList<EventGuestBean>  arrEventGuestWithNoRSVP = new ArrayList<EventGuestBean>();
        if(webRespRequest!=null && !Utility.isNullOrEmpty(webRespRequest.getEventId())) {

            EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
            eventGuestMetaData.setEventId(ParseUtil.checkNull(webRespRequest.getEventId()));

            EventGuestManager eventGuestManager = new EventGuestManager();
            arrEventGuestWithNoRSVP = eventGuestManager.getGuestWhoDidNotRSVP(eventGuestMetaData);
            applLogging.info("Number of event Guest with no RSVP : " + arrEventGuestWithNoRSVP.size() + " Seating plan id : " + webRespRequest.getEventId() );
        } else {
            applLogging.info("Invalid request used or invalid event id used" );
        }
        return arrEventGuestWithNoRSVP;
    }
}
