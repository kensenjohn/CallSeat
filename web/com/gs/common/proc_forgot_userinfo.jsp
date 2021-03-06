<%@page import="org.xml.sax.ErrorHandler"%>
<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.forgot.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
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
	// we need a security check around here. to make sure this page does not get pounded by DDOS
	
	String sEmailId =  ParseUtil.checkNull(request.getParameter("reg_email_id"));
	
	if(sEmailId!=null && !"".equalsIgnoreCase(sEmailId))
	{
		
		
		AdminManager adminManager = new AdminManager();
		//boolean isSuccess = adminManager.resetPasswordAndEmail(sEmailId);
		
		ForgotInfoManager forgotInfoManager = new ForgotPassword(sEmailId);
		boolean isSuccess = forgotInfoManager.createUserRequest(); 
		
		if(isSuccess)
		{
            appLogging.info("Successfully created User Request for Forgot Password. Email : " + sEmailId);
            Text okText = new ErrorText("An email with a link to reset your password has been sent to the email address specified.","success_mssg") ;
			arrOkText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
			jsonResponseObj.put("email_id",sEmailId);
		}
		else
		{
            appLogging.info("An error was encountered while User Request for Forgot Password. Email : " + sEmailId);
			Text errorText = new ErrorText("An email with a link to reset your password has been sent to the email address specified.","success_mssg") ;
			arrOkText.add(errorText);
			
			responseStatus = RespConstants.Status.OK;
			jsonResponseObj.put("email_id",sEmailId);
		}
	}
	else
	{
		Text errorText = new ErrorText("Please use  a valid email address.","err_mssg") ;	 	
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
	appLogging.error("General Error: Forgot userinfo." + ExceptionHandler.getStackTrace(e));
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","err_mssg") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
%>