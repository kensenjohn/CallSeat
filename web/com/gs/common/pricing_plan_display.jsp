<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.manager.event.EventPricingGroupManager" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.gs.bean.PricingGroupBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>

<%
    Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);

    EventPricingGroupManager eventPricingManager = new EventPricingGroupManager();
    ArrayList<PricingGroupBean> arrPricingBean = eventPricingManager.getPremiumPricingGroups();
%>

<body style="height:auto;">
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
    <div class="fnbx_scratch_area">
        <div style="padding:20px">
            <div class="row">
                <div class="offset_0_5 span12">
                    <h1>Pricing per Seating Plan</h1>
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    &nbsp;
                </div>
            </div>
            <div class="row">
                <div class="offset1 span12">
                    <div  id="pricing_grid" >

            <%
                NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(Locale.US);
                for(PricingGroupBean pricingGroupBean : arrPricingBean )  {
                    String priceClass = Constants.EMPTY;
                    String subTitle = "&nbsp;";
                    if( pricingGroupBean.isDefault()) {
                        priceClass = "most_popular";
                        subTitle = "Most Popular";
                    }
                    BigDecimal currencyPrice = new BigDecimal(pricingGroupBean.getPrice());
            %>
                        <div  class="<%=priceClass%> display_dimension">
                            <div class="header"><h1> <%=ParseUtil.checkNull(pricingGroupBean.getPricegroupname())%> </h1><span style="text-align: center;"><%=subTitle%></span></div>
                            <div style="padding: 3px;">
                                <div class="body" ><span ><%=pricingGroupBean.getMaxMinutes()%> call minutes</span></div>
                                <div class="body" ><span ><%=pricingGroupBean.getSmsCount()%> txt messages</span></div>
                                <div class="body"><span >&nbsp;</span></div>
                                <div class="body" ><span >1 phone number</span></div>
                                <div class="body" ><span >Emails and Online RSVP and seating</span></div>
                                <div class="body"><span >&nbsp;</span></div>
                                <div class="body" ><span ><%=dollarFormat.format(currencyPrice.doubleValue())%> per plan</span></div>
                            </div>
                        </div>
            <%
                }
            %>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    &nbsp;
                </div>
            </div>
            <div class="row">
                <div class="offset1 span12">
                    <span>1) One phone number for RSVP and guest seating. Switch between modes when required.</span>
                </div>
            </div>
            <div class="row">
                <div class="offset1 span12">
                    <span>2) Guests can RSVP and get seating information online.</span>
                </div>
            </div>
            <div class="row">
                <div class="offset1 span12">
                    <span>3) Send text message or email to confirm RSVP or to provide seating information.</span>
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    &nbsp;
                </div>
            </div>
            <div class="row">
                <div class="offset1 span12">
                    <h4>Coming Soon</h4>
                </div>
            </div>
            <div class="row">
                <div class="offset1 span12">
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
</body>
<script src="/web/js/pricing/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        if(mixpanel!=undefined) {
            mixpanel.track('Pg pricing_plan_display.jsp', {'Referrer' : 'Footer link'});
        }
    });
</script>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/>
</html>