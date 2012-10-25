<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body  style="height:auto;">
		<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
			<div style="padding:5px;">
				<div class="logo span4"><a href="#">Guests</a></div>
			</div>
		</div>
		<div class="fnbx_scratch_area">
			<div class="row" >
				<div class="offset1 span6">
					<div class="row">
						<div class="span6">
							<h2 class="txt txt_center">Regenerate New Password</h2>
						</div>
					</div>
					<div class="row">
						<div class="span2" >
							&nbsp;
						</div>
					</div>
					<form id="frm_forgot_password" >
						<div class="row">
							<div class="span2" >
								Email : 
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="reg_email_id" name="reg_email_id"/>
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								 <input type="button" id="but_submit_email" name="but_submit_email" 
								 class="btn btn-large" value="Submit Request">			 			
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
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
						//displayMessages( varArrErrorMssg );
						displayMessages( varArrErrorMssg , true);
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
						//displayMessages( varArrErrorMssg );
						displayMessages( varArrErrorMssg , false);
					}
				}
			}
		}
		

		
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
	</script>
