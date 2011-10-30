(function($)
{
	$.fn.tableformatter = function( jsonTableDetails ) 
	{
		
		 return this.each(function() { 
			 //tableformat_function.init(this);
			// json_table_details = jsonTableDetails;
			 
			 var config = $.extend({},$.fn.tableformatter.defaults,jsonTableDetails);
			 
			//alert('in init -' + jsonTableDetails.num_of_rows );
			
			 json_table_details = config;
			 tableformat_function.init(this);
			 
		 });
			
	};
	
	 $.fn.tableformatter.defaults = 
     {
			 varTableDetails: ''
     };
	
	var json_table_details = '';
	
	tableformat_function = {
		
		init : function ( div_table_details)
		{
			$(div_table_details).children().detach()
			$(div_table_details).append(this.table_create.create_table())
		},
		table_create :
		{
			create_table : function()
			{
				return '<table cellspacing="1" class="addtabledetail" id="table_details"> '+this.create_header()+''+this.create_rows()+'</table>';
			},
			create_header : function ()
			{
				var valHeader = '<thead><tr> ' + 
				'<th style="width:30%">Table Name</th><th style="width:10%">Number</th>'+
				'<th style="width:20%">Assigned Seats</th>'+
				'<th style="width:35%"></th>'
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
					if(i%2==0)
					{
						varOddClass = '';
					}
					else 
					{
						varOddClass = 'class="odd"';
					}
					var tmpTable = allTables[i];
					
					var assignments = '';
					if( tmpTable.num_of_seats > 0 )
					{
						assignments = tmpTable.guest_assigned_seats > 0 ? tmpTable.guest_assigned_seats : '0' + ' of ' + tmpTable.num_of_seats;
					}
					
					
					valRows = valRows + '<tr '+varOddClass+'><td>'+tmpTable.table_name+'</td>'+
										'<td>'+tmpTable.table_num+'</td>'+
										'<td>'+ assignments + '</td>'+
										'<td> &nbsp; </td>' + '</tr>';
				}
				return valRows;
			}
		}
	}
	
})(jQuery);