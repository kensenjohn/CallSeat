package com.gs.call.twilio.twiml;

import com.gs.bean.EventBean;
import com.gs.bean.InformGuestBean;
import com.gs.bean.NumberVoiceBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.*;
import com.gs.common.exception.ExceptionHandler;
import com.gs.manager.event.EventFeatureManager;
import com.gs.manager.event.EventManager;
import com.gs.call.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.call.CallResponse;

public class RsvpTwiml
{
    private static Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

    private static String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);
    private static String DEFAULT_VOICE = applicationConfig.get(Constants.PROP_VOICE_RECORDING_DEFAULT_VOICE);

    private static String VOICE_PATH = VOICE_RECORDING_FOLDER + "/"  + DEFAULT_VOICE;

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    Logger telephonyLogging = LoggerFactory.getLogger(Constants.TELEPHONY_LOGS);

	public CallResponse getRsvpDigitsSuccess(CallResponse callResponse, String sMessage) {
		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
            EventBean eventBean = callResponse.getEventBean();

			TwiMLResponse response = new TwiMLResponse();

            Play playThankYou = new Play(VOICE_PATH+"_thank_you.wav");
            Play playYourRSVPhasBeenAccepted = new Play(VOICE_PATH+"_your_rsvp_has_been_accepted.wav");

            Play playToChangeYourRsvp = new Play(VOICE_PATH+"_please_call_back_if_you_would_like_to_change_rsvp.wav");
            Hangup hangup = new Hangup();
            try {
                response.append( playThankYou );
                response.append( playYourRSVPhasBeenAccepted );

                response.append( playToChangeYourRsvp );

                response.append(hangup);

                callResponse.setResponse(response);
                callResponse.setTwilResponseSuccess(true);


                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventBean.getEventId() );
                informGuestBean.setAdminId( eventBean.getEventAdminId() );
                informGuestBean.setGuestId( eventGuestBean.getGuestId() );
                informGuestBean.setEventTask( Constants.EVENT_TASK.PREMIUM_TELEPHONE_NUMBER );
                telephonyLogging.info("Informing guest their RSVP confirmation ");
                InformGuestTask.sendRSVPConfirmation(informGuestBean);
            } catch (TwiMLException e) {
                callResponse.setTwilResponseSuccess(false);
                telephonyLogging.error("Demo - Twilio exception for RSVP digit success : " +ExceptionHandler.getStackTrace(e));
            }
		}
		return callResponse;
	}

	public CallResponse getRsvpDigitsFail(CallResponse callResponse, String sMessage,
			Constants.RSVP_STATUS rsvpStaus,
            TwilioIncomingCallBean twilioIncomingBean) {
		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
			TwiMLResponse response = new TwiMLResponse();


            if (Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS.equals(rsvpStaus)
                    || Constants.RSVP_STATUS.RSVP_UPDATE_FAIL.equals(rsvpStaus)) {

                int iTotalNumberOfSeats =  ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats() );

                Play playThankYou = new Play(VOICE_PATH+"_thank_you.wav");

                try {
                    telephonyLogging.info("RSVP - After failure Creating the Gather RSVP Verb : Total Invited " + iTotalNumberOfSeats + " From " + twilioIncomingBean.getFrom() + " To(Event Number) : " + twilioIncomingBean.getTo() );
                    Gather gatherRsvp = RsvpTwiml.getGatherRSVPVerb(iTotalNumberOfSeats, twilioIncomingBean,Constants.CALL_TYPE.RSVP_DIGIT_RESP);
                    if(gatherRsvp!=null && Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS.equals(rsvpStaus) ) {
                        Play playThatWasAnInvalidEntry = new Play(VOICE_PATH+"_that_was_an_invalid_entry.wav") ;
                        Play playPleaseTryAgain = new Play(VOICE_PATH+"_please_try_again.wav") ;
                        response.append(playThatWasAnInvalidEntry);
                        response.append(playPleaseTryAgain);
                        response.append(gatherRsvp);
                        response.append(playThankYou);
                    } else {
                        Play playIamSorryUnableTOProcessRequest = new Play(VOICE_PATH+"_i_am_sorry_we_were_unable_to_process_request.wav");
                        Play playPleaseTryAgainLater = new Play(VOICE_PATH+"_please_try_again_later.wav");

                        response.append(playIamSorryUnableTOProcessRequest);
                        response.append(playPleaseTryAgainLater);
                        response.append(playThankYou);

                        Hangup hangup = new Hangup();
                        response.append( hangup );
                    }

                    callResponse.setResponse(response);
                    callResponse.setTwilResponseSuccess(true);
                } catch (TwiMLException e) {
                    callResponse.setTwilResponseSuccess(false);
                    telephonyLogging.error("RSVP - TWilio exception when RSVP digit fails" + ExceptionHandler.getStackTrace(e));
                }

            }
		} else {

		}

		return callResponse;
	}

    public CallResponse getCallForwardingResponse(CallResponse callResponse,IncomingCallBean incomingCallBean ) {
        if (callResponse != null && incomingCallBean!=null) {
            EventBean eventBean = callResponse.getEventBean();

            TwiMLResponse response = new TwiMLResponse();

            Play playWelcome = new Play(VOICE_PATH+"_hello_and_welcome_to_callseat.wav");

            boolean isCallForward = false;
            String sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature(eventBean.getEventId(), Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER);

            Play playMessage = null;
            if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) ) {
                playMessage = new Play( VOICE_PATH+"_please_hold_while_transfer_to_rsvp_coordinator.wav" );
                isCallForward = true;
            }  else {
                playMessage = new Play( VOICE_PATH+"_i_am_sorry_unable_to_process_your_request.wav" );
                isCallForward = false;
            }

            Play playThankYouEnjoyDay = new Play(VOICE_PATH+"_thank_you_and_enjoy_your_day.wav");
            Play playHalfSecondDelaySilence = new Play(VOICE_PATH+"_silence_0.5.wav");
            try{

                response.append(playWelcome);

                response.append(playHalfSecondDelaySilence);

                response.append(playMessage);
                response.append(playHalfSecondDelaySilence);

                if (isCallForward) {
                    response.append(callForwardUsher(sCallForwaringNum));
                }

                response.append(playThankYouEnjoyDay);

                callResponse.setResponse(response);
                callResponse.setTwilResponseSuccess(true);

            }  catch (TwiMLException e)  {
                callResponse.setTwilResponseSuccess(false);
                telephonyLogging.error("RSVP - Twilio exception while trying to forward call " + ExceptionHandler.getStackTrace(e));
            }
        }
        return callResponse;
    }

    private Verb callForwardUsher(String sForwardingNumber)
    {
        Dial dialUsher = new Dial(sForwardingNumber);
        dialUsher.setTimeout(60);
        return dialUsher;
    }

	public CallResponse getFirstResponse(CallResponse callResponse,IncomingCallBean incomingCallBean ) {
		if (callResponse != null && callResponse.getEventGuestBean() != null && incomingCallBean!=null) {
            TwilioIncomingCallBean twilioIncomingBean = (TwilioIncomingCallBean)incomingCallBean;
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();

            Play playWelcome = new Play(VOICE_PATH+"_hello_and_welcome_to_callseat.wav");
            Play playIWillHelpWithRSVP = new Play(VOICE_PATH+"_i_will_help_you with_your_rsvp.wav");
            Play playHalfSecondSilence = new Play(VOICE_PATH+"_silence_0.5.wav");

            Play playThankYouForResponse = new Play(VOICE_PATH+"_thank_you.wav") ;

            Integer iTotalInvitedSeats = ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats());

			try {
                TwiMLResponse response = new TwiMLResponse();

                response.append(playWelcome);
                response.append(playIWillHelpWithRSVP);
                response.append(playHalfSecondSilence);
                telephonyLogging.error("RSVP - Building Voice to gather RSVP " +
                        " From : " + twilioIncomingBean.getFrom()  + " To : " + twilioIncomingBean.getFrom() + " Invited seats : " + iTotalInvitedSeats );

                Gather gatherRsvp = getGatherRSVPVerb(iTotalInvitedSeats,twilioIncomingBean,Constants.CALL_TYPE.RSVP_DIGIT_RESP);
                if( gatherRsvp!=null )  {
                    response.append(gatherRsvp);
                }  else {
                    telephonyLogging.error("RSVP - Invalid Gather Verb encountered " +
                            " From : " + twilioIncomingBean.getFrom()  + " To : " + twilioIncomingBean.getFrom() + " Invited seats : " + iTotalInvitedSeats );
                }
                response.append(playThankYouForResponse);

                callResponse.setResponse(response);
                callResponse.setTwilResponseSuccess(true);

			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				telephonyLogging.info("RSVP Twilio exception when trying to gather RSVP from viewer. " + ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}


    public static Gather getGatherRSVPVerb( Integer iTotalInvitedSeats , TwilioIncomingCallBean twilioIncomingBean ,  Constants.CALL_TYPE callType) throws TwiMLException {

        Gather gatherRsvp = new Gather();

        gatherRsvp.setMethod("POST");
        gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean, callType ).toString());

        Play playYouHaveBeenInvited = new Play(VOICE_PATH +"_you_have_been_invited_to.wav") ;

        Play playSingleSeat = new Play(VOICE_PATH+"_seat.wav") ;
        Play playMultipleSeats = new Play(VOICE_PATH+"_seats.wav") ;

        NumberVoiceBean invitedSeats = Utility.getNumberInVoice(iTotalInvitedSeats);
        Play playTensPlaceInvitedSeats = new Play(VOICE_PATH+invitedSeats.getTensPlace()) ;
        Play playOnesPlaceInvitedSeats = new Play(VOICE_PATH +invitedSeats.getOnesPlace()) ;

        Play playSelectDoNotWishToAttend = new Play(VOICE_PATH+"_if_you_do_not_wish_to_attend_please_press_0_followed_pound.wav") ;

        Play playSelectOneInviteRSVP = new Play(VOICE_PATH+"_if_you_will_attend_please_press_1_followed_by_pound.wav") ;
        Play playSelectMultiInviteRSVP = new Play(VOICE_PATH+"_if_you_will_attend_please_rsvp_by_entering_total_seats_from_1_to.wav") ;

        Play playFollowedByPoundSign = new Play(VOICE_PATH+"_followed_by_the_pound_sign.wav") ;

        Play playThankYouForResponse = new Play(VOICE_PATH+"_thank_you.wav") ;

        Play playHalfSecondDelay = new Play(VOICE_PATH+"_silence_0.5.wav") ;

        gatherRsvp.append( playYouHaveBeenInvited );
        if( (iTotalInvitedSeats)>19 ) {
            if( ((iTotalInvitedSeats)%10) == 0 ) {
                gatherRsvp.append( playTensPlaceInvitedSeats );
            }  else  {
                gatherRsvp.append( playTensPlaceInvitedSeats );
                gatherRsvp.append( playOnesPlaceInvitedSeats );
            }

        } else {
            gatherRsvp.append( playOnesPlaceInvitedSeats );
        }
        if( iTotalInvitedSeats<=1 )  {
            gatherRsvp.append( playSingleSeat );
        } else {
            gatherRsvp.append( playMultipleSeats );
        }
        gatherRsvp.append( playHalfSecondDelay );
        if(iTotalInvitedSeats==1){
            gatherRsvp.append( playSelectOneInviteRSVP );
        }

        if(iTotalInvitedSeats>1){
            gatherRsvp.append( playSelectMultiInviteRSVP );
            if( (iTotalInvitedSeats)>19 ) {
                if( ((iTotalInvitedSeats)%10) == 0 ) {
                    gatherRsvp.append( playTensPlaceInvitedSeats );
                }  else  {
                    gatherRsvp.append( playTensPlaceInvitedSeats );
                    gatherRsvp.append( playOnesPlaceInvitedSeats );
                }

            } else {
                gatherRsvp.append( playOnesPlaceInvitedSeats );
            }
            gatherRsvp.append( playFollowedByPoundSign );
        }

        gatherRsvp.append( playHalfSecondDelay );
        gatherRsvp.append( playSelectDoNotWishToAttend );

        return gatherRsvp;

    }

}
