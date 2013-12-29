package be.dafke.Mortgage.Action;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.Accountings;

import java.awt.event.ActionEvent;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:13
 */
public class MortgageActionListener extends AccountingActionListener{

    private final Accountings accountings;

    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE_CALCULATOR = "MortgageCalculator";
    public static final String MORTGAGE_TABLE = "MortgageTable";

    public MortgageActionListener(Accountings accountings){
        super(accountings);
        this.accountings=accountings;
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        String actionCommand = ae.getActionCommand();
        String key = accountings.getCurrentObject().toString() + actionCommand;
        AccountingComponentMap.getDisposableComponent(key).setVisible(true);
        AccountingComponentMap.refreshAllFrames();
    }
}
