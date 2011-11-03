package com.gs.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateSupport
{
	private static final DateTimeFormatter PRETTY_DATE_2 = DateTimeFormat
			.forPattern(Constants.PRETTY_DATE_PATTERN_2);

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

	public static String getTimeByZone(Long epochDate, String sTimeZone)
	{
		DateTimeZone timeZone = DateTimeZone.forID(sTimeZone);

		DateTime localDateTime = new DateTime(epochDate);
		DateTime localTime = localDateTime.withZone(timeZone);

		return PRETTY_DATE_2.print(localTime);

	}
}
