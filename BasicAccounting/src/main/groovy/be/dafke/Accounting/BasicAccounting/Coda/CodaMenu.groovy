package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.CounterParties
import be.dafke.Accounting.BusinessModel.Statements

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class CodaMenu extends JMenu {
    JMenuItem movementsItem, counterPartiesItem
    CounterParties counterParties
    Statements statements

    CodaMenu(){
        super(getBundle("Accounting").getString("CODA"))
        movementsItem = new JMenuItem("Show movements")
        movementsItem.addActionListener({ e -> StatementTableFrame.showStatements(statements, counterParties).visible = true })
        movementsItem.enabled = false
        counterPartiesItem = new JMenuItem("Show Counterparties")
        counterPartiesItem.addActionListener({ e -> StatementTableFrame.showStatements(statements, counterParties).visible = true })
        counterPartiesItem.enabled = false

        add(movementsItem)
        add(counterPartiesItem)
    }

    void setAccounting(Accounting accounting) {
        counterParties = accounting?accounting.counterParties:null
        statements = accounting?accounting.statements:null
        counterPartiesItem.enabled  =counterParties!=null
        movementsItem.enabled = statements!=null
    }
}