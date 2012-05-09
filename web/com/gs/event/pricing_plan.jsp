<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>


<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		

		String sPassthruRsvpNumber = ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"));
		String sPassthruSeatingNumber = ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"));
	
	String sGateAdminId = sAdminId;
%>
<%@include file="../common/gatekeeper.jsp"%>

<body>
		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Pricing Plan</h2>
				<div class="row">							
					<div class="span8">
						<form id="frm_pricing_plan" >
							<fieldset>
								<div class="clearfix-tight">
									<div class="input">
										<input type="radio" name="optionsRadios"  id="pricing_plan_1" value="pricing_plan_1"/>
										<label class="radio" id="lbl_pricing_plan_1" style="float:right;">Pricing Plan 1</label>
									</div>
									
								</div>
								<div class="clearfix-tight">
									<div class="input">
										<input type="radio" name="optionsRadios"  id="pricing_plan_2" value="pricing_plan_2"/>
										<label class="radio" id="lbl_pricing_plan_2"  style="float:right;">Pricing Plan 2</label>
									</div>
									
								</div>
								<div class="clearfix-tight">
									<div class="input">
										<input type="radio" name="optionsRadios"  id="pricing_plan_3" value="pricing_plan_3"/>
										<label class="radio" id="lbl_pricing_plan_3"  style="float:right;">Pricing Plan 3</label>
									</div>
									
								</div>
								<div class="clearfix-tight">
									<div class="input">
										<input type="radio" name="optionsRadios"  id="pricing_plan_4" value="pricing_plan_4"/>
									</div>
									<label class="radio" id="lbl_pricing_plan_4"  style="float:right;">Pricing Plan 4</label>
								</div>
								<div class="clearfix-tight">
									<div class="input">
										<input type="radio" name="optionsRadios"  id="pricing_plan_5" value="pricing_plan_5"/>
									</div>
									<label class="radio" id="lbl_pricing_plan_5"  style="float:right;" >Pricing Plan 5</label>
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
	loadPricingPlan();
	$('#btn_pricing_plan').click(submitPricingPlan);
});

function loadPricingPlan()
{
	var actionUrl = "proc_pricing_plan.jsp";
	var methodType = "POST";
	var dataString = "admin_id="+varAdminId+"&event_id="+varEventId;
	dataString = dataString + $('#').serialize();
	
	
	pricingPlanData(actionUrl,dataString,methodType,displayPricingPlan);
}
function pricingPlanData(actionUrl,dataString,methodType,callBackMethod)
{
	$.ajax({
		  url: actionUrl ,
		  type: methodType ,
		  dataType: "json",
		  data: dataString ,
		  success: callBackMethod,
		  error:function(a,b,c)
		  {
			  alert(a.responseText + ' = ' + b + " = " + c);
		  }
		});
}
function displayPricingPlan(jsonResult)
{
	//alert('status = '+jsonResult.status);
	if(jsonResult!=undefined)
	{
		var varResponseObj = jsonResult.response;
		if(jsonResult.status == 'error'  && varResponseObj !=undefined )
		{
			
			var varIsMessageExist = varResponseObj.is_message_exist;
			if(varIsMessageExist == true)
			{
				var jsonResponseMessage = varResponseObj.messages;
				var varArrErrorMssg = jsonResponseMessage.error_mssg
				displayMessages( varArrErrorMssg );
			}
			
		}
		else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
		{
			var varIsPayloadExist = varResponseObj.is_payload_exist;
			//alert(varIsPayloadExist);
			
			if(varIsPayloadExist == true)
			{
				var jsonResponseObj = varResponseObj.payload;
				processPricingPlan( jsonResponseObj );
			}
		}
		else
		{
			alert("Please try again later.");
		}
	}
	else
	{
		alert("Please try again later.");
	}
}
function processPricingPlan(varResponse)
{
	//alert('groing to display');
	var varArrPricingPlan = varResponse.value;
	for(vari = 0 ; vari<varArrPricingPlan.length; vari++)
	{
		//alert('pricing grid. - ' + varArrPricingPlan[vari].pricing_group_id);
		$('#pricing_plan_' + (vari+1) ).val( varArrPricingPlan[vari].pricing_group_id );
		$('#lbl_pricing_plan_' + (vari+1) ).text( varArrPricingPlan[vari].pricing_group_name 
					+ '( '+ varArrPricingPlan[vari].pricing_group_name +' call mins.   ' 
					+ varArrPricingPlan[vari].pricing_group_name + ' sms )' );
	}
	
}
function submitPricingPlan()
{
	if( $('input[name=optionsRadios]:checked', '#frm_pricing_plan').val() != undefined )
	{
		//alert($('#pass_thru_rsvp_num').val()+ '- ' + $('#pass_thru_seating_num').val());
		if( $('#pass_thru_rsvp_num').val( )!='' && $('#pass_thru_seating_num').val()!='' )
		{
			$('#pricing_plan').val( $('input[name=optionsRadios]:checked', '#frm_pricing_plan').val() );
			$('#frm_telnum_bill_passthru').attr('action','billing.jsp');
			$('#frm_telnum_bill_passthru').attr('method','POST');
			$('#frm_telnum_bill_passthru').submit();
		}
	}
	else
	{
		alert('Please select at least one pricing plan');
	}
	
}
</script>