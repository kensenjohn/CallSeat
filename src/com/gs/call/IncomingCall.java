package com.gs.call;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gs.bean.CallTransactionBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.common.CallTransaction;
import com.gs.common.Constants;
import com.gs.common.Utility;
import com.gs.common.exception.ExceptionHandler;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncomingCall extends HttpServlet
{
    Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    Logger telephonyLogging = LoggerFactory.getLogger(Constants.TELEPHONY_LOGS);
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
        try
        {

            String sCallType = request.getParameter("incoming_call_type");
            telephonyLogging.info("Incoming Call Type " + sCallType);

            IncomingCallManager incominManager = new IncomingCallManager();

            IncomingCallBean incomingCallBean = incominManager.getIncomingCallRequest(request);

            CallTransaction callTransaction = CallTransaction.getInstance();
            if ("rsvp_ans".equalsIgnoreCase(sCallType))
            {
                if (incomingCallBean != null && !Utility.isNullOrEmpty(incomingCallBean.getTo()) )
                {
                    int iNumOfAttempts = incomingCallBean.getCallAttemptNumber();
                    telephonyLogging.info(" RSVP - for rsvp ans iNumOfAttempts " + iNumOfAttempts);
                    if (iNumOfAttempts <= 6) {
                        incomingCallBean.setCallType(Constants.CALL_TYPE.RSVP_DIGIT_RESP);
                    } else {
                        incomingCallBean.setCallType(Constants.CALL_TYPE.ERROR_HANGUP);
                    }
                    callResponse = incominManager.processCall(incomingCallBean);
                }
            } else if ("end_call".equalsIgnoreCase(sCallType)) {
                CallTransactionBean callTransactionBean = new CallTransactionBean();
                callTransaction.updateTransaction(incomingCallBean,callTransactionBean );
            } else if ("call_fail".equalsIgnoreCase(sCallType)) {
                telephonyLogging.info(" RSVP - Call Failed : " + incomingCallBean);
            }  else {
                if (incomingCallBean != null  && !Utility.isNullOrEmpty(incomingCallBean.getTo()) )  {
                    incomingCallBean.setCallType(Constants.CALL_TYPE.FIRST_REQUEST);
                    callTransaction.createTransaction(incomingCallBean);
                    callResponse = incominManager.processCall(incomingCallBean);
                }
            }
        }  catch(Exception e)  {
            appLogging.error("RSVP - Exception while Incoming Call Request\n" + e.getMessage() + "\n" + ExceptionHandler.getStackTrace(e));
        }
		return callResponse;

	}
}
