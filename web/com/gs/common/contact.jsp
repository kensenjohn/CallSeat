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
            <div class="logo span4"><a href="#">Contact Us</a></div>
        </div>
    </div>
    <div class="fnbx_scratch_area">
        <div style="padding:20px">
            <div id="div_login" name="div_login" class="row">
                <div class="offset1 span5">
                    <h1>Contact Us</h1>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span7" >
                            <span>To learn how we can help you, please contact kensenjohn@callseat.com</span>  <br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span7" >
                            <h4>Phone:</h4><span> (267) 250 2719</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span7" >
                            <h4>Address</h4>
                        </div>
                    </div>
                    <div class="row">
                        <div class="offset1 span7" >
                            <span>5316 Carnaby St #155</span>  <br>
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