package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.math.BigDecimal;

import static be.dafke.BasicAccounting.Accounts.AccountManagementGUI.showAccountManager;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsTablePopupMenu extends JPopupMenu {
    private final JMenuItem manage, add, debit, credit, details;
    private final JTable table;

    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;
    private JournalInputGUI journalInputGUI;

    public AccountsTablePopupMenu(JTable table, JournalInputGUI journalInputGUI) {
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
            showAccountManager(accounts, accountTypes);
            setVisible(false);
        });
        add.addActionListener(e -> {
            new NewAccountGUI(accounts, accountTypes).setVisible(true);
            setVisible(false);
        });
        details.addActionListener(e -> {
            for(int i: table.getSelectedRows()){
                Account account = accounts.getBusinessObjects().get(i);
                AccountDetails.getAccountDetails(account, journals, journalInputGUI);
            }
            setVisible(false);
        });
    }

    public void book(boolean debit) {
        for(int i: table.getSelectedRows()){
            Account account = accounts.getBusinessObjects().get(i);
            if (account != null) {
                BigDecimal amount = journalInputGUI.askAmount(account, debit);
                if (amount != null) {
                    journalInputGUI.addBooking(new Booking(account, amount, debit));
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