package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BasicAccounting.MainApplication.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsTablePopupMenu extends JPopupMenu {
    private final JMenuItem manage, add, debit, credit, details;
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

        add(debit);
        add(credit);
        add(details);
        addSeparator();
        add(add);
        add(manage);

        debit.addActionListener(e -> book(true));
        credit.addActionListener(e -> book(false));
        manage.addActionListener(e -> {
            GUIActions.showAccountManager(accounts, accountTypes);
            setVisible(false);
        });
        add.addActionListener(e -> {
            new NewAccountGUI(accounts, accountTypes).setVisible(true);
            setVisible(false);
        });
        details.addActionListener(e -> {
            ArrayList<Account> selectedAccounts = table.getSelectedObjects();
            for(Account selectedAccount:selectedAccounts) {
                Main.getAccountDetails(selectedAccount, journals);
            }
            setVisible(false);
        });
    }

    public void book(boolean debit) {
        for (Account selectedAccount : table.getSelectedObjects()) {
            if (selectedAccount != null) {
                BigDecimal amount = journalInputGUI.askAmount(selectedAccount, debit);
                if (amount != null) {
                    journalInputGUI.addBooking(new Booking(selectedAccount, amount, debit));
                }
            }
        }
        setVisible(false);
    }

    public void setAccounting(Accounting accounting) {
        setAccounts(accounting == null ? null : accounting.getAccounts());
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
        setJournals(accounting == null ? null : accounting.getJournals());
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }
}