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
    private StockOrders stockOrders;
    private PromoOrders promoOrders;
    private IngredientOrders ingredientOrders;
    private Transactions transactions;
    private VATTransactions vatTransactions;
    private VATFields vatFields;
    private Articles articles;
    private Ingredients ingredients;
    private Allergenes allergenes;
    private Contact companyContact=null;
    private Contact contactNoInvoice=null;
    private CounterParties counterParties;
    private Statements statements;
    private Meals meals;
    private MealOrders mealOrders;
    private boolean read = false;
    private boolean projectsAccounting = true;
    private boolean mortgagesAccounting = false;
    private boolean contactsAccounting = false;
    private boolean vatAccounting = false;
    private boolean tradeAccounting = false;
    private boolean mealsAccounting = false;

    public Accounting(String name) {
        setName(name);
        accountTypes = new AccountTypes();

        accounts = new Accounts();

//        companyContact = new Contact();

        journalTypes = new JournalTypes();

        transactions = new Transactions(this);

        vatFields = new VATFields(this);
        vatTransactions = new VATTransactions();

        journals = new Journals(this);

        balances = new Balances(accounts, accountTypes);

        contacts = new Contacts(this);

        mortgages = new Mortgages();

        projects = new Projects();

        articles = new Articles();

        ingredients = new Ingredients();

        allergenes = new Allergenes();

        stockTransactions = new StockTransactions();

        purchaseOrders = new PurchaseOrders();

        salesOrders = new SalesOrders();

        stockOrders = new StockOrders();

        promoOrders = new PromoOrders();

        ingredientOrders = new IngredientOrders();

        meals = new Meals();

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

    public Ingredients getIngredients() {
        return ingredients;
    }

    public Allergenes getAllergenes() {
        return allergenes;
    }

    public PurchaseOrders getPurchaseOrders() {
        return purchaseOrders;
    }

    public SalesOrders getSalesOrders() {
        return salesOrders;
    }

    public StockOrders getStockOrders() {
        return stockOrders;
    }

    public PromoOrders getPromoOrders() {
        return promoOrders;
    }

    public IngredientOrders getIngredientOrders() {
        return ingredientOrders;
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

    public void copyVatSettings(VATTransactions vatTransactions) {
        vatFields.clear();
        vatFields.addDefaultFields();
        Account debitAccount = vatTransactions.getDebitAccount();
        Account creditAccount = vatTransactions.getCreditAccount();
        Account debitCNAccount = vatTransactions.getDebitCNAccount();
        Account creditCNAccount = vatTransactions.getCreditCNAccount();

        if(debitAccount!=null){
            Account account = accounts.getBusinessObject(debitAccount.getName());
            vatTransactions.setDebitAccount(account);
        }
        if(creditAccount!=null){
            Account account = accounts.getBusinessObject(creditAccount.getName());
            vatTransactions.setCreditAccount(account);
        }
        if(debitCNAccount!=null){
            Account account = accounts.getBusinessObject(debitCNAccount.getName());
            vatTransactions.setDebitCNAccount(account);
        }
        if(creditCNAccount!=null){
            Account account = accounts.getBusinessObject(creditCNAccount.getName());
            vatTransactions.setCreditCNAccount(account);
        }
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

    public boolean isMealsAccounting() {
        return mealsAccounting;
    }

    public void setMealsAccounting(boolean mealsAccounting) {
        this.mealsAccounting = mealsAccounting;
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

    public Meals getMeals() {
        return meals;
    }

    public void setMeals(Meals meals) {
        this.meals = meals;
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