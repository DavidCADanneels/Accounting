package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static be.dafke.BusinessActions.ActionUtils.TRANSACTION_REMOVED;
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
                Journal oldJournal = transaction.getJournal();

                Journal newJournal = getNewJournal(transaction, journals);
                if(newJournal!=null) { // e.g. when Cancel has been clicked
                    TransactionActions.moveTransaction(transaction, oldJournal, newJournal);
                    Main.fireJournalDataChanged(oldJournal);
                    Main.fireJournalDataChanged(newJournal);
                    for (Account account : transaction.getAccounts()) {
                        Main.fireAccountDataChanged(account);
                    }

                    ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_MOVED, oldJournal.getName(), newJournal.getName());
                }
            } else if (e.getSource() == edit) {
                Journal journal = transaction.getJournal();

                TransactionActions.deleteTransaction(transaction);
                Main.fireJournalDataChanged(journal);
                for (Account account : transaction.getAccounts()) {
                    Main.fireAccountDataChanged(account);
                }

                Main.setTransaction(transaction);
                journal.setCurrentObject(transaction);
                Main.setJournal(journal);

                ActionUtils.showErrorMessage(TRANSACTION_REMOVED,journal.getName());
            } else if (e.getSource() == delete) {
                Journal journal = transaction.getJournal();

                TransactionActions.deleteTransaction(transaction);
                Main.fireJournalDataChanged(journal);
                for (Account account : transaction.getAccounts()) {
                    Main.fireAccountDataChanged(account);
                }

                ActionUtils.showErrorMessage(TRANSACTION_REMOVED, journal.getName());
            }
        }
    }

    private Journal getNewJournal(Transaction transaction, Journals journals){
        Journal journal = transaction.getJournal();
        ArrayList<Journal> dagboeken = journals.getAllJournalsExcept(journal);
        Object[] lijst = dagboeken.toArray();
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("BusinessActions").getString("CHOOSE_JOURNAL"),
                getBundle("BusinessActions").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
        if(keuze!=JOptionPane.CANCEL_OPTION && keuze!=JOptionPane.CLOSED_OPTION){
            return (Journal) lijst[keuze];
        }else return null;
    }
}
