<%@ page import="com.gs.manager.*" %>
<%@ page import="com.gs.manager.event.EventManager" %>
<%@ page import="com.gs.bean.*" %>
<%@ page import="com.gs.common.*" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>


<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/header_bottom.jsp"/>

<%
	Logger jspLogging = LoggerFactory.getLogger("JspLogging");

	String sEventDate = ParseUtil.checkNull(request.getParameter("hid_event_date"));
	boolean isFromLanding = ParseUtil.sTob(request.getParameter("from_landing"));
	jspLogging.info("Invoked by landing page : " + isFromLanding);
	String sEventTitle = "New Event";
	
	//UserInfoManager userInfo = new UserInfoManager();	
	//userInfo.createUserInfoBean(new UserInfoBean());
	
	String sAdminUserId = "";
	AdminBean adminBean = new AdminBean();
	EventBean eventBean = new EventBean();	
	if(isFromLanding)
	{
		AdminManager adminManager = new AdminManager();		
		adminBean = adminManager.createAdmin();
		EventManager eventManager = new EventManager();
		eventBean = eventManager.createEvent(adminBean.getAdminId());
		jspLogging.debug("Admin Bean : " + adminBean);
	}
%>
<link rel="stylesheet" type="text/css" href="/web/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/web/css/blue/style.css" media="screen" />

<link href="/web/css/jquery.datepick.css" rel="stylesheet" type="text/css" media="screen"/> 
<body>
   <div class="page_setup">
		<div class="container rounded-corners">
			<jsp:include page="../common/top_nav.jsp"/>
			<div class="main_body">
				<div class="clear_both landing_input">					
					<div>
						<span class="m_b_txt"><%=sEventTitle %></span>
						<span class="m_b_txt" style="float:right;">
							Scheduled For : 
							<input type="text"  class="clearOnClick" id="sched_date" name="sched_date" value="<%= sEventDate %>"> 
						</span> 
									
					</div>
					<jsp:include page="../common/action_nav.jsp">
						<jsp:param name="admin_id" value="<%=adminBean.getAdminId() %>"/>
						<jsp:param name="event_id" value="<%=eventBean.getEventId() %>"/>
					</jsp:include>
				</div>
				<div  class="clear_both" style="width: 100%;  text-align: center;">
				<div  class="clear_both" id="div_table_details">
					
				</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	!window.jQuery && document.write('<script src="/web/js/fancybox/jquery-1.4.3.min.js"><\/script>');
</script>
<script type="text/javascript" src="/web/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="/web/js/jquery.tableformatter.1.0.0.js"></script>
<script type="text/javascript" src="/web/js/jquery.datepick.js"></script> 
<script type="text/javascript">
	var varEventID = '<%=eventBean.getEventId()%>'
	$(document).ready(function() {
		
		$("#sched_date").datepick();
		
		$("#add_table").fancybox({
			'width'				: '75%',
			'height'			: '75%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
		$("#add_guest").fancybox({
			'width'				: '75%',
			'height'			: '75%',
			'autoScale'			: false,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
		
		loadTables();
	});
	
	function loadTables()
	{
		//alert("table lolad");
		
		var dataString = '&event_id='+ varEventID;
		var actionUrl = "proc_load_table.jsp";
		var methodType = "POST";
		
		getAllTablesData(actionUrl,dataString,methodType);
		
	}
	
	function getAllTablesData(actionUrl,dataString,methodType)
	{
		$.ajax({
			  url: actionUrl ,
			  type: methodType ,
			  dataType: "json",
			  data: dataString ,
			  success: getTableGuestResult,
			  error:function(a,b,c)
			  {
				  alert(a.responseText + ' = ' + b + " = " + c);
			  }
			});
	}
	
	var varHashTableId = '';
	var varHashTables = '';
	
	function getTableGuestResult(jsonResult)
	{
		//alert(jsonResult.value);
		
		if(!jsonResult.success)
		{
			var varResponse = jsonResult.response;
			if(varResponse!=undefined)
			{
				var varMessage = varResponse.error_message;
				if(varMessage!=undefined && varMessage!= '' )
				{
					$("#err_mssg").text(varMessage);
				}
			}
			
		}
		else
		{
			//alert(jsonResult.success);
			
			var tableDetails = jsonResult.table_detail;
			
			if(tableDetails!=undefined)
			{
				var numOfRows = tableDetails.num_of_rows;
				//alert( numOfRows); 
				var allTables = tableDetails.tables;
				
				//createTableDetails(allTables ,  numOfRows);
				
				/*$("#table_details").tablesorter({ 
			        // pass the headers argument and assing a object 
			        headers: { 
			            // assign the secound column (we start counting zero) 
			            0: { 
			                // disable it by setting the property sorter to false 
			                sorter: false 
			            }, 
			            // assign the third column (we start counting zero) 
			            1: { 
			                // disable it by setting the property sorter to false 
			                sorter: false 
			            } 
			        } 
			    });*/
			    
				$("#div_table_details").tableformatter({
					varTableDetails : tableDetails
				});
				
			}
			
		}
	}
	function createTableDetails(allTables,numOfRows)
	{
		var tableDetail = '<table cellspacing="1" class="addtabledetail" id="table_details"> '+tableDetailHeader()+''+tableDetailRows(allTables,numOfRows)+'</table>';
		$("#div_table_details").replaceWith(tableDetail);
	}
	
	function tableDetailRows(allTables,numOfRows )
	{
		var valRows = '';
		for( i=0; i<numOfRows; i++ )
		{
			var tmpTable = allTables[i];
			valRows = valRows + '<tr><td>'+tmpTable.table_name+'</td>'+
								'<td>'+tmpTable.table_num+'</td></tr>';
		}
		return valRows;
	}
	function tableDetailHeader()
	{
		/*
		    <thead>> 
		        <tr> 
		            <th class="{sorter: false}">first name</th> 
		            <th>last name</th> 
		            <th>age</th> 
		            <th>total</th> 
		            <th class="{sorter: false}">discount</th> 
		            <th>date</th> 
		        </tr> 
		    </thead>
		*/
		
		var valHeader = '<thead><tr> ' + 
							'<th style="width:30%">Table Name</th><th style="width:10%">Number</th>'+
							'<th style="width:20%">Assigned Seats</th>'+
							'<th style="width:35%"></th>'
						+'</tr></thead>';
		return valHeader;
	}
	
</script>
<jsp:include page="../common/footer_top.jsp"/>
<jsp:include page="../common/footer_bottom.jsp"/>
