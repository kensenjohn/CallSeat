var varCredEventID = '';
function credentialSuccess(jsonResponse,varSource)
{	
	$("#login_name_display").text(jsonResponse.first_name);
	$("#login_name_display").addClass("bold_text");
	$('#lobby_admin_id').val(jsonResponse.user_id);
	
	$("#li_user_my_account").show();
	$("#login_user_account").removeAttr("href");
	$("#login_user_account").attr('href','/web/com/gs/event/host_dashboard.jsp?lobby_admin_id='+jsonResponse.user_id);
	
	
	$("#li_user_sign_out").show();
	$("#sign_out_user").removeAttr("href");
	$("#sign_out_user").attr('href','/web/com/gs/common/sign_out.jsp?admin_id='+jsonResponse.user_id);
	
	
	$('#div_goto_lobby').show();
	$('#link_goto_lobby').removeAttr("href");
	$('#link_goto_lobby').attr('href','/web/com/gs/event/host_dashboard.jsp?lobby_admin_id='+jsonResponse.user_id);
	
	if(varSource == 'event_setup.jsp' || varSource == 'guest_setup.jsp')
	{
		//phoneNumTab();
		resetAdminId(jsonResponse.user_id);
		varIsSignedIn = true;
	}
	
	if(varSource == 'guest_setup.jsp')
	{
		loadGuests(varCredEventID,jsonResponse.user_id);
	}
	
}


function resetAdminId(tmpAdminId)
{
	varAdminID = tmpAdminId;
	setNewEventClick();
	setLobbyButtonClick();
	setAllGuestButtonClick();
}

// This should be acopied everywhere.
function setNewEventClick()
{
	$("#lnk_new_event_id").unbind("click");
	$("#lnk_new_event_id").click(function() 
	{
		$("#frm_lobby_tab").attr("action" , "/web/com/gs/event/event_setup.jsp");
		$("#lobby_create_new").val(true);
		$("#lobby_admin_id").val(varAdminID);
		
		$("#frm_lobby_tab").submit();
	});
}
// This should be acopied everywhere.
function setAllGuestButtonClick()
{
	$("#lnk_guest_id").unbind("click");
	$("#lnk_guest_id").click(function() 
	{
		$("#frm_lobby_tab").attr("action" , "/web/com/gs/event/guest_setup.jsp");
		$("#lobby_event_id").val(varCredEventID);
		$("#lobby_admin_id").val(varAdminID);
		
		$("#frm_lobby_tab").submit();
	});
}

function setLobbyButtonClick()
{
	$("#lnk_dashboard_id").unbind("click");
	
	$("#lnk_dashboard_id").click(function() {
		$("#frm_lobby_tab").attr("action" , "/web/com/gs/event/host_dashboard.jsp");
		$("#lobby_event_id").val(varCredEventID);
		$("#lobby_admin_id").val(varAdminID);
		$("#frm_lobby_tab").submit();
	});
	//alert( 'lobey_event_id = ' + $("#lobby_event_id").val() );
}

function setCredentialEventId(varEventId)
{
	varCredEventID = varEventId;
}

function setTopNavLogin(varAdminId, varEventId, varSource)
{
	$("#login_name_display").attr("href","/web/com/gs/common/credential.jsp?admin_id="+varAdminId
			+"&event_id="+varEventId+"&referrer_source="+varSource);
}