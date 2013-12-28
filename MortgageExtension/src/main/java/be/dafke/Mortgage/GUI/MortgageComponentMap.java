package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;

import java.awt.event.ActionListener;

/**
 * User: david
 * Date: 28-12-13
 * Time: 11:31
 */
public class MortgageComponentMap extends ComponentMap {
    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE_CALCULATOR = "MortgageCalculator";
    public static final String MORTGAGE_TABLE = "MortgageTable";


    public static void addAccountingComponents(Accounting accounting, ActionListener actionListener){
        addDisposableComponent(accounting.toString() + MORTGAGES, new MortgageGUI(accounting, actionListener));
    }
}
