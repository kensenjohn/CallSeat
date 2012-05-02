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
	EventPricingGroupManager eventPricingManager = new EventPricingGroupManager();
	ArrayList<PricingGroupBean> arrPricingBean = eventPricingManager.getPricingGroups();
	
	JSONArray jsonPricingGroupArray = eventPricingManager.getPricingGroupJsonArray(arrPricingBean);
	if(arrPricingBean!=null && !arrPricingBean.isEmpty() && jsonPricingGroupArray!=null)
	{
		//jsonResponseObj.put(Constants.J_RESP_SUCCESS, true);
		jsonResponseObj.put("value",jsonPricingGroupArray);
		
		Text okText = new OkText("Loading Data Complete ","my_id");		
		arrOkText.add(okText);
		responseStatus = RespConstants.Status.OK;
	}
	else
	{
		appLogging.error("Error retieving Pricing Groups ");
		
		Text errorText = new ErrorText("The pricing data could not be accessed at this time. Please try again later.","my_id") ;		
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
	jsonResponseObj.put("message", "Phone numbers could not be loaded. Please try again.");
	appLogging.error("Error processing phone numbers." );
	out.println(jsonResponseObj);
}
%>