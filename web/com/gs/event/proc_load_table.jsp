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
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");

JSONObject jsonResponseObj = new JSONObject();
String sEventID = ParseUtil.checkNull(request.getParameter("event_id"));
String sTableId = ParseUtil.checkNull(request.getParameter("table_id"));
boolean isLoadSingleTable = ParseUtil.sTob(request.getParameter("load_single_table"));

response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();
try
{
	if(sEventID!=null)
	{
		if(isLoadSingleTable)
		{
			if(sTableId!=null && !"".equalsIgnoreCase(sTableId))
			{
				TableManager tableManager = new TableManager();
				TableBean tableBean = tableManager.getTable(sTableId);

				jsonResponseObj.put("table_detail",tableBean.toJson());
				jsonResponseObj.put("load_table",true);
				
				Text okText = new OkText("Loading Data Complete ","my_id");		
				arrOkText.add(okText);
				responseStatus = RespConstants.Status.OK;
			}
			else
			{
				Text errorText = new ErrorText("Table details were not loaded. Please try again.","my_id") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
		}
		else
		{
			GuestTableManager guestTableManager = new GuestTableManager();
			HashMap<Integer, TableGuestsBean> hmTables =  guestTableManager.getTablesAndGuest(sEventID);
			
			HashMap<String, TableGuestsBean> hmConsTableGuestBean = guestTableManager.consolidateTableAndGuest(hmTables);
			jsonResponseObj.put("table_detail",guestTableManager.getTablesAndGuestJson(hmConsTableGuestBean));
			
			Text okText = new OkText("Loading Data Complete ","my_id");		
			arrOkText.add(okText);
			responseStatus = RespConstants.Status.OK;
		}
				
	}
	else
	{
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		appLogging.error("Invalid event used to retrieve table details");
		
		Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
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
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading table for event : " + sEventID + ExceptionHandler.getStackTrace(e));
	

	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
}
%>