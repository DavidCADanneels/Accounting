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
    private StockTransactions stockTransactions;
    private PurchaseOrders purchaseOrders;
    private SalesOrders salesOrders;
    private Transactions transactions;
    private VATTransactions vatTransactions;
    private VATFields vatFields;
    private Articles articles;
    private Contact companyContact=null;
    private Contact contactNoInvoice=null;
    private CounterParties counterParties;
    private Statements statements;
    private DeliverooMeals deliverooMeals;
    private MealOrders mealOrders;
    private boolean read = false;
    private boolean projectsAccounting = true;
    private boolean mortgagesAccounting = false;
    private boolean contactsAccounting = false;
    private boolean vatAccounting = false;
    private boolean tradeAccounting = false;
    private boolean deliverooAccounting = false;
    private Journal activeJournal;

    public Accounting(String name) {
        setName(name);
        accountTypes = new AccountTypes();

        accounts = new Accounts();

//        companyContact = new Contact();

        journalTypes = new JournalTypes();

        transactions = new Transactions(this);

        vatFields = new VATFields(this);
        vatTransactions = new VATTransactions(this);

        journals = new Journals(this);

        balances = new Balances(accounts, accountTypes);

        contacts = new Contacts(this);

        mortgages = new Mortgages();

        projects = new Projects();

        articles = new Articles();

        stockTransactions = new StockTransactions();

        purchaseOrders = new PurchaseOrders();

        salesOrders = new SalesOrders();

        deliverooMeals = new DeliverooMeals();

        mealOrders = new MealOrders();
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

    public Contact getContactNoInvoice() {
        return contactNoInvoice;
    }

    public CounterParties getCounterParties() {
        return counterParties;
    }

    public Statements getStatements() {
        return statements;
    }

    public Articles getArticles() {
        return articles;
    }

    public PurchaseOrders getPurchaseOrders() {
        return purchaseOrders;
    }

    public SalesOrders getSalesOrders() {
        return salesOrders;
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

    public void copyContacts(Contacts contacts) {
        this.contacts = new Contacts(contacts, accounts);
    }

    public void copyJournals(Journals journals) {
        this.journals = new Journals(journals);
    }

    public void copyJournalTypes(JournalTypes journalTypes) {
        this.journalTypes = new JournalTypes(journalTypes);
    }

    public boolean isVatAccounting() {
        return vatAccounting;
    }

    public void setVatAccounting(boolean vatAccounting) {
        this.vatAccounting = vatAccounting;
    }

    public boolean isMortgagesAccounting() {
        return mortgagesAccounting;
    }

    public void setMortgagesAccounting(boolean mortgagesAccounting) {
        this.mortgagesAccounting = mortgagesAccounting;
    }

    public boolean isProjectsAccounting() {
        return projectsAccounting;
    }

    public void setProjectsAccounting(boolean projectsAccounting) {
        this.projectsAccounting = projectsAccounting;
    }

    public boolean isTradeAccounting() {
        return tradeAccounting;
    }

    public void setTradeAccounting(boolean tradeAccounting) {
        this.tradeAccounting = tradeAccounting;
    }

    public boolean isContactsAccounting() {
        return contactsAccounting;
    }

    public void setContactsAccounting(boolean contactsAccounting) {
        this.contactsAccounting = contactsAccounting;
    }

    public boolean isDeliverooAccounting() {
        return deliverooAccounting;
    }

    public void setDeliverooAccounting(boolean deliverooAccounting) {
        this.deliverooAccounting = deliverooAccounting;
    }

    public void setCompanyContact(Contact companyContact) {
        this.companyContact = companyContact;
    }

    public void setContactNoInvoice(Contact contactNoInvoice) {
        this.contactNoInvoice = contactNoInvoice;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public void setActiveJournal(Journal activeJournal) {
        this.activeJournal = activeJournal;
    }

    public Journal getActiveJournal() {
        return activeJournal;
    }

    public DeliverooMeals getDeliverooMeals() {
        return deliverooMeals;
    }

    public void setDeliverooMeals(DeliverooMeals deliverooMeals) {
        this.deliverooMeals = deliverooMeals;
    }

    public MealOrders getMealOrders() {
        return mealOrders;
    }

    public void setMealOrders(MealOrders mealOrders) {
        this.mealOrders = mealOrders;
    }

    public StockTransactions getStockTransactions() {
        return stockTransactions;
    }
}