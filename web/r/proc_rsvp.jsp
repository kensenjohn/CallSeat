<%@page import="com.gs.response.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@ page import="com.gs.common.ParseUtil" %>
<%@ page import="com.gs.bean.response.WebRespRequest" %>
<%@ page import="com.gs.common.Constants" %>
<%@ page import="com.gs.bean.response.GuestWebResponseBean" %>
<%@ page import="com.gs.bean.EventGuestBean" %>
<%@ page import="com.gs.manager.event.EventGuestManager" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();

try {
    String sLinkId = ParseUtil.checkNull(request.getParameter("link_id"));
    String sWillGuestAttend = ParseUtil.checkNull(request.getParameter("will_you_attend"));
    String sNumOfGuestSeats = ParseUtil.checkNull(request.getParameter("num_of_guests"));
    Integer iNumOfGuestSeats = ParseUtil.sToI(request.getParameter("num_of_guests"));
    boolean isError = false;

    if( sLinkId==null || "".equalsIgnoreCase(sLinkId) ) {
        Text errorText = new ErrorText("Please use a valid link id.","link_id") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
        isError = true;
    } else if (sWillGuestAttend==null || "".equalsIgnoreCase(sWillGuestAttend)) {
        Text errorText = new ErrorText("Please specify whether you will attend.","link_id") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
        isError = true;
    }  else if (sNumOfGuestSeats==null || "".equalsIgnoreCase(sNumOfGuestSeats)) {
        Text errorText = new ErrorText("Please select a valid guest number.","link_id") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
        isError = true;
    }
    if(!isError) {
        WebRespRequest  webRsvpRequestBean = new WebRespRequest();
        webRsvpRequestBean.setLinkId(sLinkId);
        webRsvpRequestBean.setGuestWebResponseType(Constants.GUEST_WEB_RESPONSE_TYPE.RSVP);

        ReadWebRsvpResponse readWebRsvpResponse = new ReadWebRsvpResponse();
        GuestWebResponseBean guestWebResponseBean = readWebRsvpResponse.isValidLinkId(webRsvpRequestBean);

        if(guestWebResponseBean!=null && !"".equalsIgnoreCase(guestWebResponseBean.getGuestWebResponseId())) {
            webRsvpRequestBean.setGuestId( ParseUtil.checkNull(guestWebResponseBean.getGuestId()) );
            webRsvpRequestBean.setEventId( ParseUtil.checkNull(guestWebResponseBean.getEventId()) );


            EventGuestBean eventGuestBean = readWebRsvpResponse.getEventGuestSeating(webRsvpRequestBean);
            if(eventGuestBean!=null && !"".equalsIgnoreCase(eventGuestBean.getEventGuestId())
                    && (ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats()) >= ParseUtil.iToI(iNumOfGuestSeats+1)) )  {
                if("yes".equalsIgnoreCase(sWillGuestAttend)){

                    eventGuestBean.setRsvpSeats( ParseUtil.iToI(iNumOfGuestSeats+1).toString() );
                } else if( "no".equalsIgnoreCase(sWillGuestAttend) ) {
                    eventGuestBean.setRsvpSeats( ParseUtil.iToI( 0 ).toString() );

                } else {
                    Text errorText = new ErrorText("Your RSVP could not be processed at this time. Please try again later.(1)","err_mssg") ;
                    arrErrorText.add(errorText);
                    appLogging.error("There was no RSVP response selected:  " + eventGuestBean );
                    responseStatus = RespConstants.Status.ERROR;
                    isError = true;
                }

                if(!isError) {
                    EventGuestManager eventGuestManager = new EventGuestManager();
                    Integer iNumOfEventGuestRecs = eventGuestManager.setGuestInviteRsvpForEvent( eventGuestBean );

                    Text okText = new OkText("Your RSVP has been updated.","rsvp_num_of_seats") ;
                    arrOkText.add(okText);

                    responseStatus = RespConstants.Status.OK;
                }
            } else {
                Text errorText = new ErrorText("Your RSVP could not be processed at this time. Please try again later.(3)","err_mssg") ;
                arrErrorText.add(errorText);

                appLogging.error("After updating the Guest RSVP eventGuestBean :  " + eventGuestBean );

                responseStatus = RespConstants.Status.ERROR;
                isError = true;
            }

        } else{
            Text errorText = new ErrorText("Your RSVP could not be processed at this time. Please try again later.(3)","err_mssg") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            isError = true;
        }
    }

    responseObject.setErrorMessages(arrErrorText);
    responseObject.setOkMessages(arrOkText);
    responseObject.setResponseStatus(responseStatus);
    responseObject.setJsonResponseObj(jsonResponseObj);

    out.println(responseObject.getJson());
}catch(Exception e ) {
    Text errorText = new ErrorText("We were unable to complete your request. Please try again later.","err_mssg") ;
    arrErrorText.add(errorText);

    responseStatus = RespConstants.Status.ERROR;

    responseObject.setErrorMessages(arrErrorText);
    responseObject.setOkMessages(arrOkText);
    responseObject.setResponseStatus(responseStatus);
    responseObject.setJsonResponseObj(jsonResponseObj);

    out.println(responseObject.getJson());
    appLogging.error("Error updating the Guest RSVP " + ExceptionHandler.getStackTrace(e) );
}
%>