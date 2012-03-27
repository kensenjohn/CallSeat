<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>


<jsp:include page="../common/header_top.jsp"/>

<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%@include file="/web/com/gs/common/gatekeeper.jsp"%>

<%
%>
<body>
		<div class="container rounded-corners">
			<div style="padding:20px">
				<div class="row">
					<div class="span16">
						<div class="row">							
							<div class="span8">
								<h3 class="txt txt_center">Close an Account</h3>
								<form id="frm_close_account" >
									<fieldset>
										<div class="clearfix-tight">
											<label for="lab_account_num">Account Number :</label>
											<div class="input">
												<input type="text" id="account_sid" name="account_sid"/>
												<input type="hidden" id="manage_action" name="manage_action" value="close_account"/>
											</div>
										</div>
										<div class="actions">									
								            <button id="close_account_button" name="close_account_button" type="button" class="action_button primary large">Close Account</button>
								            <br>
								            <span id="err_mssg"></span>
								        </div>
									</fieldset>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
</body>

<script type="text/javascript">
$(document).ready(function() {
	$("#frm_close_account").click(closeAccount);
});
function closeAccount()
{
	var dataString = $("#frm_close_account").serialize();
	
	var actionUrl = "proc_manage_phone_account.jsp";
	var methodType = "POST";
	
	submitAccountAction(actionUrl,dataString,methodType,getResult);
}

function getResult()
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
				//processTableGuest( jsonResponseObj );
				var varAccountSid = jsonResponseObj.account_sid;
				if(varAccountSid!=undefined)
				{
					alert('account was closed.');	
				}
			}
		}
	}
}
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>