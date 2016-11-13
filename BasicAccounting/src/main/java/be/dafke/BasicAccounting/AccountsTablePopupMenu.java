package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BasicAccounting.MainApplication.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsTablePopupMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem manage, add, debit, credit,details;
    public final String ADD = "add";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private final RefreshableTable<Account> table;

    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;
    private JournalInputGUI journalInputGUI;

    public AccountsTablePopupMenu(RefreshableTable<Account> table, JournalInputGUI journalInputGUI) {
        this.table = table;
        this.journalInputGUI = journalInputGUI;

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        debit = new JMenuItem(getBundle("Accounting").getString("DEBIT"));
        credit = new JMenuItem(getBundle("Accounting").getString("CREDIT"));
        details = new JMenuItem(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        manage.setActionCommand(MANAGE);
        add.setActionCommand(ADD);
        details.setActionCommand(DETAILS);

        debit.addActionListener(e -> {book(true);});
        credit.addActionListener(e -> {book(false);});

        add(debit);
        add(credit);
        add(details);
        addSeparator();
        add(add);
        add(manage);

        manage.addActionListener(this);
        add.addActionListener(this);
        debit.addActionListener(this);
        credit.addActionListener(this);
        details.addActionListener(this);
    }

    public void book(boolean debit){
        for (Account selectedAccount : table.getSelectedObjects()) {
            if(selectedAccount!=null){
                BigDecimal amount = journalInputGUI.askAmount(selectedAccount,debit);
                if(amount!=null) {
                    journalInputGUI.addBooking(new Booking(selectedAccount, amount, debit));
                }
            }
        }
    }

    public void setAccounting(Accounting accounting) {
        setAccounts(accounting==null?null:accounting.getAccounts());
        setAccountTypes(accounting==null?null:accounting.getAccountTypes());
        setJournals(accounting==null?null:accounting.getJournals());
    }

    public void setAccountTypes(AccountTypes accountTypes){
        this.accountTypes = accountTypes;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setJournals(Journals journals){
        this.journals = journals;
    }

    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        ArrayList<Account> selectedAccounts = table.getSelectedObjects();
        if (MANAGE.equals(actionCommand)) {
            GUIActions.showAccountManager(accounts, accountTypes);
        } else if (DETAILS.equals(actionCommand)) {
            for(Account selectedAccount:selectedAccounts) {
                Main.getAccountDetails(selectedAccount, journals);
            }
        } else if (ADD.equals(actionCommand)) {
            new NewAccountGUI(accounts, accountTypes).setVisible(true);
        }
        setVisible(false);
    }
}
