package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BasicAccounting.Accounts.AccountManagementGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Journals.JournalDetails;
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

    public static AccountDetails showDetails(Account account, Journals journals){
        String key = "Details" + account.hashCode();
        AccountDetails gui = (AccountDetails)ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountDetails(account, journals);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return gui;
    }

    public static void showBalance(Journals journals, Balance balance) {
        String key = ""+balance.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new BalanceGUI(journals, balance);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void showTestBalance(Journals journals, Accounts accounts, AccountTypes accountTypes) {
        String key = journals.hashCode()+""+accounts.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new TestBalance(journals, accounts, accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void showJournalManager(Journals journals, JournalTypes journalTypes) {
        String key = "" + journals.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes);
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

    public static JournalDetails showDetails(Journal journal, Journals journals){
        String key = "Details" + journal.hashCode();
        JournalDetails gui = (JournalDetails)ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalDetails(journal, journals);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return gui;
    }

    public static Journal switchJournal(Accounts accounts, Journal oldJournal, Journal newJournal) {
        Journal journal;
        if(newJournal!=null && oldJournal!=null){
            journal = checkTransfer(accounts, oldJournal, newJournal);
        } else {
            journal = newJournal;
        }
        return journal;
//        journals.setCurrentObject(journal);
//        //ComponentMap.refreshAllFrames();
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
                return newJournal;
            } else {
                return oldJournal;
            }
        } else {
            return newJournal;
        }
    }
}
