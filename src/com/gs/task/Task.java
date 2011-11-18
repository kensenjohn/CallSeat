package com.gs.task;

import com.gs.bean.twilio.IncomingCallBean;

public abstract class Task
{
	protected String eventId = "";
	protected String adminId = "";

	public Task(String eventId, String adminId)
	{
		this.eventId = eventId;
		this.adminId = adminId;
	}

	public abstract void processTask(IncomingCallBean incomingCallBean);
}
