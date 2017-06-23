package com.sanjay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(date);
	}

	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss:SS");
		return formatter.format(date);

	}

	/*
	 * if date1>date2 return 1 , date2>date1 return 2, otherwise 0.
	 */
	public int compareDates(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return 1;
		} else if (date1.compareTo(date2) > 0) {
			return 2;
		} else {
			return 0;
		}
	}

	public static void main(String a[]) throws ParseException {
//		Date led = new SimpleDateFormat("DDMMyyyyHHMM", Locale.ENGLISH).parse("301220121300");
//		System.out.println(led.toString());
		System.out.println(CommonUtils.getTimeFromMilliSeconds(new Date().getTime()));
	}
}
