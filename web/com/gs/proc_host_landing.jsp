<%@ page import="org.json.JSONObject"%>
<%@ page import="com.gs.common.Utility" %>
<%@ page import="com.gs.common.DateSupport" %>
<%@ page import="com.gs.common.ParseUtil" %>
<%@include file="/web/com/gs/common/security_proc_page.jsp"%>
<%

	JSONObject jsonParentObj = new JSONObject();
    try{
        String sEmail = ParseUtil.checkNull(request.getParameter("tmp_email"));

        if(!Utility.isNullOrEmpty(sEmail) && sEmail.contains("@") ) {
            jsonParentObj.put("success",true);
        }   else {
            jsonParentObj.put("success",false);
        }
    } catch (Exception e) {
        jsonParentObj.put("success",true);
    }

	
	out.println( jsonParentObj.toString() );
	
%>
