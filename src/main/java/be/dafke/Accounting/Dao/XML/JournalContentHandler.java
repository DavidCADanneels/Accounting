package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;

public class JournalContentHandler extends DefaultHandler {
	private final Journal journal;
	private final Accounting accounting;
	private Account bookingAccount;
	private boolean nr = false;
	private boolean description = false;
	private boolean date = false;
	private boolean account = false;
	private boolean debit = false;
	private boolean credit = false;
    private Transaction transaction = null;

//	private final Transaction transaction;

	public JournalContentHandler(Accounting accounting, Journal journal) {
		this.journal = journal;
		this.accounting = accounting;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals("action")){
            transaction = new Transaction();
        }
		if (qName.equals("nr")) {
			nr = true;
		} else if (qName.equals("date")) {
			date = true;
		} else if (qName.equals("account")) {
			account = true;
		} else if (qName.equals("debet")) {
			debit = true;
		} else if (qName.equals("credit")) {
			credit = true;
		} else if (qName.equals("description")) {
			description = true;
		}
	}

	@Override
	public void characters(char[] text, int start, int length) {
		if (nr) {
			String s = new String(text, start, length);
			s = s.replaceAll("DIV", "");
			transaction.setId(Integer.valueOf(s));
			nr = false;
		}
		if (date) {
			String s = new String(text, start, length);
			transaction.setDate(Utils.toCalendar(s));
			date = false;
		}
		if (description) {
			String s = new String(text, start, length);
			transaction.setDescription(s);
			description = false;
		}
		if (account) {
			String s = new String(text, start, length);
			bookingAccount = accounting.getAccounts().get(s);
			account = false;
		}
		if (debit) {
			String s = new String(text, start, length);
			BigDecimal amount = new BigDecimal(s);
			transaction.debiteer(bookingAccount, amount);
			debit = false;
		}
		if (credit) {
			String s = new String(text, start, length);
			BigDecimal amount = new BigDecimal(s);
			transaction.crediteer(bookingAccount, amount);
			credit = false;
		}
	}


    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (qName.equals("action")){
            transaction.book(journal);
        }
    }
}
