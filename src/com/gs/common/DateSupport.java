package com.gs.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateSupport
{

	public static Long getEpochMillis()
	{
		DateTime dt = new DateTime();
		return dt.getMillis();
	}

	public static String getUTCDateTime()
	{
		DateTimeZone zoneUTC = DateTimeZone.UTC;
		final DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z");
		DateTime localDateTime = new DateTime();

		DateTime utcTime = localDateTime.withZone(zoneUTC);

		return formatter1.print(utcTime);
	}
}
