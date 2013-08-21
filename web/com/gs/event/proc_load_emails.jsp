<%@page import="com.gs.response.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@ page import="com.gs.common.ParseUtil" %>
<%@ page import="com.gs.common.mail.MailingServiceData" %>
<%@ page import="com.gs.bean.email.EmailTemplateBean" %>
<%@ page import="com.gs.manager.event.TelNumberMetaData" %>
<%@ page import="com.gs.manager.event.TelNumberManager" %>
<%@ page import="com.gs.manager.AdminManager" %>
<%@ page import="com.gs.bean.response.WebRespRequest" %>
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
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));

    if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId) )  {

        WebRespRequest webRespRequest = new WebRespRequest();
        webRespRequest.setAdminId(sAdminId);
        webRespRequest.setEventId(sEventId);

        AdminManager adminManager = new AdminManager();
        EmailTemplateBean guestResponseEmailTemplate = adminManager.getFormattedRSVPResponseEmail( webRespRequest );

        if(guestResponseEmailTemplate!=null && !"".equalsIgnoreCase(guestResponseEmailTemplate.getEmailTemplateId())) {
            jsonResponseObj.put("rsvp_response_email",  guestResponseEmailTemplate.toJson());

            Text okText = new OkText("RSVP response was retrieved.","my_id");
            arrOkText.add(okText);
            responseStatus = RespConstants.Status.OK;
        } else {
            Text errorText = new ErrorText("There is currently no RSVP email setup. Please contact your support representative","my_id") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            jspLogging.error("Email Template for RSVP Response empty Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
        }
    }else  {
        Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
        //jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
        jspLogging.error("Missing paramters in request Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
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
    appLogging.error("Error loading emails" + ExceptionHandler.getStackTrace(e) );
}
%>