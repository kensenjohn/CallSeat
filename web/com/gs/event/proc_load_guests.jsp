<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%
JSONObject jsonResponseObj = new JSONObject();
String sEventID = ParseUtil.checkNull(request.getParameter("event_id"));
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");
try
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
	}
	else
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		appLogging.error("Invalid admin Id used to retrieve table details");
	}
	out.println(jsonResponseObj);
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading table for event : " + sEventID + ExceptionHandler.getStackTrace(e));
	out.println(jsonResponseObj);
}
%>