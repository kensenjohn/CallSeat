<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.manager.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@page import="com.gs.json.*"%>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%
JSONObject jsonResponseObj = new JSONObject();
String sAdminID = ParseUtil.checkNull(request.getParameter("admin_id"));
String sGuestID = ParseUtil.checkNull(request.getParameter("guest_id"));
boolean  isLoadData = ParseUtil.sTob(request.getParameter("load_data"));
boolean  isSaveData = ParseUtil.sTob(request.getParameter("save_data"));
boolean  isInviteGuest = ParseUtil.sTob(request.getParameter("invite_guest"));
boolean  isUnInviteGuest = ParseUtil.sTob(request.getParameter("un_invite_guest"));
boolean isValidInvitationNumber = false;
boolean isValidRsvpNumber = false;
Integer iInvitedSeats = 0;
if(ParseUtil.isValidInteger(request.getParameter("invited_seats")) && ParseUtil.sToI(request.getParameter("invited_seats"))>0) {
    iInvitedSeats = ParseUtil.sToI(request.getParameter("invited_seats"));
    isValidInvitationNumber = true;
}
Integer iRsvpSeats = 0;
if(ParseUtil.isValidInteger(request.getParameter("rsvp_seats")) || "".equalsIgnoreCase(ParseUtil.checkNull(request.getParameter("rsvp_seats")))) {
    iRsvpSeats = ParseUtil.sToI(request.getParameter("rsvp_seats"));
    if(iRsvpSeats>=-1) {
        if("".equalsIgnoreCase(ParseUtil.checkNull(request.getParameter("rsvp_seats"))))  {
            iRsvpSeats = -1;
        }
        isValidRsvpNumber = true;
    }
} else if("No Response".equalsIgnoreCase(ParseUtil.checkNull(request.getParameter("rsvp_seats")))) {
    iRsvpSeats = -1;
    isValidRsvpNumber = true;
}  else if("Not Attending".equalsIgnoreCase(ParseUtil.checkNull(request.getParameter("rsvp_seats")))) {
    iRsvpSeats = 0;
    isValidRsvpNumber = true;
}
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sEventGuestId = ParseUtil.checkNull(request.getParameter("event_guest_id"));
Logger jspLogging = LoggerFactory.getLogger("JspLogging");
Logger appLogging = LoggerFactory.getLogger("AppLogging");
response.setContentType("application/json");

ArrayList<Text> arrOkText = new ArrayList<Text>();
ArrayList<Text> arrErrorText = new ArrayList<Text>();
RespConstants.Status responseStatus = RespConstants.Status.ERROR;

RespObjectProc responseObject = new RespObjectProc();


try
{
	if(sAdminID!=null && !"".equalsIgnoreCase(sAdminID) && sGuestID!=null && !"".equalsIgnoreCase(sGuestID))
	{
		if(isLoadData)
		{
			EventManager eventManager = new EventManager();
			ArrayList<EventBean> arrEventBean = eventManager.getAllEvents(sAdminID);
			
			jsonResponseObj.put("event_detail",eventManager.getEventJson(arrEventBean));
			
			GuestBean guestBean = new GuestBean();
			guestBean.setGuestId(sGuestID);
			ArrayList<GuestBean> arrGuestBean = new ArrayList<GuestBean>();
			arrGuestBean.add(guestBean);
			
			EventGuestManager eventGuestManager = new EventGuestManager();
			HashMap<String, ArrayList<EventGuestBean>>  hmEventGuestBean = eventGuestManager.getEventGuests(arrGuestBean);
			
			jsonResponseObj.put("event_guest_rows",eventGuestManager.getEventGuestsJson(hmEventGuestBean));
			
			Text okText = new OkText("All events retrieved for the admin.","my_id");		
			arrOkText.add(okText);
			responseStatus = RespConstants.Status.OK;
			
		}
		else if(isUnInviteGuest)
		{
			if(sEventId==null || "".equalsIgnoreCase(sEventId) ||  sGuestID==null || "".equalsIgnoreCase(sGuestID) )
			{
				Text errorText = new ErrorText("Invalid parameters used. Please try again with valid data.","err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
			else if(sEventGuestId==null || "".equalsIgnoreCase(sEventGuestId))
			{
				Text errorText = new ErrorText("Invalid parameters used. Please try again with valid data.","err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
			else
			{
				EventGuestMetaData eventGuestMeta = new EventGuestMetaData();
				eventGuestMeta.setEventGuestId(sEventGuestId);
				eventGuestMeta.setEventId(sEventId);
				eventGuestMeta.setGuestId(sGuestID);
				
				EventGuestManager eventGuestMan = new EventGuestManager();
				Integer iNumOfRecsDeleted = eventGuestMan.deleteGuestFromEvent(eventGuestMeta);
				
							
				if(iNumOfRecsDeleted>0)
				{

					jsonResponseObj.put("response_guest_id",sGuestID);
					jsonResponseObj.put("response_event_id",sEventId);
					jsonResponseObj.put("un_invite",true);
					
					Text okText = new OkText("The guest was removed from the event's guest list.","success_mssg");		
					arrOkText.add(okText);
					responseStatus = RespConstants.Status.OK;
				}
				else
				{
					Text errorText = new ErrorText("Your request to uninvite this guest could not be processed. Please try again later.","err_mssg") ;		
					arrErrorText.add(errorText);
					
					responseStatus = RespConstants.Status.ERROR;
				}
				
			}
		}
		else if(isInviteGuest)
		{
			if(iInvitedSeats <= 0)
			{
				Text errorText = new ErrorText("Please invite at least one guest to the event.","err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
				
			} else if(!isValidInvitationNumber ) {
                Text errorText = new ErrorText("Please enter a valid Invite number.","err_mssg") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
            } else if(!isValidRsvpNumber ) {
                Text errorText = new ErrorText("Please enter a valid RSVP number.","err_mssg") ;
                arrErrorText.add(errorText);

                responseStatus = RespConstants.Status.ERROR;
            }
			else if(iRsvpSeats > iInvitedSeats)
			{
				Text errorText = new ErrorText("Guest's RSVP is greater than invited number of seats.","err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
			else if(sEventId==null || "".equalsIgnoreCase(sEventId) ||  sGuestID==null || "".equalsIgnoreCase(sGuestID) )
			{
				Text errorText = new ErrorText("Invalid parameters used. Please try again with valid data.","err_mssg") ;		
				arrErrorText.add(errorText);
				
				responseStatus = RespConstants.Status.ERROR;
			}
			else if(iInvitedSeats>0 )
			{
				EventGuestBean  eventGuestBean = new EventGuestBean();
				eventGuestBean.setEventId(sEventId);
				eventGuestBean.setGuestId(sGuestID);
				eventGuestBean.setTotalNumberOfSeats(iInvitedSeats.toString());
				eventGuestBean.setRsvpSeats(iRsvpSeats.toString());
				
				EventGuestManager eventGuestMan = new EventGuestManager();
				int iNumOfRecs = 0;
				if(sEventGuestId==null || "".equalsIgnoreCase(sEventGuestId))
				{
					sEventGuestId = Utility.getNewGuid();
					eventGuestBean.setEventGuestId(sEventGuestId);
					eventGuestBean.setDeleteRow("0");
					eventGuestBean.setIsTemporary("0");
					iNumOfRecs = eventGuestMan.assignGuestToEvent(eventGuestBean);
				}
				else
				{
					eventGuestBean.setEventGuestId(sEventGuestId);
					iNumOfRecs = eventGuestMan.setGuestInviteRsvpForEvent(eventGuestBean);
				}
				
				
				if(iNumOfRecs>0)
				{
					jsonResponseObj.put("event_guest_id",sEventGuestId);
					jsonResponseObj.put("response_guest_id",sGuestID);
					jsonResponseObj.put("response_event_id",sEventId);

					jsonResponseObj.put("invite",true);
					
					Text okText = new OkText("Guest's invite and rsvp numbers have been updated.","success_mssg");		
					arrOkText.add(okText);
					responseStatus = RespConstants.Status.OK;
					
				}
				else
				{
					Text errorText = new ErrorText("There was an error processing your request. Please try again later.","err_mssg") ;		
					arrErrorText.add(errorText);
					
					responseStatus = RespConstants.Status.ERROR;
				}
				
			}
			else
			{
			}
		}
		else if(isUnInviteGuest)
		{
			
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
	appLogging.error("Error loading events for Admin : " + sAdminID + ExceptionHandler.getStackTrace(e));
	
	//jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error loading events for admin : " + sAdminID );
	
	Text errorText = new ErrorText("Oops!! Your request could not be processed at this time.","my_id") ;		
	arrErrorText.add(errorText);
	
	responseObject.setErrorMessages(arrErrorText);
	responseObject.setResponseStatus(RespConstants.Status.ERROR);
	responseObject.setJsonResponseObj(jsonResponseObj);
	
	out.println(responseObject.getJson());
	
}
%>