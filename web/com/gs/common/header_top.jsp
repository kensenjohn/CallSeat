<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <link rel="icon"  type="image/png" href="/web/img/favicon.png">
  	<link type="text/css" rel="stylesheet" href="/web/css/style.css" /> 
<%
	String sPageTitle =  com.gs.common.ParseUtil.checkNull(request.getParameter("page_title"));

	if(!"".equals(sPageTitle))
	{
%>
		<title><%=sPageTitle%></title>
<%
	}
%>