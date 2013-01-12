package be.dafke.Accounting.XML;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/** Een implementatie van ErrorHandler, die enkel de fout-stapel uitschrijft naar console */
public class FoutHandler implements ErrorHandler {
	@Override
	public void error(SAXParseException e) {
		e.printStackTrace();
	}

	@Override
	public void fatalError(SAXParseException e) {
		e.printStackTrace();
	}

	@Override
	public void warning(SAXParseException e) {
		e.printStackTrace();
	}
}