package be.dafke.Accounting.XML;

import org.apache.fop.cli.InputHandler;

import java.io.File;
import java.io.OutputStream;
import java.util.Vector;

public class XMLtoHTMLTransformer {
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
