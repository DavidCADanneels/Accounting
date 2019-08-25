package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Accounts.AccountsMenu;
import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountsTablePanel;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalanceGUI;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.Contacts.ContactsGUI;
import be.dafke.BasicAccounting.Contacts.ContactsMenu;
import be.dafke.BasicAccounting.Meals.*;
import be.dafke.BasicAccounting.Trade.*;
import be.dafke.BasicAccounting.Journals.*;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgageGUI;
import be.dafke.BasicAccounting.Mortgages.MortgagesPanel;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.VAT.VATFieldsGUI;
import be.dafke.BasicAccounting.VAT.VATMenu;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.XMLReader;
import be.dafke.BusinessModelDao.XMLWriter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Set;

import static javax.swing.JSplitPane.BOTTOM;
import static javax.swing.JSplitPane.LEFT;
import static javax.swing.JSplitPane.RIGHT;
import static javax.swing.JSplitPane.TOP;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

public class Main {
    private static final ArrayList<JFrame> disposableComponents = new ArrayList<>();

    protected static Accountings accountings;
    private static JournalViewPanel journalViewPanel;
    private static JournalSelectorPanel journalSelectorPanel;
    private static JournalEditPanel journalEditPanel;
    private static AccountsTablePanel accountGuiLeft;
    private static AccountsTablePanel accountGuiRight;
    private static MortgagesPanel mortgagesPanel;
    private static JMenuBar menuBar;
    private static AccountingMenu accountingMenu;
    private static AccountingGUIFrame frame;

    private static AccountsMenu accountsMenu;
    private static JournalsMenu journalsMenu;
    private static BalancesMenu balancesMenu;
    private static MorgagesMenu morgagesMenu;
    private static ContactsMenu contactsMenu;
    private static TradeMenu tradeMenu;
    private static MealsMenu mealsMenu;
    private static ProjectsMenu projectsMenu;
    private static CodaMenu codaMenu;
    private static VATMenu vatMenu;
    private static TransactionOverviewPanel transactionOverviewPanel;
    private static JPanel cardPanel;
    private static CardLayout cardLayout;
    private static JSplitPane journalViewAndEditSplitPane;

    public static void main(String[] args) {
        readXmlData();
        createComponents();
        frame = new AccountingGUIFrame("Accounting-all");
        frame.setContentPane(createContentPanel());
        createMenu();
        frame.setJMenuBar(menuBar);

        setCloseOperation();

        setAccounting(Accountings.getActiveAccounting());

        launchFrame();
    }

    private static void createComponents() {
        journalEditPanel = new JournalEditPanel();
        journalViewPanel = new JournalViewPanel();
        transactionOverviewPanel = new TransactionOverviewPanel();
        journalSelectorPanel = new JournalSelectorPanel(journalEditPanel);
        accountGuiLeft = new AccountsTablePanel(true);
        accountGuiRight = new AccountsTablePanel(false);
        mortgagesPanel = new MortgagesPanel(journalEditPanel);
    }

    private static void setCloseOperation() {
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PopupForTableActivator.closeAllPopups();
                Main.closeAllFrames();
                Main.saveData(false);
            }
        });
    }

    public static JPanel createContentPanel() {
        JPanel links = new JPanel();
        links.setLayout(new BorderLayout());
        links.add(accountGuiLeft, BorderLayout.CENTER);
        links.add(mortgagesPanel, BorderLayout.SOUTH);

        JPanel accountingMultiPanel = new JPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(journalViewPanel, JournalSelectorPanel.VIEW1);
        cardPanel.add(transactionOverviewPanel, JournalSelectorPanel.VIEW2);
        journalViewAndEditSplitPane = createSplitPane(cardPanel, journalEditPanel, VERTICAL_SPLIT);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(journalViewAndEditSplitPane, BorderLayout.CENTER);
        centerPanel.add(journalSelectorPanel, BorderLayout.NORTH);

        accountingMultiPanel.add(accountGuiRight, BorderLayout.EAST);
        accountingMultiPanel.add(centerPanel, BorderLayout.CENTER);
        accountingMultiPanel.add(links, BorderLayout.WEST);
        return accountingMultiPanel;
    }

    public static JSplitPane createSplitPane(JComponent panel1, JComponent panel2, int orientation) {
        JSplitPane splitPane = new JSplitPane(orientation);
        if (orientation == JSplitPane.VERTICAL_SPLIT) {
            splitPane.add(panel1, TOP);
            splitPane.add(panel2, BOTTOM);
        } else {
            splitPane.add(panel1, LEFT);
            splitPane.add(panel2, RIGHT);
        }
        return splitPane;
    }

    private static void createMenu() {
        menuBar = new JMenuBar();

        accountingMenu = new AccountingMenu(accountings);
        accountsMenu = new AccountsMenu();
        journalsMenu = new JournalsMenu();
        balancesMenu = new BalancesMenu();
        contactsMenu = new ContactsMenu();
        tradeMenu = new TradeMenu();
        mealsMenu = new MealsMenu();
        morgagesMenu = new MorgagesMenu();
        projectsMenu = new ProjectsMenu();
        codaMenu = new CodaMenu();
        vatMenu = new VATMenu();

        menuBar.add(accountingMenu);
        menuBar.add(journalsMenu);
        menuBar.add(accountsMenu);
        menuBar.add(balancesMenu);
        menuBar.add(projectsMenu);
        menuBar.add(morgagesMenu);
        menuBar.add(contactsMenu);
        menuBar.add(vatMenu);
        menuBar.add(tradeMenu);
        menuBar.add(mealsMenu);
        menuBar.add(codaMenu);
    }

    private static void launchFrame() {
        Main.addFrame(frame); // MAIN
        frame.pack();
        frame.setVisible(true);
    }

    private static void readXmlData() {
        accountings = new Accountings();

        XMLReader.readAccountings(accountings);
        for (Accounting accounting : accountings.getBusinessObjects()) {
            XMLReader.readAccountingSkeleton(accounting);
        }

        XMLReader.readSession(accountings);

        Accounting accounting = Accountings.getActiveAccounting();
        if (accounting != null) {
            XMLReader.readAccountingDetails(accounting);
            Accountings.setActiveAccounting(accounting);
            accounting.setRead(true);
        }
    }

    public static void setAccounting(Accounting accounting) {
        setAccounting(accounting, true);
    }

    public static void setAccounting(Accounting accounting, boolean readDetails) {

        Accounting activeAccounting = Accountings.getActiveAccounting();
        XMLWriter.writeAccounting(activeAccounting, false);

        if (readDetails) XMLReader.readAccountingDetails(accounting);
        Accountings.setActiveAccounting(accounting); // only need to write to XML, call this only when writing XML files?

        frame.setAccounting(accounting);

        accountGuiLeft.setAccounting(accounting);
        accountGuiRight.setAccounting(accounting);
        journalEditPanel.setAccounting(accounting);
        journalViewPanel.setAccounting(accounting);
        transactionOverviewPanel.setAccounting(accounting);
        journalSelectorPanel.setAccounting(accounting);
        mortgagesPanel.setMortgages(accounting == null ? null : accounting.getMortgages());

        setMenuAccounting(accounting);
        if (accounting != null) {
            setJournal(accounting.getActiveJournal());
        }

    }

    public static void setMenuAccounting(Accounting accounting) {
        projectsMenu.setAccounting(accounting);
        morgagesMenu.setAccounting(accounting);
        tradeMenu.setAccounting(accounting);
        mealsMenu.setAccounting(accounting);
        codaMenu.setAccounting(accounting);
        contactsMenu.setAccounting(accounting);
        accountsMenu.setAccounting(accounting);
        journalsMenu.setAccounting(accounting);
        balancesMenu.setAccounting(accounting);
        accountingMenu.setAccounting(accounting);
        vatMenu.setAccounting(accounting);

        if (accounting != null) {
            vatMenu.setVisible(accounting.isVatAccounting());
            morgagesMenu.setVisible(accounting.isMortgagesAccounting());
            tradeMenu.setVisible(accounting.isTradeAccounting());
            contactsMenu.setVisible(accounting.isContactsAccounting());
            projectsMenu.setVisible(accounting.isProjectsAccounting());
            mealsMenu.setVisible(accounting.isMealsAccounting());
            mortgagesPanel.setVisible(accounting.isMortgagesAccounting());

        }
    }

    public static void fireShowInputChanged(boolean enabled) {
        journalEditPanel.setVisible(enabled);
    }

    public static void fireMultiTransactionChanged(boolean enabled) {
        transactionOverviewPanel.setMultiSelection(enabled);
}

    public static void fireBalancesChanged(){
        balancesMenu.fireBalancesChanged();
    }

    public static void fireVATFieldsUpdated(/*VATFields vatFields*/){
        VATFieldsGUI.fireVATFieldsUpdated(/*vatFields*/);
    }

    public static void setJournal(Journal journal) {
        if(journal!=null) {
            Accounting accounting = journal.getAccounting();
            accounting.setActiveJournal(journal);  // idem, only needed for XMLWriter
        }
        journalSelectorPanel.setJournal(journal);
        journalViewPanel.setJournal(journal);
        transactionOverviewPanel.setJournal(journal);
        journalEditPanel.setJournal(journal);
        frame.setJournal(journal);
        accountGuiLeft.setJournal(journal, true);
        accountGuiRight.setJournal(journal, false);
    }

    public static void fireTransactionInputDataChanged(){
        journalEditPanel.fireTransactionDataChanged();
    }

    public static void editTransaction(Transaction transaction){
        journalEditPanel.editTransaction(transaction);
    }

    public static void deleteBookings(ArrayList<Booking> bookings){
        journalEditPanel.deleteBookings(bookings);
    }

    public static void deleteTransactions(Set<Transaction> transactions){
        journalEditPanel.deleteTransactions(transactions);
    }

    public static void moveBookings(ArrayList<Booking> bookings, Journals journals){
        journalEditPanel.moveBookings(bookings, journals);
    }

    public static void moveTransactions(Set<Transaction> bookings, Journals journals){
        journalEditPanel.moveTransaction(bookings, journals);
    }

    public static Transaction getTransaction(){
        return journalEditPanel.getTransaction();
    }

    public static void addBooking(Booking booking){
        journalEditPanel.addBooking(booking);
    }

    public static void fireJournalDataChanged(Journal journal){
        JournalDetailsGUI.fireJournalDataChangedForAll(journal);
        JournalManagementGUI.fireJournalDataChangedForAll();
        journalViewPanel.fireJournalDataChanged();
        transactionOverviewPanel.fireJournalDataChanged();
        journalsMenu.fireJournalDataChanged();
        frame.fireDataChanged();
    }

    public static void fireJournalAdded(Journals journals) {
        journalSelectorPanel.setJournals(journals);
    }

    public static void fireAccountingTypeChanged(Accounting accounting){
        Accounting activeAccounting = Accountings.getActiveAccounting();
        if(activeAccounting == accounting){
            setMenuAccounting(accounting);
        }
    }

    public static void fireAccountDataChanged(Account account){
        AccountDetailsGUI.fireAccountDataChangedForAll(account);
        AccountSelectorDialog.fireAccountDataChangedForAll();
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        journalViewPanel.fireJournalDataChanged();
        transactionOverviewPanel.fireJournalDataChanged();
        accountGuiLeft.fireAccountDataChanged();
        accountGuiRight.fireAccountDataChanged();

        AccountManagementGUI.fireAccountDataChangedForAll();
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        TestBalanceGUI.fireAccountDataChangedForAll();
        BalanceGUI.fireAccountDataChangedForAll();
    }

    public static void fireContactAdded(Accounting accounting, Contact contact) {
        ContactSelectorDialog.fireContactDataChangedForAll();
        ContactsGUI.fireTableUpdateForAccounting(accounting);

        if(contact.isCustomer()){
        SalesOrderCreateGUI.fireCustomerAddedOrRemovedForAll();
//         } else if(contact.isSupplier()){
             // TODO: implement below method
//        PromoOrderCreateGUI.fireCustomerAddedOrRemovedForAccounting();
         }
    }

    public static void fireCustomerDataChanged() {
        ContactsGUI.fireCustomerDataChanged();
    }

    public static void fireContactDataChanged() {
        // TODO: do we need to refresh the Contact Selector
        // What are Contact Selectors used for. Only Customers? ...
        ContactSelectorDialog.fireContactDataChangedForAll();
        ContactsGUI.fireContactDataChangedForAll();
    }

    public static void fireCustomerAddedOrRemoved(Accounting accounting) {
        ContactsGUI.fireCustomerAddedOrRemovedForAccounting(accounting);
        SalesOrderCreateGUI.fireCustomerAddedOrRemovedForAll();
    }

    public static void fireIngredientsAddedOrRemoved(Accounting accounting) {
        ArticlesGUI.fireIngredientAddedOrRemovedForAccounting(accounting);
        ArticlesGUI.fireTableUpdateForAccounting(accounting);
    }

    public static void fireAllergeneAddedOrRemoved() {
    }

    public static void fireRecipeDataUpdated(Accounting accounting) {
        MealRecipeViewGUI.fireTableUpdateForAccounting(accounting);
        MealRecipeEditGUI.fireTableUpdateForAccounting(accounting);
        MealIngredientsViewGUI.fireTableUpdateForAccounting(accounting);
        MealIngredientsEditGUI.fireTableUpdateForAccounting(accounting);
    }

    public static void fireArticleAddedOrRemoved(Accounting accounting) {
        ArticlesGUI.fireTableUpdateForAccounting(accounting);
    }

    public static void fireMealCountUpdated(Accounting accounting) {
        MealRecipeViewGUI.fireTableUpdateForAccounting(accounting);
        MealRecipeEditGUI.fireTableUpdateForAccounting(accounting);
    }

    public static void fireSupplierAddedOrRemoved(Accounting accounting) {
        ContactsGUI.fireTableUpdateForAccounting(accounting);
        ArticlesGUI.fireSupplierAddedOrRemovedForAccounting(accounting);
        ArticlesGUI.fireTableUpdateForAccounting(accounting);
        PurchaseOrderCreateGUI.fireSupplierAddedOrRemovedForAll();
    }

    public static void saveData(boolean writeHtml) {
        XMLWriter.writeAccountings(accountings, writeHtml);
    }


    public static void closeAllFrames(){
        for(JFrame frame: disposableComponents){
            frame.dispose();
        }
    }

    public static void addFrame(JFrame frame) {
        disposableComponents.add(frame);
    }

    public static void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if(journalType == accountGuiLeft.getJournalType())
            accountGuiLeft.setAccountTypesList(accountTypes);
    }
    public static void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if(journalType == accountGuiRight.getJournalType())
            accountGuiRight.setAccountTypesList(accountTypes);
    }
//    public static void setAccountsListLeft(JournalType journalType, AccountsList accountsList) {
//        if(journalType == accountGuiLeft.getJournalType())
//            accountGuiLeft.setAccountsList(accountsList);
//    }
//
//    public static void setAccountsListRight(JournalType journalType, AccountsList accountsList) {
//        if(journalType == accountGuiRight.getJournalType())
//            accountGuiRight.setAccountsList(accountsList);
//    }

    public static void selectTransaction(Transaction transaction){
        journalViewPanel.selectTransaction(transaction);
        transactionOverviewPanel.selectTransaction(transaction);
    }

    public static void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        if(journal==accountGuiLeft.getJournal()) {
            accountGuiLeft.setJournalType(journalType);
            accountGuiLeft.setAccountsList(journalType.getLeft());
        }
        if(journal==accountGuiRight.getJournal()) {
            accountGuiRight.setJournalType(journalType);
            accountGuiRight.setAccountsList(journalType.getRight());
        }
    }

    public static void fireMortgageAddedOrRemoved(Mortgages mortgages) {
        MortgageGUI.refreshAllFrames();
        mortgagesPanel.setMortgages(mortgages);
    }

    public static void fireMortgageEditedPayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage);
    }

    public static void fireMortgageEdited(Mortgage mortgage) {
        MortgageGUI.selectMortgage(mortgage);
    }

    public static void switchView(String view) {
        cardLayout.show(cardPanel, view);
        journalEditPanel.fireTransactionDataChanged();
        transactionOverviewPanel.fireJournalDataChanged();
    }
}