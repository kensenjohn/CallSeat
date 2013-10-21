package com.gs.call.task;

import java.util.ArrayList;

import com.gs.bean.*;
import com.gs.common.Utility;
import com.gs.manager.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.call.twilio.twiml.DemoCallTwiml;
import com.gs.call.twilio.twiml.TwimlSupport;
import com.gs.common.Constants;
import com.gs.common.exception.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.data.event.EventData;
import com.twilio.sdk.verbs.TwiMLException;

import com.gs.common.CallTransaction;

public class DemoCallTask extends Task {

	//private Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
    //private Logger telephonyLogging = LoggerFactory.getLogger(Constants.TELEPHONY_LOGS);

	public DemoCallTask(String eventId, String adminId) {
		super(eventId, adminId);
	}

	@Override
	public CallResponse processTask(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (incomingCallBean != null) {
			DemoCallTwiml demoTwiml = new DemoCallTwiml();
            boolean isErrorEncountered = false;
			try {
				switch (incomingCallBean.getCallType()) {
				case DEMO_FIRST_REQUEST:
					callResponse = demoTwiml.getEventNumFromUser(callResponse,twilioIncomingCallBean);
					break;
				case DEMO_GATHER_EVENT_NUM:
                    ArrayList<TelNumberBean> arrTelNumBean = processSeatingPlanId(incomingCallBean);
                    if (arrTelNumBean != null && !arrTelNumBean.isEmpty())  {
                        twilioIncomingCallBean.setCallerInputEventId(twilioIncomingCallBean.getDigits());
                        callResponse = processTelephoneNumber(callResponse,	arrTelNumBean, twilioIncomingCallBean);
                        String sSeatingPlanMode = TwimlSupport.getSeatingPlanModeFromTelnumber(arrTelNumBean);

                        if (Constants.EVENT_SEATINGPLAN_MODE.RSVP.getMode().equalsIgnoreCase(sSeatingPlanMode))  {
                            if(callResponse!=null && callResponse.getEventGuestBean()!=null && !callResponse.isEventBeanExists() && !callResponse.isEventGuestBeanExists()){
                                telephonyLogging.info("Guest RSVP could not find valid event match. Does not return RSVP response : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                                callResponse = demoTwiml.getCallForwardingResponse(callResponse,incomingCallBean,Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER);
                            } else  {
                                telephonyLogging.info("Going to get voice to Gather guest RSVP  : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                                callResponse = demoTwiml.getRsvpResponse(callResponse, twilioIncomingCallBean);
                            }
                        } else if (Constants.EVENT_SEATINGPLAN_MODE.SEATING.getMode().equalsIgnoreCase(sSeatingPlanMode)) {
                            telephonyLogging.info("Guest request seating info  : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );

                            callResponse = processSeatingResponse(callResponse,	twilioIncomingCallBean);
                            callResponse = demoTwiml.getSeatingResponse(callResponse, twilioIncomingCallBean);
                        }

                    } else {
                        telephonyLogging.info("get Seating plan ID again because of error : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                        callResponse = demoTwiml.getEventNumFromUser( callResponse, twilioIncomingCallBean);
                    }
					break;
				case DEMO_GATHER_SECRET_KEY:
					/*ArrayList<TelNumberBean> arrTelNumBean = processSecretKey(incomingCallBean);

					twilioIncomingCallBean.setCallerInputSecretKey(twilioIncomingCallBean.getDigits());
					if (arrTelNumBean != null && !arrTelNumBean.isEmpty())  {
						callResponse = processTelephoneNumber(callResponse,	arrTelNumBean, twilioIncomingCallBean);
						String sTelNumType = TwimlSupport.getTelNumberType(arrTelNumBean);

                        if (Constants.EVENT_TASK.DEMO_RSVP.getTask().equalsIgnoreCase(sTelNumType))  {
                            telephonyLogging.info("Guest trying to  RSVP  : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );

                            if(callResponse!=null && callResponse.getEventGuestBean()!=null
                                    && !callResponse.isEventBeanExists() && !callResponse.isEventGuestBeanExists()){
                                telephonyLogging.info("Guest RSVP could not find valid event match. Does not return RSVP response : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                                callResponse = demoTwiml.getCallForwardingResponse(callResponse,incomingCallBean,Constants.EVENT_FEATURES.RSVP_CALL_FORWARD_NUMBER);
                            } else  {
                                telephonyLogging.info("Going to get voice to Gather guest RSVP  : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                                callResponse = demoTwiml.getRsvpResponse(callResponse, twilioIncomingCallBean);
                            }
						}  else if (Constants.EVENT_TASK.DEMO_SEATING.getTask().equalsIgnoreCase(sTelNumType)) {
                            telephonyLogging.info("Guest request seating info  : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );

                            callResponse = processSeatingResponse(callResponse,	twilioIncomingCallBean);
							callResponse = demoTwiml.getSeatingResponse(callResponse, twilioIncomingCallBean);
						} else  {

						}
					} else {
                        telephonyLogging.info("get Extension id again because of error : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                        callResponse = demoTwiml.getSecretKeyFromUser( callResponse, twilioIncomingCallBean);
					}    */
					break;
				case DEMO_GATHER_RSVP_NUM:
                    telephonyLogging.info("Gathering the RSVP digits that a guest enters : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                    callResponse = processRsvpDigits(callResponse,twilioIncomingCallBean);
					break;
				}
			} catch (TwiMLException e) {
                isErrorEncountered = true;
                telephonyLogging.error("Twiml exception encountered : "+ ExceptionHandler.getStackTrace(e));
			} catch (Exception e) {
                isErrorEncountered = true;
                telephonyLogging.error("Generic exception encountered : "+ ExceptionHandler.getStackTrace(e));
			}
            finally
            {
                if(isErrorEncountered)
                {
                    callResponse = TwimlSupport.getStandardError(callResponse,twilioIncomingCallBean);
                }
            }
		}
		return callResponse;
	}

	private ArrayList<TelNumberBean> processSecretKey( IncomingCallBean incomingCallBean )
    {
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (twilioIncomingCallBean != null && !Utility.isNullOrEmpty(twilioIncomingCallBean.getDigits() ) )
        {
			TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setSecretEventSecretKey(ParseUtil.checkNull(twilioIncomingCallBean.getDigits()));
			telNumMetaData.setSecretEventIdentifier(ParseUtil.checkNull(twilioIncomingCallBean.getCallerInputEventId()));

			TelNumberManager telNumManager = new TelNumberManager();
			arrTelNumBean = telNumManager.getTelNumbersFromSecretEventNumAndKey(telNumMetaData);

            telephonyLogging.info("DEMO - Processing Extension number - seating plan id" + telNumMetaData.getSecretEventIdentifier() +  " extension number : " +  telNumMetaData.getSecretEventSecretKey() + ". Associated Numbers from event " + ParseUtil.checkNullObject(arrTelNumBean)
                    + " : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
		}
		return arrTelNumBean;
	}

	private ArrayList<TelNumberBean> processSeatingPlanId(IncomingCallBean incomingCallBean) {
        ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (twilioIncomingCallBean != null && !Utility.isNullOrEmpty(twilioIncomingCallBean.getDigits()) ) {


            TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setSecretEventIdentifier(ParseUtil.checkNull(twilioIncomingCallBean.getDigits()));

			TelNumberManager telNumManager = new TelNumberManager();
			arrTelNumBean = telNumManager.getTelNumbersFromSecretEventNum(telNumMetaData);

            telephonyLogging.info("Processing Seating plan Id " + telNumMetaData.getSecretEventIdentifier() +  ". It is " + ((arrTelNumBean!=null && !arrTelNumBean.isEmpty())?"valid" : " NOT valid " )
                    + " : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
        } else {
            telephonyLogging.error("Invalid request to process the Seating plan ID entered by the guest " + ParseUtil.checkNullObject(twilioIncomingCallBean)  );
        }

		return arrTelNumBean;
	}

	private CallResponse processSeatingResponse(CallResponse callResponse,IncomingCallBean incomingCallBean) {

		String sGuestTelNumber = incomingCallBean.getFrom();

		TelNumberMetaData telNumMetaData = new TelNumberMetaData();
		telNumMetaData.setGuestTelNumber(sGuestTelNumber);
		telNumMetaData.setAdminId(super.adminId);
		telNumMetaData.setEventId(super.eventId);
		TelNumberManager telNumManager = new TelNumberManager();
		EventGuestBean eventGuestBean = telNumManager.getTelNumGuestDetails(telNumMetaData);

		EventData eventData = new EventData();
		EventBean eventBean = eventData.getEvent(eventGuestBean.getEventId());

		GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
		guestTableMetaData.setGuestId(eventGuestBean.getGuestId());
        guestTableMetaData.setEventId( eventBean.getEventId() );

		GuestTableManager guestTableManager = new GuestTableManager();
		ArrayList<TableGuestsBean> arrTableGuestBean = guestTableManager.getGuestsAssignments(guestTableMetaData);

		callResponse.setEventGuestBean(eventGuestBean);
		callResponse.setEventBean(eventBean);
		callResponse.setArrTableGuestBean(arrTableGuestBean);

		return callResponse;
	}

	private CallResponse processRsvpDigits(CallResponse callResponse,
			IncomingCallBean incomingCallBean) {
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (twilioIncomingCallBean != null
				&& twilioIncomingCallBean.getDigits() != null
				&& !"".equalsIgnoreCase(twilioIncomingCallBean.getDigits())) {
			String rsvpNum = twilioIncomingCallBean.getDigits();

			String sGuestTelNumber = twilioIncomingCallBean.getFrom();

			TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setGuestTelNumber(sGuestTelNumber);
			telNumMetaData.setAdminId(super.adminId);
			telNumMetaData.setEventId(super.eventId);
			telNumMetaData.setDigits(twilioIncomingCallBean.getDigits());

			TelNumberManager telNumManager = new TelNumberManager();
			EventGuestBean tmpEventGuestBean = telNumManager.getTelNumGuestDetails(telNumMetaData);

			int iTotalSeats = ParseUtil.sToI(tmpEventGuestBean.getTotalNumberOfSeats());
			int iRsvpSeatsSel = ParseUtil.sToI(rsvpNum);


            ArrayList<String> arrGuestID = new ArrayList<String>();
            arrGuestID.add(tmpEventGuestBean.getGuestId());

            EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
            eventGuestMetaData.setEventId(super.eventId);
            eventGuestMetaData.setRsvpDigits(twilioIncomingCallBean
                    .getDigits());
            eventGuestMetaData.setArrGuestId(arrGuestID);

            EventGuestManager eventGuestManager = new EventGuestManager();

			DemoCallTwiml demoTwiml = new DemoCallTwiml();

			if (iRsvpSeatsSel <= iTotalSeats) {


                eventGuestManager.setGuestRsvpForEvent(eventGuestMetaData);
                EventGuestBean eventGuestBean = eventGuestManager.getGuest(eventGuestMetaData);

                callResponse.setEventGuestBean(eventGuestBean);

                EventManager eventManager = new EventManager();
                EventBean eventBean = eventManager.getEvent(eventGuestMetaData.getEventId());
                callResponse.setEventBean(eventBean);

				if (eventGuestBean.getRsvpSeats().equalsIgnoreCase(twilioIncomingCallBean.getDigits())) {
                    telephonyLogging.info("RSVP of guest was accepted => RSVP digits " + eventGuestBean.getRsvpSeats() + ": From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );

                    callResponse = demoTwiml.getRsvpDigitsSuccess(callResponse,"Your RSVP of "+ eventGuestBean.getRsvpSeats() + " has been accepted.",
							twilioIncomingCallBean);
				} else {
                    telephonyLogging.info("Unable to process Guests' RSVP : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );

                    callResponse = demoTwiml.getRsvpDigitsFail(callResponse,"We were unable to process your RSVP. Please call again later.",
									Constants.RSVP_STATUS.RSVP_UPDATE_FAIL, twilioIncomingCallBean);
				}
			} else {
                telephonyLogging.info("Guest entered RSVP greater than Invited seats : From " + twilioIncomingCallBean.getFrom() + " To(Event Number) : " + twilioIncomingCallBean.getTo() );
                EventGuestBean eventGuestBean = eventGuestManager
                        .getGuest(eventGuestMetaData);

                callResponse.setEventGuestBean(eventGuestBean);
                // RSVP selected has exceed the total invited seats.
				callResponse = demoTwiml.getRsvpDigitsFail(callResponse,"Please select a RSVP number less than or equal to " + iTotalSeats ,
								Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS, twilioIncomingCallBean);
			}

		}
		return callResponse;
	}

	private CallResponse processTelephoneNumber(CallResponse callResponse,ArrayList<TelNumberBean> arrTelNumBean,IncomingCallBean incomingCallBean) {
		if (arrTelNumBean != null && !arrTelNumBean.isEmpty()) {
			for (TelNumberBean telNumBean : arrTelNumBean) {
				String sEventId = telNumBean.getEventId();
				String sAdminId = telNumBean.getAdminId();
				String sGuestTelNumber = incomingCallBean.getFrom();

				super.adminId = sAdminId;
				super.eventId = sEventId;


                EventData eventData = new EventData();
                EventBean eventBean = eventData.getEvent( super.eventId );
                callResponse.setEventBean(eventBean);

				TelNumberMetaData telNumMetaData = new TelNumberMetaData();
				telNumMetaData.setGuestTelNumber(sGuestTelNumber);
				telNumMetaData.setAdminId(super.adminId);
				telNumMetaData.setEventId(super.eventId);
				TelNumberManager telNumManager = new TelNumberManager();
				EventGuestBean eventGuestBean = telNumManager.getTelNumGuestDetails(telNumMetaData);

                if(eventGuestBean!=null && eventGuestBean.getGuestId()!=null && !"".equalsIgnoreCase(eventGuestBean.getGuestId() ))
                {
                    telephonyLogging.debug("Event Guest Bean was identified. Set it in response. guest Id : " + eventGuestBean.getGuestId() + " Invited to " + eventGuestBean.getTotalNumberOfSeats() );
                    callResponse.setEventGuestBean(eventGuestBean);
                } else {
                    telephonyLogging.debug("Vould not idenify a valid Event Guest Bean : GuestCall Number " +sGuestTelNumber + " Event Id  " + super.eventId + " Admin Id : " + super.adminId );

                }

                CallTransactionBean callTransactionBean = new CallTransactionBean();
                callTransactionBean.setAdminId(super.adminId);
                callTransactionBean.setEventId(super.eventId);
                if(eventGuestBean!=null && !"".equalsIgnoreCase(eventGuestBean.getGuestId()))
                {
                    callTransactionBean.setGuestId(eventGuestBean.getGuestId());
                }
                CallTransaction.getInstance().updateTransaction(incomingCallBean,callTransactionBean );

			}
		}
		return callResponse;
	}
}
