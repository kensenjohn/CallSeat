<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.manager.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@page import="com.gs.json.*"%>

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
	String sGuestId =  ParseUtil.checkNull(request.getParameter("guest_id"));
	
	if(sGuestId!=null && !"".equalsIgnoreCase(sGuestId)  )
	{
		// deleteing guest from all table assignements
		TableManager tableManager = new TableManager();
		tableManager.deleteGuestFromTables(sGuestId);
		jspLogging.info("Delete guest from all tables invoke complete");
		
		//deleteing guest from all event assignments, i.e invite and rsvp
		EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
		eventGuestMetaData.setGuestId(sGuestId);
		EventGuestManager eventGuestManager = new EventGuestManager();
		eventGuestManager.deleteGuestFromAllEvent(eventGuestMetaData);
		jspLogging.info("Delete guest from all events invoke complete");
		
		// delete actual guest
		GuestManager guestManager = new GuestManager();
		Integer iNumOfRecs = guestManager.deleteGuest(sGuestId);
		
		if(iNumOfRecs<=0)
		{
			jspLogging.error("Delete guest has failed. Guestid : " + sGuestId );
			appLogging.error("Delete guest has failed. Guestid : " + sGuestId );
			Text errorText = new ErrorText("The guest was not deleted. Please try again later.","my_id") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}
		else
		{
			jspLogging.info("Delete guest successful. Guestid : " + sGuestId );
			appLogging.info("Delete guest successful. Guestid : " + sGuestId );
			
			Text okText = new OkText("The guest was successfully deleted.","my_id") ;		
			arrOkText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
			
		}
		
	}
	else
	{
		appLogging.error("Invalid guest id selected for deleteion. GuestId : " + sGuestId);
		Text errorText = new ErrorText("Please select a valid guest for deletion.","my_id") ;		
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
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error deleting table " );
	out.println(jsonResponseObj);
}
%>