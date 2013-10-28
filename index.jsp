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

<jsp:include page="/web/com/gs/common/header_top.jsp">
    <jsp:param name="page_title" value=""/>
</jsp:include>
<%@include file="/web/com/gs/common/security.jsp"%>
<%
    HttpSession  tmpSession = request.getSession(false);
    if(tmpSession!=null)
    {
        AdminBean adminBean = (AdminBean) tmpSession.getAttribute(Constants.USER_SESSION);
        if(adminBean!=null && adminBean.isAdminExists()){
            String sSource = "host_dashboard.jsp?host_lobby_admin_id="+adminBean.getAdminId();
            String sReqSource = ParseUtil.checkNull(request.getParameter("source"));
            if(sReqSource!=null && !"".equalsIgnoreCase(sReqSource))   {
                sSource = sReqSource+"?lobby_admin_id="+adminBean.getAdminId();
            }
            response.sendRedirect("/web/com/gs/event/"+sSource);
        }
    }

%>
<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/>
<jsp:include page="/web/com/gs/common/header_bottom.jsp"/>
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body style="min-width: 960px; margin: 0 auto;" >
<jsp:include page="/web/com/gs/common/top_nav.jsp">
    <jsp:param name="referrer_source" value="index.jsp"/>
</jsp:include>
<div class="welcome_img welcome_img_ie">
    <div class="blank_scratch_area">
        <div class="row">
            <div class="offset3 span9">
                &nbsp;
            </div>
        </div>
        <div class="row">
            <div class="offset1 span11">
                <div class="slide-caption" style="text-align:center;">
                    <h1 style="color:#f5f5f5">Quickest RSVP and Guest Seating App</h1>
                    <form id="frm_event_dt" name="frm_event_dt">
                        <input type="text" id="tmp_email" class="ispn3 inp-large" style="vertical-align: bottom;"  value="Email" name="tmp_email">
                        <!-- <input type="text" id="event_date" class="ispn3 inp-large" style="vertical-align: bottom;"  value="Wedding Date (" name="event_date" readonly>   -->

                        <input type="button" class="btn btn-large btn-green" style="margin-bottom: 7px;" id="event_dt_sbt" value="Get Started for Free"/>
                    </form>
                    <div class="row">
                        <div class="span9">
                            <h4 style="color:#f5f5f5">Personalized phone number, text messages and online access for your guests</h4>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="offset3 span9">
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
                &nbsp;
            </div>
        </div>
    </div>
</div>
<section class="step_instructions" style="display: block; opacity: 1;  min-height: 144px; padding-top:10px;">
    <div class="blank_scratch_area">

        <div class="row" style="text-align:center;">
            <h2>Manage information and communicate with your guests quickly&nbsp;&nbsp;<a href="/web/com/gs/common/how_it_works.jsp?admin_id=<%=sTmpUserId %>&referrer_source=host_landing_steps" id="btn_how_it_works" class="btn btn-blue btn-large" >See how it works</a></h2>
        </div>
        <div class="row" style="text-align:center;">
            &nbsp;
        </div>

        <div class="row">
            <div class="offset2 span11">
                <h4><span class="icon-ok">&nbsp;</span>&nbsp;&nbsp;You don't have to personally wait for your guests' RSVP.</h4>
            </div>
        </div>
        <div class="row">
            <div class="offset2 span11">
                <h4><span class="icon-ok">&nbsp;</span>&nbsp;&nbsp;Recognizes your guests from their called id.</h4>
            </div>
        </div>
        <div class="row">
            <div class="offset2 span11">
                <h4><span class="icon-ok">&nbsp;</span>&nbsp;&nbsp;No delays or long lines while seating your guests at the reception.</h4>
            </div>
        </div>
        <div class="row">
            <div class="offset2 span11">
                <h4><span class="icon-ok">&nbsp;</span>&nbsp;&nbsp;Invites, RSVP and table seating accessible by phone, email or online.</h4>
            </div>
        </div>

        <div class="row" style="text-align:center;">
            &nbsp;
        </div>
        <div class="row">
            <div class="offset1 span3 step_box" style="text-align: center;  border-radius: 15px; min-height: 134px; ">
                <h1 style="padding-top:15px;">Step 1</h1>
                <h4  style="padding:5px;">Create a Seating Plan</h4>
                <span class="fld_txt_small">Invite guests and create tables.</span>
            </div>
            <div class="span3 step_box" style="text-align: center; border-radius: 15px; min-height: 134px; ">
                <h1 style="padding-top:15px;">Step 2</h1>
                <h4 style="padding:5px;">Gather RSVP and Provide Seating</h4>
                <span class="fld_txt_small">By phone, text message or online</span>
            </div>
            <div class="span3 step_box" style="text-align: center;  border-radius: 15px; min-height: 134px;  ">
                <h1 style="padding-top:15px;">Step 3</h1>
                <h4 style="padding:5px;">Get our Premium Service</h4>
                <span class="fld_txt_small" style="padding:3px;">Personalized phone number, more call minutes and text message, online and email access.</span>
            </div>
        </div>
        <div class="row" style="margin: auto;text-align:center;">
            &nbsp;
        </div>
        <div class="row" style="margin: auto;text-align:center;">
            &nbsp;
        </div>
    </div>
</section>
<form action="/web/com/gs/event/host_dashboard.jsp" method="POST" id="frm_lobby_admin">
    <input type="hidden" id="host_lobby_admin_id" name="host_lobby_admin_id" value="<%=isSignedIn ? sAdminIdSecure: "" %>"/>
</form>
</body>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script>
<script type="text/javascript" src="/web/js/credential.js"></script>
<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<jsp:include page="/web/com/gs/common/footer_top.jsp"/>
<script type="text/javascript">
    function isDateSelected() {
        displayMssgBoxAlert('A date must be selected to create a seating plan. Please select a date from the calendar pop up.', true);
    }
    $(document).ready(function() {
        managePlaceholder('tmp_email','Email');
        managePlaceholder('event_date','Wedding Date');

        $("#event_date").datepick({ minDate: 1, maxDate: "+1Y" });
        $( "#event_dt_sbt" ).bind( "click", function() {
            callSubmitEvent();
        }).bind( "keypress", function(event) {
                    if ( event.which == 13 ) {
                        callSubmitEvent();
                    }
                });

        $("#show_me_lobby").click(function(event)
        {
            $('#frm_lobby_admin').submit();
        });

        $('#event_date').live("keypress", function(event) {
            if ( event.which == 13 ) {
                callSubmitEvent();
            }
        });


        $("#lnk_how_it_works").fancybox({
            'width'				: '75%',
            'height'			: '90%',
            'autoScale'			: false,
            'transitionIn'		: 'none',
            'transitionOut'		: 'none',
            'type'				: 'iframe',
            'padding'			: 0,
            'margin'			: 0
        });


        $("#btn_how_it_works").fancybox({
            'width'				: '75%',
            'height'			: '90%',
            'autoScale'			: false,
            'transitionIn'		: 'none',
            'transitionOut'		: 'none',
            'type'				: 'iframe',
            'padding'			: 0,
            'margin'			: 0
        });
        mixpanel.track("Landing Page");
    });

    function managePlaceholder(varId, defaultTxt) {
        $('#'+varId).focus(function() {
            if( $('#'+varId).val() == defaultTxt) {
                $('#'+varId).val('');
            }
        }).blur(function() {
                    if( $('#'+varId).val() == '') {
                        $('#'+varId).val(defaultTxt);
                    }
                });
    }
    function callSubmitEvent()
    {
        var url = '/web/com/gs/proc_host_landing.jsp';
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

    function getResult(jsonResult) {
        if(!jsonResult.success) {
            $( "#event_dt_sbt" ).blur();
            displayMssgBoxAlert('Please use a valid email address.', true);
        } else  {
            $("#hid_event_date").attr( "value", $("#event_date").attr("value") );
            $("#hid_tmp_email").attr( "value", $("#tmp_email").attr("value") );
            $("#call_forward").attr("action","/web/com/gs/event/event_setup.jsp");
            $("#from_landing").val("true");
            $("#call_forward").submit();
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
<jsp:include page="/web/com/gs/common/footer_bottom.jsp"/>