//*******************************************************************//
// MessageUtil.class    										    
//															      
// A utility class												 
//******************************************************************//
/**
 * @version 1.0
 * 
 * 1/4/2019
 * 
 * @author Shurel Reynolds (shurel_reynolds@yahoo.com)
 */

package com.shurel.sale;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class MessageUtil {
	/**
	 * timeStamp of this object.
	 */
	protected long timeStamp;
	/**
	 * numberFormatter used to handle numeric values.
	 */
	protected static NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.UK);
	/**
	 * formatter used to convert the currency value.
	 */
	protected static DecimalFormat formatter = (DecimalFormat) numberFormatter;
	/**
	 * formatter used to convert the timestamp into date.
	 */
	protected DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	/**
	 * formatter used to convert the timestamp into time.
	 */
	protected DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	static {
		formatter.applyPattern("###,###,##0.00");
	}

	/**
	 * This method will be used to get the MessageObject's timeStamp as time.
	 * 
	 * @return a String representation of the MessageObject's timeStamp.
	 */
	public String getTime() {
		if (timeStamp == 0)
			return "N/A";

		return LocalDateTime.ofInstant(Instant.ofEpochSecond(this.timeStamp), TimeZone.getDefault().toZoneId())
				.format(timeFormatter);
	}

	/**
	 * This method will be used to get the MessageObject's timeStamp as date.
	 * 
	 * @return a String representation of the MessageObject's timeStamp.
	 */

	public String getDate() {
		if (timeStamp == 0)
			return "N/A";

		return LocalDateTime.ofInstant(Instant.ofEpochSecond(this.timeStamp), TimeZone.getDefault().toZoneId())
				.format(dateFormatter);
	}

	/**
	 * Capitalizes the first character of String s
	 * 
	 * @param s the String to be capitalized.
	 * @return a String with the first char capitalized.
	 */

	public String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
    * @param value.
	 * @return a String representation of the value as a price.
	 */
	protected String longToPrice(long value) {

		return value < 100 ? value + "p" : "£" + formatter.format(value / 100d);
	}

	protected static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * Adds the specified percentage to the number
	 * 
	 * @param number 
	 * 				the original number
	 * @param percent
	 * 				the percentage to add to the number
	 * @return the result in long
	 */
	protected static long addPercentage(long number, double percent) {
		return (long) (((percent / 100) * number) + number);
	}

}