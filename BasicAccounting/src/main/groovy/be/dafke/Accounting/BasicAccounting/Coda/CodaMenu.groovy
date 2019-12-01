package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.CounterParties
import be.dafke.Accounting.BusinessModel.Statements

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class CodaMenu extends JMenu {
    private JMenuItem movementsItem, counterPartiesItem
    private CounterParties counterParties
    private Statements statements

    CodaMenu(){
        super(getBundle("Accounting").getString("CODA"))
        movementsItem = new JMenuItem("Show movements")
        movementsItem.addActionListener({ e -> StatementTableFrame.showStatements(statements, counterParties).setVisible(true) })
        movementsItem.setEnabled(false)
        counterPartiesItem = new JMenuItem("Show Counterparties")
        counterPartiesItem.addActionListener({ e -> StatementTableFrame.showStatements(statements, counterParties).setVisible(true) })
        counterPartiesItem.setEnabled(false)

        add(movementsItem)
        add(counterPartiesItem)
    }

    void setAccounting(Accounting accounting) {
        counterParties = accounting==null?null:accounting.getCounterParties()
        statements = accounting==null?null:accounting.getStatements()
        counterPartiesItem.setEnabled(counterParties!=null)
        movementsItem.setEnabled(statements!=null)
    }
}