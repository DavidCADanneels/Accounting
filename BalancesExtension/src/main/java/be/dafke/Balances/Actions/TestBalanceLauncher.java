package be.dafke.Balances.Actions;

import be.dafke.Balances.GUI.TestBalance;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class TestBalanceLauncher {
    public static final String TEST_BALANCE = "TestBalance";

    public void showBalance(Accounting accounting) {
        String key = TEST_BALANCE;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new TestBalance(accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}