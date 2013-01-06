<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.manager.event.PurchaseTransactionManager" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>


<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));

    String sGateAdminId = sAdminId;
%>
<%@include file="../common/gatekeeper.jsp"%>

<link href="/web/css/pricing/pricing.css" rel="stylesheet">
<link href="/web/css/pricing/bootstrap.css" rel="stylesheet">
<link href="/web/css/pricing/bootstrap-responsive.css" rel="stylesheet">

<body style="height:auto;">
		<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
			<div  style="padding-top:5px;">
				<div class="logo span4"><a href="#">CallSeat</a></div>
			</div>
		</div>
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
							<div class="span8">
								<h2 >Step 2 : Select a Pricing Plan</h2>
							</div>
						</div>
						<div class="row">							
							<div class="span8">
								&nbsp;
							</div>
						</div>
                        <div class="row" id="pricing_grid_options">
                          <!-- <div class="span11">
                              <form id="frm_pricing_plan" >

                              </form>
                          </div> -->
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
        </form>
</body>
<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/web/js/pricing/jquery-1.7.2.min.js"></script>
<script src="/web/js/pricing/bootstrap.js"></script>

<script type="text/javascript">

var varAdminId = '<%=sAdminId%>';
var varEventId = '<%=sEventId%>';
var varIsSignedIn = <%=isSignedIn%>;

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
        if(varArrPricingPlan[vari].is_default == true)
        {
            varPricingGrid = varPricingGrid + '<div class="span2 pricing4 no-zoom shadow" id="div_pricing_'+varArrPricingPlan[vari].pricing_group_id+'"><ul class="popullar"><li class="head" ><h1>Popular</h1>';
        }
        else
        {
            varPricingGrid = varPricingGrid + '<div class="span2 pricing2 no-zoom shadow" id="div_pricing_'+varArrPricingPlan[vari].pricing_group_id+'"><ul><li class="head" ><h1>&nbsp;</h1>';
        }
        varPricingGrid = varPricingGrid + '</li>';
        varPricingGrid = varPricingGrid + '<li class="price">$'+varArrPricingPlan[vari].price+'</li>';
        varPricingGrid = varPricingGrid + '<li>'+varArrPricingPlan[vari].max_minutes+' minutes</li>';
        varPricingGrid = varPricingGrid + '<li>'+varArrPricingPlan[vari].sms_count+' texts</li>';
        varPricingGrid = varPricingGrid + '<li class="footer"><a href="#" id="" class="btn btn-inverse btn-large">Buy</a></li>';
        varPricingGrid = varPricingGrid + '</ul></div>';

        $('#pricing_grid_options').append(varPricingGrid);

        //$('#div_pricing_'+varArrPricingPlan[vari].pricing_group_id).bind('click',submitPricingPlan(varArrPricingPlan[vari].pricing_group_id));

        $('#div_pricing_' + varArrPricingPlan[vari].pricing_group_id).click({pricingGridId: varArrPricingPlan[vari].pricing_group_id,price: varArrPricingPlan[vari].price }, submitPricingPlan);

	}
	
}
function submitPricingPlan(event)
{
    //alert(event.data.pricingGridId + ' -- ' + event.data.price );
    if( varIsSignedIn )
    {
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

            if( varIsSignedIn )
            {
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
</script>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>