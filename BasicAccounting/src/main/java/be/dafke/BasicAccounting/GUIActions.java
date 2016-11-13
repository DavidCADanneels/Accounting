package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import javax.swing.*;
import java.util.Calendar;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class GUIActions {
    public static void showAccountManager(Accounts accounts, AccountTypes accountTypes) {
        String key = ""+accounts.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounts, accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void showJournalManager(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes) {
        String key = "" + journals.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes, accounts, accountTypes);
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

    public static Journal switchJournal(Accounts accounts, Journal oldJournal, Journal newJournal) {
        Journal journal;
        if(newJournal!=null && oldJournal!=null){
            journal = checkTransfer(accounts, oldJournal, newJournal);
        } else {
            journal = newJournal;
        }
        return journal;
    }

    private static Journal checkTransfer(Accounts accounts, Journal oldJournal, Journal newJournal){
        Transaction oldTransaction = oldJournal.getCurrentObject();
        Transaction newTransaction = newJournal.getCurrentObject();
        if(oldTransaction!=null && !oldTransaction.getBusinessObjects().isEmpty()){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(oldJournal).append(" to ").append(newJournal);
            if(newTransaction!=null && !newTransaction.getBusinessObjects().isEmpty()){
                builder.append("\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer");
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString());
            if(answer == JOptionPane.YES_OPTION){
                newJournal.setCurrentObject(oldTransaction);
                oldJournal.setCurrentObject(new Transaction(accounts, Calendar.getInstance(), ""));
                return newJournal;
            } else if(answer == JOptionPane.NO_OPTION){
                oldJournal.setCurrentObject(oldTransaction);
                return newJournal;
            } else {
                return oldJournal;
            }
        } else {
            return newJournal;
        }
    }
}
