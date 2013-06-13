<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>

<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%
    Logger jspLogging = LoggerFactory.getLogger("JspLogging");
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    String sReferrer = ParseUtil.checkNull( request.getHeader("referer") );

    String sGateAdminId = sAdminId;
    jspLogging.info("How it works : Admin Id - " + sAdminId + " Referrer : " + sReferrer );
%>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body style="height:auto;">
    <div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
        <div  style="padding-top:5px;">
            <div class="logo span4"><a href="#">CallSeat</a></div>
        </div>
    </div>
    <div class="fnbx_scratch_area">
        <div class="row">
            <div class="offset1 span5">
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        <h1>How it works!!</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="offset1 span6">
                        <h3>Step 1: Setup the seating plan</h3>
                        Create a seating plan. Determine the number of seats that you require for the event and create them.
                        Invite guests to the wedding.
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="offset1 span6">
                        <h3>Step 2: Personalize phone numbers.</h3>
                        Purchase personalized phone numbers. One number will be used for RSVP. The second number will be used to get seating information on day of the wedding.
                        Send the numbers to your guests.
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="offset1 span6">
                        <h3>Step 3: RSVP and seating information by phone.</h3>
                        Guests will RSVP by calling personalized number. Assign seats for your guests at their tables. On the day of the event, the guest can call the second number to get their seating information.
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    !window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>

<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/>
</html>