package com.gs.call.twilio.twiml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gs.bean.*;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.manager.event.EventFeatureManager;
import com.gs.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.common.Configuration;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;

public class DemoCallTwiml {
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String VOICE_ACTOR = applicationConfig.get(Constants.PROP_TWILIO_VOICE);
    private String VOICE_RECORDING_DOMAIN = applicationConfig.get(Constants.PROP_VOICE_RECORDING_DOMAIN);
	private Say sayThankYou = new Say("Thank You.");

	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public CallResponse getSeatingResponse(CallResponse callResponse,
			TwilioIncomingCallBean twilioIncomingBean) throws TwiMLException,
			EncoderException {
		Pause pause = new Pause();
		pause.setLength(1);

		boolean isCallForward = false;

		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventBean eventBean = callResponse.getEventBean();

			TwiMLResponse response = new TwiMLResponse();

			Say sayWelcome = new Say("Welcome");
			sayWelcome.setVoice(VOICE_ACTOR);

			ArrayList<TableGuestsBean> arrTableGuestBean = callResponse
					.getArrTableGuestBean();

            String sCallForwaringNum = "";
			String sSeatingMessage = "";
			boolean isFirst = true;
			if (arrTableGuestBean != null && !arrTableGuestBean.isEmpty()) {
                int totalNumOfSeats = 0;
                String sTableNumberMessage = "";
                for (TableGuestsBean tableGuestBean : arrTableGuestBean) {
					int numOfSeats = ParseUtil.sToI(tableGuestBean
							.getGuestAssignedSeats());
                    totalNumOfSeats = totalNumOfSeats + numOfSeats;

					if (!isFirst) {
                        sTableNumberMessage = sTableNumberMessage + ", ";
					}

                    sTableNumberMessage = sTableNumberMessage + " table "
							+ tableGuestBean.getTableNum();
					isFirst = false;
				}
                if(totalNumOfSeats>0)
                {
                    if (totalNumOfSeats == 1)
                    {
                        sSeatingMessage = "You are seated at ";
                    }
                    else if( totalNumOfSeats == 2 )
                    {
                        sSeatingMessage = "You and your guest are seated at ";
                    }
                    else if( totalNumOfSeats > 2 )
                    {
                        sSeatingMessage = "You and your guests are seated at ";
                    }
                    sSeatingMessage = sSeatingMessage + sTableNumberMessage;
                }
                else
                {
                    sSeatingMessage = "We are sorry we were unable to retrieve your information. Please call again later.";
                }

			} else {
                sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature( eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER );
                if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) )
                {
                    sSeatingMessage = "Your call will be forwarded to an usher.";
                    isCallForward = true;
                }
			}

			Say sayMessage = new Say(sSeatingMessage);
			sayMessage.setVoice(VOICE_ACTOR);

			Say sayThankYou = new Say("Thank You, and enjoy your day.");
			sayThankYou.setVoice(VOICE_ACTOR);

			try {
				response.append(sayWelcome);
				response.append(pause);

				response.append(sayMessage);
				response.append(pause);

				if (isCallForward) {
					response.append(callForwardUsher(sCallForwaringNum));
				}

				response.append(sayThankYou);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);

                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventBean.getEventId() );
                informGuestBean.setAdminId( eventBean.getEventAdminId() );
                informGuestBean.setGuestId( callResponse.getEventGuestBean().getGuestId() );
                informGuestBean.setEventTask( Constants.EVENT_TASK.SEATING );

                InformGuestTask.sendSeatingConfirmation( informGuestBean );
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}
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
                sMessage = "An usher will assist you. Please wait for the call to be forwarded.";
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

	private Verb callForwardUsher(String sTelNumber) {
		Dial dialUsher = new Dial(sTelNumber);
		dialUsher.setTimeout(60);
		// dialUsher.append(verb)

		return dialUsher;
	}

	public CallResponse getRsvpResponse(CallResponse callResponse,
			TwilioIncomingCallBean twilioIncomingBean) throws TwiMLException,
			EncoderException {
		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();

            Say sayWelcome = new Say("Welcome");
            sayWelcome.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
					Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM).toString());

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

			try {
				TwiMLResponse response = new TwiMLResponse();
                gatherRsvp.append(sayWelcome);

				gatherRsvp.append(sayInfo);
				response.append(gatherRsvp);
				response.append(sayThankYou);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
				throw e;
			}
		}
		return callResponse;
	}

	public CallResponse getSecretKeyFromUser(CallResponse callResponse,
			TwilioIncomingCallBean twilioIncomingBean) throws EncoderException,
			TwiMLException {
		if (callResponse != null) {
			int iNumOfCallAttempt = twilioIncomingBean.getCallAttemptNumber();
			appLogging.info("Gathering Secret Key from user : attempt : "
					+ iNumOfCallAttempt + " from : "
					+ twilioIncomingBean.getFrom());
			//Say saySorryWrongSecretKey = new Say("I am sorry, The key you entered was not valid.");
			//saySorryWrongSecretKey.setVoice(VOICE_ACTOR);

            Play playSorryInvalid = new Play(VOICE_RECORDING_DOMAIN+"/invalid_entry_1.wav");

			Gather gatherSecretKey = new Gather();

			gatherSecretKey.setMethod("POST");
			gatherSecretKey.setAction(TwimlSupport.buildURL(twilioIncomingBean,
					Constants.CALL_TYPE.DEMO_GATHER_SECRET_KEY).toString());

			//Say sayGetSecretKey = new Say("Please enter your secret key followed by the pound sign.");
			//sayGetSecretKey.setVoice(VOICE_ACTOR);
            Play playGatherSecretKey = new Play(VOICE_RECORDING_DOMAIN+"/gather_secret_key_1.wav");

			try {
				TwiMLResponse response = new TwiMLResponse();

				if (iNumOfCallAttempt > 0) {
					// previous attempt errored out, so apologize
					response.append(playSorryInvalid);
				}

				gatherSecretKey.append(playGatherSecretKey);
				response.append(gatherSecretKey);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
				throw e;
			}
		}
		return callResponse;
	}

	public CallResponse getEventNumFromUser(CallResponse callResponse,
			TwilioIncomingCallBean twilioIncomingBean) throws TwiMLException,
			EncoderException {
		if (callResponse != null) {

			int iNumOfCallAttempt = twilioIncomingBean.getCallAttemptNumber();

			//Say sayWelcome = new Say("Welcome to call seat.");
			//sayWelcome.setVoice(VOICE_ACTOR);

            Play playWelcome = new Play(VOICE_RECORDING_DOMAIN+"/welcome_to_callseat_1.wav");

			//Say saySorryWrongEvent = new Say("I am sorry, The event number was not valid.");
			//saySorryWrongEvent.setVoice(VOICE_ACTOR);

            Play playSorryInvalid = new Play(VOICE_RECORDING_DOMAIN+"/invalid_entry_1.wav");

			Gather gatherEventNum = new Gather();

			gatherEventNum.setMethod("POST");
			URLCodec urlEncoder = new URLCodec();
			gatherEventNum.setAction(TwimlSupport.buildURL(twilioIncomingBean,Constants.CALL_TYPE.DEMO_GATHER_EVENT_NUM).toString());

			//Say sayGetEventNum = new Say("Please enter the event number followed by the pound sign.");
			//sayGetEventNum.setVoice(VOICE_ACTOR);

            Play playGatherEventNumber = new Play(VOICE_RECORDING_DOMAIN+"/gather_event_number_1.wav");


			try {
				TwiMLResponse response = new TwiMLResponse();
				gatherEventNum.append(playGatherEventNumber);
				if (iNumOfCallAttempt == 0) {
					response.append(playWelcome);
				} else {
					response.append(playSorryInvalid);
				}

				response.append(gatherEventNum);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
				throw e;
			}
		}
		return callResponse;
	}

	public CallResponse getRsvpDigitsSuccess(CallResponse callResponse,
			String sMessage, TwilioIncomingCallBean twilioIncomingBean) {
		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
			TwiMLResponse response = new TwiMLResponse();

			Say sayThankYou = new Say("Your RSVP response of "
					+ eventGuestBean.getRsvpSeats() + " has been accepted.");
			sayThankYou.setVoice(VOICE_ACTOR);

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
					Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM).toString());

            Say sayInfo = new Say(
                    "To change your RSVP please select a number from 0 to "
                            + eventGuestBean.getTotalNumberOfSeats()
                            + " followed by the pound sign.");
			sayInfo.setVoice(VOICE_ACTOR);

			Hangup hangup = new Hangup();

			try {
				response.append(sayThankYou);

				gatherRsvp.append(sayInfo);
				response.append(gatherRsvp);
				response.append(hangup);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);

                EventBean eventBean = callResponse.getEventBean();
                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventBean.getEventId() );
                informGuestBean.setAdminId( eventBean.getEventAdminId() );
                informGuestBean.setGuestId( eventGuestBean.getGuestId() );
                informGuestBean.setEventTask( Constants.EVENT_TASK.RSVP );

                InformGuestTask.sendRSVPConfirmation(informGuestBean);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}

	public CallResponse getRsvpDigitsFail(CallResponse callResponse,
			String sMessage, Constants.RSVP_STATUS rsvpStaus,
			TwilioIncomingCallBean twilioIncomingBean) {
		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
			TwiMLResponse response = new TwiMLResponse();

			if (Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS.equals(rsvpStaus)
					|| Constants.RSVP_STATUS.RSVP_UPDATE_FAIL.equals(rsvpStaus)) {
				Say saySorry = new Say("We are sorry, " + sMessage + ".");
				saySorry.setVoice(VOICE_ACTOR);
				Gather gatherRsvp = new Gather();

				gatherRsvp.setMethod("POST");
				gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
						Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM).toString());

				Say sayInfo = new Say(
						"You can hang up now or try again. Please select a number from zero to "
								+ ParseUtil.sToI( eventGuestBean.getTotalNumberOfSeats() )
								+ " followed by the pound sign.");
				sayInfo.setVoice(VOICE_ACTOR);

				Say sayThankYou = new Say(
						"Thank You for your response.");
				sayThankYou.setVoice(VOICE_ACTOR);

				try {
					response.append(saySorry);

					gatherRsvp.append(sayInfo);
					response.append(gatherRsvp);
					response.append(sayThankYou);

					callResponse.setResponse(response);
					callResponse.setTwilResponseSuccess(true);
				} catch (TwiMLException e) {
					callResponse.setTwilResponseSuccess(false);
					appLogging.info(ExceptionHandler.getStackTrace(e));
				}

			}
		} else {

		}

		return callResponse;
	}

}
