package com.gs.call.twilio.twiml;

import java.util.ArrayList;
import java.util.HashMap;

import com.gs.bean.*;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.common.*;
import com.gs.common.exception.ExceptionHandler;
import com.gs.manager.event.EventFeatureManager;
import com.gs.call.task.InformGuestTask;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.call.CallResponse;

public class SeatingTwiml {
    private static Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private static String VOICE_RECORDING_FOLDER = applicationConfig.get(Constants.PROP_VOICE_RECORDING_FOLDER);
    private static String DEFAULT_VOICE = applicationConfig.get(Constants.PROP_VOICE_RECORDING_DEFAULT_VOICE);

    private static String VOICE_PATH = VOICE_RECORDING_FOLDER + "/"  + DEFAULT_VOICE;

	Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    Logger telephonyLogging = LoggerFactory.getLogger(Constants.TELEPHONY_LOGS);

	public CallResponse getFirstResponse(CallResponse callResponse,
                                         TwilioIncomingCallBean twilioIncomingBean) {
        Pause pause = new Pause();
		pause.setLength(1);

		boolean isCallForward = false;

		if (callResponse != null && callResponse.getEventGuestBean() != null)
		{
			EventBean eventBean = callResponse.getEventBean();
			TwiMLResponse response = new TwiMLResponse();

            Play playWelcome = new Play(VOICE_PATH+"_hello_and_welcome_to_callseat.wav");
            Play playIWillHelpYouFindYourTable = new Play(VOICE_PATH+"_i_will_help_you_find_your_table.wav");
            Play playHalfSecondSilence = new Play(VOICE_PATH+"_silence_0.5.wav");
            Play playTenthSecondSilence = new Play(VOICE_PATH+"_silence_0.10.wav");
            Play playQuarterSecondSilence = new Play(VOICE_PATH+"_silence_0.25.wav");

			ArrayList<TableGuestsBean> arrTableGuestBean = callResponse.getArrTableGuestBean();

            Play playSeatingMessage = null;
			String sSeatingMessage = "";
			boolean isFirst = true;
            String sCallForwaringNum = "";
            HashMap<Integer,Play> hmPlaySequence = new HashMap<Integer,Play>();
            Integer iPlaySequece = 0;
            if (arrTableGuestBean != null && !arrTableGuestBean.isEmpty()) {
                int totalNumOfSeats = 0;
                for (TableGuestsBean tableGuestBean : arrTableGuestBean) {
                    int numOfSeats = ParseUtil.sToI(tableGuestBean.getGuestAssignedSeats());
                    totalNumOfSeats = totalNumOfSeats + numOfSeats;
                }

                telephonyLogging.info("SEATING - Seating Info for guest Total Seats : " + totalNumOfSeats  + " From : "  + twilioIncomingBean.getFrom() + " To : " + twilioIncomingBean.getTo() );
                if(totalNumOfSeats>0) {
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
                    hmPlaySequence.put(iPlaySequece, playTenthSecondSilence );
                    iPlaySequece++;
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

            Play playThankYouEnjoyDay = new Play(VOICE_PATH+"_thank_you_and_enjoy_your_day.wav");

			try
			{
                response.append(playWelcome);
                if(!isCallForward) {
                    response.append(playIWillHelpYouFindYourTable);
                }
                response.append(playHalfSecondSilence);

                response.append(playSeatingMessage);
                if(iPlaySequece>0)
                {
                    for(int i = 0; i<iPlaySequece; i++ )
                    {
                        response.append( hmPlaySequence.get(i) );
                    }
                }

                response.append(playHalfSecondSilence);
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
				telephonyLogging.error("SEATING - TWilio Exception while generating the Seating response " + ExceptionHandler.getStackTrace(e));
			}
		}
		return callResponse;
	}

	private Verb callForwardUsher(String sForwardingNumber) {
		Dial dialUsher = new Dial(sForwardingNumber);
		dialUsher.setTimeout(60);
		return dialUsher;
	}
}
