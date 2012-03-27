<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sGuestId = ParseUtil.checkNull(request.getParameter("guest_id"));
		

		String sGuestFirstName = ParseUtil.checkNull(request.getParameter("guest_first_name"));
		
		
		jspLogging.info("Assign to event  for : " + sAdminId);
%>

		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Assign <%=sGuestFirstName %> to Events</h2>
				<br>
				<span id="err_mssg" style="color: #9d261d;"></span><br>
				<span id="success_mssg" style="color: #46a546;"></span>
			</div>
			<form id='frm_event_guests'>
				<div id='event_list' style="padding:15px">
				</div>
			</form>
			
		</div>
	</body>
	
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varGuestId = '<%=sGuestId%>';
		$(document).ready(function() {
			loadEvents();
		});
		
		function loadEvents()
		{
			var actionUrl = "proc_guest_events.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId+"&guest_id="+varGuestId+'&load_data=true';
			makeAjaxCall(actionUrl,dataString,methodType,createEventList);
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
						var varPayload = varResponseObj.payload;
						
						var eventDetails = varPayload.event_detail;
						if(eventDetails!=undefined)
						{
							var varEventDD = generateEventTable(eventDetails);
							$("#div_event_list").append(varEventDD);
							
							var eventGuestDetails = varPayload.event_guest_rows;
							if(eventGuestDetails!=undefined)
							{
								populateInviteRsvpBox(eventGuestDetails,varGuestId);
							}
							
							createEventHandler(eventDetails);
						}	
						
						
					}
				}
			}
		}
		 
		function generateEventTable(eventDetails)
		{
			var numOfEvents = eventDetails.num_of_rows;
			if(numOfEvents>0)
			{
				var varArrEvents = eventDetails.events;
				var varEventTable = '<table cellspacing="1" id="table_details" class="bordered-table zebra-striped tbl"> '+create_header()+''+create_rows(varArrEvents)+'</table>';
				$('#event_list').append(varEventTable);
			}
			
		}
		
		function populateInviteRsvpBox(varArrEventGuestDetail,guestId)
		{
			if(varArrEventGuestDetail!=undefined)
			{
				var varArrEventGuest = varArrEventGuestDetail[guestId];
				var varNumOfEvents = varArrEventGuest.num_of_event_guest_rows;
				var isEventAssignedToGuest = false;
				if(varNumOfEvents>0)
				{
					
					
					var varEventGuests = varArrEventGuest.event_guests;
					for(var i=0;i<varNumOfEvents;i++)
					{
						var varGuest = varEventGuests[i];
						var varTmpEventId = varGuest.event_id;
						$('#event_invited_'+varTmpEventId).val(varGuest.total_seats);
						$('#event_rsvp_'+varTmpEventId).val(varGuest.total_seats);
					}
				}
			}
		}
		
		function createEventHandler(eventDetails)
		{
			var numOfEvents = eventDetails.num_of_rows;
			if(numOfEvents>0)
			{
				var varArrEvents = eventDetails.events;
				if(varArrEvents!= undefined)
				{
					for(i = 0; i<varArrEvents.length; i++)
					{
						var varTmpEvent = varArrEvents[i];
						var varTmpEventId = varTmpEvent.event_id;
						//alert('event id - ' + varTmpEventId);
						
						$('#event_assign_'+varTmpEventId).bind('click', {event_id: varTmpEventId} , function(event){  inviteEventHandler(event.data.event_id) });
						$('#event_remove_'+varTmpEventId).bind('click', {event_id: varTmpEventId} , function(event){  unInviteEventHandler(event.data.event_id) });
					}
				}
			}
			
			
		}
		
		function inviteEventHandler(varEventId)
		{
			//alert('invite to ' + varEventId);
			var dataString = 'event_id='+varEventId+'&guest_id='+varGuestId+'&admin_id='+varAdminId+
					'&invited_seats='+$('#event_invited_'+varEventId).val()+'&rsvp_seats='+$('#event_rsvp_'+varEventId).val()+
					'&event_guest_id='+ $('#event_guest_id_'+varEventId).val() +
					'&invite_guest=true';
			var actionUrl = 'proc_guest_events.jsp';
			var methodType = 'POST';
			$('#err_mssg').text('');
			$('#success_mssg').text('');
			makeAjaxCall(actionUrl,dataString,methodType,redrawGuestEventList);
			
		}
		function unInviteEventHandler(varEventId)
		{
			var dataString = 'event_id='+varEventId+'&guest_id='+varGuestId+'&admin_id='+varAdminId+
			'&event_guest_id='+ $('#event_guest_id_'+varEventId).val() +
			'&un_invite_guest=true';
			
			var actionUrl = 'proc_guest_events.jsp';
			var methodType = 'POST';
			$('#err_mssg').text('');
			$('#success_mssg').text('');
			makeAjaxCall(actionUrl,dataString,methodType,redrawGuestEventList);
		}
		
		function redrawGuestEventList(jsonResult)
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
						var varPayload = varResponseObj.payload;
						
						var varResponseEventId = varPayload.response_event_id;
						var varResponseGuestId = varPayload.response_guest_id;
						var varEventGuestId = varPayload.event_guest_id;
						
						if(varEventGuestId!=undefined && varEventGuestId!='')
						{
							$('#event_guest_id_'+varResponseEventId).val(varEventGuestId);
						}
						parent.loadGuests();
						var varIsMessageExist = varResponseObj.is_message_exist;
						if(varIsMessageExist == true)
						{
							var jsonResponseMessage = varResponseObj.messages;
							var varArrErrorMssg = jsonResponseMessage.ok_mssg
							displayMessages( varArrErrorMssg );
						}
					}
				}
			}
		}
		
		function create_header()
		{
			var valHeader = '<thead><tr> ' + 
			'<th style="width:30%" class="tbl_th">Event Name</th>'+
			'<th style="width:20%" class="tbl_th">Invited for seats</th>'+
			'<th style="width:20%" class="tbl_th">RSVP to seats</th><th style="width:30%" class="tbl_th"></th>'+
			'</tr></thead>';
			return valHeader; 
		}
		function create_rows(varArrEvents)
		{
			valRows = '';
			for(var i = 0; i<varArrEvents.length; i++)
			{
				var varTmpEvent = varArrEvents[i];
				valRows = valRows + '<tr id="event_'+varTmpEvent.event_id+'">'+					
					'<td  class="tbl_td">'+varTmpEvent.event_name+'</td>'+
					'<td  class="tbl_td txt_center"><input id="event_invited_'+varTmpEvent.event_id+'" name="event_invited_'+varTmpEvent.event_id+'" class="span3"  type="textbox"></td>' +
					'<td  class="tbl_td txt_center"><input id="event_rsvp_'+varTmpEvent.event_id+'" name="event_rsvp_'+varTmpEvent.event_id+'" class="span3" type="textbox" ></td>'+
					'<td  class="tbl_td txt_center">'+
					'<button id="event_assign_'+varTmpEvent.event_id+'" name="event_assign_'+varTmpEvent.event_id+'" type="button" class="action_button primary small" >Invite to event</button>'+
					'<button id="event_remove_'+varTmpEvent.event_id+'" name="event_remove_'+varTmpEvent.event_id+'" type="button" class="action_button primary small" >Uninvite</button>'+
					'</td><input type="hidden" value="" id="event_guest_id_'+varTmpEvent.event_id+'" name="event_guest_id_'+varTmpEvent.event_id+'"></tr>'
					;
			}
			return valRows;
		}
		function displayMessages(varArrMessages)
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
		}
	</script>
</html>
			
		