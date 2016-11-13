package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalManagementGUI;
import be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

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
}
