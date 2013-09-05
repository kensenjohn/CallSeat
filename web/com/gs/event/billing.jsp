<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.manager.event.PurchaseTransactionManager" %>
<%@ page import="com.gs.payment.PaymentChannel" %>
<%@ page import="com.gs.user.Permission" %>
<%@ page import="com.gs.user.User" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<link type="text/css" rel="stylesheet" href="/web/css/jquery.select-to-autocomplete.css" /> 

<%
    Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sPricingPlanId = ParseUtil.checkNull(request.getParameter("pricing_plan"));


    String sPassthruRsvpNumber = ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"));
    String sPassthruSeatingNumber = ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"));

    PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
    purchaseTransactionBean.setAdminId(sAdminId);
    purchaseTransactionBean.setEventId(sEventId);

    PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
    PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);

    String sFirstName = "";
    String sLastName = "";
    String sZipCode = "";
    String sState = "";
    String sApiKeyType = Constants.API_KEY_TYPE.LIVE_KEY.name();
    if(purchaseResponseTransactionBean!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getPurchaseTransactionId()))
    {
        if(purchaseResponseTransactionBean.getFirstName()!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getFirstName()) )
        {
            sFirstName = ParseUtil.checkNull(purchaseResponseTransactionBean.getFirstName());
        }

        if(purchaseResponseTransactionBean.getLastName()!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getLastName()) )
        {
            sLastName = ParseUtil.checkNull(purchaseResponseTransactionBean.getLastName());
        }

        if(purchaseResponseTransactionBean.getState()!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getState()) )
        {
            sState = ParseUtil.checkNull(purchaseResponseTransactionBean.getState());
        }

        if(purchaseResponseTransactionBean.getZipcode()!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getZipcode()) )
        {
            sZipCode = ParseUtil.checkNull(purchaseResponseTransactionBean.getZipcode());
        }

        if(purchaseResponseTransactionBean.getApiKeyType()!=null && !"".equalsIgnoreCase(purchaseResponseTransactionBean.getApiKeyType()) ) {
            sApiKeyType = ParseUtil.checkNull(purchaseResponseTransactionBean.getApiKeyType());
        }
    }

    Long currentServerTime = DateSupport.getEpochMillis();
    Integer iCurrentYear = DateSupport.getYear(currentServerTime);

    ArrayList<Integer> arrYears = new ArrayList<Integer>();
    for(Integer iYearCount = 0; iYearCount<10;iYearCount++  )
    {
        arrYears.add(iYearCount, (iCurrentYear+iYearCount) );
    }

	String sGateAdminId = sAdminId;

    AdminManager adminManager = new AdminManager();
    AdminBean adminBean = adminManager.getAdmin(sAdminId);

    boolean hasPermToUsePayChannelTestKey = false;
    if(adminBean!=null && !Utility.isNullOrEmpty(adminBean.getAdminId())) {
        User user = new User(adminBean );
        hasPermToUsePayChannelTestKey = user.can(Permission.USE_PAYMENT_CHANNEL_TEST_API_KEY);
    }
    PaymentChannelRequest paymentChannelRequest = new PaymentChannelRequest();
    if(hasPermToUsePayChannelTestKey && Constants.API_KEY_TYPE.TEST_KEY.name().equalsIgnoreCase(sApiKeyType)) {
        paymentChannelRequest.setApiKeyType( Constants.API_KEY_TYPE.TEST_KEY );
    } else {
        paymentChannelRequest.setApiKeyType( Constants.API_KEY_TYPE.LIVE_KEY );
    }
    PaymentChannel paymentChannel = PaymentChannel.getPaymentChannel();
    String sPublishableKey = paymentChannel.getPublicKey( paymentChannelRequest );
%>
<%@include file="../common/gatekeeper.jsp"%>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
	<body style="height:auto;">
        <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
		<div  class="fnbx_scratch_area">
            <div class="row">
                <div class="offset1 span11">
                    <jsp:include page="breadcrumb_shopping_cart.jsp">
                        <jsp:param name="active_now" value="billing.jsp" />
                    </jsp:include>
                </div>
            </div>
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
				<div class="offset1 span5">
						<div class="row">
							<div class="span5">
								Credit/Debit Card Number:<br><span class="fld_txt_small" style="font-size:11px;">The digits in front of your card</span>
							</div>
						</div>
						<div class="row">
							<div class="span5">
								<input type="text" id="credit_card_num" name="credit_card_num"/>
							</div>
						</div>
                        <div class="row">
                            <div class="span5">
                                <img src="/web/img/credit_card_logos.png" alt="Visa, MasterCard, Discover, American Express logos">
                            </div>
                        </div>
                        <div class="row">
                            <div class="span5">
                               &nbsp;
                            </div>
                        </div>
						<div class="row">
							<div class="span5">
								Expiration date<br>
                                <span class="fld_txt_small" style="font-size:11px;">The date your card expires</span>
							</div>
						</div>
						<div class="row">
							<div class="span4">
                                <select id="credit_card_expire_month"   name="credit_card_expire_month">
                                    <option value="">Month</option>
                                    <option value="1">01 - Jan</option>
                                    <option value="2">02 - Feb</option>
                                    <option value="3">03 - Mar</option>
                                    <option value="4">04 - Apr</option>
                                    <option value="5">05 - May</option>
                                    <option value="6">06 - Jun</option>
                                    <option value="7">07 - Jul</option>
                                    <option value="8">08 - Aug</option>
                                    <option value="9">09 - Sep</option>
                                    <option value="10">10 - Oct</option>
                                    <option value="11">11 - Nov</option>
                                    <option value="12">12 - Dec</option>
                                </select>
                                &nbsp;&nbsp;/&nbsp;&nbsp;
                                <select id="credit_card_expire_year"   name="credit_card_expire_year">
                                    <option value="">Year</option>
                                    <%
                                        if(arrYears!=null && !arrYears.isEmpty())
                                        {
                                            for(Integer ddYear : arrYears )
                                            {
                                    %>
                                                <option value="<%=ddYear%>"><%=ddYear%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
							</div>
						</div>


                        <div class="row">
                            <div class="span5">
                                &nbsp;
                            </div>
                        </div>
                        <div class="row">
                            <div class="span5">
                                Security Code <span style="font-size:12px;">(or CVV or CVC)</span><br>
                                <span class="fld_txt_small" style="font-size:11px;">Last 3 digits on the back of the card<br>(Amex:4 digits on front.)</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="span5">
                                <input type="text" id="credit_card_CCV" name="credit_card_CCV"/>
                            </div>
                        </div>
					</div>
				<div class="span5">
					<div class="row">
						<div class="span5">
							First Name :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_first_name" name="bill_first_name" value="<%=sFirstName%>">
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Last Name :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_last_name" name="bill_last_name"  value="<%=sLastName%>">
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Zip :
						</div>
					</div>
					<div class="row">
						<div class="span5">
							<input type="text" id="bill_zip" name="bill_zip"   value="<%=sZipCode%>">
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
							<option value=""></option>
                            <%
                                SortedMap<String, Constants.US_STATES> mapStates = Constants.SORTED_US_STATES();

                                if(mapStates!=null && !mapStates.isEmpty())
                                {
                                   for( Map.Entry<String, Constants.US_STATES> entryMapUsStates : mapStates.entrySet() )
                                   {
                                       boolean isSelected = false;
                                       if( entryMapUsStates.getValue().getShortForm().equalsIgnoreCase(sState))
                                       {
                                           isSelected = true;
                                       }
                                        %>

                                            <option value="<%=entryMapUsStates.getValue().getShortForm()%>"  <%=isSelected?"selected=\"selected\"":""%>><%=entryMapUsStates.getValue().getFullName()%></option>
                                        <%
                                   }
                                }
                            %>
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
							<button id="bt_checkout_tel_numbers" name="bt_checkout_tel_numbers" type="button" class="btn">Checkout</button>
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
        <form id="frm_credential_check">
            <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
            <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>

            <input type="hidden" id="referrer_source" name="referrer_source" value="billing.jsp"/>
            <input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>
        </form>

        <form  id="frm_process_purchase_transaction">
            <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
            <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
            <input type="hidden" id="purchase_first_name" name="purchase_first_name" value=""/>
            <input type="hidden" id="purchase_last_name" name="purchase_last_name" value=""/>
            <input type="hidden" id="purchase_state" name="purchase_state" value=""/>
            <input type="hidden" id="purchase_zip_code" name="purchase_zip_code" value=""/>
            <input type="hidden" id="purchase_country" name="purchase_country" value=""/>
            <input type="hidden" id="purchase_stripe_token" name="purchase_stripe_token" value=""/>
            <input type="hidden" id="purchase_cc_last4" name="purchase_cc_last4" value=""/>
            <input type="hidden" id="process_purchase_transaction" name="process_purchase_transaction" value="true"/>
        </form>
        <form id="frm_checkout_passthru">
            <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
            <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>

            <input type="hidden" id="referrer_source" name="referrer_source" value="billing.jsp"/>
            <input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>

        </form>
</body>
<script type="text/javascript" src="https://js.stripe.com/v1/"></script>
<script type="text/javascript" src="/web/js/jquery.select-to-autocomplete.js"></script>
<script type="text/javascript" src="/web/js/jquery-ui-1.8.13.custom.min.js"></script>

<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript">
    var varIsSignedIn = <%=isSignedIn%>;

$(document).ready(function() 
{
	$("#loading_wheel").hide();
	$('#bill_state').selectToAutocomplete();
	$("#bt_checkout_tel_numbers").click(checkoutNumbers);
	 Stripe.setPublishableKey('<%=sPublishableKey%>');
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
		//alert('error');
        displayMssgBoxAlert('There was an error processing your request. Please try again later.' , true);
	}
	else
	{
        $('#purchase_first_name').val($('#bill_first_name').val());
        $('#purchase_last_name').val($('#bill_last_name').val());
        $('#purchase_state').val($('#bill_state').val());
        $('#purchase_zip_code').val($('#bill_zip').val());
        $('#purchase_country').val($('#bill_country').val());
        $('#purchase_stripe_token').val(response['id']);
        $('#purchase_cc_last4').val(response.card['last4']);
        var actionUrl = "proc_billing.jsp";
        var methodType = "POST";
        var dataString = $('#frm_process_purchase_transaction').serialize();


        phoneNumberData(actionUrl,dataString,methodType,processPurchaseTransactions);

		// var form$ = $("#payment-form");
	        // token contains id, last4, and card type
	   /*  var token = response['id'];
	     var last4 = response.card['last4'];
	 	
	 	
		var actionUrl = "proc_save_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_billing_info").serialize();
		dataString = dataString + '&stripe_token='+token+'&last4='+last4;
		
		phoneNumberData(actionUrl,dataString,methodType,savePhoneNumbers);
		*/
	}
}

    function processPurchaseTransactions(jsonResult)
    {
        if(jsonResult!=undefined)
        {
            var varResponseObj = jsonResult.response;
            if(jsonResult.status == 'error'  && varResponseObj !=undefined )
            {
                var varIsMessageExist = varResponseObj.is_message_exist;
                if(varIsMessageExist == true)
                {
                    var jsonResponseMessage = varResponseObj.messages;
                    var varArrErrorMssg = jsonResponseMessage.error_mssg;
                    displayMssgBoxMessages( varArrErrorMssg , true);
                }
            }
            else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
            {
                if( varIsSignedIn )
                {
                    $("#frm_checkout_passthru").attr('action','/web/com/gs/event/checkout.jsp');
                    $("#frm_checkout_passthru").attr('method','POST');


                    $("#frm_checkout_passthru").submit();
                }

            }
            else
            {
                displayMssgBoxAlert("Oops!! Your request was not processed. Please try again later.");
            }
        }
        $("#loading_wheel").hide();
    }

function validateCardDetails()
{
	var isValid = true;
	var ccValidateMssg = '';
	if( isValid && !Stripe.validateCardNumber( $('#credit_card_num').val() ) )
	{
		isValid = false;
		ccValidateMssg =ccValidateMssg + 'We could not identify a valid credit card number. Please try again.' ;
	}

    if( isValid && !Stripe.validateCVC( $('#credit_card_CCV').val() ) )
    {
        isValid = false;
        ccValidateMssg =ccValidateMssg + 'We could not identify a valid Security Code ( CVV / CVC ). Please try again.' ;
    }
	
	if( isValid && !Stripe.validateExpiry($('#credit_card_expire_month').val(),  $('#credit_card_expire_year').val() ) )
	{
		isValid = false;
		ccValidateMssg = ccValidateMssg + 'We could not identify a valid expiry date. Please try again.' ;
	}

    if( isValid && $('#bill_first_name').val() == ''  )
    {
        isValid = false;
        ccValidateMssg = ccValidateMssg + 'You did not enter a first name. Please enter the first name as seen on the credit card.';
    }

    if( isValid && $('#bill_last_name').val() == ''  )
    {
        isValid = false;
        ccValidateMssg = ccValidateMssg + 'You did not enter a last name. Please enter the last name as seen on the credit card.';
    }

    if( isValid && $('#bill_zip').val() == ''  )
    {
        isValid = false;
        ccValidateMssg = ccValidateMssg + 'You did not enter a valid zip code. Please use the credit card billing zip code.';
    }

    if( isValid && $('#bill_state').val() == ''  )
    {
        isValid = false;
        ccValidateMssg = ccValidateMssg + 'You did not select a state. Please select the credit card billing state.';
    }

    displayMssgBoxAlert(ccValidateMssg,true);
	return isValid;
}

function checkoutNumbers()
{
    if(varIsSignedIn)
    {
        if( validateCardDetails() == true )
        {

            $("#loading_wheel").show();
            Stripe.createToken({
                number: $('#credit_card_num').val(),
                cvc: $('#credit_card_CCV').val(),
                exp_month: $('#credit_card_expire_month').val(),
                exp_year: $('#credit_card_expire_year').val(),
                name: $('#bill_first_name').val() + ' ' + $('#bill_last_name').val(),
                address_zip: $('#bill_zip').val(),
                address_state: $('#bill_state').val(),
                address_country: $('#bill_country').val()
            }, stripeResponseHandler);
        }
    }
    else
    {
        $("#frm_credential_check").attr('action',"/web/com/gs/common/credential.jsp");
        $("#frm_credential_check").attr('method','POST');
        $("#frm_credential_check").submit();
    }

	$('#status_mssg').text('');
	


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
            displayMssgBoxAlert("Your request was not processed. Please try again later.",true);
		}
	}
	else
	{
        displayMssgBoxAlert("Please try again later.",true);
	}
}

    function displayMssgBoxAlert(varMessage, isError)
    {
        var varTitle = 'Status';
        var varType = 'info';
        if(isError)
        {
            varTitle = 'Error';
            varType = 'error';
        }
        else
        {
            varTitle = 'Status';
            varType = 'info';
        }

        if(varMessage!='')
        {
            $.msgBox({
                title: varTitle,
                content: varMessage,
                type: varType
            });
        }
    }

    function displayMssgBoxMessages(varArrMessages, isError)
    {
        if(varArrMessages!=undefined)
        {


            var varMssg = '';
            var isFirst = true;
            for(var i = 0; i<varArrMessages.length; i++)
            {
                if(isFirst == false)
                {
                    varMssg = varMssg + '\n';
                }
                varMssg = varMssg + varArrMessages[i].text;
            }

            if(varMssg!='')
            {
                displayMssgBoxAlert(varMssg,isError);
            }
        }


    }
</script>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>