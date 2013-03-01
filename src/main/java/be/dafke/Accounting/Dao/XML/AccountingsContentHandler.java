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
			String xmlFolder = atts.getValue("xmlFolder");
			acc.setXmlFolder(new File(xmlFolder));
            String xslFolder = atts.getValue("xslFolder");
            acc.setXslFolder(new File(xslFolder));
            String htmlFolder = atts.getValue("htmlFolder");
            acc.setHtmlFolder(new File(htmlFolder));
            String xml = atts.getValue("xml");
            acc.setXmlFile(new File(xml));
            String html = atts.getValue("html");
            acc.setHtmlFile(new File(html));
            String xsl2xml = atts.getValue("xsl2xml");
            acc.setXsl2XmlFile(new File(xsl2xml));
            String xsl2html = atts.getValue("xsl2html");
            acc.setXsl2HtmlFile(new File(xsl2html));
            accountings.addAccounting(acc);
            String current = atts.getValue("current");
            if("true".equals(current)){
                accountings.setCurrentAccounting(name);
            }
		}
	}
}
