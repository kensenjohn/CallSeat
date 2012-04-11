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
	String sFirstName =  ParseUtil.checkNull(request.getParameter("first_name"));
	String sLastName =  ParseUtil.checkNull(request.getParameter("last_name"));
	String sEmailAddr =  ParseUtil.checkNull(request.getParameter("email_addr"));
	String sInvitedNumOfSeats =  ParseUtil.checkNull(request.getParameter("invited_num_of_seats"));
	String sRsvpNumOfSeats =  ParseUtil.checkNull(request.getParameter("rsvp_num_of_seats"));
	String sCellNumber =  ParseUtil.checkNull(request.getParameter("cell_num"));
	String sHomeNumber =  ParseUtil.checkNull(request.getParameter("home_num"));
	String sGuestId =  ParseUtil.checkNull(request.getParameter("guest_id"));
	String sGuestUserInfoId =  ParseUtil.checkNull(request.getParameter("guest_userinfo_id"));
	boolean isAllGuestEdit =  ParseUtil.sTob(request.getParameter("all_guest_edit"));
	boolean isSingleGuestEventEdit =  ParseUtil.sTob(request.getParameter("is_single_guest_event_edit")); 
	
	boolean isError = false;
	if(sGuestId!=null && !"".equalsIgnoreCase(sGuestId) && sGuestUserInfoId!=null && !"".equalsIgnoreCase(sGuestUserInfoId) )
	{
		if(sFirstName==null || "".equalsIgnoreCase(sFirstName))
		{
			Text errorText = new ErrorText("Please enter the First Name.","first_name") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			isError = true;
		}
		if(sLastName==null || "".equalsIgnoreCase(sLastName))
		{
			Text errorText = new ErrorText("Please enter the Last Name.","last_name") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			isError = true;
		}
		if(sCellNumber==null || "".equalsIgnoreCase(sCellNumber))
		{
			Text errorText = new ErrorText("Please enter the Cell Number.","cell_num") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			isError = true;
		}
		
		if(!isAllGuestEdit && isSingleGuestEventEdit)
		{
			int iNumInvited =  ParseUtil.sToI(sInvitedNumOfSeats);
			if(sInvitedNumOfSeats==null || "".equalsIgnoreCase(sInvitedNumOfSeats) || iNumInvited<=0)
			{
				appLogging.warn("Invited number of guests was invalid : " + sInvitedNumOfSeats );
				
				Text errorText = new ErrorText("Enter a number more than 0.","invited_num_of_seats") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				isError = true;
			}
			
			
			
			int iNumRsvp =  ParseUtil.sToI(sRsvpNumOfSeats);
			if(sRsvpNumOfSeats==null || "".equalsIgnoreCase(sRsvpNumOfSeats) || iNumRsvp<0)
			{
				appLogging.warn("RSVP number of guests was invalid : " + sRsvpNumOfSeats );
				
				Text errorText = new ErrorText("Enter 0 if no RSVP.","rsvp_num_of_seats") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				isError = true;
			}
			
			if(!isError && iNumRsvp > iNumInvited)
			{
				appLogging.warn("RSVP number larger than the invited number : rsvp :" + sRsvpNumOfSeats + " invited : " + iNumInvited );
				
				Text errorText = new ErrorText("Enter 0 if no RSVP.","rsvp_num_of_seats") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				isError = true;
			}
		}
		

		
		if(!isError)
		{
			UserInfoBean userInfoBean = new UserInfoBean();
			userInfoBean.setUserInfoId(sGuestUserInfoId);
			userInfoBean.setFirstName(sFirstName);
			userInfoBean.setLastName(sLastName);
			userInfoBean.setCellPhone(sCellNumber);
			userInfoBean.setPhoneNum(sHomeNumber);
			userInfoBean.setEmail(sEmailAddr);
			
			UserInfoManager userInforManager = new UserInfoManager();
			
			Integer iNumOfRecs = userInforManager.updateGuestUserInfo(userInfoBean);
			
			if(!isAllGuestEdit && isSingleGuestEventEdit)
			{
				EventGuestBean eventGuestBean = new EventGuestBean();
				eventGuestBean.setEventId(sEventId);
				
				eventGuestBean.setGuestId( sGuestId );
				eventGuestBean.setTotalNumberOfSeats( sInvitedNumOfSeats );
				eventGuestBean.setRsvpSeats( sRsvpNumOfSeats );
				
				EventGuestManager eventGuestManager = new EventGuestManager();
				Integer iNumOfEventGuestRecs = eventGuestManager.setGuestInviteRsvpForEvent( eventGuestBean );
							
			}
			
			jsonResponseObj.put("guest_id",sGuestId);
			jsonResponseObj.put("guest_first_name",sFirstName);
			jsonResponseObj.put("guest_last_name",sLastName);
			
			Text okText = new OkText("Changes were saved successfully","rsvp_num_of_seats") ;		
			arrOkText.add(okText);
			
			responseStatus = RespConstants.Status.OK;
		}
	}
	else
	{
		
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