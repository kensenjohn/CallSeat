package com.gs.bean;

import java.util.HashMap;

public final class ConfigBean
{
	private long modifiedTime = 0L;
	private String sFileName = "";
	private HashMap<String, String> hmConfig = new HashMap<String, String>();

	public long getModifiedTime()
	{
		return modifiedTime;
	}

	public void setModifiedTime(long modifiedTime)
	{
		this.modifiedTime = modifiedTime;
	}

	public HashMap<String, String> getHmConfig()
	{
		return hmConfig;
	}

	public void setHmConfig(HashMap<String, String> hmConfig)
	{
		this.hmConfig = hmConfig;
	}

	public String getFileName()
	{
		return sFileName;
	}

	public void setFileName(String sFileName)
	{
		this.sFileName = sFileName;
	}

}
