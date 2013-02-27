package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;

public class AccountingsContentHandler extends DefaultHandler {
	private final Accountings accountings;

	public AccountingsContentHandler(Accountings accountings) {
		this.accountings = accountings;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("Accounting")) {
			String name = atts.getValue("name");
			Accounting acc = new Accounting(name);
			String xml = atts.getValue("xml");
			acc.setXmlFolder(new File(xml));
            String xsl = atts.getValue("xsl");
            acc.setXslFolder(new File(xsl));
            String html = atts.getValue("html");
            acc.setHtmlFolder(new File(html));
            accountings.addAccounting(acc);
            String current = atts.getValue("current");
            if("true".equals(current)){
                accountings.setCurrentAccounting(name);
            }
		}
	}
}
