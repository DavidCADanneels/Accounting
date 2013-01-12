package be.dafke;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	/** "D/M/YYYY" -> Data */
	public static Calendar toCalendar(String s) {
		Pattern p = Pattern.compile("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}");
		Matcher m = p.matcher(s);
		if (m.matches()) {
			try {
				String[] delen = s.split("/", 3);
				int dag = Integer.parseInt(delen[0]);
				int maand = Integer.parseInt(delen[1]) - 1;
				int jaar = Integer.parseInt(delen[2]);
				return new GregorianCalendar(jaar, maand, dag);
			} catch (NumberFormatException nfe) {
				return null;
			}
		}
		return null;
	}

	/** Data -> "D/M/YYYY" */
	public static String toString(Calendar c) {
		StringBuffer buf = new StringBuffer();
		buf.append(c.get(Calendar.DATE) + "/");
		buf.append(c.get(Calendar.MONTH) + 1 + "/");
		buf.append(c.get(Calendar.YEAR));
		return buf.toString();
	}
}
