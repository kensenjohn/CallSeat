package com.gs.common;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateSupport {
	private static final DateTimeFormatter PRETTY_DATE_2 = DateTimeFormat.forPattern(Constants.PRETTY_DATE_PATTERN_2);
    private static final DateTimeFormatter DATE_PATTERN_TZ = DateTimeFormat.forPattern(Constants.DATE_PATTERN_TZ);  // yyyy-MM-dd HH:mm:ss z

	private static final Logger appLogging = LoggerFactory.getLogger("AppLogging");

	public static Long getEpochMillis() {
		DateTime dt = new DateTime();
		return dt.getMillis();
	}

	public static String getUTCDateTime() {
		DateTimeZone zoneUTC = DateTimeZone.UTC;
		DateTime localDateTime = new DateTime();

		DateTime utcTime = localDateTime.withZone(zoneUTC);

		return DATE_PATTERN_TZ.print(utcTime);
	}

	public static Long getMillis(String sDate, String sPattern, String sTimeZone) {
		DateTimeZone timeZone = DateTimeZone.forID(sTimeZone);

		DateTimeFormatter formatter = DateTimeFormat.forPattern(sPattern);
		DateTime dateTime = formatter.parseDateTime(sDate);
		dateTime = dateTime.withZone(timeZone);
		return dateTime.getMillis();

	}

    public static String getTimeByZone(Long epochDate, String sTimeZone, String sPattern) {

        if(sPattern==null || "".equalsIgnoreCase(sPattern))
        {
            sPattern = Constants.DATE_PATTERN_TZ;
        }
        final DateTimeFormatter dateTimePattern = DateTimeFormat.forPattern(sPattern);

        DateTimeZone timeZone = DateTimeZone.forID(sTimeZone);

        DateTime localDateTime = new DateTime(epochDate,timeZone);
        DateTime currentDateTime = new DateTime();
        return dateTimePattern.print(localDateTime);
    }

	public static String getTimeByZone(Long epochDate, String sTimeZone) {

        return getTimeByZone(epochDate, sTimeZone, Constants.DATE_PATTERN_TZ);
	}
	
	public static Long subtractHours(Long epochDate, Integer iNumOfHours)
	{
		DateTime srcTime = new DateTime(epochDate);
		DateTime afterMinus = srcTime.minusHours(iNumOfHours);
		return afterMinus.getMillis();
	}

    public static Long subtractTime(Long epochDate, Integer iNumOfTimeUnits, Constants.TIME_UNIT timeUnit )
    {
        // DateTime srcTime = new DateTime(epochDate);
        DateTime afterAddition = new DateTime(epochDate);
        if(Constants.TIME_UNIT.SECONDS.equals(timeUnit))
        {
            afterAddition = afterAddition.minusSeconds( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.MINUTES.equals(timeUnit) )
        {
            afterAddition = afterAddition.minusMinutes( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.HOURS.equals(timeUnit) )
        {
            afterAddition = afterAddition.minusHours( iNumOfTimeUnits );
        }
        return afterAddition.getMillis();
    }

    public static Long addTime(Long epochDate, Integer iNumOfTimeUnits, Constants.TIME_UNIT timeUnit )
    {
        // DateTime srcTime = new DateTime(epochDate);
        DateTime afterAddition = new DateTime(epochDate);
        if(Constants.TIME_UNIT.SECONDS.equals(timeUnit))
        {
            afterAddition = afterAddition.plusSeconds( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.MINUTES.equals(timeUnit) )
        {
            afterAddition = afterAddition.plusMinutes( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.HOURS.equals(timeUnit) )
        {
            afterAddition = afterAddition.plusHours( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.DAYS.equals(timeUnit) )
        {
            afterAddition = afterAddition.plusDays( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.MONTHS.equals(timeUnit) )
        {
            afterAddition = afterAddition.plusMonths( iNumOfTimeUnits );
        }
        else if( Constants.TIME_UNIT.YEARS.equals(timeUnit) )
        {
            afterAddition = afterAddition.plusYears( iNumOfTimeUnits );
        }
        return afterAddition.getMillis();
    }

    public static Integer getYear(Long epochDate)
    {
        DateTime srcTime = new DateTime(epochDate);
        Integer iYear = srcTime.getYear();
        return iYear;
    }

    public static Long convertToMillis (  Constants.TIME_UNIT timeUnit , Long sourceTime ) {
        Long milliseconds = 0L;
        switch(timeUnit ) {
            case SECONDS:
                milliseconds = sourceTime * 1000;
                break;
            case MINUTES:milliseconds = sourceTime * 60 * 1000;
                break;
        }
       return milliseconds ;
    }
}
