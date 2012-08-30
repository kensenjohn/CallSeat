<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.*" %>
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
			
			if(eventBean!=null && eventBean.getEventId()!=null && !"".equalsIgnoreCase(eventBean.getEventId()))
			{
				TelNumberManager telNumberManager = new TelNumberManager();
				telNumberManager.setEventDemoNumber(eventBean.getEventId(),adminBean.getAdminId());
			}
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
	

	String sGateAdminId = sAdminId;
%>
	<%@include file="../common/gatekeeper.jsp"%>
<%
	
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
	<jsp:include page="../common/top_nav.jsp">
		<jsp:param name="referrer_source" value="event_setup.jsp"/>	
	</jsp:include>
	<div class="scratch_area" >
		<jsp:include page="lobby_tab.jsp">
			<jsp:param name="select_tab" value="event_tab"/>
			<jsp:param name="lobby_header" value="<%=eventName %>"/>
			<jsp:param name="lobby_sec_header" value="<%=eventDate%>"/>
		</jsp:include>
		<div class="row">
			<div class="span3">
				&nbsp;
			</div>
		</div>
		<div class = "row">
			<div class = "span10">
				<jsp:include page="../common/tab_view_nav.jsp">
					<jsp:param name="admin_id" value="<%=sAdminId %>"/>
				</jsp:include>	
			</div>
		</div>
		<div class = "row" style="margin-left:-18px;">
			<div class = "tab_view_span12 white_bkg light_border"  >
					<div class="row">
						<div class = "offset1 span11">
							<jsp:include page="../common/action_nav.jsp">
								<jsp:param name="admin_id" value="<%=sAdminId %>"/>
								<jsp:param name="event_id" value="<%=sEventId %>"/>
								<jsp:param name="select_action_nav" value="table_tab"/>
								<jsp:param name="logged_in" value="<%=isSignedIn %>"/>
								<jsp:param name="referrer_source" value="event_setup.jsp"/>						
							</jsp:include> 
						</div>
					</div>
					<div class="row">
						<div class="span1">
							&nbsp;
						</div>
					</div>
					<div  class="clear_both" id="tab_view_area">
					
					</div>
					<div  class="clear_both" id="div_table_details">
						
					</div>
					<div  class="clear_both" id="div_guests_details">
						
					</div>
					
					<div  class="row" id="div_event_summary"  style="display:none;" >
						<div class="offset_0_5 span7">
						
							<div class="row">
								<div class="span5">
									<h4>Event </h4>
								</div>
							</div>
							<div  class="row">
								<div class="offset1 span6">
									<form id="frm_event_update" >
										<div class="row">
											<div class="span2" >
												<span class="fld_name">Name :  </span>
											</div>
										</div>
										<div class="row">											
											<div class="span4" >
												<input type="text"class="ispn4" id="e_summ_event_name" name="e_summ_event_name"/>
											</div>
											<div class="span2"  style="text-align : left; padding-top:5px;" >
												<span id="e_summ_event_name_mssg"></span>
											</div>
										</div>
										<div class="row">											
											<div class="span3" >
												&nbsp;
											</div>
										</div>
										<div class="row">
											<div class="span5" >
												<span class="fld_name">When : </span>
											</div>
										</div>
										<div class="row">											
											<div class="span4" >
												<input type="text" class="ispn4" id="e_summ_event_date" name="e_summ_event_date"/>
											</div>
											<div class="span2" style="text-align : left;  padding-top:5px;" >
												<span id="e_summ_event_date_mssg"></span>
											</div>
										</div>
										<div class="row"style="text-align:right">											
											<div class="span4" >
										<%
												if(isNewEventClicked)
												{
		%>
													<input type="button" class="btn ispn2" id="create_event" name="create_event" value="Create New Event"/>									
		<%											
												}
												else
												{
		%>
													<input type="button" class="btn ispn2" id="save_event" name="save_event" value="Save Changes"/>
		<%
												}
										%>
											</div>
											<div class="span2" style="text-align : left; padding-top:5px;" >
												<span id="act_mssg" ></span>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="row">
								<div class="span1">
									&nbsp;
								</div>
							</div>
							<div class="row">
								<div class="span1">
									&nbsp;
								</div>
							</div>
							<div class="row">
								<div class="span5">
									<h4>Phone Numbers</h4>
								</div>
							</div>
							<div class="row">
								<div class="offset1 span4">
									<span class="fld_name"> Seating : </span>
									<span class="fld_txt" id="e_summ_seating_telnum1" >(267) 432 5420 ext: 5432</span>
								</div>
							</div>
							<div class="row">
								<div class="offset1 span4">
									<span class="fld_name"> RSVP : </span>
									<span class="fld_txt" id="e_summ_rsvp_telnum1" >(214) 432 5420 ext: 5432</span>
								</div>
							</div>							
							<div class="row">
								<div class="span1">
									&nbsp;
								</div>
							</div>
							<div class="row">
								<div class="span5">
									<h4>Seating Plan</h4>
								</div>
							</div>
							<div class="row">
							<div class="offset1 span10">
							 <table>
								<tr>
									<td ><span class="fld_name">Tables created : </span></td>
									<td><span id="e_summ_total_table" class="fld_txt" > 0 </span></td>
								</tr>
								<tr>
									<td  ><span class="fld_name">Seats created : </span></td>
									<td><span id="e_summ_total_seats" class="fld_txt" > 0 </span></td>
								</tr>
								<tr>
									<td  ><span class="fld_name">Seats assigned : </span></td>
									<td><span id="e_summ_assigned_seats" class="fld_txt" > 0 </span></td>
								</tr>
								<tr>
									<td ><span class="fld_name">Guest parties : </span></td>
									<td><span id="e_summ_guest_parties" class="fld_txt" > 0 </span></td>
								</tr>
								<tr>
									<td ><span class="fld_name">Total invited : </span></td>
									<td><span id="e_summ_total_invited" class="fld_txt"> 0 </span></td>
								</tr>
								<tr>
									<td ><span class="fld_name">Total rsvp : </span></td>
									<td><span id="e_summ_total_rsvp" class="fld_txt"> 0 </span></td>
								</tr>
							</table>
							</div>
						</div>
							<div class="row">
								<div class="span1">
									&nbsp;
								</div>
							</div>
							<div class="row">
								<div class="span1">
									&nbsp;
								</div>
							</div>
				
					</div>
				</div>
				
				<!-- The phone number summary -->
				<div  class="row" id="div_phone_numbers"  style="display:none;">
					<div class="offset_0_5 span9">
							<div class="row">
								<div class="span2">
									<h4>Seating</h4>
								</div>
							</div>
							<div class="row">
								<div class="offset1">
									<div class="row">
										<div class="span2">
											<span class="fld_name">Phone Number :  </span>
										</div>
										<div class="span4">
											<span class="fld_txt" id="seating_gen_num"></span>
										</div>
										<div class="span2" style="display:none;"><label id="seating_search">Advanced Search</label></div>
									</div>
									<div class="row">
										<div class="span2">
											<span class="fld_name">Event Number :  </span>
										</div>
										<div class="span4">
											<span class="fld_txt" id="seating_event_id"></span>
										</div>
									</div>
									<div class="row">
										<div class="span2">
											<span class="fld_name">Extension :  </span>
										</div>
										<div class="span4">
											<span class="fld_txt" id="seating_secret_key"></span>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="span2">
									&nbsp;
								</div>
							</div>
							<div class="row">
								<div class="span2">
									<h4>RSVP</h4>
								</div>
							</div>
							<div class="row">
								<div class="offset1">
									<div class="row">
										<div class="span2">
											<span class="fld_name">Phone Number :  </span>
										</div>
										<div class="span4">
											<span class="fld_txt" id="rsvp_gen_num"></span>
										</div>
										<div class="span2" style="display:none;"><label id="seating_search">Advanced Search</label></div>
									</div>
									<div class="row">
										<div class="span2">
											<span class="fld_name">Event Number :  </span>
										</div>
										<div class="span4">
											<span class="fld_txt" id="rsvp_event_id"></span>
										</div>
									</div>
									<div class="row">
										<div class="span2">
											<span class="fld_name">Extension :  </span>
										</div>
										<div class="span4">
											<span class="fld_txt" id="rsvp_secret_key"></span>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="span3">
									 &nbsp;
								</div>
							</div>
							<div class="row">
								<div class="span3">
									 <button id="bt_get_own_phone" name="bt_get_own_phone" type="button" href="search_phone_number.jsp?event_id=<%=sEventId%>&admin_id=<%=sAdminId%>" class="btn">Get a direct phone number</button>
								</div>
							</div>
					</div>				
			</div>
		</div>										
		
	</div>
	<div style="clear:both">&nbsp;</div>
		<div class="container rounded-corners">
			<div style="margin:5px;">
			<div class="main_body">
				<div class="clear_both">					
					
					
				</div>
				<div  class="clear_both" style="width: 100%;  text-align: center;">
				<div  class="clear_both" id="tab_view_area">
					
				</div>
				<div  class="clear_both" id="div_table_details">
					
				</div>
				<div  class="clear_both" id="div_guests_details">
					
				</div>
				<div  class="clear_both" id="div_phone_numbers"  style="display:none;">
					<div class="row">
				  <div class="span8">
				    <div class="row">
				      <div class="span2"><label for="table_name">Seating Number :</label></div>
				      <div class="span4"><label id="seating_gen_num"></label></div>
				      <div class="span2" style="display:none;"><label id="seating_search">Advanced Search</label></div>
				    </div>
				    <div class="row"  style="display:none;"  id="seating_div_event_id">
				    	<div class="span8"><label for="table_name" class="span3">Event ID :&nbsp;</label><span class="span1"></span><label id="seating_event_id"  style="text-align:left"></label></div>
				    </div>
				    <div class="row"  style="display:none;"  id="seating_div_secret_key">
				    	<div class="span8"><label for="table_name" class="span3">Secret key :&nbsp;</label><span class="span1"></span><label id="seating_secret_key"  style="text-align:left"></label></div>
				    </div>
				    <div class="row" id="seating_numbers_gen" style="display:none;">
				    	 <div class="span8">
				    	 	<form id="frm_seating_numbers" >
				    	 		<fieldset>
									<div class="clearfix-tight">
										<label for="table_name">Area Code :</label>
										<div class="input">
											<input type="text" id="seating_area_code" name="seating_area_code"/>
										</div>
									</div>								
									<div class="clearfix-tight">
										<label for="table_name">Contains :</label>
										<div class="input">
											<input type="text" id="seating_text_pattern" name="seating_text_pattern"/>
										</div>
									</div>
									<div class="actions">									
							            <button id="gen_seating_tel_num" name="gen_seating_tel_num" type="button" class="action_button primary small">Generate New Number</button>
							        </div>	
								</fieldset>
								<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
								<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
				    	 	</form>
				    	 </div>
				    </div>
				     <div class="row">
				     	<div class="span2"><label for="table_name">RSVP Number :</label></div>
				      	<div class="span4"><label id="rsvp_gen_num"></label></div>
				      <div class="span2"  style="display:none;"><label id="rsvp_search">Advanced Search</label></div>
				     </div>
				     <div class="row"  style="display:none;"  id="rsvp_div_event_id">
				    	<div class="span8"><label for="table_name" class="span3">Event ID : &nbsp;</label><span class="span1"></span><label id="rsvp_event_id" class="span4" style="text-align:left" ></label></div>
				    </div>
				    <div class="row"  style="display:none;"  id="rsvp_div_secret_key">
				    	<div class="span8"><label for="table_name" class="span3">Secret key : &nbsp;</label><span class="span1"></span><label id="rsvp_secret_key" class="span4"  style="text-align:left"  ></label></div>
				    </div>
				     <div class="row" id="rsvp_numbers_gen"  style="display:none;">
				     	<div class="span8">
							<form id="frm_rsvp_numbers" >
				    	 		<fieldset>
									<div class="clearfix-tight">
										<label for="table_name">Area Code :</label>
										<div class="input">
											<input type="text" id="rsvp_area_code" name="rsvp_area_code"/>
										</div>
									</div>								
									<div class="clearfix-tight">
										<label for="table_name">Contains :</label>
										<div class="input">
											<input type="text" id="rsvp_contains" name="rsvp_contains"/>
										</div>
									</div>
									<div class="actions">									
							            <button id="gen_rsvp_tel_num" name="gen_rsvp_tel_num" type="button" class="action_button primary small">Generate New Number</button>
							        </div>	
								</fieldset>
								<input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
								<input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
				    	 	</form>
				    	 </div>
				     </div>
				  </div>
				 </div>
				 	<div class="row">
				  <div class="span8">
				  	<div class="row">
				  		&nbsp;
				  	</div>
				  </div>
				  </div>
				  	<div class="row">
				  <div class="span8">
				  	<div class="row">
				  		<div class="span8" style="text-align:center;">
				  			<button id="bt_get_own_phone" name="bt_get_own_phone" type="button" href="search_phone_number.jsp?event_id=<%=sEventId%>&admin_id=<%=sAdminId%>" class="action_button primary big">Get direct line.</button>
				  		</div>
				   	</div>
				  </div>
				</div>
			</div>
			</div>
		</div>
	</div>
	<div id="action_fancy_box">
		
	</div>
	<div id="loading_wheel" style="display:none;">
		<img src="/web/img/wheeler.gif">
	</div>
</body>

<script type="text/javascript" src="/web/js/jquery.tableformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript" src="/web/js/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="/web/js/jquery.eventguests.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/credential.js"></script>

<script type="text/javascript">
	var varEventID = '<%=sEventId%>';
	var varAdminID = '<%=sAdminId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	var varIsNewEventCreateClicked = <%=isNewEventClicked%>;

	var varSeatingNumType = '<%=Constants.EVENT_TASK.SEATING.getTask()%>';
	var varDemoSeatingNumType = '<%=Constants.EVENT_TASK.DEMO_SEATING.getTask()%>';
	var varRsvpNumType = '<%=Constants.EVENT_TASK.RSVP.getTask()%>';
	var varDemoRsvpNumType = '<%=Constants.EVENT_TASK.DEMO_RSVP.getTask()%>';
	
	$(document).ready(function() {
		$("#loading_wheel").hide();
		setCredentialEventId(varEventID);
		if(!varIsSignedIn)
		{
			setTopNavLogin(varAdminID,varEventID,'event_setup.jsp');
			
		}
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
		
		$("#phone_num_tab").click(function(){
			toggleActionNavs('li_phone_num');
			displayPhoneNumberView('li_phone_num');
		});
		
		/*if(!varIsSignedIn)
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
		}*/
		
		if(varIsNewEventCreateClicked)
		{
			$('#div_event_summary').show();
		}
		else
		{
			//$("#table_action_nav").show();
			$("#div_event_summary").show();
			
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
		
		$("#bt_get_own_phone").fancybox({
			'width'				: '75%',
			'height'			: '90%',
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
			switchTab('event_summary_tab');
			$('#div_event_summary').show();
		}
		else
		{
			loadEventSummary();
		}
		
	});
	
	$.ajaxSetup({
	    beforeSend: function(data) {
	        $("#loading_wheel").show();
	    },
	    complete: function(data) {
	        $("#loading_wheel").hide();
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
		$('#div_phone_numbers').hide();
		
	}
	
	function switchTab(current_tab_id)
	{
		$('#div_tab_nav a').each(function(index) {
		    $(this).removeClass('active');
		});
		$('#'+current_tab_id).addClass('active');
		
	}
	
	function displayEventSummaryView(tab_id)
	{		
		switchTab('event_summary_tab');
		$('#div_event_summary').show();
		loadEventSummary();
	}
	
	function displayTableView(tab_id)
	{
		switchTab('table_view_tab');
		loadTables();
	}
	
	function displayGuestView(tab_id)
	{
		switchTab('guest_view_tab');
		loadGuests();
	}
	
	function displayPhoneNumberView(tab_id)
	{		
		switchTab('phone_num_tab');
		$('#div_phone_numbers').show();
		loadPhoneNumber();
		//loadPhoneNumberFrame();
	}
	
	function loadPhoneNumber()
	{
		//alert("proc_load_phone_numbers.jsp");
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = "admin_id="+varAdminID+"&event_id="+varEventID;
		
		
		getDataAjax(actionUrl,dataString,methodType,displayPhoneNumbers);
	}
	
	function loadActions()
	{
		setNewEventClick();
		setAllGuestButtonClick();
		setLobbyButtonClick();
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
		
		clearMessages();
		getDataAjax(actionUrl,dataString,methodType, getUpdatedEvent);
	}
	
	function createEvent()
	{
		var dataString = 'admin_id='+ varAdminID+'&create_event=true';
		dataString = dataString + '&'+$("#frm_event_update").serialize();
		var actionUrl = "proc_save_event.jsp";
		var methodType = "POST";
		
		clearMessages();
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
					
					displayStatus('Changes were not saved.');
				}
			}
			else  if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
				displayStatus('Success!');
				
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
						
						assignNewEventId(varEventID, varAdminID);//this is defined in action_nav.jsp
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
	function displayStatus(varStatMssg)
	{
		var varStatusSpan = $("#act_mssg");
		varStatusSpan.html('');
		
		varStatusSpan.removeClass();
		varStatusSpan.addClass("info_mssg");
		varStatusSpan.addClass("small");
		
		varStatusSpan.html(varStatMssg);
		
	}
	
	function clearMessages()
	{
		var varStatusSpan = $("#act_mssg");
		varStatusSpan.removeClass();
		varStatusSpan.html('');
		
		var varEventNameMssg = $("#e_summ_event_name_mssg");
		varEventNameMssg.removeClass();
		varEventNameMssg.html('');
		
		var varEventDateMssg = $("#e_summ_event_date_mssg");
		varEventDateMssg.removeClass();
		varEventDateMssg.html('');
		
	}
	function displayMessages(varArrMessages)
	{
		if(varArrMessages!=undefined)
		{
			for(var i = 0; i<varArrMessages.length; i++)
			{
				var varMessageSpan = $("#"+varArrMessages[i].txt_loc_id+"_mssg");
				varMessageSpan.html("");
				
				varMessageSpan.addClass("error_mssg");
				varMessageSpan.addClass("small");
				varMessageSpan.html(varArrMessages[i].text);
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
	
	function displayPhoneNumbers(jsonResult)
	{
		//alert('status = '+jsonResult.status);
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
				//alert(varIsPayloadExist);
				
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					processTelNumbers( jsonResponseObj );
				}
			}
			else
			{
				alert("Please try again later.");
			}
		}
		else
		{
			alert("Please try again later.");
		}
	}
	
	function processTelNumbers( jsonResponseObj )
	{
		var varTelNumbers= jsonResponseObj.telnumbers;
		var totalRows = varTelNumbers.num_of_rows;
		//alert('processTelNumbers = ' + totalRows);
		if(totalRows!=undefined)
		{
			var varTelNumList = varTelNumbers.telnum_array;
			if(varTelNumList!=undefined)
			{
				for(var iRow = 0; iRow < totalRows ; iRow++ )
				{
					var telNumBean = varTelNumList[iRow];
					
					//alert('tel number = ' + telNumBean.telnum);
					
					if(telNumBean.telnum_type == varSeatingNumType || telNumBean.telnum_type ==  varDemoSeatingNumType)
					{
						$("#seating_gen_num").text(telNumBean.human_telnum);
						
					}
					if(telNumBean.telnum_type == varRsvpNumType || telNumBean.telnum_type ==  varDemoRsvpNumType )
					{
						$("#rsvp_gen_num").text(telNumBean.human_telnum);
					}
					
					if(telNumBean.telnum_type ==  varDemoSeatingNumType)
					{
						$("#seating_div_event_id").show();
						$("#seating_event_id").text(telNumBean.secret_event_identity);
						
						$("#seating_div_secret_key").show();
						$("#seating_secret_key").text(telNumBean.secret_event_key);
					}
					if(telNumBean.telnum_type ==  varSeatingNumType)
					{
						$("#seating_div_event_id").hide();
						$("#seating_event_id").text('');
						$("#seating_div_secret_key").hide();
						$("#seating_secret_key").text('');
					}
					
					if(telNumBean.telnum_type ==  varDemoRsvpNumType)
					{
						$("#rsvp_div_event_id").show();
						$("#rsvp_event_id").text(telNumBean.secret_event_identity);
						
						$("#rsvp_div_secret_key").show();
						$("#rsvp_secret_key").text(telNumBean.secret_event_key);
					}
					if(telNumBean.telnum_type ==  varRsvpNumType)
					{
						$("#rsvp_div_event_id").hide();
						$("#rsvp_event_id").text('');
						
						$("#rsvp_div_secret_key").hide();
						$("#rsvp_secret_key").text('');
					}
					
						
				}
			}
			
		}
		
	}
	
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>
