<%@ page import="com.gs.common.*" %>
<%
String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sSelectTab = ParseUtil.checkNull(request.getParameter("select_action_nav"));
boolean isLoggedIn = ParseUtil.sTob(request.getParameter("logged_in"));
String sRefererSource = ParseUtil.checkNull(request.getParameter("referrer_source"));


%>
<div class="horiz_nav" style="padding:1px;" id='action_nav_div'>
<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<div class="row" id="table_action_nav"  style="display:none;">
			<div class="span11">
				<div class="row">
					<div class="span8">
						&nbsp;
					</div>
					<div class="span3">
						<input type="button" id="add_table" name="add_table" href="../event/add_table.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="btn" value="Add Table">
					</div>
				</div>
			</div>
		</div>
		
		<div class="row" id="invite_guest_action_nav" style="display:none;">
			<div class="span11">
				<div class="row">
					<div class="span6">
						&nbsp;
					</div>
					<div class="span2" style="text-align:right;">
						<input type="button" id="add_guest" name="add_guest" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="btn ispn2" value="Add Guest">
					</div>
					<div class="span2" style="text-align:right;">
						<input type="button" id="invite_guest" name="invite_guest" href="../event/invite_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="btn ispn2" value="Invite Guest">
					</div>
				</div>
			</div>
		</div>

	<%
	}
	else if("all_guest_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<div class="row" id="all_guests_action_nav"  style="display:none;">
			<div class="span12">
				<div class="row">
					<div class="span8">
						&nbsp;
					</div>
				</div>
				<div class="row">
					<div class="offset1 span2" style="text-align:left;">
						<input type="button" id="add_all_guests" name="add_all_guests" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&all_guest_tab=true" class="btn ispn2" value="Create New Guest">
					</div>
				</div>
				<div class="row">
					<div class="span8">
						&nbsp;
					</div>
				</div>
			</div>
		</div>
<%
	}
	%>

</div>
<script>
	function assignNewEventId(varNewEventId, varTmpAdminId)
	{
		$('#add_table').attr('href','../event/add_table.jsp?admin_id='+varTmpAdminId+'&event_id='+varNewEventId);
		$('#add_guest').attr('href','../event/add_guest.jsp?admin_id='+varTmpAdminId+'&event_id='+varNewEventId);
		$('#invite_guest').attr('href','../event/invite_guest.jsp?admin_id='+varTmpAdminId+'&event_id='+varNewEventId);
		$('#add_all_guests').attr('href','../event/add_guest.jsp?admin_id='+varTmpAdminId+'&event_id='+varNewEventId);
		$('#bt_get_own_phone').attr('href','../event/search_phone_number.jsp?admin_id='+varTmpAdminId+'&event_id='+varNewEventId);
	}
</script>