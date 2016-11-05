package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class DetailsPopupMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem move, delete, edit, details;
    public enum Mode{ JOURNAL, ACCOUNT}
    private Mode mode;
    private RefreshableTable<Booking> gui;
    private Journals journals;

    public DetailsPopupMenu(Journals journals, RefreshableTable<Booking> gui, Mode mode) {
        this(gui, mode);
        this.journals=journals;
    }

    public DetailsPopupMenu(RefreshableTable<Booking> gui, Mode mode) {
        this.mode = mode;
        this.gui = gui;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        if(mode == Mode.JOURNAL) {
            details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        }
        else{
            details = new JMenuItem(getBundle("Accounting").getString("GO_TO_JOURNAL_DETAILS"));
        }
        delete.addActionListener(this);
        move.addActionListener(this);
        edit.addActionListener(this);
        details.addActionListener(this);

        add(delete);
        add(move);
        add(edit);
        add(details);
    }

    public void setJournals(Journals journals){
        this.journals=journals;
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            if (e.getSource() == details) {
                if (mode == Mode.JOURNAL) {
                    Account account = booking.getAccount();
                    AccountDetails newGui = Main.getAccountDetails(account, journals);
                    newGui.selectObject(booking);
                } else {
                    Journal journal = transaction.getJournal();
                    JournalDetails newGui = Main.getJournalDetails(journal, journals);;
                    newGui.selectObject(booking);
                }
            } else if (e.getSource() == move) {
                TransactionActions.moveTransaction(transaction, journals);
                // refresh needed: with other listeners
            } else if (e.getSource() == edit) {
                TransactionActions.editTransaction(transaction, journals);
                Main.setJournal(transaction.getJournal());
            } else if (e.getSource() == delete) {
                TransactionActions.deleteTransaction(transaction);
                // refresh needed: with other listeners
            }
        }
    }
}
