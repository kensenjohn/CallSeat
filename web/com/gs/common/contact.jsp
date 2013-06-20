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
                    <h2>Contact Us</h2>
                    <div class="row">
                        <div class="span2" >
                            &nbsp;
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    </body>
    <jsp:include page="../common/footer_bottom_fancybox.jsp"/>
</html>