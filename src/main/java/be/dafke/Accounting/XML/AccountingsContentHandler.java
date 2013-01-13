package be.dafke.Accounting.XML;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;

public class AccountingsContentHandler extends DefaultHandler {
	private final File file;
	private final Accountings accountings;

	public AccountingsContentHandler(File file, Accountings accountings) {
		this.file = file;
		this.accountings = accountings;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("Accounting")) {
			String name = atts.getValue("name");
			Accounting acc = new Accounting(name);
			String location = atts.getValue("location");
			acc.setLocationXml(new File(location));
			if (acc != null) {
				accountings.addAccounting(acc);
			}
		}
	}
}
