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
	String sAdminId = ParseUtil.checkNull(request.getParameter("host_lobby_admin_id"));
	
	String sGateAdminId = sAdminId;

%>
<%@include file="../common/gatekeeper.jsp"%>
<%
    if(sAdminId==null || "".equalsIgnoreCase(sAdminId)){
        response.sendRedirect("/web/com/gs/host_landing.jsp");
    }
%>

<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
	<jsp:include page="../common/top_nav.jsp">
		<jsp:param name="referrer_source" value="host_dashboard.jsp"/>
	</jsp:include>
	<div class="scratch_area" id="div_host_dash"> 
				<jsp:include page="lobby_tab.jsp">
					<jsp:param name="select_tab" value="guest_tab"/>
					<jsp:param name="lobby_header" value="Seating Plans"/>
					<jsp:param name="lobby_sec_header" value=""/>
				</jsp:include>
                <jsp:include page="../common/action_nav.jsp">
                    <jsp:param name="admin_id" value="<%=sAdminId %>"/>
                    <jsp:param name="event_id" value="<%=sEventId %>"/>
                    <jsp:param name="select_action_nav" value="dashboard_tab"/>
                </jsp:include>
				<div class="row">
					<div  class="span10">
						&nbsp;
					</div>
				</div>
				<div class="row">
					<div  class="span10">
						&nbsp;
					</div>
				</div>
				<div class="row">
					<div  class="span12" id="div_dashboard_details">
						
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
<script type="text/javascript" src="/web/js/credential.js"></script>
<script type="text/javascript">
	var varAdminID = '<%=sAdminId%>';
	var varEventID = '<%=sEventId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	$(document).ready(function() {
		setCredentialEventId(varEventID);
		if(!varIsSignedIn) {
			setTopNavLogin(varAdminID,varEventID,'host_dashboard.jsp');
			setTopNavSingup(varAdminID,varEventID,'host_dashboard.jsp');
		}
		loadActions();
		loadDashboard( varEventID , varAdminID );
	});
	function loadActions() {
		setNewEventClick();
		setAllGuestButtonClick();
		setLobbyButtonClick();
	}
	
	function loadDashboard(varTmpEventId,varTmpAdminId) {
		var dataString = '&event_id='+ varTmpEventId + '&admin_id='+ varTmpAdminId;
		var actionUrl = "proc_load_dashboard.jsp";
		var methodType = "POST";
		getDataAjax(actionUrl,dataString,methodType, getDashboardResult);
	}
	function getDashboardResult(jsonResult) {
		if(jsonResult!=undefined) {
			var varResponseObj = jsonResult.response;
			if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
				var varIsMessageExist = varResponseObj.is_message_exist;
				if(varIsMessageExist == true) {
					var jsonResponseMessage = varResponseObj.messages;
					var varArrErrorMssg = jsonResponseMessage.error_mssg
					displayMessages( varArrErrorMssg );
				}
			} else  if( jsonResult.status == 'ok' && varResponseObj !=undefined) {
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				if(varIsPayloadExist == true) {
					var jsonResponseObj = varResponseObj.payload;
					
					var eventList = jsonResponseObj.event_list;
					
					$("#div_dashboard_details").dashboard({
						varEventList : eventList
					});
				}					
			}
		}
	}
	function getDataAjax(actionUrl,dataString,methodType, callBackMethod) {
		$.ajax({
			  url: actionUrl ,
			  type: methodType ,
			  dataType: "json",
			  data: dataString ,
			  success: callBackMethod,
			  error:function(a,b,c) {  alert(a.responseText + ' = ' + b + " = " + c);  }
			});
	}

	//TODO: load the lobby after login with current admin's user.
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>