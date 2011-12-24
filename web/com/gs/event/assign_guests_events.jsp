<%@ page import="com.gs.common.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<jsp:include page="../common/header_top.jsp"/>
<jsp:include page="../common/security.jsp"/>
<%@include file="../common/header_bottom.jsp"%>
	<body>
<%
		Logger jspLogging = LoggerFactory.getLogger("JspLogging");
		String sEventId = ParseUtil.checkNull(request.getParameter("event_id"));
		String sAdminId = ParseUtil.checkNull(request.getParameter("admin_id"));
		

		String sGuestFirstName = ParseUtil.checkNull(request.getParameter("guest_first_name"));
		
		
		jspLogging.info("Assign to event  for : " + sAdminId);
%>

		<div class="container-filler rounded-corners">
			<div style="padding:20px">
				<h2 class="txt txt_center">Assign <%=sGuestFirstName %> to Events</h2>
			</div>
		</div>
	</body>
				
			