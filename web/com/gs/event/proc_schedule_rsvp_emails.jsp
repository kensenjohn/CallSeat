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
<%@ page import="com.gs.bean.TelNumberBean" %>
<%@ page import="com.gs.common.Constants" %>
<%@ page import="com.gs.bean.email.EmailSchedulerRequest" %>
<%@ page import="com.gs.common.mail.EmailSchedulerService" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);
Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();

try {
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId) )  {
        TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
        telNumberMetaData.setAdminId(sAdminId);
        telNumberMetaData.setEventId(sEventId);



        EmailSchedulerRequest emailSchedulerRequest = new EmailSchedulerRequest();
        emailSchedulerRequest.setEventId( sEventId );
        emailSchedulerRequest.setAdminId( sAdminId );
        emailSchedulerRequest.setUpdateScheduleIfExists( true );

        TelNumberManager telNumManager = new TelNumberManager();
        ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumEventDetails(telNumberMetaData);

        if(arrTelNumberBean!=null && !arrTelNumberBean.isEmpty()){
            for(TelNumberBean telNumberBean : arrTelNumberBean ) {
                if( Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()))  {
                    emailSchedulerRequest.setEmailTemplate(Constants.EMAIL_TEMPLATE.RSVPRESPONSE);
                } else if ( Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) ) {
                    emailSchedulerRequest.setEmailTemplate(Constants.EMAIL_TEMPLATE.RSVPRESPONSEDEMO);
                }
            }
        }

        if(emailSchedulerRequest.getEmailTemplate()!=null) {
            EmailSchedulerService emailSchedulerService = new EmailSchedulerService();
            Integer iNumOfRows = emailSchedulerService.createEmailSchedule( emailSchedulerRequest );
            if(iNumOfRows>0) {
                Text okText = new OkText("An email will be sent you to your guest within the next 15 minutes. The email will contain a link for your guests to RSVP.","my_id");
                arrOkText.add(okText);
                responseStatus = RespConstants.Status.OK;
            } else {
                Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.(1)","my_id") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
                jspLogging.error("The request to create the schedule failed." );
            }

        } else {
            Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            jspLogging.error("Template does not exist Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
        }

    } else {
        Text errorText = new ErrorText("Oops!! Your request could not be processed at this time. Please try again later.","my_id") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
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
    appLogging.error("Error scheduling rsvp emails" + ExceptionHandler.getStackTrace(e) );
}
%>