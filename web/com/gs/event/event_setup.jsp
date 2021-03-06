<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.data.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.joda.time.DateTimeZone" %>
<%@ page import="com.gs.json.RespConstants" %>
<%@ page import="com.gs.json.ErrorText" %>
<%@ page import="com.gs.json.Text" %>


<jsp:include page="../common/header_top.jsp">
	<jsp:param name="page_title" value="Event Setup"/>	
</jsp:include>

<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<%
	Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);

	String sTmpEmail = ParseUtil.checkNull(request.getParameter("hid_tmp_email"));
	String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
	boolean isFromLanding = ParseUtil.sTob(request.getParameter("from_landing"));
	String sEventId = ParseUtil.checkNull(request.getParameter("lobby_event_id"));
	String sAdminId = ParseUtil.checkNull(request.getParameter("lobby_admin_id"));
	boolean isNewEventClicked= ParseUtil.sTob(request.getParameter("lobby_create_new"));
	
	jspLogging.info("Invoked by landing page : " + isFromLanding);
	String sEventTitle = "New Seating Plan";

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
		} else {
			adminBean = adminManager.createAdmin();
			adminManager.createTemporaryContact(adminBean , sTmpEmail );

            sTmpEmail = sTmpEmail.replaceAll("\'","&apos;");
		}
		
		
		if(adminBean!=null) {

            if ( Utility.isNullOrEmpty(sEventDate) ) {
                Long tmpEventCreationDate = DateSupport.addTime( DateSupport.getEpochMillis(), 1 , Constants.TIME_UNIT.YEARS );
                sEventDate = DateSupport.getTimeByZone(tmpEventCreationDate , DateTimeZone.UTC.getID() , Constants.PRETTY_DATE_PATTERN_2);
            }

            Long lCurrentTime = DateSupport.getEpochMillis();
            Long lEventCreateDate = DateSupport.getMillis( sEventDate + " 00:00:00","MM/dd/yyyy HH:mm:ss", DateTimeZone.UTC.getID() );
            Long lFutureDateLimit = DateSupport.addTime( lCurrentTime , 1 , Constants.TIME_UNIT.YEARS  );
            if( lEventCreateDate < lCurrentTime ) {
            }  else if (lEventCreateDate  > lFutureDateLimit )  {
            } else {
                EventCreationMetaDataBean eventMeta = new EventCreationMetaDataBean();
                eventMeta.setAdminBean(adminBean);
                eventMeta.setEventDate(sEventDate);
                eventMeta.setEventDatePattern(Constants.PRETTY_DATE_PATTERN_2);
                eventMeta.setEventTimeZone("central");
                eventMeta.setCreateEvent(true);
                eventMeta.setRsvpDeadlineDate(sEventDate);
                eventMeta.setRsvpDeadlineDateDatePattern(Constants.PRETTY_DATE_PATTERN_2);

                EventManager eventManager = new EventManager();
                eventBean = eventManager.createEvent(eventMeta);

                if(eventBean!=null && eventBean.getEventId()!=null && !"".equalsIgnoreCase(eventBean.getEventId()))
                {
                    EventPricingGroupManager eventPricingManager = new EventPricingGroupManager();
                    ArrayList<PricingGroupBean> arrPricingBean = eventPricingManager.getDemoPricingGroups();
                    EventFeatureManager eventFeatureManager = new EventFeatureManager();
                    if( arrPricingBean !=null ) {
                        for(PricingGroupBean pricingGroupBean : arrPricingBean ) {
                            if(pricingGroupBean!=null) {
                                eventFeatureManager.createEventFeatures(eventBean.getEventId(), Constants.EVENT_FEATURES.DEMO_TOTAL_CALL_MINUTES,ParseUtil.iToS(pricingGroupBean.getMaxMinutes()));
                                eventFeatureManager.createEventFeatures(eventBean.getEventId(), Constants.EVENT_FEATURES.DEMO_TOTAL_TEXT_MESSAGES,ParseUtil.iToS(pricingGroupBean.getSmsCount()));
                                eventFeatureManager.createEventFeatures(eventBean.getEventId(), Constants.EVENT_FEATURES.SEATINGPLAN_TELNUMBER_TYPE, Constants.TELNUMBER_TYPE.DEMO.getType() );
                                break;
                            }

                        }
                    }
                    eventFeatureManager.createEventFeatures( eventBean.getEventId() ,  Constants.EVENT_FEATURES.SEATINGPLAN_MODE, Constants.EVENT_SEATINGPLAN_MODE.RSVP.getMode() );
                    TelNumberManager telNumberManager = new TelNumberManager();
                    telNumberManager.setEventDemoNumber(eventBean.getEventId(),adminBean.getAdminId());
                }
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
		if(isNewEventClicked) {
			
		} else {
			if(sEventId!=null && !"".equalsIgnoreCase(sEventId)) {
				
				EventManager eventManager = new EventManager();
				eventBean = eventManager.getEvent(sEventId);
				
				sEventDate = eventBean.getHumanEventDate();
			}
		}
		
	}
	
	sEventId = eventBean.getEventId();
	sAdminId = adminBean.getAdminId();
	

	String sGateAdminId = sAdminId;


    // identifying whether
    boolean isPremiumSeatingPlanTelnum = false;
    String sEventTelnumType = ParseUtil.checkNull( EventFeatureManager.getStringValueFromEventFeature(sEventId, Constants.EVENT_FEATURES.SEATINGPLAN_TELNUMBER_TYPE) );
    if(sEventTelnumType!=null && Constants.TELNUMBER_TYPE.PREMIUM.getType().equalsIgnoreCase(sEventTelnumType))
    {
        isPremiumSeatingPlanTelnum = true;
    }

%>
	<%@include file="../common/gatekeeper.jsp"%>
<%
	
	String eventName = "";
	String eventDate = "";
	if(isNewEventClicked)
	{
		eventName = "New Seating Plan";
		eventDate = "";
	}
	else
	{
		eventName = eventBean.getEventName();
		eventDate = "("+DateSupport.getTimeByZone(eventBean.getEventDate(), DateSupport.getTimeZone(eventBean.getEventTimeZone()).getID(), Constants.PRETTY_DATE_PATTERN_2)+")";
	}
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >

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
			<div class = "span12 white_bkg light_border"  >
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
						<div class="offset_0_5 span11">
                            <div class="row" id="tab_event_summary_demo_warning"  style="background-color:#BE5F6C;display:none;">
                                <div class="span11">
                                    <h2 style="color:white;">Demo Phone Number Mode</h2>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span2">
                                    &nbsp;
                                </div>
                            </div>
							<div class="row">
								<div class="span5">
									<h4>Seating Plan </h4>
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
												<span class="fld_name">When (can be changed later): </span>
											</div>
                                            <div class="span2" style="text-align : left;  padding-top:5px;" >
                                                <span id="e_summ_event_date_mssg"></span>
                                            </div>
										</div>
										<div class="row">											
											<div class="span12" >
												<input type="text" class="ispn2" id="e_summ_event_date" name="e_summ_event_date"/>
                                                <select id="e_summ_event_hour" name="e_summ_event_hour">
                                                    <option value="01">1</option>
                                                    <option value="02">2</option>
                                                    <option value="03">3</option>
                                                    <option value="04">4</option>
                                                    <option value="05">5</option>
                                                    <option value="06">6</option>
                                                    <option value="07">7</option>
                                                    <option value="08">8</option>
                                                    <option value="09">9</option>
                                                    <option value="10">10</option>
                                                    <option value="11">11</option>
                                                    <option value="12">12</option>
                                                </select>
                                                <select  id="e_summ_event_min" name="e_summ_event_min">
                                                    <option value="00">00</option>
                                                    <option value="30">30</option>
                                                </select>
                                                <select  id="e_summ_event_ampm" name="e_summ_event_ampm">
                                                    <option value="AM">AM</option>
                                                    <option value="PM">PM</option>
                                                </select>
                                                <select  id="e_summ_event_timezone" name="e_summ_event_timezone">
                                                    <option value="central">Central Time</option>
                                                    <option value="eastern">Eastern Time</option>
                                                    <option value="pacific">Pacific Time</option>
                                                    <option value="mountain">Mountain Time</option>
                                                    <option value="hawaii">Hawaii Time</option>
                                                    <option value="alaska">Alaska Time</option>
                                                </select>
											</div>
										</div>
                                        <div class="row">
                                            <div class="span3" >
                                                &nbsp;
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="span5" >
                                                <span class="fld_name">Current Mode : </span>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="span12" >
                                                <select id="e_current_mode" name="e_current_mode">
                                                    <option id="<%=Constants.EVENT_SEATINGPLAN_MODE.RSVP.getMode()%>" value="<%=Constants.EVENT_SEATINGPLAN_MODE.RSVP.getMode()%>"><%=Constants.EVENT_SEATINGPLAN_MODE.RSVP.getDisplayText()%></option>
                                                    <option id="<%=Constants.EVENT_SEATINGPLAN_MODE.SEATING.getMode()%>" value="<%=Constants.EVENT_SEATINGPLAN_MODE.SEATING.getMode()%>"><%=Constants.EVENT_SEATINGPLAN_MODE.SEATING.getDisplayText()%></option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="span3" >
                                                &nbsp;
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="span5" >
                                                <span class="fld_name">RSVP deadline (can be changed later) : </span>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="span12" >
                                                <input type="text" class="ispn2" id="e_rsvp_deadline_date" name="e_rsvp_deadline_date"/>
                                            </div>
                                        </div>
										<div class="row"style="text-align:right">											
											<div class="span6" >
										<%
												if(isNewEventClicked)
												{
		%>
													<input type="button" class="btn ispn3 btn-blue" id="create_event" name="create_event" value="Create Seating Plan"/>
		<%											
												}
												else
												{
		%>
													<input type="button" class="btn ispn2 btn-blue" id="save_event" name="save_event" value="Save Changes"/>
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
									<h4>Phone Settings</h4>
								</div>
							</div>
                            <div class="row">
                                <div class="offset1 span5">
                                    <span class="fld_name"> Phone Number: </span>
                                    <span class="fld_txt" id="e_summ_telnum" ><span class="fld_txt_small">Create a new seating plan to see your telephone number. </span> </span>
                                </div>
                            </div>
                            <div class="row" id="div_event_num" style="display:none;">
                                <div class="offset1 span5">
                                    <span class="fld_txt_small" style="font-weight:bold;"> Plan Id: </span>
                                    <span class="fld_txt_small" id="e_summ_event_num" ></span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span1">
                                    &nbsp;
                                </div>
                            </div>
                            <div class="row">
                                <div class="span5">
                                    <h4>Phone Calls</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="offset1 span7">
                                    <table>
                                        <tr>
                                            <td  style="text-align:right;" ><span class="fld_txt_small"> Minutes remaining: </span></td>
                                            <td  style="text-align:left;"><span class="fld_txt_small" id="e_summ_call_minutes_remaining" >0</span></td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:right;" ><span class="fld_txt_small"> Minutes used: </span></td>
                                            <td style="text-align:left;"><span class="fld_txt_small" id="e_summ_call_minutes_used" >0</span></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span5">
                                    <h4>Text Messages</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="offset1 span7">
                                    <table>
                                        <tr>
                                            <td  style="text-align:right;" ><span class="fld_txt_small"> Text remaining: </span></td>
                                            <td  style="text-align:left;"><span class="fld_txt_small" id="e_summ_text_mmsg_remaining" >0</span></td>
                                        </tr>
                                        <tr>
                                            <td  style="text-align:right;" ><span class="fld_txt_small"> Text sent: </span></td>
                                            <td  style="text-align:left;"><span class="fld_txt_small" id="e_summ_text_mmsg_sent" >0</span></td>
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
							<div class="row">
								<div class="span5">
									<h4>Overview</h4>
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
					<div class="offset_0_5 span11">
                        <div class="row" id="tab_phone_demo_warning"  style="background-color:#BE5F6C;display:none;">
                            <div class="span11">
                                <h2 style="color:white;">Demo Phone Number Mode</h2>
                            </div>
                        </div>
                        <div class="row">
                            <div class="span2">
                                &nbsp;
                            </div>
                        </div>
                            <form id="frm_phone_numbers" >
                            <div class="row">
                                <div class="span2"  style="text-align:right;">
                                    <h4>Phone Number : </h4>
                                </div>
                                <div class="span4">
                                    <span class="fld_txt" id="telephone_num"></span>
                                </div>
                            </div>
                            <div class="row"  id="telephone_div_event_id" style="display:none;">
                                <div class="span2"   style="text-align:right;">
                                    <span class="fld_txt_small" style="font-weight:bold;">Plan Id :  </span>
                                </div>
                                <div class="span4">
                                    <span class="fld_txt" id="telephone_event_id"></span>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span2">
                                    <h4>&nbsp;</h4>
                                </div>
                            </div>
                            <div class="row" >
                                <div class="span2"   style="text-align:right;">
                                    <h4>Current Mode :  </h4>
                                </div>
                                <div class="span4">
                                    <span class="fld_txt" id="telephone_current_mode"></span>
                                </div>
                            </div>

                            <div class="row">
                                <div class="span2">
                                    &nbsp;
                                </div>
                            </div>
                            <div class="row">
                                <div class="span2">
                                    <h4>&nbsp;</h4>
                                </div>
                            </div>
							<div class="row">
								<div class="offset_0_5 span2" >
									<h4>Seating</h4>
								</div>
							</div>
							<div class="row">
								<div class="offset1">
                                    <div class="row" id="seating_div_error_call_forward">
                                        <div class="span2"   style="text-align:right;">
                                            <span class="fld_name" >Forward Calls to :  </span>
                                        </div>
                                        <div class="span4">
                                            <input type="text" id="seating_call_forward" name="seating_call_forward" value="">
                                        </div>
                                    </div>
                                    <div class="row" id="seating_div_sms_confirmation">
                                        <div class="span2"   style="text-align:right;">
                                            <input type="checkbox" id="seating_sms_confirmation" name="seating_sms_confirmation" style="width: 30px;"/>
                                        </div>
                                        <div class="span4">
                                            <span class="fld_txt">Send text to the guest's cellphone after the call</span>
                                        </div>
                                    </div>
                                    <div class="row" id="seating_div_email_confirmation">
                                        <div class="span2"   style="text-align:right;">
                                            <input type="checkbox" id="seating_email_confirmation" name="seating_email_confirmation" style="width: 30px;"/>
                                        </div>
                                        <div class="span4">
                                            <span class="fld_txt" >Send email to the guest after the call</span>
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
                                    &nbsp;
                                </div>
                            </div>
							<div class="row">
								<div class="offset_0_5 span2">
									<h4>RSVP</h4>
								</div>
							</div>
							<div class="row">
								<div class="offset1">
                                    <div class="row" id="rsvp_div_error_call_forward">
                                        <div class="span2"   style="text-align:right;">
                                            <span class="fld_name" >Forward Calls to :  </span>
                                        </div>
                                        <div class="span4">
                                            <input type="text" id="rsvp_call_forward"  name="rsvp_call_forward"value="">
                                        </div>
                                    </div>
                                    <div class="row" id="rsvp_div_sms_confirmation">
                                        <div class="span2"   style="text-align:right;">
                                           <input type="checkbox" id="rsvp_sms_confirmation" name="rsvp_sms_confirmation" style="width: 30px;"/>
                                        </div>
                                        <div class="span4">
                                            <span class="fld_txt" >Send text confirmation to guest after RSVP</span>
                                        </div>
                                    </div>
                                    <div class="row" id="rsvp_div_email_confirmation">
                                        <div class="span2"   style="text-align:right;">
                                            <input type="checkbox" id="rsvp_email_confirmation" name="rsvp_email_confirmation" style="width: 30px;"/>
                                        </div>
                                        <div class="span4">
                                            <span class="fld_txt" >Send Email confirmation to guest after RSVP</span>
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
                                    &nbsp;
                                </div>
                            </div>
                            <div class="row">
                                <div class="span7">
                                    <h4>When call minutes or text message limit is reached</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="offset1">
                                    <%
                                        if(isPremiumSeatingPlanTelnum)
                                        {
                                    %>
                                            <div class="row">
                                                <div class="span7">
                                                    <input id="limit_reached_auto_extend" name="usage_limit_reached_action" value="<%=Constants.USAGE_LIMIT_REACHED_ACTION.AUTO_EXTEND.getAction()%>"  type="radio"  style="width:10px"> &nbsp;&nbsp;<span>Automatically switch to next pricing tier.</span><span class="fld_txt_small">(Your credit card will be charged the price difference.)</span>
                                                </div>
                                            </div>
                                    <%
                                        }
                                    %>

                                    <div class="row">
                                        <div class="span7">
                                            <input id="limit_reached_stop_access" name="usage_limit_reached_action"  value="<%=Constants.USAGE_LIMIT_REACHED_ACTION.STOP_USAGE.getAction()%>"  type="radio"   style="width:10px" > &nbsp;&nbsp;<span>Stop access to event.</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            </form>
							<div class="row">
								<div class="span3">
									 &nbsp;
								</div>
							</div>
							<div class="row">
                                <div class="span3">
                                    &nbsp;
                                </div>
                                <div class="span2">
                                    <button id="btn_save_phone_number" name="btn_save_phone_number" type="button" href="search_phone_number.jsp?event_id=<%=sEventId%>&admin_id=<%=sAdminId%>" class="btn">Save Changes</button>
                                </div>
								<div class="span3"  id="div_get_own_phone"  style="display:none;">
									 <input id="bt_get_own_phone" name="bt_get_own_phone" type="button" href="search_phone_number.jsp?event_id=<%=sEventId%>&admin_id=<%=sAdminId%>" class="btn btn-blue btn-large ispn4" value="Get a personalized phone number"/>
								</div>
							</div>
                            <div class="row">
                                <div class="span3">
                                    &nbsp;
                                </div>
                            </div>
					</div>				
			</div>
                    <!-- The emails setup summary -->
                    <div  class="row" id="div_emails"  style="display:none;">
                        <div class="offset_0_5 span11">
                            <div class="row" id="tab_emails_warning"  style="background-color:#BE5F6C;display:none;">
                                <div class="span11">
                                    <h2 style="color:white;">Demo Phone Number Mode</h2>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span2">
                                    &nbsp;
                                </div>
                            </div>
                            <div class="row">
                                <div class="span6">
                                    <h2>Gather RSVP from Guests (who did not RSVP)</h2>
                                </div>
                            </div>
                            <div class="row">
                                <div class="offset_0_5 span12">
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span12">
                                            <h4>Subject: </h4> <span id="email_template_rsvp_response_subject"></span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span12">
                                            <h4>Body: </h4>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span10"  style="height: 325px;background-color: #f7f7f7;border:1px solid #A4BE5F;">
                                            <div class="row" style="height: 325px;">
                                                <div class="offset_0_5 span9">
                                                    <span  id="email_template_rsvp_response_body" style="padding: 10px;"> </span>
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
                                        <div class="span12">
                                            Number of times email was sent : <span id="rsvp_num_of_times_email_sent">0</span><br>
                                            Is email scheduled to be sent :  <span id="rsvp_email_scheduled_for_send">No</span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                        <div class="span2">
                                            <input type="button" id="send_rsvp_response_email" name="send_rsvp_response_email" class="btn btn-large" value="Email Guests"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
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
                                <div class="span6">
                                    <h2>Seating Information for Guests </h2>
                                </div>
                            </div>

                            <div class="row">
                                <div class="offset_0_5 span12">

                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span12">
                                            <h4>Subject: </h4> <span id="email_template_seating_info_subject"></span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span12">
                                            <h4>Body: </h4>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span10"  style="height: 325px;background-color: #f7f7f7;border:1px solid #A4BE5F;">
                                            <div class="row" style="height: 325px;">
                                                <div class="offset_0_5 span9">
                                                    <span  id="email_template_seating_info_body" style="padding: 10px;"> </span>
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
                                        <div class="span12">
                                            Number of times email was sent : <span id="seating_info_num_of_times_email_sent">0</span><br>
                                            Is email scheduled to be sent :  <span id="seating_info_email_scheduled_for_send">No</span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                        <div class="span2">
                                            <input type="button" id="send_seating_info_email" name="send_seating_info_email" class="btn btn-large" value="Email Guests"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="span2">
                                            &nbsp;
                                        </div>
                                    </div>
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

			</div>
		</div>
	</div>
	<div id="action_fancy_box">
		
	</div>
	<div id="loading_wheel" style="display:none;">
		<img src="/web/img/wheeler.gif">
	</div>
    </div>
    </div>
</body>

<script type="text/javascript" src="/web/js/jquery.tableformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript" src="/web/js/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="/web/js/jquery.eventguests.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/credential.js"></script>
<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript" src="/web/js/general.js"></script>
<script type="text/javascript">
	var varEventID = '<%=sEventId%>';
	var varAdminID = '<%=sAdminId%>';
	var varIsSignedIn = <%=isSignedIn%>;
	var varIsNewEventCreateClicked = <%=isNewEventClicked%>;

	var varSeatingNumType = '<%=Constants.EVENT_TASK.SEATING.getTask()%>';
	var varDemoSeatingNumType = '<%=Constants.EVENT_TASK.DEMO_SEATING.getTask()%>';
	var varRsvpNumType = '<%=Constants.EVENT_TASK.RSVP.getTask()%>';
	var varDemoRsvpNumType = '<%=Constants.EVENT_TASK.DEMO_RSVP.getTask()%>';

    var varPremiumTelephoneNumType = '<%=Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask()%>';
    var varDemoTelephoneNumType = '<%=Constants.EVENT_TASK.DEMO_TELEPHONE_NUMBER.getTask()%>';

    var varIsFromLandingPage = <%=isFromLanding%>;
    var varTmpAdminId = '<%=adminBean.getAdminId()%>';

	$(document).ready(function() {
		$("#loading_wheel").hide();
        if(varIsFromLandingPage) {
            if(mixpanel!=undefined) {
                mixpanel.track("Create Free Plan",  {"Admin id": varTmpAdminId});
            }
        }
		setCredentialEventId(varEventID);
		if(!varIsSignedIn)
		{
			setTopNavLogin(varAdminID,varEventID,'event_setup.jsp');
			setTopNavSingup(varAdminID,varEventID,'event_setup.jsp');
			
		}
		$("#e_summ_event_date").datepick({ defaultDate :1,minDate: 1, maxDate: "+1Y" ,showDefault: true});
        $("#e_rsvp_deadline_date").datepick({ defaultDate:1, minDate: 1, maxDate: "+1Y" ,showDefault: true});
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
			if(varEventID == '')
			{
				//alert('First create the event before adding tables;');
				displayMssgBoxAlert('Please create the seating plan before adding tables', true);
			} else {
                if(mixpanel!=undefined) {
                    mixpanel.track('Add/Edit Table Tab', {'Admin id' : varAdminID, 'Event Id' : varEventID });
                }
				toggleActionNavs('table_action_nav');
				displayTableView('li_table_view');
			}

		});
		$("#guest_view_tab").click(function(){
			if(varEventID == '')
			{
				//alert('First create the event before adding tables;');
				displayMssgBoxAlert('Please create the seating plan before inviting guests', true);
			} else {
                if(mixpanel!=undefined) {
                    mixpanel.track('Invite Guest Tab', {'Admin id' : varAdminID, 'Event Id' : varEventID });
                }
				displayGuestView('li_guest_view');
				toggleActionNavs('invite_guest_action_nav');				
			}
		});
		
		$("#phone_num_tab").click(function(){
			if(varEventID == '')
			{
				//alert('First create the event before adding tables;');
				displayMssgBoxAlert('Please create the seating plan before creating phone numbers', true);
			} else {
                if(mixpanel!=undefined) {
                    mixpanel.track('Personalize Phone Number Tab', {'Admin id' : varAdminID, 'Event Id' : varEventID });
                }
				toggleActionNavs('li_phone_num');
				displayPhoneNumberView('li_phone_num');
                loadEventFeatures();
			}

		});
        $("#email_tab").click(function(){
            if(varEventID == '') {
                displayMssgBoxAlert('Please create the seating plan before creating phone numbers', true);
            } else {
                if(mixpanel!=undefined) {
                    mixpanel.track('Email Guest Tab', {'Admin id' : varAdminID, 'Event Id' : varEventID });
                }
                toggleActionNavs('li_email');
                displayEmailView('li_email');
                //loadEventFeatures();
            }
        });
		
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
			'height'			: '90%',
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
			'width'				: '75%',
			'height'			: '90%',
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
			switchTab('li_event_summary');
			$('#div_event_summary').show();
		}
		else
		{
			loadEventSummary();
		}


        $('#rsvp_call_forward').keydown( function(e){
            FormatPhone(e, this);
        });

        $('#seating_call_forward').keydown( function(e){
            FormatPhone(e, this);
        });

        $('#btn_save_phone_number').click( function(e){
            savePhoneNumberFeatures();
        });

        $('#send_rsvp_response_email').click(function(event) {
            scheduleRsvpResponseEmailSend();
        });
        $('#send_seating_info_email').click(function(event) {
            scheduleSeatingInfoEmailSend();
        });


        $('#e_summ_event_name').live("keypress", function(event) {
            if ( event.which == 13 ) {
                if(varIsNewEventCreateClicked)
                {
                    createEvent();
                }
                else
                {
                    saveEvent();
                }
                $('#e_summ_event_name').blur();
            }
        });
        $('#e_summ_event_date').live("keypress", function(event) {
            if ( event.which == 13 ) {
                if(varIsNewEventCreateClicked)
                {
                    createEvent();
                }
                else
                {
                    saveEvent();
                }
                $('#e_summ_event_date').blur();
            }
        });
		
	});
	
	$.ajaxSetup({
	    beforeSend: function(data) {
	        $("#loading_wheel").show();
	    },
	    complete: function(data) {
	        $("#loading_wheel").hide();
	    }
	});

    function loadEventFeatures()
    {
        var actionUrl = "proc_phone_number_features.jsp";
        var dataString = $('#frm_phone_numbers').serialize()+'&action=load' +"&admin_id="+varAdminID+"&event_id="+varEventID;
        var methodType = "POST";

        getDataAjax(actionUrl,dataString,methodType, loadPhoneNumberFeaturesResult);
    }

    function savePhoneNumberFeatures()
    {
        var actionUrl = "proc_phone_number_features.jsp";
        var dataString = $('#frm_phone_numbers').serialize()+'&action=save' +"&admin_id="+varAdminID+"&event_id="+varEventID;
        var methodType = "POST";

        getDataAjax(actionUrl,dataString,methodType, savePhoneNumberFeaturesResult);
    }

    function loadPhoneNumberFeaturesResult(jsonResult)
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

                    var varJsonAllEventFeatures = jsonResponseObj.all_features;
                    var varMapFeatureValue = varJsonAllEventFeatures.map_feature_value;

                    var varUsageLimitAction = 'STOP_USAGE'; //default action
                    if(varMapFeatureValue != undefined)
                    {
                        $('#seating_call_forward').val(   varMapFeatureValue.SEATING_CALL_FORWARD_NUMBER_HUMAN );
                        $('#rsvp_call_forward').val(   varMapFeatureValue.RSVP_CALL_FORWARD_NUMBER_HUMAN );

                        if(varMapFeatureValue.SEATING_SMS_GUEST_AFTER_CALL == 'true')
                        {
                            $('#seating_sms_confirmation').prop('checked', true);
                        }

                        if(varMapFeatureValue.SEATING_EMAIL_GUEST_AFTER_CALL == 'true')
                        {
                            $('#seating_email_confirmation').prop('checked', true);
                        }

                        if(varMapFeatureValue.RSVP_SMS_CONFIRMATION == 'true')
                        {
                            $('#rsvp_sms_confirmation').prop('checked', true);;
                        }

                        if(varMapFeatureValue.RSVP_EMAIL_CONFIRMATION == 'true')
                        {
                            $('#rsvp_email_confirmation').prop('checked', true);
                        }

                        varUsageLimitAction = varMapFeatureValue.USAGE_LIMIT_REACHED_ACTION;
                    }

                    $('#telephone_current_mode').text(jsonResponseObj.event_seatingplan_mode);


                    var varUsageLimitAction = 'STOP_USAGE'; //default action
                    var varTmpUsageLimitAction = varMapFeatureValue.USAGE_LIMIT_REACHED_ACTION;
                    if(varMapFeatureValue != undefined && varTmpUsageLimitAction != undefined && varTmpUsageLimitAction != '' )
                    {
                        varUsageLimitAction = varTmpUsageLimitAction;
                    }
                    if( varUsageLimitAction == 'AUTO_EXTEND' )
                    {
                        $('#limit_reached_auto_extend').prop('checked', true);
                    }
                    else if (  varUsageLimitAction == 'STOP_USAGE'  )
                    {
                        $('#limit_reached_stop_access').prop('checked', true);
                    }

                }
            }
            else
            {
                displayMssgBoxAlert("There was an error processing your request. Please try again later.");
            }
        }
        else
        {
            displayMssgBoxAlert("There was an error processing your request. Please try again later.");
        }
    }

    function savePhoneNumberFeaturesResult(jsonResult)
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
                    displayMssgBoxMessages( varArrErrorMssg , true);
                }

            }
            else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
            {
                var varIsPayloadExist = varResponseObj.is_payload_exist;
                //alert(varIsPayloadExist);
                displayMssgBoxAlert("Your changes were successfully updated.");
                /*if(varIsPayloadExist == true)
                {
                    var jsonResponseObj = varResponseObj.payload;
                    processTelNumbers( jsonResponseObj );
                }*/
            }
            else
            {
                displayMssgBoxAlert("There was an error processing your request. Please try again later.");
            }
        }
        else
        {
            displayMssgBoxAlert("There was an error processing your request. Please try again later.");
        }
    }
	
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
        $('#div_emails').hide();
		
	}
	
	function switchTab(current_tab_id)
	{
		$('#div_tab_nav li').each(function(index) {
		    $(this).removeClass('active');
		});
		$('#'+current_tab_id).addClass('active');
		
	}
	
	function displayEventSummaryView(tab_id)
	{		
		switchTab('li_event_summary');
		$('#div_event_summary').show();
		loadEventSummary();
	}
	
	function displayTableView(tab_id)
	{
		switchTab('li_table_view');
		loadTables();
	}
	
	function displayGuestView(tab_id)
	{
		switchTab('li_guest_view');
		loadGuests();
	}
	
	function displayPhoneNumberView(tab_id)
	{		
		switchTab('li_phone_num');
		$('#div_phone_numbers').show();
		loadPhoneNumber();
	}
    function displayEmailView(tab_id)
    {
        switchTab('li_email');
        $('#div_emails').show();
        loadEmails();
    }
	
	function loadPhoneNumber()
	{
		//alert("proc_load_phone_numbers.jsp");
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = "admin_id="+varAdminID+"&event_id="+varEventID;
		
		
		getDataAjax(actionUrl,dataString,methodType,displayPhoneNumbers);
	}

    function loadEmails() {
        var actionUrl = "proc_load_emails.jsp";
        var methodType = "POST";
        var dataString = "admin_id="+varAdminID+"&event_id="+varEventID;


        getDataAjax(actionUrl,dataString,methodType,displayEmails);
    }

    function scheduleRsvpResponseEmailSend(){
        //displayMssgBoxAlert( 'schedule RSVP email',false );

        var actionUrl = "proc_schedule_rsvp_emails.jsp";
        var methodType = "POST";
        var dataString = "admin_id="+varAdminID+"&event_id="+varEventID;


        getDataAjax(actionUrl,dataString,methodType,getScheduleRsvpResult);
    }

    function scheduleSeatingInfoEmailSend(){
        //displayMssgBoxAlert( 'schedule RSVP email',false );

        var actionUrl = "proc_schedule_seating_info_emails.jsp";
        var methodType = "POST";
        var dataString = "admin_id="+varAdminID+"&event_id="+varEventID;


        getDataAjax(actionUrl,dataString,methodType,getScheduleRsvpResult);
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
					//displayMessages( varArrErrorMssg );

                    displayMssgBoxMessages( varArrErrorMssg , true);
					
					//displayStatus('Changes were not saved.');
				}
			}
			else  if( jsonResult.status == 'ok' && varResponseObj !=undefined)
			{
                var varEventName =  $("#e_summ_event_name").val();
                 if(varEventName.length > 20) {
                     varEventName = varEventName.substr(0,17) + '...';
                 }
				$("#primary_header").text( varEventName  );
				
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				
				if(varIsPayloadExist == true)
				{
					var jsonResponseObj = varResponseObj.payload;
					if(jsonResponseObj.create_event == true)
					{
                        //$("#save_event").show();
                        $("#create_event").val('Save Changes');

                        $("#create_event").unbind('click');
                        $("#create_event").click(function(){
                            saveEvent();
                        });

                        displayMssgBoxAlert('Your new seating plan was successfully created.', false);
						//alert(jsonResponseObj.event_bean.event_id)
						varEventID = jsonResponseObj.event_bean.event_id;
						
						assignNewEventId(varEventID, varAdminID);//this is defined in action_nav.jsp

                        loadEventSummary();
					}
                    else if(jsonResponseObj.update_event == true)
                    {
                        displayMssgBoxAlert('Your changes to the seating plan were successfully updated.', false);
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
						'width'				: '75%',
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
									
									$.msgBox({
									    title: "Uninvite Guest",
									    content: "Are you sure you want to remove guest from the invitee list?",
									    type: "confirm",
									    buttons: [{ value: "Yes" }, { value: "No" }, { value: "Cancel"}],
									    success: function (result) {
									        if (result == "Yes") {
									            //alert("One cup of coffee coming right up!");
									        	uninvite_event_guest_action(event.data.tmp_guest_id , event.data.tmp_event_id , event.data.tmp_event_guest_id  );
									        }
									    }
									});
									
								/*if(confirm('Do you want to uninvite this guest?'))
								{
									uninvite_event_guest_action(event.data.tmp_guest_id , event.data.tmp_event_id , event.data.tmp_event_guest_id  );
								}*/
							});
					
				}
			}
		}
	}
	
	function uninvite_event_guest_action(varTmpGuestId,varTmpEventId,varTmpEventGuestId)
	{		
			//$("#table_"+tableid).remove();
			
			var dataString = '&event_id='+ varTmpEventId + '&guest_id='+varTmpGuestId + '&event_guest_id=' + varTmpEventGuestId;
			var actionUrl = "proc_uninvite_guest.jsp";
			var methodType = "POST";
			
			//getAllTablesData(actionUrl,dataString,methodType);
			
			getDataAjax(actionUrl,dataString,methodType, uninviteGuest);
		
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
			$("#e_summ_total_table").text(eventSummary.total_table);
			$("#e_summ_total_seats").text(eventSummary.total_seats);
			$("#e_summ_assigned_seats").text(eventSummary.assigned_seats);
			$("#e_summ_total_invited").text(eventSummary.total_guest_invited);
			$("#e_summ_total_rsvp").text(eventSummary.total_guest_rsvp);

            $("#e_summ_telnum").text(eventSummary.telephone_number);


            var varEventDateObj = eventSummary.event_date_obj;
            $("#e_summ_event_date").val(varEventDateObj.event_date);
            $("#e_summ_event_hour").val(varEventDateObj.event_hr);
            $("#e_summ_event_min").val(varEventDateObj.event_min);
            $("#e_summ_event_ampm").val(varEventDateObj.event_ampm);
            $("#e_summ_event_timezone").val(varEventDateObj.event_timezone);

            $("#e_rsvp_deadline_date").val(eventSummary.rsvp_deadline_date);
            // phone usage summary
            var varPhoneUsage = eventSummary.phone_call_usage;
            var varRemainingMinutes = eval(varPhoneUsage.telnum_premium_mins_remain) + eval( varPhoneUsage.telnum_demo_mins_remain );
            $("#e_summ_call_minutes_remaining").text(varRemainingMinutes);

            var varUsedMinutes = eval(varPhoneUsage.telnum_premium_mins_used) + eval( varPhoneUsage.telnum_demo_mins_used );
            $("#e_summ_call_minutes_used").text(varUsedMinutes);

            // text message summary
            var varTextMessageUsage = eventSummary.text_message_usage;

            var varRemainingTxtMssg = eval(varTextMessageUsage.telnum_premium_text_mssg_remaining) + eval( varTextMessageUsage.telnum_demo_text_mssg_remaining );
            $("#e_summ_text_mmsg_remaining").text(varRemainingTxtMssg);

            var varTxtMssgSent = eval(varTextMessageUsage.telnum_premium_text_mssg_sent) + eval( varTextMessageUsage.telnum_demo_text_mssg_sent );
            $("#e_summ_text_mmsg_sent").text(varTxtMssgSent);
            $('#e_current_mode').val(eventSummary.seating_plan_mode);
            if( eventSummary.is_demo_numbers == true )
            {
                $("#div_event_num").show();
                $("#e_summ_event_num").text(eventSummary.telephony_event_number);

                $("#tab_event_summary_demo_warning").show();

            } else {

                $("#div_event_num").hide();
                $("#e_summ_event_num").text('');

                $("#tab_event_summary_demo_warning").hide();
            }
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
			$("#link_del_table_"+varTableId).bind('click',{'tmp_del_table_link':'/web/com/gs/event/proc_delete_table.jsp','tmp_table_id':varTableId},function(event)
			{
				$.msgBox({
				    title: "Delete Table",
				    content: "Are you sure you want to delete this table?",
				    type: "confirm",
				    buttons: [{ value: "Yes" }, { value: "No" }, { value: "Cancel"}],
				    success: function (result) {
				        if (result == "Yes") {
				            //alert("One cup of coffee coming right up!");
				        	delete_table_action(event.data.tmp_del_table_link , event.data.tmp_table_id  );
				        }
				    }
				});
			});
			/*$('#link_del_table_'+varTableId).click(function() {
				delete_table_action('/web/com/gs/event/proc_delete_table.jsp',varTableId);
			});*/
			$("#link_table_"+varTableId).fancybox({
				'width'				: '75%',
				'height'			: '90%',
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
				'width'				: '75%',
				'height'			: '90%',
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
		//alert('Edit Guests');
	}
	
	function edit_table_action(url,tableid)
	{
		//alert('Edit Table');
	}
	
	function delete_table_action(url,tableid)
	{
		//var confirmDelete = confirm('Do you want to delete this table?');
		
		//if(confirmDelete == true)
		//{
			$("#table_"+tableid).remove();
			
			var dataString = '&event_id='+ varEventID + '&table_id='+tableid;
			var actionUrl = "proc_delete_table.jsp";
			var methodType = "POST";
			
			//getAllTablesData(actionUrl,dataString,methodType);
			
			getDataAjax(actionUrl,dataString,methodType, deleteTable);
		//}
		
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
    function getScheduleRsvpResult(jsonResult) {
        if(jsonResult!=undefined){
            var varResponseObj = jsonResult.response;
            if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
                var varIsMessageExist = varResponseObj.is_message_exist;
                if(varIsMessageExist == true) {
                    var jsonResponseMessage = varResponseObj.messages;
                    var varArrErrorMssg = jsonResponseMessage.error_mss;
                    displayMssgBoxMessages( varArrErrorMssg , true );
                }
            } else if( jsonResult.status == 'ok' && varResponseObj !=undefined ) {
                var varIsMessageExist = varResponseObj.is_message_exist;
                resetWebEmailStats();
                if(varIsMessageExist == true) {
                    var jsonResponseMessage = varResponseObj.messages;
                    var varArrOkMssg = jsonResponseMessage.ok_mssg
                    displayMssgBoxMessages( varArrOkMssg , false );
                }
            }
        }
    }

    function resetWebEmailStats() {
        $('#seating_info_email_scheduled_for_send').empty();
        $('#seating_info_email_scheduled_for_send').text('Yes');

        $('#rsvp_email_scheduled_for_send').empty();
        $('#rsvp_email_scheduled_for_send').text('Yes');


    }
    function displayEmails( jsonResult ) {
        if(jsonResult!=undefined){
            var varResponseObj = jsonResult.response;
            if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
                var varIsMessageExist = varResponseObj.is_message_exist;
                if(varIsMessageExist == true) {
                    var jsonResponseMessage = varResponseObj.messages;
                    var varArrErrorMssg = jsonResponseMessage.error_mssg
                    displayMssgBoxMessages( varArrErrorMssg,true );
                }
            } else if( jsonResult.status == 'ok' && varResponseObj !=undefined ) {
                var varIsPayloadExist = varResponseObj.is_payload_exist;
                if(varIsPayloadExist == true)  {
                    var jsonResponseObj = varResponseObj.payload;

                    processEmails( jsonResponseObj );
                }
            } else {
                displayMssgBoxAlert("There was an error processing your request. Please try again later.");
            }
        }  else  {
            displayMssgBoxAlert("There was an error processing your request. Please try again later.");
        }
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
                displayMssgBoxAlert("There was an error processing your request. Please try again later.");
			}
		}
		else
		{
            displayMssgBoxAlert("There was an error processing your request. Please try again later.");
		}
	}

    function processEmails( jsonResponseObj ) {

        var varRsvpResponseEmail = jsonResponseObj.rsvp_response_email;
        if(varRsvpResponseEmail!=undefined) {
            $('#email_template_rsvp_response_subject').text(varRsvpResponseEmail.email_subject);
            $('#email_template_rsvp_response_body').html(varRsvpResponseEmail.html_body);
        }

        $('#rsvp_num_of_times_email_sent').empty();
        $('#rsvp_num_of_times_email_sent').text(jsonResponseObj.num_of_rsvp_email_send_complete);

        $('#rsvp_email_scheduled_for_send').empty();
        if( jsonResponseObj.num_of_rsvp_email_scheduled > 0 ) {
            $('#rsvp_email_scheduled_for_send').text('Yes');
        } else {
            $('#rsvp_email_scheduled_for_send').text('No');
        }


        var varSeatingInfoEmail = jsonResponseObj.seating_info_email;
        if(varSeatingInfoEmail!=undefined) {
            $('#email_template_seating_info_subject').text(varSeatingInfoEmail.email_subject);
            $('#email_template_seating_info_body').html(varSeatingInfoEmail.html_body);
        }

        $('#seating_info_num_of_times_email_sent').empty();
        $('#seating_info_num_of_times_email_sent').text(jsonResponseObj.num_of_seating_info_email_send_complete);

        $('#seating_info_email_scheduled_for_send').empty();
        if( jsonResponseObj.num_of_seating_info_email_scheduled > 0 ) {
            $('#seating_info_email_scheduled_for_send').text('Yes');
        } else {
            $('#seating_info_email_scheduled_for_send').text('No');
        }
    }
	function processTelNumbers( jsonResponseObj )
	{
		var varTelNumbers= jsonResponseObj.telnumbers;
		var totalRows = varTelNumbers.num_of_rows;
		if(totalRows!=undefined)
		{
			var varTelNumList = varTelNumbers.telnum_array;
			if(varTelNumList!=undefined)
			{
				for(var iRow = 0; iRow < totalRows ; iRow++ )
				{
					var telNumBean = varTelNumList[iRow];

                    $("#telephone_num").text(telNumBean.human_telnum);
                    $("#telephone_current_mode").text( telNumBean.event_seatingplan_mode );

                    if( telNumBean.telnum_type == varDemoTelephoneNumType ) {
                        $("#telephone_div_event_id").show();
                        $("#telephone_event_id").text(telNumBean.secret_event_identity);

                        $("#tab_phone_demo_warning").show();
                        $("#div_get_own_phone").show();
                    } else {
                        $("#tab_phone_demo_warning").hide();
                        $("#div_get_own_phone").hide();
                        $("#telephone_div_event_id").hide();
                        $("#tab_phone_demo_warning").remove();
                        $("#div_get_own_phone").remove();
                        $("#telephone_div_event_id").remove();

                    }
				}
			}
			
		}
		
	}
	
	function displayMssgBoxAlert(varMessage, isError)
	{
		var varTitle = 'Status';
		var varType = 'info';
		if(isError)
		{
			varTitle = 'Error';
			varType = 'error';
		}
		else
		{
			varTitle = 'Status';	
			varType = 'info';
		}
		
		if(varMessage!='')
		{
			$.msgBox({
                title: varTitle,
                content: varMessage,
                type: varType
            });
		}
	}
	
	function displayMssgBoxMessages(varArrMessages, isError)
	{
		if(varArrMessages!=undefined)
		{
			
				
			var varMssg = '';
			var isFirst = true;
			for(var i = 0; i<varArrMessages.length; i++)
			{
				if(isFirst == false)
				{
					varMssg = varMssg + '\n';
				}
				varMssg = varMssg + varArrMessages[i].text;
			}
			
			if(varMssg!='')
			{
                displayMssgBoxAlert(varMssg,isError);
			}
		}
		

	}
	
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>
