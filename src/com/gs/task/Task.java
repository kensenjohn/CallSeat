package com.gs.task;

import com.gs.bean.twilio.IncomingCallBean;
import com.gs.call.CallResponse;

public abstract class Task
{
	protected String eventId = "";
	protected String adminId = "";

	public Task(String eventId, String adminId)
	{
		this.eventId = eventId;
		this.adminId = adminId;
	}

	public abstract CallResponse processTask(IncomingCallBean incomingCallBean);
}
