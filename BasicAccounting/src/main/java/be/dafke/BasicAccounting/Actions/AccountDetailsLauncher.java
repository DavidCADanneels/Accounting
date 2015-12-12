package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Details.AccountDetails;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableTableFrame;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AccountDetailsLauncher {
    private static final String ACCOUNT_DETAILS = "AccountDetails";

    public RefreshableTableFrame<Booking> showDetails(Accounting accounting, Account account){
        String key = ACCOUNT_DETAILS + account.getName();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountDetails(account, accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return (RefreshableTableFrame<Booking>)gui;
    }
}
