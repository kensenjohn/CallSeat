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
	String sNumOfSeats =  ParseUtil.checkNull(request.getParameter("num_of_seats"));
	String sCellNumber =  ParseUtil.checkNull(request.getParameter("cell_num"));
	
	if(sFirstName==null || "".equalsIgnoreCase(sFirstName) || sCellNumber==null || "".equalsIgnoreCase(sCellNumber)
			|| sNumOfSeats==null || "".equalsIgnoreCase(sNumOfSeats) )
	{
		jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);
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
			userInfoBean.setEmail(sEmailAddr);
			
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
				guestBean.setTotalSeat(sNumOfSeats);
				
				GuestManager guestManager = new GuestManager();
				
				guestBean = guestManager.createGuest(guestBean);
				
				if (guestBean!=null && guestBean.getGuestId() !=null && !"".equalsIgnoreCase(guestBean.getGuestId()))
				{
					appLogging.info("Guest creation was successful : " + guestBean.getGuestId());
					jsonResponseObj.put(Constants.J_RESP_SUCCESS,true);
				} else
				{
					appLogging.error("Error creating Guest " + guestBean.getGuestId());
					jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);
				}
				
			}
			
		}
		else
		{
			jsonResponseObj.put(Constants.J_RESP_SUCCESS,false);
		}
	}
	out.println(jsonResponseObj.toString());
}
catch(Exception e)
{
	jsonResponseObj.put(Constants.J_RESP_SUCCESS, false);
	appLogging.error("Error creating guest " );
	out.println(jsonResponseObj);
}
%>