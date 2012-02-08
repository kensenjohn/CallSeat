(function($)
{
	$.fn.assignedtableguest = function( jsonAssignedGuests ) 
	{
		return this.each(function() { 
			var config = $.extend({},$.fn.assignedtableguest.defaults,jsonAssignedGuests);
			 
			json_assigned_guests = config;
			assignedguests_function.init(this); 
		});
	};
	
	$.fn.assignedtableguest.defaults = 
    {
			varAssignedGuest: '',
			varAllTableGuests : '',
			var_event_id : '',
			var_admin_id : '',
			var_table_id : ''
    };
	
	var json_assigned_guests = '';
		
	assignedguests_function = {
			init : function ( div_assigned_guests_table)
			{
				$(div_assigned_guests_table).children().detach();
				$(div_assigned_guests_table).append(this.table_create.create_table());
			},
			table_create :
			{
				create_table : function()
				{
					return '<table  cellspacing="1"  class="bordered-table zebra-striped tbl" id="assigned_guests"> '+this.create_header()+''+this.create_rows()+'</table>';
				},
				create_header : function ()
				{
					var valHeader = '<thead><tr> ' + 
					'<th style="width:15%">First Name</th><th style="width:15%">Last Name</th>'+
					'<th style="width:10%">RSVP Seats</th>'+
					'<th style="width:15%">Assigned Seats</th>'+
					'<th style="width:10%">Cell Phone</th>'+
					'<th style="width:12%">Home Phone</th>'+
					'<th style="width:23%">&nbsp;</th>'
					+'</tr></thead>';
					return valHeader; 
				},
				create_rows : function()
				{
					var tmpVarAssignedGuest = json_assigned_guests.varAssignedGuest;
					var numOfRows = tmpVarAssignedGuest.num_of_rows;
					var allTables = tmpVarAssignedGuest.guests;
					var valRows = '';
					if(numOfRows==undefined || numOfRows<=0)
					{
						valRows = valRows + '<tr id="table_dummy_row"><td>Assign guests.</td>'+
						'<td>&nbsp;</td>'+
						'<td>&nbsp;</td>'+
						'<td>&nbsp;</td>' + 
						'<td>&nbsp;</td>' +
						'<td>&nbsp;</td>' +
						'<td>&nbsp;</td>' +'</tr>';
					}
					else
					{
						var tmpAllTable = 	json_assigned_guests.varAllTableGuests;
						
						var numOfRows = tmpAllTable.num_of_rows;
						var varTables = tmpAllTable.tables;
						
						var varTotalAssignedPerTable = 0;
						for( var i =0 ; i < numOfRows ; i++)
						{
							if(i%2==0)
							{
								varOddClass = '';
							}
							else 
							{
								varOddClass = 'class="odd"';
							}
							var tmpTableGuest = varTables[i];
							
							if(tmpTableGuest!=undefined && tmpTableGuest.table_guest_id != ''
										&& tmpTableGuest.table_id == json_assigned_guests.var_table_id )
							{
								valRows = valRows + '<tr id="table_dummy_row">' +
								'<td>'+tmpTableGuest.guest_first_name+'</td>'+
								'<td>'+tmpTableGuest.guest_last_name+'</td>'+
								'<td>'+tmpTableGuest.guest_rsvp_num+'</td>'+
								'<td>'+tmpTableGuest.guest_assigned_seats+'</td>' + 
								'<td>'+tmpTableGuest.guest_cell_phone+'</td>' +
								'<td>'+tmpTableGuest.guest_phone_num+'</td>' +
								'<td>&nbsp;</td>' +'</tr>';
								
								varTotalAssignedPerTable = eval(varTotalAssignedPerTable) + eval(tmpTableGuest.guest_assigned_seats);
							}
							
						}
						
						$("#assigned_per_table").text(varTotalAssignedPerTable);
						
						
					}
					return valRows;
				}
			}
	}
	
})(jQuery);