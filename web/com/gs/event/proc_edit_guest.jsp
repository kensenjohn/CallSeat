<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@ page import="com.google.i18n.phonenumbers.Phonenumber" %>
<%@ page import="com.google.i18n.phonenumbers.PhoneNumberUtil" %>
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
	String sCellNumberHumanFormat =  ParseUtil.checkNull(request.getParameter("cell_num"));
	String sHomeNumberHumanFormat =  ParseUtil.checkNull(request.getParameter("home_num"));
	String sGuestId =  ParseUtil.checkNull(request.getParameter("guest_id"));
	String sGuestUserInfoId =  ParseUtil.checkNull(request.getParameter("guest_userinfo_id"));
	boolean isAllGuestEdit =  ParseUtil.sTob(request.getParameter("all_guest_edit"));
	boolean isSingleGuestEventEdit =  ParseUtil.sTob(request.getParameter("is_single_guest_event_edit"));
    Integer iCountryCode = 1; // 1 -> USA & Canada
	boolean isError = false;
	if(sGuestId!=null && !"".equalsIgnoreCase(sGuestId) && sGuestUserInfoId!=null && !"".equalsIgnoreCase(sGuestUserInfoId) )
	{
		if(sFirstName==null || "".equalsIgnoreCase(sFirstName))
		{
			Text errorText = new ErrorText("First Name is required","first_name") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			isError = true;
		}
		if(sLastName==null || "".equalsIgnoreCase(sLastName))
		{
			Text errorText = new ErrorText("Last Name is required","last_name") ;		
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			isError = true;
		}
        String sCellNumber = "";
        if(sCellNumberHumanFormat!=null&& !"".equalsIgnoreCase(sCellNumberHumanFormat))
        {
            sCellNumber = Utility.convertHumanToInternationalTelNum(sCellNumberHumanFormat);

            if(sCellNumber!=null && !"".equalsIgnoreCase(sCellNumber) && sCellNumber.length()>0)
            {
                sCellNumber = sCellNumber.substring(1);
            }
        }

        if(sCellNumber==null || "".equalsIgnoreCase(sCellNumber))
        {
            Text errorText = new ErrorText("We were unable to recognize the cellphone number. Please enter a valid cellphone number.<br>","cell_num") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            isError = true;
        }
        else
        {
            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber cellPhoneNumber = new Phonenumber.PhoneNumber();
            cellPhoneNumber.setCountryCode(iCountryCode);
            cellPhoneNumber.setNationalNumber(ParseUtil.sToL(sCellNumber.substring(1)));
            PhoneNumberUtil cellPhoneNumberUtil = PhoneNumberUtil.getInstance();
            if(!cellPhoneNumberUtil.isValidNumber(cellPhoneNumber))
            {
                Text errorText = new ErrorText("We were unable to recognize the cellphone number. Please enter a valid cellphone number.<br>","cell_num") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
                isError = true;
            }
            else
            {
                //sCellNumber = ParseUtil.iToS(iCountryCode)+sCellNumber.substring(1);
            }
        }

        String sHomeNumber = "";
        if(sHomeNumberHumanFormat!=null&& !"".equalsIgnoreCase(sHomeNumberHumanFormat))
        {
            sHomeNumber = Utility.convertHumanToInternationalTelNum(sHomeNumberHumanFormat);

            if(sHomeNumber!=null && !"".equalsIgnoreCase(sHomeNumber) && sHomeNumber.length()>0)
            {
                sHomeNumber = sHomeNumber.substring(1);
            }
        }

        if(sHomeNumber!=null && !"".equalsIgnoreCase(sHomeNumber))
        {
            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber homePhoneNumber = new Phonenumber.PhoneNumber();
            homePhoneNumber.setCountryCode(iCountryCode);
            homePhoneNumber.setNationalNumber(ParseUtil.sToL(sHomeNumber.substring(1)));
            PhoneNumberUtil homePhoneNumberUtil = PhoneNumberUtil.getInstance();
            if(!homePhoneNumberUtil.isValidNumber(homePhoneNumber))
            {
                Text errorText = new ErrorText("We were unable to recognize the phone number. Please enter a valid phone number.<br>","home_num") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
                isError = true;
            }
            else
            {
                //sCellNumber = ParseUtil.iToS(iCountryCode)+sCellNumber.substring(1);
            }
        }
		
		if(!isAllGuestEdit && isSingleGuestEventEdit)
		{
			int iNumInvited =  ParseUtil.sToI(sInvitedNumOfSeats);
			if(sInvitedNumOfSeats==null || "".equalsIgnoreCase(sInvitedNumOfSeats) || iNumInvited<=0)
			{
				appLogging.warn("Invited number of guests was invalid : " + sInvitedNumOfSeats );
				
				Text errorText = new ErrorText("Number of invited seats must be greater than 0.","invited_num_of_seats") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				isError = true;
			}
			
			
			
			int iNumRsvp =  ParseUtil.sToI(sRsvpNumOfSeats);
			if(sRsvpNumOfSeats==null || "".equalsIgnoreCase(sRsvpNumOfSeats) || iNumRsvp<0)
			{
				appLogging.warn("RSVP number of guests was invalid : " + sRsvpNumOfSeats );
				
				Text errorText = new ErrorText("Please use a valid RSVP number. Enter 0 if guest did not RSVP.","rsvp_num_of_seats") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				isError = true;
			}
			
			if(!isError && iNumRsvp > iNumInvited)
			{
				appLogging.warn("RSVP number larger than the invited number : rsvp :" + sRsvpNumOfSeats + " invited : " + iNumInvited );
				
				Text errorText = new ErrorText("Guest's RSVP number should be less than invited seats","rsvp_num_of_seats") ;		
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