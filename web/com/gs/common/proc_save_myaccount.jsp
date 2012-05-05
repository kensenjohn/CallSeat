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
	String sEmail =   ParseUtil.checkNull(request.getParameter("login_email"));
	String sFirstName =   ParseUtil.checkNull(request.getParameter("register_fname"));
	String sLastName =   ParseUtil.checkNull(request.getParameter("register_lname"));
	
	if(sFirstName==null || "".equalsIgnoreCase(sFirstName)
			|| sLastName==null || "".equalsIgnoreCase(sLastName)
			|| sEmail==null || "".equalsIgnoreCase(sEmail) )
	{
		Text errorText = new ErrorText("Please fill all the fields.","reg_err_mssg") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
	}
	else
	{
		AdminBean adminBean = new AdminBean();
		
		AdminManager adminManager = new AdminManager();
		UserInfoBean userInfoBean = adminManager.getAminUserInfo(sAdmindId);
		
		adminBean.setAdminId(sAdmindId);
		userInfoBean.setFirstName(sFirstName);
		userInfoBean.setLastName(sLastName);
		userInfoBean.setEmail(sEmail);
		adminBean.setAdminUserInfoBean(userInfoBean);
		
		int numOfRecords = adminManager.updateUser(adminBean);
		
		if(numOfRecords>0)
		{
			Text okText = new OkText("Successfully saved changed.","reg_success_mssg") ;		
			arrOkText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
		}
		else
		{
			Text errorText = new ErrorText("Your request was not processed. Please try again later.","reg_err_mssg") ;		
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