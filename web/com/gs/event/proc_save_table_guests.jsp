<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.data.event.*" %>

<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@page import="java.util.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

try
{
	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
	String sTableId =  ParseUtil.checkNull(request.getParameter("table_id"));
	String sGuestId =  ParseUtil.checkNull(request.getParameter("guest_id"));
	Integer sNumOfSeats = ParseUtil.sToI(request.getParameter("num_of_new_seats"));
	
	if(sNumOfSeats<=0)
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	}
	else if(sGuestId==null || "".equalsIgnoreCase(sGuestId) || "".equalsIgnoreCase(sGuestId))
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	}
	else
	{
		GuestTableMetaData guestMetaData = new GuestTableMetaData();
		guestMetaData.setAdminId(sAdminId);
		guestMetaData.setEventId(sEventId);
		guestMetaData.setTableId(sTableId);
		guestMetaData.setGuestId(sGuestId);
		guestMetaData.setNumOfSeats(sNumOfSeats);
		
		GuestTableManager guestTableManager = new GuestTableManager();
		guestTableManager.assignSeatsForGuest(guestMetaData);
		
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
	}

	out.println(jsonResponseObj);
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	jsonResponseObj.put("message", "Your request to assign guest to table was lost. Please try again later.");
	appLogging.error("Error creating guest " );
	out.println(jsonResponseObj);
}
%>