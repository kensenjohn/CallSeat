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
	String sLoginEmails =  ParseUtil.checkNull(request.getParameter("login_email"));
	String sLoginPass =  ParseUtil.checkNull(request.getParameter("login_password"));
	
	if( sLoginEmails==null || "".equalsIgnoreCase(sLoginEmails)
			|| sLoginPass==null || "".equalsIgnoreCase(sLoginPass) )
	{
		Text errorText = new ErrorText("Please fill all the fields.","login_err_mssg") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else
	{
		RegisterAdminBean loginAdminBean = new RegisterAdminBean();
		loginAdminBean.setEmail(sLoginEmails);
		loginAdminBean.setPassword(sLoginPass);
		
		AdminManager adminManager = new AdminManager();
		AdminBean adminBean = adminManager.authenticateUser( loginAdminBean );
		
		
		if( adminBean!=null && adminBean.isAdminExists() )
		{
			Text okText = new ErrorText("Successfully authenticated user.","my_id") ;		
			arrOkText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
			jsonResponseObj.put("user_id",adminBean.getAdminId());
			
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute(Constants.USER_SESSION,adminBean);
			
			adminManager.assignTmpToPermAdmin(sAdminId,adminBean.getAdminId());
		}
		else
		{
			Text errorText = new ErrorText("Invalid Credentials.","login_err_mssg") ;		
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
	appLogging.error("General Error logging in user." );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
}
%>