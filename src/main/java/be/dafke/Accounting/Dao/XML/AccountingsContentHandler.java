package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
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
			String xml = atts.getValue("xml");
			acc.setLocationXml(new File(xml));
            String html = atts.getValue("html");
            acc.setLocationHtml(new File(html));
			if (acc != null) {
				accountings.addAccounting(acc);
			}
		}
	}
}
