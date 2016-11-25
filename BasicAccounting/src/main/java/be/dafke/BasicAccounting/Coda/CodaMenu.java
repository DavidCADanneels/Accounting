package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.CounterParties;
import be.dafke.BusinessModel.Statements;

import javax.swing.*;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class CodaMenu extends JMenu {
    private static JMenuItem movementsItem, counterPartiesItem;
    private static CounterParties counterParties;
    private static Statements statements;

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

    public static void setAccounting(Accounting accounting) {
        // TODO: integrate Counterparties and Statements (with getters and setters, etc.)
        counterParties = (CounterParties)accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
        statements = (Statements)accounting.getBusinessObject(Statements.STATEMENTS);
        counterPartiesItem.setEnabled(counterParties!=null);
        movementsItem.setEnabled(statements!=null);
    }
}
