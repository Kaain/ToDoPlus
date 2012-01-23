package de.fhb.mobile.ToDoListAndroidApp.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Class DateHelper.
 */
public class DateHelper {
	
	/**
	 * Gets the date as string.
	 *
	 * @param calendar
	 * @return the date as string in Format: dd.MM.yyyy, HH:mm
	 */
	public static String getDateTimeAsString(Calendar cal){
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy | HH:mm");
		return format.format(cal.getTime());
	}
	
	public static String getDateAsString(Calendar cal){
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		return format.format(cal.getTime());
	}
	
	public static String getTimeAsString(Calendar cal){
		DateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(cal.getTime());
	}
	
	/**
	 * Gets the calendar by string.
	 *
	 * @param dateString the date string
	 * @return the calendar by string
	 */
	public static Calendar getCalendarByString(String dateString){
		Date date = new Date(dateString);
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	

	/**
	 * Parses the string to dateformat.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String parseStringToDateformat(String string){
		Date date = new Date(string);
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy | HH:mm");
		return format.format(date);
	}
}
