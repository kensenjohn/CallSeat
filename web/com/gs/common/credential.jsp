<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sSource = ParseUtil.checkNull(request.getParameter("source"));
		
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
%>

		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<div class="row">
					<div class="span16">
						<div class="row">							
							<div class="span8">
								<h3 class="txt txt_center">Login</h3>
								<form id="frm_login" >
									<fieldset>
										<div class="clearfix-tight">
											<label for="login_email">Email :</label>
											<div class="input">
												<input type="text" id="login_email" name="login_email"/>
											</div>
										</div>
										<div class="clearfix-tight">
											<label for="login_password">Password :</label>
											<div class="input">
												<input type="password" id="login_password" name="login_password"/>
											</div>
										</div>
										<div class="actions">									
								            <button id="login_user" name="login_user" type="button" class="action_button primary large">Login</button>
								            <br>
											<a id="link_forgot_password" href="#">Forgot Password?</a><br>
								            <span id="login_err_mssg"  style="color: #9d261d;" ></span><br>
								             <span id="login_success_mssg"  style="color: #46a546;" ></span>
								        </div>
									</fieldset>
									<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
									<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>	
								</form>
							</div>
							
							<div class="span8">
								<h3  class="txt txt_center">Register</h3>
								<form id="frm_register" >
									<fieldset>
										<div class="clearfix-tight">
											<label for="register_email">Email :</label>
											<div class="input">
												<input type="text" id="register_email" name="register_email"/>
											</div>
										</div>
										<div class="clearfix-tight">
											<label for="register_fname">First Name :</label>
											<div class="input">
												<input type="text" id="register_fname" name="register_fname"/>
											</div>
										</div>
										<div class="clearfix-tight">
											<label for="register_lname">Last Name :</label>
											<div class="input">
												<input type="text" id="register_lname" name="register_lname"/>
											</div>
										</div>
										<div class="clearfix-tight">
											<label for="register_pass">Password :</label>
											<div class="input">
												<input type="password" id="register_pass" name="register_pass"/>
											</div>
										</div>
										<div class="clearfix-tight">
											<label for="register_pass_conf">Confirm Password :</label>
											<div class="input">
												<input type="password" id="register_pass_conf" name="register_pass_conf"/>
											</div>
										</div>
										<div class="actions">									
								            <button id="register_user" name="register_user" type="button" class="action_button primary large">Register</button>
								            <br>								            
								            <span id="reg_err_mssg"  style="color: #9d261d;" ></span><br>
								            <span id="reg_success_mssg"  style="color: #46a546;" ></span>
								        </div>
									</fieldset>
									<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
									<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>							
								</form>
							</div>
						</div>
					</div>
				</div>		
			</div>
		</div>
		<form id="frm_forgot_password" id="frm_forgot_password" action="forgotuserinfo.jsp">
		</form>
	</body>
	<script type="text/javascript">
	var varSource = '<%=sSource%>';
	$(document).ready(function() {
		$("#login_user").click(loginUser);
		$("#register_user").click(registerUser);
		$("#link_forgot_password").click(submitForgotPassword);
	});
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
		//alert(dataString);
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
					setUserCookie('<%=Constants.COOKIE_APP_USERID%>',varUserId);
					
					var varFirstName = jsonResponseObj.first_name;
					//alert(varFirstName);
					parent.credentialSuccess(jsonResponseObj,varSource);
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
	