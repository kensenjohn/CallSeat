package com.gs.call.twilio.twiml;

import java.util.ArrayList;

import com.gs.bean.InformGuestBean;
import com.gs.task.InformGuestTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventBean;
import com.gs.bean.TableGuestsBean;
import com.gs.call.CallResponse;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.Pause;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.Verb;

public class SeatingTwiml
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String VOICE_ACTOR = applicationConfig.get(Constants.PROP_TWILIO_VOICE);

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public CallResponse getFirstResponse(CallResponse callResponse)
	{
        //Verb pauseVerb = new Verb(Verb.V_PAUSE,null);
        Pause pause = new Pause();
		pause.setLength(1);

		boolean isCallForward = false;

		if (callResponse != null && callResponse.getEventGuestBean() != null)
		{
			EventBean eventBean = callResponse.getEventBean();

			TwiMLResponse response = new TwiMLResponse();

			Say sayWelcome = new Say("Welcome to " + eventBean.getEventName() + "'s seating app.");
			sayWelcome.setVoice(VOICE_ACTOR);

			ArrayList<TableGuestsBean> arrTableGuestBean = callResponse.getArrTableGuestBean();

			String sSeatingMessage = "";
			boolean isFirst = true;
			if (arrTableGuestBean != null && !arrTableGuestBean.isEmpty())
			{
				sSeatingMessage = " You have been assigned ";
				for (TableGuestsBean tableGuestBean : arrTableGuestBean)
				{
					int numOfSeats = ParseUtil.sToI(tableGuestBean.getGuestAssignedSeats());

					String sSeats = "";
					if (numOfSeats > 1)
					{
						sSeats = "seats";
					} else
					{
						sSeats = "seat";
					}

					if (!isFirst)
					{
						sSeatingMessage = sSeatingMessage + ", ";
					}

					sSeatingMessage = sSeatingMessage + numOfSeats + " " + sSeats
							+ " at table number " + tableGuestBean.getTableNum() + ".";
					isFirst = false;
				}

                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventBean.getEventId() );
                informGuestBean.setAdminId( eventBean.getEventAdminId() );
                informGuestBean.setGuestId( callResponse.getEventGuestBean().getGuestId() );
                informGuestBean.setEventTask( Constants.EVENT_TASK.SEATING );

                InformGuestTask.sendSeatingConfirmation( informGuestBean );
			}
            else
			{
				sSeatingMessage = "Your call will now be forwarded to an usher. The usher will provide you with more assistance.";
				isCallForward = true;
			}

			Say sayMessage = new Say(sSeatingMessage);
			sayMessage.setVoice(VOICE_ACTOR);

			Say sayThankYou = new Say("Thank You, and enjoy your day.");
			sayThankYou.setVoice(VOICE_ACTOR);

			try
			{
				response.append(sayWelcome);
				response.append(pause);

				response.append(sayMessage);
				response.append(pause);

				if (isCallForward)
				{
					response.append(callForwardUsher(callResponse));
				}

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

	private Verb callForwardUsher(CallResponse callResponse)
	{
		Dial dialUsher = new Dial("267-250-2719");
		dialUsher.setTimeout(60);
		// dialUsher.append(verb)

		return dialUsher;
	}
}
