package com.gs.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		processRequest( request , response );
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		processRequest( request , response );
	}
	
	private void processRequest( HttpServletRequest request, HttpServletResponse response ) throws  ServletException, IOException
	{
		String sUsername = request.getParameter("username");
		System.out.println("Username = " +sUsername);
		
		TestHome testHome = new TestHome();
		System.out.println(testHome.getSomething());

		//response.sendRedirect("web/com/gs/login.jsp?username="+testHome.getSomething());
		this.getServletContext().getRequestDispatcher("/web/com/gs/login.jsp?username="+testHome.getSomething()).forward(request, response);
		System.out.println("Called after forward");
	}
}
