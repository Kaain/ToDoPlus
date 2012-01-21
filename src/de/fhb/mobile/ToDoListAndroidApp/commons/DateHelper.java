package de.fhb.mobile.ToDoListAndroidApp.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.text.method.DateTimeKeyListener;

public class DateHelper {
	public static String getDateAsString(Calendar cal){
		DateFormat format = new SimpleDateFormat("d.MM.yyyy, HH:mm");
		return format.format(cal.getTime());
	}
	public static Calendar getCalendarByString(String dateString){
		Date date = new Date(dateString);
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	public static String parseToDateformat(String string){
		Date date = new Date(string);
		DateFormat format = new SimpleDateFormat("d.MM.yyyy, HH:mm");
		return format.format(date);
	}
}
