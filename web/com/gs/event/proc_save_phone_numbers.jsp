<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@page import="com.gs.payment.*"%>
<%@page import="com.gs.manager.event.*"%>
<%@page import="com.gs.common.*" %>
<%@include file="../common/security.jsp" %>
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
	String sSeatingNumber = ParseUtil.checkNull(request.getParameter("seating_tel_num")); 
	String sRsvpNumber = ParseUtil.checkNull(request.getParameter("rsvp_tel_num"));
	String sPriceGroupId = ParseUtil.checkNull(request.getParameter("priceOption"));
	String sBillFirstName = ParseUtil.checkNull(request.getParameter("bill_first_name"));
	String sBillLastName = ParseUtil.checkNull(request.getParameter("bill_last_name"));
	String sBillMiddleName = ParseUtil.checkNull(request.getParameter("bill_middle_name"));
	String sBillAddress1 = ParseUtil.checkNull(request.getParameter("bill_addr_1"));
	String sBillAddress2 = ParseUtil.checkNull(request.getParameter("bill_addr_2"));
	String sBillZip = ParseUtil.checkNull(request.getParameter("bill_zip"));
	String sBillCity = ParseUtil.checkNull(request.getParameter("bill_city"));
	String sBillState = ParseUtil.checkNull(request.getParameter("bill_state"));
	String sBillCountry = ParseUtil.checkNull(request.getParameter("bill_country"));
	String sCreditCardNum = ParseUtil.checkNull(request.getParameter("cc_num"));
	String sSecureNum = ParseUtil.checkNull(request.getParameter("cc_secure_num"));
		
	if(isSignedIn)
	{
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId)  && sEventId!=null && !"".equalsIgnoreCase(sEventId))
		{
			if(sSeatingNumber!=null && !"".equalsIgnoreCase(sSeatingNumber)
					&& sRsvpNumber!=null && !"".equalsIgnoreCase(sRsvpNumber) )
			{
				EventPricingGroupManager eventPricingGroupManager = new EventPricingGroupManager();
				PricingGroupBean pricingGroupBean = eventPricingGroupManager.getPricingGroups(sPriceGroupId);
				
				if(pricingGroupBean!=null )
				{
					BillingMetaData billingMetaData = new BillingMetaData();
					billingMetaData.setAdminId(sAdminId);
					billingMetaData.setEventId(sEventId);
					billingMetaData.setFirstName(sBillFirstName);
					billingMetaData.setLastName(sBillLastName);
					billingMetaData.setMiddletName(sBillMiddleName);
					billingMetaData.setAddress1(sBillAddress1);
					billingMetaData.setAddress2(sBillAddress2);
					billingMetaData.setZip(sBillZip);
					billingMetaData.setCity(sBillCity);
					billingMetaData.setState(sBillState);
					billingMetaData.setCountry(sBillCountry);
					billingMetaData.setCreditCardNum(sCreditCardNum);
					billingMetaData.setSecureNum(sSecureNum);
					billingMetaData.setPrice(pricingGroupBean.getPrice().toString());
					
					BillingManager billingManager = new BillingManager();
					if(billingManager.isCreditCardAccepted(billingMetaData)){
						billingManager.saveBillingInfo(billingMetaData);
					}
					
					
				}
				
				
				
				TelNumberMetaData telNumberMetaData = new TelNumberMetaData();
				telNumberMetaData.setAdminId(sAdminId);
				telNumberMetaData.setEventId(sEventId);
				telNumberMetaData.setRsvpTelNumDigit(sRsvpNumber);
				telNumberMetaData.setSeatingTelNumDigit(sSeatingNumber);
				
				TelNumberManager telNumManager = new TelNumberManager();
				telNumManager.saveTelNumbers(telNumberMetaData);
			}
			else
			{
				Text errorText = new ErrorText("Please fill in at least a one phone number.","my_id") ;
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
				jspLogging.error("Missing paramteres in request Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
			}
		}
		else
		{
			Text errorText = new ErrorText("Oops!! Please try again later. Your request was not completed.","my_id") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
			jspLogging.error("Missing paramteres in request Admin Id : " + sAdminId  + " Event ID : " +  sEventId );
		}
	}
	else
	{
		Text errorText = new ErrorText("Please log in or register before trying to save the phone numbers","my_id") ;		
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