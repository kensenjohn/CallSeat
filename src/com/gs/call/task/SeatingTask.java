package com.gs.call.task;

import java.util.ArrayList;

import com.gs.bean.*;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.call.twilio.twiml.SeatingTwiml;
import com.gs.call.twilio.twiml.TwimlSupport;
import com.gs.common.CallTransaction;
import com.gs.common.Constants;
import com.gs.data.event.EventData;
import com.gs.manager.event.GuestTableManager;
import com.gs.manager.event.GuestTableMetaData;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeatingTask extends Task
{
    Logger appLogging = LoggerFactory.getLogger(Constants.APP_LOGS);
	public SeatingTask(String eventId, String adminId)
	{
		super(eventId, adminId);
	}

	@Override
	public CallResponse processTask(IncomingCallBean incomingCallBean)
	{
		CallResponse callResponse = new CallResponse();
		if (incomingCallBean != null)
		{
			SeatingTwiml seatingTwiml = new SeatingTwiml();
			if (Constants.CALL_TYPE.FIRST_REQUEST.equals(incomingCallBean.getCallType()))
			{
				callResponse = processFirstResponseTask(incomingCallBean);
                if( canCallUsageFeatureContinue( callResponse ) ) {
                    TwilioIncomingCallBean twilioIncomingCallBean = (TwilioIncomingCallBean) incomingCallBean;
                    callResponse = seatingTwiml.getFirstResponse(callResponse, twilioIncomingCallBean);
                } else {
                    callResponse = TwimlSupport.rejectCall( callResponse );
                }
			}
		}
		return callResponse;

	}

	private CallResponse processFirstResponseTask(IncomingCallBean incomingCallBean)
	{
		CallResponse callResponse = new CallResponse();

		String sGuestTelNumber = incomingCallBean.getFrom();

		TelNumberMetaData telNumMetaData = new TelNumberMetaData();
		telNumMetaData.setGuestTelNumber(sGuestTelNumber);
		telNumMetaData.setAdminId(super.adminId);
		telNumMetaData.setEventId(super.eventId);

		TelNumberManager telNumManager = new TelNumberManager();
		EventGuestBean eventGuestBean = telNumManager.getTelNumGuestDetails(telNumMetaData);

        //appLogging.info("SeatingTask Admin Id : " + super.adminId + " Event Id : " + super.eventId + " event guest bean : " + eventGuestBean );

		EventData eventData = new EventData();
		EventBean eventBean = eventData.getEvent(eventGuestBean.getEventId());

		GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
		guestTableMetaData.setGuestId( eventGuestBean.getGuestId() );
        guestTableMetaData.setEventId( super.eventId );

		GuestTableManager guestTableManager = new GuestTableManager();
		ArrayList<TableGuestsBean> arrTableGuestBean = guestTableManager.getGuestsEventTableAssignments(guestTableMetaData);

        //appLogging.info("SeatingTask  Guest Id : " + eventGuestBean.getGuestId() + "  New Guest Tables :  " + arrTableGuestBean );

		callResponse.setEventGuestBean(eventGuestBean);
		callResponse.setEventBean(eventBean);
		callResponse.setArrTableGuestBean(arrTableGuestBean);

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
