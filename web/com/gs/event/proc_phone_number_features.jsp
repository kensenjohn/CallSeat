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
<%@ page import="com.google.i18n.phonenumbers.PhoneNumberUtil" %>
<%@ page import="com.google.i18n.phonenumbers.Phonenumber" %>
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
            String sRsvpCallForwardHumanNumber = ParseUtil.checkNull(request.getParameter("rsvp_call_forward"));
            boolean isRsvpSmsConfirmation = ParseUtil.sTob( request.getParameter("rsvp_sms_confirmation") );
            boolean isRsvpEmailConfirmation = ParseUtil.sTob( request.getParameter("rsvp_email_confirmation") );

            EventFeatureManager eventFeatureManager = new EventFeatureManager();
            appLogging.info("Editing Phone number features Action = " + sAction + " Admin:" + sAdminId);
            if(sAction!=null && "save".equalsIgnoreCase(sAction))
            {
                boolean isError = false;
                //Seating Forwarding Number
                EventFeatureBean seatingCallForwardEventFeatureBean =  eventFeatureManager.getEventFeatures(sEventId, Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER);
                String sSeatingCallForward = "";
                if(sSeatingCallForwardHumanNumber!=null&& !"".equalsIgnoreCase(sSeatingCallForwardHumanNumber))
                {
                    sSeatingCallForward = Utility.convertHumanToInternationalTelNum(sSeatingCallForwardHumanNumber);
                }
                com.google.i18n.phonenumbers.Phonenumber.PhoneNumber seatingPhoneNumber = new Phonenumber.PhoneNumber();
                seatingPhoneNumber.setCountryCode(1);
                seatingPhoneNumber.setNationalNumber(ParseUtil.sToL(sSeatingCallForward.substring(1)));
                PhoneNumberUtil seatingNumberUtil = PhoneNumberUtil.getInstance();
                if(!seatingNumberUtil.isValidNumber(seatingPhoneNumber))
                {
                    Text errorText = new ErrorText("We were unable to save the forwarding number for Seating. Please enter a valid Seating forward call number.<br>","cell_num") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                    isError = true;
                }

                //RSVP Forwarding Number
                EventFeatureBean rsvpCallForwardEventFeatureBean =  eventFeatureManager.getEventFeatures(sEventId, Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER);
                String sRsvpCallForward = "";
                if(sRsvpCallForwardHumanNumber!=null&& !"".equalsIgnoreCase(sRsvpCallForwardHumanNumber))
                {
                    sRsvpCallForward = Utility.convertHumanToInternationalTelNum(sRsvpCallForwardHumanNumber);
                }

                com.google.i18n.phonenumbers.Phonenumber.PhoneNumber rsvpPhoneNumber = new Phonenumber.PhoneNumber();
                rsvpPhoneNumber.setCountryCode(1);
                rsvpPhoneNumber.setNationalNumber(ParseUtil.sToL(sRsvpCallForward.substring(1)));
                PhoneNumberUtil rsvpNumberUtil = PhoneNumberUtil.getInstance();
                if(!rsvpNumberUtil.isValidNumber(rsvpPhoneNumber))
                {
                    Text errorText = new ErrorText("We were unable to save the forwarding number for RSVP. Please enter a valid RSVP forward call number.<br>","cell_num") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                    isError = true;
                }

                if(!isError)
                {
                    // Saving Seating Call Forward number
                    if(seatingCallForwardEventFeatureBean!=null && seatingCallForwardEventFeatureBean.getHmFeatureValue()!=null &&
                            !seatingCallForwardEventFeatureBean.getHmFeatureValue().isEmpty())
                    {
                        eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER,sSeatingCallForward);
                    }
                    else
                    {
                        eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER,sSeatingCallForward);
                    }

                    // Saving RSVP Call Forward number


                    if(rsvpCallForwardEventFeatureBean!=null && rsvpCallForwardEventFeatureBean.getHmFeatureValue()!=null &&
                            !rsvpCallForwardEventFeatureBean.getHmFeatureValue().isEmpty())
                    {
                        eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER,sRsvpCallForward);
                    }
                    else
                    {
                        eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER,sRsvpCallForward);
                    }

                    //Save Seating : Is Text/SMS to be sent to Guest after call
                    EventFeatureBean seatingIsSmsConfirmationBean = eventFeatureManager.getEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_SMS_GUEST_AFTER_CALL);

                    if(seatingIsSmsConfirmationBean!=null && seatingIsSmsConfirmationBean.getHmFeatureValue()!=null &&
                            !seatingIsSmsConfirmationBean.getHmFeatureValue().isEmpty())
                    {
                        eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_SMS_GUEST_AFTER_CALL,isSeatingSmsConfirmation?"true":"false");
                    }
                    else
                    {
                        eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_SMS_GUEST_AFTER_CALL,isSeatingSmsConfirmation?"true":"false");
                    }

                    //Save Seating : Is Email to be sent to Guest after call
                    EventFeatureBean seatingIsEmailConfirmationBean = eventFeatureManager.getEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_EMAIL_GUEST_AFTER_CALL);

                    if(seatingIsEmailConfirmationBean!=null && seatingIsEmailConfirmationBean.getHmFeatureValue()!=null &&
                            !seatingIsEmailConfirmationBean.getHmFeatureValue().isEmpty())
                    {
                        eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_EMAIL_GUEST_AFTER_CALL,isSeatingEmailConfirmation?"true":"false");
                    }
                    else
                    {
                        eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.SEATING_EMAIL_GUEST_AFTER_CALL,isSeatingEmailConfirmation?"true":"false");
                    }

                    //Save RSVP : Is Text/SMS confirmation to be sent to Guest
                    EventFeatureBean rsvpIsSmsConfirmationBean = eventFeatureManager.getEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_SMS_CONFIRMATION);

                    if(rsvpIsSmsConfirmationBean!=null && rsvpIsSmsConfirmationBean.getHmFeatureValue()!=null &&
                            !rsvpIsSmsConfirmationBean.getHmFeatureValue().isEmpty())
                    {
                        eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_SMS_CONFIRMATION,isRsvpSmsConfirmation?"true":"false");
                    }
                    else
                    {
                        eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_SMS_CONFIRMATION,isRsvpSmsConfirmation?"true":"false");
                    }

                    //Save RSVP : Is Email confirmation to be sent to Guest
                    EventFeatureBean rsvpIsEmailConfirmationBean = eventFeatureManager.getEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_EMAIL_CONFIRMATION);

                    if(rsvpIsEmailConfirmationBean!=null && rsvpIsEmailConfirmationBean.getHmFeatureValue()!=null &&
                            !rsvpIsEmailConfirmationBean.getHmFeatureValue().isEmpty())
                    {
                        eventFeatureManager.updateEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_EMAIL_CONFIRMATION,isRsvpEmailConfirmation?"true":"false");
                    }
                    else
                    {
                        eventFeatureManager.createEventFeatures(sEventId,Constants.EVENT_FEATURES.RSVP_EMAIL_CONFIRMATION,isRsvpEmailConfirmation?"true":"false");
                    }

                    Text okText = new OkText("Your changes were saved successfully.","err_mssg") ;
                    arrErrorText.add(okText);

                    responseStatus = RespConstants.Status.OK;
                }


            }
            else if(sAction!=null && "load".equalsIgnoreCase(sAction))
            {
                EventFeatureBean allEventFeatureBean =  eventFeatureManager.getEventFeatures(sEventId);

                jsonResponseObj.put("all_features",allEventFeatureBean.toJson());

                Text okText = new OkText("Loading data complete.","err_mssg") ;
                arrErrorText.add(okText);

                responseStatus = RespConstants.Status.OK;
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