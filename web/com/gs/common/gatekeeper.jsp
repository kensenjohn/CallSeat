<%@page import="com.gs.bean.*"%>
<%@page import="com.gs.manager.*"%>
<%@ page import="com.gs.common.*" %>
<%

	if(!isSignedIn)
	{
		boolean showGateErrorPage = true;
		//sGateAdminId = request.getParameter("gate_adminid");
		securityLogging.error("gatekeeper admin id -" + sGateAdminId);
		if(sGateAdminId!=null && !"".equalsIgnoreCase(sGateAdminId))
		{
			AdminManager gateAdminManager = new AdminManager();
			AdminBean gateAdminBean = gateAdminManager.getAdmin(sGateAdminId);
			
			securityLogging.error("gatekeeper admin bean -" + gateAdminBean.getIsTemporary() );
			if(ParseUtil.sTob(gateAdminBean.getIsTemporary()))
			{
				showGateErrorPage = false;
			}
			
		}
		
		securityLogging.error("gatekeeper show error page -" + showGateErrorPage);
		if(showGateErrorPage)
		{
			response.sendRedirect("/web/com/gs/common/error/error.jsp");
			return;
		}
		
	}
%>