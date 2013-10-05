<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.*"%>
<%@page import="com.gs.json.*"%>
<%@ page import="com.google.i18n.phonenumbers.PhoneNumberUtil" %>
<%@ page import="com.google.i18n.phonenumbers.Phonenumber" %>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
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
	String sEventIdSelected =  ParseUtil.checkNull(request.getParameter("dd_event_list"));

	boolean isGuestCreate =  ParseUtil.sTob(request.getParameter("is_guest_create"));
	boolean isGuestAddToEvent =  ParseUtil.sTob(request.getParameter("is_guest_add_event"));

    Integer iCountryCode = 1; // 1 -> USA & Canada
	
	boolean isError = false;
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

    String sCellNumber = "";
    if(  !Utility.isNullOrEmpty(sCellNumberHumanFormat) ) {
        sCellNumber = Utility.convertHumanToInternationalTelNum(sCellNumberHumanFormat);
    }
    appLogging.info("Human format number "+ sCellNumberHumanFormat + " cellnumber :"+sCellNumber);
	if( !Utility.isNullOrEmpty(sCellNumber) ) {
        com.google.i18n.phonenumbers.Phonenumber.PhoneNumber cellPhoneNumber = new Phonenumber.PhoneNumber();
        cellPhoneNumber.setCountryCode(iCountryCode);
        cellPhoneNumber.setNationalNumber(ParseUtil.sToL(sCellNumber.substring(2)));
        PhoneNumberUtil cellPhoneNumberUtil = PhoneNumberUtil.getInstance();
        appLogging.info("cellPhoneNumber just before checking validity :"+cellPhoneNumber);
        if(!cellPhoneNumberUtil.isValidNumber(cellPhoneNumber))
        {
            Text errorText = new ErrorText("Please enter a valid cellphone number.<br>" + cellPhoneNumber.getNationalNumber(),"cell_num") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            isError = true;
        }
	}  else  {

        Text errorText = new ErrorText("Please enter a valid cellphone number.<br>","cell_num") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
        isError = true;
    }

    appLogging.info("BEfore checking home number");
    String sHomeNumber = "";
    if( !Utility.isNullOrEmpty(sHomeNumberHumanFormat) ) {
        sHomeNumber = Utility.convertHumanToInternationalTelNum(sHomeNumberHumanFormat);
    }

    if( !Utility.isNullOrEmpty(sHomeNumber) )  {
        com.google.i18n.phonenumbers.Phonenumber.PhoneNumber homePhoneNumber = new Phonenumber.PhoneNumber();
        homePhoneNumber.setCountryCode(iCountryCode);
        homePhoneNumber.setNationalNumber(ParseUtil.sToL(sHomeNumber.substring(2)));
        PhoneNumberUtil homePhoneNumberUtil = PhoneNumberUtil.getInstance();
        if(!homePhoneNumberUtil.isValidNumber(homePhoneNumber))
        {
            Text errorText = new ErrorText("Please enter a valid home phone number.<br>","home_num") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
            isError = true;
        }
    }   else  {

        Text errorText = new ErrorText("Please enter a valid home phone number.<br>","cell_num") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;
        isError = true;
    }
	if(isGuestAddToEvent)
	{
		int iNumInvited =  ParseUtil.sToI(sInvitedNumOfSeats);
		if(sInvitedNumOfSeats==null || "".equalsIgnoreCase(sInvitedNumOfSeats) || iNumInvited<=0)
		{
			appLogging.warn("Invited number of guests was invalid : " + sInvitedNumOfSeats );
			
			Text errorText = new ErrorText("Please enter valid number of invited seats greater than 0.","invited_num_of_seats") ;
			arrErrorText.add(errorText);
			
			responseStatus = RespConstants.Status.ERROR;
			isError = true;
		}
		
		
		
		int iNumRsvp =  ParseUtil.sToI(sRsvpNumOfSeats);
		if(sRsvpNumOfSeats==null || "".equalsIgnoreCase(sRsvpNumOfSeats) || iNumRsvp<0){
            sRsvpNumOfSeats = "-1";
        }
	}
	String sMessage = "";

	if(!isError)
	{
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
		{
            UserInfoManager userInforManager = new UserInfoManager();

            ArrayList<UserInfoBean> arrCellPhoneUserInfoBean = userInforManager.getUserInfoBeanByCellPhone(sCellNumber, sAdminId );
            ArrayList<UserInfoBean> arrHomePhoneUserInfoBean = userInforManager.getUserInfoBeanByHomePhone(sHomeNumber, sAdminId );

            if((arrCellPhoneUserInfoBean==null || arrCellPhoneUserInfoBean.isEmpty()) && (arrHomePhoneUserInfoBean == null || arrHomePhoneUserInfoBean.isEmpty()) )
            {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setUserInfoId(Utility.getNewGuid());
                userInfoBean.setFirstName(sFirstName);
                userInfoBean.setLastName(sLastName);
                userInfoBean.setCellPhone(sCellNumber);
                userInfoBean.setPhoneNum(sHomeNumber);
                userInfoBean.setEmail(sEmailAddr);
                userInfoBean.setCreateDate(DateSupport.getEpochMillis());
                userInfoBean.setHumanCreateDate(DateSupport.getUTCDateTime());



                userInfoBean = userInforManager.createUserInfoBean(userInfoBean);

                if(userInfoBean!=null && userInfoBean.getUserInfoId()!=null && !"".equalsIgnoreCase(userInfoBean.getUserInfoId()))
                {
                    GuestBean guestBean = new GuestBean();
                    guestBean.setGuestId(Utility.getNewGuid());
                    guestBean.setUserInfoId(userInfoBean.getUserInfoId());
                    guestBean.setAdminId(sAdminId);
                    guestBean.setCreateDate(DateSupport.getEpochMillis());
                    guestBean.setIsTemporary("1");
                    guestBean.setDeleteRow("0");
                    guestBean.setTotalSeat(sInvitedNumOfSeats);
                    guestBean.setRsvpSeat(sRsvpNumOfSeats);
                    guestBean.setHumanCreateDate(DateSupport.getUTCDateTime());

                    GuestManager guestManager = new GuestManager();

                    guestBean = guestManager.createGuest(guestBean);



                    if (guestBean!=null && guestBean.getGuestId() !=null && !"".equalsIgnoreCase(guestBean.getGuestId()))
                    {
                        jsonResponseObj.put("guest_id",guestBean.getGuestId());

                        if(sEventIdSelected!=null && !"".equalsIgnoreCase(sEventIdSelected) && !"all".equalsIgnoreCase(sEventIdSelected))
                        {


                            EventGuestBean eventGuestBean = new EventGuestBean();
                            eventGuestBean.setEventGuestId(Utility.getNewGuid());
                            eventGuestBean.setEventId(sEventIdSelected);
                            eventGuestBean.setGuestId( guestBean.getGuestId() );
                            eventGuestBean.setTotalNumberOfSeats( sInvitedNumOfSeats );
                            eventGuestBean.setRsvpSeats( sRsvpNumOfSeats );
                            eventGuestBean.setIsTemporary("1");
                            eventGuestBean.setDeleteRow("0");

                            EventGuestManager eventGuestManager = new EventGuestManager();
                            Integer iNumOfEventGuestRecs = eventGuestManager.assignGuestToEvent( eventGuestBean );

                            if(iNumOfEventGuestRecs<=0)
                            {
                                sMessage = "The guest could not be added to Event";
                                jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);

                                appLogging.warn("The guest could not be added to Event");

                                Text errorText = new ErrorText("The guest could not be added to Event","err_mssg") ;
                                arrErrorText.add(errorText);

                                responseStatus = RespConstants.Status.ERROR;
                                isError = true;
                            }
                            else
                            {
                                Text okText = new OkText("The guest was successfully assigned  to the event.","err_mssg") ;
                                arrErrorText.add(okText);

                                responseStatus = RespConstants.Status.OK;

                                jsonResponseObj.put("event_id",guestBean.getGuestId());
                                jsonResponseObj.put(Constants.J_RESP_SUCCESS,true);
                            }
                        }
                        else if( "all".equalsIgnoreCase(sEventIdSelected) || "".equalsIgnoreCase(sEventIdSelected) )
                        {
                            appLogging.info("Guest creation was successful : " + guestBean.getGuestId());
                            Text okText = new OkText("The guest was created successfully.","err_mssg") ;
                            arrErrorText.add(okText);

                            responseStatus = RespConstants.Status.OK;
                        }


                    } else
                    {
                        sMessage = "The guest could not be created at this time. Please try again later.";
                        appLogging.error("Error creating Guest " + guestBean.getGuestId());

                    }

                }
            }
            else
            {
                sMessage =  "A guest with the same";
                boolean isFirst = true;
                if( arrCellPhoneUserInfoBean != null &&  !arrCellPhoneUserInfoBean.isEmpty())
                {
                    sMessage = sMessage + " cellphone number ";
                    isFirst = false;
                }

                if( arrHomePhoneUserInfoBean != null &&  !arrHomePhoneUserInfoBean.isEmpty())
                {
                    if(!isFirst)
                    {
                        sMessage = sMessage + "and";
                    }
                    sMessage = sMessage + " home phone number ";
                }
                sMessage = sMessage + "exists. Please  click on \"Invite Guest\" to edit this guest.";

                Text errorText = new ErrorText(sMessage,"err_mssg") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;

                appLogging.error("Guest with same cellphone or home phone number already exists.");
            }
			
		}
		else
		{
			sMessage = "The Viewer could not be identified.";
		}
	}
	
	appLogging.error("Response " + sEventId + " table : " + jsonResponseObj.toString());
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
	//out.println(jsonResponseObj.toString());
}
catch(Exception e)
{
	Text errorText = new ErrorText("We were unable to complete your request. Please try again later.","err_mssg") ;
	arrErrorText.add(errorText);
	
	responseStatus = RespConstants.Status.ERROR;
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setOkMessages(arrOkText);
	responseObject.setResponseStatus(responseStatus);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	appLogging.error("Error creating guest " + ExceptionHandler.getStackTrace(e) );
}
%>