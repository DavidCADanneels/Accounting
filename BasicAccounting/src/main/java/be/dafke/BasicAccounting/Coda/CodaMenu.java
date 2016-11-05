package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.CounterParties;
import be.dafke.BusinessModel.Statements;
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
public class CodaMenu extends JMenu implements ActionListener, AccountingListener {
    private JMenuItem movementsItem, counterPartiesItem;
    private BusinessCollection<BusinessObject> counterParties, statements;

    public CodaMenu(){
        movementsItem = new JMenuItem("Show movements");
        movementsItem.addActionListener(this);
        movementsItem.setEnabled(false);
        counterPartiesItem = new JMenuItem("Show Counterparties");
        counterPartiesItem.addActionListener(this);
        counterPartiesItem.setEnabled(false);

        add(movementsItem);
        add(counterPartiesItem);
    }

    public void actionPerformed(ActionEvent e) {
        String key = Statements.STATEMENTS + statements.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new StatementTableFrame((Statements)statements, (CounterParties)counterParties);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    @Override
    public void setAccounting(Accounting accounting) {
        counterParties = accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
        statements = accounting.getBusinessObject(Statements.STATEMENTS);
        counterPartiesItem.setEnabled(counterParties!=null);
        movementsItem.setEnabled(statements!=null);
    }
}
