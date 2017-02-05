package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessObject{
    private final AccountTypes accountTypes;
    private Accounts accounts;
	private Journals journals;
    private JournalTypes journalTypes;
    private Balances balances;
    private Mortgages mortgages;
    private Projects projects;
    private Contacts contacts;
    private VATTransactions vatTransactions;
    private VATFields vatFields;
    private Contact companyContact;
    private CounterParties counterParties;
    private Statements statements;
    private boolean read = false;

    public Accounting(String name) {
        setName(name);
        accountTypes = new AccountTypes();

        accounts = new Accounts(this);

        companyContact = new Contact();

        journalTypes = new JournalTypes();

        vatFields = new VATFields();
        vatTransactions = new VATTransactions(vatFields);

        journals = new Journals(this);

        balances = new Balances(accounts, accountTypes);

        contacts = new Contacts();

        mortgages = new Mortgages(this);

        projects = new Projects();
	}

    // Collections
    //
    public AccountTypes getAccountTypes() {
        return accountTypes;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public Journals getJournals() {
        return journals;
    }
    //
    public JournalTypes getJournalTypes() {
        return journalTypes;
    }

    public Balances getBalances() {
        return balances;
    }

    public Mortgages getMortgages() {
        return mortgages;
    }

    public Projects getProjects() {
        return projects;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public VATTransactions getVatTransactions() {
        return vatTransactions;
    }

    public VATFields getVatFields() {
        return vatFields;
    }

    public Contact getCompanyContact() {
        return companyContact;
    }

    public CounterParties getCounterParties() {
        return counterParties;
    }

    public Statements getStatements() {
        return statements;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void copyAccounts(Accounts accounts) {
        this.accounts = new Accounts(accounts);
    }

    public void copyJournals(Journals journals) {
        this.journals = new Journals(journals);
    }

    public void copyJournalTypes(JournalTypes journalTypes) {
        this.journalTypes = new JournalTypes(journalTypes);
    }
}