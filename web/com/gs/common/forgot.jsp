<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.bean.*"%>
<%@page import="com.gs.manager.forgot.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp">
	<jsp:param name="page_title" value="Forgot User Response"/>
</jsp:include>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<%
	String sForgotUserResponseId = ParseUtil.checkNull(request.getParameter("lotophagi"));

	ForgotInfoManager forgotInfoManager = new ForgotPassword();
	SecurityForgotInfoBean securityForgInfoBean = forgotInfoManager.identifyUserResponse(sForgotUserResponseId);
%>
<body  style="height:auto;">
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
<%
	if(sForgotUserResponseId==null || "".equalsIgnoreCase(sForgotUserResponseId))
	{
		// Invalid token Id
%>
		<div class="fnbx_scratch_area">
			<div class="row" >
				<div class="offset1 span6">
					<h2 class="txt txt_center">
						Please use a valid link to access this page.
					</h2>
				</div>
			</div>
		</div>
<%
	}
	else if(securityForgInfoBean !=null && securityForgInfoBean.getAdminId()!=null && !"".equalsIgnoreCase(securityForgInfoBean.getAdminId())
			&& securityForgInfoBean.getSecureTokenId().equalsIgnoreCase(sForgotUserResponseId))
	{
		if(Constants.FORGOT_INFO_ACTION.PASSWORD.getAction().equalsIgnoreCase(securityForgInfoBean.getActionType().getAction()))
		{
			//password reset

            Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
            String sHomeDomain = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_APPLICATION_DOMAIN));
			%>
			<div class="fnbx_scratch_area">
				<div class="row" >
					<div class="offset1 span10">
						<div class="row">
							<div class="span10">
								<h2 class="txt txt_center">Reset your Password</h2>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								&nbsp;
							</div>
						</div>
						<form id="frm_reset_password" >
							<div class="row">
								<div class="span2" >
									New Password : 
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									<input type="password" id="new_passwd" name="new_passwd"/>
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									Confirm Password : 
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									<input type="password" id="confirm_new_passwd" name="confirm_new_passwd"/>
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									 <input type="button" id="but_reset_password" name="but_reset_password" 
									 class="btn btn-large" value="Save Changes">			 			
								</div>
							</div>
							 <input type="hidden" id="security_token_record_guid" name="security_token_record_guid" 
									value="<%=ParseUtil.checkNull(securityForgInfoBean.getSecurityForgotInfoId())%>">
							<input type="hidden" id="security_token_id" name="security_token_id" 
									value="<%=sForgotUserResponseId%>">
                            <div class="row">
                                <div class="span2" >
                                    &nbsp;
                                </div>
                            </div>
                            <div class="row">
                                <div class="span2" >
                                    <%if(sHomeDomain!=null && !"".equalsIgnoreCase(sHomeDomain)) {%>
                                        <a id="link_back_to_home" name="link_back_to_home" href="<%=sHomeDomain%>">Home</a><br>
                                    <% } else { %>
                                        &nbsp;
                                    <% } %>
                                </div>
                            </div>
						</form>
					</div>
				</div>
			</div>
            <form id="frm_home" id="frm_home"
                  action="http:">
            </form>
	<%			
		}
	}
	else
	{
		//password link expired. Invoke Reset password again
%>
		<div class="fnbx_scratch_area">
			<div class="row" >
				<div class="offset1 span10">
					<h2 class="txt txt_center">
						Your link has expired. Please regenerate a new link to this page
					</h2>
				</div>
			</div>
		</div>
<%
	}
%>
</body>
<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#but_reset_password").click(resetPassword);
	});
	function resetPassword() {
		//frm_reset_password
		var dataString = $("#frm_reset_password").serialize();
		
		var actionUrl = "proc_forgot_resetinfo.jsp";
		var methodType = "POST";
		
		submitRequest(actionUrl,dataString,methodType,getResetPasswordResult);
	}
	
	function submitRequest(  actionUrl,dataString,methodType,callBackMethod ) {
		$.ajax({
			  url: actionUrl ,
			  type: methodType ,
			  dataType: "json",
			  data: dataString ,
			  success: callBackMethod,
			  error:function(a,b,c) {  alert(a.responseText + ' = ' + b + " = " + c); }
			});
	}
	
	function getResetPasswordResult(jsonResult) {
		if(jsonResult!=undefined) {
			var varResponseObj = jsonResult.response;
			if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
				var varIsMessageExist = varResponseObj.is_message_exist;
				if(varIsMessageExist == true) {
					var jsonResponseMessage = varResponseObj.messages;
					var varArrErrorMssg = jsonResponseMessage.error_mssg;
					displayMessages( varArrErrorMssg , true);
				}
			} else if( jsonResult.status == 'ok' && varResponseObj !=undefined) {
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				
				if(varIsPayloadExist == true) {
					var jsonResponseObj = varResponseObj.payload;
					
				}
				
				var varIsMessageExist = varResponseObj.is_message_exist;
				if(varIsMessageExist == true) {
					var jsonResponseMessage = varResponseObj.messages;
					var varArrErrorMssg = jsonResponseMessage.ok_mssg;
					displayMessages( varArrErrorMssg , false);
				}
			}
		}
	}
	
	function displayAlert(varMessage, isError) {
		var varTitle = 'Status';
		var varType = 'info';
		if(isError) {
			varTitle = 'Error';
			varType = 'error';
		} else {
			varTitle = 'Status';	
			varType = 'info';
		}
		
		if(varMessage!='') {
			$.msgBox({
                title: varTitle,
                content: varMessage,
                type: varType
            });
		}
	}
	
	function displayMessages(varArrMessages, isError) {
		if(varArrMessages!=undefined) {
			
				
			var varMssg = '';
			var isFirst = true;
			for(var i = 0; i<varArrMessages.length; i++) {
				if(isFirst == false) {
					varMssg = varMssg + '\n';
				}
				varMssg = varMssg + varArrMessages[i].text;
			}
			
			if(varMssg!='') {
				displayAlert(varMssg,isError);
			}
		}
	}
</script>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 