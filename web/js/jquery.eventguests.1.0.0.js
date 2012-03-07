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
			varEventGuestDetails: ''
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
				return '<table cellspacing="1"  class="bordered-table zebra-striped tbl" id="guest_details"> '+this.create_header()+''+this.create_rows()+'</table>';
			},
			create_header : function ()
			{
				var valHeader = '<thead><tr> ' + 
				'<th style="width:23%"  class="tbl_th">Guest Name</th>'+
				'<th style="width:5%" class="tbl_th">Cell</th>'+
				'<th style="width:6%" class="tbl_th">Invited</th>'+
				'<th style="width:15%" class="tbl_th">RSVP</th>'+
				'<th style="width:25%" class="tbl_th">&nbsp;</th>'+
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
									'<td class="tbl_td">' + tmpGuest.guest_bean.user_info.first_name + ' ' + tmpGuest.guest_bean.user_info.last_name + '</td>' + 
									'<td class="tbl_td">' + tmpGuest.guest_bean.user_info.cell_phone + '</td>' +
									'<td class="tbl_td">' + tmpGuest.total_seats + '</td>' +
									'<td class="tbl_td">' + tmpGuest.rsvp_seats + '</td>' +
									'<td class="tbl_td"> &nbsp; </td>';
						}
						
						
					}
				}
				//alert(valRows);
				return valRows;
			},
			create_action_urls : function( single_guest_detail )
			{
				var actionLinks = '';
				 
				actionLinks = actionLinks + this.create_edit_guest( single_guest_detail ) + '&nbsp;&nbsp;&nbsp;';
				actionLinks = actionLinks + this.create_delete_guest( single_guest_detail );
				
				actionLinks = '<div class="action_column">' + actionLinks + '</div>';
				
				return actionLinks;
			},
			create_edit_guest :  function ( single_guest_detail )
			{
				var varEditLink = '<span id="edit_'+single_guest_detail.guest_id+'">Edit</span>';
				return varEditLink;
			},
			create_delete_guest :  function ( single_guest_detail )
			{
				var varEditLink = '<span id="del_'+single_guest_detail.guest_id+'">Delete</span>';
				return varEditLink;
			}
			
		}
		
	}
	
})(jQuery);