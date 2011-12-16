package com.gs.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateSupport {
	private static final DateTimeFormatter PRETTY_DATE_2 = DateTimeFormat
			.forPattern(Constants.PRETTY_DATE_PATTERN_2);

	private static final Logger appLogging = LoggerFactory
			.getLogger("AppLogging");

	public static Long getEpochMillis() {
		DateTime dt = new DateTime();
		return dt.getMillis();
	}

	public static String getUTCDateTime() {
		DateTimeZone zoneUTC = DateTimeZone.UTC;
		final DateTimeFormatter formatter1 = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss z");
		DateTime localDateTime = new DateTime();

		DateTime utcTime = localDateTime.withZone(zoneUTC);

		return formatter1.print(utcTime);
	}

	public static Long getMillis(String sDate, String sPattern, String sTimeZone) {
		DateTimeZone timeZone = DateTimeZone.forID(sTimeZone);

		DateTimeFormatter formatter = DateTimeFormat.forPattern(sPattern);
		DateTime dateTime = formatter.parseDateTime(sDate);
		dateTime = dateTime.withZone(timeZone);
		return dateTime.getMillis();

	}

	public static String getTimeByZone(Long epochDate, String sTimeZone) {
		DateTimeZone timeZone = DateTimeZone.forID(sTimeZone);

		DateTime localDateTime = new DateTime(epochDate);
		DateTime localTime = localDateTime.withZone(timeZone);

		return PRETTY_DATE_2.print(localTime);

	}
}
