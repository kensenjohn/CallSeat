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
            <div class="offset1 span12">
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
                        <h3>Step 1: Create a seating plan</h3>
                    </div>
                    <div class="offset2 span9">
                        Select a name and date for the seating plan. Create new guests with their phone numbers and email.
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="offset1 span6">
                        <h3>Step 2: Personalize phone numbers</h3>
                    </div>
                    <div class="offset2 span9">
                        Purchase personalized phone number package for guests to RSVP and get seating information.
                        Send these numbers along with the guest's invitation.
                    </div>
                </div>
                <div class="row">
                    <div class="span5">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="offset1 span9">
                        <h3>Step 3: RSVP and seating information by phone.</h3>
                    </div>
                    <div class="offset2 span9">
                        Guests can RSVP or change their previous RSVP by calling the personalized number. <br>
                        Assign seating for the guests after they RSVP.<br>
                        Guests can also call to get their seating assignment information.
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