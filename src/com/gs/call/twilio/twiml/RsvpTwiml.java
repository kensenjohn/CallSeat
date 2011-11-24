package com.gs.call.twilio.twiml;

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

	Logger appLogging = LoggerFactory.getLogger("AppLogging");

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
			} catch (TwiMLException e)
			{
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}

	public CallResponse getRsvpDigitsFail(CallResponse callResponse, String sMessage,
			Constants.RSVP_STATUS rsvpStaus)
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
				gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");

				Say sayInfo = new Say(
						"Please try again or hang up and try later. Enter a number from from zero to "
								+ eventGuestBean.getTotalNumberOfSeats()
								+ " seats to RSVP for this invite. When you are done press the pound sign");
				sayInfo.setVoice(VOICE_ACTOR);

				Say sayThankYou = new Say(
						"Thank You for your response. Please hold while I process the RSVP seats.");
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

	public CallResponse getFirstResponse(CallResponse callResponse)
	{
		if (callResponse != null && callResponse.getEventGuestBean() != null)
		{
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();

			TwiMLResponse response = new TwiMLResponse();

			Say sayWelcome = new Say("Welcome to the automated RSVP for");
			sayWelcome.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");

			Say sayInfo = new Say(
					"You have been been invited for "
							+ eventGuestBean.getTotalNumberOfSeats()
							+ " seats. Please enter the number of seats you would like to RSVP to after the beep. "
							+ " Enter a the number of seats you would like to RSVP for followed by the pound sign.");
			sayInfo.setVoice("woman");
			// sayInfo.setLoop(3);

			Say sayThankYou = new Say("Thank You.");
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
