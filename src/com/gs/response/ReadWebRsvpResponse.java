package com.gs.response;

import com.gs.bean.EventBean;
import com.gs.bean.EventGuestBean;
import com.gs.bean.GuestBean;
import com.gs.bean.response.GuestWebResponseBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.common.ParseUtil;
import com.gs.manager.GuestManager;
import com.gs.manager.event.EventGuestManager;
import com.gs.manager.event.EventGuestMetaData;
import com.gs.manager.event.EventManager;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 7:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReadWebRsvpResponse {
    public GuestWebResponseBean isValidLinkId(WebRespRequest webRsvpRequestBean ) {
        GuestWebResponseBean guestWebResponseBean = new GuestWebResponseBean();
        if(webRsvpRequestBean!=null && webRsvpRequestBean.getLinkId()!=null && !"".equalsIgnoreCase(webRsvpRequestBean.getLinkId())) {
            WebResponseData webResponseData = new WebResponseData();
            guestWebResponseBean = webResponseData.getGuestWebResponse(webRsvpRequestBean);
        }
        return guestWebResponseBean;
    }

    public GuestBean getGuestFromLink(WebRespRequest webRsvpRequestBean) {
        GuestBean guestBean = new GuestBean();
        if(webRsvpRequestBean!=null && !"".equalsIgnoreCase(webRsvpRequestBean.getGuestId()) )  {
            GuestManager guestManager = new GuestManager();
            guestBean = guestManager.getGuest(ParseUtil.checkNull(webRsvpRequestBean.getGuestId()));
        }
        return guestBean;
    }
    public EventBean getEventFromLink(WebRespRequest webRsvpRequestBean) {
        EventBean eventBean = new EventBean();
        if(webRsvpRequestBean!=null && !"".equalsIgnoreCase(webRsvpRequestBean.getEventId()) )  {
            EventManager eventManager = new EventManager();
            eventBean = eventManager.getEvent(webRsvpRequestBean.getEventId());
        }
        return eventBean;
    }

    public EventGuestBean getEventGuestSeating( WebRespRequest webRsvpRequestBean ) {
        EventGuestBean eventGuestBean = new EventGuestBean();
        if(webRsvpRequestBean!=null && !"".equalsIgnoreCase(webRsvpRequestBean.getGuestId()) && !"".equalsIgnoreCase(webRsvpRequestBean.getEventId()) ){
            EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
            eventGuestMetaData.setEventId(ParseUtil.checkNull(webRsvpRequestBean.getEventId()));

            ArrayList<String> arrGuestId = new ArrayList<String>();
            arrGuestId.add(ParseUtil.checkNull(webRsvpRequestBean.getGuestId()));
            eventGuestMetaData.setArrGuestId(arrGuestId);

            EventGuestManager eventGuestManager = new EventGuestManager();
            eventGuestBean = eventGuestManager.getGuest(eventGuestMetaData);
        }

        return eventGuestBean;
    }

}
