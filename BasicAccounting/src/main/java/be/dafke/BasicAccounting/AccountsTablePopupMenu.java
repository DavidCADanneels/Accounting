package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.JournalDataChangedListener;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsTablePopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem manage, add, debit, credit,details;
    public final String ADD = "add";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private final RefreshableTable<Account> table;
    private ArrayList<JournalDataChangedListener> journalDataChangedListeners = new ArrayList<>();

    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;

    public AccountsTablePopupMenu(RefreshableTable<Account> table) {
        this.table = table;

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        debit = new JMenuItem(getBundle("Accounting").getString("DEBIT"));
        credit = new JMenuItem(getBundle("Accounting").getString("CREDIT"));
        details = new JMenuItem(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        manage.setActionCommand(MANAGE);
        add.setActionCommand(ADD);
        details.setActionCommand(DETAILS);

        debit.addActionListener(e -> {
            for(JournalDataChangedListener journalDataChangedListener : journalDataChangedListeners){
                Transaction transaction = journalDataChangedListener.getTransaction();
                for (Account selectedAccount : table.getSelectedObjects()) {
                    TransactionActions.addBookingToTransaction(selectedAccount, transaction, true);
                }
                journalDataChangedListener.fireJournalDataChanged();
            }
        });
        credit.addActionListener(e -> {
            for(JournalDataChangedListener journalDataChangedListener : journalDataChangedListeners){
                for (Account selectedAccount : table.getSelectedObjects()) {
                    Transaction transaction = journalDataChangedListener.getTransaction();
                    TransactionActions.addBookingToTransaction(selectedAccount, transaction, false);
                }
                journalDataChangedListener.fireJournalDataChanged();
            }
        });

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

    public void addAddBookingListener(JournalDataChangedListener journalDataChangedListener){
        journalDataChangedListeners.add(journalDataChangedListener);
    }

    public void setAccounting(Accounting accounting) {
        accounts = accounting.getAccounts();
        accountTypes = accounting.getAccountTypes();
        journals = accounting.getJournals();
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
