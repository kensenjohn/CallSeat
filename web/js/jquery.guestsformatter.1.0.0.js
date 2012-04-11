(function($)
{
	$.fn.guestformatter = function( jsonGuestDetails ) 
	{
		
		 return this.each(function() { 
			 //guestformat_function.init(this);
			// json_guest_details = jsonGuestDetails;
			 
			 var config = $.extend({},$.fn.guestformatter.defaults,jsonGuestDetails);
			 
			 json_guest_details = config;
			 guestformat_function.init(this);
			 
		 });
			
	};
	
	 $.fn.guestformatter.defaults = 
     {
			varAdminId: '',
			varGuestDetails: '',
			varDeleteTableURL : '',
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
				'<th style="width:23%"  class="tbl_th">First Name</th>'+
				'<th style="width:22%" class="tbl_th">Last Name</th>'+
				'<th style="width:5%" class="tbl_th">Cell</th>'+
				'<th style="width:6%" class="tbl_th">Home</th>'+
				'<th style="width:15%" class="tbl_th">Invited To</th>'+
				'<th style="width:25%" class="tbl_th">&nbsp;</th>'+
				'</tr></thead>';
				return valHeader; 
			},
			create_rows : function()
			{
				var numOfRows = json_guest_details.varGuestDetails.num_of_rows;
				var allGuests = json_guest_details.varGuestDetails.guests;
				//alert(numOfRows);
				var valRows = '';
				var varOddClass = '';
				if(numOfRows <= 0)
				{
					valRows = valRows + '<tr id="table_dummy_row">' +
					'<td class="tbl_td">Add Guests.</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'</tr>';
				}
				else
				{
					for( i=0; i<numOfRows; i++ )
					{
						
						var tmpGuest = allGuests[i];
						
						if(tmpGuest!=undefined && tmpGuest.guest_id != '')
						{
							/*var assignments = '';
							if( tmpTable.num_of_seats > 0 )
							{
								assignments = tmpTable.guest_assigned_seats > 0 ? tmpTable.guest_assigned_seats : '0' + ' of ' + tmpTable.num_of_seats;
							}*/
							
							
							/*valRows = valRows + '<tr id="guest_'+tmpGuest.guest_id+'" '+varOddClass+'><td>'+tmpGuest.table_name+'</td>'+
												'<td>'+tmpTable.table_num+'</td>'+
												'<td>'+ assignments +'</td>'+
												'<td>'+ this.create_action_urls( tmpTable ) +'</td>' + '</tr>';*/
							valRows = valRows + '<tr id="guest_'+tmpGuest.guest_id+'" '+varOddClass+'>' + 
									'<td class="tbl_td">' + tmpGuest.user_info.first_name + '</td>' + 
									'<td class="tbl_td">' + tmpGuest.user_info.last_name + '</td>';
									//'<td>Y</td><td>N</td><td> O events </td>' + 
									//'<td>' + this.create_action_urls( tmpGuest ) + '</td>';
							
							if(tmpGuest.user_info.cell_phone!=undefined && tmpGuest.user_info.cell_phone!='')
							{
								valRows = valRows + '<td class="tbl_td">Y</td>';
							}
							else
							{
								valRows = valRows + '<td class="tbl_td">N</td>';
							}
							
							if(tmpGuest.user_info.phone_num!=undefined && tmpGuest.user_info.phone_num!='')
							{
								valRows = valRows + '<td class="tbl_td">Y</td>';
							}
							else
							{
								valRows = valRows + '<td class="tbl_td">N</td>';
							}
							
							var varEventGuest = json_guest_details.varEventGuestDetails;
							valRows = valRows + '<td class="tbl_td">' + this.create_event_numbers_link(varEventGuest, tmpGuest) + '</td>';
							valRows = valRows + '<td class="tbl_td">' + this.create_action_urls( tmpGuest ) + '</td>';
						}
						
						
					}
				}
				//alert(valRows);
				return valRows;
			},
			create_event_numbers_link : function (varEventGuest , currentViewer)
			{ 
				var varEventMessage = '<span id=\"event_sel_' + currentViewer.guest_id + '\">';
				
				if(varEventGuest!=undefined && currentViewer!= undefined)
				{
					varIndividualGuest = varEventGuest[currentViewer.guest_id];
					
					var varNumOfEventGuests = varIndividualGuest.num_of_event_guest_rows;
					
					var eventText = '';
					if( varIndividualGuest.num_of_event_guest_rows > 1 )
					{
						eventText = 'events';
					}
					else
					{
												
						eventText = 'event';
					}
					varEventMessage = varEventMessage + varNumOfEventGuests + ' ' + eventText ;
				}
				
				varEventMessage = varEventMessage + '</span>';
				
				return varEventMessage;
			},
			create_action_urls : function( single_guest_detail )
			{
				var actionLinks = '';
				 
				actionLinks = actionLinks + this.create_edit_guest( single_guest_detail ) + '&nbsp;&nbsp;&nbsp;&nbsp;';
				actionLinks = actionLinks + this.create_edit_guest_event_assign( single_guest_detail ) + '&nbsp;&nbsp;&nbsp;&nbsp;';
				actionLinks = actionLinks + this.create_delete_guest( single_guest_detail );
				
				actionLinks = '<div class="action_column">' + actionLinks + '</div>';
				
				return actionLinks;
			},
			create_edit_guest :  function ( single_guest_detail )
			{
				var varEditLink = '<span id="edit_'+single_guest_detail.guest_id+'">'+
					'<a id="link_edit_guest_'+single_guest_detail.guest_id+'" '+ 
					' href="/web/com/gs/event/add_guest.jsp?guest_id='+single_guest_detail.guest_id+
					'&admin_id='+json_guest_details.varAdminId+'&all_guest_tab_edit=true&all_guest_tab=true"> '+
					' Edit</a></span>';
				
				return varEditLink;
			},
			create_edit_guest_event_assign : function (single_guest_detail)
			{
				var varEditLink = '<span id="edit_event_assign_'+single_guest_detail.guest_id+'">'+
				'<a id="link_edit_event_assign_'+single_guest_detail.guest_id+'" '+ 
				' href="/web/com/gs/event/assign_guests_events.jsp?guest_id='+single_guest_detail.guest_id+
				'&admin_id='+json_guest_details.varAdminId+
				'&guest_first_name='+single_guest_detail.user_info.first_name+
				'&guest_last_name='+single_guest_detail.user_info.last_name+'&all_guest_tab_edit=true&all_guest_tab=true">'+
				'Invite to events</a></span>';
			
				return varEditLink;
			},
			create_delete_guest :  function ( single_guest_detail )
			{
				var varEditLink = '<span id="del_'+single_guest_detail.guest_id+'"><a id="link_del_'+single_guest_detail.guest_id+'" >Delete</a></span>';
				return varEditLink;
			}
			
		}
		
	}
	
})(jQuery);