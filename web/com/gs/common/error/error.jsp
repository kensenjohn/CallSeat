<%@ page import="com.gs.common.Constants" %>
<%@ page import="com.gs.common.Configuration" %>
<%@ page import="com.gs.common.ParseUtil" %>
<jsp:include page="/web/com/gs/common/header_top.jsp">
    <jsp:param name="page_title" value="Callseat"/>
</jsp:include>
<jsp:include page="/web/com/gs/common/header_bottom.jsp"/>
<%
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    String sDomain = ParseUtil.checkNull(applicationConfig.get("domain"));
    String sProtocol = ParseUtil.checkNull(applicationConfig.get("site_protocol"));
%>
<body >
<html>
<body>
<div class="navbar navbar_format site_theme_color" id="nav_bar" >
    <div class="blank_scratch_area" style="padding:5px;">
        <div class="row"><div class="span2"><div class="logo">&nbsp;</div></div></div>
    </div>
</div>
<div class="blank_scratch_area">
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            <h1>Uh Oh! We were unable to complete your request. Please try again.</h1>
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            <%
                if(sDomain!=null && !"".equalsIgnoreCase(sDomain)) {
            %>
            <a href="<%=sProtocol%>://<%=sDomain%>" value="Take me Home">Take me to the Home Page</a>
            <%
            } else {
            %>
            &nbsp;
            <%
                }
            %>
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
    <div class="row" >
        <div  class="span10">
            &nbsp;
        </div>
    </div>
</div>
</body>
<jsp:include page="/web/com/gs/common/footer_bottom.jsp"/>