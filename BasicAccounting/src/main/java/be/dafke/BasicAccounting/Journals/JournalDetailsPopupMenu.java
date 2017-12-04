package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, details;
    private JournalInputGUI journalInputGUI;

    private SelectableTable<Booking> gui;
    private Journals journals;

    public JournalDetailsPopupMenu(Journals journals, SelectableTable<Booking> gui, JournalInputGUI journalInputGUI) {
        this(gui, journalInputGUI);
        this.journals=journals;
    }

    public JournalDetailsPopupMenu(SelectableTable<Booking> gui, JournalInputGUI journalInputGUI) {
        this.gui = gui;
        this.journalInputGUI=journalInputGUI;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        delete.addActionListener(e -> deleteTransaction());
        move.addActionListener(e -> moveTransaction());
        edit.addActionListener(e -> editTransaction());
        details.addActionListener(e -> showDetails());

        add(delete);
        add(move);
        add(edit);
        add(details);
    }

    public void setJournals(Journals journals){
        this.journals=journals;
    }

    private void moveTransaction() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        Set<Transaction> transactions = journalInputGUI.getTransactions(bookings);
        journalInputGUI.moveTransaction(transactions, journals);
    }

    private void deleteTransaction() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        Set<Transaction> transactions = journalInputGUI.getTransactions(bookings);
        journalInputGUI.deleteTransaction(transactions);
    }

    private void editTransaction() {
        setVisible(false);
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        journalInputGUI.editTransaction(transaction);
    }

    private void showDetails() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Account account = booking.getAccount();
            AccountDetails newGui = AccountDetails.getAccountDetails(account, journals, journalInputGUI);
            newGui.selectObject(booking);
        }
    }

}
