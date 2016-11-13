package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BasicAccounting.Journals.JournalDetails;
import be.dafke.BasicAccounting.MainApplication.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.util.ArrayList;

import static be.dafke.BusinessActions.ActionUtils.TRANSACTION_REMOVED;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class DetailsPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, details;
    private JournalInputGUI journalInputGUI;

    public enum Mode{ JOURNAL, ACCOUNT}
    private Mode mode;
    private RefreshableTable<Booking> gui;
    private Journals journals;

    public DetailsPopupMenu(Journals journals, RefreshableTable<Booking> gui, JournalInputGUI journalInputGUI, Mode mode) {
        this(gui, mode, journalInputGUI);
        this.journals=journals;
    }

    public DetailsPopupMenu(RefreshableTable<Booking> gui, Mode mode, JournalInputGUI journalInputGUI) {
        this.mode = mode;
        this.gui = gui;
        this.journalInputGUI=journalInputGUI;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        if(mode == Mode.JOURNAL) {
            details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        }
        else{
            details = new JMenuItem(getBundle("Accounting").getString("GO_TO_JOURNAL_DETAILS"));
        }
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
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
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
        }
    }

    private void deleteTransaction() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            Journal journal = transaction.getJournal();

            TransactionActions.deleteTransaction(transaction);
            Main.fireJournalDataChanged(journal);
            for (Account account : transaction.getAccounts()) {
                Main.fireAccountDataChanged(account);
            }

            ActionUtils.showErrorMessage(TRANSACTION_REMOVED, journal.getName());
        }
    }

    private void editTransaction() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            Journal journal = transaction.getJournal();

            TransactionActions.deleteTransaction(transaction);
            Main.fireJournalDataChanged(journal);
            for (Account account : transaction.getAccounts()) {
                Main.fireAccountDataChanged(account);
            }
            //TODO: GUI with question where to open the transaction? (only usefull if multiple input GUIs are open)
            // set Journal before Transaction: setJournal sets transaction to currentObject !!!
            Main.setJournal(journal);
            journal.setCurrentObject(transaction);
            // TODO: when calling setTransaction we need to check if the currentTransaction is empty (see switchJournal() -> checkTransfer)
            journalInputGUI.setTransaction(transaction);

            ActionUtils.showErrorMessage(TRANSACTION_REMOVED,journal.getName());
        }
    }

    private void showDetails() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Transaction transaction = booking.getTransaction();
            if (mode == Mode.JOURNAL) {
                Account account = booking.getAccount();
                AccountDetails newGui = Main.getAccountDetails(account, journals);
                newGui.selectObject(booking);
            } else {
                Journal journal = transaction.getJournal();
                JournalDetails newGui = Main.getJournalDetails(journal, journals);;
                newGui.selectObject(booking);
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
