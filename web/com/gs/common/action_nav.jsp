<%@ page import="com.gs.common.*" %>
<%
String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sSelectTab = ParseUtil.checkNull(request.getParameter("select_action_nav"));
boolean isLoggedIn = ParseUtil.sTob(request.getParameter("logged_in"));


%>
<div class="horiz_nav" style="padding:1px;" id='action_nav_div'>
<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<div class="row" id="table_action_nav"  style="display:none;">
			<div class="span7">
				<div class="row">
					<div class="span2 txt_center">
						<button id="add_table" name="add_table" href="../event/add_table.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="action_button default small ">Add Table</button>
					</div>
				
<%
				if(!isLoggedIn)
				{
%>
					<div class="span5 txt_center" id="get_phone_num_div">
						<button id="get_phone_num" name="get_phone_num" href="../common/credential.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="action_button success large ">Get Phone Number!!</button>
					</div>
<%
				}
%>
				</div>
			</div>
		</div>
		
		<div class="row" id="invite_guest_action_nav" style="display:none;">
			<div class="span7">
				<div class="row">
					<div class="span2 txt_center">
						<button id="add_guest" name="add_guest" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="action_button default small ">Add Guest</button>
					</div>
					<div class="span2 txt_center">
						<button id="invite_guest" name="invite_guest" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="action_button default small ">Invite Guest</button>
					</div>
					<div class="span2 txt_center">
						<button id="assign_seats" name="assign_seats" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" class="action_button default small ">Assign Seats</button>
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
			<div class="span7">
				<div class="row">
					<div class="span2 txt_center">
						<button id="add_all_guests" name="add_all_guests" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&all_guest_tab=true" class="action_button default small ">Create Guest</button>
					</div>
				</div>
			</div>
		</div>
<%
	}
	%>

</div>