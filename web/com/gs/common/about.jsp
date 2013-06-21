<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
    <body  style="height:auto;">

    <div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
        <div style="padding:5px;">
            <div class="logo span4"><a href="#">About Us</a></div>
        </div>
    </div>
    <div class="fnbx_scratch_area">
        <div style="padding:20px">
            <div id="div_login" name="div_login" class="row">
                <div class="offset1 span5">
                    <h1>About Callseat</h1>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span6" >
                            <span>Callseat brings the privacy and convenience of a smart phone or land line for guests to RSVP to an invitation. Guests will also get their seating information by calling a number which the host has provided.<br>
                                As a host you can keep track of your invitation and RSVP. You will be able to assign seats to a guest based on their RSVP.<br>
                                The guest can get their seating information by dialing a private number which you own. The guests will not have to stand in long lines to get their table numbers.<br>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    </body>
    <jsp:include page="../common/footer_bottom_fancybox.jsp"/>
</html>
