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
	Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);

	String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
	boolean isFromLanding = ParseUtil.sTob(request.getParameter("from_landing"));
	String sEventId = ParseUtil.checkNull(request.getParameter("lobby_event_id"));
	String sAdminId = ParseUtil.checkNull(request.getParameter("lobby_admin_id"));
	boolean isNewEventClicked= ParseUtil.sTob(request.getParameter("lobby_create_new"));
	
	if(!isFromLanding)
	{
	%>
		<%@include file="/web/com/gs/common/gatekeeper.jsp"%>
	<%
	}
	
	jspLogging.info("Invoked by landing page : " + isFromLanding);
	String sEventTitle = "New Event";
	
	//UserInfoManager userInfo = new UserInfoManager();	
	//userInfo.createUserInfoBean(new UserInfoBean());
	
	String sAdminUserId = "";
	AdminBean adminBean = new AdminBean();
	EventBean eventBean = new EventBean();	
	if(isFromLanding)
	{

		if(sAdminId==null || "".equalsIgnoreCase(sAdminId))
		{
			sAdminId = ParseUtil.checkNullObject(session.getAttribute("u_id"));
		}
		
		AdminManager adminManager = new AdminManager();		
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
		{
			 adminBean = adminManager.getAdmin(sAdminId);
		}
		else
		{
			adminBean = adminManager.createAdmin();
		}
		
		
		
		
		if(adminBean!=null)
		{
			EventCreationMetaDataBean eventMeta = new EventCreationMetaDataBean();
			eventMeta.setAdminBean(adminBean);
			eventMeta.setEventDate(sEventDate);
			eventMeta.setEventDatePattern("MM/dd/yyyy");
			eventMeta.setEventTimeZone("UTC");
			
			EventManager eventManager = new EventManager();
			eventBean = eventManager.createEvent(eventMeta);
		}
		
		jspLogging.debug("Admin Bean : " + adminBean);
	}
	else
	{
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId))
		{
			AdminManager adminManager = new AdminManager();		
			adminBean = adminManager.getAdmin(sAdminId);
		}
		if(isNewEventClicked)
		{
			
		}
		else
		{
			if(sEventId!=null && !"".equalsIgnoreCase(sEventId))
			{
				
				EventManager eventManager = new EventManager();
				eventBean = eventManager.getEvent(sEventId);
				
				sEventDate = eventBean.getHumanEventDate();
			}
		}
		
	}
	
	sEventId = eventBean.getEventId();
	sAdminId = adminBean.getAdminId();
	
	String eventName = "";
	String eventDate = "";
	if(isNewEventClicked)
	{
		eventName = "Create New";
		eventDate = "";
	}
	else
	{
		eventName = eventBean.getEventName();
		eventDate = "("+sEventDate+")";
	}
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />


<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
		<div class="container rounded-corners">
			<div style="margin:5px;">
			<jsp:include page="../common/top_nav.jsp"/>
			<jsp:include page="lobby_tab.jsp">
				<jsp:param name="select_tab" value="event_tab"/>
				<jsp:param name="lobby_header" value="<%=eventName %>"/>
				<jsp:param name="lobby_sec_header" value="<%=eventDate%>"/>
			</jsp:include>
			<div class="main_body">
				<div class="clear_both">					
					<jsp:include page="../common/tab_view_nav.jsp">
						<jsp:param name="admin_id" value="<%=sAdminId %>"/>
					</jsp:include>	
					<jsp:include page="../common/action_nav.jsp">
						<jsp:param name="admin_id" value="<%=sAdminId %>"/>
						<jsp:param name="event_id" value="<%=sEventId %>"/>
						<jsp:param name="select_action_nav" value="table_tab"/>
						<jsp:param name="logged_in" value="<%=isSignedIn %>"/>
					</jsp:include> 
				</div>
				<div  class="clear_both" style="width: 100%;  text-align: center;">
				<div  class="clear_both" id="tab_view_area">
					
				</div>
				<div  class="clear_both" id="div_table_details">
					
				</div>
				<div  class="clear_both" id="div_guests_details">
					
				</div>
				<div  class="clear_both" id="div_event_summary" style="display:none;">
				<div class="row">
					<div class="span7">
					<form id="frm_event_update" >
					<fieldset>
								<div class="clearfix-tight">
									<label for="table_name">Event Name :</label>
									<div class="input">
										<input type="text"  class="span4" value="" id="e_summ_event_name" name = "e_summ_event_name"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="table_name">Event Date : </label>
									<div class="input">
										<input type="text" class="span4" value="" id="e_summ_event_date" name = "e_summ_event_date"/>
									</div>
								</div>
								<div class="actions">
								<%
										if(isNewEventClicked)
										{
								%>
											<button id="create_event" name="create_event" type="button" class="action_button primary small">Create New Event</button>
								<%
										}
										else
										{
								%>
											<button id="save_event" name="save_event" type="button" class="action_button primary small">Save Changes</button>
								<%			
										}
								%>								
						            
						        </div>
						        
					</fieldset>	
					</form>	
					</div>
				</div>
				<div class="row">
					<div class="span2">
						&nbsp;
					</div>
					<div class="span5">
					 <table>
						<tr>
							<td align="right">Tables created : </td>
							<td><span id="e_summ_total_table" > 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Seats created : </td>
							<td><span id="e_summ_total_seats"> 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Seats assigned : </td>
							<td><span id="e_summ_assigned_seats"> 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Guest parties : </td>
							<td><span id="e_summ_guest_parties"  > 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Total invited : </td>
							<td><span id="e_summ_total_invited" > 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Total rsvp : </td>
							<td><span id="e_summ_total_rsvp"> 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Rsvp Telephone : </td>
							<td><span id="e_summ_rsvp_telnum" > 0 </span></td>
						</tr>
						<tr>
							<td  align="right">Seating Telephone : </td>
							<td><span id="e_summ_seating_telnum"> 0 </span></td>
						</tr>
					</table>
					</div>
				</div>
				</div>
			</div>
			</div>
		</div>
	</div>
	<div id="action_fancy_box">
		
	</div>
</body>

<script type="text/javascript" src="/web/js/jquery.tableformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript" src="/web/js/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="/web/js/jquery.eventguests.1.0.0.js"></script>

<script type="text/javascript">
	var varEventID = '<%=sEventId%>';
	var varAdminID = '<%=sAdminId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	var varIsNewEventCreateClicked = <%=isNewEventClicked%>;
	$(document).ready(function() {
		$("#e_summ_event_date").datepick();
		$("#event_summary_tab").click(function(){

			toggleActionNavs('li_event_summary');
			displayEventSummaryView('li_event_summary');
			
			
		});
		$("#save_event").click(function(){			
			saveEvent();
		});
		$("#create_event").click(function(){
			createEvent();
		});
		
		$("#table_view_tab").click(function(){

			toggleActionNavs('table_action_nav');
			displayTableView('li_table_view');
		});
		$("#guest_view_tab").click(function(){
			displayGuestView('li_guest_view');

			toggleActionNavs('invite_guest_action_nav');
		});
		if(!varIsSignedIn)
		{
			$("#phone_num_tab").attr("href","/web/com/gs/common/credential.jsp?admin_id="+varAdminID
					+"&event_id"+varEventID+"&source=phone_tab");
			
			$("#phone_num_tab").fancybox({
				'width'				: '80%',
				'height'			: '80%',
				'autoScale'			: false,
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'type'				: 'iframe',
				'padding'			: 0,
				'margin'			: 0
			});
			
			$("#login_name_display").removeAttr('href');
			$("#login_name_display").attr('href','/web/com/gs/common/credential.jsp?admin_id='+varAdminID+'&event_id='+varEventID);
		}
		else
		{
			phoneNumTab();
		}
		
		if(varIsNewEventCreateClicked)
		{
			$('#div_event_summary').show();
		}
		else
		{
			$("#table_action_nav").show();
		}
		
		
		
		$("#sched_date").datepick();
		
		
		$("#add_table").fancybox({
			'width'				: '75%',
			'height'			: '80%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
				'padding'			: 0,
				'margin'			: 0
		});
		$("#add_guest").fancybox({
			'width'				: '75%',
			'height'			: '90%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
				'padding'			: 0,
				'margin'			: 0
		});
		$("#invite_guest").fancybox({
			'width'				: '75%',
			'height'			: '90%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
				'padding'			: 0,
				'margin'			: 0
		});		
		
		$("#get_phone_num").fancybox({
			'width'				: '80%',
			'height'			: '80%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			'padding'			: 0,
			'margin'			: 0
		});
		
		loadActions();
		if(varIsNewEventCreateClicked)
		{
			switchTab('li_event_summary');
			$('#div_event_summary').show();
		}
		else
		{
			loadTables();
		}
		
	});
	
	function phoneNumTab()
	{
		$("#phone_num_tab").unbind();
		$("#phone_num_tab").removeAttr("href");
		
		
		$("#phone_num_tab").click(function(){

			toggleActionNavs('li_phone_num');			
			displayPhoneNumberView('li_phone_num');
		});
	}
	
	function toggleActionNavs(action_nav_id)
	{
		$("#action_nav_div div.row").each(function(index){
			$(this).hide();
		});
		if(action_nav_id!=undefined && action_nav_id!='')
		{
			$("#"+action_nav_id).show();
			$("#"+action_nav_id + " div.row").show();
		}
		toggleTabViewArea();	
	}
	
	function toggleTabViewArea()
	{
		$('#tab_view_area').empty();
		$("#div_guests_details").empty();
		$('#div_event_summary').hide();
	}
	
	function switchTab(current_tab_id)
	{
		$('#div_tab_nav li').each(function(index) {
		    $(this).removeClass('active');
		});
		$('#'+current_tab_id).addClass('active');
		
	}
	
	function displayEventSummaryView(tab_id)
	{		
		switchTab(tab_id);
		$('#div_event_summary').show();
		loadEventSummary();
	}
	
	function displayTableView(tab_id)
	{
		switchTab(tab_id);
		loadTables();
	}
	
	function displayGuestView(tab_id)
	{
		switchTab(tab_id);
		loadGuests();
	}
	
	function displayPhoneNumberView(tab_id)
	{
		switchTab(tab_id);
		loadPhoneNumberFrame();
	}
	
	function loadPhoneNumberFrame()
	{
		$('<iframe id="phone_number_frame"/>').load(function(){
		}).appendTo("#tab_view_area");
		 $('#phone_number_frame').attr('src','/web/com/gs/event/phone_number.jsp?admin_id='+varAdminID+'&event_id='+varEventID);
		    $('#phone_number_frame').attr('height','100%');
		    $('#phone_number_frame').attr('width','100%');
		    $('#phone_number_frame').attr('frameborder','0');
	}
	
	function loadActions()
	{
		$("#lnk_event_id").click(function() 
		{
			
		});
		setNewEventClick();
		setAllGuestButtonClick();
		setLobbyButtonClick();
	}
	// This should be acopied everywhere.
	function setNewEventClick()
	{
		$("#lnk_new_event_id").unbind("click");
		$("#lnk_new_event_id").click(function() 
		{
			$("#frm_lobby_tab").attr("action" , "event_setup.jsp");
			$("#lobby_create_new").val(true);
			$("#lobby_admin_id").val(varAdminID);
			
			$("#frm_lobby_tab").submit();
		});
	}
	// This should be acopied everywhere.
	function setAllGuestButtonClick()
	{
		$("#lnk_guest_id").unbind("click");
		$("#lnk_guest_id").click(function() 
		{
			$("#frm_lobby_tab").attr("action" , "guest_setup.jsp");
			$("#lobby_event_id").val(varEventID);
			$("#lobby_admin_id").val(varAdminID);
			
			$("#frm_lobby_tab").submit();
		});
	}
	
	function setLobbyButtonClick()
	{
		$("#lnk_dashboard_id").unbind("click");
		
		$("#lnk_dashboard_id").click(function() {
			$("#frm_lobby_tab").attr("action" , "host_dashboard.jsp");
			$("#lobby_event_id").val(varEventID);
			$("#lobby_admin_id").val(varAdminID);
			
			$("#frm_lobby_tab").submit();
		});
	}
	
	function loadEventSummary()
	{

		var dataString =  '&event_id='+ varEventID + '&admin_id='+ varAdminID;
		var actionUrl = "proc_load_event_summary.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getEventSummaryResult);
	}
	
	function loadTables()
	{
		//alert("table lolad");
		
		var dataString = '&event_id='+ varEventID;
		var actionUrl = "proc_load_table.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getTableGuestResult);
		
	}
	
	function loadGuests()
	{
		var dataString = '&event_id='+ varEventID + '&admin_id='+ varAdminID + '&for_event=true';
		var actionUrl = "proc_load_guests.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getEventGuestResult);
	}
	
	function saveEvent()
	{
		var dataString = '&event_id='+ varEventID + '&admin_id='+ varAdminID;
		dataString = dataString + '&'+$("#frm_event_update").serialize();
		var actionUrl = "proc_save_event.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getUpdatedEvent);
	}
	
	function createEvent()
	{
		var dataString = 'admin_id='+ varAdminID+'&create_event=true';
		dataString = dataString + '&'+$("#frm_event_update").serialize();
		var actionUrl = "proc_save_event.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getUpdatedEvent);
	}
	
	function getUpdatedEvent(jsonResult)
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
			else  if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
				$("#primary_header").text( $("#e_summ_event_name").val() );
				$("#secondary_header").text( '('+$("#e_summ_event_date").val()+')' );
				
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					if(jsonResponseObj.create_event == true)
					{
						//alert(jsonResponseObj.event_bean.event_id)
						varEventID = jsonResponseObj.event_bean.event_id;
					}
				}
				
			}
		}
	}
	
	function getEventGuestResult(jsonResult)
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
			else  if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					
					var eventGuestRows = jsonResponseObj.event_guest_rows;
					
					
					if(eventGuestRows!=undefined)
					{
						var eventGuestDetail = eventGuestRows[varEventID];
						
						$("#div_guests_details").eventguests({
							varEventGuestDetails : eventGuestDetail,
							varAdminId : varAdminID,
							varEventId : varEventID
						});
						applyInvitedGuestActionEvents(eventGuestDetail);
					}
				}
			}
		}
	}
	
	function applyInvitedGuestActionEvents(guestRows)
	{
		var numOfRows = Number(guestRows.num_of_event_guest_rows);
		
		if(numOfRows > 0)
		{
			
			
			var allGuests = guestRows.event_guests;
			
			
			for( i=0; i<numOfRows; i++ )
			{
				var tmpGuest = allGuests[i];
				if(tmpGuest!=undefined && tmpGuest.guest_id != '')
				{
					$("#link_edit_event_guest_"+tmpGuest.guest_id).fancybox({
						'width'				: '80%',
						'height'			: '90%',
						'autoScale'			: false,
						'transitionIn'		: 'none',
						'transitionOut'		: 'none',
						'type'				: 'iframe',
						'padding'			: 0,
						'margin'			: 0
					});
					$("#link_uninvite_event_guest_"+tmpGuest.guest_id).bind('click',
							{'tmp_guest_id':tmpGuest.guest_id,'tmp_event_id':varEventID,
								'tmp_event_guest_id':tmpGuest.event_guest_id},function(event)
							{
								if(confirm('Do you want to uninvite this guest?'))
								{
									uninvite_event_guest_action(event.data.tmp_guest_id , event.data.tmp_event_id , event.data.tmp_event_guest_id  );
								}
								else 
								{
									alert('close dialog');
								}
							});
					
				}
			}
		}
	}
	
	function uninvite_event_guest_action(varTmpGuestId,varTmpEventId,varTmpEventGuestId)
	{
		var confirmDelete = confirm('Do you want to delete this table?');
		
		if(confirmDelete == true)
		{
			//$("#table_"+tableid).remove();
			
			var dataString = '&event_id='+ varTmpEventId + '&guest_id='+varTmpGuestId + '&event_guest_id=' + varTmpEventGuestId;
			var actionUrl = "proc_uninvite_guest.jsp";
			var methodType = "POST";
			
			//getAllTablesData(actionUrl,dataString,methodType);
			
			getDataAjax(actionUrl,dataString,methodType, uninviteGuest);
		}
		
	}
	
	function uninviteGuest(jsonResult)
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
				loadGuests();
			}
		}
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
	
	function processEventSummary( jsonResponseObj )
	{
		var eventSummary = jsonResponseObj.event_summary;
		if(eventSummary!=undefined)
		{
			$("#e_summ_event_name").val(eventSummary.event_name);
			$("#e_summ_event_date").val(eventSummary.event_date);
			$("#e_summ_total_table").text(eventSummary.total_table);
			$("#e_summ_total_seats").text(eventSummary.total_seats);
			$("#e_summ_assigned_seats").text(eventSummary.assigned_seats);
			$("#e_summ_guest_parties").text(eventSummary.total_guest_party);
			$("#e_summ_total_invited").text(eventSummary.total_guest_invited);
			$("#e_summ_total_rsvp").text(eventSummary.total_guest_rsvp);
			$("#e_summ_rsvp_telnum").text(eventSummary.rsvp_tel_number);
			$("#e_summ_seating_telnum").text(eventSummary.seating_tel_number);
		}
		//div_event_summary
	}
	var varHashTableId = '';
	var varHashTables = '';
	
	function processTableGuest( jsonResponseObj )
	{
		var tableDetails = jsonResponseObj.table_detail;
		if(tableDetails!=undefined)
		{
			var numOfRows = tableDetails.num_of_rows;
			var allTables = tableDetails.tables;
			
			$("#tab_view_area").tableformatter({
				varTableDetails : tableDetails,
				varDeleteTableURL : '/web/com/gs/event/proc_delete_table.jsp',
				var_event_id : varEventID,
				var_admin_id : varAdminID
			});
			
			applyActionEvents(tableDetails);
			if(numOfRows>0)
			{	// code to sort the table if there is any data.. the last column containing the action
				// links should not be sortable.
			
				$('#table_details').tablesorter({ headers: { 3: { sorter: false} } });
				
			}
			
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
	
	function getEventSummaryResult(jsonResult)
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
					processEventSummary( jsonResponseObj );
				}
			}
		}
	}
	
	function getTableGuestResult(jsonResult)
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
					processTableGuest( jsonResponseObj );
				}
			}
		}
	}
	
	function applyActionEvents( json_table_details )
	{
		var numOfRows = json_table_details.num_of_rows;
		var allTables = json_table_details.tables;
		
		for( i = 0 ; i<numOfRows ; i++)
		{
			var tmpTable = allTables[i];
			
			var varTableId = tmpTable.table_id;
			
			$('#edit_'+varTableId).click(function() {
				edit_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
			});
			$('#link_del_table_'+varTableId).click(function() {
				delete_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
			});
			$("#link_table_"+varTableId).fancybox({
				'width'				: '90%',
				'height'			: '75%%',
				'autoScale'			: false,
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'type'				: 'iframe',
				'padding'			: 0,
				'margin'			: 0,
				'onClosed'			: function() {
										loadTables();
										}
			});
			$("#link_guest_"+varTableId).fancybox({
				'width'				: '90%',
				'height'			: '75%',
				'autoScale'			: false,
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'type'				: 'iframe',
				'padding'			: 0,
				'margin'			: 0,
				'onClosed'			: function() {
										loadTables();
										}
			});
			
		}
	}
	
	function guest_table_action(url,tableid)
	{
		alert('Edit Guests');
	}
	
	function edit_table_action(url,tableid)
	{
		alert('Edit Table');
	}
	
	function delete_table_action(url,tableid)
	{
		var confirmDelete = confirm('Do you want to delete this table?');
		
		if(confirmDelete == true)
		{
			$("#table_"+tableid).remove();
			
			var dataString = '&event_id='+ varEventID + '&table_id='+tableid;
			var actionUrl = "proc_delete_table.jsp";
			var methodType = "POST";
			
			//getAllTablesData(actionUrl,dataString,methodType);
			
			getDataAjax(actionUrl,dataString,methodType, deleteTable);
		}
		
	}
	
	function deleteTable(jsonResult)
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
			//var varTa
			loadTables();
		}
	}
	
	function credentialSuccess(jsonResponse,varSource)
	{
		$("#get_phone_num_div").hide();
		$("#login_name_display").text(jsonResponse.first_name);
		$("#login_name_display").addClass("bold_text");
		varIsSignedIn = true;
		phoneNumTab();
		
		resetAdminId(jsonResponse.user_id);
	}
	
	function resetAdminId(tmpAdminId)
	{
		varAdminID = tmpAdminId;
		$('#phone_number_frame').removeAttr('src')
		$('#phone_number_frame').attr('src','/web/com/gs/event/phone_number.jsp?admin_id='+varAdminID+'&event_id='+varEventID);
		setLobbyButtonClick();
		setAllGuestButtonClick();
	}
	
	function resetPhoneNumber()
	{
		$("#phone_num_tab").unbind();
		$("#phone_num_tab").removeAttr("href");
		$("#phone_num_tab").click(function(){

			toggleActionNavs('li_phone_num');			
			displayPhoneNumberView('li_phone_num');
		});
	}
	
	function viewPhoneNumberTab()
	{
		toggleActionNavs('li_phone_num');
		displayPhoneNumberView('li_phone_num');
	}
	
	
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>
