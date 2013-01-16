<%@ page import="org.json.JSONObject"%>
<%@ page import="com.gs.common.*" %>

<%
String sTmpUserId = "";
//dreamsopen@__
Cookie[] cookies = request.getCookies();

if(cookies!=null)
{
	for(int cookieCount = 0; cookieCount < cookies.length; cookieCount++) 
	{ 
		Cookie cookie1 = cookies[cookieCount];
        if (Constants.COOKIE_APP_USERID.equals(cookie1.getName())) {
        	sTmpUserId = cookie1.getValue();
        	session.setAttribute("u_id",sTmpUserId);
        	session.setAttribute("from_cookie","true");
        }
	}
}
%>

<jsp:include page="common/header_top.jsp">
	<jsp:param name="page_title" value="Host Landing"/>	
</jsp:include>
<%@include file="common/security.jsp"%>
<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<jsp:include page="common/header_bottom.jsp"/>

<body >
		<jsp:include page="common/top_nav.jsp">
			<jsp:param name="referrer_source" value="host_landing.jsp"/>	
		</jsp:include>
		<div class="welcome_img">
			<div class="blank_scratch_area">
                <div class="row">
                    <div class="offset3 span9">
                        <h1 style="color:#525451">Seat your guests with a phone call</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="offset3 span9">
                        <h3 style="color:#525451">Create custom phone numbers for seating and RSVP</h3>
                    </div>
                </div>
					
                <div class="row">

                    <div class="offset3 span3">
                        <form id="frm_event_dt" name="frm_event_dt">
                            <input type="text" id="tmp_email" class="ispn3 inp-large" placeholder="Email" name="tmp_email">
                            <input type="text" id="event_date" class="ispn3 inp-large" placeholder="Select date of wedding" name="event_date" readonly>
                            <input type="button" class="btn btn-large btn-blue" id="event_dt_sbt" value="Create Phone Number"/>
                        </form>
                    </div>
                    <div class="span3">
                        <form id="call_forward" name="call_forward"  method="POST">
                                        <input type="hidden" id="hid_tmp_email" name="hid_tmp_email" value="">
                                        <input type="hidden" id="hid_event_date" name="hid_event_date" value="">
                                        <input type="hidden" id="from_landing" name="from_landing" value="">
                                        <input type="hidden" id="lobby_event_id" name="lobby_event_id" value="">
                                        <input type="hidden" id="lobby_admin_id" name="lobby_admin_id"
                                            value="<%=isSignedIn ? sAdminIdSecure:""%>">

                                    </form>
                    </div>
                </div>
                <div class="row">
                    <div class="offset4 span3">
                        <div id="div_goto_lobby" style="<%=!isSignedIn?"display:none;":""%>">
                            <h3><a id="link_goto_lobby" href="<%=isSignedIn ? "event/host_dashboard.jsp?host_lobby_admin_id="+sAdminIdSecure:""%>">Goto lobby</a></h3>
                        </div>
                    </div>
                </div>
		    </div>
	    </div>
        <section class="step_instructions" style="display: block; opacity: 1; -webkit-transition: all 0s cubic-bezier(0.25, 0.1, 0.25, 1); min-height: 144px; padding-top:10px;">
            <div class="blank_scratch_area">
                <div class="row" style="margin: auto;">
                    <div class="offset1 span3" style="text-align: center; background-color:#EDEEF1; border-radius: 15px; min-height: 134px; ">
                        <h1 style="padding-top:25px;">Step 1</h1>
                        <h4  style="padding:5px;">Create a Seating Plan</h4>
                        <span class="fld_txt_small">Add guests and tables.</span>
                    </div>
                    <div class="span3" style="text-align: center;  background-color:#EDEEF1; border-radius: 15px; min-height: 134px; ">
                        <h1 style="padding-top:25px;">Step 2</h1>
                        <h4 style="padding:5px;">Personalize telephone numbers</h4>
                        <span class="fld_txt_small">Direct numbers with no extensions.</span>
                    </div>
                    <div class="span3" style="text-align: center;  background-color:#EDEEF1; border-radius: 15px; min-height: 134px; ">
                        <h1 style="padding-top:25px;">Step 3</h1>
                        <h4 style="padding:5px;">RSVP and seating by phone</h4>
                        <span class="fld_txt_small">Guests call to respond and for details</span>
                    </div>
                </div>
            </div>
        </section>
	<form action="event/host_dashboard.jsp" method="POST" id="frm_lobby_admin">
		<input type="hidden" id="host_lobby_admin_id" name="host_lobby_admin_id" value="<%=isSignedIn ? sAdminIdSecure: "" %>"/>
	</form>
</body>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript" src="/web/js/credential.js"></script>

<jsp:include page="common/footer_top.jsp"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#event_date").datepick();
		$("#event_dt_sbt").click( 
				function()
				{
					callSubmitEvent();
				});
		
		$("#event_dt_sbt").keypress( function(event){
			if ( event.which == 13 ) {
				callSubmitEvent();
			   }
		} );

	});
	function callSubmitEvent()
	{
		var url = 'proc_host_landing.jsp';
		var formData = $("#frm_event_dt").serialize();
		var method = "POST";
		submitEventDt(url,formData,method)
		
	}
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
			$("#hid_event_date").attr( "value", $("#event_date").attr("value") );
			$("#hid_tmp_email").attr( "value", $("#tmp_email").attr("value") );
			$("#call_forward").attr("action","event/event_setup.jsp");
			$("#from_landing").val("true");
			$("#call_forward").submit();
		}
	}
	</script>
<jsp:include page="common/footer_bottom.jsp"/>