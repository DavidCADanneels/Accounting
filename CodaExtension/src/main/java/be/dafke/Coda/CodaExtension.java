package be.dafke.Coda;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Coda.Actions.ShowCounterpartiesActionListener;
import be.dafke.Coda.Actions.ShowStatementsActionListener;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.Statements;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:22
 */
public class CodaExtension {
    private static JMenu banking = null;

    public CodaExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(banking == null){
            createMenu(menuBar, accountings);
        }
        for(Accounting accounting: accountings.getBusinessObjects()) {
            new CounterParties(accounting);
            new Statements(accounting);
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
}
