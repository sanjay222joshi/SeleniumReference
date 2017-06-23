package com.sanjay.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.testng.log4testng.Logger;

public class CommonUtils {
	private static final Logger log = Logger.getLogger(CommonUtils.class);

	public static Boolean isDBQueryResultsAreSame(String query1, String query2) {
		DataLoader dataLoader = DataLoader.getInstance();
		Map<Integer, List<String>> results1 = dataLoader
				.executeSQLQuery(query1);
		Map<Integer, List<String>> results2 = dataLoader
				.executeSQLQuery(query2);
		dataLoader.closeConnection();

		if (results1.size() != results2.size()) {
			return false;
		}
		for (Integer key : results1.keySet()) { //
			// with EntrySet
			List<String> rowValues1 = results1.get(key);
			List<String> rowValues2 = results2.get(key);
			rowValues1.removeAll(rowValues2);
			if (rowValues1.size() > 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Works with any two maps with common key / value types. The key type must
	 * implement Comparable though (for sorting). Returns a map containing all
	 * keys that appear in either of the supplied maps. The values will be true
	 * if and only if either - map1.get(key)==map2.get(key) (values may be null)
	 * or - map1.get(key).equals(map2.get(key)).
	 */
	public static <K extends Comparable<? super K>, V> Map<K, Boolean> compareEntries(
			final Map<K, V> map1, final Map<K, V> map2) {
		final Collection<K> allKeys = new HashSet<K>();
		allKeys.addAll(map1.keySet());
		allKeys.addAll(map2.keySet());
		final Map<K, Boolean> result = new TreeMap<K, Boolean>();
		for (final K key : allKeys) {
			result.put(key, map1.containsKey(key) == map2.containsKey(key)
					&& Boolean.valueOf(equal(map1.get(key), map2.get(key))));
		}
		return result;
	}

	private static boolean equal(final Object obj1, final Object obj2) {
		return obj1 == obj2 || obj1 != null && obj1.equals(obj2);
	}

	public static int getRowNo(final Map<Integer, List<String>> tableData,
			int colNo, String searchItem) {
		int returnKey = -1;
		for (Integer key : tableData.keySet()) {
			List<String> rowData = tableData.get(key);
			for (int i = 0; i < rowData.size(); i++) {
				if (rowData.get(i).equals(searchItem)) {
					returnKey = key;
				}
			}
		}
		return returnKey;
	}

	/**
	 * Get timeStamp from MilliSeconds.
	 * 
	 * @param milliSeconds
	 * @return
	 */
	public static String getTimeFromMilliSeconds(long milliSeconds) {
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException(
					"Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		// sb.append(days);
		// sb.append(" Days ");
		sb.append(hours);
		sb.append(" Hours ");
		sb.append(minutes);
		sb.append(" Minutes ");
		sb.append(seconds);
		sb.append(" Seconds");

		return sb.toString();
	}

	public static String arrayToString(String[] a, String separator) {
		StringBuffer result = new StringBuffer();
		if (a.length > 0) {
			result.append(a[0]);
			for (int i = 1; i < a.length; i++) {
				result.append(separator);
				result.append(a[i]);
			}
		}
		return result.toString();
	}
	
}
