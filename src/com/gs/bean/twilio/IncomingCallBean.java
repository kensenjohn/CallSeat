package com.gs.bean.twilio;

import com.gs.common.Constants;

public abstract class IncomingCallBean
{
	protected String from = "";
	protected String to = "";
	protected String callStatus = "";
	protected String callid = "";
	protected String accountid = "";

	protected Constants.CALL_TYPE callType = null;

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getCallStatus()
	{
		return callStatus;
	}

	public void setCallStatus(String callStatus)
	{
		this.callStatus = callStatus;
	}

	public String getCallid()
	{
		return callid;
	}

	public void setCallid(String callid)
	{
		this.callid = callid;
	}

	public String getAccountid()
	{
		return accountid;
	}

	public void setAccountid(String accountid)
	{
		this.accountid = accountid;
	}

	public Constants.CALL_TYPE getCallType()
	{
		return callType;
	}

	public void setCallType(Constants.CALL_TYPE callType)
	{
		this.callType = callType;
	}

}
