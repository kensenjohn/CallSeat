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
				<form id="frm_add_guest" >
				<div style="text-align:left;" >
<%
					if(isAllGuestAdd)
					{
%>
						<span class="l_txt" style="padding:10px;" >Add Guest to</span> <div id="div_event_list"></div>
<%	
					}
					else
					{
%>
						<span class="l_txt" style="padding:10px;" >Guest for</span> <div id="div_event_list"></div>
<%
					}
%>
					
				</div>
				<br/>
				<div>
				
				<div>
				<span>First Name :</span> <input type="text" id="table_name" name="first_name"/> &nbsp;&nbsp;
				<span>Last Name :</span> <input type="text" id="last_name" name="last_name"/><br/>
				<span>Cell Number :</span> <input type="text" id="cell_num" name="cell_num"/><br/>
				<span>Home Number :</span> <input type="text" id="home_num" name="home_num"/><br/>			
				<span>Email :</span> <input type="text" id="email_addr" name="email_addr"/><br/>
				<span>Invited to :</span> <input type="text" id="invited_num_of_seats" name="invited_num_of_seats"/><br/>
				<span>RSVP to :</span> <input type="text" id="rsvp_num_of_seats" name="rsvp_num_of_seats"/><br/>
				
				<a class="action_button" id="add_guest" name="add_guest">Add Guest</a></br>
<%
				if(isAllGuestAdd)
				{
%>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
<%
				}
%>

				<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
				</div>
				
				</div>	
				</form>			
				<span id="err_mssg"></span>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varIsAllGuestAdd = <%=isAllGuestAdd%>;
		$(document).ready(function() 
		{	if(varIsAllGuestAdd)
			{
				loadEvents(); //load all events only if there are guests.
			}
			
			$("#add_guest").click(addGuest);
		});
		function loadEvents()
		{
			var actionUrl = "proc_load_events.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId;
			//alert(dataString);
			makeAjaxCall(actionUrl,dataString,methodType,createEventList);
			
		}
		function addGuest()
		{	//alert('add guest called');
			var dataString = $("#frm_add_guest").serialize();
			var actionUrl = "proc_add_guest.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';
			makeAjaxCall(actionUrl,dataString,methodType,getResult);
		}
		
		function makeAjaxCall(actionUrl,dataString,methodType,callBackMethod)
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
		
		function createEventList(jsonResult)
		{
			if(!jsonResult.success)
			{
				
			}
			else
			{
				var eventDetails = jsonResult.event_detail;
				if(eventDetails!=undefined)
				{
					
					var varEventDD = generateEventDropDown(eventDetails);
					
					$("#div_event_list").append(varEventDD);
					
				}
			}
		}
		
		function generateEventDropDown(eventDetails)
		{
			var varNumOfEvents = eventDetails.num_of_rows;
			var varEventList = eventDetails.events;
			
			var varEventDD = '<select id="dd_event_list" name="dd_event_list"> <option id="all" value="all">Add to list</option>';
			for(i=0; i<varNumOfEvents ; i++ )
			{
				varEventDD = varEventDD + '<option id="'+varEventList[i].event_id+'"  value="'+varEventList[i].event_id+'">' 
					+ varEventList[i].event_name + ' ' + varEventList[i].human_event_date + '</option>'
			}
			varEventDD = varEventDD + '</select>';
			
			return varEventDD;
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
				alert('is all guest added = ' + varIsAllGuestAdd);
				if(varIsAllGuestAdd == true)
				{
					alert('cam to if');
					parent.loadGuests();
				}
				else
				{
					alert('cam to else');
					//parent.loadTables();
				}
				parent.$.fancybox.close();
				
			}
		}
	</script>
</html>