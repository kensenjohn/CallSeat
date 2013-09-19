package com.gs.call.twilio.twiml;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.bean.*;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.common.*;
import com.gs.common.exception.ExceptionHandler;
import com.gs.manager.event.EventFeatureManager;
import com.gs.call.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;

public class DemoCallTwiml {
    private static Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

    private static String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);
    private static String DEFAULT_VOICE = applicationConfig.get(Constants.PROP_VOICE_RECORDING_DEFAULT_VOICE);

    private static String VOICE_PATH = VOICE_RECORDING_FOLDER + "/"  + DEFAULT_VOICE;

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    Logger telephonyLogging = LoggerFactory.getLogger(Constants.TELEPHONY_LOGS);

	public CallResponse getSeatingResponse(CallResponse callResponse,
			TwilioIncomingCallBean twilioIncomingBean) throws TwiMLException,
			EncoderException {
		Pause pause = new Pause();
		pause.setLength(1);

		boolean isCallForward = false;

		if (callResponse != null && callResponse.getEventGuestBean() != null) {
			EventBean eventBean = callResponse.getEventBean();

			TwiMLResponse response = new TwiMLResponse();

            Play playWelcome = new Play(VOICE_PATH+"_i_will_help_you_find_your_table.wav");

			ArrayList<TableGuestsBean> arrTableGuestBean = callResponse.getArrTableGuestBean();

            Play playSeatingMessage = null;
            String sCallForwaringNum = "";
			boolean isFirst = true;
            HashMap<Integer,Play> hmPlaySequence = new HashMap<Integer,Play>();
            Integer iPlaySequece = 0;
			if (arrTableGuestBean != null && !arrTableGuestBean.isEmpty()) {
                int totalNumOfSeats = 0;
                String sTableNumberMessage = "";
                for (TableGuestsBean tableGuestBean : arrTableGuestBean) {
					int numOfSeats = ParseUtil.sToI(tableGuestBean.getGuestAssignedSeats());
                    totalNumOfSeats = totalNumOfSeats + numOfSeats;
                }

                telephonyLogging.info("DEMO - Seating Info for guest Total Seats : " + totalNumOfSeats  + " From : "  + twilioIncomingBean.getFrom() + " To : " + twilioIncomingBean.getTo() );
                if(totalNumOfSeats>0)  {
                    if (totalNumOfSeats == 1) {
                        playSeatingMessage = new Play( VOICE_PATH+"_you_are_seated_at_table.wav" );
                    }  else if( totalNumOfSeats == 2 )  {
                        playSeatingMessage = new Play( VOICE_PATH+"_you_and_guest_seated_table.wav" );
                    }  else if( totalNumOfSeats > 2 )  {
                        playSeatingMessage = new Play( VOICE_PATH+"_you_and_guest_seated_table.wav" );
                    }
                } else  {
                    playSeatingMessage = new Play( VOICE_PATH+"_i_am_sorry_unable_to_retrieve_your_information.wav" );
                }

                Play playSilenceHalfSecond = new Play(VOICE_PATH+"_silence_0.5.wav") ;
                Play playTableData = new Play( VOICE_PATH+"_table.wav" );
                Play playAndTable = new Play( VOICE_PATH+"_and_table.wav" );
                Integer iNumOfTables = 1;
                for (TableGuestsBean tableGuestBean : arrTableGuestBean) {
                    if (!isFirst) {
                        iNumOfTables++;
                        if(iNumOfTables == arrTableGuestBean.size() && iNumOfTables>1) {
                            hmPlaySequence.put(iPlaySequece, playAndTable );
                            iPlaySequece++;
                        } else {
                            hmPlaySequence.put(iPlaySequece, playTableData );
                            iPlaySequece++;
                        }
                    }

                    int iTableNum = ParseUtil.sToI( tableGuestBean.getTableNum());
                    NumberVoiceBean numberVoiceTableNum = Utility.getNumberInVoice(  iTableNum );

                    Play playTensPlaceTableNum = new Play(VOICE_PATH + numberVoiceTableNum.getTensPlace()) ;
                    Play playOnesPlaceTableNum = new Play(VOICE_PATH + numberVoiceTableNum.getOnesPlace()) ;

                    if( (iTableNum)>19 ) {
                        if( (iTableNum%10) == 0 ) {
                            hmPlaySequence.put(iPlaySequece, playTensPlaceTableNum );
                            iPlaySequece++;
                        } else {
                            hmPlaySequence.put(iPlaySequece, playTensPlaceTableNum );
                            iPlaySequece++;
                            hmPlaySequence.put(iPlaySequece, playOnesPlaceTableNum );
                            iPlaySequece++;
                        }
                    }  else {
                        hmPlaySequence.put(iPlaySequece, playOnesPlaceTableNum );
                        iPlaySequece++;
                    }
                    hmPlaySequence.put(iPlaySequece, playSilenceHalfSecond );
                    iPlaySequece++;
                    sTableNumberMessage = sTableNumberMessage + " table "
                            + tableGuestBean.getTableNum();
                    isFirst = false;
                }

			} else {
                sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature( eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER );
                if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) ) {
                    playSeatingMessage = new Play( VOICE_PATH+"_please_hold_transfer_call_to_seating_coordinator.wav" );
                    isCallForward = true;
                } else {
                    playSeatingMessage = new Play( VOICE_PATH+"_i_am_sorry_unable_to_retrieve_your_information.wav" );
                }
			}

            Play playThankYouEnjoyDay = new Play(VOICE_PATH+"_thank_you.wav");

          //  Play playQuarterSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/quarter_second_stereo.wav");
            try {
				response.append(playWelcome);

				//
                response.append(playSeatingMessage);
                if(iPlaySequece>0)
                {
                    for(int i = 0; i<iPlaySequece; i++ )
                    {
                        response.append( hmPlaySequence.get(i) );
                    }
                }
				if (isCallForward) {
					response.append(callForwardUsher(sCallForwaringNum));
				}

				response.append(playThankYouEnjoyDay);

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
				telephonyLogging.info("Twilio exception while trying to get Seating response" + ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}

    public CallResponse getCallForwardingResponse(CallResponse callResponse,IncomingCallBean incomingCallBean,Constants.EVENT_FEATURES eventFeatures ) {
        if (callResponse != null && incomingCallBean!=null) {
            EventBean eventBean = callResponse.getEventBean();

            TwiMLResponse response = new TwiMLResponse();

            Play playWelcome = new Play(VOICE_PATH+"_hello_and_welcome_to_callseat.wav");

            boolean isCallForward = false;
            String sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature(eventBean.getEventId(), eventFeatures );

            Play playMessage = null;
            if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) ) {
                if(eventFeatures.getEventFeature().equalsIgnoreCase(Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER.getEventFeature())) {
                    playMessage = new Play( VOICE_PATH+"_please_hold_while_transfer_to_rsvp_coordinator.wav" );
                    isCallForward = true;
                } else if( eventFeatures.getEventFeature().equalsIgnoreCase(Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER.getEventFeature()) ) {
                    playMessage = new Play( VOICE_PATH+"_please_hold_transfer_call_to_seating_coordinator.wav" );
                    isCallForward = true;
                }
            }  else {
                playMessage = new Play( VOICE_PATH+"_i_am_sorry_unable_to_process_your_request.wav" );
                isCallForward = false;
            }

            Play playThankYouEnjoyDay = new Play(VOICE_PATH+"_thank_you_and_enjoy_your_day.wav");
            Play playHalfSecondDelaySilence = new Play(VOICE_PATH+"_silence_0.5.wav");
            try {
                Pause pause = new Pause();
                pause.setLength(1);

                response.append(playMessage);

                response.append(pause);

                response.append(playMessage);
                response.append(pause);

                if (isCallForward) {
                    response.append(callForwardUsher(sCallForwaringNum));
                }

                response.append(playThankYouEnjoyDay);

                callResponse.setResponse(response);
                callResponse.setTwilResponseSuccess(true);

            }
            catch (TwiMLException e)
            {
                callResponse.setTwilResponseSuccess(false);
                telephonyLogging.info("DEMO - Twilio exception while doing call forward " + ExceptionHandler.getStackTrace(e));
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

            Integer iTotalInvitedSeats = ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats());

            Play playThankYouForResponse = new Play(VOICE_PATH+"_thank_you.wav") ;


			try {
				TwiMLResponse response = new TwiMLResponse();


                telephonyLogging.error("DEMO - Building Voice to gather RSVP exception occurred when trying to RSVP Respond to guest " +
                        " From : " + twilioIncomingBean.getFrom()  + " To : " + twilioIncomingBean.getFrom() + " Invited seats : " + iTotalInvitedSeats );
                Gather gatherRsvp = RsvpTwiml.getGatherRSVPVerb(iTotalInvitedSeats,twilioIncomingBean,Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM) ;
                if( gatherRsvp!=null )  {
                    response.append(gatherRsvp);
                }  else {
                    telephonyLogging.error("Invalid Gather Verb encountered " +
                            " From : " + twilioIncomingBean.getFrom()  + " To : " + twilioIncomingBean.getFrom() + " Invited seats : " + iTotalInvitedSeats );
                }

				response.append(playThankYouForResponse);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				telephonyLogging.error("Twilio exception occurred when trying to RSVP Respond to guest " +ExceptionHandler.getStackTrace(e));
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
			Gather gatherSecretKey = new Gather();

			gatherSecretKey.setMethod("POST");
			gatherSecretKey.setAction(TwimlSupport.buildURL(twilioIncomingBean,
					Constants.CALL_TYPE.DEMO_GATHER_SECRET_KEY).toString());



			try {
				TwiMLResponse response = new TwiMLResponse();

				if (iNumOfCallAttempt > 0) {
                    telephonyLogging.info("User entered wrong extension number : attempt : " + iNumOfCallAttempt + " From : "
                            + twilioIncomingBean.getFrom() + " To : " + twilioIncomingBean.getTo() );
                    Play playSorryInvalid = new Play(VOICE_PATH+"_i_am_sorry_unable_to_recognize_extension_number.wav");
					response.append(playSorryInvalid);
				}

                telephonyLogging.info("Build Voice to gathering Extension Number from user : attempt : " + iNumOfCallAttempt +
                        " From : " + twilioIncomingBean.getFrom()  + " To : " + twilioIncomingBean.getFrom());
                Play playGatherSecretKey = new Play(VOICE_PATH+"_please_enter_extension_number_followed_by_pound.wav");
				gatherSecretKey.append(playGatherSecretKey);
				response.append(gatherSecretKey);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
                telephonyLogging.error("Twilio exception occurred when trying to gather the extension number : " + ExceptionHandler.getStackTrace(e));
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
			try {
				TwiMLResponse response = new TwiMLResponse();
				if (iNumOfCallAttempt == 0) {
                    telephonyLogging.info("Hello and Welcome to Callseat invoked : From " + twilioIncomingBean.getFrom() + " To(Event Number) : " + twilioIncomingBean.getTo() );
                    Play playWelcome = new Play(VOICE_PATH+"_hello_and_welcome_to_callseat.wav");
                    response.append(playWelcome);
				} else {
                    telephonyLogging.info("User entered a wrong seating id : From " + twilioIncomingBean.getFrom() + " To(Event Number) : " + twilioIncomingBean.getTo() );
                    Play playSorryInvalid = new Play(VOICE_PATH+"_i_am_sorry_unable_to_recognize_seating_plan_id.wav");
					response.append(playSorryInvalid);
				}

                telephonyLogging.info("Build voice to gather seating id : From " + twilioIncomingBean.getFrom() + " To(Event Number) : " + twilioIncomingBean.getTo() );

                Gather gatherEventNum = new Gather();
                gatherEventNum.setMethod("POST");
                gatherEventNum.setAction(TwimlSupport.buildURL(twilioIncomingBean,Constants.CALL_TYPE.DEMO_GATHER_EVENT_NUM).toString());


                Play playGatherEventNumber = new Play(VOICE_PATH+"_please_enter_seating_plan_id_followed_by_pound.wav");
                gatherEventNum.append(playGatherEventNumber);

				response.append(gatherEventNum);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				telephonyLogging.error("Twilio Exception occurred when first request came in " + ExceptionHandler.getStackTrace(e));
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

                EventBean eventBean = callResponse.getEventBean();
                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventBean.getEventId() );
                informGuestBean.setAdminId( eventBean.getEventAdminId() );
                informGuestBean.setGuestId( eventGuestBean.getGuestId() );
                informGuestBean.setEventTask( Constants.EVENT_TASK.RSVP );

                InformGuestTask.sendRSVPConfirmation(informGuestBean);
			} catch (TwiMLException e) {
				callResponse.setTwilResponseSuccess(false);
				telephonyLogging.error("Demo - Twilio exception for RSVP digit success : " +ExceptionHandler.getStackTrace(e));
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

                int iTotalNumberOfSeats =  ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats() );

                Play playThankYou = new Play(VOICE_PATH+"_thank_you.wav");

				try {
                    telephonyLogging.info("DEMO - Creating the Gather RSVP Verb : Total Invited " + iTotalNumberOfSeats + " From " + twilioIncomingBean.getFrom() + " To(Event Number) : " + twilioIncomingBean.getTo() );
                    Gather gatherRsvp = RsvpTwiml.getGatherRSVPVerb(iTotalNumberOfSeats, twilioIncomingBean,Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM);
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
					telephonyLogging.info(ExceptionHandler.getStackTrace(e));
				}

			}
		}
		return callResponse;
	}

}
