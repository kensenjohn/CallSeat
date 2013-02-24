package com.gs.call.twilio.twiml;

import com.gs.bean.EventBean;
import com.gs.bean.InformGuestBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.manager.event.EventManager;
import com.gs.task.InformGuestTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.call.CallResponse;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.twilio.sdk.verbs.Gather;
import com.twilio.sdk.verbs.Hangup;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

public class RsvpTwiml
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String VOICE_ACTOR = applicationConfig.get(Constants.PROP_TWILIO_VOICE);

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public CallResponse getRsvpDigitsSuccess(CallResponse callResponse, String sMessage)
	{
		if (callResponse != null && callResponse.getEventGuestBean() != null)
		{
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
			TwiMLResponse response = new TwiMLResponse();

			Say sayThankYou = new Say("Your RSVP reponse of " + eventGuestBean.getRsvpSeats()
					+ " has been accepted.");
			sayThankYou.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");

			Say sayInfo = new Say(
					"Enter a new RSVP number if you would like to change your previous selection. Enter a number from 0 to "
							+ eventGuestBean.getTotalNumberOfSeats()
							+ " seats to RSVP followed by the pound or hash key.");
			sayInfo.setVoice(VOICE_ACTOR);

			Hangup hangup = new Hangup();

			try
			{
				response.append(sayThankYou);

				gatherRsvp.append(sayInfo);
				response.append(gatherRsvp);
				response.append(hangup);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);


                EventManager eventManager = new EventManager();
                EventBean eventBean = eventManager.getEvent( eventGuestBean.getEventId() );
                //EventBean eventBean = callResponse.getEventBean();
                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventGuestBean.getEventId() );
                informGuestBean.setAdminId(eventBean.getEventAdminId());
                informGuestBean.setGuestId(eventGuestBean.getGuestId());
                informGuestBean.setEventTask(Constants.EVENT_TASK.RSVP);
                appLogging.info("RSVP TWIML invoking send RSVP Confirmation\n informGuestBean : " + informGuestBean + " \n Event bean : " + eventBean + " \n Guest Bean : " + eventGuestBean);
                InformGuestTask.sendRSVPConfirmation(informGuestBean);
			} catch (TwiMLException e)
			{
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}

	public CallResponse getRsvpDigitsFail(CallResponse callResponse, String sMessage,
			Constants.RSVP_STATUS rsvpStaus,
            TwilioIncomingCallBean twilioIncomingBean)
	{
		if (callResponse != null && callResponse.getEventGuestBean() != null)
		{
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
			TwiMLResponse response = new TwiMLResponse();

			if (Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS.equals(rsvpStaus)
					|| Constants.RSVP_STATUS.RSVP_UPDATE_FAIL.equals(rsvpStaus))
			{
				Say saySorry = new Say("I am sorry, " + sMessage + ".");
				saySorry.setVoice(VOICE_ACTOR);
				Gather gatherRsvp = new Gather();

				gatherRsvp.setMethod("POST");
				//gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");
                gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
                        Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

				Say sayInfo = new Say(
						"Please enter a R S V P number from zero to "
								+ eventGuestBean.getTotalNumberOfSeats()
								+ " seats followed by the pound sign");
				sayInfo.setVoice(VOICE_ACTOR);
                sayInfo.setLoop(2);

				Say sayThankYou = new Say(
						"Thank You for your response.");
				sayThankYou.setVoice(VOICE_ACTOR);

				try
				{
					response.append(saySorry);

					gatherRsvp.append(sayInfo);
					response.append(gatherRsvp);
					response.append(sayThankYou);

					callResponse.setResponse(response);
					callResponse.setTwilResponseSuccess(true);
				} catch (TwiMLException e)
				{
					callResponse.setTwilResponseSuccess(false);
					appLogging.info(ExceptionHandler.getStackTrace(e));
				}

			}
		} else
		{

		}

		return callResponse;
	}

	public CallResponse getFirstResponse(CallResponse callResponse,IncomingCallBean incomingCallBean )
	{
		if (callResponse != null && callResponse.getEventGuestBean() != null && incomingCallBean!=null)
		{
            TwilioIncomingCallBean twilioIncomingBean = (TwilioIncomingCallBean)incomingCallBean;
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();

			TwiMLResponse response = new TwiMLResponse();

			Say sayWelcome = new Say("Welcome to the automated RSVP for");
			sayWelcome.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			//gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");
            gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
                    Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

			Say sayInfo = new Say(
					"You have been been invited to "
							+ eventGuestBean.getTotalNumberOfSeats()
							+ " seats. Please R S V P by entering the number of seats.");
			sayInfo.setVoice(VOICE_ACTOR);
			// sayInfo.setLoop(3);

			Say sayThankYou = new Say("Thank You for your response.");
			sayThankYou.setVoice(VOICE_ACTOR);

			try
			{
				response.append(sayWelcome);

				gatherRsvp.append(sayInfo);
				response.append(gatherRsvp);
				response.append(sayThankYou);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e)
			{
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}

		}
		return callResponse;
	}

}
