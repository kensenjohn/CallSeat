package com.gs.response;

import com.gs.bean.email.EmailScheduleBean;
import com.gs.bean.email.EmailTemplateBean;
import com.gs.bean.response.WebRespRequest;
import com.gs.bean.response.WebRespResponse;
import com.gs.common.*;
import com.gs.common.mail.EmailSchedulerData;
import com.gs.common.mail.MailingServiceData;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 8/15/13
 * Time: 7:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateWebRsvpResponse {
    public WebRespResponse generateGuestWebResponseBean( WebRespRequest webRespRequest ) {
        WebRespResponse webRespResponse = new WebRespResponse();
        if(webRespRequest!=null && !Utility.isNullOrEmpty(webRespRequest.getEventId())) {
            String sEventId = ParseUtil.checkNull(webRespRequest.getEventId());

        }
        return webRespResponse;
    }
}
