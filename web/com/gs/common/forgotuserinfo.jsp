<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<body>
<div class="container-filler rounded-corners">
			
			<div class="row">
					<div class="span8">
						<h3 class="txt txt_center">Regenerate New Password</h3>
						<form id='frm_forgot_password'>
							<fieldset>
								<div class="clearfix-tight">
									<label for="reg_email_id">Email :</label>
									<div class="input">
										<input type="text" id="reg_email_id" name="reg_email_id"/>
									</div>
								</div>
								<div class="actions">									
						            <button id="but_submit_email" name="but_submit_email" type="button" 
						            	class="action_button primary large">Submit Request</button>
						            <br>
						             <span id="err_mssg"  style="color: #9d261d;" ></span><br>
								     <span id="success_mssg"  style="color: #46a546;" ></span>
						        </div>
							</fieldset>
						</form>
					</div>
			</div>
			
			
		</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#but_submit_email").click(submitEmail);
		});
		function submitEmail()
		{
			var dataString = $("#frm_forgot_password").serialize();
			
			var actionUrl = "proc_forgot_userinfo.jsp";
			var methodType = "POST";
			
			submitPasswordRequest(actionUrl,dataString,methodType,getResult);
		}
		function submitPasswordRequest(  actionUrl,dataString,methodType,callBackMethod )
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
						
					}
					
					var varIsMessageExist = varResponseObj.is_message_exist;
					if(varIsMessageExist == true)
					{
						var jsonResponseMessage = varResponseObj.messages;
						var varArrErrorMssg = jsonResponseMessage.ok_mssg
						displayMessages( varArrErrorMssg );
					}
				}
			}
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
