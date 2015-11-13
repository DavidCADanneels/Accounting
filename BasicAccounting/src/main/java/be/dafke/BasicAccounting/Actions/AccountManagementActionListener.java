package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AccountManagementActionListener implements ActionListener{
    private Accountings accountings;
    public static final String ACCOUNT_MANAGEMENT = "AccountManagement";

    public AccountManagementActionListener(Accountings accountings) {
        this.accountings=accountings;
    }

    public void actionPerformed(ActionEvent ae) {
        Accounting accounting = accountings.getCurrentObject();
        String key = accounting.toString() + ACCOUNT_MANAGEMENT;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
