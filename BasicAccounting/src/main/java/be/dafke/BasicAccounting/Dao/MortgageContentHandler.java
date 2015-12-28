package be.dafke.BasicAccounting.Dao;

import be.dafke.BusinessModel.Mortgage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

public class MortgageContentHandler extends DefaultHandler {
	private final Mortgage mortgage;
	private boolean b_nr = false;
	private boolean b_mensuality = false;
	private boolean b_capital = false;
	private boolean b_intrest = false;
	private boolean b_restCapital = false;
	private final ArrayList<Vector<BigDecimal>> mortgageTable;
	private Vector<BigDecimal> vector;

	public MortgageContentHandler(Mortgage mortgage) {
		this.mortgage = mortgage;
		this.mortgageTable = new ArrayList<Vector<BigDecimal>>();
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("nr")) {
			b_nr = true;
		} else if (qName.equals("mensuality")) {
			b_mensuality = true;
		} else if (qName.equals("capital")) {
			b_capital = true;
		} else if (qName.equals("intrest")) {
			b_intrest = true;
		} else if (qName.equals("restCapital")) {
			b_restCapital = true;
		}
	}

	@Override
	public void characters(char[] text, int start, int length) throws SAXException {
		if (b_nr) {
			vector = new Vector<BigDecimal>();
			b_nr = false;
		} else if (b_mensuality) {
			String amountString = new String(text, start, length);
			BigDecimal amount = new BigDecimal(amountString);
			vector.add(amount);
			b_mensuality = false;
		} else if (b_capital) {
			String amountString = new String(text, start, length);
			BigDecimal amount = new BigDecimal(amountString);
			vector.add(amount);
			b_capital = false;
		} else if (b_intrest) {
			String amountString = new String(text, start, length);
			BigDecimal amount = new BigDecimal(amountString);
			vector.add(amount);
			b_intrest = false;
		} else if (b_restCapital) {
			String amountString = new String(text, start, length);
			BigDecimal amount = new BigDecimal(amountString);
			vector.add(amount);
			b_restCapital = false;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		mortgage.setTable(mortgageTable);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (qName.equals("line")) {
			mortgageTable.add(vector);
		}
	}
}
