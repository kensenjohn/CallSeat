package com.gs.bean.twilio.twiml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response
{
	private String say = "";

	@XmlElement(name = "Say")
	public String getSay()
	{
		return say;
	}

	public void setSay(String say)
	{
		this.say = say;
	}
}
