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

<body style="height:auto;">
	<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
		<div  style="padding-top:5px;">
			<div class="logo span4"><a href="#">CallSeat</a></div>
		</div>
	</div>
	<div class="fnbx_scratch_area">
		<div class="row">
			<div class="offset1 span5">
				<div class="row">
					<div class="span5">
						<h2>User Information</h2>
					</div>
				</div>
				<form id="frm_user_data" >
					<div class="row">
						<div class="span5">
							Email :
						</div>
					</div>
					<div class="row">
						<div class="span5" >
							<input type="text" id="login_email" name="login_email"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							First Name :
						</div>
					</div>
					<div class="row">
						<div class="span5" >
							<input type="text" id="register_fname" name="register_fname"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Last Name :
						</div>
					</div>
					<div class="row">
						<div class="span5" >
							<input type="text" id="register_lname" name="register_lname"/>
						</div>
					</div>
					<div class="row">								
						<div class="span5" >
							<input type="button" id="update_admin_info" name="update_admin_info" type="button" class="btn" value="Save Changes">
						</div>
					</div>
					<input type="hidden" id="admin_id" name="admin_id" value="<%=sAdminId%>"/>
				</form>
				<div class="row">
					<div class="span6">
						&nbsp;
					</div>
				</div>
			</div>
			<div class="span1">
				&nbsp;
			</div>
			<div class="span5">
				<div class="row">
					<div class="span5">
						<h2>Reset Password</h2>
					</div>
				</div>
				<form id="frm_new_password" >
					<div class="row">
						<div class="span5">
							Current Password :
						</div>
					</div>
					<div class="row">
						<div class="span5" >
							<input type="password" id="current_password" name="current_password"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							New Password :
						</div>
					</div>
					<div class="row">
						<div class="span5" >
							<input type="password" id="new_password" name="new_password"/>
						</div>
					</div>
					<div class="row">
						<div class="span5">
							Confirm Password :
						</div>
					</div>
					<div class="row">
						<div class="span5" >
							<input type="password" id="confirm_password" name="confirm_password"/>
						</div>
					</div>
					<div class="row">								
						<div class="span5" >
							<input type="button" id="update_user_password" name="update_user_password" type="button" class="btn" value="Update my password">
						</div>
					</div>
					<input type="hidden" id="admin_id" name="admin_id" value="<%=sAdminId%>"/>
				</form>
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