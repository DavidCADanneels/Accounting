package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.GUI.Details.AccountDetails;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableTableFrame;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AccountActions {
    public static void showAccountManager(Accounts accounts, AccountTypes accountTypes) {
        String key = ""+accounts.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounts, accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static RefreshableTableFrame<Booking> showDetails(Account account, Journals journals){
        String key = "Details" + account.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountDetails(account, journals);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return (RefreshableTableFrame<Booking>)gui;
    }
}
