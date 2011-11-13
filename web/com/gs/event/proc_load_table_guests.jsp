<%@page import="com.gs.json.CustomRespJsonObject"%>
<%@page import="com.gs.json.Response"%>
<%@page import="com.gs.json.Payload"%>
<%@page import="com.gs.json.OkText"%>
<%@page import="com.gs.json.Text"%>
<%@page import="com.gs.json.Messages"%>
<%@page import="com.gs.json.RespConstants"%>
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
	
	GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
	guestTableMetaData.setEventId(sEventId);
	guestTableMetaData.setAdminId(sAdminId);
	guestTableMetaData.setTableId(sTableId);
	
	CustomRespJsonObject custRespJson = new CustomRespJsonObject();
	
	if(sEventId!=null && !"".equalsIgnoreCase(sEventId) && sAdminId!=null && !"".equalsIgnoreCase(sAdminId)
		&& sTableId!=null && !"".equalsIgnoreCase(sTableId))
	{
				
		
		GuestTableManager guestTableManager = new GuestTableManager();
		guestTableManager.getGuestWithNoTable(sEventId);
		
		
		HashMap<Integer, AssignedGuestBean> hmAssignedTables =  guestTableManager.getAssignedGuest(sEventId,sTableId);
		HashMap<Integer, AssignedGuestBean> hmUnAssignedTables =  guestTableManager.getUnAssignedGuest(guestTableMetaData);
		
		JSONObject jsonAssignGuests = guestTableManager.getAssignedGuestBeanJson(hmAssignedTables);
		JSONObject jsonUnAssignGuests = guestTableManager.getAssignedGuestBeanJson(hmUnAssignedTables);
		
		TableManager tableManager = new TableManager();
		TableBean tableBean = tableManager.getTable(sTableId);
				
		jsonResponseObj.put("assigned_guests", jsonAssignGuests);
		jsonResponseObj.put("un_assigned_guests", jsonUnAssignGuests);
		jsonResponseObj.put("all_tables_assigned", guestTableManager.getTablesAndGuestJson(sEventId));
		jsonResponseObj.put("this_table", tableBean.toJson());
		
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
		
		Text okText = new OkText("Loading Data Complete ","my_id");
		ArrayList<Text> arrOkText = new ArrayList<Text>();
		arrOkText.add(okText);
		
		
		Messages messages = new Messages();
		messages.setArrOkText(arrOkText);
		
		
		
		Payload payload = new Payload(jsonResponseObj);
		Response processResponse = new Response();
		processResponse.setPayload(payload);
		processResponse.setMessages(messages);
		
		
		custRespJson.setStatus( RespConstants.Status.OK);
		custRespJson.setResponse( processResponse );
		
		
		//jsonResponseObj.put("new_stuff" , custRespJson.toJson());
		
		
		
	}
	else
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	}
	out.println(custRespJson.toJson());
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	jsonResponseObj.put("message", "Your request to add guest was lost. Please try again later.");
	appLogging.error("Error creating guest " );
	out.println(jsonResponseObj);
}
%>