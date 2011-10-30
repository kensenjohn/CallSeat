<html>
	<body>
		<%
			String sUserName = request.getParameter("username");
			if( sUserName == null )
			{
				sUserName = "";
			}
		%>
		<form method="POST" action="MAIN_ACTION.go">
		Username : <input type="text" id="username" name="username" value="<%=sUserName%>"/><br>
		Password :  <input type="password" id="password" name="password"/><br>
		<input type="Submit" value="Submit"/><br><br>
		</form>
		
	</body>
</html>