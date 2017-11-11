package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.CounterParties;
import be.dafke.BusinessModel.Statements;

import javax.swing.*;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class CodaMenu extends JMenu {
    private JMenuItem movementsItem, counterPartiesItem;
    private CounterParties counterParties;
    private Statements statements;

    public CodaMenu(){
        movementsItem = new JMenuItem("Show movements");
        movementsItem.addActionListener(e -> StatementTableFrame.showStatements(statements,counterParties).setVisible(true));
        movementsItem.setEnabled(false);
        counterPartiesItem = new JMenuItem("Show Counterparties");
        counterPartiesItem.addActionListener(e -> StatementTableFrame.showStatements(statements,counterParties).setVisible(true));
        counterPartiesItem.setEnabled(false);

        add(movementsItem);
        add(counterPartiesItem);
    }

    public void setAccounting(Accounting accounting) {
        counterParties = accounting==null?null:accounting.getCounterParties();
        statements = accounting==null?null:accounting.getStatements();
        counterPartiesItem.setEnabled(counterParties!=null);
        movementsItem.setEnabled(statements!=null);
    }
}
