<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="/web/com/gs/common/header_top.jsp"/>

<%@include file="/web/com/gs/common/security.jsp"%>
<jsp:include page="/web/com/gs/common/header_bottom.jsp"/>

<%
    String userAgent = ParseUtil.checkNull(request.getHeader( "User-Agent" ));
    boolean isInternetExplorer = false;
    boolean isOldInternetExplorer = false;
    boolean isOtherBrowsers = false;
    boolean isAppleBrowser = false;
    if(userAgent.contains("MSIE")) {
        isInternetExplorer = true;
        if(userAgent.contains("MSIE 7") || userAgent.contains("MSIE 8")) {
            isOldInternetExplorer = true;
        }
    } else {
        isOtherBrowsers = true;
        if(userAgent.contains("iPhone") || userAgent.contains("iPad") || userAgent.contains("iPod")) {
            isAppleBrowser = true;
        }
    }

    Logger jspLogging = LoggerFactory.getLogger("JspLogging");
    String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
    String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
    String sReferrer = ParseUtil.checkNull( request.getHeader("referer") );

    String sGateAdminId = sAdminId;
    jspLogging.info("How it works : Admin Id - " + sAdminId + " Referrer : " + sReferrer );
%>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body style="height:auto;">
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
    <div class="fnbx_scratch_area">
        <div class="row">
            <div class="offset_0_5 span9">
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
                    <div class="offset_0_5 span6">
                        <h3>Step 1: Create a free seating plan</h3>
                    </div>
                    <div class="offset1 span7">
                        <p>Select a name and date for the seating plan. Set the mode of the plan (RSVP or Seating).
                            Invite your guests and create tables for them to be seated.</p>
                        <p>You get a demo phone number with an extension. This demo service has limited free call minutes and text message.</p>
                    </div>
                </div>
                <div class="row">
                    <div class="offset_0_5 span9">
                        <h3>Step 2: Gather RSVP and provide seating information.</h3>
                    </div>
                    <div class="offset1 span7">
                        <p> When in RSVP mode, guests can RSVP or change their previous RSVP, online by email or by calling the phone number.
                            A confirmation text message or email can be sent out after the RSVP.
                            Assign seating for the guests after they RSVP.<br>
                        <%
                        if(isOtherBrowsers) {
                            if(!isAppleBrowser){
                            %>
                            Listen to RSVP Sample &nbsp; <audio src="/web/voice/sample_RSVP_message_wav.wav" controls  ></audio> &nbsp;&nbsp;
                            <%
                            }

                        } else if( isInternetExplorer ) {
                            if(isOldInternetExplorer) {
                            %>
                                <a type="application/octet-stream" href="/web/voice/sample_RSVP_message.mp3" target="_blank">Click to listen to RSVP Message</a> &nbsp;&nbsp;
                            <%
                            } else {
                            %>
                                Listen to RSVP Sample &nbsp; <audio src="/web/voice/sample_RSVP_message.mp3" controls  ></audio> &nbsp;&nbsp;
                            <%
                            }
                        }
                        %>
                        </p>
                        <p> In Seating mode guest can get their table number online, by phone or text message.
                        <%
                        if(isOtherBrowsers) {
                            if(!isAppleBrowser){
                            %>
                            Listen to Seating Message sample <audio src="/web/voice/sample_Seating_message_wav.wav" controls  ></audio>
                            <%
                            }

                        } else if( isInternetExplorer ) {
                            if(isOldInternetExplorer) {
                        %>
                            <a type="application/octet-stream" href="/web/voice/sample_Seating_message.mp3" target="_blank">Click to listen to Seating message sample</a>
                        <%
                            } else {
                        %>
                            Listen to Seating Message sample &nbsp; <audio src="web/voice/sample_Seating_message.mp3" controls  ></audio>
                        <%
                            }
                         }
                        %>
                        </p>
                    </div>
                </div>
                <div class="row">
                    <div class="offset_0_5 span7">
                        <h3>Step 3: Get our Premium service (Personalized Phone Number)</h3>
                    </div>
                    <div class="offset1 span7">
                        <p>You get access to a personalized phone number with more call minutes and text messages.</p>
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
<script type="text/javascript">
    $(document).ready(function() {
        if(mixpanel!=undefined) {
            mixpanel.track('Pg how_it_works.jsp', {'Referrer' : '<%=sReferrer%>'});
        }
    });
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/>
</html>