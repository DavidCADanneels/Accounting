package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;

public class JournalContentHandler extends DefaultHandler {
	private final Journal journal;
	private final Accounts accounts;
	private Account bookingAccount;
	private boolean nr = false;
	private boolean description = false;
	private boolean date = false;
	private boolean account = false;
	private boolean debit = false;
	private boolean credit = false;
	private boolean action = false;
    private Transaction transaction = new Transaction();

//	private final Transaction transaction;

	public JournalContentHandler(Accounts accounts, Journal journal) {
		this.journal = journal;
		this.accounts = accounts;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("nr")) {
			if (action) {
				transaction.book(journal);
				transaction = new Transaction();
			} else {
				action = true;
			}
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
            // TODO: split up in abbreviation and number / or make 2 separate tags
			s = s.replaceAll("DIV", "");
            transaction.setAbbreviation("DIV");
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
			bookingAccount = accounts.get(s);
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
	public void endDocument() {
		transaction.book(journal);
	}
}
