package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

/**
 * @author David Danneels
 */
class Accounting extends BusinessObject{
    private final AccountTypes accountTypes
    private Accounts accounts
    private Journals journals
    private JournalTypes journalTypes
    private Balances balances
    private Mortgages mortgages
    private Projects projects
    private Contacts contacts
    private StockTransactions stockTransactions
    private PurchaseOrders purchaseOrders
    private SalesOrders salesOrders
    private StockOrders stockOrders
    private PromoOrders promoOrders
    private IngredientOrders ingredientOrders
    private Transactions transactions
    private VATTransactions vatTransactions
    private VATFields vatFields
    private Articles articles
    private Ingredients ingredients
    private Allergenes allergenes
    private Contact companyContact=null
    private Contact contactNoInvoice=null
    private CounterParties counterParties
    private Statements statements
    private Meals meals
    private MealOrders mealOrders
    private boolean read = false
    private boolean projectsAccounting = true
    private boolean mortgagesAccounting = false
    private boolean contactsAccounting = false
    private boolean vatAccounting = false
    private boolean tradeAccounting = false
    private boolean mealsAccounting = false

    Accounting(String name) {
        setName(name)
        accountTypes = new AccountTypes()

        accounts = new Accounts()

//        companyContact = new Contact()

        journalTypes = new JournalTypes()

        transactions = new Transactions(this)

        vatFields = new VATFields(this)
        vatTransactions = new VATTransactions()

        journals = new Journals(this)

        balances = new Balances(accounts, accountTypes)

        contacts = new Contacts(this)

        mortgages = new Mortgages()

        projects = new Projects()

        articles = new Articles()

        ingredients = new Ingredients()

        allergenes = new Allergenes()

        stockTransactions = new StockTransactions()

        purchaseOrders = new PurchaseOrders()

        salesOrders = new SalesOrders()

        stockOrders = new StockOrders()

        promoOrders = new PromoOrders()

        ingredientOrders = new IngredientOrders()

        meals = new Meals()

        mealOrders = new MealOrders()
    }

    // Collections
    //
    AccountTypes getAccountTypes() {
        accountTypes
    }

    Accounts getAccounts() {
        accounts
    }

    Journals getJournals() {
        journals
    }
    //
    JournalTypes getJournalTypes() {
        journalTypes
    }

    Balances getBalances() {
        balances
    }

    Mortgages getMortgages() {
        mortgages
    }

    Projects getProjects() {
        projects
    }

    Contacts getContacts() {
        contacts
    }

    VATTransactions getVatTransactions() {
        vatTransactions
    }

    VATFields getVatFields() {
        vatFields
    }

    Contact getCompanyContact() {
        companyContact
    }

    Contact getContactNoInvoice() {
        contactNoInvoice
    }

    CounterParties getCounterParties() {
        counterParties
    }

    Statements getStatements() {
        statements
    }

    Articles getArticles() {
        articles
    }

    Ingredients getIngredients() {
        ingredients
    }

    Allergenes getAllergenes() {
        allergenes
    }

    PurchaseOrders getPurchaseOrders() {
        purchaseOrders
    }

    SalesOrders getSalesOrders() {
        salesOrders
    }

    StockOrders getStockOrders() {
        stockOrders
    }

    PromoOrders getPromoOrders() {
        promoOrders
    }

    IngredientOrders getIngredientOrders() {
        ingredientOrders
    }

    boolean isRead() {
        read
    }

    void setRead(boolean read) {
        this.read = read
    }

    void copyAccounts(Accounts accounts) {
        this.accounts = new Accounts(accounts)
    }

    void copyContacts(Contacts contacts) {
        this.contacts = new Contacts(contacts, accounts)
    }

    void copyJournals(Journals journals) {
        this.journals = new Journals(journals)
    }

    void copyVatSettings(VATTransactions vatTransactions) {
        vatFields.clear()
        vatFields.addDefaultFields()
        Account debitAccount = vatTransactions.getDebitAccount()
        Account creditAccount = vatTransactions.getCreditAccount()
        Account debitCNAccount = vatTransactions.getDebitCNAccount()
        Account creditCNAccount = vatTransactions.getCreditCNAccount()

        if(debitAccount!=null){
            Account account = accounts.getBusinessObject(debitAccount.getName())
            vatTransactions.setDebitAccount(account)
        }
        if(creditAccount!=null){
            Account account = accounts.getBusinessObject(creditAccount.getName())
            vatTransactions.setCreditAccount(account)
        }
        if(debitCNAccount!=null){
            Account account = accounts.getBusinessObject(debitCNAccount.getName())
            vatTransactions.setDebitCNAccount(account)
        }
        if(creditCNAccount!=null){
            Account account = accounts.getBusinessObject(creditCNAccount.getName())
            vatTransactions.setCreditCNAccount(account)
        }
    }

    void copyJournalTypes(JournalTypes journalTypes) {
        this.journalTypes = new JournalTypes(journalTypes)
    }

    boolean isVatAccounting() {
        vatAccounting
    }

    void setVatAccounting(boolean vatAccounting) {
        this.vatAccounting = vatAccounting
    }

    boolean isMortgagesAccounting() {
        mortgagesAccounting
    }

    void setMortgagesAccounting(boolean mortgagesAccounting) {
        this.mortgagesAccounting = mortgagesAccounting
    }

    boolean isProjectsAccounting() {
        projectsAccounting
    }

    void setProjectsAccounting(boolean projectsAccounting) {
        this.projectsAccounting = projectsAccounting
    }

    boolean isTradeAccounting() {
        tradeAccounting
    }

    void setTradeAccounting(boolean tradeAccounting) {
        this.tradeAccounting = tradeAccounting
    }

    boolean isContactsAccounting() {
        contactsAccounting
    }

    void setContactsAccounting(boolean contactsAccounting) {
        this.contactsAccounting = contactsAccounting
    }

    boolean isMealsAccounting() {
        mealsAccounting
    }

    void setMealsAccounting(boolean mealsAccounting) {
        this.mealsAccounting = mealsAccounting
    }

    void setCompanyContact(Contact companyContact) {
        this.companyContact = companyContact
    }

    void setContactNoInvoice(Contact contactNoInvoice) {
        this.contactNoInvoice = contactNoInvoice
    }

    Transactions getTransactions() {
        transactions
    }

    Meals getMeals() {
        meals
    }

    void setMeals(Meals meals) {
        this.meals = meals
    }

    MealOrders getMealOrders() {
        mealOrders
    }

    void setMealOrders(MealOrders mealOrders) {
        this.mealOrders = mealOrders
    }

    StockTransactions getStockTransactions() {
        stockTransactions
    }
}