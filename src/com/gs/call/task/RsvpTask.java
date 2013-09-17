package com.gs.call.task;

import java.util.ArrayList;

import com.gs.bean.CallTransactionBean;
import com.gs.call.twilio.twiml.TwimlSupport;
import com.gs.common.CallTransaction;

import com.gs.bean.EventBean;
import com.gs.bean.EventGuestBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.call.twilio.twiml.RsvpTwiml;
import com.gs.common.Constants;
import com.gs.common.ParseUtil;
import com.gs.data.event.EventData;
import com.gs.manager.event.EventGuestManager;
import com.gs.manager.event.EventGuestMetaData;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;

public class RsvpTask extends Task {

	public RsvpTask(String eventId, String adminId) {
		super(eventId, adminId);
	}

	@Override
	public CallResponse processTask(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();
		if (incomingCallBean != null) {

			RsvpTwiml rsvpTwiml = new RsvpTwiml();
            telephonyLogging.info("RSVP processTask invoked calltype : " + incomingCallBean.getCallType() + " From :" + incomingCallBean.getFrom() + " To : " + incomingCallBean.getTo());
			if (Constants.CALL_TYPE.FIRST_REQUEST.equals(incomingCallBean.getCallType())) {
				callResponse = processFirstResponseTask(incomingCallBean);

                if( canCallUsageFeatureContinue( callResponse ) ) {
                    if(callResponse!=null && callResponse.getEventGuestBean()!=null  && callResponse.isEventBeanExists() && callResponse.isEventGuestBeanExists()) {
                        telephonyLogging.info("RSVP First Response  From :" + incomingCallBean.getFrom() + " To : " + incomingCallBean.getTo() );
                        callResponse = rsvpTwiml.getFirstResponse(callResponse,incomingCallBean);
                    }  else  {
                        telephonyLogging.info("RSVP Call forwarding  From :" + incomingCallBean.getFrom() + " To : " + incomingCallBean.getTo() );
                        callResponse = rsvpTwiml.getCallForwardingResponse(callResponse,incomingCallBean);
                    }
                }  else  {
                    telephonyLogging.info("RSVP Reject Call Invoked  From :" +  incomingCallBean.getFrom() + " To : " + incomingCallBean.getTo() );
                    callResponse = TwimlSupport.rejectCall( callResponse );
                }
			} else if (Constants.CALL_TYPE.RSVP_DIGIT_RESP.equals(incomingCallBean.getCallType())) {
				callResponse = processRsvpDigits(incomingCallBean);
			}
		}
		return callResponse;

	}

	private CallResponse processRsvpDigits(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();
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



            CallTransactionBean callTransactionBean = new CallTransactionBean();
            callTransactionBean.setAdminId(super.adminId);
            callTransactionBean.setEventId(super.eventId);
            callTransactionBean.setGuestId(tmpEventGuestBean.getGuestId());
            CallTransaction.getInstance().updateTransaction(incomingCallBean,callTransactionBean );

			RsvpTwiml rsvpTwiml = new RsvpTwiml();


            ArrayList<String> arrGuestID = new ArrayList<String>();
            arrGuestID.add(tmpEventGuestBean.getGuestId());

            EventGuestMetaData eventGuestMetaData = new EventGuestMetaData();
            eventGuestMetaData.setEventId(super.eventId);
            eventGuestMetaData.setRsvpDigits(twilioIncomingCallBean
                    .getDigits());
            eventGuestMetaData.setArrGuestId(arrGuestID);

            EventGuestManager eventGuestManager = new EventGuestManager();
            EventGuestBean eventGuestBean = eventGuestManager
                    .getGuest(eventGuestMetaData);

            callResponse.setEventGuestBean(eventGuestBean);

			if (iRsvpSeatsSel <= iTotalSeats) {
				eventGuestManager.setGuestRsvpForEvent(eventGuestMetaData);


				if (eventGuestBean.getRsvpSeats().equalsIgnoreCase(
						twilioIncomingCallBean.getDigits())) {
					callResponse = rsvpTwiml.getRsvpDigitsSuccess(callResponse,
							"Your RSVP of "
									+ eventGuestBean.getRsvpSeats() +" has been accepted.");
				} else {
					callResponse = rsvpTwiml
							.getRsvpDigitsFail(
									callResponse,
									" We were unable to process your RSVP. Please call again later.",
									Constants.RSVP_STATUS.RSVP_UPDATE_FAIL,twilioIncomingCallBean);

				}

			} else {
				// RSVP selected has exceed the total invited seats.
				callResponse = rsvpTwiml
						.getRsvpDigitsFail(
								callResponse,
								"Please select a RSVP number less than or equal to " + iTotalSeats,
								Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS,twilioIncomingCallBean);
			}
		}
		return callResponse;
	}

	private CallResponse processFirstResponseTask(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();

		String sGuestTelNumber = incomingCallBean.getFrom();

        EventData eventData = new EventData();
        EventBean eventBean = eventData.getEvent( super.eventId );
        callResponse.setEventBean(eventBean);


		TelNumberMetaData telNumMetaData = new TelNumberMetaData();
		telNumMetaData.setGuestTelNumber(sGuestTelNumber);
		telNumMetaData.setAdminId(super.adminId);
		telNumMetaData.setEventId(super.eventId);

		TelNumberManager telNumManager = new TelNumberManager();
		EventGuestBean eventGuestBean = telNumManager.getTelNumGuestDetails(telNumMetaData);

        if(eventGuestBean!=null && eventGuestBean.getGuestId()!=null && !"".equalsIgnoreCase(eventGuestBean.getGuestId() )) {
            callResponse.setEventGuestBean(eventGuestBean);
        }
        CallTransactionBean callTransactionBean = new CallTransactionBean();
        callTransactionBean.setAdminId(super.adminId);
        callTransactionBean.setEventId(super.eventId);
        if(eventGuestBean!=null && !"".equalsIgnoreCase(eventGuestBean.getGuestId())) {
            callTransactionBean.setGuestId(eventGuestBean.getGuestId());
        }
        CallTransaction.getInstance().updateTransaction(incomingCallBean,callTransactionBean );

        appLogging.info("processFirstResponseTask Call Response : " + callResponse);
		return callResponse;
	}

}
