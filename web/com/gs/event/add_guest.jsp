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
		
		boolean isAllGuestAdd = ParseUtil.sTob(request.getParameter("all_guest_tab"));
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
%>

		<div class="box_container rounded-corners fill_box">
			<div style="padding:20px">
				<div style="text-align:left;" >
					<span class="l_txt" style="padding:10px;" >Add Guest to</span>
				</div>
				<br/>
				<div>
				<form id="frm_add_guest" >
				<div>
				<span>First Name :</span> <input type="text" id="table_name" name="first_name"/> &nbsp;&nbsp;
				<span>Last Name :</span> <input type="text" id="last_name" name="last_name"/><br/>
				<span>Email :</span> <input type="text" id="email_addr" name="email_addr"/><br/>
				<span>Number of Seats :</span> <input type="text" id="num_of_seats" name="num_of_seats"/><br/>
				<span>Cell Number :</span> <input type="text" id="cell_num" name="cell_num"/><br>
				<span>Assign to Table</span> 
				<a class="action_button" id="add_guest" name="add_guest">Add Guest</a></br>
				<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
				<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
				</div>
				</form>
				</div>				
				<span id="err_mssg"></span>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		$.fancybox.showActivity;
		$(document).ready(function() {
			loadEvents();
			$("#add_guest").click(addGuest);
		});
		function loadEvents()
		{
			var actionUrl = "proc_get_tables.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId;
			
			submitTableData(actionUrl,dataString,methodType);
			
		}
		function addGuest()
		{	alert('add guest called');
			var dataString = $("#frm_add_guest").serialize();
			var actionUrl = "proc_add_guest.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';
			submitTableData(actionUrl,dataString,methodType);
		}
		
		function submitTableData(actionUrl,dataString,methodType)
		{
			$.ajax({
				  url: actionUrl ,
				  type: methodType ,
				  dataType: "json",
				  data: dataString ,
				  success: getResult,
				  error:function(a,b,c)
				  {
					  alert(a.responseText + ' = ' + b + " = " + c);
				  }
				});
		}
		
		function getResult(jsonResult)
		{
			if(!jsonResult.success)
			{
				var varResponse = jsonResult.response;
				if(varResponse!=undefined)
				{
					var varMessage = varResponse.error_message;
					if(varMessage!=undefined && varMessage!= '' )
					{
						$("#err_mssg").text(varMessage);
					}
				}
				
			}
			else
			{
				parent.loadTables();
				parent.$.fancybox.close();
				
			}
		}
	</script>
</html>