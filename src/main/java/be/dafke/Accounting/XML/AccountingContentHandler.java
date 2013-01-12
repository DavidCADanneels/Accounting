package be.dafke.Accounting.XML;

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;

public class AccountingContentHandler extends DefaultHandler {
	private Accounting accounting = null;

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("Accounting")) {
			String name = atts.getValue("name");
			accounting = Accountings.getAccounting(name);
			String location = atts.getValue("location");
			accounting.setLocation(new File(location));
		} else if (qName.equals("Accounts")) {
			String location = atts.getValue("location");
			accounting.setAccountLocation(new File(location));
		} else if (qName.equals("Journals")) {
			String location = atts.getValue("location");
			accounting.setJournalLocation(new File(location));
		} else if (qName.equals("Balances")) {
			String location = atts.getValue("location");
			accounting.setBalanceLocation(new File(location));
		} else if (qName.equals("Mortgages")) {
			String location = atts.getValue("location");
			accounting.setMortgageLocation(new File(location));
		} else if (qName.equals("Movements")) {
			String location = atts.getValue("location");
			accounting.setMovementLocation(new File(location));
		} else if (qName.equals("Counterparties")) {
			String location = atts.getValue("location");
			accounting.setCounterpartyLocation(new File(location));
		}
	}
}
