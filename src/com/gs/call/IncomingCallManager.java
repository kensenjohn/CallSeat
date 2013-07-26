package com.gs.call;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.gs.common.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.twilio.ProcessTwilioCalls;
import com.gs.common.Configuration;
import com.gs.common.Constants;

public class IncomingCallManager {

	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);

	private String SELECTED_CALL_SERVICE = applicationConfig
			.get(Constants.PROP_CALL_SERVICE);

	public CallResponse processCall(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();
		if (incomingCallBean != null) {
			ProcessCalls processCalls = null;
			if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(SELECTED_CALL_SERVICE))
            {
				TwilioIncomingCallBean twilioIncominCallBean = (TwilioIncomingCallBean) incomingCallBean;
				processCalls = new ProcessTwilioCalls(twilioIncominCallBean);
			}

			if (processCalls != null) {
				callResponse = processCalls.process();
			}

		}
		return callResponse;
	}

	private TwilioIncomingCallBean getTwilioIncomingCallBean(
			HttpServletRequest httpRequest) {
		TwilioIncomingCallBean twilioIncomingCallBean = new TwilioIncomingCallBean();
		Enumeration allReqParams = httpRequest.getParameterNames();

		if (allReqParams != null) {
			while (allReqParams.hasMoreElements()) {
				String paramKey = (String) allReqParams.nextElement();
				if (paramKey != null && !"".equalsIgnoreCase(paramKey)) {
					String value = ParseUtil.checkNull(httpRequest.getParameter(paramKey));
					if ("callSid".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallid(value);
					} else if ("accountSid".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setAccountid(value);
					} else if ("from".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setFrom(value);
					} else if ("to".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setTo(value);
					} else if ("callStatus".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallStatus(value);
					} else if ("apiVersion".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setApiVersion(value);
					} else if ("direction".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setDirection(value);
					} else if ("forwardFrom".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setForwardFrom(value);
					} else if ("fromCity".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setFromCity(value);
					} else if ("fromState".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setFromState(value);
					} else if ("fromZip".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setFromZip(value);
					} else if ("fromCountry".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setFromCountry(value);
					} else if ("toZip".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setToZip(value);
					} else if ("toCity".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setToCity(value);
					} else if ("toCountry".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setToCity(value);
					} else if ("toState".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setToState(value);
					} else if ("callDuration".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setActualCallDuration(value);
					} else if ("calledState".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCalledState(value);
					} else if ("calledCountry".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCalledCountry(value);
					} else if ("calledCity".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCalledCity(value);
					} else if ("calledZip".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCalledZip(value);
					} else if ("duration".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setBillDuration(value);
					} else if ("caller".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCaller(value);
					} else if ("callerCity".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerCity(value);
					} else if ("callerState".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerState(value);
					} else if ("callerCountry".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerCountry(value);
					} else if ("callerZip".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerZip(value);
					} else if ("callerZip".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerZip(value);
					} else if ("Digits".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setDigits(value);
					} else if ("input_event_num".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerInputEventId(value);
					} else if ("input_secret_key".equalsIgnoreCase(paramKey)) {
						twilioIncomingCallBean.setCallerInputSecretKey(value);
					} else if ("call_attempt".equalsIgnoreCase(paramKey)) {
                        twilioIncomingCallBean.setCallAttemptNumber(ParseUtil.sToI(value));
                    } else if ("call_log_id".equalsIgnoreCase(paramKey)) {
                        twilioIncomingCallBean.setCallLogId(value);
                    }
				}
			}
		}
		appLogging.info(twilioIncomingCallBean.toString());
		return twilioIncomingCallBean;
	}

	public IncomingCallBean getIncomingCallRequest(
			HttpServletRequest httpRequest) {
		IncomingCallBean incomingCallBean = null;

		if (Constants.CALL_SERVICE.TWILIO.getCallService().equalsIgnoreCase(
				SELECTED_CALL_SERVICE)) {
			incomingCallBean = getTwilioIncomingCallBean(httpRequest);
		}
		return incomingCallBean;

	}
}
