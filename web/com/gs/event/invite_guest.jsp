<%@ page import="com.gs.common.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>

	<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
	<body style="height:auto;">
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
        <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
		<div  class="fnbx_scratch_area">
				<div class="row" >
					<div class="offset_0_5 span10">
						<h2 class="txt txt_center">Invite guests to Event <span id="span_event_name"><%=sTitle %></span></h2>
					</div>
				</div>
				<div class="row">
					<div class="offset_0_5 span6">
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
					<div class="offset_0_5 span11" id='guest_list'>
					</div>
				</div>
			</form>
			
		</div>
		<div id="loading_wheel" style="display:none;">
			<img src="/web/img/wheeler.gif">
		</div>
	</body>
	
	<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varEventId = '<%=sEventId%>';
		$(document).ready(function() {
			loadUninvitedGuests();
		});
		
		function loadUninvitedGuests()
		{
			$("#loading_wheel").show();
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
		
		$.ajaxSetup({
		    beforeSend: function(data) {
		        $("#loading_wheel").show();
		    },
		    complete: function(data) {
		        $("#loading_wheel").hide();
		    }
		});
		
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

						displayMessages( varArrErrorMssg , true);
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
							displayMessages( varArrOkMssg , false);
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
			$("#loading_wheel").hide();
		}
		
		function generateUninvitedTable(varUninvitedGuest)
		{
			var numOfEvents = varUninvitedGuest.num_of_rows;
			if(numOfEvents>0)
			{
				var varArrGuest = varUninvitedGuest.guests;
				var varGuestTable = '<table cellspacing="1" id="table_details" class="table table-striped"> '+create_header()+''+create_rows(varArrGuest)+'</table>';
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
			//$('#success_mssg').text('');
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
			//$('#success_mssg').text('');
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
						displayMessages( varArrErrorMssg , true);
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
							$('#event_assign_'+varResponseGuestId).text('Invite Guest');
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
							displayMessages( varArrErrorMssg , false);
						}
					}
				}
			}
		}
		
		function create_header()
		{
			var valHeader = '<thead><tr> ' + 
			'<th style="width:7%" >Invited</th>'+
			'<th style="width:27%"  >Guest Name</th>'+
			'<th style="width:18%" >Invited for seats</th>'+
			'<th style="width:18%" >RSVP to seats</th><th style="width:30%" class="tbl_th"></th>'+
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
					'<td ><span style="text-align:center;" id="event_invite_status_'+varTmpGuest.guest_id+'">No</span></td>'+
					'<td  >'+varTmpGuest.user_info.first_name+' '+varTmpGuest.user_info.last_name+'</td>'+
					'<td ><input id="event_invited_'+varTmpGuest.guest_id+'" name="event_invited_'+varTmpGuest.guest_id+'" class="span1"  type="textbox"></td>' +
					'<td  ><input id="event_rsvp_'+varTmpGuest.guest_id+'" name="event_rsvp_'+varTmpGuest.guest_id+'" class="span1" type="textbox" ></td>'+
					'<td  >'+
					'<button id="event_assign_'+varTmpGuest.guest_id+'" name="event_assign_'+varTmpGuest.guest_id+'" type="button" class="btn btn-small" >Invite Guest</button>'+
					'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
					'<button id="event_remove_'+varTmpGuest.guest_id+'" name="event_remove_'+varTmpGuest.guest_id+'" type="button" class="btn btn-small" style="display:none;">Uninvite Guest</button>'+
					'</td><input type="hidden" value="" id="event_guest_id_'+varTmpGuest.guest_id+'" name="event_guest_id_'+varTmpGuest.guest_id+'"></tr>'
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
<%
	}
	catch(Exception e)
	{
		jspLogging.info("Exception while inviting new guests to event : " + ExceptionHandler.getStackTrace(e) );
	}
%>	
	