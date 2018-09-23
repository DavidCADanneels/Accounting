package be.dafke.BasicAccounting.Accounts.AccountDetails;

import be.dafke.BasicAccounting.Journals.JournalDetailsGUI;
import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class AccountDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, details;

    private SelectableTable<Booking> gui;
    private Journals journals;

    public AccountDetailsPopupMenu(Journals journals, SelectableTable<Booking> gui) {
        this(gui);
        this.journals=journals;
    }

    public AccountDetailsPopupMenu(SelectableTable<Booking> gui) {
        this.gui = gui;
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
        Main.moveBookings(bookings, journals);
    }

    private void deleteTransaction() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        Main.deleteBookings(bookings);
    }

    private void editTransaction() {
        setVisible(false);
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        Main.editTransaction(transaction);
    }

    private void showDetails() {
        Point locationOnScreen = getLocationOnScreen();
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            Journal journal = transaction.getJournal();
            JournalDetailsGUI newGui = JournalDetailsGUI.getJournalDetails(locationOnScreen, journal, journals);
            newGui.selectObject(booking);
        }
    }

}
