<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@page import="com.gs.manager.forgot.*" %>
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
	String sNewPassword =  ParseUtil.checkNull(request.getParameter("new_passwd"));
	String sConfirmNewPassword =  ParseUtil.checkNull(request.getParameter("confirm_new_passwd"));
	String sSecurityForgotInfoId =  ParseUtil.checkNull(request.getParameter("security_token_record_guid"));
	String sSecurityTokenId =  ParseUtil.checkNull(request.getParameter("security_token_id"));
	
	
	
	if( sNewPassword==null || "".equalsIgnoreCase(sNewPassword) || 
			sConfirmNewPassword==null || "".equalsIgnoreCase(sConfirmNewPassword) )
	{
		Text errorText = new ErrorText("Please enter a new password and a matching confirmation password.","reset_password") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else if(!sNewPassword.equalsIgnoreCase(sConfirmNewPassword))
	{
		Text errorText = new ErrorText("The confirmation password does not match the new password.","reset_password") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else if( sNewPassword!=null && !"".equalsIgnoreCase(sNewPassword) && 
			sConfirmNewPassword!=null && !"".equalsIgnoreCase(sConfirmNewPassword) 
			&& sNewPassword.equalsIgnoreCase(sConfirmNewPassword))
	{
		ForgotInfoManager forgotInfoManager = new ForgotPassword();
		SecurityForgotInfoBean securityForgInfoBean = forgotInfoManager.identifyUserResponse(sSecurityTokenId); 
		
		if(securityForgInfoBean!=null && securityForgInfoBean.getSecurityForgotInfoId().equalsIgnoreCase(sSecurityForgotInfoId))
		{
			AdminManager adminManager = new AdminManager();
			AdminBean adminBean = adminManager.getAdmin(securityForgInfoBean.getAdminId());
			RestPasswordResponseBean resetPasswordRespBean = adminManager.resetPassword(adminBean , sNewPassword );
			
			if(resetPasswordRespBean!=null && sNewPassword.equalsIgnoreCase(resetPasswordRespBean.getNewPassword()))
			{
				forgotInfoManager.processUserResponse(securityForgInfoBean);
				Text okText = new OkText("The password was successfullly created. Please login using the new password.","reset_password") ;		
				arrOkText.add(okText);
				
				responseStatus = RespConstants.Status.OK;
			}
			else
			{
				Text errorText = new ErrorText("There was an error setting creating the new password. Please try again later.","reset_password") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
		}
		else
		{
			Text errorText = new ErrorText("There was an error processing your request. Please try again.","reset_password") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}

	}
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
	Text errorText = new ErrorText("Your request to edit the guest's information was not processed. Please try again later.","err_mssg") ;		
	arrErrorText.add(errorText);
	
	responseStatus = RespConstants.Status.ERROR;
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	appLogging.error("Error editing guest : " + ExceptionHandler.getStackTrace(e) );
}
%>