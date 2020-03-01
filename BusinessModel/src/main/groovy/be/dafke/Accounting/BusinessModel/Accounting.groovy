package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

/**
 * @author David Danneels
 */
class Accounting extends BusinessObject{
    final AccountTypes accountTypes
    Accounts accounts
    Journals journals
    JournalTypes journalTypes
    Balances balances
    Mortgages mortgages
    Projects projects
    Contacts contacts
    StockTransactions stockTransactions
    PurchaseOrders purchaseOrders
    SalesOrders salesOrders
    StockOrders stockOrders
    PromoOrders promoOrders
    IngredientOrders ingredientOrders
    Transactions transactions
    VATTransactions vatTransactions
    VATFields vatFields
    Articles articles
    Services services
    Goods goods
    Ingredients ingredients
    Allergenes allergenes
    Contact companyContact=null
    Contact contactNoInvoice=null
    CounterParties counterParties
    Statements statements
    Meals meals
    MealOrders mealOrders
    boolean read = false
    boolean projectsAccounting = true
    boolean mortgagesAccounting = false
    boolean contactsAccounting = false
    boolean vatAccounting = false
    boolean tradeAccounting = false
    boolean mealsAccounting = false

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

        services = new Services()

        goods = new Goods()

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

        if(debitAccount){
            Account account = accounts.getBusinessObject(debitAccount.name)
            vatTransactions.setDebitAccount(account)
        }
        if(creditAccount){
            Account account = accounts.getBusinessObject(creditAccount.name)
            vatTransactions.setCreditAccount(account)
        }
        if(debitCNAccount){
            Account account = accounts.getBusinessObject(debitCNAccount.name)
            vatTransactions.setDebitCNAccount(account)
        }
        if(creditCNAccount){
            Account account = accounts.getBusinessObject(creditCNAccount.name)
            vatTransactions.setCreditCNAccount(account)
        }
    }

    void copyJournalTypes(JournalTypes journalTypes) {
        this.journalTypes = new JournalTypes(journalTypes)
    }
}