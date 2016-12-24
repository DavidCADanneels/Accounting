package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.*;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Contacts.ContactsMenu;
import be.dafke.BasicAccounting.Journals.*;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModelDao.XMLReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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

        setCloseOperation();

        setAccounting(accountings.getCurrentObject());

        launchFrame();
    }

    private static void createComponents() {
        journalInputGUI = new JournalInputGUI();
        journalReadGUI = new JournalGUI(journalInputGUI);
        journalsGUI = new JournalsGUI(journalReadGUI,journalInputGUI);
        accountsGUI1 = new AccountsGUI(journalInputGUI);
        accountsGUI2 = new AccountsGUI(journalInputGUI);
        accountsTableGUI = new AccountsTableGUI(journalInputGUI);
        mortgagesGUI = new MortgagesGUI(journalInputGUI);
    }

    private static void setCloseOperation(){
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new SaveAllActionListener(accountings));
    }

    public static JPanel createContentPanel(){
        JPanel links = new JPanel();
        links.setLayout(new BorderLayout());
        JSplitPane accountsPanel = createSplitPane(accountsGUI1, accountsGUI2, VERTICAL_SPLIT);
        links.add(accountsPanel, BorderLayout.CENTER);
        links.add(mortgagesGUI, BorderLayout.SOUTH);
        links.add(journalsGUI, BorderLayout.NORTH);

        JPanel accountingMultiPanel = new JPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = createSplitPane(journalReadGUI, journalInputGUI, VERTICAL_SPLIT);
        JSplitPane mainSplitPane = createSplitPane(splitPane, accountsTableGUI, HORIZONTAL_SPLIT);

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
        balancesMenu = new BalancesMenu(journalInputGUI);
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

        frame.setAccounting(accounting);

        accountsGUI1.setAccounting(accounting);
        accountsGUI2.setAccounting(accounting);
        accountsTableGUI.setAccounting(accounting);
        journalsGUI.setAccounting(accounting);
        journalInputGUI.setAccounting(accounting);
        journalReadGUI.setJournals(accounting==null?null:accounting.getJournals());
        mortgagesGUI.setMortgages(accounting==null?null:accounting.getMortgages());

        ProjectsMenu.setAccounting(accounting);
        MorgagesMenu.setAccounting(accounting);
        CodaMenu.setAccounting(accounting);
        ContactsMenu.setAccounting(accounting);
        BalancesMenu.setAccounting(accounting);
        AccountingMenuBar.setAccounting(accounting);
    }

    public static void setJournal(Journal journal) {
        accountings.getCurrentObject().getJournals().setCurrentObject(journal);  // idem, only needed for XMLWriter
        journalsGUI.setJournal(journal);
        JournalType journalType = journal.getType();
        accountsGUI1.setAccountTypes(journalType.getDebetTypes());
        accountsGUI2.setAccountTypes(journalType.getCreditTypes());
    }

    public static void fireJournalDataChanged(Journal journal){
        JournalDetails.fireJournalDataChangedForAll(journal);
        JournalManagementGUI.fireJournalDataChangedForAll();
        journalReadGUI.fireJournalDataChanged();
    }

    public static void fireAccountDataChanged(Account account){
        AccountDetails.fireAccountDataChangedForAll(account);
        AccountSelector.fireAccountDataChangedForAll();
        // fireAccountDataChanged in AccountsGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        accountsGUI1.fireAccountDataChanged();
        accountsGUI2.fireAccountDataChanged();
        accountsTableGUI.fireAccountDataChanged();

        AccountManagementGUI.fireAccountDataChangedForAll();
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        TestBalance.fireAccountDataChangedForAll();
        BalanceGUI.fireAccountDataChangedForAll();
    }

    public static void newAccounting(Accountings accountings) {
        String name = JOptionPane.showInputDialog(null, "Enter a name");
        try {
            Accounting accounting = new Accounting();
            accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes());
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