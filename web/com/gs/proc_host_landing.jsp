<%@ page import="org.json.JSONObject"%>
<%@ page import="com.gs.common.Utility" %>
<%@ page import="com.gs.common.DateSupport" %>
<%@ page import="com.gs.common.ParseUtil" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%

	JSONObject jsonParentObj = new JSONObject();

	String sEventDate = ParseUtil.checkNull(request.getParameter("event_date"));

	if(!Utility.isNullOrEmpty(sEventDate) && DateSupport.isValidDate(sEventDate,"MM/dd/yyyy") ) {
        jsonParentObj.put("success",true);
		
	} else {
        jsonParentObj.put("success",false);
	}
	
	out.println( jsonParentObj.toString() );
	
%>
