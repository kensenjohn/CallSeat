package com.gs.call.twilio.twiml;

import com.gs.bean.EventBean;
import com.gs.bean.InformGuestBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.ParseUtil;
import com.gs.manager.event.EventFeatureManager;
import com.gs.manager.event.EventManager;
import com.gs.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.call.CallResponse;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;

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

			Say sayThankYou = new Say("Your RSVP response of " + eventGuestBean.getRsvpSeats()
					+ " has been accepted.");
			sayThankYou.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
            gatherRsvp.setAction(TwimlSupport.buildURL(new TwilioIncomingCallBean(),
                    Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

			Say sayInfo = new Say(
					"To change your RSVP please select a number from 0 to "
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
                        "You can hang up now or try again. Please select a number from zero to "
                                + ParseUtil.sToI( eventGuestBean.getTotalNumberOfSeats() )
                                + " followed by the pound sign.");
				sayInfo.setVoice(VOICE_ACTOR);

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

    public CallResponse getCallForwardingResponse(CallResponse callResponse,IncomingCallBean incomingCallBean )
    {
        if (callResponse != null && incomingCallBean!=null)
        {
            EventBean eventBean = callResponse.getEventBean();

            TwilioIncomingCallBean twilioIncomingBean = (TwilioIncomingCallBean)incomingCallBean;
            String sFromNumber = ParseUtil.checkNull( twilioIncomingBean.getFrom() );
            String sToNumber = ParseUtil.checkNull( twilioIncomingBean.getTo() );

            TwiMLResponse response = new TwiMLResponse();

            Say sayWelcome = new Say("Welcome");
            sayWelcome.setVoice(VOICE_ACTOR);

            boolean isCallForward = false;
            String sMessage = "";
            String sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature(eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER);
            if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) )
            {
                sMessage = "Your call will be forwarded to an usher.";
                isCallForward = true;
            }
            else
            {
                sMessage = "We are sorry we were unable to process your RSVP. Please call again later.";
                isCallForward = false;
            }

            Say sayMessage = new Say(sMessage);
            sayMessage.setVoice(VOICE_ACTOR);

            Say sayThankYou = new Say("Thank You, and enjoy your day.");
            sayThankYou.setVoice(VOICE_ACTOR);

            try
            {
                //Verb pauseVerb = new Verb(Verb.V_PAUSE,null);
                Pause pause = new Pause();
                pause.setLength(1);

                response.append(sayWelcome);

                response.append(pause);

                response.append(sayMessage);
                response.append(pause);

                if (isCallForward)
                {
                    response.append(callForwardUsher(sCallForwaringNum));
                }

                response.append(sayThankYou);

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

    private Verb callForwardUsher(String sForwardingNumber)
    {
        Dial dialUsher = new Dial(sForwardingNumber);
        dialUsher.setTimeout(60);
        // dialUsher.append(verb)

        return dialUsher;
    }

	public CallResponse getFirstResponse(CallResponse callResponse,IncomingCallBean incomingCallBean )
	{
		if (callResponse != null && callResponse.getEventGuestBean() != null && incomingCallBean!=null)
		{
            TwilioIncomingCallBean twilioIncomingBean = (TwilioIncomingCallBean)incomingCallBean;
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();



			Say sayWelcome = new Say("Welcome");
			sayWelcome.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			//gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");
            gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
                    Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

            Integer iTotalInvitedSeats = ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats());

            String sInfoMessage = "You have been been invited to ";
            if(iTotalInvitedSeats == 1)
            {
                sInfoMessage = sInfoMessage + iTotalInvitedSeats + " seat. ";
            }
            else if( iTotalInvitedSeats > 1 )
            {
                sInfoMessage = sInfoMessage + iTotalInvitedSeats + " seats. ";
            }
            Say sayInfo = new Say(
                    sInfoMessage + " To RSVP please select a number from 0 to " + iTotalInvitedSeats + " followed by the pound sign." );
            sayInfo.setVoice(VOICE_ACTOR);
			// sayInfo.setLoop(3);

			Say sayThankYou = new Say("Thank You for your response.");
			sayThankYou.setVoice(VOICE_ACTOR);

			try
			{
                TwiMLResponse response = new TwiMLResponse();
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
