<%@page import="com.gs.common.*"%>
<%@page import="com.gs.bean.*"%>
<%
HttpSession  reqSession = request.getSession(false);
boolean isSignedIn = false;

String sFirstName = "";
if(reqSession!=null)
{
	AdminBean adminBean = (AdminBean) reqSession.getAttribute(Constants.USER_SESSION);
	if(adminBean!=null && adminBean.isAdminExists())
	{
		isSignedIn = true;
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
		if(isSignedIn)
		{
%>
				<li>
	    			<a class="" href="/login" id=""><%=sFirstName %></a>
	    		</li>
<%
		}
		else
		{
%>
				<li>
	    			<a class="" href="/login" id="">Login</a>
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