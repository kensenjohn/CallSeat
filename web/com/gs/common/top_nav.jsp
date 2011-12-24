<%@page import="com.gs.common.*"%>
<%@page import="com.gs.bean.*"%>
<%
HttpSession  reqSession = request.getSession(false);
boolean isTmpSignedIn = false;
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sFirstName = "";
if(reqSession!=null)
{
	AdminBean adminBean = (AdminBean) reqSession.getAttribute(Constants.USER_SESSION);
	if(adminBean!=null && adminBean.isAdminExists())
	{
		sAdminId = adminBean.getAdminId();
		isTmpSignedIn = true;
		UserInfoBean userInfoBean = adminBean.getAdminUserInfoBean();
		if(userInfoBean!=null)
		{
			sFirstName = userInfoBean.getFirstName();
		}
	}
}




%>
<header>
	<div style="padding:5px;">
	    <h1><a href="/">Guests</a></h1>
	    <nav>
	    	<ul>
<%
		if(isTmpSignedIn)
		{
%>
				<li>
	    			<a class="bold_text" href="#" id="login_name_display"><%=sFirstName %></a>
	    		</li>
<%
		}
		else
		{
%>
				<li>
	    			<a id="login_name_display" class="" href="/web/com/gs/common/credential.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>">Login</a>
	    		</li>
<%
		}
%>
	    		
	    		<li>
	    			<a class="" href="/learn" id="">Learn More</a>
	    		</li>
	    		<li>
	    			<a class="" href="http://blog.uber.com" id="">Blog</a>
	    		</li> 
	    	</ul>
	    </nav>
	    </div>
</header> 