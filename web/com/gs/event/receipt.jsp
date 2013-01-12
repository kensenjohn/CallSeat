<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.manager.event.PurchaseTransactionManager" %>
<%@ page import="com.gs.manager.event.EventPricingGroupManager" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%
try
{
    Logger jspLogging = LoggerFactory.getLogger("JspLogging");
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));

    PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
    purchaseTransactionBean.setAdminId(sAdminId);
    purchaseTransactionBean.setEventId(sEventId);

    PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
    PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);

    if(!purchaseResponseTransactionBean.isPurchaseComplete())
    {
        throw new Exception();
    }
    String sItemName = "";

    EventPricingGroupManager eventPricingGroupManager = new EventPricingGroupManager();
    CheckoutBean checkoutBean = eventPricingGroupManager.getCheckoutBean(sAdminId, sEventId);

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
                &nbsp;
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                <h2 class="txt txt_center">Receipt</h2>
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                &nbsp;
            </div>
        </div>
        <div class="row">
            <div class="offset1 span10">
                <div class="row">
                    <div class="span11">
                        Thank you,<br>Please review your purchase. This event's telephone numbers have have been switched to Pro mode.<br>
                        You should be able to access the event's seating and RSVP data without using extensions.
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
                        <h4 style="color:#37291C;"><%=checkoutBean.getFormattedItemPrice()%></h4>
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
                        <h2 style="color:#37291C;">Total Paid &nbsp;&nbsp;&nbsp;<%=checkoutBean.getFormattedGrandTotal()%></h2>
                    </div>
                </div>
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
</body>
<%
}
catch(Exception e)
{

}
%>