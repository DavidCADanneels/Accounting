package be.dafke.Balances.Actions;

import be.dafke.Balances.GUI.BalanceGUI;
import be.dafke.Balances.Objects.Balance;
import be.dafke.Balances.Objects.Balances;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class BalanceActionListener implements ActionListener {
    private Accountings accountings;
    private String balanceName;

    public BalanceActionListener(Accountings accountings, String balanceName) {
        this.accountings=accountings;
        this.balanceName=balanceName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        BusinessCollection<BusinessObject> balances = accounting.getBusinessObject(Balances.BALANCES);
        String key = accounting.toString() + balanceName;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            Balance balance = (Balance)balances.getBusinessObject(balanceName);
            gui = new BalanceGUI(accounting, balance);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}