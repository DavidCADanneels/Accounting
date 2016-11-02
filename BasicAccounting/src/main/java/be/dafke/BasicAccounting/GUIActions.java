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
    public static void showAccountManager(Accounting accounting) {
        String key = ""+accounting.getAccounts().hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounting);
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

    public static void showJournalManager(Accounting accounting) {
        String key = "" + accounting.getJournals().hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(accounting);
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

    public static void switchJournal(Accounts accounts, Journals journals, Journal newJournal) {
        Journal oldJournal = journals.getCurrentObject();
        if(newJournal!=null && oldJournal!=null){
            checkTransfer(accounts, journals, oldJournal, newJournal);
        } else {
            journals.setCurrentObject(newJournal);
        }
        ComponentMap.refreshAllFrames();
    }

    private static void checkTransfer(Accounts accounts, Journals journals, Journal oldJournal, Journal newJournal){
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
