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
				return '<table cellspacing="1" class="addtabledetail" id="guest_details"> '+this.create_header()+''+this.create_rows()+'</table>';
			},
			create_header : function ()
			{
				var valHeader = '<thead><tr> ' + 
				'<th style="width:23%">First Name</th>'+
				'<th style="width:22%">Last Name</th>'+
				'<th style="width:5%">Cell</th>'+
				'<th style="width:6%">Home</th>'+
				'<th style="width:15%">Invited To</th>'+
				'<th style="width:25%">&nbsp;</th>'+
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
					'<td>Add Guests.</td>'+
					'<td>&nbsp;</td>'+
					'<td>&nbsp;</td>'+
					'<td>&nbsp;</td>'+
					'<td>&nbsp;</td>'+
					'<td>&nbsp;</td>'+
					'</tr>';
				}
				else
				{
					for( i=0; i<numOfRows; i++ )
					{
						if(i%2==0)
						{
							varOddClass = '';
						}
						else 
						{
							varOddClass = 'class="odd"';
						}
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
									'<td>' + tmpGuest.user_info.first_name + '</td>' + 
									'<td>' + tmpGuest.user_info.last_name + '</td>';
									//'<td>Y</td><td>N</td><td> O events </td>' + 
									//'<td>' + this.create_action_urls( tmpGuest ) + '</td>';
							
							if(tmpGuest.user_info.cell_phone!=undefined && tmpGuest.user_info.cell_phone!='')
							{
								valRows = valRows + '<td>Y</td>';
							}
							else
							{
								valRows = valRows + '<td>N</td>';
							}
							
							if(tmpGuest.user_info.phone_num!=undefined && tmpGuest.user_info.phone_num!='')
							{
								valRows = valRows + '<td>Y</td>';
							}
							else
							{
								valRows = valRows + '<td>N</td>';
							}
							
							var varEventGuest = json_guest_details.varEventGuestDetails;
							valRows = valRows + '<td>' + this.create_event_numbers_link(varEventGuest, tmpGuest) + '</td>';
							valRows = valRows + '<td>' + this.create_action_urls( tmpGuest ) + '</td>';
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