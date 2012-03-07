<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
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
	String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
	String sAdminId =  ParseUtil.checkNull(request.getParameter("admin_id"));
	String sEventName =  ParseUtil.checkNull(request.getParameter("e_summ_event_name"));
	String sEventDate =  ParseUtil.checkNull(request.getParameter("e_summ_event_date"));
	
	if(sEventId!=null && !"".equalsIgnoreCase(sEventId))
	{
		if(sEventName==null || "".equalsIgnoreCase(sEventName))
		{
			Text errorText = new ErrorText("Please enter a valid event name.","e_summ_event_name") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}
		else if(sEventDate==null || "".equalsIgnoreCase(sEventDate))
		{
			Text errorText = new ErrorText("Please enter a valid event date.","e_summ_event_date") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}
		else
		{
			EventCreationMetaDataBean eventCreateMetaBean = new EventCreationMetaDataBean();
			eventCreateMetaBean.setEventId(sEventId);
			eventCreateMetaBean.setEventName(sEventName);
			eventCreateMetaBean.setEventDate(sEventDate);
			eventCreateMetaBean.setEventDatePattern("MM/dd/yyyy");
			eventCreateMetaBean.setEventTimeZone("UTC");
			
			EventManager eventManager = new EventManager();
			Integer iNumOfRowsUpdated = eventManager.updateEvent(eventCreateMetaBean); 
			if(iNumOfRowsUpdated<1)
			{
				Text errorText = new ErrorText("Oops!! Please try again later.","e_summ_event_date") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
			else
			{
				
				responseStatus = RespConstants.Status.OK;
			}
		}
		
	}
	else
	{
		Text errorText = new ErrorText("Error processing request.","my_id") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}


	appLogging.error("Response " + sEventId );
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error creating table " );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
}
%>