package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;


/**
 * Created by ddanneel on 14/02/2015.
 */
public class GUIActions {
    public static void showAccountManager(Accounts accounts, AccountTypes accountTypes) {
        String key = ""+accounts.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounts, accountTypes);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void showJournalManager(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes) {
        String key = "" + journals.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes, accounts, accountTypes);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void showJournalTypeManager(JournalTypes journalTypes, AccountTypes accountTypes) {
        String key = "" + accountTypes.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if(gui == null){
            gui = new JournalTypeManagementGUI(journalTypes, accountTypes);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
