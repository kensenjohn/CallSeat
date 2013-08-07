<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp">
	<jsp:param name="page_title" value="Add/Edit Table"/>	
</jsp:include>
<jsp:include page="../common/security.jsp"/>
<jsp:include page="../common/header_bottom.jsp"/>
<link type="text/css" rel="stylesheet" href="/web/css/jquery.select-to-autocomplete.css" />
<link rel="stylesheet" type="text/css" href="/web/css/msgBoxLight.css" media="screen" >
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
    <jsp:include page="/web/com/gs/common/top_nav_fancybox.jsp"/>
		<div  class="fnbx_scratch_area">
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
						
						<form id="frm_add_table" >
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
                                    <select  id="table_num" name="table_num" >
                                        <option value=""></option>
                                        <%
                                            for(int numOfTables = 1; numOfTables<100; numOfTables++ )
                                            {
                                        %>
                                                <option value="<%=numOfTables%>"><%=numOfTables%></option>
                                        <%
                                            }
                                        %>
                                    </select>
								</div>
							</div>
							<div class="row">
								<div class="span4" >
									Number of Seats :
								</div>
							</div>
							<div class="row">
								<div class="span2" >
                                    <select  id="num_of_seats" name="num_of_seats" >
                                        <option value=""></option>
                                        <%
                                            for(int numOfSeats = 1; numOfSeats<100; numOfSeats++ )
                                            {
                                        %>
                                                <option value="<%=numOfSeats%>"><%=numOfSeats%></option>
                                        <%
                                            }
                                        %>
                                    </select>
								</div>
							</div>
							 <div class="span8">
							  	<div class="row">
							  		<div class="span8">
							  			<input type="button" id="add_table" name="add_table" type="button" class="btn" value="<%=sButtonName %>">
							  		</div>
							   	</div>
							  </div>
							<div class="row">																			
								<div class="span8" >
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
	</body>
    <script type="text/javascript" src="/web/js/jquery.select-to-autocomplete.js"></script>
    <script type="text/javascript" src="/web/js/jquery-ui-1.8.13.custom.min.js"></script>
    <script type="text/javascript" src="/web/js/jquery.msgBox.js"></script>
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

            $('#table_name').live("keypress", function(event) {
                if ( event.which == 13 ) {
                    $('#table_name').blur();
                    enterButtonActivation();
                }
            });

            $('#table_num').live("keypress", function(event) {
                if ( event.which == 13 ) {
                    enterButtonActivation();
                    $('#table_num').blur();
                }
            });

            $('#num_of_seats').live("keypress", function(event) {
                if ( event.which == 13 ) {
                    enterButtonActivation();
                    $('#num_of_seats').blur();
                }
            });
		});

        function enterButtonActivation()
        {
            if(!varIsTableExists)
            {
                addTable();
            }
            else
            {
                loadTableDetails();
                editTable();
            }
        }
		
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
                    displayMssgBoxAlert(varArrMessages[i].text,false);
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
						//displayMessages( varArrErrorMssg );
                        displayMssgBoxMessages( varArrErrorMssg , true);
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


        function displayMssgBoxAlert(varMessage, isError)
        {
            var varTitle = 'Status';
            var varType = 'info';
            if(isError)
            {
                varTitle = 'Error';
                varType = 'error';
            }
            else
            {
                varTitle = 'Status';
                varType = 'info';
            }

            if(varMessage!='')
            {
                $.msgBox({
                    title: varTitle,
                    content: varMessage,
                    type: varType
                });
            }
        }

        function displayMssgBoxMessages(varArrMessages, isError)
        {
            if(varArrMessages!=undefined)
            {


                var varMssg = '';
                var isFirst = true;
                for(var i = 0; i<varArrMessages.length; i++)
                {
                    if(isFirst == false)
                    {
                        varMssg = varMssg + '\n';
                    }
                    varMssg = varMssg + varArrMessages[i].text;
                }

                if(varMssg!='')
                {
                    displayMssgBoxAlert(varMssg,isError);
                }
            }


        }
	</script>
	<jsp:include page="../common/footer_bottom_fancybox.jsp"/> 
</html>
