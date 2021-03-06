<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<%
String sTmpUserId =  ParseUtil.checkNull(request.getParameter("admin_id"));

Cookie[] cookies = request.getCookies();
if(cookies!=null)
{
	for(int cookieCount = 0; cookieCount < cookies.length; cookieCount++) 
	{ 
		Cookie cookie1 = cookies[cookieCount];
        if (Constants.COOKIE_APP_USERID.equals(cookie1.getName())) {
        	String cookieUserId = cookie1.getValue();
        	if(cookieUserId != null && sTmpUserId.equalsIgnoreCase(cookieUserId))
        	{
        		cookie1.setMaxAge( 0 ); //deletes cookie immediately
        		cookie1.setPath("/");
        		cookie1.setValue(sTmpUserId);
        		session.removeAttribute( "u_id" );
            	session.removeAttribute( "from_cookie" );
        		session.removeAttribute( Constants.USER_SESSION );
            	
            	//response.set
            	response.addCookie( cookie1 );
        	}
        	
        }
	}
}
response.sendRedirect("/");
%>
<body>
	<div class="container rounded-corners">
		<jsp:include page="top_nav.jsp">
			<jsp:param name="referrer_source" value="sign_out.jsp"/>	
		</jsp:include>
		<div class="blank_scratch_area">
			<div>
				<h2>Thank for using our application. </h2> <br><br>
				<h3><a id="go_home" href="/">Go back to Home Page</a></h3>
			</div>
		</div>
	</div>
	
	<form id="sign_out_forward" action="/">
	</form>
</body>
	
<script type="text/javascript" src="/web/js/credential.js"></script>
	<script type="text/javascript">
	var varAdminId = '<%=sTmpUserId%>'
	$(document).ready(function() {
		unSetUserCookie('<%=Constants.COOKIE_APP_USERID%>',varAdminId);
		//$('#sign_out_forward').submit();
	});
	function unSetUserCookie(cookieName, cookieValue)
	{
		var exdays = 1;
		var exdate=new Date();
		exdate.setDate(exdate.getDate() - exdays);   event_setup.jsp
		var c_value=escape(cookieValue) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString())
		 	+ ("; path=/");
		
		document.cookie=cookieName + "=" + c_value;
	}
	</script>
	<jsp:include page="footer_top.jsp"/>
<jsp:include page="footer_bottom.jsp"/>