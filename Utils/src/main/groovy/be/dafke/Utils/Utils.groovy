package be.dafke.Utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class Utils {
    /** "D/M/YYYY" -> Data */
    static Calendar toCalendar(String s) {
        if (s==null) null
        Pattern p = Pattern.compile("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}")
        Matcher m = p.matcher(s)
        if (m.matches()) {
            try {
                String[] delen = s.split("/", 3)
                int dag = Integer.parseInt(delen[0])
                int maand = Integer.parseInt(delen[1]) - 1
                int jaar = Integer.parseInt(delen[2])
                return new GregorianCalendar(jaar, maand, dag)
            } catch (NumberFormatException nfe) {
                return null
            }
        }
        return null
    }

    /**
     *
     * @param id the number to transform/extend into a String
     * @param length the number of digit to extend to
     * @return
     */
    static String toIDString(String prefix, int id, int length){
        StringBuilder builder = new StringBuilder(prefix)
        for(int power=1;power<=length;power++){
            double maxDouble = Math.pow(10, power)
            int maxInt = ((int) maxDouble)
            if(id<maxInt){
                int numberOfZeroToAdd = length - power
                for (int i = 0; i < numberOfZeroToAdd; i++) {
                    builder.append("0")
                }
                builder.append(id)
                return builder.toString()
            }
        }
        builder.append(id)
        return builder.toString()
    }

    /** "D, M, YYYY" -> Data */
    static Calendar toCalendar(String day, String month, String year) {
        try {
            int jaar = Integer.parseInt(year)
            int maand = Integer.parseInt(month)
            int dag = Integer.parseInt(day)
            new GregorianCalendar(jaar, maand-1, dag)
        } catch (NumberFormatException nfe) {
            null
        }
    }

    /** "D, M, YYYY" -> Data */
    static Calendar toCalendar(int day, int month, int year) {
        new GregorianCalendar(year, month-1, day)
    }

    /** Date -> "D/M/YYYY" */
    static String toString(Calendar c) {
//	    if(c == null) "null"
        StringBuilder builder = new StringBuilder()
        builder.append(c.get(Calendar.DATE)).append("/")
        builder.append(c.get(Calendar.MONTH) + 1).append("/")
        builder.append(c.get(Calendar.YEAR))
        builder.toString()
    }

    /** Date -> "Day" */
    static int toDay(Calendar c) {
        c.get(Calendar.DATE)
    }

    /** Date -> "Month" */
    static int toMonth(Calendar c){
        c.get(Calendar.MONTH) + 1
    }

    /** Date -> "Year" */
    static int toYear(Calendar c){
        c.get(Calendar.YEAR)
    }

    // 6 -> 0.06
    static BigDecimal getPercentage(int pct){
        new BigDecimal(pct).divide(new BigDecimal(100))
    }

    // 6 -> 1.06
    static BigDecimal getFactor(int pct){
        BigDecimal.ONE.add(getPercentage(pct))
    }

    static BigDecimal parseBigDecimal(String s) {
        try {
            new BigDecimal(s)
        } catch (NumberFormatException nfe) {
            null
        }
    }

    static BigInteger parseBigInteger(String s) {
        try {
            new BigInteger(s)
        } catch (NumberFormatException nfe) {
            null
        }
    }

    static int parseInt(String s) {
        if(s==null) 0
        try {
            Integer.parseInt(s)
        } catch (NumberFormatException nfe) {
            0
        }
    }

    static ArrayList<String> parseStringList(String string){
        ArrayList<String> result = new ArrayList<String>()
        if(string!=null){
            String[] aliasesStrings = string.split("\\Q | \\E")
            for(String s : aliasesStrings){
                result.add(s)
            }
        }
        result
    }

    static String toString(ArrayList<String> stringList){
        if(stringList.size()==0){
            return ""
        }
        StringBuilder builder = new StringBuilder(stringList.get(0))
        for(int i=1; i<stringList.size(); i++){
            builder.append(" | ").append(stringList.get(i))
        }
        builder.toString()
    }
}
