<%@page import="com.gs.common.*"%>
<%@page import="com.gs.bean.*"%>
<%@page import="com.gs.manager.*"%>
<%@page import="java.util.*"%>  
    <!--[if lte IE 8]>
      <script type="text/javascript" src="/web/js/html5.js"></script>
    <![endif]--> 
    
     <script type="text/javascript" src="/web/js/jquery-1.6.1.js"></script> 
     
</head>

<%
	boolean isFromCookie = ParseUtil.sTob(request.getParameter("from_cookie"));
	boolean isSignedIn = false;
	HttpSession  reqSession = request.getSession(false);
	if(reqSession!=null)
	{
		AdminBean adminBean = (AdminBean) reqSession.getAttribute(Constants.USER_SESSION);
		
		
		String sFirstName = "";
		if( adminBean!=null && adminBean.isAdminExists() )
		{
			isSignedIn = true;
		}
		
		if(isFromCookie && !isSignedIn)
		{
			String sUserId = request.getParameter("u_id");
			
			if(sUserId!=null && !"".equalsIgnoreCase(sUserId))
			{
				 AdminManager adminManager = new AdminManager();
				 adminBean = adminManager.getAdmin(sUserId);
			}
		}
		
		if( adminBean!=null && adminBean.isAdminExists() )
		{
			isSignedIn = true;
			
			
			reqSession.setAttribute(Constants.USER_SESSION,adminBean);
		}
	}

%>
