package com.gs.task;

import java.util.ArrayList;

import com.gs.bean.CallTransactionBean;
import com.gs.bean.EventBean;
import com.gs.bean.EventGuestBean;
import com.gs.bean.TableGuestsBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.call.twilio.twiml.SeatingTwiml;
import com.gs.common.CallTransaction;
import com.gs.common.Constants;
import com.gs.data.event.EventData;
import com.gs.manager.event.GuestTableManager;
import com.gs.manager.event.GuestTableMetaData;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;

public class SeatingTask extends Task
{

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
				callResponse = seatingTwiml.getFirstResponse(callResponse);
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

		EventData eventData = new EventData();
		EventBean eventBean = eventData.getEvent(eventGuestBean.getEventId());

		GuestTableMetaData guestTableMetaData = new GuestTableMetaData();
		guestTableMetaData.setGuestId(eventGuestBean.getGuestId());

		GuestTableManager guestTableManager = new GuestTableManager();
		ArrayList<TableGuestsBean> arrTableGuestBean = guestTableManager
				.getGuestsAssignments(guestTableMetaData);

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
