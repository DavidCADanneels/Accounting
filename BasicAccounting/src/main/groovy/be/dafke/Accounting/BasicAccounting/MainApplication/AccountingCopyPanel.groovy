package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.Meals
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.PurchaseOrders
import be.dafke.Accounting.BusinessModel.StockTransactions
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JPanel

class AccountingCopyPanel extends JPanel {

    private final JCheckBox copyAccounts, copyJournals, copyContacts, copyVat, copyTrade, copyMealOrders
    private AccountingSettingsPanel accountingSettingsPanel
    private Accounting copyFrom
    private Accounting newAccounting

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

        copyAccounts.setEnabled(false)
        copyJournals.setEnabled(false)
        copyContacts.setEnabled(false)
        copyVat.setEnabled(false)
        copyTrade.setEnabled(false)
        copyMealOrders.setEnabled(false)

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
        selectCopyAccounts(copyAccounts.isSelected())
        selectCopyJournals(copyJournals.isSelected())
        selectCopyContacts(copyContacts.isSelected())
        selectCopyVat(copyVat.isSelected())
        selectCopyTrade(copyTrade.isSelected())
        selectCopyMeals(copyMealOrders.isSelected())
    }

    // CREATE

    private Accounting createAccounting(){
        Accounting accounting = new Accounting("New Accounting")
        accounting.getAccountTypes().addDefaultTypes()
        accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes())
        accounting.getBalances().addDefaultBalances()
        accounting
    }

    // COPY

    void copyAccounts(){
        newAccounting.copyAccounts(copyFrom.getAccounts())
    }

    void copyJournals(){
        newAccounting.copyJournals(copyFrom.getJournals())
        newAccounting.copyJournalTypes(copyFrom.getJournalTypes())
    }

    void copyContacts(){
        newAccounting.copyContacts(copyFrom.getContacts())
    }

    void copyVatSettings(){
        newAccounting.copyVatSettings(copyFrom.getVatTransactions())
    }

    private void copyArticles() {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
        purchaseOrder.setName("PO0")

        Articles articlesFrom = copyFrom.getArticles()
        Articles articlesTo = newAccounting.getArticles()
        articlesTo.clear()
        articlesFrom.getBusinessObjects().forEach({ article ->
            Article newArticle = new Article(article, newAccounting.getContacts())
            Integer numberOfItems = article.getNrInStock()
            if (numberOfItems > 0) {
                OrderItem orderItem = new OrderItem(numberOfItems, newArticle, purchaseOrder)
                purchaseOrder.addBusinessObject(orderItem)
                // TODO: call setPurchaseTransaction (= beginBalans) 'later' iso setPoOrdered
                // setPurchaseTransaction calls setPoOrdered as well
                newArticle.setPoOrdered(numberOfItems)
            }
            try {
                articlesTo.addBusinessObject(newArticle)
            } catch (EmptyNameException e) {
                e.printStackTrace()
            } catch (DuplicateNameException e) {
                e.printStackTrace()
            }
        })
        if (!purchaseOrder.getBusinessObjects().isEmpty()) {
            // TODO: 'later' = here !
//            purchaseOrder.setPurchaseTransaction(beginBalans)
            try {
                PurchaseOrders purchaseOrders = newAccounting.getPurchaseOrders()
                purchaseOrders.addBusinessObject(purchaseOrder)
            } catch (EmptyNameException e) {
                e.printStackTrace()
            } catch (DuplicateNameException e) {
                e.printStackTrace()
            }
            StockTransactions stockTransactions = newAccounting.getStockTransactions()
            stockTransactions.addOrder(purchaseOrder)
        }
    }

    private void copyMeals() {
        Meals mealsFrom = copyFrom.getMeals()
        Meals mealsTo = newAccounting.getMeals()
        mealsTo.clear()
        mealsFrom.getBusinessObjects().forEach({ meal ->
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

    private void updateCopyAccountsSelected() {
        boolean copyAccountsSelected = copyAccounts.isSelected()
        if(copyAccountsSelected){
            copyAccounts()
        } else {
            selectCopyContacts(false)
        }
    }

    private void updateCopyJournalsSelected() {
        boolean copyJournalsSelected = copyJournals.isSelected()
        if(copyJournalsSelected){
            copyJournals()
        } else {

        }
    }

    private void updateCopyContactsSelected() {
        boolean enabled = copyFrom.isContactsAccounting()
        boolean selected = enabled && copyContacts.isSelected()
        if(selected){
            selectCopyAccounts(true)
            accountingSettingsPanel.setContactsSelected(true)
            copyContacts()
            if(copyFrom!=null){
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

    private void updateCopyVATSelected() {
        boolean selected = copyVat.isSelected()
        if(selected){
            selectCopyAccounts(true)
            selectCopyJournals(true)
            selectCopyContacts(true)
            accountingSettingsPanel.setVatSelected(true)
            copyVatSettings()
            if(copyFrom!=null){
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

    private void updateCopyTradeSelected() {
        boolean selected = copyTrade.isSelected()
        if(selected){
            selectCopyVat(true)
            accountingSettingsPanel.setTradeSelected(true)
            if(copyFrom!=null){
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

    private void updateCopyMealSelected() {
        boolean selected = copyMealOrders.isSelected()
        if(selected){
            selectCopyVat(true)
            accountingSettingsPanel.setMealsSelected(true)
            if(copyFrom!=null){
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
        copyAccounts.setEnabled(enabled)
        copyAccounts.setSelected(enabled && selected)
        updateCopyAccountsSelected()
    }

    void selectCopyJournals(boolean selected){
        boolean enabled = copyFrom != null
        copyJournals.setEnabled(enabled)
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
        boolean enabled = copyFrom!=null && copyFrom.isContactsAccounting() && newAccounting.isContactsAccounting()
        copyContacts.setEnabled(enabled)
        if(!enabled) copyContacts.setSelected(false)
        enabled
    }

    boolean enableCopyVat() {
        boolean enabled = copyFrom!=null && copyFrom.isVatAccounting() && newAccounting.isVatAccounting()
        copyVat.setEnabled(enabled)
        if(!enabled) copyVat.setSelected(false)
        enabled
    }

    boolean enableCopyTrade() {
        boolean enabled = copyFrom!=null && copyFrom.isTradeAccounting() && newAccounting.isTradeAccounting()
        copyTrade.setEnabled(enabled)
        if(!enabled) copyTrade.setSelected(false)
        enabled
    }

    boolean enableCopyMeals() {
        boolean enabled = copyFrom!=null && copyFrom.isMealsAccounting() && newAccounting.isMealsAccounting()
        copyMealOrders.setEnabled(enabled)
        if(!enabled) copyMealOrders.setSelected(false)
        enabled
    }

}
