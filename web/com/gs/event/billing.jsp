<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<link type="text/css" rel="stylesheet" href="/web/css/jquery.select-to-autocomplete.css" /> 

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
	<body style="height:auto;">
		<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
			<div  style="padding-top:5px;">
				<div class="logo span4"><a href="#">CallSeat</a></div>
			</div>
		</div>
		<div  class="fnbx_scratch_area">
			<div class="row">
				  <div class="offset1 span11">
				  	<h2 class="txt txt_center">Billing Info</h2>
				  </div>
			</div>
			<div class="row">
				<div class="offset1 span11">
					&nbsp;
				</div>
			</div>
			<div class="row">
				<form id="frm_billing_info" name="frm_billing_info">
				<div class="offset1 span7">
						<div class="row">
							<div class="span5">
								Credit/Debit :
							</div>
						</div>
						<div class="row">
							<div class="span5">
								<input type="text" id="credit_card_num" name="credit_card_num"/>
							</div>
						</div>
						<div class="row">
							<div class="span5">
								Security Code :
							</div>
						</div>
						<div class="row">
							<div class="span5">
								<input type="text" id="credit_card_CCV" name="credit_card_CCV"/>
							</div>
						</div>
						<div class="row">
							<div class="span5">
								Expiry Month :
							</div>
						</div>
						<div class="row">
							<div class="span5">
								<input type="text" id="credit_card_expire_month" name="credit_card_expire_month"/>
							</div>
						</div>
						<div class="row">
							<div class="span5">
								Expiry Year :
							</div>
						</div>
						<div class="row">
							<div class="span5">
								<input type="text" id="credit_card_expire_year" name="credit_card_expire_year"/>
							</div>
						</div>
					</div>
				<div class="span6">
					<div class="row">
						<div class="span5">
							First Name :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_first_name" name="bill_first_name"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Middle Name :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_middle_name" name="bill_middle_name"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Last Name :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_last_name" name="bill_last_name"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Address 1 :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_addr_1" name="bill_addr_1"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Address 2 :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_addr_2" name="bill_addr_2"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							City :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_city" name="bill_city"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Zip :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_zip" name="bill_zip"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							State :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<select  id="bill_state" name="bill_state" >
							<option value="" selected="selected"></option>
							<option value="AL">Alabama</option>
							<option value="AK">Alaska</option>
							<option value="AZ">Arizona</option>
							<option value="AR">Arkansas</option>
							<option value="CA">California</option>
							<option value="CO">Colorado</option>
							<option value="CT">Connecticut</option>
							<option value="DE">Delaware</option>
							<option value="DC">District of Columbia</option>
							<option value="FL">Florida</option>
							<option value="GA">Georgia</option>
							<option value="HI">Hawaii</option>
							<option value="ID">Idaho</option>
							<option value="IL">Illinois</option>
							<option value="IN">Indiana</option>
							<option value="IA">Iowa</option>
							<option value="KS">Kansas</option>
							<option value="KY">Kentucky</option>
							<option value="LA">Louisiana</option>
							<option value="ME">Maine</option>
							<option value="MD">Maryland</option>
							<option value="MA">Massachusetts</option>
							<option value="MI">Michigan</option>
							<option value="MN">Minnesota</option>
							<option value="MS">Mississippi</option>
							<option value="MO">Missouri</option>
							<option value="MT">Montana</option>
							<option value="NE">Nebraska</option>
							<option value="NV">Nevada</option>
							<option value="NH">New Hampshire</option>
							<option value="NJ">New Jersey</option>
							<option value="NM">New Mexico</option>
							<option value="NY">New York</option>
							<option value="NC">North Carolina</option>
							<option value="ND">North Dakota</option>
							<option value="OH">Ohio</option>
							<option value="OK">Oklahoma</option>
							<option value="OR">Oregon</option>
							<option value="PA">Pennsylvania</option>
							<option value="RI">Rhode Island</option>
							<option value="SC">South Carolina</option>
							<option value="SD">South Dakota</option>
							<option value="TN">Tennessee</option>
							<option value="TX">Texas</option>
							<option value="UT">Utah</option>
							<option value="VT">Vermont</option>
							<option value="VA">Virginia</option>
							<option value="WA">Washington</option>
							<option value="WV">West Virginia</option>
							<option value="WI">Wisconsin</option>
							<option value="WY">Wyoming</option>
						</select>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							&nbsp;
						</div>
					</div>
				</div>
				  		<input type="hidden" id="bill_country" name="bill_country" value="USA"/>
				  		<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
						<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
						<input type="hidden" id="seating_tel_num" name="seating_tel_num"  value="<%=sPassthruSeatingNumber%>"/>
						<input type="hidden" id="rsvp_tel_num" name="rsvp_tel_num"  value="<%=sPassthruRsvpNumber%>"/>
						<input type="hidden" name="pricing_plan_id" name="pricing_plan_id" value="<%=sPricingPlanId%>"/>
				</form>
				</div>
					<div class="row">
						<div class="offset3 span5">
							<button id="bt_buy_tel_numbers" name="bt_buy_tel_numbers" type="button" class="btn">Purchase my numbers</button>
						</div>
						<div class="span5">
							<span id="status_mssg"  name="status_mssg"></span>
						</div>
						
					</div>
					
					<div class="row">
						<div class="span5">
							&nbsp;
						</div>
					</div>
			</div>
	<div id="loading_wheel" style="display:none;">
		<img src="/web/img/wheeler.gif">
	</div>
</body>
<script type="text/javascript" src="https://js.stripe.com/v1/"></script>
<script type="text/javascript" src="/web/js/jquery.select-to-autocomplete.js"></script>
<script type="text/javascript" src="/web/js/jquery-ui-1.8.13.custom.min.js"></script>

<script type="text/javascript">
$(document).ready(function() 
{
	$("#loading_wheel").hide();
	$('#bill_state').selectToAutocomplete();
	$("#bt_buy_tel_numbers").click(purchaseNumbers);
	 Stripe.setPublishableKey('pk_wATk5Gx38D4jnRXebNX5ilayLKdGY');
});
$.ajaxSetup({
    beforeSend: function(data) {
        $("#loading_wheel").show();
    },
    complete: function(data) {
        $("#loading_wheel").hide();
    }
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
		ccValidateMssg =ccValidateMssg + 'Please enter a valid credit card number.' ;
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
		ccValidateMssg = ccValidateMssg + 'Please use a Visa, Mastercard or Discover.';
	}
	displayErrorMessage(ccValidateMssg);
	return isValid;
}

function purchaseNumbers()
{
	$('#status_mssg').text('');
	
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
				displayMessages( varArrErrorMssg, true );
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
			alert("Your request was not processed. Please try again later.");
		}
	}
	else
	{
		alert("Please try again later.");
	}
}

function displayErrorMessage(varMessage)
{
	$("#status_mssg").addClass("error_mssg");
	$("#status_mssg").addClass("small");
	$("#status_mssg").text( varMessage );
}
function displayMessages(varArrMessages, isError)
{
	if(varArrMessages!=undefined)
	{
		$("#status_mssg").removeClass();
		if(isError == true )
		{
			$("#status_mssg").addClass("error_mssg");
		}
		else
		{
			$("#status_mssg").addClass("success_mssg");
		}
		for(var i = 0; i<varArrMessages.length; i++)
		{
			$("#status_mssg").text( varArrMessages[i].text );
			//alert( varArrMessages[i].text );
		}
	}
}
</script>