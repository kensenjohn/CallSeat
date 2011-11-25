package com.gs.call;

import java.util.ArrayList;

import com.gs.bean.EventBean;
import com.gs.bean.EventGuestBean;
import com.gs.bean.TableGuestsBean;
import com.twilio.sdk.verbs.TwiMLResponse;

public class CallResponse
{

	private boolean isSuccess = false;
	private EventGuestBean eventGuestBean = new EventGuestBean();
	private EventBean eventBean = new EventBean();
	private TwiMLResponse response = new TwiMLResponse();
	private ArrayList<TableGuestsBean> arrTableGuestBean = new ArrayList<TableGuestsBean>();

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

	public EventBean getEventBean()
	{
		return eventBean;
	}

	public void setEventBean(EventBean eventBean)
	{
		this.eventBean = eventBean;
	}

	public ArrayList<TableGuestsBean> getArrTableGuestBean()
	{
		return arrTableGuestBean;
	}

	public void setArrTableGuestBean(ArrayList<TableGuestsBean> arrTableGuestBean)
	{
		this.arrTableGuestBean = arrTableGuestBean;
	}

}
