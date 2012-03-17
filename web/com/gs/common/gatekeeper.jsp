<%

	if(!isSignedIn)
	{
		response.sendRedirect("/web/com/gs/common/error/error.jsp");
		return;
	}
%>