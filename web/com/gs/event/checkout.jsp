<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.manager.event.PurchaseTransactionManager" %>
<%@ page import="com.gs.manager.event.EventPricingGroupManager" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%
    Logger jspLogging = LoggerFactory.getLogger("JspLogging");
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));

    PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
    purchaseTransactionBean.setAdminId(sAdminId);
    purchaseTransactionBean.setEventId(sEventId);

    PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
    PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);
    if(purchaseResponseTransactionBean!=null)
    {
        purchaseResponseTransactionBean.setUniquePurchaseToken(Utility.getNewGuid());
        purchaseTransactionManager.modifyPurchaseTransaction(purchaseResponseTransactionBean);
    }


    String sItemName = "";

    EventPricingGroupManager eventPricingGroupManager = new EventPricingGroupManager();
    CheckoutBean checkoutBean = eventPricingGroupManager.getCheckoutBean(sAdminId, sEventId);

    String sGateAdminId = sAdminId;
%>
<%@include file="../common/gatekeeper.jsp"%>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body style="height:auto;">
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
    <div  class="fnbx_scratch_area">
        <div class="row">
            <div class="offset1 span11">
                <jsp:include page="breadcrumb_shopping_cart.jsp">
                    <jsp:param name="active_now" value="checkout.jsp" />
                </jsp:include>
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                <h2 class="txt txt_center">Order Summary and Purchase</h2>
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                &nbsp;
            </div>
        </div>
        <div class="row">
            <form id="frm_checkout_summary" name="frm_checkout_summary">
                <div class="offset1 span10">
                    <div class="row">
                        <div class="offset1 span11">
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span11">
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span4">
                            <div class="row">
                                <div class="span4">
                                    <h4><%=checkoutBean.getItemName()%></h4>
                                </div>
                                <div class="offset_0_5 span4">
                                    <span class="fld_txt_small">Seating Phone Number:&nbsp;<%=purchaseResponseTransactionBean.getSeatingTelNumber()%></span>
                                </div>
                                <div class="offset_0_5 span4">
                                    <span class="fld_txt_small">RSVP Phone Number:&nbsp;<%=purchaseResponseTransactionBean.getRsvpTelNumber()%></span>
                                </div>
                            </div>

                        </div>
                        <div class="span1" style="text-align:right;margin-top: 10px;">
                            <%=checkoutBean.getFormattedItemPrice()%>
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span11">
                            &nbsp;
                        </div>
                    </div>
                    <%
                        if(checkoutBean.getDiscountAmount()>0)
                        {
                    %>
                            <div class="row">
                                <div class="offset_0_5 span4"   style="text-align:right;" >
                                    <span class="fld_txt_small">Discount at <%=checkoutBean.getFormattedDiscountPercentage()%>%</span>
                                </div>
                                <div class="span1" style="text-align:right;border-width:2px;border-bottom-color:#7F7F7F;border-bottom-style: solid;">
                                    <span class="fld_txt_small">- <%=ParseUtil.checkNull(checkoutBean.getFormattedDiscountAmount())%></span>
                                </div>
                            </div>
                    <%
                        }
                    %>
                    <%
                        if(checkoutBean.getDiscountAmount()>0 && checkoutBean.getTaxPercentage()>0)
                        {
                    %>
                            <div class="row">
                                <div class="offset_0_5 span4"   style="text-align:right;">
                                    <span class="fld_txt_small">Total Before tax</span>
                                </div>
                                <div class="span1" style="text-align:right;">
                                    <span class="fld_txt_small"><%=ParseUtil.checkNull(checkoutBean.getFormattedBeforeTaxTotal())%></span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="offset1 span11">
                                    &nbsp;
                                </div>
                            </div>
                    <%
                        }
                    %>
                    <%
                        if(checkoutBean.getTaxPercentage()>0)
                        {
                    %>

                            <div class="row">
                                <div class="offset_0_5 span4"   style="text-align:right;">
                                    <span class="fld_txt_small">Sales Tax at <%=checkoutBean.getFormattedTaxPercentage()%>%</span>
                                </div>
                                <div class="span1"   style="text-align:right;border-width:2px;border-bottom-color:#7F7F7F;border-bottom-style: solid;">
                                    <span class="fld_txt_small">+ <%=ParseUtil.checkNull(checkoutBean.getFormattedTaxAmount())%> </span>
                                </div>
                            </div>
                    <%
                        }
                    %>
                    <div class="row">
                        <div class="offset1 span11">
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span5" style="text-align:right;">
                            <h2 style="color:#37291C;">You Pay &nbsp;&nbsp;&nbsp;<%=checkoutBean.getFormattedGrandTotal()%></h2>
                        </div>
                    </div>

                </div>
            </form>
        </div>
        <div class="row">
            <div class="offset1 span11">
                &nbsp;
            </div>
        </div>
        <div class="row">
            <div class="offset4 span11">
                <input type="button" class="btn btn-blue btn-large" id="btn_purchase_package" value="&nbsp;&nbsp;Purchase&nbsp;&nbsp;">
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                &nbsp;
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                &nbsp;
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                Billing Information
            </div>
        </div>
        <div class="row">
            <div class="offset1 span2"   style="text-align:right;">
                Credit Card:
            </div>
            <div class="span4"  style="text-align:left;">
                xxxx-xxxx-xxxx-<%=purchaseResponseTransactionBean.getCreditCardLast4Digits()%>
            </div>
        </div>
        <div class="row">
            <div class="offset1 span2"   style="text-align:right;">
                Name:
            </div>
            <div class="span4"  style="text-align:left;">
                <%=purchaseResponseTransactionBean.getFirstName()%>&nbsp;<%=purchaseResponseTransactionBean.getLastName()%>
            </div>
        </div>
        <div class="row">
            <div class="offset1 span2"   style="text-align:right;">
                Address:
            </div>
            <div class="span4"  style="text-align:left;">
                <%=purchaseResponseTransactionBean.getZipcode()%> <br> <%=purchaseResponseTransactionBean.getState()%>&nbsp;<%=purchaseResponseTransactionBean.getCountry()%>
            </div>
        </div>
    </div>
    <form id="frm_checkout_passthru">
        <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
        <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>

        <input type="hidden" id="referrer_source" name="referrer_source" value="receipt.jsp"/>
        <input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>
        <input type="hidden" id="unique_purchase_token" name="unique_purchase_token" value="<%=ParseUtil.checkNull(purchaseResponseTransactionBean.getUniquePurchaseToken())%>"/>


    </form>

    <div id="loading_wheel" style="display:none;">
        <img src="/web/img/wheeler.gif">
    </div>
</body>
<script type="text/javascript" src="/web/js/jquery-ui-1.8.13.custom.min.js"></script>

<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript">
    var varIsSignedIn = <%=isSignedIn%>;

    $(document).ready(function()
    {
        $("#loading_wheel").hide();
        enablePurchaseButton();

    });
    $.ajaxSetup({
        beforeSend: function(data) {
            $("#loading_wheel").show();
        },
        complete: function(data) {
            $("#loading_wheel").hide();
        }
    });

    function enablePurchaseButton()
    {
        $('#btn_purchase_package').bind('click',purchasePackage);
    }
    function disablePurchaseButton()
    {
        $('#btn_purchase_package').unbind('click');
    }

    function purchasePackage()
    {
        $("#loading_wheel").show();
        disablePurchaseButton();
        var actionUrl = "proc_purchase_numbers.jsp";
        var methodType = "POST";
        var dataString = $('#frm_checkout_passthru').serialize();

        phoneNumberData(actionUrl,dataString,methodType,processPurchaseTelephonyPackage);
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

    function processPurchaseTelephonyPackage(jsonResult)
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
                var varIsPayloadExist = varResponseObj.is_payload_exist;
                if(varIsPayloadExist == true)
                {
                    var jsonResponseObj = varResponseObj.payload;
                    var varUniquePurchaseToken = jsonResponseObj.unique_purchase_token;

                    $('#unique_purchase_token').val(varUniquePurchaseToken);
                }

                enablePurchaseButton();
            }
            else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
            {
                if( varIsSignedIn )
                {
                    //alert('purchase complete');
                    //displayMssgBoxAlert("Than", false);
                    parent.loadPhoneNumber();
                    $("#frm_checkout_passthru").attr('action','/web/com/gs/event/receipt.jsp');
                    $("#frm_checkout_passthru").attr('method','POST');


                    $("#frm_checkout_passthru").submit();
                }

            }
            else
            {
                displayMssgBoxAlert("Your request was not processed. Please try again later.",true);
                enablePurchaseButton();
            }
        }
        $("#loading_wheel").hide();
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