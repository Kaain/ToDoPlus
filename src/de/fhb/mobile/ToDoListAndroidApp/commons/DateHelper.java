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
	 * Gets the date as string. "MM-dd-yyyy | HH:mm"
	 * 
	 * @param cal
	 *            the cal
	 * @return the date as string in Format: dd.MM.yyyy, HH:mm
	 */
	public static String getDateTimeAsString(Calendar cal) {
		if (cal == null)
			return "";
		else {
			DateFormat format = new SimpleDateFormat("MM-dd-yyyy | HH:mm");
			return format.format(cal.getTime());
		}
	}

	/**
	 * Gets the date as string. "MM-dd-yyyy"
	 * 
	 * @param cal
	 *            the cal
	 * @return the date as string
	 */
	public static String getDateAsString(Calendar cal) {
		if (cal == null)
			return "";
		else {
			DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			return format.format(cal.getTime());
		}
	}

	/**
	 * Gets the time as string. "HH:mm"
	 * 
	 * @param cal
	 *            the cal
	 * @return the time as string
	 */
	public static String getTimeAsString(Calendar cal) {
		if (cal == null)
			return "";
		else {
			DateFormat format = new SimpleDateFormat("HH:mm");
			return format.format(cal.getTime());
		}
	}

	/**
	 * Gets the calendar by string.
	 * 
	 * @param dateString
	 *            the date string
	 * @return the calendar by string
	 */
	public static Calendar getCalendarByString(String dateString) {
		Date date = new Date(dateString);
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * Gets the calendar by long.
	 * 
	 * @param millis
	 *            the millis
	 * @return the calendar by long
	 */
	public static Calendar getCalendarByLong(long millis) {
		Date date = new Date(millis);
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * Parses the string to dateformat. "MM-dd-yyyy | HH:mm"
	 * 
	 * @param string
	 *            the string
	 * @return the string
	 */
	public static String parseStringToDateformat(String string) {
		Date date = new Date(string);
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy | HH:mm");
		return format.format(date);
	}
	
	public static long getActualTime(){
		return GregorianCalendar.getInstance().getTime().getTime();
	}
}
