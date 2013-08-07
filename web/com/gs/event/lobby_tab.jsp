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
    if(sHeaderTxt!=null && sHeaderTxt.length()>13) {
        sHeaderTxt = sHeaderTxt.substring(0,13);
    }
	String sSecHeaderTxt = ParseUtil.checkNull(request.getParameter("lobby_sec_header"));
	
	int iSpanNum = 5;

%>
		<div class="row">
			<div class="span1">
				&nbsp;
			</div>
		</div>
		
		<div class="row" id="lobby_link" >
			<span class="span4" >
				<span id="primary_header" class="h_big" style="margin-left:18px;"><%=sHeaderTxt%></span>
                &nbsp;&nbsp;<span id="secondary_header" style="font-size:110%; color:#324B38;"><%=sSecHeaderTxt%></span>

			</span>
			<span class="span3" style="margin-left:45px;" >
				<input type="button" class="btn ispn3 btn-green btn-large" style="margin-top: 0px;margin-bottom: 0px;" id="lnk_new_event_id" name="lnk_new_event_id" value="New Seating Plan"/>
			</span>
            <span class="span2" style="text-align:center;margin-top:10px" >
                <a id="lnk_guest_id" name="lnk_guest_id" style="font-size:130%;text-decoration: none;" class="bold_text" href="#" >My Guests</a>
			</span>
			<span class="span2"  style="text-align:center;margin-top:10px" >
                <a id="lnk_dashboard_id" name="lnk_dashboard_id"  style="font-size:130%;text-decoration: none;"  class="bold_text" href="#" >My Seating Plans</a>
			</span>
		</div>
<form id="frm_lobby_tab" method="POST" action="">
	<input type="hidden" id="lobby_event_id"  name="lobby_event_id" value=""/>
	<input type="hidden" id="host_lobby_admin_id"  name="host_lobby_admin_id" value=""/>
	<input type="hidden" id="lobby_admin_id"  name="lobby_admin_id" value=""/>
	<input type="hidden" id="lobby_create_new"  name="lobby_create_new" value=""/>
</form>