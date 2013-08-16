<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.gs.common.Constants" %>
<%@ page import="com.gs.common.ParseUtil" %>
<%@ page import="com.gs.bean.response.WebRespRequest" %>
<%@ page import="com.gs.response.ReadWebRsvpResponse" %>
<%@ page import="com.gs.bean.response.GuestWebResponseBean" %>
<%@ page import="com.gs.bean.GuestBean" %>
<%@ page import="com.gs.bean.EventBean" %>
<%@ page import="com.gs.bean.EventGuestBean" %>
<%@ page import="com.gs.common.exception.InvalidRsvpResponseException" %>
<%@ page import="com.gs.common.exception.ExceptionHandler" %>
<%@ page import="com.gs.bean.UserInfoBean" %>

<jsp:include page="/web/com/gs/common/header_top.jsp"/>

<jsp:include page="/web/com/gs/common/header_bottom.jsp"/>

<%
    Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);
    String sLinkId = ParseUtil.checkNull(request.getParameter(Constants.RSVP_WEB_PARAM.LINK_ID.getParam()));

    WebRespRequest  webRsvpRequestBean = new WebRespRequest();
    webRsvpRequestBean.setLinkId(sLinkId);
    webRsvpRequestBean.setGuestWebResponseType(Constants.GUEST_WEB_RESPONSE_TYPE.RSVP);

    ReadWebRsvpResponse readWebRsvpResponse = new ReadWebRsvpResponse();
    GuestWebResponseBean guestWebResponseBean = readWebRsvpResponse.isValidLinkId(webRsvpRequestBean);
    if(guestWebResponseBean!=null && !"".equalsIgnoreCase(guestWebResponseBean.getGuestWebResponseId())) {
        webRsvpRequestBean.setGuestId( ParseUtil.checkNull(guestWebResponseBean.getGuestId()) );
        webRsvpRequestBean.setEventId( ParseUtil.checkNull(guestWebResponseBean.getEventId()) );
    }
    jspLogging.info(" GuestResponseBean : " + guestWebResponseBean + "\nwebRsvpRequestBean : " + webRsvpRequestBean);
    GuestBean guestBean = readWebRsvpResponse.getGuestFromLink(webRsvpRequestBean);
    EventBean eventBean = readWebRsvpResponse.getEventFromLink(webRsvpRequestBean);
    EventGuestBean eventGuestBean = readWebRsvpResponse.getEventGuestSeating(webRsvpRequestBean);
    try{

%>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body style="height:auto;">
<div class="navbar navbar_format site_theme_color" id="nav_bar" >
    <div class="blank_scratch_area" style="padding:5px;">
        <div class="row">
            <!-- <div class="logo span2"><a href="/">Guests</a></div> -->
            <div class="span2"><div class="logo">&nbsp;</div></div>
         </div>
    </div>
    <div class="scratch_area">
        <div class="row">
            <div class="span3">
                &nbsp;
            </div>
        </div>
        <%if(guestBean!=null && !"".equalsIgnoreCase(guestBean.getGuestId()) && eventBean!=null && !"".equalsIgnoreCase(eventBean.getEventId())
                && eventGuestBean!=null && ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats()) > 0 ) {
            UserInfoBean guestUserInfoBean = guestBean.getUserInfoBean();
            String sGuestGivenName = ParseUtil.checkNull( guestUserInfoBean.getFirstName() );
            if(sGuestGivenName!=null && !"".equalsIgnoreCase(sGuestGivenName)) {
                sGuestGivenName = sGuestGivenName + " ";
            }
            sGuestGivenName = sGuestGivenName + ParseUtil.checkNull( guestUserInfoBean.getLastName() );

            if(sGuestGivenName == null || "".equalsIgnoreCase(sGuestGivenName )) {
                sGuestGivenName = "Guest";
            }
            Integer iTotalInvitedSeats = ParseUtil.sToI( eventGuestBean.getTotalNumberOfSeats() );
            Integer iRsvpSeats = ParseUtil.sToI( eventGuestBean.getRsvpSeats() );

        %>
        <div class="row">
            <div class="offset_0_5 span12">
                <div class="row">
                    <div class="span8" style="text-align: center;">
                        <h1>Invitation for <%=eventBean.getEventName()%> on <%=eventBean.getHumanEventDate()%></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        &nbsp;
                    </div>
                </div>

                <div class="row">
                    <div class="span12">
                        <h4>Dear <%=sGuestGivenName%>,</h4>
                    </div>
                </div>
                <div class="row">
                    <div class="span3" style="text-align: center;">
                        <h4>Will you attend? </h4>
                    </div>
                    <div class="span1" style="text-align: left;">
                       <input type="radio" name="will_you_attend" id="attend_yes" value="yes"  style="width: 10px;">&nbsp;Yes
                    </div>
                    <div class="span1" style="text-align: left;">
                        <input type="radio" name="will_you_attend" id="attend_no" value="no"  style="width:10px;">&nbsp;No
                    </div>
                </div>
                <div class="row" id="row_rsvp_seat_selection" style="display:none;">
                    <div class="span3">
                       &nbsp;
                    </div>
                    <div class="span6" style="text-align: left;">
                        <%
                            if( iTotalInvitedSeats>1 ) {
                                int optionSelected = (iRsvpSeats-1);
                                if(optionSelected<0 && iRsvpSeats==-1) {
                                    optionSelected =  (iTotalInvitedSeats-1);
                                }
                        %>
                                I will attend along with <select name="num_of_guests" id="num_of_guests">
                        <%
                                for(int i=0; i < iTotalInvitedSeats; i++ ) {
                        %>
                                    <option value="<%=i%>" <%=(optionSelected==i)?"selected":"" %> ><%=i%></option>
                        <%
                                }
                        %> </select> guests.

                        <%
                            } else {
                        %>
                                I am the only one who will attend.
                        <%
                            }
                        %>
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="span6">
                        &nbsp;
                    </div>
                    <div class="span3">
                        <input type="button" class="btn btn-green btn-large" value="Update" id="update_rsvp"/>
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        &nbsp;
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        &nbsp;
                    </div>
                </div>
            </div>
        </div>
        <%
        } else {
            throw new InvalidRsvpResponseException("Invalid parameters used when loading the RSVP link. link_id : " + sLinkId );
        }
    }  catch(Exception e ) {
            jspLogging.info(e.getMessage() + " " + ExceptionHandler.getStackTrace(e));
            response.sendRedirect("/web/com/gs/common/error/error.jsp");
     }

        %>


    </div>

    <script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#update_rsvp").click(function(){
                updateGuestRSVP();
            });

            $( "#attend_yes" ).change(  function() {
                showHideRsvpSelection();
            });
            $( "#attend_no" ).change(  function() {
                showHideRsvpSelection();
            });
        });
        function showHideRsvpSelection() {
            if($( "#attend_yes" ).is(":checked") ) {
                $("#row_rsvp_seat_selection").show("slow");
            } else {
                $("#row_rsvp_seat_selection").hide("slow");
            }
        }
        function updateGuestRSVP() {
            displayMssgBoxAlert('Update clicked', false);
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
</body>
<jsp:include page="/web/com/gs/common/footer_top.jsp"/>
<jsp:include page="/web/com/gs/common/footer_bottom_fancybox.jsp"/>
</html>