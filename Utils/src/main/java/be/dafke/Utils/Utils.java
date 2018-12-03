package be.dafke.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /** "D/M/YYYY" -> Data */
	public static Calendar toCalendar(String s) {
	    if (s==null) return null;
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

    /**
     *
     * @param id the number to transform/extend into a String
     * @param length the number of digit to extend to
     * @return
     */
	public static String toIDString(String prefix, int id, int length){
        StringBuilder builder = new StringBuilder(prefix);
        for(int power=1; power<=length; power++){
            double maxDouble = Math.pow(10, power);
            int maxInt = ((int) maxDouble);
            if(id<maxInt){
                int numberOfZeroToAdd = length - power;
                for (int i = 0; i < numberOfZeroToAdd; i++) {
                    builder.append("0");
                }
                builder.append(id);
                return builder.toString();
            }
        }
        return prefix + id;
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
//	    if(c == null) return "null";
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

    // 6 -> 0.06
    public static BigDecimal getPercentage(int pct){
        return new BigDecimal(pct).divide(new BigDecimal(100));
    }

    // 6 -> 1.06
    public static BigDecimal getFactor(int pct){
        return BigDecimal.ONE.add(getPercentage(pct));
    }

    public static BigDecimal parseBigDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static BigInteger parseBigInteger(String s) {
        try {
            return new BigInteger(s);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static int parseInt(String s) {
        if(s==null) return 0;
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
