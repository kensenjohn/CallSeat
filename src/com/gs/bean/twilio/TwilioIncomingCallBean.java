package com.gs.bean.twilio;

public class TwilioIncomingCallBean extends IncomingCallBean
{

	private String rsvpDigits = "";
	private String apiVersion = "";
	private String direction = "";
	private String forwardFrom = "";

	private String fromCity = "";
	private String fromState = "";
	private String fromZip = "";
	private String fromCountry = "";

	private String toZip = "";
	private String toCity = "";
	private String toCountry = "";
	private String toState = "";

	private String callDuration = "";

	private String calledState = "";
	private String calledCountry = "";
	private String calledCity = "";
	private String calledZip = "";

	private String duration = "";

	private String caller = "";
	private String callerCity = "";
	private String callerState = "";
	private String callerCountry = "";
	private String callerZip = "";

	public String getCallid()
	{
		return this.callid;
	}

	public void setCallid(String callSid)
	{
		this.callid = callSid;
	}

	public String getAccountid()
	{
		return accountid;
	}

	public void setAccountid(String accountid)
	{
		this.accountid = accountid;
	}

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

	public String getApiVersion()
	{
		return apiVersion;
	}

	public void setApiVersion(String apiVersion)
	{
		this.apiVersion = apiVersion;
	}

	public String getDirection()
	{
		return direction;
	}

	public void setDirection(String direction)
	{
		this.direction = direction;
	}

	public String getForwardFrom()
	{
		return forwardFrom;
	}

	public void setForwardFrom(String forwardFrom)
	{
		this.forwardFrom = forwardFrom;
	}

	public String getFromCity()
	{
		return fromCity;
	}

	public void setFromCity(String fromCity)
	{
		this.fromCity = fromCity;
	}

	public String getFromState()
	{
		return fromState;
	}

	public void setFromState(String fromState)
	{
		this.fromState = fromState;
	}

	public String getFromZip()
	{
		return fromZip;
	}

	public void setFromZip(String fromZip)
	{
		this.fromZip = fromZip;
	}

	public String getFromCountry()
	{
		return fromCountry;
	}

	public void setFromCountry(String fromCountry)
	{
		this.fromCountry = fromCountry;
	}

	public String getToZip()
	{
		return toZip;
	}

	public void setToZip(String toZip)
	{
		this.toZip = toZip;
	}

	public String getToCity()
	{
		return toCity;
	}

	public void setToCity(String toCity)
	{
		this.toCity = toCity;
	}

	public String getToCountry()
	{
		return toCountry;
	}

	public void setToCountry(String toCountry)
	{
		this.toCountry = toCountry;
	}

	public String getToState()
	{
		return toState;
	}

	public void setToState(String toState)
	{
		this.toState = toState;
	}

	public String getCallDuration()
	{
		return callDuration;
	}

	public void setCallDuration(String callDuration)
	{
		this.callDuration = callDuration;
	}

	public String getCalledState()
	{
		return calledState;
	}

	public void setCalledState(String calledState)
	{
		this.calledState = calledState;
	}

	public String getCalledCountry()
	{
		return calledCountry;
	}

	public void setCalledCountry(String calledCountry)
	{
		this.calledCountry = calledCountry;
	}

	public String getCalledCity()
	{
		return calledCity;
	}

	public void setCalledCity(String calledCity)
	{
		this.calledCity = calledCity;
	}

	public String getCalledZip()
	{
		return calledZip;
	}

	public void setCalledZip(String calledZip)
	{
		this.calledZip = calledZip;
	}

	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration = duration;
	}

	public String getCaller()
	{
		return caller;
	}

	public void setCaller(String caller)
	{
		this.caller = caller;
	}

	public String getCallerCity()
	{
		return callerCity;
	}

	public void setCallerCity(String callerCity)
	{
		this.callerCity = callerCity;
	}

	public String getCallerState()
	{
		return callerState;
	}

	public void setCallerState(String callerState)
	{
		this.callerState = callerState;
	}

	public String getCallerCountry()
	{
		return callerCountry;
	}

	public void setCallerCountry(String callerCountry)
	{
		this.callerCountry = callerCountry;
	}

	public String getCallerZip()
	{
		return callerZip;
	}

	public void setCallerZip(String callerZip)
	{
		this.callerZip = callerZip;
	}

	public String getRsvpDigits()
	{
		return rsvpDigits;
	}

	public void setRsvpDigits(String rsvpDigits)
	{
		this.rsvpDigits = rsvpDigits;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TwilioIncomingCallBean [apiVersion=").append(apiVersion)
				.append(", direction=").append(direction).append(", forwardFrom=")
				.append(forwardFrom).append(", fromCity=").append(fromCity).append(", fromState=")
				.append(fromState).append(", fromZip=").append(fromZip).append(", fromCountry=")
				.append(fromCountry).append(", toZip=").append(toZip).append(", toCity=")
				.append(toCity).append(", toCountry=").append(toCountry).append(", toState=")
				.append(toState).append(", callDuration=").append(callDuration)
				.append(", calledState=").append(calledState).append(", calledCountry=")
				.append(calledCountry).append(", calledCity=").append(calledCity)
				.append(", calledZip=").append(calledZip).append(", duration=").append(duration)
				.append(", caller=").append(caller).append(", callerCity=").append(callerCity)
				.append(", callerState=").append(callerState).append(", callerCountry=")
				.append(callerCountry).append(", callerZip=").append(callerZip).append(", from=")
				.append(from).append(", to=").append(to).append(", callStatus=").append(callStatus)
				.append(", callid=").append(callid).append(", accountid=").append(accountid)
				.append("]");
		return builder.toString();
	}

}
