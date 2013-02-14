package com.gs.task;

import java.util.ArrayList;

import com.gs.bean.CallTransactionBean;
import com.gs.bean.InformGuestBean;
import com.gs.common.CallTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public RsvpTask(String eventId, String adminId) {
		super(eventId, adminId);
	}

	@Override
	public CallResponse processTask(IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();
		if (incomingCallBean != null) {

			RsvpTwiml rsvpTwiml = new RsvpTwiml();

			if (Constants.CALL_TYPE.FIRST_REQUEST.equals(incomingCallBean
					.getCallType())) {
				callResponse = processFirstResponseTask(incomingCallBean);
				callResponse = rsvpTwiml.getFirstResponse(callResponse,incomingCallBean);
			} else if (Constants.CALL_TYPE.RSVP_DIGIT_RESP
					.equals(incomingCallBean.getCallType())) {
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
					callResponse = rsvpTwiml.getRsvpDigitsSuccess(callResponse,
							"You have successfully updated the number of seats for RSVP to "
									+ eventGuestBean.getRsvpSeats());

                    InformGuestBean informGuestBean = new InformGuestBean();
                    informGuestBean.setEventId( super.eventId );
                    informGuestBean.setAdminId( super.eventId );
                    informGuestBean.setGuestId( eventGuestBean.getGuestId() );
                    informGuestBean.setEventTask( Constants.EVENT_TASK.RSVP );

                    InformGuestTask.sendRSVPConfirmation( informGuestBean );
				} else {
					callResponse = rsvpTwiml
							.getRsvpDigitsFail(
									callResponse,
									"Your request could not be processed as this time. Please try again later.",
									Constants.RSVP_STATUS.RSVP_UPDATE_FAIL,twilioIncomingCallBean);

				}

			} else {
				// RSVP selected has exceed the total invited seats.
				callResponse = rsvpTwiml
						.getRsvpDigitsFail(
								callResponse,
								"Your R S V P number exceeded the total number of seats you have been invited to.",
								Constants.RSVP_STATUS.RSVP_EXCEED_TOTAL_SEATS,twilioIncomingCallBean);
			}
		}
		return callResponse;
	}

	private CallResponse processFirstResponseTask(
			IncomingCallBean incomingCallBean) {
		CallResponse callResponse = new CallResponse();

		String sGuestTelNumber = incomingCallBean.getFrom();

		TelNumberMetaData telNumMetaData = new TelNumberMetaData();
		telNumMetaData.setGuestTelNumber(sGuestTelNumber);
		telNumMetaData.setAdminId(super.adminId);
		telNumMetaData.setEventId(super.eventId);

		TelNumberManager telNumManager = new TelNumberManager();
		EventGuestBean eventGuestBean = telNumManager
				.getTelNumGuestDetails(telNumMetaData);

		EventData eventData = new EventData();
		EventBean eventBean = eventData.getEvent(eventGuestBean.getEventId());

		callResponse.setEventGuestBean(eventGuestBean);
		callResponse.setEventBean(eventBean);

        CallTransactionBean callTransactionBean = new CallTransactionBean();
        callTransactionBean.setAdminId(super.adminId);
        callTransactionBean.setEventId(super.eventId);
        if(eventGuestBean!=null && !"".equalsIgnoreCase(eventGuestBean.getGuestId()))
        {
            callTransactionBean.setGuestId(eventGuestBean.getGuestId());
        }
        CallTransaction.getInstance().updateTransaction(incomingCallBean,callTransactionBean );

		return callResponse;
	}

}
