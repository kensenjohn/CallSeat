<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.manager.event.PurchaseTransactionManager" %>
<%@ page import="com.gs.user.User" %>
<%@ page import="com.gs.user.Permission" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>


<%
		Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));

    String sGateAdminId = sAdminId;
    AdminManager adminManager = new AdminManager();
    AdminBean adminBean = adminManager.getAdmin(sAdminId);

    boolean hasPermToUsePayChannelTestKey = false;
    String sUserEmail = Constants.EMPTY;
    if(adminBean!=null && !Utility.isNullOrEmpty(adminBean.getAdminId())) {
        User user = new User(adminBean );
        hasPermToUsePayChannelTestKey = user.can(Permission.USE_PAYMENT_CHANNEL_TEST_API_KEY);
        UserInfoBean userInforBean = adminBean.getAdminUserInfoBean();
        if( userInforBean!=null && !Utility.isNullOrEmpty((userInforBean.getEmail())) ) {
            sUserEmail = userInforBean.getEmail();
        }

    }
%>
<%@include file="../common/gatekeeper.jsp"%>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<link rel="stylesheet" type="text/css" href="/web/css/style.css" media="screen" >
<body style="height:auto;">
        <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
		<div class="fnbx_scratch_area">
            <div class="row">
                <div class="offset1 span11">
                    <jsp:include page="breadcrumb_shopping_cart.jsp">
                        <jsp:param name="active_now" value="pricing_plan.jsp" />
                    </jsp:include>
                </div>
            </div>
				<div class="row">
				  <div class="offset1 span11">
						<div class="row">							
							<div class="span3">
								<h2 style="font-size:155%; color:#793866; margin : 0px; clear:both">Select a Pricing Plan</h2>
							</div>
                            <% if(hasPermToUsePayChannelTestKey)  { %>
                            <div class="span5">
                                <input type="checkbox" id="test_api" style="width:50px" checked>&nbsp;&nbsp;<span style="font-size:110%; color:#793866; margin : 0px; clear:both">Use Test API Key</span>
                            </div>
                            <% } %>
						</div>
						<div class="row">							
							<div class="span8">
								&nbsp;
							</div>
						</div>
                        <div class="row" id="pricing_grid">
                          <!-- <div class="span11">
                              <form id="frm_pricing_plan" >

                              </form>
                          </div> -->
                        </div>
                      <div class="row">
                          <div class="span12">
                              &nbsp;
                          </div>
                      </div>

                      <div class="row">
                          <div class="span12">
                              <span>1) One phone number for RSVP and guest seating. Switch between modes when required.</span>
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              <span>2) Guests can RSVP and get seating information online.</span>
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              <span>3) Send text message or email to confirm RSVP or to provide seating information.</span>
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              &nbsp;
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              <h4>Coming Soon</h4>
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              <span>Your guests will be able to select their food preference online or on the phone.</span>
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              &nbsp;
                          </div>
                      </div>
                      <div class="row">
                          <div class="span12">
                              &nbsp;
                          </div>
                      </div>
				</div>
		</div>
	</div>
			<form id="frm_billing_passthru">
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
					<input type="hidden" id="pricing_plan" name="pricing_plan" value=""/>

					<input type="hidden" id="referrer_source" name="referrer_source" value="pricing_plan.jsp"/>
                <input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>
					
			</form>

        <form id="frm_credential_check">
            <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
            <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>

            <input type="hidden" id="referrer_source" name="referrer_source" value="pricing_plan.jsp"/>
            <input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>
        </form>

        <form  id="frm_process_purchase_transaction">
            <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
            <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
            <input type="hidden" id="purchase_grid_id" name="purchase_grid_id" value=""/>
            <input type="hidden" id="process_purchase_transaction" name="process_purchase_transaction" value="true"/>
            <input type="hidden" id="use_test_api" name="use_test_api"  value=""/>
        </form>
</body>
<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/web/js/pricing/jquery-1.7.2.min.js"></script>
<script src="/web/js/pricing/bootstrap.js"></script>
<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript">

var varAdminId = '<%=sAdminId%>';
var varEventId = '<%=sEventId%>';
var varIsSignedIn = <%=isSignedIn%>;

$(document).ready(function() 
{
	loadPricingPlan();
	$('#btn_pricing_plan').click(submitPricingPlan);
    mixpanel.track('Pg pricing_plan.jsp', {'Referrer' : 'Shopping Cart','Admin id' : varAdminId ,'Event id' : varEventId });
});

function loadPricingPlan()
{
	var actionUrl = "proc_pricing_plan.jsp";
	var methodType = "POST";
	var dataString = "admin_id="+varAdminId+"&event_id="+varEventId;
	//dataString = dataString + $('#').serialize();
	
	
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
        var varPricingGrid = '';

        var varPriceClass = '';
        var varSubTitle = '&nbsp;';
        if(varArrPricingPlan[vari].is_default == true) {
            varPriceClass = 'most_popular';
            varSubTitle = 'Most Popular';
        }
        varPricingGrid = varPricingGrid + '<div  class="'+varPriceClass+' action_display" id="div_pricing_'+varArrPricingPlan[vari].pricing_group_id+'">';
        varPricingGrid = varPricingGrid + '<div class="header"><h1>'+varArrPricingPlan[vari].pricing_group_name+'</h1><span style="text-align: center;">'+varSubTitle+'</span></div>';
        varPricingGrid = varPricingGrid + '<div style="padding: 3px;">';
        varPricingGrid = varPricingGrid + '<div class="body" ><span >'+ varArrPricingPlan[vari].max_minutes +' call minutes</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body" ><span >'+ varArrPricingPlan[vari].sms_count +' txt messages</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body"><span >&nbsp;</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body" ><span >1 phone number</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body"><span >Emails and Online RSVP and seating</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body"><span >&nbsp;</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body" ><span >$'+varArrPricingPlan[vari].price+' per plan</span></div>';
        varPricingGrid = varPricingGrid + '<div class="body"><span ><input class="btn btn-blue ispn2" type="button" value="Select"></span></div>';
        varPricingGrid = varPricingGrid + '</div>';
        varPricingGrid = varPricingGrid + '</div>';

        $('#pricing_grid').append(varPricingGrid);
        $('#div_pricing_' + varArrPricingPlan[vari].pricing_group_id).click({pricingGridId: varArrPricingPlan[vari].pricing_group_id,price: varArrPricingPlan[vari].price }, submitPricingPlan);

	}
	
}

function setIsTestApiUsed() {
    if ($('#test_api').is(':checked')) {
        $('#use_test_api').val("true");
    } else {
        $('#use_test_api').val( "false");
    }

}
function submitPricingPlan(event)
{
    //alert(event.data.pricingGridId + ' -- ' + event.data.price );
    if( varIsSignedIn )
    {
        setIsTestApiUsed();
        $('#purchase_grid_id').val(event.data.pricingGridId);
        var actionUrl = "proc_pricing_plan.jsp";
        var methodType = "POST";
        var dataString = $('#frm_process_purchase_transaction').serialize();
        purchaseGridData(actionUrl,dataString,methodType,processPurchaseTransactions);
    }
    else
    {
        $("#frm_credential_check").attr('action',"/web/com/gs/common/credential.jsp");
        $("#frm_credential_check").attr('method','POST');
        $("#frm_credential_check").submit();
    }
	
}

function purchaseGridData(actionUrl,dataString,methodType,callBackMethod)
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
            $('#pass_thru_rsvp_num').val( $("#rsvp_gen_num").text() );
            $('#pass_thru_seating_num').val( $("#seating_gen_num").text() );

            if( varIsSignedIn ) {



                $("#frm_billing_passthru").attr('action','/web/com/gs/event/billing.jsp');
                $("#frm_billing_passthru").attr('method','POST');


                $("#frm_billing_passthru").submit();
            }
        }
        else
        {
            alert("Please try again later.");
        }
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