<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>


<jsp:include page="../common/header_top.jsp"/>

<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%
	Logger jspLogging = LoggerFactory.getLogger("JspLogging");
	String sEventId = ParseUtil.checkNull(request.getParameter("lobby_event_id"));
	String sAdminId = ParseUtil.checkNull(request.getParameter("lobby_admin_id"));
	String sGateAdminId = sAdminId;
%>
	<%@include file="../common/gatekeeper.jsp"%>

<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
	<jsp:include page="../common/top_nav.jsp"/>
	<div class="scratch_area"> 
		<jsp:include page="lobby_tab.jsp">
			<jsp:param name="select_tab" value="guest_tab"/>
			<jsp:param name="lobby_header" value="All Guests"/>
			<jsp:param name="lobby_sec_header" value=""/>
		</jsp:include>
		<jsp:include page="../common/action_nav.jsp">
			<jsp:param name="admin_id" value="<%=sAdminId %>"/>
			<jsp:param name="event_id" value="<%=sEventId %>"/>
			<jsp:param name="select_action_nav" value="all_guest_tab"/> 
		</jsp:include>
		<div class="row">
			<div  class="offset1 span11" id="div_guests_details">
				
			</div>
		</div>
	</div>
</body>
<script>
	!window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>
<script type="text/javascript" src="/web/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="/web/js/jquery.guestsformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript" src="/web/js/credential.js"></script>
<script type="text/javascript">
	var varAdminID = '<%=sAdminId%>';
	var varEventID = '<%=sEventId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	$(document).ready(function() {
		setCredentialEventId(varEventID);
		if(!varIsSignedIn)
		{
			setTopNavLogin(varAdminID,varEventID,'guest_setup.jsp');			
		}
		$("#add_all_guests").fancybox({
			'width'				: '80%',
			'height'			: '90%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			'padding'			: 0,
			'margin'			: 0
		});
		$("#all_guests_action_nav").show();
		loadActions();
		loadGuests(varEventID,varAdminID);		
	});
	function loadActions()
	{		
		setNewEventClick();
		setAllGuestButtonClick();
		setLobbyButtonClick();
	}
	
	function createNewInputElement(elemId,elemValue,elemParent,elemType)
	{
		var varElem = 
			jQuery('<input/>', {
			    id:		elemId,
			    name:	elemId,
			    value: 	elemValue,
			    type:	elemType
			});
		
		varElem.appendTo('#'+elemParent);
	}
	function loadGuests(varTmpEventId, varTmpAdminId)
	{
		var dataString = '&event_id='+ varTmpEventId + '&admin_id='+ varTmpAdminId;
		var actionUrl = "proc_load_guests.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getEventGuestResult);
	}
	
	function getDataAjax(actionUrl,dataString,methodType, callBackMethod)
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
	
	function getEventGuestResult(jsonResult)
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
		else  if( jsonResult.status == 'ok' && varResponseObj !=undefined)
		{
			var varIsPayloadExist = varResponseObj.is_payload_exist;
			if(varIsPayloadExist == true)
			{
				var jsonResponseObj = varResponseObj.payload;	
				
				var guestRows = jsonResponseObj.guest_rows;
				var eventGuestRows = jsonResponseObj.event_guest_rows;
				
				if(guestRows!=undefined)
				{
					var numOfRows = guestRows.num_of_rows;
					var allTables = guestRows.guests;
					
					$("#div_guests_details").guestformatter({
						varGuestDetails : guestRows,
						varEventGuestDetails : eventGuestRows,
						varAdminId : varAdminID,
						varDeleteTableURL : '/web/com/gs/event/proc_delete_table.jsp'
					});
					applyActionEvents(guestRows);
				}
			}
		}
	}
	
	function applyActionEvents(guestRows)
	{
		var numOfRows = guestRows.num_of_rows;
		
		if(numOfRows > 0)
		{
			var allGuests = guestRows.guests;
			for( i=0; i<numOfRows; i++ )
			{
				var tmpGuest = allGuests[i];
				
				if(tmpGuest!=undefined && tmpGuest.guest_id != '')
				{
					$("#link_edit_guest_"+tmpGuest.guest_id).fancybox({
						'width'				: '80%',
						'height'			: '90%',
						'autoScale'			: false,
						'transitionIn'		: 'none',
						'transitionOut'		: 'none',
						'type'				: 'iframe',
						'padding'			: 0,
						'margin'			: 0
					});
					
					$("#link_edit_event_assign_"+tmpGuest.guest_id).fancybox({
						'width'				: '80%',
						'height'			: '90%',
						'autoScale'			: false,
						'transitionIn'		: 'none',
						'transitionOut'		: 'none',
						'type'				: 'iframe',
						'padding'			: 0,
						'margin'			: 0
					});
					$('#link_del_'+tmpGuest.guest_id).click(function(){ $( "#dialog-confirm" ).show(); });
				}
			}
		}
	}
	
	/*function credentialSuccess(jsonResponse,varSource)
	{
		$("#login_name_display").text(jsonResponse.first_name);
		$("#login_name_display").addClass("bold_text");
		resetAdminId(jsonResponse.user_id);
	}
	function resetAdminId(tmpAdminId)
	{
		varAdminID = tmpAdminId;
		setLobbyButtonClick();
	}*/
	// TODO: load the lobby after login with current admin's user.
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>