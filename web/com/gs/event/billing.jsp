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
		String sPricingPlanId = ParseUtil.checkNull(request.getParameter("pricing_plan"));
		

		String sPassthruRsvpNumber = ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"));
		String sPassthruSeatingNumber = ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"));
%>
	
		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Billing Info</h2>
				<form id="frm_billing_info" name="frm_billing_info">
					<fieldset>
					<div class="span14">
					<div class="row">							
						<div class="span7">	
							<fieldset>
								<div class="clearfix-tight">
									<label for="login_email">Credit/Debit :</label>
									<div class="input">
										<input type="text" id="login_email" name="login_email"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_password">Security Code :</label>
									<div class="input">
										<input type="password" id="login_password" name="login_password"/>
									</div>
								</div>
							</fieldset>								
				  		</div>
				  		<div class="span7">	
				  			<fieldset>
								<div class="clearfix-tight">
									<label for="login_email">First Name :</label>
									<div class="input">
										<input type="text" id="bill_first_name" name="bill_first_name"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">Middle Name :</label>
									<div class="input">
										<input type="text" id="bill_middle_name" name="bill_middle_name"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">Last Name :</label>
									<div class="input">
										<input type="text" id="bill_last_name" name="bill_last_name"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">Address 1 :</label>
									<div class="input">
										<input type="text" id="bill_addr_1" name="bill_addr_1"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">Address 2 :</label>
									<div class="input">
										<input type="text" id="bill_addr_2" name="bill_addr_2"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">City :</label>
									<div class="input">
										<input type="text" id="bill_city" name="bill_city"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">Zip :</label>
									<div class="input">
										<input type="text" id="bill_zip" name="bill_zip"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">State :</label>
									<div class="input">
										<input type="text" id="bill_state" name="bill_state"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_email">Country :</label>
									<div class="input">
										<input type="text" id="bill_country" name="bill_country"/>
									</div>
								</div>
							</fieldset>		
				  			<div>
				  		</div>				  		
				  		</div>
				  		</div>
				  		</div>
				  		<div class="actions">	
						 	<button id="bt_buy_tel_numbers" name="bt_buy_tel_numbers" type="button" class="action_button primary big">Purchase my numbers</button>
						 </div>
				  		
				  		<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
						<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
						<input type="hidden" id="seating_tel_num" name="seating_tel_num"  value="<%=sPassthruSeatingNumber%>"/>
						<input type="hidden" id="rsvp_tel_num" name="rsvp_tel_num"  value="<%=sPassthruRsvpNumber%>"/>
						<input type="hidden" name="pricing_plan_id" name="pricing_plan_id" value="<%=sPricingPlanId%>"/>
				  		</fieldset>				  	
				  	</form>
				  </div>
				</div>
</body>
<script type="text/javascript">
$(document).ready(function() 
{
	$("#bt_buy_tel_numbers").click(purchaseNumbers);
});

function purchaseNumbers()
{
	var actionUrl = "proc_save_phone_numbers.jsp";
	var methodType = "POST";
	
	//seating_tel_num  rsvp_tel_num
	//$("#rsvp_tel_num").val($("#rsvp_gen_num").text());
	//$("#seating_tel_num").val($("#seating_gen_num").text());
	
	var dataString = $("#frm_billing_info").serialize();		
	
	phoneNumberData(actionUrl,dataString,methodType,savePhoneNumbers);
}
function phoneNumberData(actionUrl,dataString,methodType,callBackMethod)
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
function savePhoneNumbers(jsonResult)
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
				//processTelNumbers( jsonResponseObj );
				alert("Success buy");
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
function displayMessages(varArrMessages)
{
	if(varArrMessages!=undefined)
	{
		for(var i = 0; i<varArrMessages.length; i++)
		{
			alert( varArrMessages[i].text );
		}
	}
}
</script>