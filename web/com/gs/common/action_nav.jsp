<%
String sEventDate = request.getParameter("hid_event_date");
String sAdminId = request.getParameter("admin_id");
String sEventId = request.getParameter("event_id");

%>
<div class="horiz_nav" style="padding:1px;">
	
	<ul>
		<li>
			<a class="action_button" href="../event/add_table.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_table">Add Table</a>
		</li>
		<li>
			<a class="action_button" href="../event/add_guest.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>" id="add_guest">Add Guest</a>
		</li>
		<li>
			<a class="action_button" href="http://blog.uber.com" id="">Save</a>
		</li> 
	</ul>
</div>