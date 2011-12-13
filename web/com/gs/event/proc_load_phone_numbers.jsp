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
	String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
	String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
	boolean isCustomNumGen = ParseUtil.sTob(request.getParameter("custom_num_gen"));
	String sNumberType = ParseUtil.checkNull(request.getParameter("num_type"));
	String sAreaCode = ParseUtil.checkNull(request.getParameter("seating_area_code"));
	String sTextPattern = ParseUtil.checkNull(request.getParameter("seating_text_pattern"));
	
	
	if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId) )
	{
		TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
		telNumberMetaData.setAdminId(sAdminId);
		telNumberMetaData.setEventId(sEventId);
		
		TelNumberManager telNumManager = new TelNumberManager();
		ArrayList<TelNumberBean> arrTelNumberBean = new ArrayList<TelNumberBean>();
		if(isCustomNumGen)
		{
			telNumberMetaData.setAreaCodeSearch(sAreaCode);
			telNumberMetaData.setTextPatternSearch(sTextPattern);
			
			arrTelNumberBean = telNumManager.searchTelNumber(telNumberMetaData);
		}
		else
		{
			arrTelNumberBean = telNumManager.getTelNumEventDetails(telNumberMetaData);
		}
		
		
		
		jsonResponseObj.put("telnumbers",  telNumManager.getTelNumberBeanJson(arrTelNumberBean));
		
		Text okText = new OkText("Loading Phone number complete","my_id");		
		arrOkText.add(okText);
		responseStatus = RespConstants.Status.OK;
	}
	else
	{
		Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
		arrErrorText.add(errorText);
		
		responseStatus = RespConstants.Status.ERROR;
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
		jspLogging.error("Missing paramteres in request Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
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
	jsonResponseObj.put("message", "Phone numbers could not be loaded. Please try again.");
	appLogging.error("Error processing phone numbers." );
	out.println(jsonResponseObj);
}
%>