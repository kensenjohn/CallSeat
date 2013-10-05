<%@ page import="com.gs.common.*" %>
<%
	String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
%>
<div class="horiz_nav" style="padding:1px; margin-left:10px" id="div_tab_nav">

	<div id="boxtab-blue">
		<ul>
			<li  id="li_event_summary"  class="active" >
				<a  id="event_summary_tab"  >Seating Plan Summary</a>
			</li>
            <li  id="li_guest_view" >
                <a  id="guest_view_tab">Invite Guests</a>
            </li>
			<li id="li_table_view">
				<a id="table_view_tab" >Add/Edit Tables</a>
			</li>
			<li  id="li_phone_num">
				<a  id="phone_num_tab">Phone Numbers</a>
			</li>
            <li  id="li_email">
                <a  id="email_tab">Email Guests</a>
            </li>
		</ul>
	</div>
</div>
