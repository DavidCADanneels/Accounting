package be.dafke.Balances.Actions;

import be.dafke.Balances.GUI.BalanceGUI;
import be.dafke.Balances.Objects.Balance;
import be.dafke.Balances.Objects.Balances;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class BalanceLauncher {

    public void showBalance(Accounting accounting, String balanceName) {
        BusinessCollection<BusinessObject> balances = accounting.getBusinessObject(Balances.BALANCES);
        Balance balance = (Balance)balances.getBusinessObject(balanceName);
        String key = ""+balance.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new BalanceGUI(accounting, balance);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}