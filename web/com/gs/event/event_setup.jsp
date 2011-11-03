<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>


<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/header_bottom.jsp"/>

<%
	Logger jspLogging = LoggerFactory.getLogger("JspLogging");

	String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
	boolean isFromLanding = ParseUtil.sTob(request.getParameter("from_landing"));
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
		EventManager eventManager = new EventManager();
		eventBean = eventManager.createEvent(adminBean.getAdminId());
		jspLogging.debug("Admin Bean : " + adminBean);
	}
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/web/css/blue/style.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
   <div class="page_setup">
		<div class="container rounded-corners">
			<jsp:include page="../common/top_nav.jsp"/>
			<jsp:include page="lobby_tab.jsp">
				<jsp:param name="select_tab" value="event_tab"/>
			</jsp:include>
			<div class="main_body">
				<div class="clear_both landing_input">					
											
					<jsp:include page="../common/action_nav.jsp">
						<jsp:param name="admin_id" value="<%=adminBean.getAdminId() %>"/>
						<jsp:param name="event_id" value="<%=eventBean.getEventId() %>"/>
						<jsp:param name="select_action_nav" value="table_tab"/>
					</jsp:include>
				</div>
				<div  class="clear_both" style="width: 100%;  text-align: center;">
				<div  class="clear_both" id="div_table_details">
					
				</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	!window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>
<script type="text/javascript" src="/web/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="/web/js/jquery.tableformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript">
	var varEventID = '<%=eventBean.getEventId()%>'
	var varAdminID = '<%=adminBean.getAdminId()%>'
	$(document).ready(function() {
		
		$("#sched_date").datepick();
		
		$("#add_table").fancybox({
			'width'				: '75%',
			'height'			: '75%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
		$("#add_guest").fancybox({
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
	
	function getTableGuestResult(jsonResult)
	{
		//alert(jsonResult.value);
		
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
			//alert(jsonResult.success);
			
			var tableDetails = jsonResult.table_detail;
			
			if(tableDetails!=undefined)
			{
				var numOfRows = tableDetails.num_of_rows;
				var allTables = tableDetails.tables;
				
				$("#div_table_details").tableformatter({
					varTableDetails : tableDetails,
					varDeleteTableURL : '/web/com/gs/event/proc_delete_table.jsp'
				});
				applyActionEvents(tableDetails);
				
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
				delete_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
			});
			$('#guest_'+varTableId).click(function() {
				delete_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
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
