package com.gs.call;

import com.gs.bean.TelNumberBean;
import com.gs.bean.twilio.IncomingCallBean;
import com.gs.common.Constants;
import com.gs.manager.event.TelNumberManager;
import com.gs.manager.event.TelNumberMetaData;
import com.gs.manager.event.TelNumberResponse;
import com.gs.task.RsvpTask;
import com.gs.task.SeatingTask;
import com.gs.task.Task;

public abstract class ProcessCalls
{
	protected IncomingCallBean incomingCallBean = null;

	public ProcessCalls(IncomingCallBean incomingCallBean)
	{
		this.incomingCallBean = incomingCallBean;
	}

	protected Task identifyTask()
	{
		Task task = null;
		if (this.incomingCallBean != null)
		{
			String sGuestTelNumber = this.incomingCallBean.getFrom();
			String sEventTelNumber = this.incomingCallBean.getTo();

			TelNumberMetaData telNumMetaData = new TelNumberMetaData();
			telNumMetaData.setGuestTelNumber(sGuestTelNumber);
			telNumMetaData.setGuestTelNumber(sEventTelNumber);

			TelNumberManager telNumManager = new TelNumberManager();
			TelNumberResponse telNumberResponse = telNumManager.getTelNumberDetails(telNumMetaData);

			if (telNumberResponse != null && telNumberResponse.getTelNumberBean() != null)
			{
				TelNumberBean telNumberBean = telNumberResponse.getTelNumberBean();

				if (telNumberBean != null && telNumberBean.isTelNumBeanSet())
				{
					String sEventId = telNumberBean.getEventId();
					String sAdminId = telNumberBean.getAdminId();
					// String sGuestTelNumber = telNumberBean.
					if (Constants.EVENT_TASK.RSVP.getTask().equalsIgnoreCase(
							telNumberBean.getTelNumberType()))
					{
						task = new RsvpTask(sEventId, sAdminId);
					} else if (Constants.EVENT_TASK.SEATING.getTask().equalsIgnoreCase(
							telNumberBean.getTelNumberType()))
					{
						task = new SeatingTask(sEventId, sAdminId);
					}
				}

			}

		}
		return task;
	}

	public abstract void process();
}
