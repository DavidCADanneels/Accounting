package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountActions;
import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalGUIPopupMenu extends JPopupMenu{
    private final JMenuItem delete, edit, change, debitCredit, details;
    private final SelectableTable<Booking> table;
    private Accounts accounts;
    private Journals journals;
    private AccountTypes accountTypes;

    public JournalGUIPopupMenu(SelectableTable<Booking> table) {
        this.table = table;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_AMOUNT"));
        change = new JMenuItem(getBundle("Accounting").getString("CHANGE_ACCOUNT"));
        debitCredit = new JMenuItem(getBundle("Accounting").getString("D_C"));
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        add(details);
        delete.addActionListener(e -> deleteBooking());
        edit.addActionListener(e -> editAmount());
        change.addActionListener(e -> changeAccount());
        debitCredit.addActionListener(e -> switchDebitCredit());
        details.addActionListener(e -> showDetails());
        add(delete);
        add(edit);
        add(change);
        add(debitCredit);
    }

    public void setAccounting(Accounting accounting){
        accounts = accounting==null?null:accounting.getAccounts();
        journals = accounting==null?null:accounting.getJournals();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
    }


    private void deleteBooking() {
        setVisible(false);
        ArrayList<Booking> bookings = table.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            transaction.removeBusinessObject(booking);
            Main.fireTransactionInputDataChanged();
        }
    }

    private void editAmount() {
        setVisible(false);
        ArrayList<Booking> bookings = table.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            Account account = booking.getAccount();
            //TODO: or JournalGUI.table should contain Movements iso Bookings
            // booking must be removed and re-added to Transaction to re-calculate the totals
            transaction.removeBusinessObject(booking);
            BigDecimal amount = AccountActions.askAmount(account, booking.isDebit(),transaction);
            if (amount != null) {
                booking.setAmount(amount);
            }
            transaction.addBusinessObject(booking);
            Main.fireTransactionInputDataChanged();
        }
    }

    private void switchDebitCredit() {
        setVisible(false);
        ArrayList<Booking> bookings = table.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            // booking must be removed and re-added to Transaction to re-calculate the totals
            transaction.removeBusinessObject(booking);
            booking.setDebit(!booking.isDebit());
            transaction.addBusinessObject(booking);
            Main.fireTransactionInputDataChanged();
        }
    }

    private void changeAccount() {
        setVisible(false);
        ArrayList<Booking> bookings = table.getSelectedObjects();
        for (Booking booking : bookings) {
            AccountSelectorDialog sel = AccountSelectorDialog.getAccountSelector(accounts, accountTypes.getBusinessObjects());
            sel.setVisible(true);
            Account account = sel.getSelection();
            if (account != null) {
                booking.setAccount(account);
            }
            Main.fireTransactionInputDataChanged();
        }
    }

    private void showDetails() {
        setVisible(false);
        ArrayList<Booking> bookings = table.getSelectedObjects();
        for (Booking booking : bookings) {
            Account account = booking.getAccount();
            AccountDetailsGUI.getAccountDetails(account, journals);
        }
    }
}
