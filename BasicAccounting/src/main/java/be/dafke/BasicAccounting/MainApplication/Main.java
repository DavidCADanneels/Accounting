package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.SaveAllActionListener;
import be.dafke.BusinessActions.*;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;
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
    private static AccountsGUI accountsGUI;
    private static AccountsTableGUI accountsTableGUI;
    private static MortgagesGUI mortgagesGUI;
    private static AccountingMenuBar menuBar;
    private static AccountingGUIFrame frame;
    private static ArrayList<JournalsListener> journalsListeners = new ArrayList<>();
    private static ArrayList<AccountingListener> accountingListeners = new ArrayList<>();
    private static ArrayList<AccountsListener> accountsListeners = new ArrayList<>();
    private static ArrayList<MortgagesListener> mortgagesListeners = new ArrayList<>();

    private static MultiValueMap<Integer, JournalDataChangeListener> journalDataChangeListeners = new MultiValueMap<>();
    private static MultiValueMap<Integer, AccountDataChangeListener> accountDataChangeListeners = new MultiValueMap<>();

    private static HashMap<Account,AccountDetails> accountDetailsMap = new HashMap<>();
    private static HashMap<Journal,JournalDetails> journalDetailsMap = new HashMap<>();
    private static HashMap<Accounts,TestBalance> testBalanceMap = new HashMap<>();
    private static HashMap<Accounts,BalanceGUI> otherBalanceMap = new HashMap<>();

    private static BalancesMenu balancesMenu;
    private static MorgagesMenu morgagesMenu;
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
        journalReadGUI = new JournalGUI();
        journalInputGUI = new JournalInputGUI();
        journalsGUI = new JournalsGUI();
        accountsGUI = new AccountsGUI();
        accountsTableGUI = new AccountsTableGUI();
        mortgagesGUI = new MortgagesGUI();
    }

    private static void createListeners() {
        accountsListeners.add(accountsGUI);
        accountsListeners.add(accountsTableGUI);

        journalsListeners.add(accountsGUI);
        journalsListeners.add(accountsTableGUI);
        journalsListeners.add(mortgagesGUI);
        journalsListeners.add(journalReadGUI);
        journalsListeners.add(journalInputGUI);
        journalsListeners.add(journalsGUI);  // will call setJournal() in JournalsGUI

        mortgagesListeners.add(mortgagesGUI);


        accountingListeners.add(accountsGUI);
        accountingListeners.add(accountsTableGUI);
        accountingListeners.add(mortgagesGUI);
        accountingListeners.add(journalsGUI);
        accountingListeners.add(journalInputGUI);
        accountingListeners.add(journalReadGUI);
        accountingListeners.add(balancesMenu);
        accountingListeners.add(morgagesMenu);
        accountingListeners.add(projectsMenu);
        accountingListeners.add(codaMenu);
        accountingListeners.add(menuBar);
        accountingListeners.add(frame);
    }
    private static void linkListeners(){
        accountsGUI.setJournalDataChangedListener(journalInputGUI);
        accountsTableGUI.addAddBookingLister(journalInputGUI);
        mortgagesGUI.setJournalDataChangedListener(journalInputGUI);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new SaveAllActionListener(accountings));
    }

    public static JPanel createContentPanel(){
        JPanel links = new JPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
//        links.add(new AccountsGUI());
        links.add(accountsGUI);
        links.add(accountsTableGUI);
        links.add(mortgagesGUI);
        links.add(journalsGUI);
        links.add(createSaveButton());

        JPanel accountingMultiPanel = new JPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = createSplitPane(journalReadGUI, journalInputGUI, JSplitPane.VERTICAL_SPLIT);
//        splitPane.add(new JournalGUI(accounting), JSplitPane.TOP);
//        splitPane.add(new JournalInputGUI(), JSplitPane.BOTTOM);
        accountingMultiPanel.add(splitPane, BorderLayout.CENTER);
        accountingMultiPanel.add(links, BorderLayout.WEST);
        return accountingMultiPanel;
    }

    private static JSplitPane createSplitPane(JPanel panel1, JPanel panel2, int orientation) {
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
        morgagesMenu = new MorgagesMenu();
        projectsMenu = new ProjectsMenu();
        codaMenu = new CodaMenu();
        menuBar.add(balancesMenu);
        menuBar.add(morgagesMenu);
        menuBar.add(projectsMenu);
        menuBar.add(codaMenu);
    }

    private static void launchFrame(){
        ComponentMap.addDisposableComponent(frame.hashCode()+"", frame); // MAIN
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
    }

    public static void setJournal(Journal journal) {
        accountings.getCurrentObject().getJournals().setCurrentObject(journal);  // idem, only needed for XMLWriter
        for(JournalsListener journalsListener :journalsListeners){
            journalsListener.setJournal(journal);
        }
    }

    public static void addJournalDataListener(Journal journal, JournalDataChangeListener gui) {
        journalDataChangeListeners.addValue(journal.hashCode(), gui);
    }

    public static void addAccountDataListener(Account account, AccountDataChangeListener gui) {
        accountDataChangeListeners.addValue(account.hashCode(), gui);
    }

    public static void fireJournalDataChanged(Journal journal){
        ArrayList<JournalDataChangeListener> journalDataChangeListenerList = journalDataChangeListeners.get(journal.hashCode());
        if(journalDataChangeListenerList!=null) {
            for (JournalDataChangeListener journalDataChangeListener : journalDataChangeListenerList) {
                journalDataChangeListener.fireJournalDataChanged();
            }
        }
        // always refresh. We could add a filter here for the active Journal
        journalReadGUI.fireJournalDataChanged();
    }

    public static void fireAccountDataChanged(Account account){
        ArrayList<AccountDataChangeListener> accountDataChangeListenerList = accountDataChangeListeners.get(account.hashCode());
        if(accountDataChangeListenerList!=null){
            for(AccountDataChangeListener accountDataChangeListener: accountDataChangeListenerList) {
                accountDataChangeListener.fireAccountDataChanged();
            }
        }
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        for(TestBalance testBalance:testBalanceMap.values()){
            testBalance.fireAccountDataChanged();
        }
        for(BalanceGUI balanceGUI:otherBalanceMap.values()){
            balanceGUI.fireAccountDataChanged();
        }
    }

    public static AccountDetails getAccountDetails(Account account, Journals journals){
        AccountDetails accountDetails = accountDetailsMap.get(account);
        if(accountDetails==null){
            accountDetails = new AccountDetails(account, journals);
            addAccountDataListener(account,accountDetails);
            accountDetailsMap.put(account, accountDetails);
            ComponentMap.addDisposableComponent("Details"+account.hashCode(),accountDetails);
        }
        accountDetails.setVisible(true);
        return accountDetails;
    }

    public static JournalDetails getJournalDetails(Journal journal, Journals journals){
        JournalDetails journalDetails = journalDetailsMap.get(journal);
        if(journalDetails==null){
            journalDetails = new JournalDetails(journal, journals);
            addJournalDataListener(journal,journalDetails);
            journalDetailsMap.put(journal, journalDetails);
            ComponentMap.addDisposableComponent("Details"+journal.hashCode(),journalDetails);
        }
        journalDetails.setVisible(true);
        return journalDetails;
    }

    public static TestBalance getTestBalance(Journals journals, Accounts accounts, AccountTypes accountTypes) {
        TestBalance testBalance = testBalanceMap.get(accounts);
        if(testBalance==null){
            testBalance = new TestBalance(journals, accounts, accountTypes);
            testBalanceMap.put(accounts,testBalance);
            ComponentMap.addDisposableComponent(journals.hashCode()+""+accounts.hashCode(),testBalance);
        }
        testBalance.setVisible(true);
        return testBalance;
    }

}