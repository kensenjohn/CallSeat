<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	
	
  	<link type="text/css" rel="stylesheet" href="/web/css/style.css" /> 
  	   
    <!--[if lte IE 8]>
      <script type="text/javascript" src="/web/js/html5.js"></script>
    <![endif]--> 
    
     <script type="text/javascript" src="/web/js/jquery-1.6.1.js"></script> 
     
	</head>
	<body>
	<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
	%>
		<div class="box_container rounded-corners fill_box" style="overflow: auto;">
			<div style="padding:20px;">
				<div style="float:left; width:50%; " >
					<div style="text-align:center;" ><span class="l_txt" style="padding:10px;" >Login</span></div>
					<div>
						<form id="frm_login" name="frm_login">
							<table class="cred_table">
								<tr>
									<td>Email:</td><td><input id="login_email" name="login_email" type="text"/></td>
								</tr>
								<tr>
									<td>Password:</td><td><input id="login_password" name="login_password" type="password"/></td>
								</tr>
								<tr>
									<td>&nbsp;</td><td><a class="action_button"  id="login_user" name="login_user">Login</a></td>
								</tr>
							</table>
							<span id="login_err_mssg"></span>
							<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
							<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
						</form>
					</div>
				</div>
				<div style="float:right;  width:50%;" >
					<div style="text-align:center;" ><span class="l_txt" style="padding:10px;" >Register</span></div>
					<div>
						<form id="frm_register"  name="frm_register">
							<table class="cred_table">
								<tr>
									<td>Email:</td><td><input id="register_email" name="register_email" type="text"/></td>
								</tr>
								<tr>
									<td>First Name:</td><td><input id="register_fname" name="register_fname" type="text"/></td>
								</tr>
								<tr>
									<td>Last Name:</td><td><input id="register_lname" name="register_lname" type="text"/></td>
								</tr>
								<tr>
									<td>Password:</td><td><input id="register_pass" name="register_pass" type="password"/></td>
								</tr>
								<tr>
									<td>Confirm Password:</td><td><input id="register_pass_conf" name="register_pass_conf" type="password"/></td>
								</tr>
								<tr>
									<td>&nbsp;</td><td><a class="action_button"  id="register_user" name="register_user">Register</a></td>
								</tr>
							</table>
							<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
							<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
							<span id="register_err_mssg"></span>
						</form>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#login_user").click(loginUser);
		$("#register_user").click(registerUser);
	});
	function loginUser()
	{
		var dataString = $("#frm_login").serialize();
		var actionUrl = "proc_login_user.jsp";
		var methodType = "POST";
		
		submitCredentials(actionUrl,dataString,methodType,getResult);
	}
	function registerUser()
	{
		var dataString = $("#frm_register").serialize();
		var actionUrl = "proc_register_user.jsp";
		var methodType = "POST";
		alert(dataString);
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
					displayMessages( varArrErrorMssg );
				}
			}
			else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					var varUserId = jsonResponseObj.user_id;
					//processTableGuest( jsonResponseObj );
					
					//parent.loadTables();
					setUserCookie('<%=Constants.COOKIE_APP_USERID%>',varUserId)
					parent.$.fancybox.close();
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
	
	function displayMessages(varArrMessages)
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
	}
	</script>
</html>
	