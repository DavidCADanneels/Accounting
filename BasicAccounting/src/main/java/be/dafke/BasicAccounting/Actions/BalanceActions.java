package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Balances.BalanceGUI;
import be.dafke.BasicAccounting.GUI.Balances.TestBalance;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

/**
 * Created by ddanneels on 12/12/2015.
 */
public class BalanceActions {
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
}
