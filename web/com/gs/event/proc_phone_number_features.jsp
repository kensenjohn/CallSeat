<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.manager.event.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%
    JSONObject jsonResponseObj = new JSONObject();

    Logger jspLogging = LoggerFactory.getLogger("JspLogging");
    Logger appLogging = LoggerFactory.getLogger("AppLogging");
    response.setContentType("application/json");

    ArrayList<Text> arrOkText = new ArrayList<Text>();
    ArrayList<Text> arrErrorText = new ArrayList<Text>();
    RespConstants.Status responseStatus = RespConstants.Status.ERROR;

    RespObjectProc responseObject = new RespObjectProc();
        try
        {
            String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
            String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
            String sAction = ParseUtil.checkNull(request.getParameter("action"));
            String sSeatingCallForwardHumanNumber = ParseUtil.checkNull(request.getParameter("seating_call_forward"));
            boolean isSeatingSmsConfirmation = ParseUtil.sTob( request.getParameter("seating_sms_confirmation") );
            boolean isSeatingEmailConfirmation = ParseUtil.sTob( request.getParameter("seating_email_confirmation") );
            String sRsvpCallForward = ParseUtil.checkNull(request.getParameter("rsvp_call_forward"));
            boolean isRsvpSmsConfirmation = ParseUtil.sTob( request.getParameter("rsvp_sms_confirmation") );
            boolean isRsvpEmailConfirmation = ParseUtil.sTob( request.getParameter("rsvp_email_confirmation") );

            EventFeatureManager eventFeatureManager = new EventFeatureManager();
            appLogging.info("Action = " + sAction + " ");
            if(sAction!=null && "save".equalsIgnoreCase(sAction))
            {

                EventFeatureBean seatingCallForwardEventFeatureBean =  eventFeatureManager.getEventFeatures(sEventId, Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER);

                String sSeatingCallForward = "";
                if(sSeatingCallForwardHumanNumber!=null&& !"".equalsIgnoreCase(sSeatingCallForwardHumanNumber))
                {
                    sSeatingCallForward = Utility.convertHumanToInternationalTelNum(sSeatingCallForwardHumanNumber);
                }

                if(seatingCallForwardEventFeatureBean!=null && seatingCallForwardEventFeatureBean.getHmFeatureValue()!=null &&
                        !seatingCallForwardEventFeatureBean.getHmFeatureValue().isEmpty())
                {
                    eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER,sSeatingCallForward);
                }
                else
                {
                    eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER,sSeatingCallForward);
                }
            }


            responseObject.setErrorMessages(arrErrorText);
            responseObject.setOkMessages(arrOkText);
            responseObject.setResponseStatus(responseStatus);
            responseObject.setJsonResponseObj(jsonResponseObj);

            out.println(responseObject.getJson());
        }
        catch(Exception e)
        {
            //jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
            appLogging.error("Error processing phone number features " + ExceptionHandler.getStackTrace(e) );

            Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;
            arrErrorText.add(errorText);

            responseObject.setErrorMessages(arrErrorText);
            responseObject.setResponseStatus(RespConstants.Status.ERROR);
            responseObject.setJsonResponseObj(jsonResponseObj);

            out.println(responseObject.getJson());

        }
%>