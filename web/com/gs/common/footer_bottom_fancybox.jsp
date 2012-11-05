<%
com.gs.common.Configuration applicationConfig = com.gs.common.Configuration.getInstance(com.gs.common.Constants.APPLICATION_PROP);

String sCopyrightYear = applicationConfig.get("copyright_year");
String sCopyrightCompany = applicationConfig.get("copyright_company");
%>
<div style="padding:5px;text-align:right;font-size:75%; color:#37291C; margin-right : 30px; clear:both ">
	&copy<%=sCopyrightYear%> - <%=sCopyrightCompany%> 
</div>