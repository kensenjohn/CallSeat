package com.gs.call.twilio.twiml;

import java.util.ArrayList;

import com.gs.common.Utility;
import com.gs.manager.event.EventFeatureManager;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.TelNumberBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.exception.ExceptionHandler;

public class TwimlSupport {

	private static final String DEMO_ENTRY_SERVLET = "DemoIncomingCall";
    private static final String PREMIUM_ENTRY_SERVLET = "IncomingCall";
	private static Logger appLogging = LoggerFactory.getLogger("AppLogging");

	private static Configuration applicationConfig = Configuration
			.getInstance(Constants.APPLICATION_PROP);
	private static String VOICE_ACTOR = applicationConfig
			.get(Constants.PROP_TWILIO_VOICE);
    private static String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);

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
    public static String getSeatingPlanModeFromTelnumber(TelNumberBean telNumBean) {
        String telNumType = Constants.EMPTY;
        if( telNumBean!=null && !Utility.isNullOrEmpty(telNumBean.getEventId()) ) {
            telNumType = EventFeatureManager.getStringValueFromEventFeature(telNumBean.getEventId(), Constants.EVENT_FEATURES.SEATINGPLAN_MODE);
        }
        return telNumType;
    }
    public static String getSeatingPlanModeFromTelnumber(ArrayList<TelNumberBean> arrTelNumBean) {
        String telNumType = Constants.EMPTY;
        if (arrTelNumBean != null && !arrTelNumBean.isEmpty()) {
            for (TelNumberBean telNumBean : arrTelNumBean) {
                telNumType = getSeatingPlanModeFromTelnumber(telNumBean);
            }
        }
        return telNumType;
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
			//Say saySorryException = new Say("I am sorry, Your request could not be processed at this time. Please try again later.");
			//saySorryException.setVoice(VOICE_ACTOR);
            Play playIAmSorry= new Play(VOICE_RECORDING_FOLDER+"/i_am_sorry_stereo.wav");
            Play playPleaseCallAgainLater= new Play(VOICE_RECORDING_FOLDER+"/please_call_again_later_stereo.wav");
            Play playQuarterSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/quarter_second_stereo.wav");

			try {
				TwiMLResponse response = new TwiMLResponse();
				response.append(playIAmSorry);
                response.append(playQuarterSecondDelay);
                response.append(playPleaseCallAgainLater);
				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}

    public static CallResponse rejectCall( CallResponse callResponse )
    {
        if (callResponse != null )
        {
            TwiMLResponse response = new TwiMLResponse();
            try
            {
                Reject reject = new Reject();
                response.append( reject  ) ;


                callResponse.setResponse(response);
                callResponse.setTwilResponseSuccess(true);
            }
            catch (TwiMLException e)
            {
                callResponse.setTwilResponseSuccess(false);
                appLogging.info(ExceptionHandler.getStackTrace(e));
            }

        }
        return callResponse;
    }
}
