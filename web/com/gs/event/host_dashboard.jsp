<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>


<jsp:include page="../common/header_top.jsp"/>

<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%@include file="/web/com/gs/common/gatekeeper.jsp"%>

<%
	Logger jspLogging = LoggerFactory.getLogger("JspLogging");
	String sEventId = ParseUtil.checkNull(request.getParameter("lobby_event_id"));
	String sAdminId = ParseUtil.checkNull(request.getParameter("lobby_admin_id"));
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
   <div class="page_setup">
		<div class="container rounded-corners">
			<div style="margin:5px;">
			<jsp:include page="../common/top_nav.jsp"/>
				<jsp:include page="lobby_tab.jsp">
					<jsp:param name="select_tab" value="guest_tab"/>
					<jsp:param name="lobby_header" value="My Lobby"/>
					<jsp:param name="lobby_sec_header" value=""/>
				</jsp:include>
				<div class="main_body">
					<div class="clear_both">					
												
						<jsp:include page="../common/action_nav.jsp">
							<jsp:param name="admin_id" value="<%=sAdminId %>"/>
							<jsp:param name="event_id" value="<%=sEventId %>"/>
							<jsp:param name="select_action_nav" value="dashboard_tab"/> 
						</jsp:include>
					</div>
					<div  class="clear_both" style="width: 100%;  text-align: center;">
					<div  class="clear_both" id="div_dashboard_details">
						
					</div>
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
<script type="text/javascript" src="/web/js/jquery.dashboard.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript">
	var varAdminID = '<%=sAdminId%>';
	var varEventID = '<%=sEventId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	$(document).ready(function() {
		loadActions();
		loadDashboard();
		if(!varIsSignedIn)
		{
			$("#login_name_display").removeAttr('href');
			$("#login_name_display").attr('href','/web/com/gs/common/credential.jsp?admin_id='+varAdminID+'&event_id='+varEventID);
		}
	});
	function loadActions()
	{
		$("#lnk_guest_id").click(function() 
		{
			//window.location = 'guest_setup.jsp?event_id='+varEventID+'&admin_id='+varAdminID;
			
			$("#frm_lobby_tab").attr("action" , "guest_setup.jsp");
			$("#lobby_event_id").val(varEventID);
			$("#lobby_admin_id").val(varAdminID);
			
			$("#frm_lobby_tab").submit();
		});

		setNewEventClick();
		setAllGuestButtonClick();
	}
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
	function loadDashboard()
	{
		var dataString = '&event_id='+ varEventID + '&admin_id='+ varAdminID;
		var actionUrl = "proc_load_dashboard.jsp";
		var methodType = "POST";
		
		getDataAjax(actionUrl,dataString,methodType, getDashboardResult);
	}
	function getDashboardResult(jsonResult)
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
					
					var eventList = jsonResponseObj.event_list;
					
					$("#div_dashboard_details").dashboard({
						varEventList : eventList
					});
					
				}					
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
	
	function credentialSuccess(jsonResponse,varSource)
	{
		$("#login_name_display").text(jsonResponse.first_name);
		$("#login_name_display").addClass("bold_text");
		resetAdminId(jsonResponse.user_id);
		/*varIsSignedIn = true;
		phoneNumTab();
		
		resetAdminId(jsonResponse.user_id);*/
	}
	function resetAdminId(tmpAdminId)
	{
		varAdminID = tmpAdminId;
		setAllGuestButtonClick();
	}
	//TODO: load the lobby after login with current admin's user.
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>