package be.dafke.Mortgage.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.Mortgage.GUI.MortgageGUI;
import be.dafke.Mortgage.Objects.Mortgages;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class ShowMortgagesActionListener implements ActionListener{
    private Accountings accountings;

    public ShowMortgagesActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject(Mortgages.MORTGAGES);
        String key = Mortgages.MORTGAGES;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new MortgageGUI(accountings, accounting, (Mortgages)mortgages);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
