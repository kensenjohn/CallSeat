(function($)
{
	$.fn.tableformatter = function( jsonTableDetails ) 
	{
		
		 return this.each(function() { 
			 //tableformat_function.init(this);
			// json_table_details = jsonTableDetails;
			 
			 var config = $.extend({},$.fn.tableformatter.defaults,jsonTableDetails);
			 
			 json_table_details = config;
			 tableformat_function.init(this);
			 
		 });
			
	};
	
	 $.fn.tableformatter.defaults = 
     {
			varTableDetails: '',
			varDeleteTableURL : '',
			var_event_id : '',
			var_admin_id : ''
     };
	
	var json_table_details = '';
	
	tableformat_function = {
		
		init : function ( div_table_details)
		{
			$(div_table_details).children().detach();
			$(div_table_details).append(this.table_create.create_table());
		},
		table_create :
		{
			create_table : function()
			{
				return '<table cellspacing="1" id="table_details" class="table table-striped"> '+this.create_header()+''+this.create_rows()+'</table>';
				
			},
			create_header : function ()
			{
				var valHeader = '<thead><tr> ' + 
				'<th style="width:30%" >Table Name</th><th style="width:10%" >Number</th>'+
				'<th style="width:20%" >Assigned Seats</th>'+
				'<th style="width:35%" ></th>'
				+'</tr></thead>';
				return valHeader; 
			},
			create_rows : function()
			{
				var numOfRows = json_table_details.varTableDetails.num_of_rows;
				var allTables = json_table_details.varTableDetails.tables;
				var valRows = '';
				var varOddClass = '';
				for( i=0; i<numOfRows; i++ )
				{
					if((i%2)>0)
					{
						varOddClass = 'class="odd"';
					}
					else
					{
						varOddClass = '';
					}
					
					var tmpTable = allTables[i];
					
					if(tmpTable.table_id != '')
					{
						var assignments = '';
						if( tmpTable.num_of_seats > 0 )
						{
							assignments = tmpTable.guest_assigned_seats > 0 ? tmpTable.guest_assigned_seats + ' of ' + tmpTable.num_of_seats : '0' + ' of ' + tmpTable.num_of_seats;
						}
						
						valRows = valRows + '<tr id="table_'+tmpTable.table_id+'" '+varOddClass+'><td class="tbl_td">'+tmpTable.table_name+'</td>'+
											'<td class="tbl_td">'+tmpTable.table_num+'</td>'+
											'<td class="tbl_td">'+ assignments +'</td>'+
											'<td class="tbl_td">'+ this.create_action_urls( tmpTable ) +'</td>' + '</tr>';
					}
					else
					{
						valRows = valRows + '<tr id="table_dummy_row"><td class="tbl_td">Add table for your guests.</td>'+
											'<td class="tbl_td">&nbsp;</td>'+
											'<td class="tbl_td">&nbsp;</td>'+
											'<td class="tbl_td">&nbsp;</td>' + '</tr>';
					}
					
					
				}
				return valRows;
			},
			create_action_urls : function( single_table_detail )
			{
				var actionLinks = '';
				 
				actionLinks = actionLinks + this.create_edit_table( single_table_detail ) + '&nbsp;&nbsp;&nbsp;';
				actionLinks = actionLinks + this.create_guest_table( single_table_detail ) + '&nbsp;&nbsp;&nbsp;';
				actionLinks = actionLinks + this.create_delete_table( single_table_detail );
				
				actionLinks = '<div>' + actionLinks + '</div>';
				
				return actionLinks;
			},
			create_delete_table : function ( single_table_detail )
			{
				var varDeleteLink = '<span id="del_'+single_table_detail.table_id+'">'+
					'<a id="link_del_table_'+single_table_detail.table_id+'" >Delete</a></span>';
				return varDeleteLink;
			},
			create_edit_table :  function ( single_table_detail )
			{
				var varEditLink = '<span id="edit_'+single_table_detail.table_id+'">'+
						'<a id="link_table_'+single_table_detail.table_id+'" '+ 
						' href="/web/com/gs/event/add_table.jsp?table_id='+single_table_detail.table_id+
						'&event_id='+json_table_details.var_event_id+'&admin_id='+json_table_details.var_admin_id+'"> '+
						' Edit Table </a></span>';
				return varEditLink;
			},
			create_guest_table :  function ( single_table_detail )
			{
				var varEditLink = '<span id="guest_'+single_table_detail.table_id+'">' + 
						'<a id="link_guest_'+single_table_detail.table_id+'" '+
						' href="/web/com/gs/event/edit_table_guests.jsp?table_id='+single_table_detail.table_id+
						'&event_id='+json_table_details.var_event_id+'&admin_id='+json_table_details.var_admin_id+'"> ' + 
						'Edit Guests </a></span>';
				return varEditLink;
			}
			
		}
		
	}
	
})(jQuery);