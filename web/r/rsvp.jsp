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
    //jspLogging.info(" GuestResponseBean : " + guestWebResponseBean + "\nwebRsvpRequestBean : " + webRsvpRequestBean);
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
    <div class="scratch_area" >
        <div class="row"  style="background-image: url('/web/img/header_bkg.png');margin-left: 0px; height:103px  ">
            <div class="span3" style>
                &nbsp;<br>
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

            StringBuilder strInvitationMessage = new StringBuilder("You are invited to attend ");
            strInvitationMessage.append( ParseUtil.checkNull(eventBean.getEventName())).append(" on ").append(ParseUtil.checkNull(eventBean.getHumanEventDate())).append(".<br>");
            if(iTotalInvitedSeats>1) {
                strInvitationMessage.append("You may bring ").append( (iTotalInvitedSeats-1));
                if((iTotalInvitedSeats-1) == 1 ) {
                    strInvitationMessage.append(" guest ");
                } else {
                    strInvitationMessage.append(" guests ");
                }
                strInvitationMessage.append(" with you.") ;
            }

            boolean isYesSelected = false;
            boolean isNoSelected = false;

            StringBuilder strRSVPMessage = new StringBuilder();
            if(iRsvpSeats>=0){
                strRSVPMessage.append("Your response:<br>");
            }
            if(iRsvpSeats==1){
                isYesSelected = true;
                strRSVPMessage.append("<span style='font-weight:bold;font-size: 115%; color: #614b5b; margin: 0px'>Yes</span> I will attend.");
            } if(iRsvpSeats>1){
                isYesSelected = true;
                strRSVPMessage.append("<span style='font-weight:bold;font-size: 115%; color: #614b5b; margin: 0px'>Yes</span> I will attend. I will bring <span style='font-weight:bold;font-size: 115%; color: #614b5b; margin: 0px'>").append(iRsvpSeats-1);
                if((iRsvpSeats-1) == 1 ) {
                    strRSVPMessage.append(" guest</span> with me. (Total ").append(iRsvpSeats).append(")");
                } else if(iRsvpSeats > 1 ) {
                    strRSVPMessage.append(" guests</span> with me. (Total ").append(iRsvpSeats).append(")");
                }
            } else if(iRsvpSeats==0){
                isNoSelected = true;
                strRSVPMessage.append("<span style='font-weight:bold;font-size: 115%; color: #614b5b; margin: 0px'>No</span> I will not attend.");
            } else if(iRsvpSeats<0){
                strRSVPMessage.append("Please respond below.");
            }

            if(iRsvpSeats>=0){
                strRSVPMessage.append("<br><br>You can update your response below.");
            }



        %>
        <div class="row">
            <div class="offset_0_5 span12">
                <div class="row"  style="height:30px  ">
                    <div class="span3" style>
                        &nbsp;<br>
                    </div>
                </div>
                <div class="row">
                    <div class="span10" style="text-align: center;">
                        <h1>Invitation for <%=eventBean.getEventName()%></h1>
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
                        Dear <%=sGuestGivenName%>,
                    </div>
                </div>
                <div class="row">
                    <div class="span12">
                        <p><%=strInvitationMessage.toString()%></p>
                        <p><%=strRSVPMessage.toString()%></p>
                    </div>
                </div>
                <form id="frm_rsvp_response">
                <div class="row">
                    <div class="span3" style="text-align: center;">
                        <h4>Will you attend? </h4>
                    </div>
                    <div class="span1" style="text-align: left;">
                       <input type="radio" name="will_you_attend" id="attend_yes" value="yes"  style="width: 10px;" <%=isYesSelected?"checked":""%>>&nbsp;Yes
                    </div>
                    <div class="span1" style="text-align: left;">
                        <input type="radio" name="will_you_attend" id="attend_no" value="no"  style="width:10px;"  <%=isNoSelected?"checked":""%>>&nbsp;No
                    </div>
                </div>
                <div class="row" id="row_rsvp_seat_selection" style="<%=isYesSelected?"":"display:none;"%>">
                    <div class="span3">
                       &nbsp;
                    </div>
                    <div class="span6" style="text-align: left;">
                        <%
                            if( iTotalInvitedSeats>1 ) {
                                int optionSelected = (iRsvpSeats-1);
                                if(optionSelected<0 && iRsvpSeats==-1) {
                                    optionSelected =  (iTotalInvitedSeats-1);  // user has not RSVPed before,
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
                                I will attend.
                        <%
                            }
                        %>
                    </div>
                </div>
                <input type="hidden" id="link_id" name="link_id" value="<%=sLinkId%>">
                </form>
                <div class="row" style="height:30px  ">
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
                <div class="row" style="height:100px  ">
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
            var url = 'proc_rsvp.jsp';
            var formData = $("#frm_rsvp_response").serialize();
            var method = "POST";
            makeAjaxCall(url,formData,method, getResult)
        }
        function makeAjaxCall(actionUrl,dataString,methodType,callBackMethod) {
            $.ajax({
                url: actionUrl ,
                type: methodType ,
                dataType: "json",
                data: dataString ,
                success: callBackMethod,
                error:function(a,b,c){
                    alert(a.responseText + ' = ' + b + " = " + c);
                }
            });
        }
        function getResult(jsonResult) {
            if(jsonResult!=undefined)  {
                var varResponseObj = jsonResult.response;
                if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
                    var varIsMessageExist = varResponseObj.is_message_exist;
                    if(varIsMessageExist == true) {
                        var jsonResponseMessage = varResponseObj.messages;
                        var varArrErrorMssg = jsonResponseMessage.error_mssg
                        displayMssgBoxMessages( varArrErrorMssg , true );
                    }
                }  else if( jsonResult.status == 'ok' && varResponseObj !=undefined) {
                    var varIsMessageExist = varResponseObj.is_message_exist;
                    if(varIsMessageExist == true) {
                        var jsonResponseMessage = varResponseObj.messages;
                        var varArrErrorMssg = jsonResponseMessage.ok_mssg;
                        displayMssgBoxMessages( varArrErrorMssg , false );
                    }
                    var varIsPayloadExist = varResponseObj.is_payload_exist;

                    if(varIsPayloadExist == true) {
                        var varPayload = varResponseObj.payload;
                    }
                }
            }
        }

        function displayMssgBoxAlert(varMessage, isError) {
            var varTitle = 'Status';
            var varType = 'info';
            if(isError) {
                varTitle = 'Error';
                varType = 'error';
            }  else  {
                varTitle = 'Status';
                varType = 'info';
            }

            if(varMessage!='')  {
                $.msgBox({
                    title: varTitle,
                    content: varMessage,
                    type: varType,
                    buttons: [{ value: "Ok" }],
                    success: function (result) {
                        if (result == "Ok") {
                            location.reload();
                        }
                    }
                });
            }
        }

        function displayMssgBoxMessages(varArrMessages, isError) {
            if(varArrMessages!=undefined) {
                var varMssg = '';
                var isFirst = true;
                for(var i = 0; i<varArrMessages.length; i++)  {
                    if(isFirst == false) {
                        varMssg = varMssg + '\n';
                    }
                    varMssg = varMssg + varArrMessages[i].text;
                }

                if(varMssg!='')  {
                    displayMssgBoxAlert(varMssg,isError);
                }
            }


        }
    </script>
</body>
<jsp:include page="/web/com/gs/common/footer_top.jsp"/>
<jsp:include page="/web/com/gs/common/footer_bottom_fancybox.jsp"/>
</html>