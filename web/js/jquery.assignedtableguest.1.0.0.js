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
			var_table_id : '',
			varCurrentTable : ''
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
					return '<table  cellspacing="1"   class="table table-striped" id="assigned_guests"> '+this.create_header()+''+this.create_rows()+'</table>';
				},
				create_header : function ()
				{
					var valHeader = '<thead><tr> ' + 
					'<th style="width:20%">Guest Name</th><th style="width:10%">RSVP</th>'+ 
					'<th style="width:20%">Assigned at this table</th>'+
					'<th style="width:10%">Unassigned</th>'+
					'<th style="width:20%">Assigned at other tables</th>'+
					'<th style="width:20%"></th>'+
					'</tr></thead>';
					return valHeader; 
				},
				create_rows : function()
				{
					var tmpVarAssignedGuest = json_assigned_guests.varAssignedGuest;
					var numOfRows = tmpVarAssignedGuest.num_of_rows;
					var allTables = tmpVarAssignedGuest.guests;
					var valRows = '';
					
					var tmpCurrentTable = json_assigned_guests.varCurrentTable;
					if(numOfRows==undefined || numOfRows<=0)
					{
						valRows = valRows + '<tr id="table_dummy_row"><td>Assign guests.</td>'+
						'<td>&nbsp;</td>'+
						'<td>&nbsp;</td>'+
						'<td>&nbsp;</td>' + 
						'<td>&nbsp;</td>' +
						'<td>&nbsp;</td>' +'</tr>';
						
						$("#vacant_per_table").text(tmpCurrentTable.num_of_seats);
					}
					else
					{
						var tmpAllTable = 	json_assigned_guests.varAllTableGuests;
						
						var numOfRows = tmpAllTable.num_of_rows;
						var varTables = tmpAllTable.tables;
						
						var varTotalAssignedPerTable = 0;
						var varVacantSeatsPerTable = 0;
						for( var i =0 ; i < numOfRows ; i++)
						{
							if((i%2)>0)
							{
								varOddClass = 'class="odd"';
							}
							else
							{
								varOddClass = '';
							}
							
							var tmpTableGuest = varTables[i];
							
							if(tmpTableGuest!=undefined && tmpTableGuest.table_guest_id != ''
										&& tmpTableGuest.table_id == json_assigned_guests.var_table_id )
							{
								var tmpAllAssignedGuests = 	json_assigned_guests.varAssignedGuest;
								var numOfAssigGuestRows = tmpAllAssignedGuests.num_of_rows;
								
								var varAssignedAtOtherTable = 0;
								for( j = 0; j<numOfRows; j++)
								{
									if(json_assigned_guests.var_table_id != varTables[j].table_id && tmpTableGuest.guest_id == varTables[j].guest_id )
									{
										varAssignedAtOtherTable = eval(varAssignedAtOtherTable) + eval(varTables[j].guest_assigned_seats);

									}
								}	
								var varUnassignedSeats = eval(tmpTableGuest.guest_rsvp_num) - ( eval(tmpTableGuest.guest_assigned_seats) + eval(varAssignedAtOtherTable));
								valRows = valRows + '<tr id="table_dummy_row" style="padding-top:6px">' +
								'<td style="padding-top:15px">'+tmpTableGuest.guest_first_name+' '+tmpTableGuest.guest_last_name+'</td>'+
								'<td style="padding-top:15px">'+tmpTableGuest.guest_rsvp_num+'</td>'+
								'<td style="padding-top:10px"><input type="text" class="ispn2" id="assign_guest_'+tmpTableGuest.guest_id+'"' + 
								' name ="assign_guest_'+tmpTableGuest.guest_id+'" value="'+tmpTableGuest.guest_assigned_seats+'"></td>'+
								'<td style="padding-top:15px">'+varUnassignedSeats+'</td>' + 
								'<td style="padding-top:15px">'+varAssignedAtOtherTable+'</td>' +
								'<td><button class="btn btn-small" id="table_guest_'+tmpTableGuest.guest_id+'">Update</button>&nbsp;&nbsp;&nbsp;&nbsp;'+
								'<button class="btn btn-small" id="delete_table_guest_'+tmpTableGuest.guest_id+'">Delete Assignment</button></td>' +'</tr>';
								
								varTotalAssignedPerTable = eval(varTotalAssignedPerTable) + eval(tmpTableGuest.guest_assigned_seats);
							}
							
						}
						
						$("#vacant_per_table").text(tmpCurrentTable.num_of_seats - varTotalAssignedPerTable);
						
						
					}
					return valRows;
				}
			}
	}
	
})(jQuery);