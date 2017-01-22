package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessCollection<BusinessCollection<BusinessObject>> {
    private final AccountTypes accountTypes;
    private final Accounts accounts;
	private final Journals journals;
    private final JournalTypes journalTypes;
    private final Balances balances;
    private final Mortgages mortgages;
    private Projects projects;
    private Contacts contacts;
    private VATTransactions vatTransactions;
    private VATFields vatFields;
    private Contact companyContact;
    private boolean read = false;

    public Accounting(String name) {
        setName(name);
        accountTypes = new AccountTypes();

        accounts = new Accounts();

        companyContact = new Contact();

        journalTypes = new JournalTypes();

        vatFields = new VATFields();
        vatTransactions = new VATTransactions(vatFields);

        journals = new Journals();

        balances = new Balances(accounts, accountTypes);

        contacts = new Contacts();

        mortgages = new Mortgages();

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
    //
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}