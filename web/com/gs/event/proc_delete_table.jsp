<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>


<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");
try
{ 
	String sTableId =  ParseUtil.checkNull(request.getParameter("table_id"));
	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	
	if(sTableId!=null && !"".equalsIgnoreCase(sTableId) && sEventId!=null && !"".equalsIgnoreCase(sEventId) )
	{
		TableBean tableBean = new TableBean();
		tableBean.setTableId(sTableId);
		
		TableManager tableManager = new TableManager();
		Integer numOfTables = tableManager.deleteTable(tableBean);
		appLogging.error("Table of tables deleted  = " + numOfTables );
		EventTableBean eventTableBean = new EventTableBean();
		eventTableBean.setTableId(sTableId);
		eventTableBean.setEventId(sEventId);
		
		Integer numOfEventTablesDel = tableManager.deleteEventTable(eventTableBean);
		
		TableGuestsBean  tableGuestBean = new TableGuestsBean();
		tableGuestBean.setTableId(sTableId);
		
		GuestTableManager guestTableManager = new GuestTableManager();
		Integer numOfGuestTablesDel = guestTableManager.deleteGuestTables(tableGuestBean);
		
		if(numOfTables>0)
		{
			jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
			jsonResponseObj.put("table_id",sTableId );
		}
		else
		{
			jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		}
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
	appLogging.error("Error deleting table " );
	out.println(jsonResponseObj);
}
%>