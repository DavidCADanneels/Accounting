package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.*;
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
    private final AccountDetailsLauncher accountDetailsLauncher;
    private final JournalDetailsLauncher journalDetailsLauncher;

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
        delete.addActionListener(new DeleteTransactionActionListener(gui));
        move.addActionListener(new MoveTransactionActionListener(accounting.getJournals(), gui));
        edit.addActionListener(new EditTransactionActionListener(accounting.getJournals(), gui));
        accountDetailsLauncher = new AccountDetailsLauncher();
        journalDetailsLauncher = new JournalDetailsLauncher();
        add(delete);
        add(move);
        add(edit);
        add(details);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        RefreshableTableFrame<Booking> newGui;
        if(e.getSource() == details){
            Booking booking = gui.getSelectedObject();
            if(mode == Mode.JOURNAL) {
                newGui = accountDetailsLauncher.showDetails(accounting, booking.getAccount());
            } else {
                newGui = journalDetailsLauncher.showDetails(accounting, booking.getTransaction().getJournal());
            }
            newGui.selectObject(booking);
        }
    }
}
