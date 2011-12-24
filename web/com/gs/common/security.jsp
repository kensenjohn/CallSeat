<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.gs.common.*"%>
<%@page import="com.gs.bean.*"%>
<%@page import="com.gs.manager.*"%>
<%@page import="java.util.*"%>  
<% request.setCharacterEncoding("UTF-8"); %>
<% response.setHeader("X-XSS-Protection", "0"); %>
<% response.setHeader("X-CONTENT-TYPE-OPTIONS", "NOSNIFF"); %>
<% response.setHeader("X-DOWNLOAD-OPTIONS","NOOPEN"); %>
<% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); %>
<% response.setHeader("Pragma", "no-cache"); %>
<% response.setDateHeader("Expires", 0); %>

	<%
		//This section will identify whether there was insecure params used in a request
  		String sErrorParam = com.gs.common.ParseUtil.checkNull( (String)request.getAttribute("INSECURE_PARAMS_ERROR") );
  		boolean isSecurityError = com.gs.common.ParseUtil.sTob(sErrorParam) ;
  	
  		if(isSecurityError)
  		{
  			response.sendRedirect("error/error.jsp");
  		}
  		
  		//This one checks to see whether the user was logged in.
  		boolean isFromCookie = ParseUtil.sTob(ParseUtil.checkNullObject(session.getAttribute("from_cookie")));
  		if(isFromCookie)
  		{
  			session.removeAttribute("from_cookie");  //reset the session.
  		}
  		
  		boolean isSignedIn = false;
  		HttpSession  reqSession = request.getSession(false);
  		if(reqSession!=null)
  		{
  			AdminBean adminBean = (AdminBean) reqSession.getAttribute(Constants.USER_SESSION);
  			
  			
  			String sFirstName = "";
  			if( adminBean!=null && adminBean.isAdminExists() )
  			{
  				isSignedIn = true;
  			}
  			
  			if(isFromCookie && !isSignedIn)
  			{
  				String sUserId = ParseUtil.checkNullObject(session.getAttribute("u_id"));
  				
  				
  				if(sUserId!=null && !"".equalsIgnoreCase(sUserId))
  				{
  					session.removeAttribute("u_id"); //reset the session.
  					 AdminManager adminManager = new AdminManager();
  					 adminBean = adminManager.getAdmin(sUserId);
  				}
  			}
  			
  			if( adminBean!=null && adminBean.isAdminExists() )
  			{
  				isSignedIn = true;
  				
  				
  				reqSession.setAttribute(Constants.USER_SESSION,adminBean);
  			}
  		}
  	%>