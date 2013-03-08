package be.dafke;

import be.dafke.Accounting.Objects.Accounting.BusinessObject;
import org.apache.fop.cli.InputHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

    public static String getValue(Document document, String tagName){
        NodeList nodeList = document.getElementsByTagName(tagName);
        return getValue(nodeList, tagName, 0);
    }

    public static String getValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        return getValue(nodeList, tagName, 0);
    }

    public static Collection<String> getValues(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        Collection<String> result = new ArrayList<String>();
        for(int i=0;i<nodeList.getLength();i++){
            result.add(getValue(nodeList, tagName, i)); // TODO add null values to the list too ?
        }
        return result;
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

    public static String getXmlHeader(BusinessObject businessObject) {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
        "<?xml-stylesheet type=\"text/xsl\" href=\"" + getXsl2XmlFile(businessObject) + "\"?>\r\n" +
        "<!DOCTYPE " + getType(businessObject) + " SYSTEM \"" + getDtdFile(businessObject) + "\">\r\n";

    }

    public static String getType(BusinessObject businessObject){
        return businessObject.getClass().getSimpleName();
    }

    public static File getDtdFile(BusinessObject businessObject){
        File dtdFolder = new File(System.getProperty("Accountings_dtd"));
        return new File(dtdFolder, businessObject.getType() + ".dtd");
    }

    public static File getXsl2XmlFile(BusinessObject businessObject){
        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        return new File(xslFolder, businessObject.getType() + "2xml.xsl");
    }

    public static File getXsl2HtmlFile(BusinessObject businessObject){
        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        return new File(xslFolder, businessObject.getType() + "2html.xsl");
    }

    public static void xmlToHtml(BusinessObject businessObject) {
        File xslFile = getXsl2HtmlFile(businessObject);
        File xmlFile = businessObject.getXmlFile();
        File htmlFile = businessObject.getHtmlFile();
        InputHandler inputHandler = new InputHandler(xmlFile, xslFile, null);
        try {
            if (!htmlFile.exists()) {
//                htmlFile.getParentFile().mkdirs();
                if(htmlFile.createNewFile()){
                    System.out.println(htmlFile + " has been created");
                }
            }
            OutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(htmlFile));
            inputHandler.transformTo(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
