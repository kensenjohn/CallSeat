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
				<h2 class="txt txt_center">Pricing Plan</h2>
				<div class="row">							
					<div class="span8">
						<form id="frm_pricing_plan" >
							<fieldset>
								<div class="clearfix-tight">
								<label class="radio" >
									<input type="radio" name="optionsRadios"  id="pricing_plan_1" value="pricing_plan_1">
									Pricing Plan 1
								</label>
								</div>
								<div class="clearfix-tight">
								<label class="radio" >
									<input type="radio" name="optionsRadios"  id="pricing_plan_2" value="pricing_plan_2">
									Pricing Plan 2
								</label>
								</div>
								<div class="clearfix-tight">
								<label class="radio" >
									<input type="radio" name="optionsRadios"  id="pricing_plan_3" value="pricing_plan_3">
									Pricing Plan 3
								</label>
								</div>
								<div class="clearfix-tight">
								<label class="radio" >
									<input type="radio" name="optionsRadios"  id="pricing_plan_4" value="pricing_plan_4">
									Pricing Plan 4
								</label>
								</div>
								<div class="actions">									
								            <button id="btn_pricing_plan" name="btn_pricing_plan" type="button"
								            	 class="action_button primary large">Payment and Billing</button>
								</div>
								          
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
			<form id="frm_telnum_bill_passthru" id="frm_telnum_bill_passthru">
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
					<input type="hidden" id="pricing_plan" name="pricing_plan" value=""/>
					
					<input type="hidden" id="pass_thru_rsvp_num" name="pass_thru_rsvp_num" value="<%=sPassthruRsvpNumber%>"/>
					<input type="hidden" id="pass_thru_seating_num" name="pass_thru_seating_num" value="<%=sPassthruSeatingNumber%>"/>
					<input type="hidden" id="referrer_source" name="referrer_source" value="pricing_plan.jsp"/>
					<input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>
					
			</form>
</body>
<script type="text/javascript">

var varAdminId = '<%=sAdminId%>';
var varEventId = '<%=sEventId%>';
$(document).ready(function() 
{
	$('#btn_pricing_plan').click(submitPricingPlan);
});
function submitPricingPlan()
{
	if( $('input[name=optionsRadios]:checked', '#frm_pricing_plan').val() != undefined )
	{
		alert($('#pass_thru_rsvp_num').val()+ '- ' + $('#pass_thru_seating_num').val());
		if( $('#pass_thru_rsvp_num').val()!='' && $('#pass_thru_seating_num').val()!='' )
		{
			$('#frm_telnum_bill_passthru').attr('action','billing.jsp');
			$('#frm_telnum_bill_passthru').attr('method','POST');
			$('#frm_telnum_bill_passthru').submit();
		}
	}
	else
	{
		
	}
	
}
</script>