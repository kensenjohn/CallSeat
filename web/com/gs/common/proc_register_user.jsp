<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
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
	String sRegEmails =  ParseUtil.checkNull(request.getParameter("register_email"));
	String sRegFirstName =  ParseUtil.checkNull(request.getParameter("register_fname"));
	String sRegLastName =  ParseUtil.checkNull(request.getParameter("register_lname"));
	String sRegPass =  ParseUtil.checkNull(request.getParameter("register_pass"));
	String sRegPassConf =  ParseUtil.checkNull(request.getParameter("register_pass_conf"));
	
	if(sRegFirstName==null || "".equalsIgnoreCase(sRegFirstName)
			|| sRegLastName==null || "".equalsIgnoreCase(sRegLastName)
			|| sRegPass==null || "".equalsIgnoreCase(sRegPass)
			|| sRegPassConf==null || "".equalsIgnoreCase(sRegPassConf) 
			|| sRegEmails==null || "".equalsIgnoreCase(sRegEmails) )
	{
		Text errorText = new ErrorText("Please fill all the fields.","my_id") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else
	{
		if(sRegPass.equalsIgnoreCase(sRegPassConf))
		{
			RegisterAdminBean regAdminBean = new RegisterAdminBean();
			regAdminBean.setAdminId(sAdminId);
			regAdminBean.setEmail(sRegEmails);
			regAdminBean.setFirstName(sRegFirstName);
			regAdminBean.setLastName(sRegLastName);
			regAdminBean.setPassword(sRegPass);
			regAdminBean.setPasswordConf(sRegPassConf);
			
			AdminManager adminManager = new AdminManager();
			
			Integer iNumOfUserRegistered = 0;
			AdminBean adminBean = new AdminBean();
			if(!adminManager.isUserExists(regAdminBean))
			{
				adminBean = adminManager.registerUser(regAdminBean);
								
				if(adminBean!=null && adminBean.getAdminId()!=null && !"".equalsIgnoreCase(adminBean.getAdminId()))					
				{
					AdminBean tmpAdminBean = adminManager.getAdmin(adminBean.getAdminId());

					HttpSession httpSession = request.getSession(false);
					httpSession.setAttribute(Constants.USER_SESSION,tmpAdminBean);
					
					if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && !sAdminId.equalsIgnoreCase(adminBean.getAdminId()))
					{
						adminManager.assignTmpToPermAdmin(sAdminId,adminBean.getAdminId());
					}
					
					
					
					Text okText = new OkText("Successfully registered user.","reg_success_mssg") ;		
					arrOkText.add(okText);
					
					responseStatus = RespConstants.Status.OK;
					jsonResponseObj.put("first_name",sRegFirstName);
					jsonResponseObj.put("user_id",adminBean.getAdminId());
					
					adminManager.sendNewRegUserEmail(regAdminBean);
				}
				else
				{
					Text errorText = new ErrorText("Oops!! User was not registered. Please try again.","reg_err_mssg") ;		
					arrErrorText.add(errorText);
					
					responseStatus = RespConstants.Status.ERROR;
				}
			}
			else
			{
				Text errorText = new ErrorText("An account with that email address already exists. Try another?","reg_err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
			
		}
		else
		{
			Text errorText = new ErrorText("The passwords your entered do not match.","reg_err_mssg") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
		}
		
	}
	
	//appLogging.error("Response " + sEventId + " table : " + jsonResponseObj.toString());
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
catch(Exception e)
{
	appLogging.error("General Error registering the user." );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","reg_err_mssg") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
%>