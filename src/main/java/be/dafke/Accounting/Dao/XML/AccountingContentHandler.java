package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Project;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.math.BigDecimal;

public class AccountingContentHandler extends DefaultHandler {
	private final Accounting accounting;
	private String account_name, journal_name;
	private String journal_short;
	private AccountType account_type;
	private boolean b_journal_short = false;
	private boolean b_journal_name = false;
	private boolean b_account_type = false;
	private boolean b_account_name = false;
	private boolean b_nrPayed = false;
	private boolean b_capital_account = false;
	private boolean b_intrest_account = false;
	private boolean b_account_project = false;
	private Mortgage mortgage;
	private Project project;
    private CounterParty counterParty;

	public AccountingContentHandler(Accounting accounting) {
		this.accounting = accounting;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("Accounting")) {
//			String name = atts.getValue("name");
//			accounting = accountings.getAccounting(name);
//			String location = atts.getValue("location");
//			accounting.setLocation(new File(location));
			String xsl = atts.getValue("xsl");
			accounting.setLocationXSL(new File(xsl));
        } else if (qName.equals("Counterparty")){
            String name = atts.getValue("name");
            counterParty = new CounterParty(name);
            // TODO: import Counterparties
//        } else if (qName.equals("AccountName")){
//            Account account = accounting.getAccounts().get()
//            counterParty.setAccount();
//        } else if (qName.equals("BankAccount")){
//            counterParty.addAccount(new BankAccount());
//        } else if (qName.equals("BIC")){
//            counterParty.s
//        } else if (qName.equals("Currency")){
//            String name = atts.getValue("name");
//            counterParty = new CounterParty(name);
		} else if (qName.equals("journal_name")) {
			b_journal_name = true;
		} else if (qName.equals("journal_short")) {
			b_journal_short = true;
		} else if (qName.equals("Mortgage")) {
			String name = atts.getValue("name");
			String totalString = atts.getValue("total");
			BigDecimal amount = new BigDecimal(totalString);
			mortgage = new Mortgage(name, amount);
//			mortgage.setAccounting(accounting);
			accounting.addMortgageTable(name, mortgage);
		} else if (qName.equals("nrPayed")) {
			b_nrPayed = true;
		} else if (qName.equals("capital_account")) {
			b_capital_account = true;
		} else if (qName.equals("intrest_account")) {
			b_intrest_account = true;
		}
	}

	@Override
	public void characters(char[] text, int start, int length) {
		if (b_journal_name) {
			journal_name = new String(text, start, length);
			b_journal_name = false;
		} else if (b_journal_short) {
			journal_short = new String(text, start, length);
			b_journal_short = false;
		} else if (b_nrPayed) {
			String nrString = new String(text, start, length);
			int nr = Integer.valueOf(nrString);
			mortgage.setPayed(nr);
			b_nrPayed = false;
		} else if (b_capital_account) {
			String accountString = new String(text, start, length);
			Account account = accounting.getAccounts().get(accountString);
			mortgage.setCapitalAccount(account);
			b_capital_account = false;
		} else if (b_intrest_account) {
			String accountString = new String(text, start, length);
			Account account = accounting.getAccounts().get(accountString);
			mortgage.setIntrestAccount(account);
			b_intrest_account = false;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) {
		if (qName.equals("Journal")) {
			Journal journal = new Journal(journal_name, journal_short);
			journal.setAccounting(accounting);
			accounting.getJournals().add(journal);
        } else if (qName.equals("Counterparty")){
            accounting.addCounterparty(counterParty);
//            accounting.getCounterParties().put(counterParty.getName(),counterParty);
		}
	}
}
