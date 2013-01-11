<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
	<body  style="height:auto;">
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sRefferrerSource = ParseUtil.checkNull(request.getParameter("referrer_source"));
		boolean isPassthru = ParseUtil.sTob(request.getParameter("pass_thru_action"));
		String sAction =  ParseUtil.checkNull(request.getParameter("action"));
		
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
%>
		<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
			<div style="padding:5px;">
				<div class="logo span4"><a href="#">Guests</a></div>
			</div>
		</div>
		<div class="fnbx_scratch_area">
			<div style="padding:20px">
				<div id="div_login" name="div_login" class="row" style="display:<%="login".equalsIgnoreCase(sAction)?"block":"none"%>;">
					<div class="offset1 span5">
						<h2>Login User</h2>
						<div class="row">
							<div class="span2" >
								&nbsp;
							</div>
						</div>
						<form id="frm_login" >
						<div class="row">
							<div class="span2" >
								Email : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="login_email" name="login_email"/>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								Password : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="password" id="login_password" name="login_password"/>
							</div>
						</div>
						<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
						<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
						<div class="row">
							<div class="span2" >
								 <input type="button" id="login_user" name="login_user" class="btn btn-large" value="Login"><br>						 			
								    <span id="login_err_mssg"  style="color: #9d261d;" ></span><br>
								    <span id="login_success_mssg"  style="color: #46a546;" ></span>
							</div>
						</div>
						</form>
						<div class="row">
							<div class="span5" >
								<a id="link_forgot_password" href="#">Forgot your password?</a> <br><br>
								<a id="link_to_signup" href="#">Sign me up. I don't have an account.</a>
							</div>
						</div>
						
						
					</div>
				</div>
				<div id="div_signup" name="div_signup" class="row" style="display:<%=("signup".equalsIgnoreCase(sAction) || "".equalsIgnoreCase(sAction) )?"block":"none"%>;">
					<div class="offset1 span5">
						<h2>Sign Me Up</h2>
						<div class="row">
							<div class="span2" >
								&nbsp;
							</div>
						</div>
						<form id="frm_register" >
						<div class="row">
							<div class="span2" >
								Email : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="register_email" name="register_email"/>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								First Name : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="register_fname" name="register_fname"/>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								Last Name : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="register_lname" name="register_lname"/>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								Password : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="password" id="register_pass" name="register_pass"/>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								Confirm Password : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="password" id="register_pass_conf" name="register_pass_conf"/>
							</div>
						</div>
						<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
						<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
						<div class="row">
							<div class="span5" >
								 <input type="button" id="register_user" name="register_user" class="btn btn-large" value="Create My Account"/>
							</div>
						</div>
						
						
						<div class="row">
							<div class="span2" >
								&nbsp;
							</div>
						</div>
						
						<div class="row">
							<div class="span6" >
						 			<a id="link_to_login" name="link_to_login" href="#">Already have an account? Click here to Login.</a><br>
								    <span id="reg_err_mssg"  style="color: #9d261d;" ></span><br>
								    <span id="reg_success_mssg"  style="color: #46a546;" ></span>
							</div>
						</div>
						
						</form>
					</div>
				</div>		
			</div>
		</div>
		<form id="frm_forgot_password" id="frm_forgot_password" action="forgotuserinfo.jsp">
			<input type="hidden" id="parent_referrer_src" name="parent_referrer_src" value="<%=sRefferrerSource %>"/>
			<input type="hidden" id="parent_admin_id" name="parent_admin_id" value="<%=sAdminId %>"/>
			<input type="hidden" id="parent_event_id" name="parent_event_id" value="<%=sEventId %>"/>
			<input type="hidden" id="parent_pass_thru_action" name="parent_pass_thru_action" value="<%=isPassthru%>"/>
			
			
			<input type="hidden" id="parent_pass_thru_rsvp_num" name="parent_pass_thru_rsvp_num" value="<%=ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"))%>"/>			
			<input type="hidden" id="parent_pass_thru_seating_num" name="parent_pass_thru_seating_num" value="<%=ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"))%>"/>
		</form>
		<%
			if( sRefferrerSource!=null  && "search_phone_number.jsp".equalsIgnoreCase(sRefferrerSource))
			{
				String sPassthruRsvpNumber = ParseUtil.checkNull(request.getParameter("pass_thru_rsvp_num"));
				String sPassthruSeatingNumber = ParseUtil.checkNull(request.getParameter("pass_thru_seating_num"));
		%>
				<form id="frm_telnum_pricing_plan_passthru" id="frm_telnum_pricing_plan_passthru">
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
					
					<input type="hidden" id="pass_thru_rsvp_num" name="pass_thru_rsvp_num" value="<%=sPassthruRsvpNumber%>"/>
					<input type="hidden" id="pass_thru_seating_num" name="pass_thru_seating_num" value="<%=sPassthruSeatingNumber%>"/>
				</form>
		<%
			}
		%>

<%
    if( sRefferrerSource!=null  && "pricing_plan.jsp".equalsIgnoreCase(sRefferrerSource))
    {
%>

        <form id="frm_telnum_purchase_finalize_passthru" id="frm_telnum_purchase_finalize_passthru">
            <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
            <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
        </form>
<%
    }
%>
		
		
	</body>
	<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
	<script type="text/javascript">
	var varSource = '<%=sRefferrerSource%>';
	$(document).ready(function() {
		$("#login_user").click(loginUser);
		$("#register_user").click(registerUser);
		$("#link_forgot_password").click(submitForgotPassword);
		$("#link_to_login").click( showLogin );
		$("#link_to_signup").click( showSignUp );
	});
	function showLogin()
	{
		$("#div_signup").hide();
		$("#div_login").show();
	}
	function showSignUp()
	{
		$("#div_login").hide();
		$("#div_signup").show();
	}
	function submitForgotPassword()
	{

		$("#frm_forgot_password").attr("method","POST");
		$("#frm_forgot_password").submit();
	}
	function loginUser()
	{ 	var dataString = $("#frm_login").serialize();
		
		var actionUrl = "proc_login_user.jsp";
		var methodType = "POST";
		
		submitCredentials(actionUrl,dataString,methodType,getResult);
	}
	function registerUser()
	{
		var dataString = $("#frm_register").serialize();
		var actionUrl = "proc_register_user.jsp";
		var methodType = "POST";
		submitCredentials(actionUrl,dataString,methodType,getResult);
	}
	
	function submitCredentials( actionUrl,dataString,methodType,callBackMethod )
	{		
		$.ajax({
			  url: actionUrl ,
			  type: methodType ,
			  dataType: "json",
			  data: dataString ,
			  success: callBackMethod,
			  error:function(a,b,c)
			  {
				  alert(a.responseText + ' = ' + b + " = " + c);
			  }
			});
	}
	
	function getResult( jsonResult )
	{
		if(jsonResult!=undefined)
		{
			var varResponseObj = jsonResult.response;
			if(jsonResult.status == 'error'  && varResponseObj !=undefined )
			{
				var varIsMessageExist = varResponseObj.is_message_exist;
				if(varIsMessageExist == true)
				{
					var jsonResponseMessage = varResponseObj.messages;
					var varArrErrorMssg = jsonResponseMessage.error_mssg
					displayMessages( varArrErrorMssg , true );
				}
			}
			else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					var varUserId = jsonResponseObj.user_id;
					setUserCookie('<%=Constants.COOKIE_APP_USERID%>',varUserId);
					
					var varFirstName = jsonResponseObj.first_name;
					//alert(varFirstName);
					parent.credentialSuccess(jsonResponseObj,varSource);
					
					if(varSource == 'search_phone_number.jsp')
					{
						//alert('before submit');
						$("#frm_telnum_pricing_plan_passthru").attr('action','/web/com/gs/event/search_phone_number.jsp');
						$("#frm_telnum_pricing_plan_passthru").attr('method','POST');
						$('#frm_telnum_pricing_plan_passthru').submit();
					}
                    else if(varSource == 'pricing_plan.jsp' || (varSource == 'billing.jsp') )
                    {
                        //alert('before submit');
                        $("#frm_telnum_purchase_finalize_passthru").attr('action','/web/com/gs/event/search_phone_number.jsp');
                        $("#frm_telnum_purchase_finalize_passthru").attr('method','POST');
                        $('#frm_telnum_purchase_finalize_passthru').submit();
                    }
					else
					{
						parent.$.fancybox.close();
                    }




					
				}
			}
		}
	}
	
	function setUserCookie(cookieName, cookieValue)
	{
		var exdays = 1;
		var exdate=new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var c_value=escape(cookieValue) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString())
		 	+ ("; path=/");
		
		document.cookie=cookieName + "=" + c_value;
	}
	
	/*function displayMessages(varArrMessages)
	{
		if(varArrMessages!=undefined)
		{
			for(var i = 0; i<varArrMessages.length; i++)
			{
				var txtMessage =  varArrMessages[i].text;
				var txtMssgLocation = varArrMessages[i].txt_loc_id;
				//alert( varArrMessages[i].text );
				
				$("#"+txtMssgLocation).text(txtMessage);
			}
		}
	}*/
	function displayAlert(varMessage, isError)
	{
		var varTitle = 'Status';
		var varType = 'info';
		if(isError)
		{
			varTitle = 'Error';
			varType = 'error';
		}
		else
		{
			varTitle = 'Status';	
			varType = 'info';
		}
		
		if(varMessage!='')
		{
			$.msgBox({
                title: varTitle,
                content: varMessage,
                type: varType
            });
		}
	}
	
	function displayMessages(varArrMessages, isError)
	{
		if(varArrMessages!=undefined)
		{
			
				
			var varMssg = '';
			var isFirst = true;
			for(var i = 0; i<varArrMessages.length; i++)
			{
				if(isFirst == false)
				{
					varMssg = varMssg + '\n';
				}
				varMssg = varMssg + varArrMessages[i].text;
			}
			
			if(varMssg!='')
			{
				displayAlert(varMssg,isError);
			}
		}
		

	}
	</script>
	<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>
	