package com.gs.task;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.call.CallResponse;

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
		return null;

	}

}
