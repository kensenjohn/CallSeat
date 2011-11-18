package com.gs.call;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gs.bean.twilio.IncomingCallBean;

public class IncomingCall extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 431401094530733570L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		performTask(request);

		// response.
	}

	private void performTask(HttpServletRequest request)
	{
		IncomingCallManager incominManager = new IncomingCallManager();

		IncomingCallBean incomingCallBean = incominManager.getIncomingCallRequest(request);
		if (incomingCallBean != null && incomingCallBean.getTo() != null
				&& !"".equalsIgnoreCase(incomingCallBean.getTo()))
		{
			incominManager.processCall(incomingCallBean);
		}

	}
}
