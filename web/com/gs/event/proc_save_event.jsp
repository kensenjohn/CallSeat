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
<%@ page import="org.joda.time.DateTimeZone" %>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@ page import="java.text.SimpleDateFormat" %>

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

try
{
	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
	String sEventName =  ParseUtil.checkNull(request.getParameter("e_summ_event_name"));
	String sEventDate =  ParseUtil.checkNull(request.getParameter("e_summ_event_date"));
    String sEventHour =  ParseUtil.checkNull(request.getParameter("e_summ_event_hour"));
    String sEventMin =  ParseUtil.checkNull(request.getParameter("e_summ_event_min"));
    String sEventAmPm =  ParseUtil.checkNull(request.getParameter("e_summ_event_ampm"));
    String sEventTimeZone =  ParseUtil.checkNull(request.getParameter("e_summ_event_timezone"));
    String sRsvpDeadlineDate =  ParseUtil.checkNull(request.getParameter("e_rsvp_deadline_date"));

	boolean  isCreateEvent =  ParseUtil.sTob(request.getParameter("create_event"));
	
	if(sEventName==null || "".equalsIgnoreCase(sEventName))
	{
		Text errorText = new ErrorText("*Required","e_summ_event_name") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else if( Utility.isNullOrEmpty(sEventDate) ||  Utility.isNullOrEmpty(sEventHour) ||  Utility.isNullOrEmpty(sEventMin) ||  Utility.isNullOrEmpty(sEventAmPm) ||  Utility.isNullOrEmpty(sEventTimeZone))
	{
		Text errorText = new ErrorText("*Required","e_summ_event_date") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else
	{
		responseStatus = RespConstants.Status.OK;
	}
	com.gs.manager.event.EventManager eventManager = new com.gs.manager.event.EventManager();
	
	if(responseStatus.equals(RespConstants.Status.OK))
	{
        Long lCurrentTime = DateSupport.getEpochMillis();

        String sEventDateTime =  sEventDate + " " + sEventHour +":" +sEventMin+":00 " + sEventAmPm;
        String sEventDateTimePattern = "MM/dd/yyyy hh:mm:ss a";

        String sRsvpDeadlineDateTime = sRsvpDeadlineDate + " 23:59:59";
        String sRsvpDeadlineDateTimePattern = "MM/dd/yyyy HH:mm:ss";

        DateObject eventDateObject = DateSupport.convertTime(sEventDateTime ,DateSupport.getTimeZone( sEventTimeZone ), sEventDateTimePattern , DateSupport.getTimeZone(Constants.DEFAULT_TIMEZONE) , Constants.DATE_PATTERN_TZ  );

        Long lEventCreateDate = eventDateObject.getMillis();
		if(isCreateEvent)
		{
			if(sEventName==null || "".equalsIgnoreCase(sEventName))
			{
				Text errorText = new ErrorText("We were unable to recognize the Admin. Please log in again.","e_summ_event_name") ;
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			} else {


                Long lFutureDateLimit = DateSupport.addTime( lCurrentTime , 1 , Constants.TIME_UNIT.YEARS  );
                if( lEventCreateDate < lCurrentTime )
                {
                    Text errorText = new ErrorText("We were unable to create a seating plan for the selected date. Please select a date in the future.","e_summ_event_name") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }
                else if (lEventCreateDate  > lFutureDateLimit )
                {
                    Text errorText = new ErrorText("We were unable to create a seating plan for the selected date. Please select a date within a year from today.","e_summ_event_name") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }
                else
                {
                    AdminManager adminManager = new AdminManager();
                    if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
                    {
                        AdminBean adminBean = adminManager.getAdmin(sAdminId);

                        EventCreationMetaDataBean eventMeta = new EventCreationMetaDataBean();
                        eventMeta.setAdminBean(adminBean);
                        eventMeta.setEventDate(sEventDateTime);
                        eventMeta.setEventDatePattern(sEventDateTimePattern);
                        eventMeta.setEventTimeZone(sEventTimeZone);
                        eventMeta.setEventName(sEventName);
                        eventMeta.setRsvpDeadlineDate(sRsvpDeadlineDateTime);
                        eventMeta.setRsvpDeadlineDateDatePattern( sRsvpDeadlineDateTimePattern );
                        eventMeta.setCreateEvent( true );

                        EventBean eventBean = eventManager.createEvent(eventMeta);


                        if(eventBean!=null && eventBean.getEventId()!=null && !"".equalsIgnoreCase(eventBean.getEventId()))
                        {
                            EventPricingGroupManager eventPricingManager = new EventPricingGroupManager();
                            ArrayList<PricingGroupBean> arrPricingBean = eventPricingManager.getDemoPricingGroups();
                            appLogging.info("Demo Pricing Bean selected " + arrPricingBean );
                            if( arrPricingBean !=null )
                            {
                                for(PricingGroupBean pricingGroupBean : arrPricingBean )
                                {
                                    if(pricingGroupBean!=null)
                                    {
                                        EventFeatureManager eventFeatureManager = new EventFeatureManager();
                                        eventFeatureManager.createEventFeatures(eventBean.getEventId(), Constants.EVENT_FEATURES.DEMO_TOTAL_CALL_MINUTES,ParseUtil.iToS(pricingGroupBean.getMaxMinutes()));
                                        eventFeatureManager.createEventFeatures(eventBean.getEventId(), Constants.EVENT_FEATURES.DEMO_TOTAL_TEXT_MESSAGES,ParseUtil.iToS(pricingGroupBean.getSmsCount()));
                                        break;
                                    }

                                }
                            }
                            TelNumberManager telNumberManager = new TelNumberManager();
                            telNumberManager.setEventDemoNumber(eventBean.getEventId(),adminBean.getAdminId());


                            responseStatus = RespConstants.Status.OK;

                            jsonResponseObj.put("event_bean",eventBean.toJson());
                            jsonResponseObj.put("create_event",true);
                        }
                        else
                        {
                            Text errorText = new ErrorText("New event was not created. Please try again later.","e_summ_event_name") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    }
                }
			}
			
		}
		else
		{
			if(sEventId!=null && !"".equalsIgnoreCase(sEventId)){

                Long lFutureDateLimit = 0L;

                TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
                telNumberMetaData.setEventId(sEventId);
                telNumberMetaData.setAdminId(sAdminId);
                TelNumberManager telNumManager = new TelNumberManager();
                ArrayList<TelNumberBean> arrTelNumberBean = telNumManager.getTelNumbersByEvent(telNumberMetaData);
                if (arrTelNumberBean != null && !arrTelNumberBean.isEmpty()) {
                    for (TelNumberBean telNumberBean : arrTelNumberBean) {
                        if (Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())
                                || Constants.EVENT_TASK.SEATING.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) )
                        {
                            PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
                            purchaseTransactionBean.setAdminId(sAdminId);
                            purchaseTransactionBean.setEventId(sEventId);

                            PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
                            PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);

                            if(purchaseResponseTransactionBean!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getPurchaseTransactionId())) {
                                lEventCreateDate = purchaseResponseTransactionBean.getCreateDate();
                                lFutureDateLimit = DateSupport.addTime( lEventCreateDate , 1 , Constants.TIME_UNIT.YEARS  );
                                break;
                            }
                        }
                        else if ( Constants.EVENT_TASK.DEMO_RSVP.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType())
                                    || Constants.EVENT_TASK.DEMO_SEATING.getTask().equalsIgnoreCase(telNumberBean.getTelNumberType()) )
                        {
                            EventBean eventBean = eventManager.getEvent(sEventId);
                            if(eventBean!=null)
                            {
                                lEventCreateDate = eventDateObject.getMillis();
                                lFutureDateLimit = DateSupport.addTime( lEventCreateDate , 1 , Constants.TIME_UNIT.YEARS  );
                                break;
                            }
                        }
                    }
                }

                if( lEventCreateDate < lCurrentTime )
                {
                    Text errorText = new ErrorText("We were unable to create a seating plan for the selected date. Please select a date in the future.","e_summ_event_name") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }
                else if (lEventCreateDate  > lFutureDateLimit  )
                {
                    Text errorText = new ErrorText("We were unable to create a seating plan for the selected date. The date must be before " +
                            DateSupport.getTimeByZone( lFutureDateLimit,DateTimeZone.UTC.getID(),Constants.PRETTY_DATE_PATTERN_2 )+ ".","e_summ_event_name") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }
                else
                {
                    AdminManager adminManager = new AdminManager();
                    AdminBean adminBean = adminManager.getAdmin(sAdminId);

                    EventCreationMetaDataBean eventMeta = new EventCreationMetaDataBean();
                    eventMeta.setEventId(sEventId );
                    eventMeta.setAdminBean(adminBean);
                    eventMeta.setEventDate(sEventDateTime);
                    eventMeta.setEventDatePattern(sEventDateTimePattern);
                    eventMeta.setEventTimeZone(sEventTimeZone);
                    eventMeta.setEventName(sEventName);
                    eventMeta.setRsvpDeadlineDate(sRsvpDeadlineDateTime);
                    eventMeta.setRsvpDeadlineDateDatePattern( sRsvpDeadlineDateTimePattern );
                    eventMeta.setUpdateEvent( true );

                    Integer iNumOfRowsUpdated = eventManager.updateEvent(eventMeta);
                    if(iNumOfRowsUpdated<1)
                    {
                        Text errorText = new ErrorText("Oops!! Please try again later.","e_summ_event_date") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }
                    else
                    {

                        jsonResponseObj.put("update_event",true);
                        responseStatus = RespConstants.Status.OK;
                    }
                }
			}
			else
			{
				Text errorText = new ErrorText("Error processing request.","my_id") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
		}
	}
	else
	{
		appLogging.error("Action isCreateEvent = " +isCreateEvent + "invoked : error detected event id : " + sEventId + " - " + arrErrorText);
	}

	appLogging.info("Action isCreateEvent = " +isCreateEvent + "invoked : event id : " + sEventId );
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error saving event data\n" + e.getMessage() + "\n" + ExceptionHandler.getStackTrace(e) );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
}
%>