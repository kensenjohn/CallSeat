<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
	<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
	<body style="height:auto;">
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sGuestId = ParseUtil.checkNull(request.getParameter("guest_id"));
		

		String sGuestFirstName = ParseUtil.checkNull(request.getParameter("guest_first_name"));
		
		
		jspLogging.info("Assign to event  for : " + sAdminId);
%>
        <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
		<div  class="fnbx_scratch_area">
				<div class="row" >
					<div class="offset1 span10">
						<h2 class="txt txt_center">Invite <%=sGuestFirstName%></h2>
					</div>
				</div>
				<div class="row">
					<div class="offset1 span6">
						<span id="err_mssg"></span>
					</div>
				</div>
				<div class="row">
					<div class="span6">
						&nbsp;
					</div>
				</div>
				<form id='frm_event_guests'>
					<div class="row" >
						<div class="offset1 span12" id='event_list'>
						</div>
					</div>
				</form>
		</div>
	</body>
	<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript" src="/web/js/general.js"></script>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varGuestId = '<%=sGuestId%>';
		var varEventId = '<%=sEventId%>';
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
						displayMessages( varArrErrorMssg , true );
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
				var varEventTable = '<table cellspacing="1" id="table_details" class="table table-striped"> '+create_header()+''+create_rows(varArrEvents)+'</table>';
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
						$('#event_invite_status_'+varTmpEventId).text('Yes');
						$('#event_invited_'+varTmpEventId).val(varGuest.total_seats);
                        if( varGuest.rsvp_seats == -1 ) {
                            $('#event_rsvp_'+varTmpEventId).val('No Response');
                        } else if( varGuest.rsvp_seats == '0' ) {
                            $('#event_rsvp_'+varTmpEventId).val('Not Attending');
                        } else {
                            $('#event_rsvp_'+varTmpEventId).val(varGuest.rsvp_seats);
                        }
						$('#event_assign_'+varTmpEventId).text('Update');
						$('#event_remove_'+varTmpEventId).show();
						$('#event_guest_id_'+varTmpEventId).val(varGuest.event_guest_id);
						
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
                        if( $('#event_invited_'+varTmpEventId).val() == 'Not Invited' ) {
                            managePlaceholder( 'event_invited_'+varTmpEventId,'Not Invited');
                        }
                        if( $('#event_rsvp_'+varTmpEventId).val() == 'No Response' ) {
                            managePlaceholder( 'event_rsvp_'+varTmpEventId,'No Response');
                        }
                        if( $('#event_rsvp_'+varTmpEventId).val() == 'Not Attending' ) {
                            managePlaceholder( 'event_rsvp_'+varTmpEventId,'Not Attending');
                        }

						$('#event_assign_'+varTmpEventId).bind('click', {event_id: varTmpEventId} , function(event){  inviteEventHandler(event.data.event_id) });
						$('#event_remove_'+varTmpEventId).bind('click', {event_id: varTmpEventId} , function(event){ 
							
								$.msgBox({
								    title: "Uninvite Guest",
								    content: "Are you sure you want to remove this guest from this event's invitee list?",
								    type: "confirm",
								    buttons: [{ value: "Yes" }, { value: "No" }, { value: "Cancel"}],
								    success: function (result) {
								        if (result == "Yes") {
								            //alert("One cup of coffee coming right up!");
								        	//uninvite_event_guest_action(event.data.tmp_guest_id , event.data.tmp_event_id , event.data.tmp_event_guest_id  );
								        	unInviteEventHandler(event.data.event_id);
								        }
								    }
								});
							
							});
					}
				}
			}
			
			
		}
		
		function inviteEventHandler(varTmpEventId)
		{
			//alert('invite to ' + varTmpEventId);
			var dataString = 'event_id='+varTmpEventId+'&guest_id='+varGuestId+'&admin_id='+varAdminId+
					'&invited_seats='+$('#event_invited_'+varTmpEventId).val()+'&rsvp_seats='+$('#event_rsvp_'+varTmpEventId).val()+
					'&event_guest_id='+ $('#event_guest_id_'+varTmpEventId).val() +
					'&invite_guest=true';
			var actionUrl = 'proc_guest_events.jsp';
			var methodType = 'POST';
			$('#err_mssg').text('');
			$('#success_mssg').text('');
			makeAjaxCall(actionUrl,dataString,methodType,redrawGuestEventList);
			
		}
		function unInviteEventHandler(varTmpEventId)
		{
			var dataString = 'event_id='+varTmpEventId+'&guest_id='+varGuestId+'&admin_id='+varAdminId+
			'&event_guest_id='+ $('#event_guest_id_'+varTmpEventId).val() +
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
						displayMessages( varArrErrorMssg , true );
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
							$('#event_guest_id_'+varResponseEventId).val('');
							$('#event_invite_status_'+varResponseEventId).text('No');
							$('#event_invited_'+varResponseEventId).val('');
							$('#event_rsvp_'+varResponseEventId).val('');
							$('#event_remove_'+varResponseEventId).hide();
							$('#event_assign_'+varResponseEventId).text('Invite');
						}
						
						if(varIsInvited == true)
						{
							if(varEventGuestId!=undefined && varEventGuestId!='')
							{
								$('#event_guest_id_'+varResponseEventId).val(varEventGuestId);
							}
							$('#event_invite_status_'+varResponseEventId).text('Yes');
							$('#event_assign_'+varResponseEventId).text('Update');
							$('#event_remove_'+varResponseEventId).show();
						}
						
						parent.loadGuests( varEventId , varAdminId );
						var varIsMessageExist = varResponseObj.is_message_exist;
						if(varIsMessageExist == true)
						{
							var jsonResponseMessage = varResponseObj.messages;
							var varArrErrorMssg = jsonResponseMessage.ok_mssg
							displayMessages( varArrErrorMssg , false );
						}
					}
				}
			}
		}
		
		function create_header()
		{
			var valHeader = '<thead><tr> ' + 
			'<th style="width:12%" class="tbl_th">Is Invited</th>'+
			'<th style="width:27%" class="tbl_th">Seating Plan Name</th>'+
			'<th style="width:18%" class="tbl_th">Invited for seats</th>'+
			'<th style="width:18%" class="tbl_th">RSVP to seats<br><h4 style="font-weight: normal;font-size: 85%;">0 if guest will not attend<br>-1 or leave blank for no response</h4></th><th style="width:25%" class="tbl_th"></th>'+
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
					'<td  class="tbl_td"><span style="text-align:center;" id="event_invite_status_'+varTmpEvent.event_id+'">No</span></td>'+
					'<td  class="tbl_td">'+varTmpEvent.event_name+'</td>'+
					'<td  class="tbl_td txt_center"><input id="event_invited_'+varTmpEvent.event_id+'" name="event_invited_'+varTmpEvent.event_id+'" class="ispn2"  type="text" value="Not Invited"></td>' +
					'<td  class="tbl_td txt_center"><input id="event_rsvp_'+varTmpEvent.event_id+'" name="event_rsvp_'+varTmpEvent.event_id+'" class="ispn2" type="text"  value=""></td>'+
					'<td  class="tbl_td txt_center">'+
					'<button id="event_assign_'+varTmpEvent.event_id+'" name="event_assign_'+varTmpEvent.event_id+'" type="button" class="btn btn-small" >Invite</button>'+
					'<button id="event_remove_'+varTmpEvent.event_id+'" name="event_remove_'+varTmpEvent.event_id+'" type="button" class="btn btn-small" style="display:none;">Uninvite</button>'+
					'</td><input type="hidden" value="" id="event_guest_id_'+varTmpEvent.event_id+'" name="event_guest_id_'+varTmpEvent.event_id+'"></tr>'
					;
			}
			return valRows;
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
					var txtMssgLocation = varArrMessages[i].txt_loc_id;
					if(txtMssgLocation=='err_mssg')
					{
						isError = true;
					}
				}
				
				if(varMssg!='')
				{
					displayAlert(varMssg,isError);
				}
			}
			

		}
	</script>
	<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>
			
		