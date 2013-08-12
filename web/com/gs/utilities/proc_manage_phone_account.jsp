<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@page import="com.gs.utilities.*"%>
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
	
	String sManageAction =  ParseUtil.checkNull(request.getParameter("manage_action"));
	String sAccountNum =  ParseUtil.checkNull(request.getParameter("account_sid"));
	 
	ManagePhoneAccounts managePhoneAccounts = new ManagePhoneAccounts();
	if(sManageAction!=null && "close_account".equalsIgnoreCase(sManageAction) && sAccountNum!=null
			&& !"".equalsIgnoreCase(sAccountNum) )
	{
		boolean isSuccess = managePhoneAccounts.closePhoneAccount(sAccountNum);
		
		if(isSuccess)
		{
			Text okText = new OkText("The guest was assigned successfully to the event successfully.","err_mssg") ;		
			arrErrorText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
			
			jsonResponseObj.put("account_sid",sAccountNum);
			jsonResponseObj.put(Constants.J_RESP_SUCCESS,true);
		}
		else
		{
			Text errorText = new ErrorText("Something went wrong processing your request. Please try again.","account_num") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}
		
	}
	else
	{
		Text errorText = new ErrorText("Invalid account number used.","account_num") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}

	//appLogging.error("Response " + sEventId + " table : " + jsonResponseObj.toString());
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
	//out.println(jsonResponseObj.toString());
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	jsonResponseObj.put("message", "Your request to add guest was lost. Please try again later.");
	appLogging.error("Error creating guest " );
	out.println(jsonResponseObj);
}
%>