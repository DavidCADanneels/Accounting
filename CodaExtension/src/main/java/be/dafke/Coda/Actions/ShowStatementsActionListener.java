package be.dafke.Coda.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Coda.GUI.StatementTable;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class ShowStatementsActionListener implements ActionListener{
    private Accountings accountings;

    public ShowStatementsActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        BusinessCollection<BusinessObject> counterParties = accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
        BusinessCollection<BusinessObject> statements = accounting.getBusinessObject(Statements.STATEMENTS);
        String key = accounting.toString() + Statements.STATEMENTS;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new StatementTable(accountings, accounting, (Statements)statements, (CounterParties)counterParties);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
