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
<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" id="nav_bar" >
	<div class="blank_scratch_area" style="padding:5px;">
	    <div class="row">
	    	<!-- <div class="logo span2"><a href="/">Guests</a></div> -->
	    	<div class="span2"><div class="logo">&nbsp;</div></div>
	    	<div class="span2 nav_link"><a id="how_it_works" class="bold_text"  href="#">How It Works</a></div>
	    	<div class="offset1 span7">
	    		<div class="row">
	    			<div class="offset2 span3 nav_link" style="text-align:right;">
<%
		if(isTmpSignedIn)
		{
%>
			<a id="login_name_display" class="bold_text" 
				href="/web/com/gs/common/myaccounts.jsp?admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>"><%=sFirstName %></a>
<%
		}
		else
		{
			if("sign_out.jsp".equalsIgnoreCase(sRefererSource))
			{
%>
				<a id="login_name_display" class="bold_text" 
					href="/web/com/gs/common/credential.jsp?action=login&admin_id=&event_id=&referrer_source=<%=sRefererSource%>">Login</a>
<%
			}
			else
			{
%>
				<a id="login_name_display" name="login_name_display" class="bold_text" 
					href="/web/com/gs/common/credential.jsp?action=login&admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>">Login</a>
				
<%
			}
		}
%>
	    					
					</div>
					<div class="span2 nav_button" style="text-align:center;">
<%
		if(!isTmpSignedIn)
		{
			if("sign_out.jsp".equalsIgnoreCase(sRefererSource))
			{
%>
				<a id="sign_up_link" name="sign_up_link" class="btn btn-green" 
					href="/web/com/gs/common/credential.jsp?action=signup&admin_id=&event_id=&referrer_source=<%=sRefererSource%>">Sign Up</a>
<%				
			}
			else
			{
%>
				<a id="sign_up_link" name="sign_up_link" class="btn btn-green"
					href="/web/com/gs/common/credential.jsp?action=signup&admin_id=<%=sAdminId %>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>">Sign Up</a>
<%				
			}
%>
			<a  class="btn btn-green" id="sign_out_user"  href="/web/com/gs/common/sign_out.jsp?admin_id=<%=sAdminId %>"
				style="display:none;" >Sign Out</a>
<%
		}
		else
		{
%>
			<a  class="btn btn-green" id="sign_out_user"  href="/web/com/gs/common/sign_out.jsp?admin_id=<%=sAdminId %>" >Sign Out</a>
<%
		}
%>
						
					</div>
	    		</div>
	    </div>
	    </div>
</div>
</div>