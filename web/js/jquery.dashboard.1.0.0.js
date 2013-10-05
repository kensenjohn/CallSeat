(function($)
{
	$.fn.dashboard = function( jsonAllEvents ) {
		
		 return this.each(function() {
			 var config = $.extend({},$.fn.dashboard.defaults,jsonAllEvents);
			 json_dashboard_details = config;
			 dashboardformat_function.init(this);
		 });
	};
	
	 $.fn.dashboard.defaults =   {  varEventList: '' };
	
	var json_dashboard_details = '';
	
	dashboardformat_function = {
		
		init : function ( div_dashboard) {
			$(div_dashboard).children().detach()
			$(div_dashboard).append(this.table_create.create_table())
		},
		table_create : {
			create_table : function() {
				return '<table cellspacing="1"  class="table table-striped" id="dashboard_event_details"> '+this.create_header()+''+this.create_rows()+'</table>';
			},
			create_header : function () {
				var valHeader = '<thead><tr> ' + 
				'<th style="width:20%"  class="tbl_th">Plan Number</th>'+
				'<th style="width:30%" class="tbl_th">Plan Name</th>'+
				'<th style="width:25%" class="tbl_th">When</th>'+
				'<th style="width:25%" class="tbl_th">&nbsp;</th>'+
				'</tr></thead>';
				return valHeader; 
			},
			create_rows : function() {
				var numOfRows = json_dashboard_details.varEventList.num_of_rows;
				var allEvents = json_dashboard_details.varEventList.events;
				var valRows = '';
				var varOddClass = '';
				if(numOfRows <= 0) {
					valRows = valRows + '<tr id="table_dummy_row">' +
					'<td class="tbl_td" colspan="2">Create a New Seating Plan</td>'+
					'<td class="tbl_td">&nbsp;</td>'+
					'<td class="tbl_td">&nbsp;</td>';
				} else {
					for( i=0; i<numOfRows; i++ ) {
						if((i%2)>0) {
							varOddClass = 'class="odd"';
						} else {
							varOddClass = '';
						}

						var tmpEvent = allEvents[i];
						
						if(tmpEvent!=undefined && tmpEvent.event_id != '') {
							valRows = valRows + '<tr id="guest_'+tmpEvent.event_num+'" '+varOddClass+'>' + 
									'<td>' + tmpEvent.event_num + '</td>' + 
									'<td>' + tmpEvent.event_name + '</td>' + 
									'<td>' + tmpEvent.human_event_date + '</td>' +
									'<td>'+ this.create_action_urls( tmpEvent ) +'</td></tr>' ;
						}
					}
				}
				return valRows;
			},
			create_action_urls : function( single_event_detail ) {
				var actionLinks = '';
				 
				actionLinks = actionLinks + this.create_edit_event( single_event_detail ) + '&nbsp;&nbsp;&nbsp;';
				//actionLinks = actionLinks + this.create_delete_guest( single_guest_detail );
				
				actionLinks = '<div class="action_column" style="text-align:center;">' + actionLinks + '</div>';
				
				return actionLinks;
			},
			create_edit_event :  function ( single_event_detail ) {
				var varEditLink = '<span id="edit_'+single_event_detail.event_id+'">'+
					'<a id="link_event_'+single_event_detail.event_id+'" '+ 
					' href="/web/com/gs/event/event_setup.jsp?lobby_event_id='+single_event_detail.event_id+
					'&from_lobby=true&lobby_admin_id='+single_event_detail.event_admin_id+'">'+
					'Edit</a></span>';
				
				return varEditLink;
			},
			create_delete_guest :  function ( single_guest_detail ) {
				var varEditLink = '<span id="del_'+single_guest_detail.guest_id+'">Delete</span>';
				return varEditLink;
			}
			
		}
		
	}
	
})(jQuery);