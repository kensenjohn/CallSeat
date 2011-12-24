<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");

		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sTableId = ParseUtil.checkNull(request.getParameter("table_id"));
%>
		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Assign Guests <span id="table_name"></span></h2>
				<div class="row">
					<div class="span5">
						<div class="row">
							<div class="span2 left">
								<h5 class="txt">Total Seats : <span id="seat_per_table"></span> </h5>
							</div>
							<div class="span3 left">
								<h5 class="txt">Assigned seats : <span id="assigned_per_table"></span> </h5>
							</div>
						</div>
					</div>
					<div class="span13">
						<div class="row">
							<div class="span7">
								<h5 class="txt right">Unassigned Guests :  <span id="div_unassinged_guests"></span> </h5>
							</div>
							<div class="span4">
								<h5 class="txt">Assign seats : <input type="text" id="new_assign_seats" name="new_assign_seats" class="span2"/> </h5>
							</div>
							<div class="span2">
								<div class="txt left">
									<button id="add_table_guest" name="add_table_guest" type="button" class="action_button primary small">Add and Save</button>
								</div>								
							</div>
						</div>
					</div>
			
				<div style="clear:both;">&nbsp;</div>
				<div id="div_assigned_guests_table">
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="/web/js/jquery.assignedtableguest.1.0.0.js"></script>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varEventId = '<%=sEventId%>';
		var varTableId = '<%=sTableId%>';
		$(document).ready(function() 
		{
			loadTableGuests();	
			$("#add_table_guest").click(addAndSaveGuestToTable);
		});
		
		function addAndSaveGuestToTable()
		{
			//alert( $("#dd_unassigned_guest option:selected").val() )
			var varSelGuestId = $("#dd_unassigned_guest option:selected").val();
			var varNumOfSeats = $("#new_assign_seats").val();
			
			//alert(varSelGuestId + '----' + varNumOfSeats);
			saveAssignedGuest(varSelGuestId , varNumOfSeats);
		}
		
		function saveAssignedGuest(guest_id, numOfNewAssignSeats)
		{
			var actionUrl = "proc_save_table_guests.jsp";
			var methodType = "POST";
			var dataString = "&admin_id="+varAdminId+"&event_id="+varEventId+"&table_id="+varTableId
								+'&guest_id='+guest_id + '&num_of_new_seats='+numOfNewAssignSeats;
			//alert(dataString);
			makeAjaxCall(actionUrl,dataString,methodType,saveTableGuestList);
		}
		
		
		function loadTableGuests()
		{
			var actionUrl = "proc_load_table_guests.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId+"&event_id="+varEventId+"&table_id="+varTableId;
			//alert(dataString);
			makeAjaxCall(actionUrl,dataString,methodType,createTableGuestList);
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
		
		function saveTableGuestList( jsonResult )
		{
			if(jsonResult!=undefined)
			{
				var varResponseObj = jsonResult.response;
				if(jsonResult.status == 'error' && varResponseObj!=undefined)
				{
					var varIsMessageExist = varResponseObj.is_message_exist;
					if(varIsMessageExist == true)
					{
						var jsonResponseMessage = varResponseObj.messages;
						var varArrErrorMssg = jsonResponseMessage.error_mssg
						displayMessages( varArrErrorMssg );
					}
				}
				else if( jsonResult.status == 'ok' && varResponseObj!=undefined)
				{
					$("#div_unassinged_guests").children().detach();
					loadTableGuests();	
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
		
		function createTableGuestList(jsonResult)
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
						processAssignedSeating( jsonResponseObj );
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
		
		function processAssignedSeating(jsonResponseObj)
		{
			var varAssignedGuests = jsonResponseObj.assigned_guests;
			var varUnAssignedGuests = jsonResponseObj.un_assigned_guests;
			var varCurrentTable = jsonResponseObj.this_table;
			var varAllTableGuests = jsonResponseObj.all_tables_assigned;
			if(varUnAssignedGuests!=undefined)
			{
				var unAssignedDD = generateGuestDropDown(varUnAssignedGuests);
				
				$("#div_unassinged_guests").append(unAssignedDD);
				
			}
			
			if( varCurrentTable!=undefined)
			{
				generateTableDetail(varCurrentTable);
			}
			if(varAssignedGuests!=undefined)
			{
				generateAssignedGuests(varAssignedGuests , varAllTableGuests);
			}
		}
		
		function generateAssignedGuests(assignedGuests, allTableGuests )
		{
			$("#div_assigned_guests_table").assignedtableguest({				
				varAssignedGuest : assignedGuests,
				varAllTableGuests : allTableGuests,
				var_event_id : varEventId,
				var_admin_id : varAdminId,
				var_table_id : varTableId
			});
		}
		
		function generateTableDetail(currentTable)
		{
			$("#seat_per_table").text(currentTable.num_of_seats);
			$("#table_name").text('to ' + currentTable.table_name);

			$("#assigned_per_table").text('0');
			
		}
		
		function generateGuestDropDown(unAssignedGuests)
		{
			var varNumOfGuests = unAssignedGuests.num_of_rows;
			var varGuests = unAssignedGuests.guests;
			
			var varUnAssignedGuestDD = '<select id="dd_unassigned_guest" name="dd_unassigned_guest"> ' +
			' <option id="all" value="all">Add to list</option>';
			
			for(i=0; i<varNumOfGuests ; i++ )
			{
				varUnAssignedGuestDD = varUnAssignedGuestDD + '<option id="'+varGuests[i].guest_id+'"  value="'+varGuests[i].guest_id+'">' 
					+ varGuests[i].first_name + ' ' + varGuests[i].last_name  + '</option>'
			}
			varUnAssignedGuestDD = varUnAssignedGuestDD + '</select>';
			
			return varUnAssignedGuestDD;
		}
	</script>
</html>