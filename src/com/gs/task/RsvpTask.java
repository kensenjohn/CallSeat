package com.gs.task;

import com.gs.bean.EventGuestBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;

public class RsvpTask extends Task
{

	public RsvpTask(String eventId, String adminId)
	{
		super(eventId, adminId);
	}

	@Override
	public void processTask(IncomingCallBean incomingCallBean)
	{
		if (incomingCallBean != null)
		{
			String sGuestTelNumber = incomingCallBean.getFrom();

			TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setGuestTelNumber(sGuestTelNumber);
			telNumMetaData.setAdminId(super.adminId);
			telNumMetaData.setEventId(super.eventId);

			TelNumberManager telNumManager = new TelNumberManager();
			EventGuestBean eventGuestBean = telNumManager.getTelNumGuestDetails(telNumMetaData);
		}

	}
}
