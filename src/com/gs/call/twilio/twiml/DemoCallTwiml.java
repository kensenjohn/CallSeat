package com.gs.call.twilio.twiml;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.bean.*;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.common.*;
import com.gs.common.exception.ExceptionHandler;
import com.gs.manager.event.EventFeatureManager;
import com.gs.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;

public class DemoCallTwiml {
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String VOICE_ACTOR = applicationConfig.get(Constants.PROP_TWILIO_VOICE);
    //private String VOICE_RECORDING_DOMAIN = applicationConfig.get(Constants.PROP_VOICE_RECORDING_DOMAIN);
    private String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);
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

			//Say sayWelcome = new Say("Welcome");
			//sayWelcome.setVoice(VOICE_ACTOR);
            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

			ArrayList<TableGuestsBean> arrTableGuestBean = callResponse
					.getArrTableGuestBean();

            Play playSeatingMessage = null;
            String sCallForwaringNum = "";
			String sSeatingMessage = "";
			boolean isFirst = true;
            HashMap<Integer,Play> hmPlaySequence = new HashMap<Integer,Play>();
            Integer iPlaySequece = 0;
			if (arrTableGuestBean != null && !arrTableGuestBean.isEmpty()) {
                int totalNumOfSeats = 0;
                String sTableNumberMessage = "";
                for (TableGuestsBean tableGuestBean : arrTableGuestBean) {
					int numOfSeats = ParseUtil.sToI(tableGuestBean
							.getGuestAssignedSeats());
                    totalNumOfSeats = totalNumOfSeats + numOfSeats;
                }
                if(totalNumOfSeats>0)
                {
                    if (totalNumOfSeats == 1)
                    {
                        sSeatingMessage = "You are seated at ";
                        playSeatingMessage = new Play( VOICE_RECORDING_FOLDER+"/you_are_seated_at_table_stereo.wav" );
                    }
                    else if( totalNumOfSeats == 2 )
                    {
                        sSeatingMessage = "You and your guest are seated at ";
                        playSeatingMessage = new Play( VOICE_RECORDING_FOLDER+"/you_and_your_guest_are_seated_at_table_stereo.wav" );
                    }
                    else if( totalNumOfSeats > 2 )
                    {
                        sSeatingMessage = "You and your guests are seated at ";
                        playSeatingMessage = new Play( VOICE_RECORDING_FOLDER+"/you_and_guests_are_seated_at_table_stereo.wav" );
                    }
                    sSeatingMessage = sSeatingMessage + sTableNumberMessage;
                }
                else
                {
                    sSeatingMessage = "We are sorry we were unable to retrieve your information. Please call again later.";
                    playSeatingMessage = new Play( VOICE_RECORDING_FOLDER+"/we_were_unable_to_retrieve_your_info_stereo.wav" );
                }

                Play playTenthOfSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/tenth_of_a_second_stereo.wav") ;
                Play playTableData = new Play( VOICE_RECORDING_FOLDER+"/table_stereo.wav" );
                for (TableGuestsBean tableGuestBean : arrTableGuestBean) {
                    if (!isFirst) {
                        sTableNumberMessage = sTableNumberMessage + ", ";

                        hmPlaySequence.put(iPlaySequece, playTableData );
                        iPlaySequece++;
                    }


                    //playSeatingMessage.append( playTableData );

                    int iTableNum = ParseUtil.sToI( tableGuestBean.getTableNum());
                    NumberVoiceBean numberVoiceTableNum = Utility.getNumberInVoice(  iTableNum );

                    Play playTensPlaceTableNum = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTableNum.getTensPlace()) ;
                    Play playOnesPlaceTableNum = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTableNum.getOnesPlace()) ;

                    if( (iTableNum)>19 )
                    {
                        if( (iTableNum%10) == 0 )
                        {
                            hmPlaySequence.put(iPlaySequece, playTensPlaceTableNum );
                            iPlaySequece++;
                            //playSeatingMessage.append( playTensPlaceTableNum );
                        }
                        else
                        {
                            hmPlaySequence.put(iPlaySequece, playTensPlaceTableNum );
                            iPlaySequece++;
                            hmPlaySequence.put(iPlaySequece, playOnesPlaceTableNum );
                            iPlaySequece++;
                            // playSeatingMessage.append( playTensPlaceTableNum );
                           // playSeatingMessage.append( playOnesPlaceTableNum );
                        }

                    }
                    else
                    {
                        hmPlaySequence.put(iPlaySequece, playOnesPlaceTableNum );
                        iPlaySequece++;
                        //playSeatingMessage.append( playOnesPlaceTableNum );
                    }
                    hmPlaySequence.put(iPlaySequece, playTenthOfSecondDelay );
                    iPlaySequece++;
                    sTableNumberMessage = sTableNumberMessage + " table "
                            + tableGuestBean.getTableNum();
                    isFirst = false;
                }

			} else {
                sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature( eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER );
                if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) )
                {
                    sSeatingMessage = "Your call will be forwarded to an usher.";
                    playSeatingMessage = new Play( VOICE_RECORDING_FOLDER+"/your_call_will_be_forwarded_stereo.wav" );
                    isCallForward = true;
                }
			}

			//Say sayMessage = new Say(sSeatingMessage);
			//sayMessage.setVoice(VOICE_ACTOR);

			//Say sayThankYou = new Say("Thank You, and enjoy your day.");
			//sayThankYou.setVoice(VOICE_ACTOR);
            Play playThankYouEnjoyDay = new Play(VOICE_RECORDING_FOLDER+"/thank_you_enjoy_your_day_stereo.wav");

            Play playQuarterSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/quarter_second_stereo.wav");
            try {
				response.append(playWelcome);
				response.append(playQuarterSecondDelay);

				//
                response.append(playSeatingMessage);
                if(iPlaySequece>0)
                {
                    for(int i = 0; i<iPlaySequece; i++ )
                    {
                        response.append( hmPlaySequence.get(i) );
                    }
                }
				//response.append(pause);
                response.append(playQuarterSecondDelay);
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

            //Say sayWelcome = new Say("Welcome");
            //sayWelcome.setVoice(VOICE_ACTOR);
            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

            boolean isCallForward = false;
            //String sMessage = "";
            String sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature(eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER);

            Play playMessage = null;
            if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) )
            {
               // sMessage = "An usher will assist you. Please wait for the call to be forwarded.";
                playMessage = new Play( VOICE_RECORDING_FOLDER+"/your_call_will_be_forwarded_stereo.wav" );
                isCallForward = true;
            }
            else
            {
              //  sMessage = "We are sorry we were unable to process your RSVP. Please call again later.";
                playMessage = new Play( VOICE_RECORDING_FOLDER+"/unable_to_process_rsvp_call_again_stereo.wav" );
                isCallForward = false;
            }

            // Say sayMessage = new Say(sMessage);
            // sayMessage.setVoice(VOICE_ACTOR);

            //Say sayThankYou = new Say("Thank You, and enjoy your day.");
            //sayThankYou.setVoice(VOICE_ACTOR);
            Play playThankYouEnjoyDay = new Play(VOICE_RECORDING_FOLDER+"/thank_you_enjoy_your_day_stereo.wav");
            try
            {
                //Verb pauseVerb = new Verb(Verb.V_PAUSE,null);
                Pause pause = new Pause();
                pause.setLength(1);

                response.append(playMessage);

                response.append(pause);

                response.append(playMessage);
                response.append(pause);

                if (isCallForward)
                {
                    response.append(callForwardUsher(sCallForwaringNum));
                }

                response.append(playThankYouEnjoyDay);

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

            //Say sayWelcome = new Say("Welcome");
            //sayWelcome.setVoice(VOICE_ACTOR);
            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
					Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM).toString());

            Integer iTotalInvitedSeats = ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats());

            String sInfoMessage = "You have been been invited to ";

            Play playYouHaveBeenInvited = new Play(VOICE_RECORDING_FOLDER+"/you_have_been_invited_to_stereo.wav") ;
            Play playSingleSeat = new Play(VOICE_RECORDING_FOLDER+"/single_seat_stereo.wav") ;
            Play playMultipleSeats = new Play(VOICE_RECORDING_FOLDER+"/multiple_seats_stereo.wav") ;

            NumberVoiceBean upperLimitSeats = Utility.getNumberInVoice(iTotalInvitedSeats + 1);

            Play playTensPlaceUpperLimitSeats = new Play(VOICE_RECORDING_FOLDER+"/"+upperLimitSeats.getTensPlace()) ;
            Play playOnesPlaceUpperLimitSeats = new Play(VOICE_RECORDING_FOLDER+"/"+upperLimitSeats.getOnesPlace()) ;


            NumberVoiceBean invitedSeats = Utility.getNumberInVoice(iTotalInvitedSeats);

            Play playSelectRsvpNumber = new Play(VOICE_RECORDING_FOLDER+"/please_select_rsvp_less_than_stereo.wav") ;
            Play playTensPlaceInvitedSeats = new Play(VOICE_RECORDING_FOLDER+"/"+invitedSeats.getTensPlace()) ;
            Play playOnesPlaceInvitedSeats = new Play(VOICE_RECORDING_FOLDER+"/"+invitedSeats.getOnesPlace()) ;
            Play playFollowedByPoundSign = new Play(VOICE_RECORDING_FOLDER+"/followed_by_pound_sign_stereo.wav") ;

            Play playThankYouForResponse = new Play(VOICE_RECORDING_FOLDER+"/thank_you_for_response_stereo.wav") ;

            Play playQuarterSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/quarter_second_stereo.wav") ;
            Play playTenthOfSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/tenth_of_a_second_stereo.wav") ;

			/*Say sayInfo = new Say(
                    sInfoMessage + " To RSVP please select a number from 0 to " + iTotalInvitedSeats + " followed by the pound sign." );
			sayInfo.setVoice(VOICE_ACTOR);
			// sayInfo.setLoop(3);

			Say sayThankYou = new Say("Thank You for your response.");
			sayThankYou.setVoice(VOICE_ACTOR);   */

			try {
				TwiMLResponse response = new TwiMLResponse();
                gatherRsvp.append(playWelcome);

                gatherRsvp.append( playYouHaveBeenInvited );
                if( (iTotalInvitedSeats)>19 )
                {
                    if( ((iTotalInvitedSeats)%10) == 0 )
                    {
                        gatherRsvp.append( playTensPlaceInvitedSeats );
                    }
                    else
                    {
                        gatherRsvp.append( playTensPlaceInvitedSeats );
                        gatherRsvp.append( playOnesPlaceInvitedSeats );
                    }

                }
                else
                {
                    gatherRsvp.append( playOnesPlaceInvitedSeats );
                }
                gatherRsvp.append( playTenthOfSecondDelay );
                if( iTotalInvitedSeats<=1 )
                {
                    gatherRsvp.append( playSingleSeat );
                }
                else
                {
                    gatherRsvp.append( playMultipleSeats );
                }
                gatherRsvp.append( playQuarterSecondDelay );

                gatherRsvp.append( playSelectRsvpNumber );
                gatherRsvp.append( playTenthOfSecondDelay );

                if( (iTotalInvitedSeats+1)>19 )
                {
                    if( ((iTotalInvitedSeats+1)%10) == 0 )
                    {
                        gatherRsvp.append( playTensPlaceUpperLimitSeats );
                    }
                    else
                    {
                        gatherRsvp.append( playTensPlaceUpperLimitSeats );
                        gatherRsvp.append( playOnesPlaceUpperLimitSeats );
                    }

                }
                else
                {
                    gatherRsvp.append( playOnesPlaceUpperLimitSeats );
                }

                gatherRsvp.append( playQuarterSecondDelay );

                gatherRsvp.append( playFollowedByPoundSign );
				response.append(gatherRsvp);
				response.append(playThankYouForResponse);

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

            Play playSorryInvalid = new Play(VOICE_RECORDING_FOLDER+"/i_am_sorry_stereo.wav");

			Gather gatherSecretKey = new Gather();

			gatherSecretKey.setMethod("POST");
			gatherSecretKey.setAction(TwimlSupport.buildURL(twilioIncomingBean,
					Constants.CALL_TYPE.DEMO_GATHER_SECRET_KEY).toString());

			//Say sayGetSecretKey = new Say("Please enter your secret key followed by the pound sign.");
			//sayGetSecretKey.setVoice(VOICE_ACTOR);
            Play playGatherSecretKey = new Play(VOICE_RECORDING_FOLDER+"/please_enter_secret_key_stereo.wav");

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

            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

			//Say saySorryWrongEvent = new Say("I am sorry, The event number was not valid.");
			//saySorryWrongEvent.setVoice(VOICE_ACTOR);

            Play playSorryInvalid = new Play(VOICE_RECORDING_FOLDER+"/i_am_sorry_stereo.wav");

			Gather gatherEventNum = new Gather();

			gatherEventNum.setMethod("POST");
			URLCodec urlEncoder = new URLCodec();
			gatherEventNum.setAction(TwimlSupport.buildURL(twilioIncomingBean,Constants.CALL_TYPE.DEMO_GATHER_EVENT_NUM).toString());

			//Say sayGetEventNum = new Say("Please enter the event number followed by the pound sign.");
			//sayGetEventNum.setVoice(VOICE_ACTOR);

            Play playGatherEventNumber = new Play(VOICE_RECORDING_FOLDER+"/please_enter_event_number_stereo.wav");


			try {
				TwiMLResponse response = new TwiMLResponse();
				gatherEventNum.append(playGatherEventNumber);
				if (iNumOfCallAttempt == 0) {
					//response.append(playWelcome);
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

			//Say sayThankYou = new Say("Your RSVP response of " + eventGuestBean.getRsvpSeats() + " has been accepted.");
			//sayThankYou.setVoice(VOICE_ACTOR);
            Play playThankYouYourRsvpResponse= new Play(VOICE_RECORDING_FOLDER+"/your_rsvp_response_of_stereo.wav");
            int iRsvpSeats =  ParseUtil.sToI(eventGuestBean.getRsvpSeats() );
            NumberVoiceBean numberVoiceRsvpSeats = Utility.getNumberInVoice( iRsvpSeats );


            Play playTensPlaceIRSVPSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceRsvpSeats.getTensPlace()) ;
            Play playOnesPlaceRSVPSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceRsvpSeats.getOnesPlace()) ;

            Play playHasBeenAccepted= new Play(VOICE_RECORDING_FOLDER+"/has_been_accepted_stereo.wav");

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean, Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM).toString());


            Play playToChangeYourRsvp = new Play(VOICE_RECORDING_FOLDER+"/to_change_your_rsvp_stereo.wav");
            Play playPleaseSelectNumfrom = new Play(VOICE_RECORDING_FOLDER+"/please_select_number_zero_to_stereo.wav");
            int iTotalNumberOfSeats =  ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats() );
            NumberVoiceBean numberVoiceTotalSeats = Utility.getNumberInVoice( iTotalNumberOfSeats );
            Play playTensPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getTensPlace()) ;
            Play playOnesPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getOnesPlace()) ;

            Play playFollowedByPoundSign = new Play(VOICE_RECORDING_FOLDER+"/followed_by_pound_sign_stereo.wav");
            //Say sayInfo = new Say("To change your RSVP please select a number from 0 to " + eventGuestBean.getTotalNumberOfSeats()+ " followed by the pound sign.");
			//sayInfo.setVoice(VOICE_ACTOR);

			Hangup hangup = new Hangup();

			try {
				response.append( playThankYouYourRsvpResponse );
                if( (iRsvpSeats+1)>19 )
                {
                    if( ((iRsvpSeats+1)%10) == 0 )
                    {
                        response.append( playTensPlaceIRSVPSeats );
                    }
                    else
                    {
                        response.append( playTensPlaceIRSVPSeats );
                        response.append( playOnesPlaceRSVPSeats );
                    }

                }
                else
                {
                    response.append( playOnesPlaceRSVPSeats );
                }
                response.append( playHasBeenAccepted );

				gatherRsvp.append( playToChangeYourRsvp );
                gatherRsvp.append( playPleaseSelectNumfrom );

                if( (iTotalNumberOfSeats+1)>19 )
                {
                    if( ((iTotalNumberOfSeats+1)%10) == 0 )
                    {
                        gatherRsvp.append( playTensPlaceTotalSeats );
                    }
                    else
                    {
                        gatherRsvp.append( playTensPlaceTotalSeats );
                        gatherRsvp.append( playOnesPlaceTotalSeats );
                    }

                }
                else
                {
                    gatherRsvp.append( playOnesPlaceTotalSeats );
                }
                gatherRsvp.append(  playFollowedByPoundSign );

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
				//Say saySorry = new Say("We are sorry, " + sMessage + ".");
				// saySorry.setVoice(VOICE_ACTOR);
                Play playIAmSorry = new Play(VOICE_RECORDING_FOLDER+"/i_am_sorry_stereo.wav") ;

				Gather gatherRsvp = new Gather();

				gatherRsvp.setMethod("POST");
				gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
						Constants.CALL_TYPE.DEMO_GATHER_RSVP_NUM).toString());

                Play playYouCanHangupNow = new Play(VOICE_RECORDING_FOLDER+"/you_can_hangup_stereo.wav") ;
                Play playPleaseSelectNumber= new Play(VOICE_RECORDING_FOLDER+"/please_select_number_zero_to_stereo.wav") ;

                int iTotalNumberOfSeats =  ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats() );
                NumberVoiceBean numberVoiceTotalSeats = Utility.getNumberInVoice( iTotalNumberOfSeats );
                Play playTensPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getTensPlace()) ;
                Play playOnesPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getOnesPlace()) ;

                Play playFollowedByPoundSign = new Play(VOICE_RECORDING_FOLDER+"/followed_by_pound_sign_stereo.wav");

				//Say sayInfo = new Say( "You can hang up now or try again. Please select a number from zero to "	+ ParseUtil.sToI( eventGuestBean.getTotalNumberOfSeats() )	+ " followed by the pound sign.");
				//sayInfo.setVoice(VOICE_ACTOR);

                Play playThankYouForResponse = new Play(VOICE_RECORDING_FOLDER+"/thank_you_for_response_stereo.wav");
				//Say sayThankYou = new Say("Thank You for your response.");
				//sayThankYou.setVoice(VOICE_ACTOR);

				try {
					response.append(playIAmSorry);

					gatherRsvp.append(playYouCanHangupNow);
                    gatherRsvp.append(playPleaseSelectNumber);
                    if( (iTotalNumberOfSeats+1)>19 )
                    {
                        if( ((iTotalNumberOfSeats+1)%10) == 0 )
                        {
                            gatherRsvp.append( playTensPlaceTotalSeats );
                        }
                        else
                        {
                            gatherRsvp.append( playTensPlaceTotalSeats );
                            gatherRsvp.append( playOnesPlaceTotalSeats );
                        }

                    }
                    else
                    {
                        gatherRsvp.append( playOnesPlaceTotalSeats );
                    }
                    gatherRsvp.append(  playFollowedByPoundSign );

					response.append(gatherRsvp);
					response.append(playThankYouForResponse);

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
