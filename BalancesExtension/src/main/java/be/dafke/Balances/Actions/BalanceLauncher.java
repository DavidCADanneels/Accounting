package be.dafke.Balances.Actions;

import be.dafke.Balances.GUI.BalanceGUI;
import be.dafke.Balances.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class BalanceLauncher {

    public void showBalance(Journals journals, Balance balance) {
        String key = ""+balance.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new BalanceGUI(journals, balance);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}