package be.dafke.Accounting.BasicAccounting.MainApplication.Settings

import be.dafke.Accounting.BasicAccounting.MainApplication.Settings.AccountingSettingsPanel
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import javax.swing.*

class AccountingCopyPanel extends JPanel {

    final JCheckBox copyAccounts, copyJournals, copyContacts, copyVat, copyTrade, copyMealOrders
    AccountingSettingsPanel accountingSettingsPanel
    Accounting copyFrom
    Accounting newAccounting

    AccountingCopyPanel(){
        newAccounting = createAccounting()
        copyAccounts = new JCheckBox("copy Accounts")
        copyJournals = new JCheckBox("copy Journals")
        copyContacts = new JCheckBox("copy Contacts")
        copyVat = new JCheckBox("copy VAT Settings")
        copyTrade = new JCheckBox("copy Trade Settings")
        copyMealOrders = new JCheckBox("copy Meal Order Settings")

        copyAccounts.addActionListener({ e -> updateCopyAccountsSelected() })
        copyJournals.addActionListener({ e -> updateCopyJournalsSelected() })
        copyContacts.addActionListener({ e -> updateCopyContactsSelected() })
        copyVat.addActionListener({ e -> updateCopyVATSelected() })
        copyTrade.addActionListener({ e -> updateCopyTradeSelected() })
        copyMealOrders.addActionListener({ e -> updateCopyMealSelected() })

        copyAccounts.enabled = false
        copyJournals.enabled = false
        copyContacts.enabled = false
        copyVat.enabled = false
        copyTrade.enabled = false
        copyMealOrders.enabled = false

        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        panel.add(copyAccounts)
        panel.add(copyJournals)
        panel.add(copyContacts)
        panel.add(copyVat)
        panel.add(copyTrade)
        panel.add(copyMealOrders)

        add(panel)
    }

    // SET

    Accounting getAccounting(){
        newAccounting
    }

    void setSettingsPanel(AccountingSettingsPanel accountingSettingsPanel) {
        this.accountingSettingsPanel = accountingSettingsPanel
    }

    void setCopyFrom(Accounting copyFrom) {
        this.copyFrom = copyFrom
        selectCopyAccounts(copyAccounts.selected)
        selectCopyJournals(copyJournals.selected)
        selectCopyContacts(copyContacts.selected)
        selectCopyVat(copyVat.selected)
        selectCopyTrade(copyTrade.selected)
        selectCopyMeals(copyMealOrders.selected)
    }

    // CREATE

    Accounting createAccounting(){
        Accounting accounting = new Accounting("New Accounting")
        accounting.accountTypes.addDefaultTypes()
        accounting.journalTypes.addDefaultType(accounting.accountTypes)
        accounting.balances.addDefaultBalances()
        accounting
    }

    // COPY

    void copyAccounts(){
        newAccounting.copyAccounts(copyFrom.accounts)
    }

    void copyJournals(){
        newAccounting.copyJournals(copyFrom.journals)
        newAccounting.copyJournalTypes(copyFrom.journalTypes)
    }

    void copyContacts(){
        newAccounting.copyContacts(copyFrom.contacts)
    }

    void copyVatSettings(){
        newAccounting.copyVatSettings(copyFrom.vatTransactions)
    }

    void copyArticles() {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
        purchaseOrder.setName("PO0")

        Articles articlesFrom = copyFrom.articles
        Articles articlesTo = newAccounting.articles
        articlesTo.clear()
        articlesFrom.businessObjects.forEach({ article ->
            Article newArticle = new Article(article, newAccounting.contacts)
            Integer numberOfItems = article.getNrInStock()
            if (numberOfItems > 0) {
                OrderItem orderItem = new OrderItem(numberOfItems, newArticle, purchaseOrder)
                purchaseOrder.addBusinessObject(orderItem)
                // TODO: call setPurchaseTransaction (= beginBalans) 'later' iso setPoOrdered
                // setPurchaseTransaction calls setPoOrdered as well
            }
            try {
                articlesTo.addBusinessObject(newArticle)
            } catch (EmptyNameException e) {
                e.printStackTrace()
            } catch (DuplicateNameException e) {
                e.printStackTrace()
            }
        })
        if (!purchaseOrder.businessObjects.isEmpty()) {
            // TODO: 'later' = here !
//            purchaseOrder.setPurchaseTransaction(beginBalans)
            try {
                PurchaseOrders purchaseOrders = newAccounting.purchaseOrders
                purchaseOrders.addBusinessObject(purchaseOrder)
            } catch (EmptyNameException e) {
                e.printStackTrace()
            } catch (DuplicateNameException e) {
                e.printStackTrace()
            }
            StockTransactions stockTransactions = newAccounting.stockTransactions
            stockTransactions.addOrder(purchaseOrder)
        }
    }

    void copyMeals() {
        Meals mealsFrom = copyFrom.meals
        Meals mealsTo = newAccounting.meals
        mealsTo.clear()
        mealsFrom.businessObjects.forEach({ meal ->
            Meal newMeal = new Meal(meal)
            try {
                mealsTo.addBusinessObject(newMeal)
            } catch (EmptyNameException e) {
                e.printStackTrace()
            } catch (DuplicateNameException e) {
                e.printStackTrace()
            }
        })
    }

    // UPDATE

    void updateCopyAccountsSelected() {
        boolean copyAccountsSelected = copyAccounts.selected
        if(copyAccountsSelected){
            copyAccounts()
        } else {
            selectCopyContacts(false)
        }
    }

    void updateCopyJournalsSelected() {
        boolean copyJournalsSelected = copyJournals.selected
        if(copyJournalsSelected){
            copyJournals()
        } else {

        }
    }

    void updateCopyContactsSelected() {
        boolean enabled = copyFrom.isContactsAccounting()
        boolean selected = enabled && copyContacts.selected
        if(selected){
            selectCopyAccounts(true)
            accountingSettingsPanel.setContactsSelected(true)
            copyContacts()
            if(copyFrom){
                accountingSettingsPanel.copyContacts(copyFrom)
            } else {
                accountingSettingsPanel.copyContacts(null)
            }
        } else {
//            accountingSettingsPanel.setVatSelected(false)
            selectCopyVat(false)
            accountingSettingsPanel.copyContacts(null)
        }
    }

    void updateCopyVATSelected() {
        boolean selected = copyVat.selected
        if(selected){
            selectCopyAccounts(true)
            selectCopyJournals(true)
            selectCopyContacts(true)
            accountingSettingsPanel.setVatSelected(true)
            copyVatSettings()
            if(copyFrom){
                accountingSettingsPanel.copyVatSettings(copyFrom)
            } else {
                accountingSettingsPanel.copyVatSettings(null)
            }
        } else {
            selectCopyTrade(false)
            selectCopyMeals(false)
            accountingSettingsPanel.copyVatSettings(null)
        }
    }

    void updateCopyTradeSelected() {
        boolean selected = copyTrade.selected
        if(selected){
            selectCopyVat(true)
            accountingSettingsPanel.setTradeSelected(true)
            if(copyFrom){
                // TODO: need separate check box to copy Articles?
                accountingSettingsPanel.copyTradeSettings(copyFrom)
                copyArticles()
            } else {
                accountingSettingsPanel.copyTradeSettings(null)
            }
        } else {
            accountingSettingsPanel.copyTradeSettings(null)
        }

    }

    void updateCopyMealSelected() {
        boolean selected = copyMealOrders.selected
        if(selected){
            selectCopyVat(true)
            accountingSettingsPanel.setMealsSelected(true)
            if(copyFrom){
                // TODO: need separate check box to copy Meals?
                copyMeals()
                accountingSettingsPanel.copyMealSettings(copyFrom)
            } else {
                accountingSettingsPanel.copyMealSettings(null)
            }
        } else {
            accountingSettingsPanel.copyMealSettings(null)
        }
    }

    // SELECT

    void selectCopyAccounts(boolean selected){
        boolean enabled = copyFrom != null
        copyAccounts.enabled = enabled
        copyAccounts.setSelected(enabled && selected)
        updateCopyAccountsSelected()
    }

    void selectCopyJournals(boolean selected){
        boolean enabled = copyFrom != null
        copyJournals.enabled = enabled
        copyJournals.setSelected(enabled && selected)
        updateCopyJournalsSelected()
    }

    void selectCopyContacts(boolean selected){
        boolean enabled = enableCopyContacts()
        copyContacts.setSelected(enabled && selected)
        updateCopyContactsSelected()
    }

    void selectCopyVat(boolean selected) {
        boolean enabled = enableCopyVat()
        copyVat.setSelected(enabled && selected)
        updateCopyVATSelected()
    }

    void selectCopyTrade(boolean selected) {
        boolean enabled = enableCopyTrade()
        copyTrade.setSelected(enabled && selected)
        updateCopyTradeSelected()
    }

    void selectCopyMeals(boolean selected) {
        boolean enabled = enableCopyMeals()
        copyMealOrders.setSelected(enabled && selected)
        updateCopyMealSelected()
    }

    // ENABLE

    boolean enableCopyContacts() {
        boolean enabled = copyFrom && copyFrom.isContactsAccounting() && newAccounting.isContactsAccounting()
        copyContacts.enabled = enabled
        if(!enabled) copyContacts.selected = false
        enabled
    }

    boolean enableCopyVat() {
        boolean enabled = copyFrom && copyFrom.vatAccounting && newAccounting.vatAccounting
        copyVat.enabled = enabled
        if(!enabled) copyVat.selected = false
        enabled
    }

    boolean enableCopyTrade() {
        boolean enabled = copyFrom && copyFrom.tradeAccounting && newAccounting.tradeAccounting
        copyTrade.enabled = enabled
        if(!enabled) copyTrade.selected = false
        enabled
    }

    boolean enableCopyMeals() {
        boolean enabled = copyFrom && copyFrom.mealsAccounting && newAccounting.mealsAccounting
        copyMealOrders.enabled = enabled
        if(!enabled) copyMealOrders.selected = false
        enabled
    }

}
