package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.AccountsGUI;
import be.dafke.BasicAccounting.Accounts.AccountsTableGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Contacts.ContactsMenu;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.Journals.JournalGUI;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.Journals.JournalsGUI;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BusinessActions.*;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModelDao.XMLReader;
import be.dafke.Utils.MultiValueMap;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JSplitPane.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Main {
    protected static Accountings accountings;
    private static File xmlFolder;
    private static File xslFolder;
    private static File htmlFolder;
    private static JournalGUI journalReadGUI;
    private static JournalInputGUI journalInputGUI;
    private static JournalsGUI journalsGUI;
    private static AccountsGUI accountsGUI1;
    private static AccountsGUI accountsGUI2;
    private static AccountsTableGUI accountsTableGUI;
    private static MortgagesGUI mortgagesGUI;
    private static AccountingMenuBar menuBar;
    private static AccountingGUIFrame frame;
    private static ArrayList<JournalListener> journalListeners = new ArrayList<>();
    private static ArrayList<AccountingListener> accountingListeners = new ArrayList<>();
    private static ArrayList<AccountsListener> accountsListeners = new ArrayList<>();
    private static ArrayList<MortgagesListener> mortgagesListeners = new ArrayList<>();

    private static MultiValueMap<Integer, JournalDataChangeListener> journalDataChangeListeners = new MultiValueMap<>();
    private static MultiValueMap<Integer, AccountDataChangeListener> accountDataChangeListeners = new MultiValueMap<>();
    private static ArrayList<JournalDataChangeListener> allJournalDataChangeListeners = new ArrayList<>();
    private static ArrayList<AccountDataChangeListener> allAccountDataChangeListeners = new ArrayList<>();

    private static HashMap<Journal,JournalDetails> journalDetailsMap = new HashMap<>();
    private static HashMap<Accounts,TestBalance> testBalanceMap = new HashMap<>();
    private static HashMap<Balance,BalanceGUI> otherBalanceMap = new HashMap<>();

    private static BalancesMenu balancesMenu;
    private static MorgagesMenu morgagesMenu;
    private static ContactsMenu contactsMenu;
    private static ProjectsMenu projectsMenu;
    private static CodaMenu codaMenu;

    public static void main(String[] args) {
        readXmlData();
        createComponents();
        frame = new AccountingGUIFrame("Accounting-all");
        frame.setContentPane(createContentPanel());
        createMenu();
        frame.setJMenuBar(menuBar);

        createListeners();
        linkListeners();

        setAccounting(accountings.getCurrentObject());


        launchFrame();

        /*
        launchFrame("input",new JournalInputGUI(),null);
        launchFrame("input",new JournalGUI(accountings.getCurrentObject()),null);
        launchFrame("journals",new JournalGUI(),null);
        launchFrame("acc1",new AccountsGUI(),null);
        launchFrame("acc2",new AccountsGUI(),null);
        */
    }

    private static void createComponents() {
        journalInputGUI = new JournalInputGUI();
        journalsGUI = new JournalsGUI(journalInputGUI);
        journalReadGUI = new JournalGUI(journalInputGUI);
        accountsGUI1 = new AccountsGUI(journalInputGUI);
        accountsGUI2 = new AccountsGUI(journalInputGUI);
        accountsTableGUI = new AccountsTableGUI(journalInputGUI);
        mortgagesGUI = new MortgagesGUI(journalInputGUI);
    }

    private static void createListeners() {
        accountsListeners.add(accountsGUI1);
        accountsListeners.add(accountsGUI2);
        accountsListeners.add(accountsTableGUI);
        // accountTypeListeners, etc.

        journalListeners.add(journalReadGUI);
        journalListeners.add(journalsGUI);  // will call setJournal() in JournalsGUI
        journalListeners.add(journalInputGUI);

        mortgagesListeners.add(mortgagesGUI);

        accountingListeners.add(accountsGUI1);
        accountingListeners.add(accountsGUI2);
        accountingListeners.add(accountsTableGUI);
        accountingListeners.add(mortgagesGUI);
        accountingListeners.add(journalsGUI);
        accountingListeners.add(journalInputGUI);
        accountingListeners.add(journalReadGUI);
        accountingListeners.add(frame);

        allAccountDataChangeListeners.add(accountsGUI1);
        allAccountDataChangeListeners.add(accountsGUI2);
        allAccountDataChangeListeners.add(accountsTableGUI);

        allJournalDataChangeListeners.add(journalReadGUI);
    }
    private static void linkListeners(){
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new SaveAllActionListener(accountings));
    }

    public static JPanel createContentPanel(){
        JPanel links = new JPanel();
        links.setLayout(new BorderLayout());
//        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        JSplitPane accountsPanel = createSplitPane(accountsGUI1, accountsGUI2, VERTICAL_SPLIT);
        links.add(accountsPanel, BorderLayout.CENTER);
//        links.add(accountsGUI1);
//        links.add(accountsGUI2);
        links.add(mortgagesGUI, BorderLayout.SOUTH);
        links.add(journalsGUI, BorderLayout.NORTH);
//        links.add(createSaveButton());

        JPanel accountingMultiPanel = new JPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = createSplitPane(journalReadGUI, journalInputGUI, VERTICAL_SPLIT);
//        splitPane.add(new JournalGUI(accounting), JSplitPane.TOP);
//        splitPane.add(new JournalInputGUI(), JSplitPane.BOTTOM);

        JSplitPane mainSplitPane = createSplitPane(splitPane, accountsTableGUI, HORIZONTAL_SPLIT);
//
        accountingMultiPanel.add(mainSplitPane, BorderLayout.CENTER);
        accountingMultiPanel.add(links, BorderLayout.WEST);
        return accountingMultiPanel;
    }

    private static JSplitPane createSplitPane(JComponent panel1, JComponent panel2, int orientation) {
        JSplitPane splitPane = new JSplitPane(orientation);
        if(orientation == JSplitPane.VERTICAL_SPLIT){
            splitPane.add(panel1,TOP);
            splitPane.add(panel2,BOTTOM);
        } else {
            splitPane.add(panel1,LEFT);
            splitPane.add(panel2,RIGHT);
        }
        return splitPane;
    }

    private static void createMenu() {
        menuBar = new AccountingMenuBar(accountings);
        balancesMenu = new BalancesMenu();
        contactsMenu = new ContactsMenu();
        morgagesMenu = new MorgagesMenu();

        projectsMenu = new ProjectsMenu();
        codaMenu = new CodaMenu();
        menuBar.add(balancesMenu);
        menuBar.add(contactsMenu);
        menuBar.add(morgagesMenu);
        menuBar.add(projectsMenu);
        menuBar.add(codaMenu);
    }

    private static void launchFrame(){
        SaveAllActionListener.addFrame(frame); // MAIN
        frame.pack();
        frame.setVisible(true);
    }

    private static void readXmlData() {
        setXmlFolder();
        accountings = new Accountings(xmlFolder, xslFolder, htmlFolder);
        if(!xmlFolder.exists()){
            xmlFolder.mkdirs();
        }
        File subFolder = new File(xmlFolder, Accountings.ACCOUNTINGS);
        if(!subFolder.exists()){
            subFolder.mkdir();
        }
        File file = new File(xmlFolder, "Accountings.xml");
        if(file.exists()){
            XMLReader.readCollection(accountings, xmlFolder);
        }
    }

    private static void setXmlFolder(){
//            File userHome = new File(System.getProperty("user.home"));
        File parentFolder = new File("data/Accounting");
        xmlFolder = new File(parentFolder, "xml");
        xslFolder = new File(parentFolder, "xsl");
        htmlFolder = new File(parentFolder, "html");
    }

    private static JButton createSaveButton(){
        JButton saveButton = new JButton("Save all");
        saveButton.addActionListener(new SaveAllActionListener(accountings));
        return saveButton;
    }

    public static void setAccounting(Accounting accounting) {
        accountings.setCurrentObject(accounting); // only need to write to XML, call this only when writing XML files?
        for(AccountingListener accountingListener:accountingListeners){
            accountingListener.setAccounting(accounting);
        }
        ProjectsMenu.setAccounting(accounting);
        MorgagesMenu.setAccounting(accounting);
        CodaMenu.setAccounting(accounting);
        ContactsMenu.setAccounting(accounting);
        BalancesMenu.setAccounting(accounting);
        AccountingMenuBar.setAccounting(accounting);
    }

    public static void setJournal(Journal journal) {
        accountings.getCurrentObject().getJournals().setCurrentObject(journal);  // idem, only needed for XMLWriter
        for(JournalListener journalListener : journalListeners){
            journalListener.setJournal(journal);
        }
        // just to be sure ???
        journalInputGUI.setTransaction(journal.getCurrentObject());
    }

    public static void addJournalDataListener(Journal journal, JournalDataChangeListener gui) {
        journalDataChangeListeners.addValue(journal.hashCode(), gui);
    }

    public static void addJournalDataListener(JournalDataChangeListener gui) {
        allJournalDataChangeListeners.add(gui);
    }

    public static void addAccountDataListener(Account account, AccountDataChangeListener gui) {
        accountDataChangeListeners.addValue(account.hashCode(), gui);
    }

    public static void addAccountDataListener(AccountDataChangeListener gui) {
        allAccountDataChangeListeners.add(gui);
    }

    public static void fireJournalDataChanged(Journal journal){
        ArrayList<JournalDataChangeListener> journalDataChangeListenerList = journalDataChangeListeners.get(journal.hashCode());
        if(journalDataChangeListenerList!=null) {
            for (JournalDataChangeListener journalDataChangeListener : journalDataChangeListenerList) {
                journalDataChangeListener.fireJournalDataChanged();
            }
        }
        for (JournalDataChangeListener journalDataChangeListener : allJournalDataChangeListeners){
            journalDataChangeListener.fireJournalDataChanged();
        }
    }

    public static void fireAccountDataChanged(Account account){
        ArrayList<AccountDataChangeListener> accountDataChangeListenerList = accountDataChangeListeners.get(account.hashCode());
        if(accountDataChangeListenerList!=null){
            for(AccountDataChangeListener accountDataChangeListener: accountDataChangeListenerList) {
                accountDataChangeListener.fireAccountDataChanged();
            }
        }
        // fireAccountDataChanged in AccountsGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        for(AccountDataChangeListener accountDataChangeListener: allAccountDataChangeListeners){
            accountDataChangeListener.fireAccountDataChanged();
        }
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        for(TestBalance testBalance:testBalanceMap.values()){
            testBalance.fireAccountDataChanged();
        }
        for(BalanceGUI balanceGUI:otherBalanceMap.values()){
            balanceGUI.fireAccountDataChanged();
        }
    }

    public static JournalDetails getJournalDetails(Journal journal, Journals journals){
        JournalDetails journalDetails = journalDetailsMap.get(journal);
        if(journalDetails==null){
            journalDetails = new JournalDetails(journal, journals, journalInputGUI);
            addJournalDataListener(journal,journalDetails);
            journalDetailsMap.put(journal, journalDetails);
            SaveAllActionListener.addFrame(journalDetails);
        }
        journalDetails.setVisible(true);
        return journalDetails;
    }

    public static TestBalance getTestBalance(Journals journals, Accounts accounts, AccountTypes accountTypes) {
        TestBalance testBalance = testBalanceMap.get(accounts);
        if(testBalance==null){
            testBalance = new TestBalance(journals, accounts, accountTypes, journalInputGUI);
            testBalanceMap.put(accounts,testBalance);
            SaveAllActionListener.addFrame(testBalance);
        }
        testBalance.setVisible(true);
        return testBalance;
    }

    public static BalanceGUI getBalance(Journals journals, Balance balance) {
        BalanceGUI balanceGUI = otherBalanceMap.get(balance);
        if(balanceGUI==null){
            balanceGUI = new BalanceGUI(journals, balance, journalInputGUI);
            otherBalanceMap.put(balance,balanceGUI);
            SaveAllActionListener.addFrame(balanceGUI);
        }
        balanceGUI.setVisible(true);
        return balanceGUI;
    }

    public static void newAccounting(Accountings accountings) {
        String name = JOptionPane.showInputDialog(null, "Enter a name");
        try {
            Accounting accounting = new Accounting();
            accounting.getBalances().addDefaultBalances();
            accounting.setName(name);
            accountings.addBusinessObject(accounting);
            accountings.setCurrentObject(accounting);
            setAccounting(accounting);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNTING_DUPLICATE_NAME);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNTING_NAME_EMPTY);
        }
    }
}