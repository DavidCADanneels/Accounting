package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Transaction;
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
    private Accounting accounting;
    private RefreshableTable<Booking> gui;
    private final AccountDetailsLauncher accountDetailsLauncher = new AccountDetailsLauncher();
    private final JournalDetailsLauncher journalDetailsLauncher = new JournalDetailsLauncher();
    private final MoveTransactionLauncher moveTransactionLauncher = new MoveTransactionLauncher();
    private final EditTransactionLauncher editTransactionLauncher = new EditTransactionLauncher();
    private final DeleteTransactionLauncher deleteTransactionLauncher = new DeleteTransactionLauncher();

    public DetailsPopupMenu(Accounting accounting, RefreshableTable<Booking> gui, Mode mode) {
        this.mode = mode;
        this.gui = gui;
        this.accounting = accounting;
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

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        RefreshableTableFrame<Booking> newGui;
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        Journals journals = accounting.getJournals();
        if(e.getSource() == details){
            if(mode == Mode.JOURNAL) {
                Account account = booking.getAccount();
                newGui = accountDetailsLauncher.showDetails(accounting, account);
            } else {
                Journal journal = transaction.getJournal();
                newGui = journalDetailsLauncher.showDetails(accounting, journal);
            }
            newGui.selectObject(booking);
        } else if (e.getSource() == move){
            moveTransactionLauncher.moveTransaction(transaction, journals);
        } else if (e.getSource() == edit){
            editTransactionLauncher.editTransaction(transaction, journals);
        } else if (e.getSource() == delete){
            deleteTransactionLauncher.deleteTransaction(transaction);
        }
    }
}
