<%@page import="com.gs.json.RespJsonObject"%>
<%@page import="com.gs.json.Response"%>
<%@page import="com.gs.json.Payload"%>
<%@page import="com.gs.json.OkText"%>
<%@page import="com.gs.json.ErrorText"%>
<%@page import="com.gs.json.Text"%>
<%@page import="com.gs.json.Messages"%>
<%@page import="com.gs.json.RespConstants"%>
<%@page import="com.gs.json.RespObjectProc"%>
<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.data.event.*" %>

<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@page import="java.util.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();
try
{
	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
	String sTableId =  ParseUtil.checkNull(request.getParameter("table_id"));
	
	GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
	guestTableMetaData.setEventId(sEventId);
	guestTableMetaData.setAdminId(sAdminId);
	guestTableMetaData.setTableId(sTableId);
	
	RespJsonObject custRespJson = new RespJsonObject();
	
	if(!Utility.isNullOrEmpty(sEventId)  && !Utility.isNullOrEmpty(sAdminId) && !Utility.isNullOrEmpty(sTableId)) {
		GuestTableManager guestTableManager = new GuestTableManager();
		
		
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
		arrOkText.add(okText);
		responseStatus = RespConstants.Status.OK;
		
	}
	else
	{
		Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	}
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
%>