(function($)
{
	$.fn.eventguests = function( jsonEventGuestDetails ) 
	{
		
		 return this.each(function() { 
			 
			 var config = $.extend({},$.fn.eventguests.defaults,jsonEventGuestDetails);
			 
			 json_guest_details = config;
			 guestformat_function.init(this);
			 
		 });
			
	};
	
	 $.fn.eventguests.defaults = 
     {
			varEventGuestDetails: '',
			varAdminId : '',
			varEventId : ''
     };
	
	var json_guest_details = '';
	
	guestformat_function = {
		
		init : function ( div_guests_details)
		{
			$(div_guests_details).children().detach()
			$(div_guests_details).append(this.table_create.create_table())
		},
		table_create :
		{
			create_table : function() 
			{
				return '<table cellspacing="1"  class="table table-striped" id="guest_details"> '+this.create_header()+''+this.create_rows()+'</table>';
			},
			create_header : function ()
			{
				var valHeader = '<thead><tr> ' + 
				'<th style="width:23%" >Guest Name</th>'+
				'<th style="width:5%" >Cell</th>'+
				'<th style="width:6%"  >Invited</th>'+
				'<th style="width:15%" >RSVP</th>'+
				'<th style="width:25%" >&nbsp;</th>'+
				'</tr></thead>';
				return valHeader; 
			},
			create_rows : function()
			{
				var numOfRows = json_guest_details.varEventGuestDetails.num_of_event_guest_rows;
				var allGuests = json_guest_details.varEventGuestDetails.event_guests;
				var valRows = '';
				var varOddClass = '';
				if(numOfRows <= 0)
				{
					/*valRows = valRows + '<tr id="table_dummy_row">' +
					'<td class="tbl_td">Add Guests.</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'</tr>';*/
				}
				else
				{
					for( i=0; i<numOfRows; i++ )
					{
						
						var tmpGuest = allGuests[i];
						
						if(tmpGuest!=undefined && tmpGuest.guest_id != '')
						{
							
							valRows = valRows + '<tr id="guest_'+tmpGuest.guest_id+'" '+varOddClass+'>' + 
									'<td  >' + tmpGuest.guest_bean.user_info.first_name + ' ' + tmpGuest.guest_bean.user_info.last_name + '</td>' + 
									'<td >' + tmpGuest.guest_bean.user_info.cell_phone + '</td>' +
									'<td >' + tmpGuest.total_seats + '</td>' +
									'<td >' + tmpGuest.rsvp_seats + '</td>' +
									'<td  >' + this.create_action_urls( tmpGuest ) + ' </td>';
						}
						
						
					}
				}
				//alert(valRows);
				return valRows;
			},
			create_action_urls : function( tmpGuest )
			{
				
				var actionLinks = '';
				 
				actionLinks = actionLinks + this.create_edit_guest( tmpGuest ) + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
				actionLinks = actionLinks + this.create_uninvite_guest( tmpGuest );
				
				actionLinks = '<div class="action_column">' + actionLinks + '</div>';
				
				return actionLinks;
			},
			create_edit_guest :  function ( tmpGuest )
			{
				var varEditLink = '<span id="edit_'+tmpGuest.guest_id+'">'+
				'<a id="link_edit_event_guest_'+tmpGuest.guest_id+'" '+ 
				' href="/web/com/gs/event/add_guest.jsp?guest_id='+tmpGuest.guest_id+
				'&admin_id='+json_guest_details.varAdminId+'&event_id='+json_guest_details.varEventId+'&single_event_guest_edit=true"> '+
				' Edit</a></span>';
				return varEditLink;
			},
			create_uninvite_guest :  function ( tmpGuest )
			{
				var varEditLink = '<span id="del_'+tmpGuest.guest_id+'">'+
				'<a id="link_uninvite_event_guest_'+tmpGuest.guest_id+'" href="#" >'+
				'Uninvite</a>'
				+'</span>';
				return varEditLink;
			}
			
		}
		
	}
	
})(jQuery);