package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountsMenu
import be.dafke.Accounting.BasicAccounting.Accounts.Selector.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.Balances.BalanceGUI
import be.dafke.Accounting.BasicAccounting.Balances.BalancesMenu
import be.dafke.Accounting.BasicAccounting.Balances.TestBalanceGUI
import be.dafke.Accounting.BasicAccounting.Coda.CodaMenu
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsDataModel
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsPanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.JournalActions
import be.dafke.Accounting.BasicAccounting.Journals.JournalSwitchPanel
import be.dafke.Accounting.BasicAccounting.Journals.JournalsMenu
import be.dafke.Accounting.BasicAccounting.Journals.Management.JournalManagementGUI
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalDetailsGUI
import be.dafke.Accounting.BasicAccounting.Meals.*
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgageGUI
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesMenu
import be.dafke.Accounting.BasicAccounting.PDFGeneration.PDFViewerPanel
import be.dafke.Accounting.BasicAccounting.Projects.ProjectsMenu
import be.dafke.Accounting.BasicAccounting.Trade.*
import be.dafke.Accounting.BasicAccounting.VAT.VATMenu
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Accounting.BusinessModelDao.XMLReader
import be.dafke.Accounting.BusinessModelDao.XMLWriter

import javax.swing.*
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import static javax.swing.JSplitPane.*

class Main {
    static final ArrayList<JFrame> disposableComponents = new ArrayList<>()

    protected static Accountings accountings
    static JournalEditPanel journalEditPanel
    static JournalSwitchPanel journalSwitchPanel
    static JMenuBar menuBar
    static AccountingMenu accountingMenu
    static AccountingGUIFrame frame

    static AccountsMenu accountsMenu
    static JournalsMenu journalsMenu
    static BalancesMenu balancesMenu
    static MortgagesMenu morgagesMenu
    static TradeMenu tradeMenu
    static MealsMenu mealsMenu
    static ProjectsMenu projectsMenu
    static CodaMenu codaMenu
    static VATMenu vatMenu
    static CardLayout cardLayoutCenter, cardLayoutTop
    static JPanel center, topRightPanel
    static MainViewSelectorPanel mainViewSelectorPanel
    static SalesOrdersOverviewPanel salesOrdersOverViewPanel
    static PurchaseOrdersOverviewPanel purchaseOrdersOverviewPanel
    static IngredientsSwitchViewPanel ingredientsSwitchViewPanel
    static AllergenesSwitchViewPanel allergenesSwitchViewPanel
    static MealViewSelectorPanel mealViewSelectorPanel
    static OrdersViewSelectorPanel ordersViewSelectorPanel
    static JournalSelectorPanel journalSelectorPanel
    static ContactsSelectorPanel contactsSelectorPanel
    static ContactsPanel contactsPanel

    static final String JOURNALS_CENTER_VIEW = 'Journals'
    static final String SO_CENTER_VIEW = 'SalesOrders'
    static final String PO_CENTER_VIEW = 'PurchaseOrders'
    static final String INGREDIENTS_CENTER_VIEW = 'Ingredients'
    static final String ALLERGENES_CENTER_VIEW = 'Allergenes'
    static final String CONTACTS_CENTER_VIEW = 'Contacts'

    static final String EMPTY_MENU_VIEW = 'empty'
    static final String ORDERS_MENU_VIEW = 'Orders'
    static final String MEALS_MENU_VIEW = 'Meals'
    static final String CONTACTS_MENU_VIEW = 'ContactSwitch'

    static void main(String[] args) {
        readXmlData()

        cardLayoutCenter = new CardLayout()
        cardLayoutTop = new CardLayout()
        center = new JPanel(cardLayoutCenter)
        topRightPanel = new JPanel(cardLayoutTop)

        journalEditPanel = new JournalEditPanel()
        journalSwitchPanel = new JournalSwitchPanel(journalEditPanel)
        salesOrdersOverViewPanel = new SalesOrdersOverviewPanel()
        purchaseOrdersOverviewPanel = new PurchaseOrdersOverviewPanel()
        ingredientsSwitchViewPanel = new IngredientsSwitchViewPanel()
        allergenesSwitchViewPanel = new AllergenesSwitchViewPanel()
        ContactsDataModel contactsDataModel = new ContactsDataModel()
        contactsPanel = new ContactsPanel(contactsDataModel)

        center.add journalSwitchPanel, JOURNALS_CENTER_VIEW
        center.add salesOrdersOverViewPanel, SO_CENTER_VIEW
        center.add purchaseOrdersOverviewPanel, PO_CENTER_VIEW
        center.add ingredientsSwitchViewPanel, INGREDIENTS_CENTER_VIEW
        center.add allergenesSwitchViewPanel, ALLERGENES_CENTER_VIEW
        center.add contactsPanel, CONTACTS_CENTER_VIEW

        mealViewSelectorPanel = new MealViewSelectorPanel()
        ordersViewSelectorPanel = new OrdersViewSelectorPanel()
        journalSelectorPanel = new JournalSelectorPanel()
        contactsSelectorPanel = new ContactsSelectorPanel(contactsDataModel)

        topRightPanel.add journalSelectorPanel, JOURNALS_CENTER_VIEW
        topRightPanel.add ordersViewSelectorPanel, ORDERS_MENU_VIEW
        topRightPanel.add mealViewSelectorPanel, MEALS_MENU_VIEW
        topRightPanel.add contactsSelectorPanel, CONTACTS_MENU_VIEW
        topRightPanel.add(new JPanel(), EMPTY_MENU_VIEW)

        JPanel top = new JPanel(new BorderLayout())

        mainViewSelectorPanel = new MainViewSelectorPanel()
        top.add mainViewSelectorPanel, BorderLayout.WEST
        top.add topRightPanel, BorderLayout.CENTER

        JPanel contentPanel = new JPanel(new BorderLayout())
        contentPanel.add top, BorderLayout.NORTH
        contentPanel.add center, BorderLayout.CENTER

        frame = new AccountingGUIFrame("Accounting-all")
        frame.setContentPane(contentPanel)
        createMenu()
        frame.setJMenuBar(menuBar)

        setCloseOperation()

        setAccounting(Session.activeAccounting)

        launchFrame()
    }

    static void switchSubView(String view) {
        cardLayoutTop.show(topRightPanel, view)
    }

    static void switchView(String view) {
        cardLayoutCenter.show(center, view)
        if(view == JOURNALS_CENTER_VIEW) {
            journalSwitchPanel.refresh()
        } else if(view == SO_CENTER_VIEW) {
            salesOrdersOverViewPanel.refresh()
        } else if(view == PO_CENTER_VIEW) {
            purchaseOrdersOverviewPanel.refresh()
        } else if(view == INGREDIENTS_CENTER_VIEW) {
            ingredientsSwitchViewPanel.refresh()
        } else if(view == ALLERGENES_CENTER_VIEW) {
            allergenesSwitchViewPanel.refresh()
        } else if(view == CONTACTS_CENTER_VIEW) {
            contactsPanel.refresh()
        }
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
        if(activeAccounting!=accounting) {
            XMLWriter.writeAccounting(activeAccounting, false)
        }

        if (readDetails) XMLReader.readAccountingDetails(accounting)
        Session.activeAccounting = accounting // only need to write to XML, call this only when writing XML files?

        frame.refresh()
        journalEditPanel.refresh()
        journalSwitchPanel.refresh()
        journalSelectorPanel.refresh()
        salesOrdersOverViewPanel.refresh()
        purchaseOrdersOverviewPanel.refresh()
        ingredientsSwitchViewPanel.refresh()
        allergenesSwitchViewPanel.refresh()
        contactsPanel.refresh()
        mainViewSelectorPanel.enableButtons()

        setMenuAccounting(accounting)
        if (accounting != null) {
            AccountingSession accountingSession = Session.getAccountingSession(Session.activeAccounting)
            setJournal(accountingSession?accountingSession.activeJournal:null)
        }

    }

    static void setMenuAccounting(Accounting accounting) {
        projectsMenu.refresh()
        morgagesMenu.refresh()
        tradeMenu.accounting = accounting
        mealsMenu.accounting = accounting
        codaMenu.accounting = accounting
        accountsMenu.refresh()
        journalsMenu.refresh()
        balancesMenu.refresh()
        accountingMenu.accounting = accounting
        vatMenu.refresh()

        if (accounting != null) {
            vatMenu.visible = accounting.vatAccounting
            morgagesMenu.visible = accounting.mortgagesAccounting
            tradeMenu.visible = accounting.tradeAccounting
            projectsMenu.visible  = accounting.projectsAccounting
            mealsMenu.visible = accounting.mealsAccounting
        }
    }

    static void fireShowInputChanged(boolean enabled) {
        journalEditPanel.visible = enabled
        journalSwitchPanel.fireShowInputChanged(enabled)
    }

    static void fireBalancesChanged(){
        balancesMenu.fireBalancesChanged()
    }

    static void fireVATFieldsUpdated(/*VATFields vatFields*/){
//        VATFieldsGUI.fireVATFieldsUpdated(/*vatFields*/)
    }

    static Journal getCurrentJournal(){
        journalEditPanel.journal
    }

    static Transaction getCurrentTransaction(){
        journalEditPanel.transaction
    }

    static void saveCurrentTransaction(){
        currentJournal.currentTransaction = currentTransaction
    }

    static Journal switchJournal(Journal newJournal){
        if (!currentTransaction.businessObjects.empty && currentJournal != newJournal) {
            newJournal = askInputMove(newJournal)
        }
        if (currentJournal != newJournal) {
            journal = newJournal
        }
        newJournal
    }

    static Journal askInputMove(Journal newJournal) {
//        Transaction transaction = Main.getCurrentTransaction()
        Transaction newTransaction = newJournal.currentTransaction
        String text = """\
Do you want to transfer the current transaction from ${currentJournal} to ${newJournal}?
${newTransaction && !newTransaction.businessObjects.isEmpty()?"""\
WARNING: ${newJournal} also has an open transactions, which will be lost if you select transfer
""":''}\
"""
        int answer = JOptionPane.showConfirmDialog(null, text)
        if (answer == JOptionPane.YES_OPTION) {
            moveTransactionToNewJournal(newJournal)
            newJournal
        } else if (answer == JOptionPane.NO_OPTION) {
            saveCurrentTransaction()
            newJournal
        } else {
            currentJournal
        }

    }

    static void moveTransactionToNewJournal(Journal newJournal){
        newJournal.currentTransaction = currentTransaction
        currentJournal.currentTransaction = new Transaction(Calendar.getInstance(), "")
    }

    static void setJournal(Journal journal) {
        if(journal) {
            Accounting accounting = journal.accounting
            AccountingSession accountingSession = Session.getAccountingSession(accounting)
            if(accountingSession) accountingSession.activeJournal = journal  // idem, only needed for XMLWriter
        }
        frame.journal = journal
        journalEditPanel.journal = journal
        journalSwitchPanel.setJournal(journal)
        journalSelectorPanel.setJournal(journal)
//        journalEditPanel.setJournalSession(journalSession)
//        journalSelectorPanel.setJournalSession(journalSession)
    }

    static void fireGlobalShowNumbersChanged(boolean enabled){
        journalSwitchPanel.fireGlobalShowNumbersChanged(enabled)
    }

    static void fireTransactionInputDataChanged(){
        journalEditPanel.fireTransactionDataChanged()
//        journalSwitchPanel.refresh()
    }

    static void fireTransactionBooked(){
        journalSwitchPanel.refresh()
    }

    static void editTransaction(Transaction transaction){
        saveCurrentTransaction()
        Journal journal = transaction?.journal
        Transaction currentTransaction = journal?.currentTransaction
        if (!currentTransaction.businessObjects.empty) {
            Accounting accounting = journal.accounting
            String text = """\
${transaction.journal} has an open transaction, which will be lost if click Y
"""
            int answer = JOptionPane.showConfirmDialog(null, text, "Continue ?", JOptionPane.OK_CANCEL_OPTION)
            if (answer == JOptionPane.OK_OPTION) {
                JournalActions.deleteTransaction(transaction)
                //TODO: GUI with question where to open the transaction? (only usefull if multiple input GUIs are open)
                // set Journal before Transaction: setJournal sets transaction to currentObject !!!

                setAccounting(accounting)
                setJournal(journal)
                Main.journal = journal
                journal.currentTransaction = transaction
                // TODO: when calling setTransaction we need to check if the currentTransaction is empty (see switchJournal() -> checkTransfer)
                journalEditPanel.setTransaction(transaction)
            }
        }
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
        journalSwitchPanel.fireJournalDataChanged()
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
        journalSwitchPanel.fireJournalDataChanged()
        journalSwitchPanel.fireAccountDataChanged()
        AccountManagementGUI.fireAccountDataChangedForAll()
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        TestBalanceGUI.fireAccountDataChangedForAll()
        BalanceGUI.fireAccountDataChangedForAll()
    }

    static void fireMealCountUpdated(Accounting accounting) {
        MealRecipeViewGUI.fireTableUpdateForAccounting(accounting)
        MealRecipeEditGUI.fireTableUpdateForAccounting(accounting)
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
        journalSwitchPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    static void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        journalSwitchPanel.setAccountsTypesRight(journalType, accountTypes)
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
        journalSwitchPanel.selectTransaction(transaction)
    }

    static void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        journalSwitchPanel.fireJournalTypeChanges(journal, journalType)
    }

//    static void fireMortgageAddedOrRemoved(Mortgages mortgages) {
//        MortgageGUI.refreshAllFrames()
////        journalSwitchPanel.setMortgages(mortgages)
//    }

    static void fireMortgageEditedPayButton(Mortgage mortgage) {
        journalSwitchPanel.enableMortgagePayButton(mortgage)
    }

    static void fireMortgageEdited(Mortgage mortgage) {
        MortgageGUI.selectMortgage(mortgage)
    }
}