package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.Details.AccountDetails;
import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModelDao.ObjectModelSAXParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * User: Dafke
 * Date: 26/02/13
 * Time: 6:36
 */
public class AccountingActionListener extends WindowAdapter implements ActionListener {

    protected final Accountings accountings;
    public static final String MAIN = "MainPanel";
    public static final String MENU = "MenuBar";
    public static final String ACCOUNT_MANAGEMENT = "AccountManagement";
    public static final String JOURNAL_MANAGEMENT = "JournalManagement";
    public static final String JOURNAL_TYPE_MANAGEMENT = "JournalTypeManagement";

    public static final String JOURNAL_DETAILS = "JournalDetails";
    public static final String ACCOUNT_DETAILS = "AccountDetails";
    public static final String NEW_ACCOUNTING = "NewAccounting";
    public static final String OPEN_ACCOUNTING = "OpenAccounting";
    public static final String NEW_ACCOUNT = "NewAccount";

    public AccountingActionListener(Accountings accountings){
        this.accountings = accountings;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        File xmlFolder = accountings.getXmlFolder();
        xmlFolder.mkdirs();
        ObjectModelSAXParser.writeCollection(accountings, xmlFolder, 0);

        File xslFolder = new File("BasicAccounting/src/main/resources/xsl");
        File htmlFolder = accountings.getHtmlFolder();
        htmlFolder.mkdirs();
//        ///
//        File xmlFile = new File(xmlFolder, "Accountings.xml");
//        File htmlFile = new File(htmlFolder, "Accountings.html");
//
//        Utils.xmlToHtml(xmlFile, new File(xslFolder, "Accountings.xsl"), htmlFile, null);

//        File accountingsXmlFolder = new File(xmlFolder, "Accountings");
//        File accountingsHtmlFolder = new File(htmlFolder, "Accountings");

           ///


//        ObjectModelSAXParser.toHtml(accountings, xmlFolder, xslFolder, htmlFolder);

        // TODO: remove this by refactoring Extension and write methods
        for(Accounting accounting : accountings.getBusinessObjects()){
            for(AccountingExtension extension: accounting.getExtensions()){
                File rootFolder = new File(accountings.getXmlFolder(), "Accountings");
                File subFolder = new File(rootFolder, accounting.getName());
                extension.extendWriteCollection(accounting, subFolder);
            }
        }
        AccountingComponentMap.closeAllFrames();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if (actionCommand.equals(AccountingActionListener.NEW_ACCOUNT)){
            new NewAccountGUI(accountings.getCurrentObject()).setVisible(true);
        } else if (actionCommand.equals(AccountingActionListener.NEW_ACCOUNTING)) {
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            try {
                Accounting accounting = new Accounting(name);
//                accounting.setName(name);
                accountings.addBusinessObject(accounting);
                accountings.setCurrentObject(name);
                AccountingComponentMap.addAccountingComponents(accounting, this);
                JOptionPane.showMessageDialog(null, "Please create a Journal.");
                String key = accounting.toString()+ AccountingActionListener.JOURNAL_MANAGEMENT;
                AccountingComponentMap.getDisposableComponent(key).setVisible(true);
            } catch (DuplicateNameException e) {
                JOptionPane.showMessageDialog(null, "There is already an accounting with the name \""+name+"\".\r\n"+
                        "Please provide a new name.");
            } catch (EmptyNameException e) {
                JOptionPane.showMessageDialog(null, "The name cannot be empty.\r\nPlease provide a new name.");
            }
            AccountingComponentMap.refreshAllFrames();
        } else if(actionCommand.startsWith(AccountingActionListener.OPEN_ACCOUNTING)){
            String accountingName = actionCommand.replaceAll(AccountingActionListener.OPEN_ACCOUNTING, "");
            accountings.setCurrentObject(accountingName);
            AccountingComponentMap.refreshAllFrames();
        } else if(actionCommand.equals(AccountingActionListener.JOURNAL_DETAILS)){
            Accounting accounting = accountings.getCurrentObject();
            Journal journal = accounting.getJournals().getCurrentObject();
            String key = AccountingActionListener.JOURNAL_DETAILS + accounting.toString() + journal.toString();
            DisposableComponent gui = AccountingComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new JournalDetails(journal, accounting);
                AccountingComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(actionCommand.equals(AccountingActionListener.ACCOUNT_DETAILS)){
            Accounting accounting = accountings.getCurrentObject();
            Account account = accounting.getAccounts().getCurrentObject();
            String key = accounting.toString() + AccountingActionListener.ACCOUNT_DETAILS + account.getName();
            DisposableComponent gui = AccountingComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new AccountDetails(account, accounting);
                AccountingComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else {
            String key = accountings.getCurrentObject().toString() + actionCommand;
            AccountingComponentMap.getDisposableComponent(key).setVisible(true);
        }
    }
}
