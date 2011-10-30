<%@page import="java.util.*" %>
<%@page import="com.gs.common.*" %>
<%@page import="com.gs.bean.twilio.*" %>
<%@page import="com.gs.twilio.*" %>
<%@page import="org.json.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page trimDirectiveWhitespaces="true"%>


<%
	Logger jspLogging = LoggerFactory.getLogger("JspLogging");
	Logger appLogging = LoggerFactory.getLogger("AppLogging");
	
	IncomingCallBean incomingCallBean = IncomingCallManager.getIncomingCallRequest(request);
	appLogging.info(incomingCallBean.toString());
	
	IncomingCallManager incomingCallManager = new IncomingCallManager();
	
	
	response.setContentType("text/xml");
	out.println(incomingCallManager.processCall(incomingCallBean));
	
%>