package com.gs.task;

import java.util.ArrayList;

import com.gs.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.call.twilio.twiml.DemoCallTwiml;
import com.gs.call.twilio.twiml.TwimlSupport;
import com.gs.common.Constants;
import com.gs.common.ExceptionHandler;
import com.gs.common.ParseUtil;
import com.gs.data.event.EventData;
import com.gs.manager.event.EventGuestManager;
import com.gs.manager.event.EventGuestMetaData;
import com.gs.manager.event.GuestTableManager;
import com.gs.manager.event.GuestTableMetaData;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import com.twilio.sdk.verbs.TwiMLException;

import com.gs.common.CallTransaction;

public class DemoCallTask extends Task {

	private Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public DemoCallTask(String eventId, String adminId) {
		super(eventId, adminId);
	}

	@Override
	public CallResponse processTask(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (incomingCallBean != null) {
			DemoCallTwiml demoTwiml = new DemoCallTwiml();
			try {
				switch (incomingCallBean.getCallType()) {
				case DEMO_FIRST_REQUEST:
					callResponse = demoTwiml.getEventNumFromUser(callResponse,twilioIncomingCallBean);
					break;
				case DEMO_GATHER_EVENT_NUM:
					boolean isValidEventNum = processEventNum(incomingCallBean);
					if (isValidEventNum) {
						twilioIncomingCallBean.setCallAttemptNumber(0);
						twilioIncomingCallBean
								.setCallerInputEventId(twilioIncomingCallBean
										.getDigits());
						callResponse = demoTwiml.getSecretKeyFromUser(
								callResponse, twilioIncomingCallBean);
					} else {
						callResponse = demoTwiml.getEventNumFromUser(
								callResponse, twilioIncomingCallBean);
					}
					break;
				case DEMO_GATHER_SECRET_KEY:
					ArrayList<TelNumberBean> arrTelNumBean = processSecretKey(incomingCallBean);

					twilioIncomingCallBean.setCallerInputSecretKey(twilioIncomingCallBean.getDigits());
					if (arrTelNumBean != null && !arrTelNumBean.isEmpty())
                    {
						callResponse = processTelephoneNumber(callResponse,	arrTelNumBean, twilioIncomingCallBean);
						String sTelNumType = TwimlSupport.getTelNumberType(arrTelNumBean);

                        if (Constants.EVENT_TASK.DEMO_RSVP.getTask().equalsIgnoreCase(sTelNumType))
                        {
                            if(callResponse!=null && callResponse.getEventGuestBean()!=null
                                    && callResponse.isEventBeanExists() && callResponse.isEventGuestBeanExists())
                            {
                                callResponse = demoTwiml.getCallForwardingResponse(callResponse,incomingCallBean);
                            }
                            else
                            {
                                callResponse = demoTwiml.getRsvpResponse(callResponse, twilioIncomingCallBean);
                            }
						}
                        else if (Constants.EVENT_TASK.DEMO_SEATING.getTask().equalsIgnoreCase(sTelNumType))
                        {

							callResponse = processSeatingResponse(callResponse,	twilioIncomingCallBean);
							callResponse = demoTwiml.getSeatingResponse(callResponse, twilioIncomingCallBean);
						} else
                        {

						}
					} else {
						callResponse = demoTwiml.getSecretKeyFromUser(
								callResponse, twilioIncomingCallBean);
					}
					break;
				case DEMO_GATHER_RSVP_NUM:
					callResponse = processRsvpDigits(callResponse,twilioIncomingCallBean);
					break;
				}
			} catch (TwiMLException e) {
				appLogging.error("Twiml exception encountered : "
						+ ExceptionHandler.getStackTrace(e));
				callResponse = TwimlSupport.getStandardError(callResponse,
						twilioIncomingCallBean);
			} catch (Exception e) {
				appLogging.error("Generic exception encountered : "
						+ ExceptionHandler.getStackTrace(e));
				callResponse = TwimlSupport.getStandardError(callResponse,
						twilioIncomingCallBean);
			}
		}
		return callResponse;
	}

	private ArrayList<TelNumberBean> processSecretKey(
			IncomingCallBean incomingCallBean) {
		ArrayList<TelNumberBean> arrTelNumBean = new ArrayList<TelNumberBean>();
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (twilioIncomingCallBean != null
				&& twilioIncomingCallBean.getDigits() != null
				&& !"".equalsIgnoreCase(twilioIncomingCallBean.getDigits())) {
			TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setSecretEventSecretKey(ParseUtil
					.checkNull(twilioIncomingCallBean.getDigits()));
			telNumMetaData.setSecretEventIdentifier(ParseUtil
					.checkNull(twilioIncomingCallBean.getCallerInputEventId()));

			TelNumberManager telNumManager = new TelNumberManager();
			arrTelNumBean = telNumManager
					.getTelNumbersFromSecretEventNumAndKey(telNumMetaData);
		}
		return arrTelNumBean;
	}

	private boolean processEventNum(IncomingCallBean incomingCallBean) {
		boolean isValidEventNum = false;
		TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
		if (twilioIncomingCallBean != null
				&& twilioIncomingCallBean.getDigits() != null
				&& !"".equalsIgnoreCase(twilioIncomingCallBean.getDigits())) {

			TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setSecretEventIdentifier(ParseUtil.checkNull(twilioIncomingCallBean.getDigits()));

			TelNumberManager telNumManager = new TelNumberManager();
			ArrayList<TelNumberBean> arrTelNumBean = telNumManager.getTelNumbersFromSecretEventNum(telNumMetaData);
			if (arrTelNumBean != null && !arrTelNumBean.isEmpty()) {
				isValidEventNum = true;
			}

		}

		return isValidEventNum;
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
			EventGuestBean tmpEventGuestBean = telNumManager
					.getTelNumGuestDetails(telNumMetaData);

			int iTotalSeats = ParseUtil.sToI(tmpEventGuestBean
					.getTotalNumberOfSeats());
			int iRsvpSeatsSel = ParseUtil.sToI(rsvpNum);

			DemoCallTwiml demoTwiml = new DemoCallTwiml();

			if (iRsvpSeatsSel <= iTotalSeats) {
				ArrayList<String> arrGuestID = new ArrayList<String>();
				arrGuestID.add(tmpEventGuestBean.getGuestId());

				EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
				eventGuestMetaData.setEventId(super.eventId);
				eventGuestMetaData.setRsvpDigits(twilioIncomingCallBean
						.getDigits());
				eventGuestMetaData.setArrGuestId(arrGuestID);

				EventGuestManager eventGuestManager = new EventGuestManager();
				eventGuestManager.setGuestRsvpForEvent(eventGuestMetaData);

				EventGuestBean eventGuestBean = eventGuestManager
						.getGuest(eventGuestMetaData);

				callResponse.setEventGuestBean(eventGuestBean);

				if (eventGuestBean.getRsvpSeats().equalsIgnoreCase(
						twilioIncomingCallBean.getDigits())) {
					callResponse = demoTwiml.getRsvpDigitsSuccess(callResponse,
							"Your RSVP of "
									+ eventGuestBean.getRsvpSeats() + " has been accepted.",
							twilioIncomingCallBean);
				} else {
					callResponse = demoTwiml
							.getRsvpDigitsFail(
									callResponse,
									"We were unable to process your RSVP. Please call again later.",
									Constants.RSVP_STATUS.RSVP_UPDATE_FAIL,
									twilioIncomingCallBean);

				}

			} else {
				// RSVP selected has exceed the total invited seats.
				callResponse = demoTwiml
						.getRsvpDigitsFail(
								callResponse,
								"Please select a RSVP number less than or equal to " + iTotalSeats ,
								Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS,
								twilioIncomingCallBean);
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
                    callResponse.setEventGuestBean(eventGuestBean);
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
