package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetails;
import be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.Accounts.AccountSelector;
import be.dafke.BasicAccounting.Accounts.AccountsMenu;
import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountsTableGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Contacts.ContactSelector;
import be.dafke.BasicAccounting.Contacts.ContactsGUI;
import be.dafke.BasicAccounting.Contacts.ContactsMenu;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.Journals.JournalGUI;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.Journals.JournalManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalsMenu;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.VAT.VATFieldsGUI;
import be.dafke.BasicAccounting.VAT.VATMenu;
import be.dafke.BasicAccounting.VAT.VATTransactionsGUI;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.XMLReader;
import be.dafke.BusinessModelDao.XMLWriter;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.JSplitPane.BOTTOM;
import static javax.swing.JSplitPane.LEFT;
import static javax.swing.JSplitPane.RIGHT;
import static javax.swing.JSplitPane.TOP;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Main {
    private static final ArrayList<JFrame> disposableComponents = new ArrayList<>();

    protected static Accountings accountings;
    private static File xmlFolder;
    private static File xslFolder;
    private static File htmlFolder;
    private static JournalGUI journalReadGUI;
    private static JournalInputGUI journalInputGUI;
    private static AccountsTableGUI accountGuiLeft;
    private static AccountsTableGUI accountGuiRight;
    private static MortgagesGUI mortgagesGUI;
    private static JMenuBar menuBar;
    private static AccountingMenu accountingMenu;
    private static AccountingGUIFrame frame;

    private static AccountsMenu accountsMenu;
    private static JournalsMenu journalsMenu;
    private static BalancesMenu balancesMenu;
    private static MorgagesMenu morgagesMenu;
    private static ContactsMenu contactsMenu;
    private static ProjectsMenu projectsMenu;
    private static CodaMenu codaMenu;
    private static VATMenu vatMenu;

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
        accountGuiLeft = new AccountsTableGUI(journalInputGUI);
        accountGuiRight = new AccountsTableGUI(journalInputGUI);
        mortgagesGUI = new MortgagesGUI(journalInputGUI);
    }

    private static void setCloseOperation(){
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PopupForTableActivator.closeAllPopups();
                Main.closeAllFrames();
                Main.saveData();
            }
        });
    }

    public static JPanel createContentPanel(){
        JPanel links = new JPanel();
        links.setLayout(new BorderLayout());
        links.add(accountGuiLeft, BorderLayout.CENTER);
        links.add(mortgagesGUI, BorderLayout.SOUTH);

        JPanel accountingMultiPanel = new JPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = createSplitPane(journalReadGUI, journalInputGUI, VERTICAL_SPLIT);

        accountingMultiPanel.add(accountGuiRight, BorderLayout.EAST);
        accountingMultiPanel.add(splitPane, BorderLayout.CENTER);
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
        menuBar = new JMenuBar();

        accountingMenu = new AccountingMenu(accountings);
        accountsMenu = new AccountsMenu(journalInputGUI);
        journalsMenu = new JournalsMenu(journalInputGUI);
        balancesMenu = new BalancesMenu(journalInputGUI);
        contactsMenu = new ContactsMenu();
        morgagesMenu = new MorgagesMenu();
        projectsMenu = new ProjectsMenu();
        codaMenu = new CodaMenu();
        vatMenu = new VATMenu();

        menuBar.add(accountingMenu);
        menuBar.add(journalsMenu);
        menuBar.add(accountsMenu);
        menuBar.add(balancesMenu);
        menuBar.add(contactsMenu);
        menuBar.add(morgagesMenu);
        menuBar.add(projectsMenu);
        menuBar.add(codaMenu);
        menuBar.add(vatMenu);
    }

    private static void launchFrame(){
        Main.addFrame(frame); // MAIN
        frame.pack();
        frame.setVisible(true);
    }

    private static void readXmlData() {
//        File userHome = new File(System.getProperty("user.home"));
        File parentFolder = new File("data/Accounting");
        xmlFolder = new File(parentFolder, "xml");
        xslFolder = new File(parentFolder, "xsl");
        htmlFolder = new File(parentFolder, "html");

        accountings = new Accountings();

        XMLReader.readAccountings(accountings, xmlFolder);
        for(Accounting accounting:accountings.getBusinessObjects()) {
            XMLReader.readAccounting(accounting, xmlFolder);
        }
        XMLReader.readSession(accountings, xmlFolder);
    }

    private static void setXmlFolder(){
    }

    private static JButton createSaveButton(){
        JButton saveButton = new JButton("Save all");
        saveButton.addActionListener(e -> Main.saveData());
        return saveButton;
    }

    public static void setAccounting(Accounting accounting) {
        accountings.setCurrentObject(accounting); // only need to write to XML, call this only when writing XML files?

        frame.setAccounting(accounting);

        accountGuiLeft.setAccounting(accounting);
        accountGuiRight.setAccounting(accounting);
        journalInputGUI.setAccounting(accounting);
        journalReadGUI.setJournals(accounting==null?null:accounting.getJournals());
        mortgagesGUI.setMortgages(accounting==null?null:accounting.getMortgages());

        projectsMenu.setAccounting(accounting);
        morgagesMenu.setAccounting(accounting);
        codaMenu.setAccounting(accounting);
        contactsMenu.setAccounting(accounting);
        accountsMenu.setAccounting(accounting);
        journalsMenu.setAccounting(accounting);
        balancesMenu.setAccounting(accounting);
        accountingMenu.setAccounting(accounting);
        vatMenu.setAccounting(accounting);

        if(accounting!=null) {
            vatMenu.setVisible(accounting.isVatAccounting());
            morgagesMenu.setVisible(accounting.isMortgagesAccounting());
            contactsMenu.setVisible(accounting.isContactsAccounting());
            projectsMenu.setVisible(accounting.isProjectsAccounting());

            mortgagesGUI.setVisible(accounting.isMortgagesAccounting());

            if (accounting.getJournals() != null) {
                setJournal(accounting.getJournals().getCurrentObject());
            }
        }
    }

    public static void fireBalancesChanged(){
        balancesMenu.fireBalancesChanged();
    }

    public static void fireVATFieldsUpdated(/*VATFields vatFields*/){
        VATFieldsGUI.fireVATFieldsUpdated(/*vatFields*/);
        VATTransactionsGUI.fireVATTransactionsUpdated();
    }

    public static void setJournal(Journal journal) {
        Accounting accounting = journal.getAccounting();
        accounting.getJournals().setCurrentObject(journal);  // idem, only needed for XMLWriter
        journalReadGUI.setJournal(journal);
        journalInputGUI.setJournal(journal);
        frame.setJournal(journal);
        accountGuiLeft.setJournal(journal);
        accountGuiRight.setJournal(journal);
        if(journal!=null){

//            mortgagesGUI.setVisible(journal.isMortgagesJournal());

            JournalType journalType = journal.getType();
            accountGuiLeft.setJournalType(journalType);
            accountGuiRight.setJournalType(journalType);

            if(journalType==null) {

//                journalType = accounting.getJournalTypes().getBusinessObject("default");
//                journal.setType(journalType);
                AccountTypes accountTypes = accounting.getAccountTypes();

                AccountsList leftList = new AccountsList();
                leftList.addAllTypes(accountTypes, true);
                accountGuiLeft.setAccountsList(leftList);

                AccountsList rightList = new AccountsList();
                rightList.addAllTypes(accountTypes,true);
                accountGuiRight.setAccountsList(rightList);

                accountGuiLeft.setVatType(null);
                accountGuiRight.setVatType(null);
//            setVatType(VATTransaction.VATType.NONE);

            } else  {
                setAccountTypes(journalType);
                setVATTypes(journalType);
            }
        } else {
        }
    }

    public static void fireTransactionInputDataChanged(){
        journalInputGUI.fireTransactionDataChanged();
    }

    public static void fireJournalDataChanged(Journal journal){
        JournalDetails.fireJournalDataChangedForAll(journal);
        JournalManagementGUI.fireJournalDataChangedForAll();
        journalReadGUI.fireJournalDataChanged();
        journalsMenu.fireJournalDataChanged();
        frame.fireDataChanged();
    }

    public static void fireAccountDataChanged(Account account){
        AccountDetails.fireAccountDataChangedForAll(account);
        AccountSelector.fireAccountDataChangedForAll();
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        journalReadGUI.fireJournalDataChanged();
        accountGuiLeft.fireAccountDataChanged();
        accountGuiRight.fireAccountDataChanged();

        AccountManagementGUI.fireAccountDataChangedForAll();
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        TestBalance.fireAccountDataChangedForAll();
        BalanceGUI.fireAccountDataChangedForAll();
    }

    public static void fireContactDataChanged() {
        ContactSelector.fireContactDataChangedForAll();
        ContactsGUI.fireContactDataChangedForAll();
    }

    public static void saveData() {
        xmlFolder.mkdirs();
        XMLWriter.writeAccountings(accountings, xmlFolder);

//        File xslFolder = accountings.getXslFolder();
//        File htmlFolder = accountings.getHtmlFolder();
//        htmlFolder.mkdirs();

//        XMLtoHTMLWriter.toHtml(accountings, xmlFolder, xslFolder, htmlFolder);


    }


    public static void closeAllFrames(){
        for(JFrame frame: disposableComponents){
            frame.dispose();
        }
    }

    public static void addFrame(JFrame frame) {
        disposableComponents.add(frame);
    }

    public static void setAccountsListLeft(JournalType journalType, AccountsList accountsList) {
        if(journalType == accountGuiLeft.getJournalType())
            accountGuiLeft.setAccountsList(accountsList);
    }

    public static void setAccountsListRight(JournalType journalType, AccountsList accountsList) {
        if(journalType == accountGuiRight.getJournalType())
            accountGuiRight.setAccountsList(accountsList);
    }

    public static void setAccountTypes(JournalType journalType) {
        AccountsList left = journalType.getLeft();
        AccountsList right = journalType.getRight();
        accountGuiLeft.setAccountsList(left);
        accountGuiRight.setAccountsList(right);
    }

    public static void setVATTypes(JournalType journalType) {
        AccountsList left = journalType.getLeft();
        AccountsList right = journalType.getRight();

        accountGuiLeft.setVatType(left.getVatType());
        accountGuiRight.setVatType(right.getVatType());
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
}