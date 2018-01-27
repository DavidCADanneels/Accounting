package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI.showAccountManager;
import static be.dafke.BasicAccounting.Balances.TestBalance.getTestBalance;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class AccountsMenu extends JMenu {
    private static JMenuItem add, manage, testBalance;

    private Journals journals;
    private Accounts accounts;
    private AccountTypes accountTypes;

    public AccountsMenu(JournalInputGUI journalInputGUI) {
        super(getBundle("BusinessModel").getString("ACCOUNTS"));
//        setMnemonic(KeyEvent.VK_P);
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        add.addActionListener(e -> new NewAccountGUI(accounts,accountTypes.getBusinessObjects()).setVisible(true));
        add.setEnabled(false);

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        manage.addActionListener(e -> showAccountManager(accounts, accountTypes.getBusinessObjects()).setVisible(true));
        manage.setEnabled(false);

        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"));
        testBalance.addActionListener(e -> getTestBalance(journals, accounts, journalInputGUI));
        testBalance.setEnabled(false);

        add(add);
        add(manage);
        add(testBalance);
    }

    public void setAccounting(Accounting accounting) {
        journals = accounting==null?null:accounting.getJournals();
        accounts = accounting==null?null:accounting.getAccounts();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
        add.setEnabled(accounts!=null);
        manage.setEnabled(accounts!=null);
        testBalance.setEnabled(accounts!=null);
    }
}
