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
		String sPricingPlanId = ParseUtil.checkNull(request.getParameter("pricing_plan"));
		

		String sPassthruRsvpNumber = ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"));
		String sPassthruSeatingNumber = ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"));
	
	String sGateAdminId = sAdminId;
%>
<%@include file="../common/gatekeeper.jsp"%>
	<body>
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
										<input type="text" id="credit_card_num"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_password">Security Code :</label>
									<div class="input">
										<input type="password" id="credit_card_CCV"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_password">Expiry Month :</label>
									<div class="input">
										<input type="password" id="credit_card_expire_month"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="login_password">Expiry Year :</label>
									<div class="input">
										<input type="password" id="credit_card_expire_year"/>
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
						 	 <br>								            
								            <span id="cc_err_mssg"  style="color: #9d261d;" ></span><br>
								            <span id="cc_success_mssg"  style="color: #46a546;" ></span>
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
<script type="text/javascript" src="https://js.stripe.com/v1/"></script>
<script type="text/javascript">
$(document).ready(function() 
{
	$("#bt_buy_tel_numbers").click(purchaseNumbers);
	 Stripe.setPublishableKey('pk_wATk5Gx38D4jnRXebNX5ilayLKdGY');
});

function stripeResponseHandler(status, response) {
	if (response.error) {
		alert('error')
	}
	else
	{
		// var form$ = $("#payment-form");
	        // token contains id, last4, and card type
	     var token = response['id'];
	     var last4 = response.card['last4'];
	 	
	 	
		var actionUrl = "proc_save_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_billing_info").serialize();
		dataString = dataString + '&stripe_token='+token+'&last4='+last4;
		
		phoneNumberData(actionUrl,dataString,methodType,savePhoneNumbers);
	}
}

function validateCardDetails()
{
	var isValid = true;
	var ccValidateMssg = '';
	if( isValid && !Stripe.validateCardNumber( $('#credit_card_num').val() ) )
	{
		isValid = false;
		ccValidateMssg =ccValidateMssg + 'Please use a valid credit card.' ;
	}
	
	if( isValid && !Stripe.validateExpiry($('#credit_card_expire_month').val(),  $('#credit_card_expire_year').val() ) )
	{
		isValid = false;
		ccValidateMssg = ccValidateMssg + 'Please enter a valid expiry month and year.' ;
	}
	//alert( Stripe.cardType( $('#credit_card_num').val() ) );
	if( isValid && Stripe.cardType( $('#credit_card_num').val() ) == 'Unknown'  ) 
	{
		isValid = false;
		ccValidateMssg = ccValidateMssg + 'Please use a Visa/Mastercard of Discover card.';
	}
	$('#cc_err_mssg').text(ccValidateMssg);
	return isValid;
}

function purchaseNumbers()
{
	$('#cc_err_mssg').text('');
	
	if( validateCardDetails() == true )
	{
		 Stripe.createToken({
		        number: $('#credit_card_num').val(),
		        cvc: $('#credit_card_CCV').val(),
		        exp_month: $('#credit_card_expire_month').val(),
		        exp_year: $('#credit_card_expire_year').val(),
		        address_zip: $('#bill_zip').val(),
		        address_state: $('#bill_state').val(),
		        address_country: $('#bill_country').val()
		    }, stripeResponseHandler);	
	}

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
				//alert("Success buy");
				parent.loadPhoneNumber();
				parent.$.fancybox.close();
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