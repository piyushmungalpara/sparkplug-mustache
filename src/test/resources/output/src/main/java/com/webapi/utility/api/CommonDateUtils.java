package com.webapi.utility.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonDateUtils {
	public static String getDate(int noOfDays, String dateFormat) {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, noOfDays);
		DateFormat formatDate = new SimpleDateFormat(dateFormat);
		return formatDate.format(date.getTime());
	}
}
