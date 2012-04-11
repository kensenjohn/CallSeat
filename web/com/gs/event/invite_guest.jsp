<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
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
	try
	{
		
		jspLogging.info("Assign to guest to event  for : " + sEventId );
		
		EventManager eventManager = new EventManager();
		EventBean eventBean = eventManager.getEvent(sEventId);
		
		String sTitle = eventBean!=null ? ParseUtil.checkNull(eventBean.getEventName()) : "";
%>

		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Invites guest to Event <span id="span_event_name"><%=sTitle %></span></h2>
				<br>
				<span id="err_mssg" style="color: #9d261d;"></span><br>
				<span id="success_mssg" style="color: #46a546;"></span>
			</div>
			<form id='frm_event_guests'>
				<div id='guest_list' style="padding:15px">
				</div>
			</form>
			
		</div>
	</body>
	
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varEventId = '<%=sEventId%>';
		$(document).ready(function() {
			loadUninvitedGuests();
		});
		
		function loadUninvitedGuests()
		{
			var actionUrl = "proc_invite_guests.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId+"&event_id="+varEventId+'&load_data=true';
			makeAjaxCall(actionUrl,dataString,methodType,createGuestList);
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
		
		function createGuestList(jsonResult)
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
						var varIsMessageExist = varResponseObj.is_message_exist;
						if(varIsMessageExist == true)
						{
							var jsonResponseMessage = varResponseObj.messages;
							var varArrOkMssg = jsonResponseMessage.ok_mssg;
							displayMessages( varArrOkMssg );
						}
						
						var varPayload = varResponseObj.payload;
						
						var isUninvitedGuestExist = varPayload.uninvited_guest_exists;
						if(isUninvitedGuestExist)
						{
							var varUninvitedGuestDetails = varPayload.uninvited_guest_detail;							
							generateUninvitedTable(varUninvitedGuestDetails);
							createEventHandler(varUninvitedGuestDetails);
						}
						else
						{
							// alert();
						}
					}
				}
			}
		}
		
		function generateUninvitedTable(varUninvitedGuest)
		{
			var numOfEvents = varUninvitedGuest.num_of_rows;
			if(numOfEvents>0)
			{
				var varArrGuest = varUninvitedGuest.guests;
				var varGuestTable = '<table cellspacing="1" id="table_details" class="bordered-table zebra-striped tbl"> '+create_header()+''+create_rows(varArrGuest)+'</table>';
				$('#guest_list').append(varGuestTable);
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
						$('#event_invite_status_'+varTmpEventId).text('Yes');
						$('#event_invited_'+varTmpEventId).val(varGuest.total_seats);
						$('#event_rsvp_'+varTmpEventId).val(varGuest.total_seats);
					}
				}
			}
		}
		
		function createEventHandler(varUninvitedGuest)
		{
			var numOfGuests = varUninvitedGuest.num_of_rows;
			if(numOfGuests>0)
			{
				var varArrGuest = varUninvitedGuest.guests;
				if(varArrGuest!= undefined)
				{
					for(i = 0; i<varArrGuest.length; i++)
					{
						var varTmpGuest = varArrGuest[i];
						var varTmpGuestId = varTmpGuest.guest_id;
						
						$('#event_assign_'+varTmpGuestId).bind('click', {guest_id: varTmpGuestId} , function(event){  inviteGuestHandler(event.data.guest_id) });
						$('#event_remove_'+varTmpGuestId).bind('click', {guest_id: varTmpGuestId} , function(event){  unInviteGuestHandler(event.data.guest_id) });

					}
				}
			}		
			
		}
		
		function inviteGuestHandler(varGuestId)
		{
			
			var dataString = 'event_id='+varEventId+'&guest_id='+varGuestId+'&admin_id='+varAdminId+
					'&invited_seats='+$('#event_invited_'+varGuestId).val()+'&rsvp_seats='+$('#event_rsvp_'+varGuestId).val()+
					'&event_guest_id='+ $('#event_guest_id_'+varGuestId).val() +
					'&invite_guest=true';
			var actionUrl = 'proc_invite_guests.jsp';
			var methodType = 'POST';
			$('#err_mssg').text('');
			$('#success_mssg').text('');
			makeAjaxCall(actionUrl,dataString,methodType,redrawGuestEventList);
			
		}
		function unInviteGuestHandler(varGuestId)
		{
			var dataString = 'event_id='+varEventId+'&guest_id='+varGuestId+'&admin_id='+varAdminId+
			'&event_guest_id='+ $('#event_guest_id_'+varGuestId).val() +
			'&un_invite_guest=true';
			var actionUrl = 'proc_invite_guests.jsp';
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
						var varIsUnInvited = varPayload.un_invite;
						var varIsInvited = varPayload.invite;
						
						
						if(varIsUnInvited == true)
						{
							$('#event_guest_id_'+varResponseGuestId).val('');
							$('#event_invite_status_'+varResponseGuestId).text('No');
							$('#event_invited_'+varResponseGuestId).val('');
							$('#event_rsvp_'+varResponseGuestId).val('');
							$('#event_remove_'+varResponseGuestId).hide();
							$('#event_assign_'+varResponseGuestId).text('Invite');
						}
						
						if(varIsInvited == true)
						{
							if(varEventGuestId!=undefined && varEventGuestId!='')
							{
								$('#event_guest_id_'+varResponseGuestId).val(varEventGuestId);
							}
							$('#event_invite_status_'+varResponseGuestId).text('Yes');
							$('#event_assign_'+varResponseGuestId).text('Update');
							$('#event_remove_'+varResponseGuestId).show();
							
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
			'<th style="width:7%" class="tbl_th">Invited</th>'+
			'<th style="width:27%" class="tbl_th">Guest Name</th>'+
			'<th style="width:18%" class="tbl_th">Invited for seats</th>'+
			'<th style="width:18%" class="tbl_th">RSVP to seats</th><th style="width:30%" class="tbl_th"></th>'+
			'</tr></thead>';
			return valHeader; 
		}
		function create_rows(varArrGuest)
		{
			valRows = '';
			for(var i = 0; i<varArrGuest.length; i++)
			{
				var varTmpGuest = varArrGuest[i];
				valRows = valRows + '<tr id="event_'+varTmpGuest.guest_id+'">'+
					'<td  class="tbl_td"><span style="text-align:center;" id="event_invite_status_'+varTmpGuest.guest_id+'">No</span></td>'+
					'<td  class="tbl_td">'+varTmpGuest.user_info.first_name+' '+varTmpGuest.user_info.last_name+'</td>'+
					'<td  class="tbl_td txt_center"><input id="event_invited_'+varTmpGuest.guest_id+'" name="event_invited_'+varTmpGuest.guest_id+'" class="span3"  type="textbox"></td>' +
					'<td  class="tbl_td txt_center"><input id="event_rsvp_'+varTmpGuest.guest_id+'" name="event_rsvp_'+varTmpGuest.guest_id+'" class="span3" type="textbox" ></td>'+
					'<td  class="tbl_td txt_center">'+
					'<button id="event_assign_'+varTmpGuest.guest_id+'" name="event_assign_'+varTmpGuest.guest_id+'" type="button" class="action_button primary small" >Invite    </button>'+
					'<button id="event_remove_'+varTmpGuest.guest_id+'" name="event_remove_'+varTmpGuest.guest_id+'" type="button" class="action_button primary small" style="display:none;">Uninvite</button>'+
					'</td><input type="hidden" value="" id="event_guest_id_'+varTmpGuest.guest_id+'" name="event_guest_id_'+varTmpGuest.guest_id+'"></tr>'
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
<%
	}
	catch(Exception e)
	{
		jspLogging.info("Exception while inviting new guests to event : " + ExceptionHandler.getStackTrace(e) );
	}
%>	
	