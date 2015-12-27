package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Coda.StatementTableFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.CounterParties;
import be.dafke.BasicAccounting.Objects.Statements;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class CodaMenu extends JMenu implements ActionListener {
    private final Accountings accountings;
    private JMenuItem movements, counterParties;

    public CodaMenu(Accountings accountings, AccountingMenuBar menuBar){
        this.accountings=accountings;
        movements = new JMenuItem("Show movements");
        movements.addActionListener(this);
        movements.setEnabled(false);
        counterParties = new JMenuItem("Show Counterparties");
        counterParties.addActionListener(this);
        counterParties.setEnabled(false);

        add(movements);
        add(counterParties);
        menuBar.addRefreshableMenuItem(movements);
        menuBar.addRefreshableMenuItem(counterParties);
    }

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        BusinessCollection<BusinessObject> counterParties = accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
        BusinessCollection<BusinessObject> statements = accounting.getBusinessObject(Statements.STATEMENTS);
        String key = accounting.toString() + Statements.STATEMENTS;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new StatementTableFrame(accountings, accounting, (Statements)statements, (CounterParties)counterParties);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
