package be.dafke.BasicAccounting.Dao;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/** Een implementatie van ErrorHandler, die enkel de fout-stapel uitschrijft naar console */
public class FoutHandler implements ErrorHandler {
	public void error(SAXParseException e) {
		e.printStackTrace();
	}
	public void fatalError(SAXParseException e) {
		e.printStackTrace();
	}
	public void warning(SAXParseException e) {
		e.printStackTrace();
	}
}