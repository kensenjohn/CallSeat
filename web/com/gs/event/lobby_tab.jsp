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
<div style="width:100%;clear:both;margin-left:5px;margin-right:5px;">
	
	<section id="lobby_section">
		<div class="row">
		
			<div class="span6">
				<div class="row">
					<div class="span4">
						<h2 class="txt" id="primary_header"><%=sHeaderTxt%></h2>
					</div>
					<div class="span2">
						<h4 class="txt" id="secondary_header"><%=sSecHeaderTxt%></h4>
					</div>
				</div>
			</div>
			<div class="span10">
				<div class="row">
					<div class="span3 txt_center">
						&nbsp;
					</div>
					<div class="span3 txt_center">
						<button id="lnk_guest_id" name="lnk_guest_id" class="action_button primary large ">All Guests</button>
					</div>
					<div class="span3 txt_center">
						<button id="lnk_dashboard_id" name="lnk_dashboard_id" class="action_button primary large ">Lobby</button>
					</div>
				</div>			
			</div>
		</div>
		
	</section>
</div>
<form id="frm_lobby_tab" method="POST" action="">
	<input type="hidden" id="lobby_event_id"  name="lobby_event_id" value=""/>
	<input type="hidden" id="lobby_admin_id"  name="lobby_admin_id" value=""/>
</form>