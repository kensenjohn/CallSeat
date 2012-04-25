<%@ page import="com.gs.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
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
		
	%>
	<div class="container-filler rounded-corners">
			<div style="padding:10px">
				<div class="row">
				  <div class="span8">
				    <div class="row">
				      <div class="span2"><label for="table_name">Seating Number :</label></div>
				      <div class="span4"><label id="seating_gen_num"></label></div>
				      <div class="span2" id="div_seating_search" style="display:none;"><label id="seating_search">Advanced Search</label></div>
				    </div>				  
				    <div class="row" id="seating_numbers_gen" style="display:none;">
				    	 <div class="span8">
				    	 	<form id="frm_seating_numbers" style="margin-bottom:5px">
				    	 		<fieldset  style="margin-bottom:5px">
									<div class="clearfix-tight">
										<label for="table_name">Area Code :</label>
										<div class="input">
											<input type="text" id="seating_area_code" name="seating_area_code"/>
										</div>
									</div>								
									<div class="clearfix-tight">
										<label for="table_name">Contains :</label>
										<div class="input">
											<input type="text" id="seating_text_pattern" name="seating_text_pattern"/>
										</div>
									</div>
									<div class="actions">									
							            <button id="gen_seating_tel_num" name="gen_seating_tel_num" type="button" class="action_button primary small">Generate New Number</button>
							        </div>	
								</fieldset>
								<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
								<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
				    	 	</form>
				    	 </div>
				    </div>
				     <div class="row">
				     	<div class="span2"><label for="table_name">RSVP Number :</label></div>
				      	<div class="span4"><label id="rsvp_gen_num"></label></div>
				      <div class="span2" id="div_rsvp_search"  style="display:none;"><label id="rsvp_search">Advanced Search</label></div>
				     </div>
				     <div class="row" id="rsvp_numbers_gen"  style="display:none;">
				     	<div class="span8">
							<form id="frm_rsvp_numbers"  style="margin-bottom:5px" >
				    	 		<fieldset style="margin-bottom:5px">
									<div class="clearfix-tight">
										<label for="table_name">Area Code :</label>
										<div class="input">
											<input type="text" id="rsvp_area_code" name="rsvp_area_code"/>
										</div>
									</div>								
									<div class="clearfix-tight">
										<label for="table_name">Contains :</label>
										<div class="input">
											<input type="text" id="rsvp_contains" name="rsvp_contains"/>
										</div>
									</div>
									<div class="actions">									
							            <button id="gen_rsvp_tel_num" name="gen_rsvp_tel_num" type="button" class="action_button primary small">Generate New Number</button>
							        </div>	
								</fieldset>
								<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
								<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
				    	 	</form>
				    	 </div>
				     </div>
				  </div>
				  <div class="span8">
				  	<div class="row">
				  		&nbsp;
				  	</div>
				  </div>
				  <div class="span8">
				  	<div class="row">
				  		<div class="span8" style="text-align:center;">
				  			<button id="bt_get_own_phone" name="bt_get_own_phone" type="button" href="search_phone_number.jsp?ei=<%=sEventId%>&admin_id=<%=sAdminId%>" class="action_button primary big">Get direct line.</button>
				  		</div>
				   	</div>
				  </div>
				</div>
			</div>
			</div>
			
</body>
<script type="text/javascript">

var  varEventId= '<%=sEventId%>';
var varAdminId = '<%=sAdminId%>';

var varSeatingNumType = '<%=Constants.EVENT_TASK.SEATING.getTask()%>';
var varRsvpNumType = '<%=Constants.EVENT_TASK.RSVP.getTask()%>';
	$(document).ready(function() {
		loadPhoneNumber();
		
		$("#bt_custom_seating_num").click(getCustomSeatingNums);
		
		$("#gen_seating_tel_num").click(genSeatingTelNum);
		$("#gen_rsvp_tel_num").click(genRsvpTelNum);
		
		//$("#save_tel_num").click();
		
		$('#rsvp_search').toggle(function() {
				$("#rsvp_numbers_gen").slideDown();
		}, function() {
				$("#rsvp_numbers_gen").slideUp();
		});
		
		$('#seating_search').toggle(function() {
			$("#seating_numbers_gen").slideDown();
		}, function() {
				$("#seating_numbers_gen").slideUp();
		});
	});
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
		var dataString = "admin_id="+varAdminId+"&event_id="+varEventId+'&get_new_phone_num=true';
		
		
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
	function displayPhoneNumbers(jsonResult)
	{
		//alert('status = '+jsonResult.status);
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
				//alert(varIsPayloadExist);
				
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
				//alert('after searching for number - payload exists = ' + varIsPayloadExist);
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
	function processTelNumbers( jsonResponseObj )
	{
		var varTelNumbers= jsonResponseObj.telnumbers;
		var totalRows = varTelNumbers.num_of_rows;
		//alert('processTelNumbers = ' + totalRows);
		if(totalRows!=undefined)
		{
			var varTelNumList = varTelNumbers.telnum_array;
			if(varTelNumList!=undefined)
			{
				for(var iRow = 0; iRow < totalRows ; iRow++ )
				{
					var telNumBean = varTelNumList[iRow];
					
					//alert('tel number = ' + telNumBean.telnum);
					
					if(telNumBean.telnum_type == varSeatingNumType)
					{
						$("#div_seating_search").show();
						$("#seating_gen_num").text(telNumBean.human_telnum);
					}
					if(telNumBean.telnum_type == varRsvpNumType)
					{
						$("#div_rsvp_search").show();
						$("#rsvp_gen_num").text(telNumBean.human_telnum);
					}
						
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
				alert( varArrMessages[i].text );
			}
		}
	}
	function genSeatingTelNum()
	{
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_seating_numbers").serialize();
		dataString = dataString + '&num_type='+varSeatingNumType+'&custom_num_gen=true';
		
		phoneNumberData(actionUrl,dataString,methodType,displayPhoneNumbers);
	}
	function  genRsvpTelNum()
	{
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_rsvp_numbers").serialize();
		dataString = dataString + '&num_type='+varRsvpNumType+'&custom_num_gen=true';
		
		phoneNumberData(actionUrl,dataString,methodType,displayPhoneNumbers);
	}
</script>
