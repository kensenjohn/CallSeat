<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<jsp:include page="../common/header_bottom.jsp"/>
	<body>
	<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sTableId = ParseUtil.checkNull(request.getParameter("table_id"));
		
		jspLogging.info("Add Table for event : " + sEventId + " by : " + sAdminId);
	%>
		<div class="container-filler rounded-corners" >
			<div style="padding:20px">
				<h2 class="txt txt_center">Add a Table</h2>
				<div class="row">
					<div class="span7">
						<form id="frm_add_table" >
							<fieldset>
								<div class="clearfix-tight">
									<label for="table_name">Table Name :</label>
									<div class="input">
										<input type="text" id="table_name" name="table_name"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="table_name">Table Number :</label>
									<div class="input">
										<input type="text" id="table_num" name="table_num"/>
									</div>
								</div>
								<div class="clearfix-tight">
									<label for="table_name">Number of Seats :</label>
									<div class="input">
										<input type="text" id="num_of_seats" name="num_of_seats"/>
									</div>
								</div>	
								<div class="actions">									
						            <button id="add_table" name="add_table" type="button" class="action_button primary small">Add Table</button>
						        </div>							
							</fieldset>
							<input type="hidden" id="event_id" name="event_id" value="<%=sEventId%>"/>
							<input type="hidden" id="admin_id" name="admin_id"  value="<%=sAdminId%>"/>
						</form>
					</div>			
				</div>
				<span id="err_mssg"></span>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			//oadTableData();
			$("#add_table").click(addTable);
		});
		
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
						parent.loadTables();
						parent.$.fancybox.close();
					}
				}
			}
		}
	</script>
	<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>
