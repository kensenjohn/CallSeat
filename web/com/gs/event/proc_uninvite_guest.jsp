<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
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

String sGuestId =  ParseUtil.checkNull(request.getParameter("guest_id"));
String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
String sGuestEventId =  ParseUtil.checkNull(request.getParameter("event_guest_id"));

try
{ 

	
	if(sGuestId!=null && !"".equalsIgnoreCase(sGuestId) && sEventId!=null && !"".equalsIgnoreCase(sEventId) 
			&& sGuestEventId!=null && !"".equalsIgnoreCase(sGuestEventId))
	{
		EventGuestMetaData eventGuestMeta = new EventGuestMetaData();
		eventGuestMeta.setEventGuestId(sGuestEventId);
		eventGuestMeta.setEventId(sEventId);
		eventGuestMeta.setGuestId(sGuestId);
		
		EventGuestManager eventGuestMan = new EventGuestManager();
		Integer iNumOfRecsDeleted = eventGuestMan.deleteGuestFromEvent(eventGuestMeta);
		
		GuestTableManager guestTableManager = new GuestTableManager();
		ArrayList<String> arrTableId = guestTableManager.getEventGuestTables(sGuestId , sEventId);
		
		if(arrTableId!=null && !arrTableId.isEmpty())
		{
			TableManager tableManager = new TableManager();
			Integer numOfEventTablesDel = tableManager.deleteGuestEventTable(arrTableId , sGuestId );
		}
		
		Text okText = new OkText("The guest was removed from the event's guest list.","success_mssg");		
		arrOkText.add(okText);
		responseStatus = RespConstants.Status.OK;
	}
	else
	{
		Text errorText = new ErrorText("Your request to uninvite this guest could not be processed. Please try again later.","err_mssg") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	

	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
appLogging.error("Error loading events for eventid : " + sEventId + ExceptionHandler.getStackTrace(e));
	
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading events for  eventid : " + sEventId);
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
%>