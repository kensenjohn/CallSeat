<%@ page import="com.gs.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.gs.manager.event.*"%>
<%@ page import="com.gs.bean.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<%@include file="../common/security.jsp"%>
<jsp:include page="../common/header_bottom.jsp"/>

<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
<body style="height:auto;">
	<%
		Logger jspLogging = LoggerFactory.getLogger(Constants.JSP_LOGS);
        Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));


        PurchaseTransactionBean purchaseTransactionBean = new PurchaseTransactionBean();
        purchaseTransactionBean.setAdminId(sAdminId);
        purchaseTransactionBean.setEventId(sEventId);

        PurchaseTransactionManager purchaseTransactionManager = new PurchaseTransactionManager();
        PurchaseTransactionBean purchaseResponseTransactionBean = purchaseTransactionManager.getPurchaseTransactionByEventAdmin(purchaseTransactionBean);

        boolean isTelephoneNumberSelectedPreviously = false;
        if(purchaseResponseTransactionBean!=null && !Utility.isNullOrEmpty(purchaseResponseTransactionBean.getTelephoneNumber()) ) {
            String sUnformattedTelephonyText  = purchaseResponseTransactionBean.getTelephoneNumber().replaceAll(" ","").replace(")","").replace("(","");

            TelNumberManager telNumManager = new TelNumberManager();

            TelNumberMetaData searchTelephoneNumberMetaData = new TelNumberMetaData();
            searchTelephoneNumberMetaData.setTextPatternSearch( sUnformattedTelephonyText  );

            ArrayList<TelNumberBean> arrTelephoneTelNumberBean  = telNumManager.searchTelNumber(searchTelephoneNumberMetaData,Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask());
            if( arrTelephoneTelNumberBean!=null && !arrTelephoneTelNumberBean.isEmpty()) {
                isTelephoneNumberSelectedPreviously = true;
            }
            appLogging.info("Telephony number : "  + sUnformattedTelephonyText + " Result after search : " + arrTelephoneTelNumberBean );
        }

        String sTelephoneNumber =  "Loading new number ..";
        if(isTelephoneNumberSelectedPreviously){
            if( !Utility.isNullOrEmpty(purchaseResponseTransactionBean.getTelephoneNumber()) ) {
                sTelephoneNumber = ParseUtil.checkNull(purchaseResponseTransactionBean.getTelephoneNumber());
            }
        }
	%>
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
	<div  class="fnbx_scratch_area">
               <div class="row">
                <div class="offset1 span11">
                    <jsp:include page="breadcrumb_shopping_cart.jsp">
                        <jsp:param name="active_now" value="search_phone_number.jsp" />
                    </jsp:include>
                </div>
               </div>
				<div class="row">
				  <div class="offset1 span11">
				  	<div class="row">
				  		<div class="span10">
				  			<h2>Personalize your phone number</h2>
				  		</div>
				  	</div>
				  	<div class="row">
				  		<div class="span10">
				  			&nbsp;
				  		</div>
				  	</div>
                    <div class="row">
                        <div class="span8">
                            <h4>Phone Number</h4>
                        </div>
                    </div>
                      <div class="row">
                          <div class="span6">
                              <span>&nbsp; </span>
                          </div>
                      </div>
                    <div class="row">
                        <div class="offset_0_5 span2"><span id="telephone_gen_num" class="fld_txt"><%=sTelephoneNumber%></span></div>
                        <div class="span3" id="div_telephony_search" style="display:none;"><span id="telephony_search" class="fld_link_txt">Customize  number</span></div>
                    </div>
                    <div class="row" id="telephony_numbers_gen"  style="display:none;">
                        <div class="offset_0_5 span10">
                              <form id="frm_telephony_numbers"  style="margin-bottom:5px" >
                                  <div class="row">
                                      <div class="span6">
                                          <span>&nbsp; </span>
                                      </div>
                                  </div>
                                  <div class="row">
                                      <div class="span6">
                                          <span class="fld_name_small">Search by</span>
                                      </div>
                                  </div>
                                  <div class="row">
                                      <div class="offset_0_5 span1" style="margin-top:6px;">
                                          <span class="fld_txt_small"  style="float:right;">Area Code:</span>
                                      </div>
                                      <div class="span1"  style="margin-left:10px;"><input type="text" id="telephony_area_code" name="telephony_area_code" style="margin-left: 10px;" /></div>
                                  </div>
                                  <div class="row">
                                      <div class="offset_0_5 span1">
                                          <span class="fld_txt_small"  style="float:right;">Contains:</span>
                                      </div>
                                      <div class="span1"  style="margin-left:10px;">
                                          <input type="text" id="telephony_text_pattern" name="telephony_text_pattern"  style="margin-left: 10px;"/>
                                      </div>
                                  </div>
                                  <div class="row">
                                      <div class="offset3 span4" >
                                          <button id="gen_telephony_tel_num" name="gen_telephony_tel_num" type="button" class="btn btn-small span1">Search</button>
                                      </div>
                                  </div>
                                  <input type="hidden" name="admin_id" id="admin_id" value="<%=sAdminId%>"/>
                                  <input type="hidden" name="event_id" id="event_id" value="<%=sEventId%>"/>
                              </form>
                        </div>
                    </div>
				  <div class="span8">
				  	<div class="row">
				  		&nbsp;
				  	</div>
				  </div>

                  <div class="row">
                      <div class="span6">
                          <span>&nbsp; </span>
                      </div>
                  </div>
				  <div class="span8">
				  	<div class="row">
				  		<div class="span8">
				  			<button id="bt_get_pricing_option" name="bt_get_pricing_option" type="button" class="btn btn-blue btn-large">Show me my pricing options </button>
				  		</div>
				   	</div>
				  </div>
                      <div class="span8">
                          <div class="row">
                              &nbsp;
                          </div>
                      </div>
                      <div class="span8">
                          <div class="row">
                              &nbsp;
                          </div>
                      </div>
				</div>
			</div>
			</div>
			<form id="frm_telnum_bill_address" name="frm_telnum_bill_address">
					<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
					<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>

                <input type="hidden" id="pass_thru_telephone_num" name="pass_thru_telephone_num" value=""/>
					<input type="hidden" id="referrer_source" name="referrer_source" value="search_phone_number.jsp"/>
					<input type="hidden" id="pass_thru_action" name="pass_thru_action" value="true"/>
					
			</form>
            <form id="frm_process_purchase_transaction" name="frm_process_purchase_transaction">
                <input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
                <input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
                <input type="hidden" id="purchase_transact_telephone_num" name="purchase_transact_telephone_num" value=""/>
            </form>
			<div id="loading_wheel" style="display:none;">
				<img src="/web/img/wheeler.gif">
			</div>
</body>

<script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
<script type="text/javascript">

var  varEventId= '<%=sEventId%>';
var varAdminId = '<%=sAdminId%>';

var varSeatingNumType = '<%=Constants.EVENT_TASK.SEATING.getTask()%>';
var varRsvpNumType = '<%=Constants.EVENT_TASK.RSVP.getTask()%>';

var varTelephoneNumType = '<%=Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER.getTask()%>';

var varIsSignedIn = <%=isSignedIn%>;

var varIsNumberSelectedPreviously = <%=isTelephoneNumberSelectedPreviously%>;

	$(document).ready(function() {

        if(!varIsNumberSelectedPreviously)
        {
            $("#loading_wheel").show();
            loadPhoneNumber();
        } else {
            $("#div_telephony_search").show();
            enablePassThruButton();
        }

        $("#gen_telephony_tel_num").click(genTelephonyTelNum);

        $('#telephony_search').toggle(function() {
            $("#telephony_numbers_gen").slideDown();
        }, function() {
            $("#telephony_numbers_gen").slideUp();
        });
        if(mixpanel!=undefined) {
            mixpanel.track('Shopping Cart Search Phone Number', {'Admin id' : varAdminId, 'Event Id' : varEventId });
        }
	});
	
	function enablePassThruButton() {
		$('#bt_get_pricing_option').bind('click',passthruForm);
	}
	function disablePassThruButton() {
		$('#bt_get_pricing_option').unbind('click');
	}
	
	function passthruForm() {
        if( varIsSignedIn ) {
            $('#purchase_transact_telephone_num').val( $("#telephone_gen_num").text() );

            var actionUrl = "proc_search_phone_number.jsp";
            var methodType = "POST";
            var dataString = $('#frm_process_purchase_transaction').serialize();


            phoneNumberData(actionUrl,dataString,methodType,processPurchaseTransactions);
        }
        else
        {
            $("#frm_telnum_bill_address").attr('action',"/web/com/gs/common/credential.jsp");
            $("#frm_telnum_bill_address").attr('method','POST');
            $("#frm_telnum_bill_address").submit();
        }
	}

    function processPurchaseTransactions(jsonResult)
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
                    var varArrErrorMssg = jsonResponseMessage.error_mssg;
                    displayMssgBoxMessages( varArrErrorMssg , true);
                }
            } else if( jsonResult.status == 'ok' && varResponseObj !=undefined)  {
                $('#pass_thru_telephone_num').val( $("#telephone_gen_num").text() );

                if( varIsSignedIn ) {
                    $("#frm_telnum_bill_address").attr('action','/web/com/gs/event/pricing_plan.jsp');
                    $("#frm_telnum_bill_address").attr('method','POST');


                    $("#frm_telnum_bill_address").submit();
                }
            } else {
                displayMssgBoxMessages( "Please try again later." , true);
            }
        }
    }
	function getCustomSeatingNums() {
		disablePassThruButton();
		searchMoreNumbers(varSeatingNumType,customSeatingNumResult);
	}
	
	function searchMoreNumbers(numType, callbackmethod) {
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = $("#frm_tel_numbers").serialize();
		dataString = dataString + "&num_type="+numType +"&custom_num_gen=true";
		phoneNumberData(actionUrl,dataString,methodType,callbackmethod);
	}
	function loadPhoneNumber() {
		var actionUrl = "proc_load_phone_numbers.jsp";
		var methodType = "POST";
		var dataString = "admin_id="+varAdminId+"&event_id="+varEventId+'&get_new_phone_num=true';
		phoneNumberData(actionUrl,dataString,methodType,displayPhoneNumbers);
	}
	function phoneNumberData(actionUrl,dataString,methodType,callBackMethod) {
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
	function displayPhoneNumbers(jsonResult)
	{
		//alert('status = '+jsonResult.status);
		if(jsonResult!=undefined)
		{
			var varResponseObj = jsonResult.response;
			if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
				
				var varIsMessageExist = varResponseObj.is_message_exist;
				if(varIsMessageExist == true) {
					var jsonResponseMessage = varResponseObj.messages;
					var varArrErrorMssg = jsonResponseMessage.error_mssg;
                    displayMssgBoxMessages( varArrErrorMssg , true);
				}
			} else if( jsonResult.status == 'ok' && varResponseObj !=undefined) {
				var varIsPayloadExist = varResponseObj.is_payload_exist;
				if(varIsPayloadExist == true) {
					var jsonResponseObj = varResponseObj.payload;
					processTelNumbers( jsonResponseObj );
				} else {
                    displayMssgBoxAlert("There were no telephone numbers available for the search parameters.",true);
                }
			} else {
                displayMssgBoxAlert("Please try again later.",true);
			}
		} else {
            displayMssgBoxAlert("Please try again later.",true);
		}
		$("#loading_wheel").hide();
		enablePassThruButton();
	}

	function customSeatingNumResult(jsonResult)
	{
		if(jsonResult!=undefined) {
			var varResponseObj = jsonResult.response;
			if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
				
				var varIsMessageExist = varResponseObj.is_message_exist;
				if(varIsMessageExist == true) {
					var jsonResponseMessage = varResponseObj.messages;
					var varArrErrorMssg = jsonResponseMessage.error_mssg
					displayMssgBoxMessages( varArrErrorMssg , true);
				}
				
			} else if( jsonResult.status == 'ok' && varResponseObj !=undefined) {
				
				var varIsPayloadExist = varResponseObj.is_payload_exist;
                if(mixpanel!=undefined) {
                    mixpanel.track('Shopping Cart Custom Phone Number search', {'Admin id' : varAdminId, 'Event Id' : varEventId });
                }
				//alert('after searching for number - payload exists = ' + varIsPayloadExist);
				if(varIsPayloadExist == true) {
					var jsonResponseObj = varResponseObj.payload;
					processTelNumbers( jsonResponseObj );
				}
			} else {
				displayMssgBoxAlert("Please try again later.",true);
			}
		} else {
			displayMssgBoxAlert("Please try again later.",true);
		}
		enablePassThruButton();
	}
	function processTelNumbers( jsonResponseObj ) {
		var varTelNumbers= jsonResponseObj.telnumbers;

        if(varTelNumbers != undefined ) {
            var totalRows = varTelNumbers.num_of_rows;
            if(totalRows!=undefined) {
                var varTelNumList = varTelNumbers.telnum_array;

                if(varTelNumList!=undefined) {
                    var varIsTelephoneFound = false;
                    var varIsRsvpFound = false;
                    for(var iRow = 0; iRow < totalRows ; iRow++ ) {
                        var telNumBean = varTelNumList[iRow];

                        if( telNumBean.telnum_type ==  varTelephoneNumType ) {
                            $("#div_telephony_search").show();
                            $("#telephone_gen_num").text(telNumBean.human_telnum);
                            varIsTelephoneFound = true;
                        }
                    }
                    if(varIsTelephoneFound==true) {
                        $("#loading_wheel").hide();
                        displayMssgBoxAlert('Success!! Please click on \"Search\" again if you would like a new number.', false);
                    }
                }

            }
        } else {
            displayMssgBoxAlert('We could not find a valid telephone number with the area code provided.', true);
        }

	}
	function displayMssgBoxAlert(varMessage, isError) {
		var varTitle = 'Status';
		var varType = 'info';
		if(isError) {
			varTitle = 'Error';
			varType = 'error';
		} else {
			varTitle = 'Status';	
			varType = 'info';
		}
		
		if(varMessage!='') {
			$.msgBox({
                title: varTitle,
                content: varMessage,
                type: varType
            });
		}
	}
	
	function displayMssgBoxMessages(varArrMessages, isError) {
		if(varArrMessages!=undefined) {
			var varMssg = '';
			var isFirst = true;
			for(var i = 0; i<varArrMessages.length; i++) {
				if(isFirst == false) {
					varMssg = varMssg + '\n';
				}
				varMssg = varMssg + varArrMessages[i].text;
			}
			
			if(varMssg!='') {
                displayMssgBoxAlert(varMssg,isError);
			}
		}
		

	}
    function genTelephonyTelNum() {
        $("#loading_wheel").show();
        disablePassThruButton();
        var actionUrl = "proc_load_phone_numbers.jsp";
        var methodType = "POST";
        var dataString = $("#frm_telephony_numbers").serialize();
        dataString = dataString + '&num_type='+varTelephoneNumType+'&custom_num_gen=true';

        phoneNumberData(actionUrl,dataString,methodType,displayPhoneNumbers);
    }
</script>
<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>