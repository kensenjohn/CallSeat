package com.gs.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.common.Constants;

public class StartupServlet extends HttpServlet
{
	private static final Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		performBootSequence(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		performBootSequence(request, response);
	}

	public void performBootSequence(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

	}
}
