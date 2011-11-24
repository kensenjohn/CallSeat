package com.gs.call.twilio;

import com.gs.bean.twilio.TwilioIncomingCallBean;
import com.gs.call.CallResponse;
import com.gs.call.ProcessCalls;
import com.gs.task.Task;

public class ProcessTwilioCalls extends ProcessCalls
{

	public ProcessTwilioCalls(TwilioIncomingCallBean twilioIncominCallBean)
	{
		super(twilioIncominCallBean);
	}

	@Override
	public CallResponse process()
	{
		CallResponse callResponse = new CallResponse();
		Task task = identifyTask();
		if (task != null)
		{
			callResponse = task.processTask(this.incomingCallBean);
		}

		return callResponse;
	}
}
