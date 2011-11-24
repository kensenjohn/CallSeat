package com.gs.call;

import com.gs.bean.EventGuestBean;
import com.twilio.sdk.verbs.TwiMLResponse;

public class CallResponse
{

	private boolean isSuccess = false;
	private EventGuestBean eventGuestBean = new EventGuestBean();
	private TwiMLResponse response = new TwiMLResponse();

	private boolean isTwilResponseSuccess = false;

	public boolean isSuccess()
	{
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public EventGuestBean getEventGuestBean()
	{
		return eventGuestBean;
	}

	public void setEventGuestBean(EventGuestBean eventGuestBean)
	{
		this.eventGuestBean = eventGuestBean;
	}

	public TwiMLResponse getResponse()
	{
		return response;
	}

	public void setResponse(TwiMLResponse response)
	{
		this.response = response;
	}

	public boolean isTwilResponseSuccess()
	{
		return isTwilResponseSuccess;
	}

	public void setTwilResponseSuccess(boolean isTwilResponseSuccess)
	{
		this.isTwilResponseSuccess = isTwilResponseSuccess;
	}

}
