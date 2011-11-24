package com.gs.call;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.common.Constants;
import com.twilio.sdk.verbs.TwiMLResponse;

public class IncomingCall extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 431401094530733570L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		CallResponse callResponse = performTask(request);

		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();

		TwiMLResponse twimlResp = callResponse.getResponse();
		out.println(twimlResp.toXML());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		CallResponse callResponse = performTask(request);

		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();

		TwiMLResponse twimlResp = callResponse.getResponse();
		out.println(twimlResp.toXML());
	}

	private CallResponse performTask(HttpServletRequest request)
	{
		CallResponse callResponse = new CallResponse();
		String sCallType = request.getParameter("incoming_call_type");

		IncomingCallManager incominManager = new IncomingCallManager();

		IncomingCallBean incomingCallBean = incominManager.getIncomingCallRequest(request);

		if ("rsvp_ans".equalsIgnoreCase(sCallType))
		{
			if (incomingCallBean != null && incomingCallBean.getTo() != null
					&& !"".equalsIgnoreCase(incomingCallBean.getTo()))
			{
				incomingCallBean.setCallType(Constants.CALL_TYPE.RSVP_DIGIT_RESP);
				callResponse = incominManager.processCall(incomingCallBean);
			}
		} else
		{
			if (incomingCallBean != null && incomingCallBean.getTo() != null
					&& !"".equalsIgnoreCase(incomingCallBean.getTo()))
			{
				if ("rsvp_first_request".equalsIgnoreCase(sCallType))
				{
					incomingCallBean.setCallType(Constants.CALL_TYPE.RSVP_FIRST_REQUEST);
				} else if ("seating_first_request".equalsIgnoreCase(sCallType))
				{
					incomingCallBean.setCallType(Constants.CALL_TYPE.SEATING_FIRST_REQUEST);
				}

				callResponse = incominManager.processCall(incomingCallBean);
			}
		}
		return callResponse;

	}
}
