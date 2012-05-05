<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>

<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%
	Logger jspLogging = LoggerFactory.getLogger("JspLogging");
	String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
	String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
	
	String sGateAdminId = sAdminId;
%>
<%@include file="../common/gatekeeper.jsp"%>

<body>
   <div class="page_setup">
		<div class="container rounded-corners">
			<div style="margin:5px;">
			<jsp:include page="../common/top_nav.jsp">
				<jsp:param name="referrer_source" value="host_dashboard.jsp"/>
			</jsp:include>
				<jsp:include page="../event/lobby_tab.jsp">
					<jsp:param name="select_tab" value="guest_tab"/>
					<jsp:param name="lobby_header" value="Account Settings"/>
					<jsp:param name="lobby_sec_header" value=""/>
				</jsp:include>
				<div class="main_body">
					<div class="clear_both">					
												
						<jsp:include page="../common/action_nav.jsp">
							<jsp:param name="admin_id" value="<%=sAdminId %>"/>
							<jsp:param name="event_id" value="<%=sEventId %>"/>
							<jsp:param name="select_action_nav" value="dashboard_tab"/> 
						</jsp:include>
					</div>
					<div  class="clear_both" style="width: 100%;  text-align: center;">
						<div  class="clear_both" id="div_account_details">
							<div class="span14">
								<div class="row">							
									<div class="span7">
										<h3 class="txt txt_center">User Information</h3>
										<form id="frm_user_data" >
											<fieldset>
												<div class="clearfix-tight">
													<label for="login_email">Email :</label>
													<div class="input">
														<input type="text" id="login_email" name="login_email"/>
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
												<div class="actions">									
										            <button id="update_admin_info" name="update_admin_info" type="button" class="action_button primary large">Save Changes</button>
										            <br>								            
										            <span id="reg_err_mssg"  style="color: #9d261d;" ></span><br>
										            <span id="reg_success_mssg"  style="color: #46a546;" ></span>
										        </div>
											</fieldset>
											<input type="hidden" id="admin_id" name="admin_id" value="<%=sAdminId%>"/>
										</form>
									</div>
									<div class="span7">
										<h3 class="txt txt_center">Reset Password</h3>
										<form id="frm_new_password" >
											<fieldset>
												<div class="clearfix-tight">
													<label for="current_password">Current Password :</label>
													<div class="input">
														<input type="text" id="current_password" name="current_password"/>
													</div>
												</div>
												<div class="clearfix-tight">
													<label for="new_password">New Password :</label>
													<div class="input">
														<input type="text" id="new_password" name="new_password"/>
													</div>
												</div>
												<div class="clearfix-tight">
													<label for="confirm_password">Confirm Password :</label>
													<div class="input">
														<input type="text" id="confirm_password" name="confirm_password"/>
													</div>
												</div>
												<div class="actions">									
										            <button id="update_user_password" name="update_user_password" type="button" class="action_button primary large">Update my password.</button>
										            <br>								            
										            <span id="reset_pass_err_mssg"  style="color: #9d261d;" ></span><br>
										            <span id="reset_pass_success_mssg"  style="color: #46a546;" ></span>
										        </div>
											</fieldset>
											<input type="hidden" id="admin_id" name="admin_id" value="<%=sAdminId%>"/>
										</form>
									</div>
									</div>

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	!window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>
<script type="text/javascript" src="/web/js/credential.js"></script>
<script type="text/javascript">
	var varAdminID = '<%=sAdminId%>';
	var varEventID = '<%=sEventId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	$(document).ready(function() {
		loadAccountDetails(varEventID,varAdminID);
		loadActions();
		
		$('#update_admin_info').bind('click', function(event){
			clearMessages();
			saveAdminUserInfoData( varEventID,varAdminID );
		});
		$('#update_user_password').bind('click', function(event){
			clearMessages();
			resetAdminPassword( varEventID,varAdminID );
		});
	});
	
	function resetAdminPassword(varTmpEventId,varTmpAdminId)
	{
		var dataString = '&event_id='+ varTmpEventId + '&admin_id='+ varTmpAdminId;
		dataString = $('#frm_new_password').serialize();
		var actionUrl = "proc_reset_password.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getDashboardResult);
	}
	
	function saveAdminUserInfoData(varTmpEventId,varTmpAdminId)
	{
		var dataString = '&event_id='+ varTmpEventId + '&admin_id='+ varTmpAdminId;
		dataString = $('#frm_user_data').serialize();
		var actionUrl = "proc_save_myaccount.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getDashboardResult);
	}
	
	function loadActions()
	{
		setNewEventClick();
		setAllGuestButtonClick();
		setLobbyButtonClick();
	}
	function loadAccountDetails(varTmpEventId,varTmpAdminId)
	{
		var dataString = '&event_id='+ varTmpEventId + '&admin_id='+ varTmpAdminId;
		var actionUrl = "proc_load_myaccount.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getDashboardResult);
	}
	function getDashboardResult(jsonResult)
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
			else  if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					
					var varAdminBean = jsonResponseObj.admin_bean;
					
					if(varAdminBean!= undefined)
					{
						displayAdminUserInfo(varAdminBean);
					}
					
					var varIsMessageExist = varResponseObj.is_message_exist;
					if(varIsMessageExist == true)
					{
						var jsonResponseMessage = varResponseObj.messages;
						var varArrOkMssg = jsonResponseMessage.ok_mssg
						displayMessages( varArrOkMssg );
					}
					
					
				}					
			}
		}
	}
	function displayAdminUserInfo(varAdminBean)
	{
		var varUserInfo = varAdminBean.user_info_bean;
		if(varUserInfo!=undefined)
		{
			$('#login_email').val(varUserInfo.email);
			$('#register_fname').val(varUserInfo.first_name);
			$('#register_lname').val(varUserInfo.last_name);
			
		}
	}
	function getDataAjax(actionUrl,dataString,methodType, callBackMethod)
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
	
	function clearMessages()
	{
		$('#reg_err_mssg').text('');
		$('#reg_success_mssg').text('');
		$('#reset_pass_err_mssg').text('');
		$('#reset_pass_success_mssg').text('');
	}
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>