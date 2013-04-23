package com.gs.call.twilio.twiml;

import com.gs.bean.EventBean;
import com.gs.bean.InformGuestBean;
import com.gs.bean.NumberVoiceBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.*;
import com.gs.manager.event.EventFeatureManager;
import com.gs.manager.event.EventManager;
import com.gs.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.EventGuestBean;
import com.gs.call.CallResponse;

public class RsvpTwiml
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	private String VOICE_ACTOR = applicationConfig.get(Constants.PROP_TWILIO_VOICE);
    private String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);

	public CallResponse getRsvpDigitsSuccess(CallResponse callResponse, String sMessage)
	{
		if (callResponse != null && callResponse.getEventGuestBean() != null)
		{
			EventGuestBean eventGuestBean = callResponse.getEventGuestBean();
			TwiMLResponse response = new TwiMLResponse();

			// Say sayThankYou = new Say("Your RSVP response of " + eventGuestBean.getRsvpSeats() + " has been accepted.");
			// sayThankYou.setVoice(VOICE_ACTOR);
            Play playThankYouYourRsvpResponse= new Play(VOICE_RECORDING_FOLDER+"/your_rsvp_response_of_stereo.wav");

            int iRsvpSeats =  ParseUtil.sToI(eventGuestBean.getRsvpSeats() );
            NumberVoiceBean numberVoiceRsvpSeats = Utility.getNumberInVoice(iRsvpSeats);


            Play playTensPlaceIRSVPSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceRsvpSeats.getTensPlace()) ;
            Play playOnesPlaceRSVPSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceRsvpSeats.getOnesPlace()) ;

            Play playHasBeenAccepted= new Play(VOICE_RECORDING_FOLDER+"/has_been_accepted_stereo.wav");

            Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
            gatherRsvp.setAction(TwimlSupport.buildURL(new TwilioIncomingCallBean(), Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

			//Say sayInfo = new Say(" To change your RSVP please select a number from 0 to "+ eventGuestBean.getTotalNumberOfSeats()
			//				+ " seats to RSVP followed by the pound or hash key.");
			//sayInfo.setVoice(VOICE_ACTOR);
            Play playToChangeYourRsvp = new Play(VOICE_RECORDING_FOLDER+"/to_change_your_rsvp_stereo.wav");
            Play playPleaseSelectNumfrom = new Play(VOICE_RECORDING_FOLDER+"/please_select_number_zero_to_stereo.wav");
            int iTotalNumberOfSeats =  ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats() );
            NumberVoiceBean numberVoiceTotalSeats = Utility.getNumberInVoice( iTotalNumberOfSeats );
            Play playTensPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getTensPlace()) ;
            Play playOnesPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getOnesPlace()) ;

            Play playFollowedByPoundSign = new Play(VOICE_RECORDING_FOLDER+"/followed_by_pound_sign_stereo.wav");


            Hangup hangup = new Hangup();

			try
			{
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


                EventManager eventManager = new EventManager();
                EventBean eventBean = eventManager.getEvent( eventGuestBean.getEventId() );
                //EventBean eventBean = callResponse.getEventBean();
                InformGuestBean informGuestBean = new InformGuestBean();
                informGuestBean.setEventId( eventGuestBean.getEventId() );
                informGuestBean.setAdminId(eventBean.getEventAdminId());
                informGuestBean.setGuestId(eventGuestBean.getGuestId());
                informGuestBean.setEventTask(Constants.EVENT_TASK.RSVP);

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
				//Say saySorry = new Say("I am sorry, " + sMessage + ".");
				//saySorry.setVoice(VOICE_ACTOR);
                Play playIAmSorry = new Play(VOICE_RECORDING_FOLDER+"/i_am_sorry_stereo.wav") ;
				Gather gatherRsvp = new Gather();

				gatherRsvp.setMethod("POST");
				//gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");
                gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean,
                        Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

                Play playYouCanHangupNow = new Play(VOICE_RECORDING_FOLDER+"/you_can_hangup_stereo.wav") ;
                Play playPleaseSelectNumber= new Play(VOICE_RECORDING_FOLDER+"/please_select_number_zero_to_stereo.wav") ;

                int iTotalNumberOfSeats =  ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats() );
                NumberVoiceBean numberVoiceTotalSeats = Utility.getNumberInVoice( iTotalNumberOfSeats );
                Play playTensPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getTensPlace()) ;
                Play playOnesPlaceTotalSeats = new Play(VOICE_RECORDING_FOLDER+"/"+numberVoiceTotalSeats.getOnesPlace()) ;

                Play playFollowedByPoundSign = new Play(VOICE_RECORDING_FOLDER+"/followed_by_pound_sign_stereo.wav");

                //Say sayInfo = new Say("You can hang up now or try again. Please select a number from zero to "  + ParseUtil.sToI( eventGuestBean.getTotalNumberOfSeats() )  + " followed by the pound sign.");
				//sayInfo.setVoice(VOICE_ACTOR);

                Play playThankYouForResponse = new Play(VOICE_RECORDING_FOLDER+"/thank_you_for_response_stereo.wav");

                //Say sayThankYou = new Say("Thank You for your response.");
				//sayThankYou.setVoice(VOICE_ACTOR);

				try
				{
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

            //Say sayWelcome = new Say("Welcome");
            //sayWelcome.setVoice(VOICE_ACTOR);
            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

            boolean isCallForward = false;
            String sMessage = "";
            String sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature(eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER);

            Play playMessage = null;
            if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) )
            {
                //sMessage = "Your call will be forwarded to an usher.";
                playMessage = new Play( VOICE_RECORDING_FOLDER+"/your_call_will_be_forwarded_stereo.wav" );
                isCallForward = true;
            }
            else
            {
                //sMessage = "We are sorry we were unable to process your RSVP. Please call again later.";
                playMessage = new Play( VOICE_RECORDING_FOLDER+"/unable_to_process_rsvp_call_again_stereo.wav" );
                isCallForward = false;
            }

            //Say sayMessage = new Say(sMessage);
            //sayMessage.setVoice(VOICE_ACTOR);

            //Say sayThankYou = new Say("Thank You, and enjoy your day.");
            //sayThankYou.setVoice(VOICE_ACTOR);
            Play playThankYouEnjoyDay = new Play(VOICE_RECORDING_FOLDER+"/thank_you_enjoy_your_day_stereo.wav");
            Play playQuarterSecondDelay = new Play(VOICE_RECORDING_FOLDER+"/quarter_second_stereo.wav");
            try
            {
                //Verb pauseVerb = new Verb(Verb.V_PAUSE,null);
                Pause pause = new Pause();
                pause.setLength(1);

                response.append(playWelcome);

                response.append(playQuarterSecondDelay);

                response.append(playMessage);
                response.append(playQuarterSecondDelay);

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



			//Say sayWelcome = new Say("Welcome");
			//sayWelcome.setVoice(VOICE_ACTOR);
            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

			Gather gatherRsvp = new Gather();

			gatherRsvp.setMethod("POST");
			//gatherRsvp.setAction("/IncomingCall?incoming_call_type=rsvp_ans");
            gatherRsvp.setAction(TwimlSupport.buildURL(twilioIncomingBean, Constants.CALL_TYPE.RSVP_DIGIT_RESP).toString());

            Integer iTotalInvitedSeats = ParseUtil.sToI(eventGuestBean.getTotalNumberOfSeats());

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

            /*String sInfoMessage = "You have been been invited to ";
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
			sayThankYou.setVoice(VOICE_ACTOR);*/

			try
			{
                /*
                TwiMLResponse response = new TwiMLResponse();
				response.append(sayWelcome);

				gatherRsvp.append(sayInfo);
				response.append(gatherRsvp);
				response.append(sayThankYou);

				callResponse.setResponse(response);
				callResponse.setTwilResponseSuccess(true);*/

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

			} catch (TwiMLException e)
			{
				callResponse.setTwilResponseSuccess(false);
				appLogging.info(ExceptionHandler.getStackTrace(e));
			}

		}
		return callResponse;
	}

}
