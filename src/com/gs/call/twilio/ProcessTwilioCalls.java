package com.gs.call.twilio;

import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.ProcessCalls;
import com.gs.task.Task;

public class ProcessTwilioCalls extends ProcessCalls
{

	public ProcessTwilioCalls(TwilioIncomingCallBean twilioIncominCallBean)
	{
		super(twilioIncominCallBean);
	}

	@Override
	public void process()
	{
		Task task = identifyTask();
		task.processTask(this.incomingCallBean);
	}
}
