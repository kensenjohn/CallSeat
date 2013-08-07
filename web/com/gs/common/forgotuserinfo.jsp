<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<%
	String sParentReferrerSource = ParseUtil.checkNull(request.getParameter("parent_referrer_src"));
	String sParentAdminId = ParseUtil.checkNull(request.getParameter("parent_admin_id"));
	String sParentEventId = ParseUtil.checkNull(request.getParameter("parent_event_id"));

	String sParentPassthruAction = ParseUtil.checkNull(request.getParameter("parent_pass_thru_action"));
	String sParentPassthruRsvpNum = ParseUtil.checkNull(request.getParameter("parent_pass_thru_rsvp_num"));
	String sParentPassthruSeatingNum = ParseUtil.checkNull(request.getParameter("parent_pass_thru_seating_num"));
%>
<body  style="height:auto;">
        <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
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
						<div class="row">
							<div class="span2" >
								&nbsp;			 			
							</div>
						</div>
						<div class="row">
							<div class="span4" >
								 <a id="link_back_to_login" name="link_back_to_login" href="#">Return to Login</a><br>		
							</div>
						</div>
					</form>
					<form  id="frm_login"
						action="credential.jsp?action=login&referrer_source=<%=sParentReferrerSource%>&admin_id=<%=sParentAdminId %>&event_id=<%=sParentEventId %>&pass_thru_action=<%=sParentPassthruAction%>&pass_thru_rsvp_num=<%=sParentPassthruRsvpNum%>&pass_thru_seating_num=<%=sParentPassthruSeatingNum%>">
					</form>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#but_submit_email").click(submitEmail);
			$("#link_back_to_login").click(submitBackToLogin);
			$("#link_back_to_signup").click(submitBackToSignup);
		});
		function submitEmail()
		{
			var dataString = $("#frm_forgot_password").serialize();
			
			var actionUrl = "proc_forgot_userinfo.jsp";
			var methodType = "POST";
			
			submitPasswordRequest(actionUrl,dataString,methodType,getResult);
		}
		function submitBackToLogin()
		{

			$("#frm_login").attr("method","POST");
			$("#frm_login").submit();
		}
		function submitBackToSignup()
		{

			$("#frm_signup").attr("method","POST");
			$("#frm_signup").submit();
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
	</script>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 