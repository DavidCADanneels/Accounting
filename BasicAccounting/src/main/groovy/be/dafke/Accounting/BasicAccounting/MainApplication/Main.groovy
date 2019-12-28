package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Accounts.Selector.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.Accounts.AccountsMenu
import be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable.AccountsTablePanel
import be.dafke.Accounting.BasicAccounting.Balances.BalanceGUI
import be.dafke.Accounting.BasicAccounting.Balances.BalancesMenu
import be.dafke.Accounting.BasicAccounting.Balances.TestBalanceGUI
import be.dafke.Accounting.BasicAccounting.Coda.CodaMenu
import be.dafke.Accounting.BasicAccounting.Contacts.ContactSelectorDialog
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsGUI
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsMenu
import be.dafke.Accounting.BasicAccounting.Journals.*
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Management.JournalManagementGUI
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.DualView.TransactionOverviewPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalDetailsGUI
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalViewPanel
import be.dafke.Accounting.BasicAccounting.Meals.*
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgageGUI
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesMenu
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BasicAccounting.Projects.ProjectsMenu
import be.dafke.Accounting.BasicAccounting.Trade.ArticlesGUI
import be.dafke.Accounting.BasicAccounting.Trade.PurchaseOrderCreateGUI
import be.dafke.Accounting.BasicAccounting.Trade.SalesOrderCreateGUI
import be.dafke.Accounting.BasicAccounting.Trade.TradeMenu
import be.dafke.Accounting.BasicAccounting.VAT.VATFieldsGUI
import be.dafke.Accounting.BasicAccounting.VAT.VATMenu
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.*

import javax.swing.*
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import static javax.swing.JSplitPane.*

class Main {
    static final ArrayList<JFrame> disposableComponents = new ArrayList<>()

    protected static Accountings accountings
    static JournalViewPanel journalViewPanel
    static JournalSelectorPanel journalSelectorPanel
    static JournalEditPanel journalEditPanel
    static AccountsTablePanel accountGuiLeft
    static AccountsTablePanel accountGuiRight
    static MortgagesPanel mortgagesPanel
    static JMenuBar menuBar
    static AccountingMenu accountingMenu
    static AccountingGUIFrame frame

    static AccountsMenu accountsMenu
    static JournalsMenu journalsMenu
    static BalancesMenu balancesMenu
    static MortgagesMenu morgagesMenu
    static ContactsMenu contactsMenu
    static TradeMenu tradeMenu
    static MealsMenu mealsMenu
    static ProjectsMenu projectsMenu
    static CodaMenu codaMenu
    static VATMenu vatMenu
    static TransactionOverviewPanel transactionOverviewPanel
    static JPanel cardPanel
    static CardLayout cardLayout
    static JSplitPane journalViewAndEditSplitPane

    static void main(String[] args) {
        readXmlData()
        createComponents()
        frame = new AccountingGUIFrame("Accounting-all")
        frame.setContentPane(createContentPanel())
        createMenu()
        frame.setJMenuBar(menuBar)

        setCloseOperation()

        setAccounting(Session.activeAccounting)

        launchFrame()
    }

    static void createComponents() {
        journalEditPanel = new JournalEditPanel()
        journalViewPanel = new JournalViewPanel()
        transactionOverviewPanel = new TransactionOverviewPanel()
        journalSelectorPanel = new JournalSelectorPanel(journalEditPanel)
        accountGuiLeft = new AccountsTablePanel(true)
        accountGuiRight = new AccountsTablePanel(false)
        mortgagesPanel = new MortgagesPanel(journalEditPanel)
    }

    static void setCloseOperation() {
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            void windowClosing(WindowEvent e) {
                PopupForTableActivator.closeAllPopups()
                Main.closeAllFrames()
                Main.saveData(false)
            }
        })
    }

    static JPanel createContentPanel() {
        JPanel links = new JPanel()
        links.setLayout(new BorderLayout())
        links.add(accountGuiLeft, BorderLayout.CENTER)
        links.add(mortgagesPanel, BorderLayout.SOUTH)

        JPanel accountingMultiPanel = new JPanel()
        accountingMultiPanel.setLayout(new BorderLayout())
        cardLayout = new CardLayout()
        cardPanel = new JPanel(cardLayout)
        cardPanel.add(journalViewPanel, JournalSelectorPanel.VIEW1)
        cardPanel.add(transactionOverviewPanel, JournalSelectorPanel.VIEW2)
        journalViewAndEditSplitPane = createSplitPane(cardPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel centerPanel = new JPanel(new BorderLayout())
        centerPanel.add(journalViewAndEditSplitPane, BorderLayout.CENTER)
        centerPanel.add(journalSelectorPanel, BorderLayout.NORTH)

        accountingMultiPanel.add(accountGuiRight, BorderLayout.EAST)
        accountingMultiPanel.add(centerPanel, BorderLayout.CENTER)
        accountingMultiPanel.add(links, BorderLayout.WEST)
        accountingMultiPanel
    }

    static JSplitPane createSplitPane(JComponent panel1, JComponent panel2, int orientation) {
        JSplitPane splitPane = new JSplitPane(orientation)
        if (orientation == JSplitPane.VERTICAL_SPLIT) {
            splitPane.add(panel1, TOP)
            splitPane.add(panel2, BOTTOM)
        } else {
            splitPane.add(panel1, LEFT)
            splitPane.add(panel2, RIGHT)
        }
        splitPane
    }

    static void createMenu() {
        menuBar = new JMenuBar()

        accountingMenu = new AccountingMenu(accountings)
        accountsMenu = new AccountsMenu()
        journalsMenu = new JournalsMenu()
        balancesMenu = new BalancesMenu()
        contactsMenu = new ContactsMenu()
        tradeMenu = new TradeMenu()
        mealsMenu = new MealsMenu()
        morgagesMenu = new MortgagesMenu()
        projectsMenu = new ProjectsMenu()
        codaMenu = new CodaMenu()
        vatMenu = new VATMenu()

        menuBar.add(accountingMenu)
        menuBar.add(journalsMenu)
        menuBar.add(accountsMenu)
        menuBar.add(balancesMenu)
        menuBar.add(projectsMenu)
        menuBar.add(morgagesMenu)
        menuBar.add(contactsMenu)
        menuBar.add(vatMenu)
        menuBar.add(tradeMenu)
        menuBar.add(mealsMenu)
        menuBar.add(codaMenu)
    }

    static void launchFrame() {
        Main.addFrame(frame) // MAIN
        frame.pack()
        frame.visible = true
    }

    static void readXmlData() {
        accountings = new Accountings()
        XMLReader.readAccountings(accountings)
        for (Accounting accounting : accountings.businessObjects) {
            XMLReader.readAccountingSkeleton(accounting)
        }

        Session.accountings = accountings
        XMLReader.readSession()

        Accounting accounting = Session.activeAccounting
        if (accounting != null) {
            XMLReader.readAccountingDetails(accounting)
            Session.activeAccounting = accounting
            accounting.read = true
        }
    }

    static void setAccounting(Accounting accounting) {
        setAccounting(accounting, true)
    }

    static void setAccounting(Accounting accounting, boolean readDetails) {

        Accounting activeAccounting = Session.activeAccounting
        XMLWriter.writeAccounting(activeAccounting, false)

        if (readDetails) XMLReader.readAccountingDetails(accounting)
        Session.activeAccounting = accounting // only need to write to XML, call this only when writing XML files?

        frame.accounting = accounting

        accountGuiLeft.setAccounting(accounting, true)
        accountGuiRight.setAccounting(accounting, false)
        journalEditPanel.accounting = accounting
        journalViewPanel.accounting = accounting
        transactionOverviewPanel.accounting = accounting
        journalSelectorPanel.accounting = accounting
        mortgagesPanel.setMortgages(accounting == null ? null : accounting.mortgages)

        setMenuAccounting(accounting)
        if (accounting != null) {
            AccountingSession accountingSession = Session.getAccountingSession(Session.activeAccounting)
            setJournal(accountingSession.activeJournal)
        }

    }

    static void setMenuAccounting(Accounting accounting) {
        projectsMenu.accounting = accounting
        morgagesMenu.accounting = accounting
        tradeMenu.accounting = accounting
        mealsMenu.accounting = accounting
        codaMenu.accounting = accounting
        contactsMenu.accounting = accounting
        accountsMenu.accounting = accounting
        journalsMenu.accounting = accounting
        balancesMenu.accounting = accounting
        accountingMenu.accounting = accounting
        vatMenu.accounting = accounting

        if (accounting != null) {
            vatMenu.setVisible(accounting.vatAccounting)
            morgagesMenu.setVisible(accounting.mortgagesAccounting)
            tradeMenu.setVisible(accounting.tradeAccounting)
            contactsMenu.setVisible(accounting.contactsAccounting)
            projectsMenu.setVisible(accounting.projectsAccounting)
            mealsMenu.setVisible(accounting.mealsAccounting)
            mortgagesPanel.setVisible(accounting.mortgagesAccounting)

        }
    }

    static void fireShowInputChanged(boolean enabled) {
        journalEditPanel.setVisible(enabled)
    }

    static void fireMultiTransactionChanged(boolean enabled) {
        transactionOverviewPanel.setMultiSelection(enabled)
    }

    static void fireBalancesChanged(){
        balancesMenu.fireBalancesChanged()
    }

    static void fireVATFieldsUpdated(/*VATFields vatFields*/){
        VATFieldsGUI.fireVATFieldsUpdated(/*vatFields*/)
    }

    static void setJournal(Journal journal) {
        if(journal) {
            Accounting accounting = journal.accounting
            AccountingSession accountingSession = Session.getAccountingSession(accounting)
            accountingSession.activeJournal = journal  // idem, only needed for XMLWriter
        }
        journalSelectorPanel.journal = journal
        journalViewPanel.journal = journal
        transactionOverviewPanel.journal = journal
        journalEditPanel.journal = journal
        frame.journal = journal
        Accounting activeAccounting = Session.activeAccounting
        AccountingSession accountingSession = Session.getAccountingSession(activeAccounting)
        Journal activeJournal = accountingSession.activeJournal
        JournalSession journalSession = accountingSession.getJournalSession(activeJournal)
        accountGuiLeft.setJournalSession(journalSession)
        accountGuiRight.setJournalSession(journalSession)
        accountGuiLeft.setJournal(journal, true)
        accountGuiRight.setJournal(journal, false)
//        journalEditPanel.setJournalSession(journalSession)
//        journalSelectorPanel.setJournalSession(journalSession)
    }

    static void fireGlobalShowNumbersChanged(boolean enabled){
        accountGuiLeft.fireGlobalShowNumbersChanged(enabled)
        accountGuiRight.fireGlobalShowNumbersChanged(enabled)
    }

    static void fireTransactionInputDataChanged(){
        journalEditPanel.fireTransactionDataChanged()
    }

    static void editTransaction(Transaction transaction){
        journalEditPanel.editTransaction(transaction)
    }

    static void deleteBookings(ArrayList<Booking> bookings){
        journalEditPanel.deleteBookings(bookings)
    }

    static void deleteTransactions(Set<Transaction> transactions){
        journalEditPanel.deleteTransactions(transactions)
    }

    static void moveBookings(ArrayList<Booking> bookings, Journals journals){
        journalEditPanel.moveBookings(bookings, journals)
    }

    static void moveTransactions(Set<Transaction> bookings, Journals journals){
        journalEditPanel.moveTransaction(bookings, journals)
    }

    static Transaction getTransaction(){
        journalEditPanel.transaction
    }

    static void addBooking(Booking booking){
        journalEditPanel.addBooking(booking)
    }

    static void fireJournalDataChanged(Journal journal){
        JournalDetailsGUI.fireJournalDataChangedForAll(journal)
        JournalManagementGUI.fireJournalDataChangedForAll()
        journalViewPanel.fireJournalDataChanged()
        transactionOverviewPanel.fireJournalDataChanged()
        journalsMenu.fireJournalDataChanged()
        frame.fireDataChanged()
    }

    static void fireJournalAdded(Journals journals) {
        journalSelectorPanel.setJournals(journals)
    }

    static void fireAccountingTypeChanged(Accounting accounting){
        Accounting activeAccounting = Session.activeAccounting
        if(activeAccounting == accounting){
            setMenuAccounting(accounting)
        }
    }

    static void fireAccountDataChanged(Account account){
        AccountDetailsGUI.fireAccountDataChangedForAll(account)
        AccountSelectorDialog.fireAccountDataChangedForAll()
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        journalViewPanel.fireJournalDataChanged()
        transactionOverviewPanel.fireJournalDataChanged()
        accountGuiLeft.fireAccountDataChanged()
        accountGuiRight.fireAccountDataChanged()

        AccountManagementGUI.fireAccountDataChangedForAll()
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        TestBalanceGUI.fireAccountDataChangedForAll()
        BalanceGUI.fireAccountDataChangedForAll()
    }

    static void fireContactAdded(Accounting accounting, Contact contact) {
        ContactSelectorDialog.fireContactDataChangedForAll()
        ContactsGUI.fireTableUpdateForAccounting(accounting)

        if(contact.isCustomer()){
            SalesOrderCreateGUI.fireCustomerAddedOrRemovedForAll()
//         } else if(contact.isSupplier()){
            // TODO: implement below method
//        PromoOrderCreateGUI.fireCustomerAddedOrRemovedForAccounting()
        }
    }

    static void fireCustomerDataChanged() {
        ContactsGUI.fireCustomerDataChanged()
    }

    static void fireContactDataChanged() {
        // TODO: do we need to refresh the Contact Selector
        // What are Contact Selectors used for. Only Customers? ...
        ContactSelectorDialog.fireContactDataChangedForAll()
        ContactsGUI.fireContactDataChangedForAll()
    }

    static void fireCustomerAddedOrRemoved(Accounting accounting) {
        ContactsGUI.fireCustomerAddedOrRemovedForAccounting(accounting)
        SalesOrderCreateGUI.fireCustomerAddedOrRemovedForAll()
    }

    static void fireIngredientsAddedOrRemoved(Accounting accounting) {
        ArticlesGUI.fireIngredientAddedOrRemovedForAccounting(accounting)
        ArticlesGUI.fireTableUpdateForAccounting(accounting)
    }

    static void fireAllergeneAddedOrRemoved() {
    }

    static void fireRecipeDataUpdated(Accounting accounting) {
        MealRecipeViewGUI.fireTableUpdateForAccounting(accounting)
        MealRecipeEditGUI.fireTableUpdateForAccounting(accounting)
        MealIngredientsViewGUI.fireTableUpdateForAccounting(accounting)
        MealIngredientsEditGUI.fireTableUpdateForAccounting(accounting)
    }

    static void fireArticleAddedOrRemoved(Accounting accounting) {
        ArticlesGUI.fireTableUpdateForAccounting(accounting)
    }

    static void fireMealCountUpdated(Accounting accounting) {
        MealRecipeViewGUI.fireTableUpdateForAccounting(accounting)
        MealRecipeEditGUI.fireTableUpdateForAccounting(accounting)
    }

    static void fireSupplierAddedOrRemoved(Accounting accounting) {
        ContactsGUI.fireTableUpdateForAccounting(accounting)
        ArticlesGUI.fireSupplierAddedOrRemovedForAccounting(accounting)
        ArticlesGUI.fireTableUpdateForAccounting(accounting)
        PurchaseOrderCreateGUI.fireSupplierAddedOrRemovedForAll()
    }

    static void saveData(boolean writeHtml) {
        XMLWriter.writeAccountings(accountings, writeHtml)
    }


    static void closeAllFrames(){
        for(JFrame frame: disposableComponents){
            if (frame) frame.dispose()
        }
    }

    static void addFrame(JFrame frame) {
        disposableComponents.add(frame)
    }

    static void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if(journalType == accountGuiLeft.getJournalType())
            accountGuiLeft.setAccountTypesList(accountTypes)
    }
    static void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if(journalType == accountGuiRight.getJournalType())
            accountGuiRight.setAccountTypesList(accountTypes)
    }
//    static void setAccountsListLeft(JournalType journalType, AccountsList accountsList) {
//        if(journalType == accountGuiLeft.getJournalType())
//            accountGuiLeft.setAccountsList(accountsList)
//    }
//
//    static void setAccountsListRight(JournalType journalType, AccountsList accountsList) {
//        if(journalType == accountGuiRight.getJournalType())
//            accountGuiRight.setAccountsList(accountsList)
//    }

    static void selectTransaction(Transaction transaction){
        journalViewPanel.selectTransaction(transaction)
        transactionOverviewPanel.selectTransaction(transaction)
    }

    static void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        if(journal==accountGuiLeft.journal) {
            accountGuiLeft.setJournalType(journalType)
            accountGuiLeft.setAccountsList(journalType.getLeft())
        }
        if(journal==accountGuiRight.journal) {
            accountGuiRight.setJournalType(journalType)
            accountGuiRight.setAccountsList(journalType.getRight())
        }
    }

    static void fireMortgageAddedOrRemoved(Mortgages mortgages) {
        MortgageGUI.refreshAllFrames()
        mortgagesPanel.setMortgages(mortgages)
    }

    static void fireMortgageEditedPayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }

    static void fireMortgageEdited(Mortgage mortgage) {
        MortgageGUI.selectMortgage(mortgage)
    }

    static void switchView(String view) {
        cardLayout.show(cardPanel, view)
        journalEditPanel.fireTransactionDataChanged()
        transactionOverviewPanel.fireJournalDataChanged()
    }
}