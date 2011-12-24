<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<jsp:include page="../common/header_bottom.jsp"/>
	<body>
	<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
	%>
			<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<div class="row">
					<div class="span12">
						<form id="frm_tel_numbers" >
							<fieldset>
								<div class="clearfix-tight">
									<label for="table_name">Seating Number :</label>
									<div class="input">
										<input type="text" id="seating_gen_num" name="seating_gen_num"/>
									</div>
								</div>
								<!-- <div class="clearfix-tight">
									<label for="table_name">Area Code :</label>
									<div class="input">
										<input type="text" id="seating_area_code" name="seating_area_code"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="table_name">Text Pattern :</label>
									<div class="input">
										<input type="text" id="seating_text_pattern" name="seating_text_pattern"/>
									</div>
								</div> -->
								<div class="clearfix-tight">
									<label for="table_name">RSVP Number :</label>
									<div class="input">
										<input type="text" id="rsvp_gen_num" name="rsvp_gen_num"/>
									</div>
								</div>
								<div class="actions">									
						            <button id="bt_save_tel_num" name="bt_save_tel_num" type="button" class="action_button primary small">Save Phone Numbers</button>
						        </div>	
							</fieldset>
							<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
							<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
						</form>
					</div>
				</div>
			
				<span id="err_mssg"></span>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	var varAdminId = '<%=sAdminId%>';
	var varEventId = '<%=sEventId%>';
	var varSeatingNumType = '<%=Constants.EVENT_TASK.SEATING.getTask()%>';
	var varRsvpNumType = '<%=Constants.EVENT_TASK.RSVP.getTask()%>';
	$(document).ready(function() {
		
		loadPhoneNumber();
		$("#bt_custom_seating_num").click(getCustomSeatingNums);
		$("#bt_save_tel_num").click(saveChanges);
		
	});
	
	function saveChanges()
	{
		var actionUrl = "proc_save_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_tel_numbers").serialize();
		dataString = dataString + "&num_type="+numType +"&custom_num_gen=true";
		
		phoneNumberData(actionUrl,dataString,methodType,callbackmethod);
	}
	
	function getCustomSeatingNums()
	{
		searchMoreNumbers(varSeatingNumType,customSeatingNumResult);
	}
	
	function searchMoreNumbers(numType, callbackmethod)
	{
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_tel_numbers").serialize();
		dataString = dataString + "&num_type="+numType +"&custom_num_gen=true";
		
		phoneNumberData(actionUrl,dataString,methodType,callbackmethod);
	}
	function loadPhoneNumber()
	{
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = "admin_id="+varAdminId+"&event_id="+varEventId;
		
		
		phoneNumberData(actionUrl,dataString,methodType,displayPhoneNumbers);
	}
	
	function phoneNumberData(actionUrl,dataString,methodType,callBackMethod)
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
	
	function customSeatingNumResult(jsonResult)
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
				alert('after searching for number - payload exists = ' + varIsPayloadExist);
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					processTelNumbers( jsonResponseObj );
				}
			}
			else
			{
				alert("Please try again later.");
			}
		}
		else
		{
			alert("Please try again later.");
		}
	}
	
	function displayPhoneNumbers(jsonResult)
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
					processTelNumbers( jsonResponseObj );
				}
			}
			else
			{
				alert("Please try again later.");
			}
		}
		else
		{
			alert("Please try again later.");
		}
	}
	function displayMessages(varArrMessages)
	{
		if(varArrMessages!=undefined)
		{
			for(var i = 0; i<varArrMessages.length; i++)
			{
				alert( varArrMessages[i].text );
			}
		}
	}
	
	function processTelNumbers( jsonResponseObj )
	{
		var varTelNumbers= jsonResponseObj.telnumbers;
		var totalRows = varTelNumbers.num_of_rows;
		if(totalRows!=undefined)
		{
			var varTelNumList = varTelNumbers.telnum_array;
			if(varTelNumList!=undefined)
			{
				for(var iRow = 0; iRow < totalRows ; iRow++ )
				{
					var telNumBean = varTelNumList[iRow];
					
					if(telNumBean.telnum_type == varSeatingNumType )
					{
						$("#seating_gen_num").val(telNumBean.telnum);
					}
					if(telNumBean.telnum_type == varRsvpNumType )
					{
						$("#rsvp_gen_num").val(telNumBean.telnum);
					}
						
				}
			}
			
		}
		
	}
	</script>
</html>