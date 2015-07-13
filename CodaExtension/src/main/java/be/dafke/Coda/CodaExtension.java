package be.dafke.Coda;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Coda.Actions.ShowCounterpartiesActionListener;
import be.dafke.Coda.Actions.ShowStatementsActionListener;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:22
 */
public class CodaExtension implements AccountingExtension{
    private static JMenu banking = null;

    public CodaExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(banking == null){
            createMenu(menuBar, accountings);
        }
    }

    private static void createMenu(AccountingMenuBar menuBar, Accountings accountings) {
        banking = new JMenu("Banking");
        JMenuItem movements = new JMenuItem("Show movements");
        movements.addActionListener(new ShowStatementsActionListener(accountings));
        movements.setEnabled(false);
        JMenuItem counterParties = new JMenuItem("Show Counterparties");
        counterParties.addActionListener(new ShowCounterpartiesActionListener(accountings));
        counterParties.setEnabled(false);

        banking.add(movements);
        banking.add(counterParties);
        menuBar.addRefreshableMenuItem(movements);
        menuBar.addRefreshableMenuItem(counterParties);
        menuBar.add(banking);
    }

    @Override
    public void extendConstructor(Accounting accounting){
        new CounterParties(accounting);
        new Statements(accounting);
    }

    @Override
    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    @Override
    public void extendWriteCollection(Accounting accounting, File xmlFolder){

    }
}
