<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.manager.event.EventManager"%>
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
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();

try
{
	if(sEventID==null || "".equalsIgnoreCase(sEventID))
	{

		Text errorText = new ErrorText("Invalid event id used to access information","my_id") ;		
		arrErrorText.add(errorText);		
		responseStatus = RespConstants.Status.ERROR;		
		appLogging.error("Invalid event Id used to retrieve event summary");
	}
	else if(sAdminId==null || "".equalsIgnoreCase(sAdminId))
	{
		Text errorText = new ErrorText("Invalid admin id used to access information","my_id") ;		
		arrErrorText.add(errorText);		
		responseStatus = RespConstants.Status.ERROR;		
		appLogging.error("Invalid admin Id used to retrieve event summary");
	}
	else
	{
		if(sEventID!=null && !"".equalsIgnoreCase(sEventID))
		{
			EventManager eventManager = new EventManager();
			EventSummaryBean eventSummaryBean  = eventManager.getEventSummary(sEventID,sAdminId);
			
			ArrayList<EventSummaryBean> arrEventBean = new ArrayList<EventSummaryBean>();
			arrEventBean.add(eventSummaryBean);
			
			jsonResponseObj.put("event_summary",eventSummaryBean.toJson());
			responseStatus = RespConstants.Status.OK;
			
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
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading table for event : " + sEventID + ExceptionHandler.getStackTrace(e));
	out.println(jsonResponseObj);
}
%>