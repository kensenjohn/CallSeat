package com.gs.call.twilio.twiml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gs.bean.*;
import com.gs.common.*;
import com.gs.manager.event.EventFeatureManager;
import com.gs.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.call.CallResponse;

public class SeatingTwiml
{
	Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

	//private String VOICE_ACTOR = applicationConfig.get(Constants.PROP_TWILIO_VOICE);

    private String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);

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

			//Say sayWelcome = new Say("Welcome");
			//sayWelcome.setVoice(VOICE_ACTOR);
            Play playWelcome = new Play(VOICE_RECORDING_FOLDER+"/welcome_stereo.wav");

			ArrayList<TableGuestsBean> arrTableGuestBean = callResponse.getArrTableGuestBean();

            Play playSeatingMessage = null;
			String sSeatingMessage = "";
			boolean isFirst = true;
            String sCallForwaringNum = "";
            HashMap<Integer,Play> hmPlaySequence = new HashMap<Integer,Play>();
            Integer iPlaySequece = 0;
            if (arrTableGuestBean != null && !arrTableGuestBean.isEmpty())
			{
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

                        hmPlaySequence.put(iPlaySequece, playTableData );
                        iPlaySequece++;
                    }

                    int iTableNum = ParseUtil.sToI( tableGuestBean.getTableNum());
                    NumberVoiceBean numberVoiceTableNum = Utility.getNumberInVoice(iTableNum);

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

                    isFirst = false;
                }
			}
            else
			{
                sCallForwaringNum = EventFeatureManager.getStringValueFromEventFeature( eventBean.getEventId(), Constants.EVENT_FEATURES.SEATING_CALL_FORWARD_NUMBER );
                if( sCallForwaringNum!=null && !"".equalsIgnoreCase(sCallForwaringNum) )
                {
                    sSeatingMessage = "Your call will now be forwarded to an usher. The usher will provide you with more assistance.";
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

			try
			{
                response.append(playWelcome);
                response.append(playQuarterSecondDelay);

                response.append(playSeatingMessage);
                if(iPlaySequece>0)
                {
                    for(int i = 0; i<iPlaySequece; i++ )
                    {
                        response.append( hmPlaySequence.get(i) );
                    }
                }

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

			} catch (TwiMLException e)
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
}
