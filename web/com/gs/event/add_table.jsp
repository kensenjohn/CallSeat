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
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
	%>
		<div class="box_container rounded-corners fill_box">
			<div style="padding:20px">
				<div style="text-align:center;" ><span class="l_txt" style="padding:10px;" >Table Details</span></div><br/>
				<div>
				<form id="frm_add_table" name="frm_add_table">
				<span>Table Name :</span> <input type="text" id="table_name" name="table_name"/><br>
				Table Number : <input type="text" id="table_num" name="table_num"/><br>
				Number of Seats : <input type="text" id="num_of_seats" name="num_of_seats"/><br/><br/>
				<div>
					<a class="action_button"  id="add_table" name="add_table">Add Table</a>
				</div>
				
				<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
				<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
				</form>
				</div>
				<span id="err_mssg"></span>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			//oadTableData();
			$("#add_table").click(addTable);
		});
		
		function addTable()
		{
			var dataString = $("#frm_add_table").serialize();
			var actionUrl = "proc_add_table.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';
			submitTableData(actionUrl,dataString,methodType,getResult);
		}
		
		function submitTableData(actionUrl,dataString,methodType, callBackMethod)
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
					alert( varArrMessages[i].text );
				}
			}
		}
		
		function getResult(jsonResult)
		{
			//alert(jsonResult.value);
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
						//var jsonResponseObj = varResponseObj.payload;
						//processTableGuest( jsonResponseObj );
						
						parent.loadTables();
						parent.$.fancybox.close();
					}
				}
			}
		}
	</script>
</html>
