package be.dafke;

import org.apache.fop.cli.InputHandler;

import java.io.File;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
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

    public static void xmlToHtml(File xmlFile, File xslFile, File htmlFile, Vector params) {
        InputHandler inputHandler = new InputHandler(xmlFile, xslFile, params);
        try {
            if (!htmlFile.exists()) {
                htmlFile.getParentFile().mkdirs();
                htmlFile.createNewFile();
            }
            OutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(htmlFile));
            inputHandler.transformTo(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeXml() {
        // TODO write generic function to write Xml file for Account, Journal, etc.
        // or at least the header: links to xsl, dtd, etc. (Marshallers)
        // or implement a Dao package + XmlDao (and later JdbcDao) implementation
    }
}
