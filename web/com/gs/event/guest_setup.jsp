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
	String sEventId = ParseUtil.checkNull(request.getParameter("lobby_event_id"));
	String sAdminId = ParseUtil.checkNull(request.getParameter("lobby_admin_id"));
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/web/css/blue/style.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
   <div class="page_setup">
		<div class="container rounded-corners">
			<jsp:include page="../common/top_nav.jsp"/>
			<jsp:include page="lobby_tab.jsp">
				<jsp:param name="select_tab" value="guest_tab"/>
			</jsp:include>
			<div class="main_body">
				<div class="clear_both landing_input">					
											
					<jsp:include page="../common/action_nav.jsp">
						<jsp:param name="admin_id" value="<%=sAdminId %>"/>
						<jsp:param name="event_id" value="<%=sEventId %>"/>
						<jsp:param name="select_action_nav" value="all_guest_tab"/>
					</jsp:include>
				</div>
				<div  class="clear_both" style="width: 100%;  text-align: center;">
				<div  class="clear_both" id="div_guests_details">
					
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
<script type="text/javascript" src="/web/js/jquery.guestsformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript">
	var varAdminID = '<%=sAdminId%>';
	var varEventID = '<%=sEventId%>';
	$(document).ready(function() {
		
		$("#add_all_guests").fancybox({
			'width'				: '75%',
			'height'			: '85%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe',
			'padding'			: 0,
			'margin'			: 0
		});
		loadActions();
		loadGuests();
	});
	function loadActions()
	{
		$("#lnk_event_id").click(function() 
		{
			$("#frm_lobby_tab").attr("action" , "event_setup.jsp");
			$("#lobby_event_id").val(varEventID);
			$("#lobby_admin_id").val(varAdminID);
			createNewInputElement('from_landing', false, 'frm_lobby_tab', 'hidden');
			
			$("#frm_lobby_tab").submit();
		});
		$("#lnk_guest_id").click(function() 
		{
			
			
			
		});
		$("#lnk_dashboard_id").click(function() {
			
		});
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
	function loadGuests()
	{
		var dataString = '&event_id='+ varEventID + '&admin_id='+ varAdminID;
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
			
			var guestRows = jsonResult.guest_rows;
			var eventGuestRows = jsonResult.event_guest_rows;
			
			
			if(guestRows!=undefined)
			{
				var numOfRows = guestRows.num_of_rows;
				var allTables = guestRows.guests;
				
				$("#div_guests_details").guestformatter({
					varGuestDetails : guestRows,
					varEventGuestDetails : eventGuestRows,
					varDeleteTableURL : '/web/com/gs/event/proc_delete_table.jsp'
				});
				//applyActionEvents(tableDetails);
				
			}
		
		}
	}
	
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>