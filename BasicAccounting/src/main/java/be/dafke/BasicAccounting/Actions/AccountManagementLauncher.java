package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AccountManagementLauncher {
    public static final String ACCOUNT_MANAGEMENT = "AccountManagement";

    public void showAccountManager(Accounting accounting) {
        String key = ACCOUNT_MANAGEMENT;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
