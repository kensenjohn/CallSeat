<%@ page import="com.gs.common.ParseUtil" %>

<%
    String sCurrentlyActive = ParseUtil.checkNull(request.getParameter("active_now"));
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
%>

<ul id="shopping_cart_breadcrumbs">
<%
    if("search_phone_number.jsp".equalsIgnoreCase(sCurrentlyActive))
    {
%>
        <li>1: Phone Number</li>
        <li><a href="#">2: Pricing Plan</a></li>
        <li><a href="#">3: Billing</a></li>
<%
    }

    if("pricing_plan.jsp".equalsIgnoreCase(sCurrentlyActive))
    {
%>
        <li><a href="search_phone_number.jsp?admin_id=<%=sAdminId%>&event_id=<%=sEventId%>">1: Phone Number</a></li>
        <li>2: Pricing Plan</li>
        <li><a href="#">3: Billing</a></li>
<%
    }

    if("billing.jsp".equalsIgnoreCase(sCurrentlyActive))
    {
%>
        <li><a href="search_phone_number.jsp?admin_id=<%=sAdminId%>&event_id=<%=sEventId%>">1: Phone Number</a></li>
        <li><a href="pricing_plan.jsp?admin_id=<%=sAdminId%>&event_id=<%=sEventId%>">2: Pricing Plan</a></li>
        <li>3: Billing</li>
<%
    }
%>
</ul>