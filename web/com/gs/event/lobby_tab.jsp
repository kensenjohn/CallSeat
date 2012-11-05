<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="org.json.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%

	String sSelectedTab = ParseUtil.checkNull(request.getParameter("select_tab"));
	String sHeaderTxt = ParseUtil.checkNull(request.getParameter("lobby_header"));
	String sSecHeaderTxt = ParseUtil.checkNull(request.getParameter("lobby_sec_header"));
	
	int iSpanNum = 5;

%>
		<div class="row">
			<div class="span1">
				&nbsp;
			</div>
		</div>
		
		<div class="row" >
			<span class="tab_view_span6" >
				<span id="primary_header" style="margin-left:18px;font-size:210%; color:#324B38;font-weight: bold;"><%=sHeaderTxt%></span> &nbsp;&nbsp;&nbsp;&nbsp;
				<span id="secondary_header" style="font-size:110%; color:#324B38;"><%=sSecHeaderTxt%></span>
			</span>
			<span class="span2" style="margin-left:45px;" >
				<input type="button" class="btn ispn2 btn-green btn-large" style="margin-top: 0px;margin-bottom: 0px;" id="lnk_new_event_id" name="lnk_new_event_id" value="New Seating Plan"/>
			</span>
			<div class="span4" style="background-image: none; background-color: RGBA(39,61,10,1.0); padding-bottom:6px; height: 30px;" id="mini_nav_bar">
				<div class="row" >
					<span class="tab_view_span2" style="text-align:right" >
						<input type="button" class="tran_btn ispn2" style="margin-top: 2px;" id="lnk_guest_id" name="lnk_guest_id" value="All Guests"/>
					</span>
					<span class="span2"  style="text-align:right" >
						<input type="button" class="tran_btn ispn2"  style="margin-top: 2px;" id="lnk_dashboard_id" name="lnk_dashboard_id" value="Seating Plans"/>
					</span>
				</div>
			</div>
		</div>
<form id="frm_lobby_tab" method="POST" action="">
	<input type="hidden" id="lobby_event_id"  name="lobby_event_id" value=""/>
	<input type="hidden" id="host_lobby_admin_id"  name="host_lobby_admin_id" value=""/>
	<input type="hidden" id="lobby_admin_id"  name="lobby_admin_id" value=""/>
	<input type="hidden" id="lobby_create_new"  name="lobby_create_new" value=""/>
</form>