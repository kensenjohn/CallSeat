package com.gs.call.twilio.twiml;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TelNumberBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

public class TwimlSupport {

	private static final String DEMO_ENTRY_SERVLET = "DemoIncomingCall";
    private static final String PREMIUM_ENTRY_SERVLET = "IncomingCall";
	private static Logger appLogging = LoggerFactory.getLogger("AppLogging");

	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);
	private static String VOICE_ACTOR = applicationConfig
			.get(Constants.PROP_TWILIO_VOICE);

	public static StringBuilder buildURL(
			TwilioIncomingCallBean twilioIncomingBean,
			Constants.CALL_TYPE callType) {

		StringBuilder strURl = new StringBuilder();

		switch (callType) {
		case DEMO_GATHER_EVENT_NUM:
			strURl.append("/").append(DEMO_ENTRY_SERVLET).append("?")
					.append("incoming_call_type=");
			strURl.append("demo_get_event_num&amp;").append("call_attempt=")
					.append(twilioIncomingBean.getCallAttemptNumber() + 1);
			break;
		case DEMO_GATHER_SECRET_KEY:
			strURl.append("/").append(DEMO_ENTRY_SERVLET).append("?")
					.append("incoming_call_type=");
			strURl.append("demo_get_secret_key&amp;").append("call_attempt=")
					.append(twilioIncomingBean.getCallAttemptNumber() + 1)
					.append("&amp;");
			strURl.append("input_event_num=").append(
					twilioIncomingBean.getCallerInputEventId());
			break;
		case DEMO_GATHER_RSVP_NUM:
			strURl.append("/").append(DEMO_ENTRY_SERVLET).append("?")
					.append("incoming_call_type=");
			strURl.append("demo_rsvp_ans&amp;").append("call_attempt=")
					.append(twilioIncomingBean.getCallAttemptNumber() + 1)
					.append("&amp;");
			strURl.append("input_event_num=")
					.append(twilioIncomingBean.getCallerInputEventId())
					.append("&amp;");
			strURl.append("input_secret_key=").append(
					twilioIncomingBean.getCallerInputSecretKey());
			break;
        case RSVP_DIGIT_RESP:
            strURl.append("/").append(PREMIUM_ENTRY_SERVLET).append("?")
                    .append("incoming_call_type=");
            strURl.append("rsvp_ans&amp;").append("call_attempt=")
                    .append(twilioIncomingBean.getCallAttemptNumber() + 1)
                    .append("&amp;");
            break;

		}
		appLogging.debug("Build URL : " + strURl.toString() + " from : "
				+ twilioIncomingBean.getFrom());
		return strURl;
	}

	public static String getTelNumberType(ArrayList<TelNumberBean> arrTelNumBean) {
		String telNumType = "";
		if (arrTelNumBean != null && !arrTelNumBean.isEmpty()) {
			for (TelNumberBean telNumBean : arrTelNumBean) {
				telNumType = telNumBean.getTelNumberType();
			}
		}
		return telNumType;
	}

	public static CallResponse getStandardError(CallResponse callResponse,
			TwilioIncomingCallBean twilioIncomingBean) {

		if (callResponse != null && twilioIncomingBean != null) {
			Say saySorryException = new Say(
					"I am sorry, Your request could not be processed at this time. Please try again later.");
			saySorryException.setVoice(VOICE_ACTOR);

			try {
				TwiMLResponse response = new TwiMLResponse();
				response.append(saySorryException);
				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}
}
