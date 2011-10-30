package com.gs.twilio;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.twiml.Response;
import com.gs.common.Constants;
import com.gs.common.DateSupport;
import com.gs.common.Utility;

public class IncomingCallManager
{

	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public String processCall(IncomingCallBean incomingCallBean)
	{
		String sResponse = "";
		if (incomingCallBean != null)
		{
			if (Constants.TWILIO_CALL_STATUS.IN_PROGRESS.getCallStatus().equalsIgnoreCase(
					incomingCallBean.getCallStatus()))
			{
				Response response = new Response();
				response.setSay("Hello, You are calling from "
						+ Utility.convertTelNumToDigits(incomingCallBean.getFrom())
						+ ". The time is now " + DateSupport.getEpochMillis());
				try
				{
					sResponse = Utility.getXmlFromBean(Response.class, response);
				} catch (JAXBException e)
				{
					appLogging.error("There was a telephony error.");
				}
			}
		}
		return sResponse;
	}

	public static IncomingCallBean getIncomingCallRequest(HttpServletRequest httpRequest)
	{
		IncomingCallBean incomingCallBean = new IncomingCallBean();
		Enumeration allReqParams = httpRequest.getParameterNames();

		if (allReqParams != null)
		{
			while (allReqParams.hasMoreElements())
			{
				String paramKey = (String) allReqParams.nextElement();
				if (paramKey != null && !"".equalsIgnoreCase(paramKey))
				{
					String value = httpRequest.getParameter(paramKey);
					if ("callSid".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallSid(value);
					} else if ("accountSid".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setAccountSid(value);
					} else if ("from".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setFrom(value);
					} else if ("to".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setTo(value);
					} else if ("callStatus".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallStatus(value);
					} else if ("apiVersion".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setApiVersion(value);
					} else if ("direction".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setDirection(value);
					} else if ("forwardFrom".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setForwardFrom(value);
					} else if ("fromCity".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setFromCity(value);
					} else if ("fromState".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setFromState(value);
					} else if ("fromZip".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setFromZip(value);
					} else if ("fromCountry".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setFromCountry(value);
					} else if ("toZip".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setToZip(value);
					} else if ("toCity".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setToCity(value);
					} else if ("toCountry".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setToCity(value);
					} else if ("toState".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setToState(value);
					} else if ("callDuration".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallDuration(value);
					} else if ("calledState".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCalledState(value);
					} else if ("calledCountry".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCalledCountry(value);
					} else if ("calledCity".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCalledCity(value);
					} else if ("calledZip".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCalledZip(value);
					} else if ("duration".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setDuration(value);
					} else if ("caller".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCaller(value);
					} else if ("callerCity".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallerCity(value);
					} else if ("callerState".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallerState(value);
					} else if ("callerCountry".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallerCountry(value);
					} else if ("callerZip".equalsIgnoreCase(paramKey))
					{
						incomingCallBean.setCallerZip(value);
					}
				}

			}
		}

		return incomingCallBean;

	}
}
