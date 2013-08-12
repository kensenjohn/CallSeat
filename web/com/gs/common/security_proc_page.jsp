<%@page contentType="application/json" %>
<%@page import="com.gs.json.*"%>
<%@page import="org.slf4j.Logger" %>
<%@page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.ArrayList" %>

<%
    String sErrorParam = com.gs.common.ParseUtil.checkNull( (String)request.getAttribute("INSECURE_PARAMS_ERROR") );
    boolean isSecurityError = com.gs.common.ParseUtil.sTob(sErrorParam) ;
    if(isSecurityError)  {
        ArrayList<Text> arrOkText = new ArrayList<Text>();
        ArrayList<Text> arrErrorText = new ArrayList<Text>();
        RespConstants.Status responseStatus = RespConstants.Status.ERROR;

        RespObjectProc responseObject = new RespObjectProc();

        Text errorText = new ErrorText("An unsafe request was identified. Please change your response and try again.","e_summ_event_name") ;
        arrErrorText.add(errorText);

        responseStatus = RespConstants.Status.ERROR;

        responseObject.setErrorMessages(arrErrorText);
        responseObject.setOkMessages(arrOkText);
        responseObject.setResponseStatus(responseStatus);

        out.println(responseObject.getJson()) ;
        return ;
    }
%>
