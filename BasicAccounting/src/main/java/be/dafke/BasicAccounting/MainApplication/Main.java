package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.*;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Contacts.ContactSelector;
import be.dafke.BasicAccounting.Contacts.ContactsMenu;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.Journals.JournalGUI;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.Journals.JournalsGUI;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.VAT.VATFieldsGUI;
import be.dafke.BasicAccounting.VAT.VATMenu;
import be.dafke.BasicAccounting.VAT.VATTransactionsGUI;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.XMLReader;
import be.dafke.BusinessModelDao.XMLWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import static be.dafke.BasicAccounting.Journals.JournalManagementGUI.fireJournalDataChangedForAll;
import static javax.swing.JSplitPane.*;

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
    private static JournalsGUI journalsGUI;
    private static AccountsGUI accountGuiLeft;
    private static AccountsGUI accountGuiRight;
    private static MortgagesGUI mortgagesGUI;
    private static AccountingMenuBar menuBar;
    private static AccountingGUIFrame frame;

    private static AccountsMenu accountsMenu;
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
        journalsGUI = new JournalsGUI(journalReadGUI,journalInputGUI);
        accountGuiLeft = new AccountsTableGUI(journalInputGUI);
        accountGuiRight = new AccountsTableGUI(journalInputGUI);
        mortgagesGUI = new MortgagesGUI(journalInputGUI);
    }

    private static void setCloseOperation(){
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
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
        links.add(journalsGUI, BorderLayout.NORTH);

        JPanel accountingMultiPanel = new JPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = createSplitPane(journalReadGUI, journalInputGUI, VERTICAL_SPLIT);
        JSplitPane mainSplitPane = createSplitPane(splitPane, accountGuiRight, HORIZONTAL_SPLIT);

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

        accountsMenu = new AccountsMenu(journalInputGUI);
        balancesMenu = new BalancesMenu(journalInputGUI);
        contactsMenu = new ContactsMenu();
        morgagesMenu = new MorgagesMenu();
        projectsMenu = new ProjectsMenu();
        codaMenu = new CodaMenu();
        vatMenu = new VATMenu();

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
//        Accounting accounting = accountings.getCurrentObject();
        for(Accounting accounting:accountings.getBusinessObjects()) {
            XMLReader.readAccounting(accounting, xmlFolder);
        }
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
        journalsGUI.setAccounting(accounting);
        journalInputGUI.setAccounting(accounting);
        journalReadGUI.setJournals(accounting==null?null:accounting.getJournals());
        mortgagesGUI.setMortgages(accounting==null?null:accounting.getMortgages());

        projectsMenu.setAccounting(accounting);
        morgagesMenu.setAccounting(accounting);
        codaMenu.setAccounting(accounting);
        contactsMenu.setAccounting(accounting);
        accountsMenu.setAccounting(accounting);
        balancesMenu.setAccounting(accounting);
        menuBar.setAccounting(accounting);
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

    public static void addJournal(Journal journal){
        journalsGUI.addJournal(journal);
        fireJournalDataChangedForAll();
    }

    public static void setJournal(Journal journal) {
        Accounting accounting = accountings.getCurrentObject();
        accounting.getJournals().setCurrentObject(journal);  // idem, only needed for XMLWriter
        journalsGUI.setJournal(journal);
        if(journal!=null && journal.getType()!=null) {

//            mortgagesGUI.setVisible(journal.isMortgagesJournal());

            JournalType journalType = journal.getType();
            setTypes(journalType, accounting.getAccountTypes());
        } else {
        }
    }

    public static void fireJournalDataChanged(Journal journal){
        JournalDetails.fireJournalDataChangedForAll(journal);
        fireJournalDataChangedForAll();
        journalReadGUI.fireJournalDataChanged();
    }

    public static void fireAccountDataChanged(Account account){
        AccountDetails.fireAccountDataChangedForAll(account);
        AccountSelector.fireAccountDataChangedForAll();
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        accountGuiLeft.fireAccountDataChanged();
        accountGuiRight.fireAccountDataChanged();

        AccountManagementGUI.fireAccountDataChangedForAll();
        // refresh all balances if an account is update, filtering on accounting/accounts/accountType could be applied
        TestBalance.fireAccountDataChangedForAll();
        BalanceGUI.fireAccountDataChangedForAll();
    }

    public static void fireContactDataChanged(Contact contact) {
        ContactSelector.fireContactDataChangedForAll();
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

    public static void setTypes(JournalType journalType, AccountTypes accountTypes) {
        if(journalType==null) {
            accountGuiLeft.setAccountTypes(accountTypes);
            accountGuiRight.setAccountTypes(accountTypes);
            accountGuiLeft.setVatType(null);
            accountGuiRight.setVatType(null);
//            setVatType(VATTransaction.VATType.NONE);
        } else {
            accountGuiLeft.setAccountTypes(journalType.getDebetTypes());
            accountGuiRight.setAccountTypes(journalType.getCreditTypes());
            VATTransaction.VATType vatType = journalType.getVatType();
            if (vatType == VATTransaction.VATType.SALE) {
                accountGuiLeft.setVatType(VATTransaction.VATType.CUSTOMER);
                accountGuiRight.setVatType(VATTransaction.VATType.SALE);
//                setVatType(VATTransaction.VATType.SALE); // 2 -> BTW
            } else if (vatType == VATTransaction.VATType.PURCHASE) {
                accountGuiLeft.setVatType(VATTransaction.VATType.PURCHASE);
                accountGuiRight.setVatType(null);
//                setVatType(VATTransaction.VATType.PURCHASE); // 1 -> BTW
            } else {
//                setVatType(VATTransaction.VATType.NONE);
                accountGuiLeft.setVatType(null);
                accountGuiRight.setVatType(null);
            }
        }
    }
}