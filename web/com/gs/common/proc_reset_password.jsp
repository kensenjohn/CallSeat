<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.manager.*" %>
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
	String sAdmindId =   ParseUtil.checkNull(request.getParameter("admin_id"));
	String sCurrentPassword =   ParseUtil.checkNull(request.getParameter("current_password"));
	String sNewPassword =   ParseUtil.checkNull(request.getParameter("new_password"));
	String sConfirmPassword =   ParseUtil.checkNull(request.getParameter("confirm_password"));
	
	if(sCurrentPassword==null || "".equalsIgnoreCase(sCurrentPassword)
			|| sNewPassword==null || "".equalsIgnoreCase(sNewPassword)
			|| sConfirmPassword==null || "".equalsIgnoreCase(sConfirmPassword) )
	{
		Text errorText = new ErrorText("Please fill all the fields.","reset_pass_err_mssg") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else if(sNewPassword!=null && !"".equalsIgnoreCase(sNewPassword) 
			&& !sNewPassword.equalsIgnoreCase(sConfirmPassword))
	{
		Text errorText = new ErrorText("Please confirm that the new passwords match.","reset_pass_err_mssg") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else
	{
		AdminBean adminBean = new AdminBean();
		
		AdminManager adminManager = new AdminManager();
		UserInfoBean userInfoBean = adminManager.getAminUserInfo(sAdmindId);
		
		if(userInfoBean!=null)
		{
			RegisterAdminBean loginAdminBean = new RegisterAdminBean();
			loginAdminBean.setEmail(userInfoBean.getEmail());
			loginAdminBean.setPassword(sCurrentPassword);
			
			adminBean = adminManager.authenticateUser( loginAdminBean );
			
			if( adminBean!=null && adminBean.isAdminExists() )
			{
				RestPasswordResponseBean resetPasswordRespBean = adminManager.resetPassword(adminBean,sNewPassword);
				if(resetPasswordRespBean!=null && sNewPassword.equalsIgnoreCase(resetPasswordRespBean.getNewPassword()))
				{
					Text okText = new OkText("Your password was changed successfully.","reset_pass_success_mssg") ;		
					arrOkText.add(okText);
					
					responseStatus = RespConstants.Status.OK;
				}
				else
				{

					Text errorText = new ErrorText("Your current password was not authenticated. Please try again.","reset_pass_err_mssg") ;		
					arrErrorText.add(errorText);
					
					responseStatus = RespConstants.Status.ERROR;
				}
			}
			else
			{
				Text errorText = new ErrorText("Your current password was not authenticated. Please try again.","reset_pass_err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
		}
		else
		{
			Text errorText = new ErrorText("There was an error processing your request. Please try again later.","reset_pass_err_mssg") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}
		
		
		
		int numOfRecords = adminManager.updateUser(adminBean);
		
		if(numOfRecords>0)
		{
			Text okText = new OkText("Successfully saved changed.","reset_pass_success_mssg") ;		
			arrOkText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
		}
		else
		{
			Text errorText = new ErrorText("Your request was not processed. Please try again later.","reset_pass_err_mssg") ;		
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