<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>

<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		

		String sPassthruRsvpNumber = ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"));
		String sPassthruSeatingNumber = ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"));
%>
	
		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Billing Info</h2>
					<form id="frm_billing_info" name="frm_billing_info">
						<div>
								  		<div style="display: inline-block;">
				  			<table>
				  				<tr>				  					
				  					<td>Credit/Debit</td><td><input type="text" id='cc_num'  name='cc_num' value=''/></td>
				  				</tr>
				  				<tr>
				  					<td>Security Code</td><td><input type="text" id='cc_secure_num' name='cc_secure_num'  value=''/></td>
				  				</tr>
				  			</table>
				  		</div>
				  		<div style="display: inline-block;">
				  			<table>
				  					<tr>
				  					<td>First Name</td><td><input type="text" id='bill_first_name'  name='bill_first_name' value=''/></td>
				  					</tr>
				  					<tr>
				  					<td>Middle Name</td><td><input type="text" id='bill_middle_name'  name='bill_middle_name' value=''/></td>
				  					</tr>
				  					<tr>
				  					<td>Last Name</td><td><input type="text" id='bill_last_name'  name='bill_last_name' value=''/></td>
				  					</tr>
				  				<tr>
				  					<td>Address 1</td><td><input type="text" id='bill_addr_1'  name='bill_addr_1' value=''/></td>
				  					</tr>
				  				<tr>
				  					<td>Address 2</td><td><input type="text" id='bill_addr_2'  name='bill_addr_2'  value=''/></td>
				  				</tr>
				  				<tr>
				  					<td>City</td><td><input type="text" id='bill_city'  name='bill_city'  value=''/></td>
				  				</tr>
				  				<tr>
				  					<td>Zip</td><td><input type="text" id='bill_zip'  name='bill_zip'  value=''/></td>
				  					</tr>
				  				<tr>
				  					<td>State</td><td><input type="text" id='bill_state'  name='bill_state'  value=''/></td>
				  				</tr>
				  				<tr>
				  					<td>Country</td><td><input type="text" id='bill_country'  name='bill_country'  value=''/></td>
				  				</tr>
				  			</table>
				  		</div>				  		
				  		</div>
				  		
				  		<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
						<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
						<input type="hidden" id="seating_tel_num" name="seating_tel_num"/>
						<input type="hidden" id="rsvp_tel_num" name="rsvp_tel_num"/>
						
						 <button id="bt_save_tel_num" name="bt_save_tel_num" type="button" class="action_button primary small">Save Number</button>
				  	</form>
				  </div>
				</div>
			</div>
		</div>
</body>
<script type="text/javascript">
$(document).ready(function() 
		{
		alert('billing indo');
		});
</script>