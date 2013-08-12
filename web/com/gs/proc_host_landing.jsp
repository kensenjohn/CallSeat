<%@ page import="org.json.JSONObject"%>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%

	JSONObject jsonParentObj = new JSONObject();

	String sEventDate = request.getParameter("event_date");

	if(sEventDate==null || "".equalsIgnoreCase(sEventDate))
	{
		jsonParentObj.put("success",false);
		
		
		
	}
	else
	{
		jsonParentObj.put("success",true);
		
		//response.sendRedirect("event/event_setup.jsp");
		//response.sendRedirect("moat_processor.jsp");
	}
	
	out.println( jsonParentObj.toString() );
	
%>
