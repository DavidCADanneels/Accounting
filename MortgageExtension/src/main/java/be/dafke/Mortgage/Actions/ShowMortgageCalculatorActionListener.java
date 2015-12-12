package be.dafke.Mortgage.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.Mortgage.GUI.MortgageCalculatorGUI;
import be.dafke.Mortgage.Objects.Mortgages;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class ShowMortgageCalculatorActionListener implements ActionListener{
    private static final String MORTGAGE_CALCULATOR = "MortgageCalculator";
    private Accountings accountings;

    public ShowMortgageCalculatorActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject(Mortgages.MORTGAGES);
        String key = MORTGAGE_CALCULATOR;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new MortgageCalculatorGUI(accounting, (Mortgages)mortgages);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
    }
}
