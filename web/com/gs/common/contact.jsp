<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
    <body  style="height:auto;">
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
    <div class="fnbx_scratch_area">
        <div style="padding:20px">
            <div id="div_login" name="div_login" class="row">
                <div class="offset_0_5 span5">
                    <h1>Contact Us</h1>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span7" >
                            <span>You may contact us through email or phone for any question regarding our product, issues or cancellations.</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span7" >
                            <h4>Email:</h4><span> kjohn@callseat.com</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span7" >
                            <h4>Phone:</h4><span> (267) 250 2719</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span7" >
                            <h4>Address</h4>
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset_0_5 span7" >
                            <span>5316 Carnaby St, Suite 155</span>  <br>
                            <span>Irving, TX 75038</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    </body>
    <jsp:include page="../common/footer_bottom_fancybox.jsp"/>
</html>