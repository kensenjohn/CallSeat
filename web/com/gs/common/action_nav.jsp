<%@ page import="com.gs.common.*" %>
<%
String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sSelectTab = ParseUtil.checkNull(request.getParameter("select_action_nav"));


%>
<div class="horiz_nav" style="padding:1px;">
	
	<ul>

<%
	if("table_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li>
			<a class="action_button" href="../event/add_table.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_table">Add Table</a>
		</li>
<%
	}
%>

<%
	if("guest_tab".equalsIgnoreCase(sSelectTab))
	{
%>
		<li>
			<a class="action_button" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_guest">Add Guest</a>
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