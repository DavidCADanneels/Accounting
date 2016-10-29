package be.dafke.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    /** "D, M, YYYY" -> Data */
    public static Calendar toCalendar(String day, String month, String year) {
        try {
            int jaar = Integer.parseInt(year);
            int maand = Integer.parseInt(month);
            int dag = Integer.parseInt(day);
            return new GregorianCalendar(jaar, maand-1, dag);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /** "D, M, YYYY" -> Data */
    public static Calendar toCalendar(int day, int month, int year) {
        return new GregorianCalendar(year, month-1, day);
    }

	/** Date -> "D/M/YYYY" */
	public static String toString(Calendar c) {
		StringBuilder builder = new StringBuilder();
		builder.append(c.get(Calendar.DATE)).append("/");
		builder.append(c.get(Calendar.MONTH) + 1).append("/");
		builder.append(c.get(Calendar.YEAR));
		return builder.toString();
	}

    /** Date -> "Day" */
    public static int toDay(Calendar c) {
        return c.get(Calendar.DATE);
    }

    /** Date -> "Month" */
    public static int toMonth(Calendar c){
        return c.get(Calendar.MONTH) + 1;
    }

    /** Date -> "Year" */
    public static int toYear(Calendar c){
        return c.get(Calendar.YEAR);
    }

    public static BigDecimal parseBigDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static ArrayList<String> parseStringList(String string){
        ArrayList<String> result = new ArrayList<String>();
        if(string!=null){
            String[] aliasesStrings = string.split("\\Q | \\E");
            for(String s : aliasesStrings){
                result.add(s);
            }
        }
        return result;
    }

    public static String toString(ArrayList<String> stringList){
        if(stringList.size()==0){
            return "";
        }
        StringBuilder builder = new StringBuilder(stringList.get(0));
        for(int i=1;i<stringList.size();i++){
            builder.append(" | ").append(stringList.get(i));
        }
        return builder.toString();

    }
}
