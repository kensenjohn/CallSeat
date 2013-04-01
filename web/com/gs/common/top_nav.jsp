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
	    			<div class="offset1 span4 nav_link" style="text-align:right;">
<%
        if("host_landing.jsp".equalsIgnoreCase(sRefererSource))
        {

%>                      <style>
                        .share {
                            width: 250px;
                            height: 20px;
                            background: #000;
                            float: left;
                            white-space: nowrap;
                            padding-top: 5px;
                            padding-bottom: 5px;
                            padding-left: 0px;
                            padding-right: 7px;
                            background-color:rgba(255, 255, 255, 0.5);
                            border-radius: 5px;

                            box-shadow: 0px 0px 10px #888;
                            vertical-align: top;
                        }
                        </style>
                        <div class="share">
                                <script>(function(d, s, id) {
                                    var js, fjs = d.getElementsByTagName(s)[0];
                                    if (d.getElementById(id)) return;
                                    js = d.createElement(s); js.id = id;
                                    js.src = "//connect.facebook.net/en_US/all.js#xfbml=1";
                                    fjs.parentNode.insertBefore(js, fjs);
                                }(document, 'script', 'facebook-jssdk'));</script>
                                <div class="fb-send" data-href="https://www.callseat.com" data-font="tahoma" style="vertical-align:top;zoom:1;*display:inline"></div>
                        <a href="https://twitter.com/share" class="twitter-share-button" data-url="https://www.callseat.com" data-text="Checkout this site" data-via="smarasoft" data-count="none" data-dnt="true">Tweet</a>
                        <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
                        <!-- Place this tag where you want the share button to render. -->
                        <div class="g-plus" data-action="share" data-annotation="none" data-href="https://www.callseat.com"></div>

                        <!-- Place this tag after the last share tag. -->
                        <script type="text/javascript">
                            (function() {
                                var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
                                po.src = 'https://apis.google.com/js/plusone.js';
                                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
                            })();
                        </script>
                        <script src="//platform.linkedin.com/in.js" type="text/javascript"></script>
                        <script type="IN/Share" data-url="https://www.callseat.com"></script>
                        </div>
 <%
        }
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
					href="/web/com/gs/common/credential.jsp?action=signup&admin_id=<%=sAdminId%>&event_id=<%=sEventId%>&referrer_source=<%=sRefererSource%>">Sign Up</a>
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