<%@page import="com.gs.common.*"%>
<%@page import="com.gs.bean.*"%>
<%
HttpSession  reqSession = request.getSession(false);
boolean isTmpSignedIn = false;
String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
String sRefererSource = ParseUtil.checkNull(request.getParameter("referrer_source"));
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
	    			<a id="login_name_display" class="bold_text" href="/web/com/gs/common/myaccounts.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>"><%=sFirstName %>&nbsp;&nbsp;&nbsp;</a>
	    		</li>
<%
		}
		else
		{
			if("sign_out.jsp".equalsIgnoreCase(sRefererSource))
			{
%>
				<li>
	    			<a id="login_name_display" class="" href="/web/com/gs/common/credential.jsp?admin_id=&event_id=&referrer_source=<%=sRefererSource%>">&nbsp;&nbsp;&nbsp;Login&nbsp;</a>
	    		</li>
<%				
			}
			else
			{
%>
				<li>
	    			<a id="login_name_display" class="" href="/web/com/gs/common/credential.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>">&nbsp;&nbsp;&nbsp;Login&nbsp;</a>
	    		</li>
<%				
			}

		}
%>

				<li id="li_user_my_account" style="<%=!isTmpSignedIn? "display:none" : "" %>">
	    			<a class="" id="login_user_account" class="" href="/web/com/gs/common/myaccounts.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>">&nbsp;&nbsp;&nbsp;My Account&nbsp;</a>
	    		</li>
   		
	    		<li>
	    			<a class="" href="#" id="">&nbsp;&nbsp;&nbsp;Learn More&nbsp;</a>
	    		</li>
	    		<li>
	    			<a class="" href="#" id="">&nbsp;&nbsp;&nbsp;Blog&nbsp;</a>
	    		</li> 

				<li  id="li_user_sign_out" style="<%=!isTmpSignedIn? "display:none" : "" %>">
	    			<a class="" id="sign_out_user" class="" href="/web/com/gs/common/sign_out.jsp?admin_id=<%=sAdminId %>">&nbsp;&nbsp;&nbsp;Sign Out&nbsp;</a>
	    		</li>
	    	</ul>
	    </nav>
	    </div>
</header>