<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@page import="com.gs.json.*"%>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
JSONObject jsonResponseObj = new JSONObject();
String sAdminID = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventID = ParseUtil.checkNull(request.getParameter("event_id"));
boolean isSingleEvent = ParseUtil.sTob(request.getParameter("single_event"));
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();

try
{
	if(isSingleEvent)
	{
		if(sEventID!=null && !"".equalsIgnoreCase(sEventID))
		{
			EventManager eventManager = new EventManager();
			EventBean eventBean = eventManager.getEvent(sEventID);
			
			ArrayList<EventBean> arrEventBean = new ArrayList<EventBean>();
			arrEventBean.add(eventBean);
			jsonResponseObj.put("event_detail",eventManager.getEventJson(arrEventBean));

			
			Text okText = new OkText("All events retrieved for the admin.","my_id");		
			arrOkText.add(okText);
			responseStatus = RespConstants.Status.OK;
		}
		else
		{
			Text errorText = new ErrorText("Could not load single event details.","my_id") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			appLogging.error("Invalid Event ID used to retrieve event details" + sEventID );
		}
	}
	else
	{
		if(sAdminID!=null && !"".equalsIgnoreCase(sAdminID))
		{
			EventManager eventManager = new EventManager();
			ArrayList<EventBean> arrEventBean = eventManager.getAllEvents(sAdminID);
			
			jsonResponseObj.put("event_detail",eventManager.getEventJson(arrEventBean));
			
			
			Text okText = new OkText("All events retrieved for the admin.","my_id");		
			arrOkText.add(okText);
			responseStatus = RespConstants.Status.OK;
		}
		else
		{
			Text errorText = new ErrorText("Could not retrieve events for this admin.","my_id") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			
			appLogging.error("Invalid Admin ID used to retrieve event details" + sAdminID );
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
	appLogging.error("Error loading events for Admin : " + sAdminID + ExceptionHandler.getStackTrace(e));
	
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading events for admin : " + sAdminID );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
}
%>