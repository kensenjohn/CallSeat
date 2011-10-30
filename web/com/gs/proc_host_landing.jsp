<%@ page import="org.json.JSONObject"%>

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
