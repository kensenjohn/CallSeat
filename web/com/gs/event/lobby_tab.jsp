<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%

	String sSelectedTab = ParseUtil.checkNull(request.getParameter("select_tab"));
%>
<div style="width:100%;">
	<ul class="lobby_tabs">
		<li class="blank show_base select">&nbsp;</li>
<%
	if( "event_tab".equalsIgnoreCase(sSelectedTab) )
	{
%>
		<li id="lnk_event_id" class="event_button select rounded">New Event</li>
<%
	}
	else
	{
%>
		<li id="lnk_event_id" class="event_button unselect rounded show_base">New Event</li>
<%
	}
%>
		<li class="blank show_base select">&nbsp;</li>	
<%
	if( "guest_tab".equalsIgnoreCase(sSelectedTab) )
	{
%>
		<li id="lnk_guest_id" class="normal_button select rounded">Guests</li>
<%
	}
	else
	{
%>
		<li id="lnk_guest_id" class="normal_button unselect rounded show_base">Guests</li>
<%
	}
%>			
		
		<li class="blank show_base select">&nbsp;</li>	
		
<%
	if( "dashboard_tab".equalsIgnoreCase(sSelectedTab) )
	{
%>
		<li id="lnk_dashboard_id" class="normal_button select rounded">Dashboard</li>
<%
	}
	else
	{
%>
		<li id="lnk_dashboard_id" class="normal_button unselect rounded show_base">Dashboard</li>
<%
	}
%>	
		<li class="blank show_base select">&nbsp;</li>	
	</ul>
</div>
<form id="frm_lobby_tab" method="POST" action="">
	<input type="hidden" id="lobby_event_id"  name="lobby_event_id" value=""/>
	<input type="hidden" id="lobby_admin_id"  name="lobby_admin_id" value=""/>
</form>