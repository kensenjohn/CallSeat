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

	String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
	boolean isFromLanding = ParseUtil.sTob(request.getParameter("from_landing"));
	String sEventId = ParseUtil.checkNull(request.getParameter("lobby_event_id"));
	String sAdminId = ParseUtil.checkNull(request.getParameter("lobby_admin_id"));
	jspLogging.info("Invoked by landing page : " + isFromLanding);
	String sEventTitle = "New Event";
	
	//UserInfoManager userInfo = new UserInfoManager();	
	//userInfo.createUserInfoBean(new UserInfoBean());
	
	String sAdminUserId = "";
	AdminBean adminBean = new AdminBean();
	EventBean eventBean = new EventBean();	
	if(isFromLanding)
	{
		AdminManager adminManager = new AdminManager();		
		adminBean = adminManager.createAdmin();
		
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
		if(sAdminId!=null && !"".equalsIgnoreCase(sAdminId) && sEventId!=null && !"".equalsIgnoreCase(sEventId))
		{
			AdminManager adminManager = new AdminManager();		
			adminBean = adminManager.getAdmin(sAdminId);
			
			EventManager eventManager = new EventManager();
			eventBean = eventManager.getEvent(sEventId);
			
			sEventDate = eventBean.getHumanEventDate();
		}
		
	}
	
	sEventId = eventBean.getEventId();
	sAdminId = adminBean.getAdminId();
	
	String eventName = eventBean.getEventName();
	String eventDate = "10/12/2011";
	
	eventDate = "10/12/2011";
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/web/css/blue/style.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
   <div class="page_setup">
		<div class="container rounded-corners">
			<div style="margin:5px;">
			<jsp:include page="../common/top_nav.jsp"/>
			<jsp:include page="lobby_tab.jsp">
				<jsp:param name="select_tab" value="event_tab"/>
				<jsp:param name="lobby_header" value="<%=eventName %>"/>
				<jsp:param name="lobby_sec_header" value="<%=eventDate %>"/>
			</jsp:include>
			<div class="main_body">
				<div class="clear_both landing_input">					
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
				</div>
			</div>
			</div>
		</div>
	</div>
	<div id="action_fancy_box">
		
	</div>
</body>
<script>
	!window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>
<script type="text/javascript" src="/web/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="/web/js/jquery.tableformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript">
	var varEventID = '<%=sEventId%>'
	var varAdminID = '<%=sAdminId%>'
	$(document).ready(function() {
		
		$("#event_summary_tab").click(function(){
			displayTableView('li_event_summary');
			toggleActionNavs('');
		});
		$("#table_view_tab").click(function(){
			displayTableView('li_table_view');
			toggleActionNavs('table_action_nav');
		});
		$("#guest_view_tab").click(function(){
			displayGuestView('li_guest_view');

			toggleActionNavs('invite_guest_action_nav');
		});
		$("#phone_num_tab").click(function(){
			displayTableView('li_phone_num');

			toggleActionNavs('');
		});
		$("#table_action_nav").show();
		
		
		$("#sched_date").datepick();
		
		
		$("#add_table").fancybox({
			'width'				: '75%',
			'height'			: '80%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
		$("#add_guest").fancybox({
			'width'				: '75%',
			'height'			: '90%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
		
		$("#credentials").fancybox({
			'width'				: '75%',
			'height'			: '75%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
		
		loadActions();
		loadTables();
	});
	
	function toggleActionNavs(action_nav_id)
	{
		$("#action_nav_div div").each(function(index){
			$(this).hide();
		});
		if(action_nav_id!=undefined && action_nav_id!='')
		{
			$("#"+action_nav_id).show();
		}
		
		
		
	}
	
	function switchTab(current_tab_id)
	{
		$('#div_tab_nav li').each(function(index) {
		    $(this).removeClass('active');
		});
		$('#'+current_tab_id).addClass('active');
		
	}
	
	function displayTableView(tab_id)
	{
		switchTab(tab_id);
		loadTables();
	}
	
	function displayGuestView(tab_id)
	{
		switchTab(tab_id);
		loadTables();
	}
	
	function loadActions()
	{
		$("#lnk_event_id").click(function() 
		{
			
		});
		$("#lnk_guest_id").click(function() 
		{
			//window.location = 'guest_setup.jsp?event_id='+varEventID+'&admin_id='+varAdminID;
			
			$("#frm_lobby_tab").attr("action" , "guest_setup.jsp");
			$("#lobby_event_id").val(varEventID);
			$("#lobby_admin_id").val(varAdminID);
			
			$("#frm_lobby_tab").submit();
		});
		$("#lnk_dashboard_id").click(function() {
			
		});
	}
	
	function loadTables()
	{
		//alert("table lolad");
		
		var dataString = '&event_id='+ varEventID;
		var actionUrl = "proc_load_table.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getTableGuestResult);
		
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
			
			$('#del_'+varTableId).click(function() {
				delete_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
			});
			$('#edit_'+varTableId).click(function() {
				edit_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
			});
			$("#link_guest_"+varTableId).fancybox({
				'width'				: '98%',
				'height'			: '98%',
				'autoScale'			: false,
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'type'				: 'iframe',
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
	
	
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>
