<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<jsp:include page="../common/header_bottom.jsp"/>
	<body style="height:auto;">
	<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sTableId = ParseUtil.checkNull(request.getParameter("table_id"));
		
		boolean isTableExists = false;
		if(sTableId!=null && !"".equalsIgnoreCase(sTableId))
		{
			isTableExists = true;
		}
		String sTitle = "Add a Table";
		if(isTableExists)
		{
			sTitle = "Edit Table";
		}
		
		String sButtonName = "Add Table";
		if(isTableExists)
		{
			sButtonName = "Save Changes";
		}
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
	%>
		<div class="navbar" style="background-image: none; background-color: RGBA(0,132,0,0.40); padding-bottom:6px; height: 49px;" >
			<div  style="padding-top:5px;">
				<div class="logo span4"><a href="#">CallSeat</a></div>
			</div>
		</div>
		<div  class="fnbx_scratch_area">
			<div style="padding:20px">
				<div class="row" >
					<div class="offset1 span6">
						<div class="row">
							<div class="span6">
								<h2 class="txt txt_center"><%=sTitle%> &nbsp; <span id='edit_table_name'></span></h2>
							</div>
						</div>
						<div class="row">
							<div class="span6">
								&nbsp;
							</div>
						</div>
						
						<form id="frm_login" >
							<div class="row">
								<div class="span6">
									Table Name :
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									<input type="text" id="table_name" name="table_name"/>
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									Table Number :
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									<input type="text" id="table_num" name="table_num"/>
								</div>
							</div>
							<div class="row">
								<div class="span4" >
									Number of Seats :
								</div>
							</div>
							<div class="row">
								<div class="span2" >
									<input type="text" id="num_of_seats" name="num_of_seats"/>
								</div>
							</div>
							<div class="row">								
								<div class="span2" >
									<input type="button" id="add_table" name="add_table" type="button" class="btn" value="<%=sButtonName %>">
								</div>
																			
								<div class="span2" >
									<span id="err_mssg"></span>
								</div>
							</div>
							
							<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
							<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
							<input type="hidden" id="table_id" name="table_id"  value="<%=sTableId%>"/>
						</form>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var varIsTableExists = <%=isTableExists%>;
		$(document).ready(function() {
			//oadTableData();
			if(!varIsTableExists)
			{
				$("#add_table").click(addTable);
			}
			else
			{
				loadTableDetails();
				$("#add_table").click(editTable);
			}
			
		});
		
		function loadTableDetails()
		{
			var dataString = $("#frm_add_table").serialize();
			var actionUrl = "proc_load_table.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&load_single_table=y';
			submitTableData(actionUrl,dataString,methodType,getResult);
		}
		
		function editTable()
		{
			var dataString = $("#frm_add_table").serialize();
			var actionUrl = "proc_add_table.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y&edit_table=y';
			submitTableData(actionUrl,dataString,methodType,getResult);
		}
		
		function addTable()
		{
			var dataString = $("#frm_add_table").serialize();
			var actionUrl = "proc_add_table.jsp";
			var methodType = "POST";
			
			dataString = dataString + '&save_data=y';
			submitTableData(actionUrl,dataString,methodType,getResult);
		}
		
		function submitTableData(actionUrl,dataString,methodType, callBackMethod)
		{
			$.ajax({
				  url: actionUrl,
				  type: methodType ,
				  dataType: "json",
				  data: dataString ,
				  success: callBackMethod,
				  error:function(a,b,c)
				  {
					  alert(a.responseText + ' = ' + b + " = " + c);
				  }
				});
		}
		
		function displayMessages(varArrMessages)
		{
			if(varArrMessages!=undefined)
			{
				for(var i = 0; i<varArrMessages.length; i++)
				{
					alert( varArrMessages[i].text );
				}
			}
		}
		
		function getResult(jsonResult)
		{
			//alert(jsonResult.value);
			if(jsonResult!=undefined)
			{
				var varResponseObj = jsonResult.response;
				if(jsonResult.status == 'error'  && varResponseObj !=undefined )
				{
					var varIsMessageExist = varResponseObj.is_message_exist;
					if(varIsMessageExist == true)
					{
						var jsonResponseMessage = varResponseObj.messages;
						var varArrErrorMssg = jsonResponseMessage.error_mssg
						displayMessages( varArrErrorMssg );
					}
				}
				else if( jsonResult.status == 'ok' && varResponseObj !=undefined)
				{
					var varIsPayloadExist = varResponseObj.is_payload_exist;
					
					if(varIsPayloadExist == true)
					{
						var varPayload = varResponseObj.payload;
						
						if(varPayload.load_table == true)
						{
							var varTableDetail = varPayload.table_detail;
							displayTableDetail(varTableDetail);
						}
						else
						{
							parent.loadTables();
							parent.$.fancybox.close();
						}
						
					}
				}
			}
		}
		
		function displayTableDetail(varTableDetail)
		{
			$('#table_name').val( varTableDetail.table_name);
			$('#table_num').val( varTableDetail.table_num);
			$('#num_of_seats').val( varTableDetail.num_of_seats);
		}
	</script>
</html>
