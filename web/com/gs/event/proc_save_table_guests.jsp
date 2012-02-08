<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.json.RespJsonObject"%>
<%@page import="com.gs.json.Response"%>
<%@page import="com.gs.json.Payload"%>
<%@page import="com.gs.json.OkText"%>
<%@page import="com.gs.json.ErrorText"%>
<%@page import="com.gs.json.Text"%>
<%@page import="com.gs.json.Messages"%>
<%@page import="com.gs.json.RespConstants"%>
<%@page import="com.gs.json.RespObjectProc"%>
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


ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();
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
		Text errorText = new ErrorText("Please select at least one seat.","my_id");		
		arrErrorText.add(errorText);
		responseStatus = RespConstants.Status.ERROR;
		appLogging.error("No seat was selected. EventId =" + sEventId + " table Id = " + sTableId + " Guest ID = " + sGuestId );
	}
	else if(sGuestId==null || "".equalsIgnoreCase(sGuestId) || "".equalsIgnoreCase(sGuestId)  || "all".equalsIgnoreCase(sGuestId))
	{
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		Text errorText = new ErrorText("Please select at least one guest.","my_id");		
		arrErrorText.add(errorText);
		responseStatus = RespConstants.Status.ERROR;
		appLogging.error("No guest was selected.  EventId =" + sEventId + " table Id = " + sTableId + " Guest ID = " + sGuestId  );
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
		GuestTableResponse guestTableResponse = guestTableManager.assignSeatsForGuest(guestMetaData);
		
		if(guestTableResponse!=null)
		{
			if(guestTableResponse.isSuccess())
			{
				Text okText = new OkText(guestTableResponse.getMessage(),"my_id");		
				arrOkText.add(okText);
				responseStatus = RespConstants.Status.OK;
			}
			else
			{
				Text errorText = new ErrorText(guestTableResponse.getMessage(),"my_id");		
				arrErrorText.add(errorText);
				responseStatus = RespConstants.Status.ERROR;
			}
		}
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
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
	jsonResponseObj.put("message", "Your request to assign guest to table was lost. Please try again later.");
	appLogging.error("Error creating guest " );
	out.println(jsonResponseObj);
}
%>