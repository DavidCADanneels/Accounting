package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Project;
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
		} else if (qName.equals("Accounts")) {
			String xmlLocation = atts.getValue("xml");
			accounting.setAccountLocationXml(new File(xmlLocation));
			String htmlLocation = atts.getValue("html");
			accounting.setAccountLocationHtml(new File(htmlLocation));
		} else if (qName.equals("Journals")) {
			String xmlLocation = atts.getValue("xml");
			accounting.setJournalLocationXml(new File(xmlLocation));
			String htmlLocation = atts.getValue("html");
			accounting.setJournalLocationHtml(new File(htmlLocation));
		} else if (qName.equals("Balances")) {
			String xmlLocation = atts.getValue("xml");
			accounting.setBalanceLocationXml(new File(xmlLocation));
			String htmlLocation = atts.getValue("html");
			accounting.setBalanceLocationHtml(new File(htmlLocation));
		} else if (qName.equals("Mortgages")) {
			String xmlLocation = atts.getValue("xml");
			accounting.setMortgageLocationXml(new File(xmlLocation));
			String htmlLocation = atts.getValue("html");
			accounting.setMortgageLocationHtml(new File(htmlLocation));
		} else if (qName.equals("Movements")) {
			String xmlLocation = atts.getValue("xml");
			accounting.setMovementLocationXml(new File(xmlLocation));
			String htmlLocation = atts.getValue("html");
			accounting.setMovementLocationHtml(new File(htmlLocation));
		} else if (qName.equals("Counterparties")) {
			String xmlLocation = atts.getValue("xml");
			accounting.setCounterpartyLocationXml(new File(xmlLocation));
			String htmlLocation = atts.getValue("html");
			accounting.setCounterpartyLocationHtml(new File(htmlLocation));
		} else if (qName.equals("account_name")) {
			b_account_name = true;
		} else if (qName.equals("account_type")) {
			b_account_type = true;
		} else if (qName.equals("account_project")) {
			b_account_project = true;
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
		if (b_account_name) {
			account_name = new String(text, start, length);
			b_account_name = false;
		} else if (b_account_type) {
			String typeString = new String(text, start, length);
			account_type = AccountType.valueOf(typeString);
			b_account_type = false;
		} else if (b_account_project) {
			String projectString = new String(text, start, length);
			project = accounting.getProjects().get(projectString);
			if (project == null) {
				project = new Project(projectString);
				accounting.getProjects().put(projectString, project);
			}
			b_account_project = false;
		} else if (b_journal_name) {
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
		if (qName.equals("Account")) {
			Account account = new Account(account_name, account_type);
			if (project != null) {
				project.addAccount(account);
				project = null;
			}
			account.setAccounting(accounting);
			accounting.getAccounts().add(account);
		} else if (qName.equals("Journal")) {
			Journal journal = new Journal(journal_name, journal_short);
			journal.setAccounting(accounting);
			accounting.getJournals().add(journal);
		}
	}
}
