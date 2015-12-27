package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.GUI.JournalManagement.JournalManagementGUI;
import be.dafke.BasicAccounting.GUI.JournalManagement.JournalTypeManagementGUI;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.JournalTypes;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.JOptionPane;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalActions {
    public static void showJournalManager(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        String key = "" + journals.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes, accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void showJournalTypeManager(AccountTypes accountTypes) {
        String key = "" + accountTypes.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalTypeManagementGUI(accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static RefreshableTableFrame<Booking> showDetails(Journal journal, Journals journals){
        String key = "Details" + journal.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalDetails(journal, journals);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return (RefreshableTableFrame<Booking>)gui;
    }

    public static void switchJournal(Journals journals, Journal newJournal) {
        Journal oldJournal = journals.getCurrentObject();
        if(newJournal!=null && oldJournal!=null){
            checkTransfer(journals, oldJournal, newJournal);
        } else {
            journals.setCurrentObject(newJournal);
        }
        ComponentMap.refreshAllFrames();
    }

    private static void checkTransfer(Journals journals, Journal oldJournal, Journal newJournal){
        Transaction oldTransaction = oldJournal.getCurrentObject();
        Transaction newTransaction = newJournal.getCurrentObject();
        if(oldTransaction!=null && !oldTransaction.getBusinessObjects().isEmpty()){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(oldJournal).append(" to ").append(newJournal);
            if(newTransaction!=null && !newTransaction.getBusinessObjects().isEmpty()){
                builder.append("\r\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer");
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString());
            if(answer == JOptionPane.YES_OPTION){
                newJournal.setCurrentObject(oldTransaction);
                oldJournal.setCurrentObject(new Transaction());
                journals.setCurrentObject(newJournal);
            } else if(answer == JOptionPane.NO_OPTION){
                journals.setCurrentObject(newJournal);
            } else {
                journals.setCurrentObject(oldJournal);
            }
        } else {
            journals.setCurrentObject(newJournal);
        }
    }
}
