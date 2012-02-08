<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@page import="com.gs.json.*"%>

<%
JSONObject jsonResponseObj = new JSONObject();
String sEventID = ParseUtil.checkNull(request.getParameter("event_id"));
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
boolean isEventSpecific = ParseUtil.sTob(request.getParameter("for_event"));
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");


ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();


try
{
	if(isEventSpecific)
	{
		if(sEventID!=null && !"".equalsIgnoreCase(sEventID))
		{
			EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
			eventGuestMetaData.setEventId(sEventID);
			
			EventGuestManager eventGuestManager = new EventGuestManager();
			ArrayList<EventGuestBean> arrEventGuestBean = eventGuestManager.getGuestsByEvent(eventGuestMetaData);
			
			HashMap<String, ArrayList<EventGuestBean>>  hmEventGuestBean = new HashMap<String, ArrayList<EventGuestBean>>();
			hmEventGuestBean.put(sEventID,arrEventGuestBean);
			

			jsonResponseObj.put("event_guest_rows",eventGuestManager.getEventGuestsJson(hmEventGuestBean));
			
			responseStatus = RespConstants.Status.OK;
		}
		else
		{
			Text errorText = new ErrorText("Invalid event id used to access guest data","my_id") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			
			appLogging.error("Invalid event Id used to retrieve table details");
		}
	}
	else
	{
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
		{
			GuestManager guestManager = new GuestManager();
			ArrayList<GuestBean> arrGuestBean = guestManager.getGuestsByAdmin(sAdminId);
			appLogging.info( "Guest = " + arrGuestBean );
			
			EventGuestManager eventGuestManager = new EventGuestManager();
			HashMap<String, ArrayList<EventGuestBean>>  hmEventGuestBean = eventGuestManager.getEventGuests(arrGuestBean);
			
			jsonResponseObj.put("guest_rows",guestManager.getGuestsJson(arrGuestBean));
			jsonResponseObj.put("event_guest_rows",eventGuestManager.getEventGuestsJson(hmEventGuestBean));
			jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
			
			responseStatus = RespConstants.Status.OK;
		}
		else
		{
			Text errorText = new ErrorText("Invalid admin id used to access guest data","my_id") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			
			appLogging.error("Invalid admin Id used to retrieve table details");
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
		
	appLogging.error("Error loading guest for event : " + sEventID + ExceptionHandler.getStackTrace(e));
	

	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
%>