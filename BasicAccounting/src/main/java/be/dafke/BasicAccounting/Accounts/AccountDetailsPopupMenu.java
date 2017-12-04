package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class AccountDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, details;
    private JournalInputGUI journalInputGUI;

    private SelectableTable<Booking> gui;
    private Journals journals;

    public AccountDetailsPopupMenu(Journals journals, SelectableTable<Booking> gui, JournalInputGUI journalInputGUI) {
        this(gui, journalInputGUI);
        this.journals=journals;
    }

    public AccountDetailsPopupMenu(SelectableTable<Booking> gui, JournalInputGUI journalInputGUI) {
        this.gui = gui;
        this.journalInputGUI=journalInputGUI;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_JOURNAL_DETAILS"));
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
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        Set<Transaction> transactions = journalInputGUI.getTransactions(bookings);
        // TODO: allow only 1 transaction for editing
        journalInputGUI.editTransaction(transactions);
    }

    private void showDetails() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            Journal journal = transaction.getJournal();
            JournalDetails newGui = JournalDetails.getJournalDetails(journal, journals, journalInputGUI);
            newGui.selectObject(booking);
        }
    }

}
