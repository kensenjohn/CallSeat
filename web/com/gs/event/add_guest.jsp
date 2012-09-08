<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
	<body style="height:auto;">
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sGuestId = ParseUtil.checkNull(request.getParameter("guest_id"));
		
		boolean isAllGuest = ParseUtil.sTob(request.getParameter("all_guest_tab"));
		boolean isAllGuestEditGuest = ParseUtil.sTob(request.getParameter("all_guest_tab_edit"));
		boolean isSingleEventGuestEdit= ParseUtil.sTob(request.getParameter("single_event_guest_edit"));
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
%>
		<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
			<div  style="padding-top:5px;">
				<div class="logo span4"><a href="#">CallSeat</a></div>
			</div>
		</div>
		<div class="fnbx_scratch_area">
			<div class="row">
				  <div class="offset1 span9">
<%
					if(isAllGuest && !isAllGuestEditGuest)
					{
%>
						<h2>Create a Guest</h2>
						<!-- <span class="l_txt" style="padding:10px;" >Add Guest to</span> <div id="div_event_list"></div> -->
<%	
					}
					else if(isAllGuest && isAllGuestEditGuest)
					{
%>
						<h2>Edit Guest</h2>
<%	
					}
					else if(isSingleEventGuestEdit)
					{
%>
						<h2>Edit Guest in event <span id="div_event_list"></span></h2>
<%						
					}
					else
					{
%>
						<h2>Add guest to <span id="div_event_list"></span></h2>
<%
					}
%>	
				  </div>
			</div>
			<div class="row">
				<div class="offset1 span12">						
		
				<div class="row">
					<div class="span6">
						&nbsp;
					</div>
				</div>
				<form id="frm_add_guest" >
					<div class="row">
						<div class="span6">
							First Name :
						</div>
					</div>
					<div class="row">
						<div class="span2" >
							<input type="text" id="first_name" name="first_name"/>
						</div>
					</div>
					<div class="row">
						<div class="span6">
							Last Name :
						</div>
					</div>
					<div class="row">
						<div class="span2" >
							<input type="text" id="last_name" name="last_name"/>
						</div>
					</div>
					<div class="row">
						<div class="span6">
							Cell Number :
						</div>
					</div>
					<div class="row">
						<div class="span2" >
							<input type="text" id="cell_num" name="cell_num"/>
						</div>
					</div>
					<div class="row">
						<div class="span6">
							Home Number :
						</div>
					</div>
					<div class="row">
						<div class="span2" >
							<input type="text" id="home_num" name="home_num"/>
						</div>
					</div>
					<div class="row">
						<div class="span6">
							Email :
						</div>
					</div>
					<div class="row">
						<div class="span2" >
							<input type="text" id="email_addr" name="email_addr"/>
						</div>
					</div>
<%
					if(!isAllGuest)
					{
%>
						<div class="row">
							<div class="span6">
								Invited seats :
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="invited_num_of_seats" name="invited_num_of_seats"/>
							</div>
						</div>
						<div class="row">
							<div class="span6">
								Guest's RSVP :
							</div>
						</div>
						<div class="row">
							<div class="span2" >
								<input type="text" id="rsvp_num_of_seats" name="rsvp_num_of_seats"/>
							</div>
						</div>
<%
					}
%>

<%
					if(isAllGuest && !isAllGuestEditGuest)
					{
%>
							<div class="row">								
								<div class="span2" >
									<input type="button" id="add_guest" name="add_guest" type="button" class="btn" value="Create Guest">
								</div>
							</div>	
								<input type="hidden" id="invited_num_of_seats" name="invited_num_of_seats" value="0"/>
						        <input type="hidden" id="rsvp_num_of_seats" name="rsvp_num_of_seats" value="0"/>
						        <input type="hidden" id="is_guest_create" name="is_guest_create" value="true"/>
<%
					}
					else if(isAllGuest && isAllGuestEditGuest)
					{
%>
							<div class="row">								
								<div class="span2" >
									<input type="button" id="add_guest" name="add_guest" type="button" class="btn" value="Save Changes">
								</div>
							</div>	
						 <input type="hidden" id="all_guest_edit" name="all_guest_edit" value="true"/>
				          <input type="hidden" id="guest_id" name="guest_id" value=""/>
				          <input type="hidden" id="guest_userinfo_id" name="guest_userinfo_id" value=""/>
<%
					}
					else if (isSingleEventGuestEdit)
					{
%>
							<div class="row">								
								<div class="span2" >
									<input type="button" id="add_guest" name="add_guest" type="button" class="btn" value="Save Changes">
								</div>
							</div>	
				        <input type="hidden" id="is_single_guest_event_edit" name="is_single_guest_event_edit" value="true"/>
						<input type="hidden" id="admin_id" name="guest_id"  value="<%=sGuestId%>"/>
				          <input type="hidden" id="guest_userinfo_id" name="guest_userinfo_id" value=""/>
<%						
					}
					else
					{
%>
							<div class="row">								
								<div class="span2" >
									<input type="button" id="add_guest" name="add_guest" type="button" class="btn" value="Add Guest">
								</div>
							</div>	

						        <input type="hidden" id="is_guest_add_event" name="is_guest_add_event" value="true"/>
<%
					}
%>
							<div class="row">									
								<div class="span9" >
									<span id="err_mssg"></span>
								</div>
							</div>
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
				</form>
				</div>
				<form id="frm_event_assign" id="frm_event_assign">
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>					
					<input type="hidden" id="guest_id" name="guest_id" value=""/>										
					<input type="hidden" id="guest_first_name" name="guest_first_name" value=""/>										
					<input type="hidden" id="guest_last_name" name="guest_last_name" value=""/>
				</form>
							
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var varAdminId = '<%=sAdminId%>';
		var varEventId = '<%=sEventId%>';
		var varGuestId = '<%=sGuestId%>';			
		var varIsAllGuest = <%=isAllGuest%>;
		var varIsAllGuestEdit = <%=isAllGuestEditGuest%>;
		var varIsSingleEventGuestEdit = <%=isSingleEventGuestEdit%>;
		$(document).ready(function() 
		{	if(varIsAllGuest && !varIsAllGuestEdit)
			{
				$("#add_guest").click(addGuest);
				//loadEvents(); //load all events only if there are guests.
			}
			else if(varIsAllGuest && varIsAllGuestEdit)
			{
				loadGuest();
				$("#add_guest").click(saveGuestInfo);
			}
			else if(varIsSingleEventGuestEdit)
			{
				loadSingleEventGuest();
				getEventName();
				$("#add_guest").click(saveGuestInfo);
			}
			else			
			{
				$("#add_guest").click(addGuest);
				getEventName();
			}
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
		
		//This for loding a ingle guest a single event relationhip
		function loadSingleEventGuest()
		{
			var dataString = '&event_id='+ varEventId + '&guest_id='+ varGuestId + '&admin_id='+ varAdminId + '&single_event_guest=true';
			var actionUrl = "proc_load_guests.jsp";
			var methodType = "GET";
			
			makeAjaxCall(actionUrl,dataString,methodType, getSingleGuestData);
		}
		
		//this is for loading only a single guest data. its relationship data with an event is not required.
		function loadGuest()
		{
			var dataString = '&guest_id='+ varGuestId + '&admin_id='+ varAdminId + '&single_guest=true';
			var actionUrl = "proc_load_guests.jsp";
			var methodType = "GET";
			
			makeAjaxCall(actionUrl,dataString,methodType, getSingleGuestData);
		}
		function addGuest()
		{	//alert('add guest called');
			var dataString = $("#frm_add_guest").serialize();
			var actionUrl = "proc_add_guest.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';
			if(!varIsAllGuest)
			{
				dataString = dataString + '&dd_event_list='+varEventId;
			}
			makeAjaxCall(actionUrl,dataString,methodType,getResult);
		}
		
		function saveGuestInfo()
		{
			//alert('add guest called');
			var dataString = $("#frm_add_guest").serialize();
			var actionUrl = "proc_edit_guest.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';			
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
						displayMessages( varArrErrorMssg , true );
					}
				}
				else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
				{
					var varIsPayloadExist = varResponseObj.is_payload_exist;
					
					if(varIsPayloadExist == true)
					{
						var varPayload = varResponseObj.payload;
						
						var eventDetails = varPayload.event_detail;
						if(eventDetails!=undefined)
						{
							var varEventDD = generateEventName(eventDetails);
							
							$("#div_event_list").append(varEventDD);
						}						
					}
				}
			}
			
		}
		
		function getSingleGuestData(jsonResult)
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
						displayMessages( varArrErrorMssg , true );
					}
				}
				else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
				{
					var varIsPayloadExist = varResponseObj.is_payload_exist;
					
					if(varIsPayloadExist == true)
					{
						var varPayload = varResponseObj.payload;
						
						var varSingleGuest = varPayload.guest_data;
						if(varSingleGuest!=undefined)
						{
							populateGuestData(varSingleGuest);
						}				
						var varIsGuestEventDataPresent = varPayload.is_guest_event_data_present;
						if(varIsGuestEventDataPresent!=undefined && varIsGuestEventDataPresent == true)
						{
							var varGuestEventData = varPayload.guest_event_data;
							populateGuestEventData(varGuestEventData);
						}
					}
				}
			}
		}
		function populateGuestEventData(varGuestEventData)
		{
			if(varGuestEventData!=undefined)
			{
				$('#invited_num_of_seats').val(varGuestEventData.total_seats);
				$('#rsvp_num_of_seats').val(varGuestEventData.rsvp_seats);
			}
		}
		function populateGuestData(varSingleGuest)
		{
			if(varSingleGuest!=undefined)
			{
				var varGuestUserInfo = varSingleGuest.user_info;
				if(varGuestUserInfo!=undefined)
				{
					$('#first_name').val( varGuestUserInfo.first_name );
					$('#last_name').val( varGuestUserInfo.last_name );

					$('#cell_num').val( varGuestUserInfo.cell_phone );
					$('#home_num').val( varGuestUserInfo.phone_num );
					
					$('#email_addr').val( varGuestUserInfo.email );
					
					$('#guest_userinfo_id').val(varSingleGuest.user_info_id);
					$('#guest_id').val(varSingleGuest.guest_id);
				}
			}
		}
		
		function createEventList(jsonResult)
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
						displayMessages( varArrErrorMssg , true );
					}
				}
				else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
				{
					var varIsPayloadExist = varResponseObj.is_payload_exist;
					
					if(varIsPayloadExist == true)
					{
						var varPayload = varResponseObj.payload;
						
						var eventDetails = varPayload.event_detail;
						if(eventDetails!=undefined)
						{
							var varEventDD = generateEventDropDown(eventDetails);
							
							$("#div_event_list").append(varEventDD);
						}						
					}
				}
			}
			/*if(!jsonResult.success)
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
			}*/
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
						displayMessages( varArrErrorMssg , true );
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
						if(varIsAllGuest && !varIsAllGuestEdit)
						{
							createAssignmentButton(varGuestId);
						}
						
						
						
						
						parent.loadGuests();
						
						if(!varIsAllGuest)
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
		function displayMessages(varArrMessages, isError)
		{
			if(varArrMessages!=undefined)
			{
				for(var i = 0; i<varArrMessages.length; i++)
				{
					if(isError)
					{
						$("#err_mssg").addClass("error_mssg");
					}
					$("#err_mssg").text(varArrMessages[i].text);
					if(varArrMessages[i].txt_loc_id == 'err_mssg')
					{
						$("#err_mssg").text(varArrMessages[i].text);
					}
					else
					{
					}
				}
			}
		}
	</script>
</html>