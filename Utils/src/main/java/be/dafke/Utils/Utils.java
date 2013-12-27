package be.dafke.Utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
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

	/** Data -> "D/M/YYYY" */
	public static String toString(Calendar c) {
		StringBuilder builder = new StringBuilder();
		builder.append(c.get(Calendar.DATE)).append("/");
		builder.append(c.get(Calendar.MONTH) + 1).append("/");
		builder.append(c.get(Calendar.YEAR));
		return builder.toString();
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

    public static String getValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        return getValue(nodeList, tagName, 0);
    }

    private static String getValue(NodeList nodeList, String tagName, int index){
        if(nodeList.getLength()==0){
//            System.err.println("The tag " + tagName + " is not present.");
            return null;
            // the tag is not present
        } else {
            nodeList = nodeList.item(index).getChildNodes();
            if(nodeList.getLength()==0){
                System.err.println("The tag " + tagName + " is empty.");
                return null;
                // the tag is empty
            } else {
                if(nodeList.item(0).getNodeValue().equals("null")){
                    System.err.println("The tag " + tagName + " equals \"null\"");
                    return null;
                }
                return nodeList.item(0).getNodeValue();
            }
        }
    }

    public static File getFile(Element element, String tagName) {
        String fileName = getValue(element, tagName);
        if(fileName == null){
            return null;
        } else {
            return new File(fileName);
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
