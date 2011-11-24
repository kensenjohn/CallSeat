<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%
JSONObject jsonResponseObj = new JSONObject();
String sAdminID = ParseUtil.checkNull(request.getParameter("admin_id"));
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");
try
{
	if(sAdminID!=null && !"".equalsIgnoreCase(sAdminID))
	{
		EventManager eventManager = new EventManager();
		ArrayList<EventBean> arrEventBean = eventManager.getAllEvents(sAdminID);
		
		jsonResponseObj.put("event_detail",eventManager.getEventJson(arrEventBean));
		
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
	}
	else
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		appLogging.error("Invalid Admin ID used to retrieve event details" + sAdminID );
	}
	out.println(jsonResponseObj);
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading events for Admin : " + sAdminID + ExceptionHandler.getStackTrace(e));
	out.println(jsonResponseObj);
	
}
%>