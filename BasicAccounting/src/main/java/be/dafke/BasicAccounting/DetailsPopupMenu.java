package be.dafke.BasicAccounting;

import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        this.mode = mode;
        this.gui = gui;
        this.journals = journals;
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

    public void setAccounting(Accounting accounting){
        if (accounting==null){
            journals = null;
        } else {
            journals = accounting.getJournals();
        }
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        RefreshableTableFrame<Booking> newGui;
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        if(e.getSource() == details){
            if(mode == Mode.JOURNAL) {
                Account account = booking.getAccount();
                newGui = GUIActions.showDetails(account, journals);
            } else {
                Journal journal = transaction.getJournal();
                newGui = GUIActions.showDetails(journal, journals);
            }
            newGui.selectObject(booking);
        } else if (e.getSource() == move){
            TransactionActions.moveTransaction(transaction, journals);
        } else if (e.getSource() == edit){
            TransactionActions.editTransaction(transaction, journals);
        } else if (e.getSource() == delete){
            TransactionActions.deleteTransaction(transaction);
        }
    }
}