<%@page import="com.gs.manager.event.*" %>
<%@page import="com.gs.manager.*" %>
<%@page import="com.gs.bean.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%
JSONObject jsonResponseObj = new JSONObject();

Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");
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
	String sEventIdSelected =  ParseUtil.checkNull(request.getParameter("dd_event_list"));
	
	String sMessage = "";
	if(sFirstName==null || "".equalsIgnoreCase(sFirstName) || sCellNumber==null 
			|| "".equalsIgnoreCase(sCellNumber)
			|| sInvitedNumOfSeats==null || "".equalsIgnoreCase(sInvitedNumOfSeats) 
			|| sRsvpNumOfSeats==null || "".equalsIgnoreCase(sRsvpNumOfSeats)
			|| sHomeNumber==null || "".equalsIgnoreCase(sHomeNumber))
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);
		sMessage = "Please fill all the parameters before adding a guest.";
	}
	else
	{
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
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
			
			UserInfoManager userInforManager = new UserInfoManager();
			
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
						}
						else
						{
							jsonResponseObj.put(Constants.J_RESP_SUCCESS,true);
						}
					}
					
					
					appLogging.info("Guest creation was successful : " + guestBean.getGuestId());
					jsonResponseObj.put(Constants.J_RESP_SUCCESS,true);
				} else
				{
					sMessage = "The guest could not be created at this time. Please try again later.";
					appLogging.error("Error creating Guest " + guestBean.getGuestId());
					jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);
				}
				
			}
			
		}
		else
		{
			sMessage = "The Viewer could not be identified.";
			jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);
		}
	}
	out.println(jsonResponseObj.toString());
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	jsonResponseObj.put("message", "Your request to add guest was lost. Please try again later.");
	appLogging.error("Error creating guest " );
	out.println(jsonResponseObj);
}
%>