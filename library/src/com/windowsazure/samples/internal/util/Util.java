package com.windowsazure.samples.internal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public final class Util {
	
	public static String dateToGmtString(Date date) {
        gmtFormatSdf.setTimeZone(TimeZone.getTimeZone(GMT_TZ));
        return gmtFormatSdf.format(date);
	}
	
	public static String dateToXmlStringWithoutTZ(Date date) {
		xmlFormatSdf.setTimeZone(TimeZone.getTimeZone(GMT_TZ));
        return xmlFormatSdf.format(date);
	}
	
	public static String dateToXmlStringWithTZ(Date date) {
		xmlTZFormatSdf.setTimeZone(TimeZone.getTimeZone(GMT_TZ));
        return xmlTZFormatSdf.format(date);
	}
	
	public static Date gmtFormatToDate(String text)
		throws ParseException {
		
		try
		{
			return gmtFormatSdf.parse(text);
		}
		catch (ParseException e)
		{
			text += ":00:00 GMT";
			return gmtFormatSdf.parse(text);
		}
	}
	
	public static String padToLength(String input, int paddedLength, char padCharacter) {
		assert(input != null);
		assert(paddedLength >= input.length());
		
		int padLength = paddedLength - input.length();
		if (padLength == 0)
			return input;
		
		char[] padding = new char[padLength];
		for (int i = 0; i < padLength; ++i) {
			padding[i] = padCharacter;
		}
		
		return input + new String(padding);
	}
	
	public static boolean isStringNullOrEmpty(String s) {
		return (s == null || s.length() == 0);
	}
	
	public static Date xmlStringToDate(String text) {
		int year = Integer.parseInt(text.substring(0, 4));
		int month = Integer.parseInt(text.substring(5, 7));
		int day = Integer.parseInt(text.substring(8, 10));
		int hour = Integer.parseInt(text.substring(11, 13));
		int minute = Integer.parseInt(text.substring(14, 16));
		int second = Integer.parseInt(text.substring(17, 19));
		return new Date(year - 1900, month - 1, day, hour, minute, second);
	}
	
	private Util() {};
	
	private static final String DAY_DATE_MONTH_YEAR_TIME_GMT_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
	private static final String GMT_TZ = "GMT";
	private static final String XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String XML_FORMAT_TZ = "yyyy-MM-dd'T'HH:mm:ssZ";
	
	private static final SimpleDateFormat gmtFormatSdf = new SimpleDateFormat(DAY_DATE_MONTH_YEAR_TIME_GMT_FORMAT);
	private static final SimpleDateFormat xmlFormatSdf = new SimpleDateFormat(XML_FORMAT);
	private static final SimpleDateFormat xmlTZFormatSdf = new SimpleDateFormat(XML_FORMAT_TZ);
}
