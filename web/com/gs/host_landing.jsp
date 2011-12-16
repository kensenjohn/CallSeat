<%@ page import="org.json.JSONObject"%>
<%@ page import="com.gs.common.*" %>

<%
String sUserId = "";
Cookie[] cookies = request.getCookies();

if(cookies!=null)
{
	for(int cookieCount = 0; cookieCount < cookies.length; cookieCount++) 
	{ 
		Cookie cookie1 = cookies[cookieCount];
        if (Constants.COOKIE_APP_USERID.equals(cookie1.getName())) {
        	sUserId = cookie1.getValue();
        }
	}
}
%>

<jsp:include page="common/header_top.jsp"/>
<jsp:include page="common/security.jsp"/>
<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<jsp:include page="common/header_bottom.jsp">
	<jsp:param name="u_id" value="<%=sUserId %>" />
	<jsp:param name="from_cookie" value="true" />
</jsp:include>

<body >
	<div class="container rounded-corners">
		<jsp:include page="common/top_nav.jsp"/>
		
		<div class="main_body">
			<div>
						<h2>Seating your Guests, with a phone call.</h2>
			</div>
			<div class="landing_when">
				<h3>When is the Event?</h3><br>
				<form id="frm_event_dt" name="frm_event_dt">
					<input type="text" id="event_date" class="span4" placeholder="Event Date" name="event_date">
				</form>
				<br>
				<button id="event_dt_sbt" name="event_dt_sbt" class="action_button primary large ">Submit</button>
							<form id="call_forward" name="call_forward"  method="POST">
								<input type="hidden" id="hid_event_date" name="hid_event_date" value="">
								<input type="hidden" id="from_landing" name="from_landing" value="">
							</form>
			</div>
		</div>
	</div>
</body>

<!-- <body class="container">
   <div class="page_setup">
		<div class="container rounded-corners">
			<jsp:include page="common/top_nav.jsp"/>
			<div class="main_body">
				<div class="clear_both landing_input">
					<div>
						<span class="l_txt">Seating your Guests, with a phone call.</span>
						<br>
						<br>
						<div class="landing_when">
							<label class="m_b_txt">When is the Event?</label><br>
							<form id="frm_event_dt" name="frm_event_dt">
								<input type="text" id="event_date" class="clearOnClick" name="event_date">
							</form>
							
								<button id="event_dt_sbt" name="event_dt_sbt" class="action_button">Submit</button>
							<form id="call_forward" name="call_forward"  method="POST">
								<input type="hidden" id="hid_event_date" name="hid_event_date" value="">
								<input type="hidden" id="from_landing" name="from_landing" value="">
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	</div>
</body>  -->
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 

<jsp:include page="common/footer_top.jsp"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#event_date").datepick();
		$("#event_dt_sbt").click( 
				function()
				{
					var url = 'proc_host_landing.jsp';
					var formData = $("#frm_event_dt").serialize();
					var method = "POST";
					submitEventDt(url,formData,method)
				});

	});
	function submitEventDt(actionUrl,formData,methodType)
	{
		$.ajax({
			  url: actionUrl ,
			  type: methodType ,
			  dataType: "json",
			  data: formData ,
			  success: getResult
			});
	}
	
	function getResult(jsonResult)
	{
		
		if(!jsonResult.success)
		{
			alert('Please enter a date');
		}
		else 
		{
			$("#hid_event_date").attr( "value", $("#event_date").attr("value") )
			$("#call_forward").attr("action","event/event_setup.jsp");
			$("#from_landing").val("true");
			$("#call_forward").submit();
		}
	}
	</script>
<jsp:include page="common/footer_bottom.jsp"/>