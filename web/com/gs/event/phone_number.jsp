<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	
	
  	<link type="text/css" rel="stylesheet" href="/web/css/style.css" /> 
  	   
    <!--[if lte IE 8]>
      <script type="text/javascript" src="/web/js/html5.js"></script>
    <![endif]--> 
    
     <script type="text/javascript" src="/web/js/jquery-1.6.1.js"></script> 
     
	</head>
	<body>
	<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		
		jspLogging.info("Phone number for event : " + sEventId + " by : " + sAdminId);
	%>
			<div class="box_container rounded-corners fill_box">
			<div style="padding:20px">
				<div style="text-align:center;" ><span class="l_txt" style="padding:10px;" >Phone Numbers</span></div><br/>
				<form id="frm_tel_numbers">
					<div>
						<span class="m_txt" style="padding:10px;" >Seating Number</span></br>
						<input type="text" name="seating_gen_num" id="seating_gen_num" value=""/></br></br>
						<span>Bonus Customization</span></br>
							Area Code : <input type="text" name="seating_area_code" id="seating_area_code" value=""/></br>
							Text Pattern(Enter Text): <input type="text" name="seating_text_pattern" id="seating_text_pattern" value=""/></br>
							<input type="button" name="bt_custom_seating_num" id="bt_custom_seating_num" value="Show me the Numbers"/>
					</div>
					<div style="clear:both;">&nbsp;</div>
					<div style="clear:both;">&nbsp;</div>
					<div>
						<span  class="m_txt" style="padding:10px;" >RSVP Number</span>
						<input type="text" name="rsvp_gen_num" id="rsvp_gen_num" value=""/>
					</div>
					</br>
					</br>
					<input type="button" name="bt_save_tel_num" id="bt_save_tel_num" value="Save"/>
					
					<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
					<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
				</form>
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
		alert(varTelNumbers.num_of_rows);
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