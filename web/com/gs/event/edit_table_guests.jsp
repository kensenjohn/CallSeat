<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
	<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");

		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sTableId = ParseUtil.checkNull(request.getParameter("table_id"));
%>

        <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
		<div  class="fnbx_scratch_area">
			<div class="row" >
				<div class="offset1 span6">
					<div class="row" >
						<div class="span6">
							<h2 class="txt txt_center">Assign seats at table <span id='table_name'></span></h2>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					&nbsp;
				</div>
			</div>
			<div class="row" >
				<div class="offset1 span12">
					<div class="row" >
						<div class="span3">
							<span class="fld_name">Seats at this table : </span><span class="fld_txt" id="seat_per_table"></span>
						</div>
						<div class="span3">
							<span class="fld_name">Seats vacant : </span><span class="fld_txt" id="vacant_per_table"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					&nbsp;
				</div>
			</div>
			<div class="row">
				<div class="span6">
					&nbsp;
				</div>
			</div>
			<div class="row" >
				<div class="offset1 span12">
					<div class="row" >
						<div class="span12"  style="padding-top:5px">
							<span class="fld_name">Seat Assignment : </span><span id="div_unassinged_guests"></span>
						</div>
					</div>
					<div class="row" >
						<div class="span1">
							&nbsp;
						</div>
						<div class="span11">
							<div class="row" id="ind_guest_assignments" style="display:none;"">
								<div class="span2"  style="padding-top:8px">
									<span class="fld_name">RSVP : </span><span class="fld_txt" id="ind_rsvp"></span>
								</div>
								<div class="span2"  style="padding-top:8px">
									<span class="fld_name">Unassigned : </span> <span class="fld_txt" id="ind_unassigned"></span>
								</div>
								<div class="span3">
									<span class="fld_name">Assign : </span> <input type="text" id="new_assign_seats" class="ispn2"/>
								</div>
								<div class="span3">
									<button id="add_table_guest" name="add_table_guest" type="button" class="btn btn-small">Save</button>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
			<div class="row">
				<div class="span6">
					&nbsp;
				</div>
			</div>
			<div class="row" >
				<div class="offset1 span12">
					<div id="div_assigned_guests_table">
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="/web/js/jquery.assignedtableguest.1.0.0.js"></script>
	<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
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
					  displayMessages(a.responseText + ' = ' + b + " = " + c, true);
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
						displayMessages( varArrErrorMssg, true);
					}
				}
				else if( jsonResult.status == 'ok' && varResponseObj!=undefined)
				{
					$("#div_unassinged_guests").children().detach();
					$("#ind_guest_assignments").hide("fast");
					displayAlert("Your change was successful.", false);
					loadTableGuests();
				}
				else
				{
					displayAlert("Please try again later.", true);
				}
			}
			else
			{
				displayAlert("Please try again later.", true);
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
						displayMessages( varArrErrorMssg, true);
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
					displayAlert("Please try again later.", true);
				}
			}
			else
			{
				displayAlert("Please try again later.", true);
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
		
		function popGuestAssignmentDet( unAssignedGuests )
		{
			var varGuestId = $("#dd_unassigned_guest").val();
			var divIndGuestAssign = $("#ind_guest_assignments");
			
			if( varGuestId == "select_guest")
				{
					
					divIndGuestAssign.hide("fast");
					$("#ind_rsvp").text('');
					$("#ind_unassigned").text('');
				}
			else
				{
					//alert( $("#dd_unassigned_guest").val() );
					divIndGuestAssign.hide("fast");
					divIndGuestAssign.show("slow");
					$("#ind_rsvp").text('');
					$("#ind_unassigned").text('');
					
					
					var varNumOfGuests = unAssignedGuests.num_of_rows;
					var varGuests = unAssignedGuests.guests;
					
					for(i=0; i<varNumOfGuests ; i++ )
					{
						if( varGuests[i].guest_id == varGuestId )
						{
							$("#ind_rsvp").text(varGuests[i].rsvp_seats);
							$("#ind_unassigned").text(varGuests[i].un_assigned_seats);
						}
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
				var unAssignedDD = generateGuestDropDown(varUnAssignedGuests, varAssignedGuests);
				
				if(unAssignedDD != '')
				{
					$("#div_unassinged_guests").append(unAssignedDD);
					
					$('#dd_unassigned_guest').bind('change', {var_unassigned_guests: varUnAssignedGuests} , function(event){  popGuestAssignmentDet(event.data.var_unassigned_guests) });
					
				}
				else
				{
					$("#div_unassinged_guests").append( '<span class="fld_txt">There are no guests that require seating.</span>' );
				}
				
				
			}
			
			if( varCurrentTable!=undefined)
			{
				generateTableDetail(varCurrentTable);
			}
			if(varAssignedGuests!=undefined)
			{
				generateAssignedGuests(varAssignedGuests , varAllTableGuests, varCurrentTable);
				createEventHandler( varAssignedGuests , varAllTableGuests );
			}
		}
		
		function createEventHandler( varAssignedGuests , varAllTableGuests )
		{
			var tmpVarAssignedGuest = varAssignedGuests ;
			var numOfRows = tmpVarAssignedGuest.num_of_rows;
			var allTables = tmpVarAssignedGuest.guests;
			if(numOfRows > 0)
			{
				var tmpAllTable = 	varAllTableGuests;
				
				var numOfRows = tmpAllTable.num_of_rows;
				var varTables = tmpAllTable.tables;
				
				for( var i =0 ; i < numOfRows ; i++)
				{
					var tmpTableGuest = varTables[i];
					
					if(tmpTableGuest!=undefined && tmpTableGuest.table_guest_id != ''
								&& tmpTableGuest.table_id == varTableId )
					{
						$('#table_guest_'+tmpTableGuest.guest_id ).bind('click', {guest_id: tmpTableGuest.guest_id } , function(event){  updateGuestAssignment(event.data.guest_id) });
						$('#delete_table_guest_'+tmpTableGuest.guest_id ).bind('click', {guest_id: tmpTableGuest.guest_id } , function(event){  deleteGuestAssignment(event.data.guest_id) });
					}
				}
			}
		}
		
		function deleteGuestAssignment( varGuestId )
		{
			var dataString = 'event_id='+varEventId+"&admin_id="+varAdminId+"&table_id="+varTableId+'&guest_id='+varGuestId+
			'&num_of_new_seats=' + $("#assign_guest_"+varGuestId).val() +"&delete_seating=true";
			var actionUrl = 'proc_save_table_guests.jsp';
			var methodType = 'POST';
			makeAjaxCall(actionUrl,dataString,methodType,saveTableGuestList);
		}
		
		function updateGuestAssignment(varGuestId)
		{			
			var dataString = 'event_id='+varEventId+"&admin_id="+varAdminId+"&table_id="+varTableId+'&guest_id='+varGuestId+
				'&num_of_new_seats=' + $("#assign_guest_"+varGuestId).val() +"&update_seating=true";
			var actionUrl = 'proc_save_table_guests.jsp';
			var methodType = 'POST';
			makeAjaxCall(actionUrl,dataString,methodType,saveTableGuestList);
			
			//console.log('updateGuestAssignment : dataString = ' + dataString );
			//alert('updateGuestAssignment : dataString = ' + dataString );
			//$('#err_mssg').text('');
			//$('#success_mssg').text('');
			//makeAjaxCall(actionUrl,dataString,methodType,redrawGuestEventList);
		}
		
		function generateAssignedGuests(assignedGuests, allTableGuests, currentTable)
		{
			$("#div_assigned_guests_table").assignedtableguest({				
				varAssignedGuest : assignedGuests,
				varAllTableGuests : allTableGuests,
				var_event_id : varEventId,
				var_admin_id : varAdminId,
				var_table_id : varTableId,
				varCurrentTable : currentTable
			});
		}
		
		function generateTableDetail(currentTable)
		{
			$("#seat_per_table").text(currentTable.num_of_seats);
			$("#table_name").text(currentTable.table_name);

			$("#vacant_per_table").text('0');
			
		}
		
		function generateGuestDropDown(unAssignedGuests, assignedGuests)
		{
			var varNumOfUnassignedGuests = unAssignedGuests.num_of_rows;
			var varNumOfAssignedGuests = assignedGuests.num_of_rows;
			var varUnAGuests = unAssignedGuests.guests;
			var varAssignedGuests = assignedGuests.guests;
			
			
			var varUnAssignedGuestDD = '';
			
			for(i=0; i<varNumOfUnassignedGuests ; i++ )
			{
				for(j=0; j<varNumOfAssignedGuests ; j++ )
				{
					var hasGuestBeenAssigned = false;
					if( varUnAGuests[i].guest_id == varAssignedGuests[j].guest_id )
					{
						hasGuestBeenAssigned = true;
						break;
					}
				}
				if(!hasGuestBeenAssigned)
				{
					varUnAssignedGuestDD = varUnAssignedGuestDD + '<option id="'+varUnAGuests[i].guest_id+'"  value="'
					+ varUnAGuests[i].guest_id+'">' 
					+ varUnAGuests[i].first_name + ' ' + varUnAGuests[i].last_name  + '</option>'
				}

			}
			
			if( varUnAssignedGuestDD != '' )
			{
				varUnAssignedGuestDD = '<select id="dd_unassigned_guest" name="dd_unassigned_guest"> ' +
				' <option id="select_guest" value="select_guest">Select a guest</option>' + varUnAssignedGuestDD + '</select>';
			}

			
			//varUnAssignedGuestDD = varUnAssignedGuestDD + '</select>';
			
			return varUnAssignedGuestDD;
		}
		</script>
		<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>