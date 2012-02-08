<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		
		boolean isAllGuestAdd = ParseUtil.sTob(request.getParameter("all_guest_tab"));
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
%>

		<div class="container-filler rounded-corners">
			<div style="padding:20px">
<%
					if(isAllGuestAdd)
					{
%>
						<h2 class="txt txt_center">Create a Guest</h2>
						<!-- <span class="l_txt" style="padding:10px;" >Add Guest to</span> <div id="div_event_list"></div> -->
<%	
					}
					else
					{
%>
						<h2 class="txt txt_center">Add guest to <span id="div_event_list"></span></h2>
<%
					}
%>
				<div class="row">
					<div class="span10">
						<form id="frm_add_guest" >
							<fieldset>
								<div class="clearfix-tight required" id="first_name_div">
									<label for="table_name" id="first_name_label">First Name :</label>
									<div class="input">
										<input type="text" id="first_name" name="first_name"/>
										<span class="help-inline" id="first_name_msg"></span>
									</div>
								</div>
								<div class="clearfix-tight required"  id="last_name_div">
									<label for="table_name"  id="last_name_label">Last Name :</label>
									<div class="input">
										<input type="text" id="last_name" name="last_name"/>
										<span class="help-inline" id="last_name_msg"></span>
									</div>
								</div>
								<div class="clearfix-tight required" id="cell_num_div">
									<label for="table_name" id="cell_num_label">Cell Number :</label>
									<div class="input">
										<input type="text" id="cell_num" name="cell_num"/>										
										<span class="help-inline" id="cell_num_msg"></span>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="table_name">Home Number :</label>
									<div class="input">
										<input type="text" id="home_num" name="home_num"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="table_name">Email :</label>
									<div class="input">
										<input type="text" id="email_addr" name="email_addr"/>
									</div>
								</div>
<%
					if(!isAllGuestAdd)
					{
%>								
								<div class="clearfix-tight" id="invited_num_of_seats_div">
									<label for="table_name" id="invited_num_of_seats_label" >Invited seats :</label>
									<div class="input">
										<input type="text" id="invited_num_of_seats" name="invited_num_of_seats"/>
										<span class="help-inline" id="invited_num_of_seats_msg">(Number of seats.)</span>
									</div>
								</div>
								<div class="clearfix-tight" id="rsvp_num_of_seats_div">
									<label for="table_name" id="rsvp_num_of_seats_label" >Guest's RSVP :</label>
									<div class="input">
										<input type="text" id="rsvp_num_of_seats" name="rsvp_num_of_seats"/>
										<span class="help-inline" id="rsvp_num_of_seats_msg">(RSVP from the guest.)</span>
									</div>									
								</div>
<%
					}
%>

<%
//isAllGuestAdd = true;
					if(isAllGuestAdd)
					{
%>
								<div class="actions">									
						            <button id="add_guest" name="add_guest" type="button" class="action_button primary small">Create Guest</button>
						            <br>
						            <span id="err_mssg"></span>
						        </div>
						        <input type="hidden" id="invited_num_of_seats" name="invited_num_of_seats" value="0"/>
						        <input type="hidden" id="rsvp_num_of_seats" name="rsvp_num_of_seats" value="0"/>
						        <input type="hidden" id="is_guest_create" name="is_guest_create" value="true"/>
<%
					}
					else
					{
%>
								<div class="actions">									
						            <button id="add_guest" name="add_guest" type="button" class="action_button primary small">Add Guest</button>
						            <br>
						            <span id="err_mssg"></span>
						        </div>
						        
						        <input type="hidden" id="is_guest_add_event" name="is_guest_add_event" value="true"/>
<%
					}
%>
							</fieldset>
							<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
							<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
						</form>
					</div>
				</div>
				<form id="frm_event_assign" id="frm_event_assign">
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>					
					<input type="hidden" id="guest_id" name="guest_id" value=""/>										
					<input type="hidden" id="guest_first_name" name="guest_first_name" value=""/>										
					<input type="hidden" id="guest_last_name" name="guest_last_name" value=""/>
				</form>
					
				<!-- <form id="frm_add_guest" >
				<br/>
				<div>
				
				<div>
				<span>First Name :</span> <input type="text" id="table_name" name="first_name"/> &nbsp;&nbsp;
				<span>Last Name :</span> <input type="text" id="last_name" name="last_name"/><br/>
				<span>Cell Number :</span> <input type="text" id="cell_num" name="cell_num"/><br/>
				<span>Home Number :</span> <input type="text" id="home_num" name="home_num"/><br/>			
				<span>Email :</span> <input type="text" id="email_addr" name="email_addr"/><br/>
				<span>Invited to :</span> <input type="text" id="invited_num_of_seats" name="invited_num_of_seats"/><br/>
				<span>RSVP to :</span> <input type="text" id="rsvp_num_of_seats" name="rsvp_num_of_seats"/><br/>
				
				<a class="action_button" id="add_guest" name="add_guest">Add Guest</a></br>
<%
				if(isAllGuestAdd)
				{
%>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
<%
				}
%>

				<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
				</div>
				
				</div>	
				</form>			 -->
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varEventId = '<%=sEventId%>'
		var varIsAllGuestAdd = <%=isAllGuestAdd%>;
		$(document).ready(function() 
		{	if(varIsAllGuestAdd)
			{
				loadEvents(); //load all events only if there are guests.
			}
			else
			{
				getEventName();
			}
			
			$("#add_guest").click(addGuest);
		});
		
		function getEventName()
		{
			var actionUrl = "proc_load_events.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId+"&event_id="+varEventId+"&single_event=true";
			//alert(dataString);
			makeAjaxCall(actionUrl,dataString,methodType,createEventName);
		}
		function loadEvents()
		{
			var actionUrl = "proc_load_events.jsp";
			var methodType = "GET";
			var dataString = "&admin_id="+varAdminId;
			//alert(dataString);
			makeAjaxCall(actionUrl,dataString,methodType,createEventList);
			
		}
		function addGuest()
		{	//alert('add guest called');
			var dataString = $("#frm_add_guest").serialize();
			var actionUrl = "proc_add_guest.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';
			if(!varIsAllGuestAdd)
			{
				dataString = dataString + '&dd_event_list='+varEventId;
			}
			makeAjaxCall(actionUrl,dataString,methodType,getResult);
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
		
		function createEventName(jsonResult)
		{
			if(!jsonResult.success)
			{
				
			}
			else
			{
				var eventDetails = jsonResult.event_detail;
				if(eventDetails!=undefined)
				{
					var varEventDD = generateEventName(eventDetails);
					
					$("#div_event_list").append(varEventDD);
				}
			}
		}
		
		function createEventList(jsonResult)
		{
			if(!jsonResult.success)
			{
				
			}
			else
			{
				var eventDetails = jsonResult.event_detail;
				if(eventDetails!=undefined)
				{
					
					var varEventDD = generateEventDropDown(eventDetails);
					
					$("#div_event_list").append(varEventDD);
					
				}
			}
		}
		
		function generateEventName(eventDetails)
		{
			var varNumOfEvents = eventDetails.num_of_rows;
			var varEventList = eventDetails.events;
			
			var varEventDD = '';
			for(i=0; i<varNumOfEvents ; i++ )
			{
				varEventDD = varEventList[i].event_name;
			}
			return varEventDD;
		}
		
		function generateEventDropDown(eventDetails)
		{
			var varNumOfEvents = eventDetails.num_of_rows;
			var varEventList = eventDetails.events;
			
			var varEventDD = '<select id="dd_event_list" name="dd_event_list"> <option id="all" value="all">Add to list</option>';
			for(i=0; i<varNumOfEvents ; i++ )
			{
				varEventDD = varEventDD + '<option id="'+varEventList[i].event_id+'"  value="'+varEventList[i].event_id+'">' 
					+ varEventList[i].event_name + ' ' + varEventList[i].human_event_date + '</option>'
			}
			varEventDD = varEventDD + '</select>';
			
			return varEventDD;
		}
		
				
		function getResult(jsonResult)
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
						//processTableGuest( jsonResponseObj );
						var varGuestId = jsonResponseObj.guest_id;
						createAssignmentButton(varGuestId);
						
						
						parent.loadGuests();
						
						if(!varIsAllGuestAdd)
						{
							parent.$.fancybox.close();
						}
						//parent.$.fancybox.close();
					}
				}
			}
		}
		
		function createAssignmentButton(varGuestId)
		{
			$("#add_guest").unbind();
			
			$("#add_guest").removeClass("primary");
			$("#add_guest").removeClass("small");
			$("#add_guest").addClass("success");
			$("#add_guest").addClass("large");
			
			var varFirstName = $("#first_name").val();
			var varLastName = $("#last_name").val();
			
			$("#add_guest").text("Assign " + varFirstName + " " + varLastName + " to an Event");
			
			$("#guest_first_name").val(varFirstName);
			$("#guest_last_name").val(varLastName);
			$("#guest_id").val(varGuestId);
			
			$("#add_guest").click(function(){
				submitPassThruButton();
			});
			//alert('berfore load');
		}
		function submitPassThruButton()
		{
			//alert('submit passthru load' + varGuestId);
			$("#frm_event_assign").attr("action","assign_guests_events.jsp");
			$("#frm_event_assign").attr("method","POST");
			$("#frm_event_assign").submit();
		}
		function displayMessages(varArrMessages)
		{
			if(varArrMessages!=undefined)
			{
				for(var i = 0; i<varArrMessages.length; i++)
				{
					//alert( varArrMessages[i].text +  '-' + varArrMessages[i].txt_loc_id );
					if(varArrMessages[i].txt_loc_id == 'err_mssg')
					{
						$("#err_mssg").text(varArrMessages[i].text);
					}
					else
					{
						
						$("#"+varArrMessages[i].txt_loc_id+"_msg").text(varArrMessages[i].text);
						$("#"+varArrMessages[i].txt_loc_id+"_div").addClass('error');
						$("#"+varArrMessages[i].txt_loc_id).addClass('error');
					}
				}
			}
		}
	</script>
</html>