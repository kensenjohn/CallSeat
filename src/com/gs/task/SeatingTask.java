package com.gs.task;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.common.Constants;

public class SeatingTask extends Task
{

	public SeatingTask(String eventId, String adminId)
	{
		super(eventId, adminId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CallResponse processTask(IncomingCallBean incomingCallBean)
	{
		CallResponse callResponse = new CallResponse();
		if (incomingCallBean != null)
		{
			if (Constants.CALL_TYPE.SEATING_FIRST_REQUEST.equals(incomingCallBean.getCallType()))
			{

			}
		}
		return callResponse;

	}

}
