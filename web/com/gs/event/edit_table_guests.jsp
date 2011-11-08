<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	
	
  	<link type="text/css" rel="stylesheet" href="/web/css/style.css" /> 
  	   
    <!--[if lte IE 8]>
      <script type="text/javascript" src="/web/js/html5.js"></script>
    <![endif]--> 
    
     <script type="text/javascript" src="/web/js/jquery-1.6.1.js"></script> 
     
	</head>
	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");

		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		String sTableId = ParseUtil.checkNull(request.getParameter("table_id"));
%>
		<div class="box_container rounded-corners fill_box">
			<div style="padding:20px">
				<span class="l_txt" style="padding:10px;" >Assign Guests <span id="table_name" name="table_name" > </span></span>
				<br/>
				<div style="width:100%" >
					<div style="width:30%;float:left;"  ><span class="m_txt">Seats per table : <span id="seat_per_table"></span></span></div>
					<div style="width:10%;float:left;" ><span>&nbsp;</span></div>
					<div  style="width:30%;float:left;"><span class="m_txt">Assigned seats : <span id="assigned_per_table"></span></span></div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	</script>
</html>