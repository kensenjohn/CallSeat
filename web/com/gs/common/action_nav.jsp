<%@ page import="com.gs.common.*" %>
<%
String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sSelectTab = ParseUtil.checkNull(request.getParameter("select_action_nav"));


%>
<div class="horiz_nav" style="padding:1px;">
	
	<ul class="action_tabs">

<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li>
			<a class="action_button" href="../event/add_table.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_table">Add Table</a>
		</li>
		<li style="width:5%">
			<span>&nbsp;</span>
		</li>
<%
	}
%>

<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li  style="width:15%;">
			<a class="action_button" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_guest">Add Guest</a>
		</li>
		<li style="width:5%">
			<span>&nbsp;</span>
		</li>
<%
	}
%> 



<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li   style="width:25%;" >
			<a class="action_button" href="../event/get_a_number.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_guest">Get a Phone Number</a>
		</li>
		<li style="width:5%">
			<span>&nbsp;</span>
		</li>
<%
	}
%>

<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>		<li style="width:25%">
			<span>&nbsp;</span>
		</li>
		<li  >
			<span class="action_link" id="event_config"  name="event_config"">Event Configuration</span>
		</li>
		<li style="width:5%">
			<span>&nbsp;</span>
		</li>
<%
	}
%>

<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li style="width:15%">
			<span>&nbsp;</span>
		</li>
		<li>
			<select id="dd_view">
				<option id="opt_table_view"  value="table_view">Table View</option>
				<option id="opt_guest_view"  value="guest_view">Guest View</option>
			</select>
		</li>
		<li style="width:5%">
			<span>&nbsp;</span>
		</li>
<%
	}
%>

<%
	if("all_guest_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li>
			<a class="action_button" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&all_guest_tab=true" id="add_all_guests">Add Guest</a>
		</li>
<%
	}
%>

	<!-- 	<li>
			<a class="action_button" href="http://blog.uber.com" id="">Save</a>
		</li>  -->
	</ul>
</div>