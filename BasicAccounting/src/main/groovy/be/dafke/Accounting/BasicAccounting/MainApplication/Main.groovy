package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountsMenu
import be.dafke.Accounting.BasicAccounting.Accounts.Selector.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.Balances.BalanceGUI
import be.dafke.Accounting.BasicAccounting.Balances.BalancesMenu
import be.dafke.Accounting.BasicAccounting.Balances.TestBalanceGUI
import be.dafke.Accounting.BasicAccounting.Coda.CodaMenu
import be.dafke.Accounting.BasicAccounting.Contacts.ContactSelectorDialog
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsGUI
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsMenu
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.JournalsMenu
import be.dafke.Accounting.BasicAccounting.Journals.Management.JournalManagementGUI
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalDetailsGUI
import be.dafke.Accounting.BasicAccounting.Meals.*
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgageGUI
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesMenu
import be.dafke.Accounting.BasicAccounting.Projects.ProjectsMenu
import be.dafke.Accounting.BasicAccounting.Trade.ArticlesGUI
import be.dafke.Accounting.BasicAccounting.Trade.PurchaseOrderCreateGUI
import be.dafke.Accounting.BasicAccounting.Trade.SalesOrderCreateGUI
import be.dafke.Accounting.BasicAccounting.Trade.TradeMenu
import be.dafke.Accounting.BasicAccounting.VAT.VATFieldsGUI
import be.dafke.Accounting.BasicAccounting.VAT.VATMenu
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Accounting.BusinessModelDao.XMLReader
import be.dafke.Accounting.BusinessModelDao.XMLWriter

import javax.swing.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import static javax.swing.JSplitPane.*

class Main {
    static final ArrayList<JFrame> disposableComponents = new ArrayList<>()

    protected static Accountings accountings
    static JournalSelectorPanel journalSelectorPanel
    static JournalEditPanel journalEditPanel
    static OldLayoutPanel oldLayoutPanel
    static NewLayoutPanel newLayoutPanel
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

    static JournalSwitchViewPanel journalSwitchViewPanel

    static void main(String[] args) {
        readXmlData()
        journalEditPanel = new JournalEditPanel()
        journalSelectorPanel = new JournalSelectorPanel(journalEditPanel)
        journalSwitchViewPanel = new JournalSwitchViewPanel()
        oldLayoutPanel = new OldLayoutPanel(journalEditPanel, journalSelectorPanel, journalSwitchViewPanel)
//        newLayoutPanel = new NewLayoutPanel(journalEditPanel, journalSelectorPanel, journalSwitchViewPanel)
        frame = new AccountingGUIFrame("Accounting-all")
        frame.setContentPane(oldLayoutPanel)
//        frame.setContentPane(newLayoutPanel)
        createMenu()
        frame.setJMenuBar(menuBar)

        setCloseOperation()

        setAccounting(Session.activeAccounting)

        launchFrame()
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

    static JSplitPane createSplitPane(JComponent panel1, JComponent panel2, int orientation) {
        JSplitPane splitPane = new JSplitPane(orientation)
        if (orientation == VERTICAL_SPLIT) {
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
        if(activeAccounting) {
            XMLWriter.writeAccounting(activeAccounting, false)
        }

        if (readDetails) XMLReader.readAccountingDetails(accounting)
        Session.activeAccounting = accounting // only need to write to XML, call this only when writing XML files?

        frame.accounting = accounting

        journalSwitchViewPanel.accounting = accounting
        oldLayoutPanel.accounting = accounting
//        newLayoutPanel.accounting = accounting
        journalEditPanel.accounting = accounting
        journalSelectorPanel.accounting = accounting

        setMenuAccounting(accounting)
        if (accounting != null) {
            AccountingSession accountingSession = Session.getAccountingSession(Session.activeAccounting)
            setJournal(accountingSession?accountingSession.activeJournal:null)
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
            vatMenu.visible = accounting.vatAccounting
            morgagesMenu.visible = accounting.mortgagesAccounting
            tradeMenu.visible = accounting.tradeAccounting
            contactsMenu.visible = accounting.contactsAccounting
            projectsMenu.visible  = accounting.projectsAccounting
            mealsMenu.visible = accounting.mealsAccounting
        }
    }

    static void fireShowInputChanged(boolean enabled) {
        oldLayoutPanel.fireShowInputChanged(enabled)
//        newLayoutPanel.fireShowInputChanged(enabled)
        journalEditPanel.visible = enabled
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
            if(accountingSession) accountingSession.activeJournal = journal  // idem, only needed for XMLWriter
        }
        journalSelectorPanel.journal = journal
        journalEditPanel.journal = journal
        frame.journal = journal
        oldLayoutPanel.setJournal(journal)
        journalSwitchViewPanel.journal = journal
//        journalEditPanel.setJournalSession(journalSession)
//        journalSelectorPanel.setJournalSession(journalSession)
    }

    static void fireGlobalShowNumbersChanged(boolean enabled){
        oldLayoutPanel.fireGlobalShowNumbersChanged(enabled)
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
        journalSwitchViewPanel.fireJournalDataChanged()
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
        journalSwitchViewPanel.fireJournalDataChanged()
        oldLayoutPanel.fireAccountDataChanged()
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
        oldLayoutPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    static void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        oldLayoutPanel.setAccountsTypesRight(journalType, accountTypes)
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
        journalSwitchViewPanel.selectTransaction(transaction)
    }

    static void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        oldLayoutPanel.fireJournalTypeChanges(journal, journalType)
    }

    static void fireMortgageAddedOrRemoved(Mortgages mortgages) {
        MortgageGUI.refreshAllFrames()
        oldLayoutPanel.setMortgages(mortgages)
//        newLayoutPanel.setMortgages(mortgages)
    }

    static void fireMortgageEditedPayButton(Mortgage mortgage) {
        oldLayoutPanel.enableMortgagePayButton(mortgage)
//        newLayoutPanel.enableMortgagePayButton(mortgage)
    }

    static void fireMortgageEdited(Mortgage mortgage) {
        MortgageGUI.selectMortgage(mortgage)
    }
}