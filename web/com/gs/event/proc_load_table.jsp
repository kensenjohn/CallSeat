<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%
JSONObject jsonResponseObj = new JSONObject();
String sEventID = ParseUtil.checkNull(request.getParameter("event_id"));
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");
try
{
	
	if(sEventID!=null)
	{
		GuestTableManager guestTableManager = new GuestTableManager();
		HashMap<Integer, TableGuestsBean> hmTables =  guestTableManager.getTablesAndGuest(sEventID);
		//appLogging.error("Table with Ints = " + hmTables);
		HashMap<String, TableGuestsBean> hmConsTableGuestBean = guestTableManager.consolidateTableAndGuest(hmTables);
		//appLogging.info("Table with String = " + hmConsTableGuestBean);
		jsonResponseObj.put("table_detail",guestTableManager.getTablesAndGuestJson(hmConsTableGuestBean));
		
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
	}
	else
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		appLogging.error("Invalid event used to retrieve table details");
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