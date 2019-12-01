package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.CounterParty
import be.dafke.Accounting.BusinessModel.Statement
import be.dafke.Accounting.BusinessModel.Statements
import be.dafke.Utils.Utils

import javax.swing.table.AbstractTableModel

class StatementDataModel extends AbstractTableModel {
    private final String[] columnNames = [ "Name", "Date", "D/C", "Amount", "CounterParty",
        "TransactionCode", "Communication" ]
    private final Class[] columnClasses = [ String.class, String.class, String.class, BigDecimal.class,
        CounterParty.class, String.class, String.class ]
    private final Statements statements

    StatementDataModel(Statements statements) {
        this.statements = statements
    }

    // DE GET METHODEN
    // ===============
    Object getValueAt(int row, int col) {
        Statement m = (Statement)statements.getBusinessObjects().get(row)
        if (col == 0) {
            m.getName()
        } else if (col == 1) {
            Utils.toString(m.getDate())
        } else if (col == 2) {
            (m.isDebit()) ? "(D) -" : "(C) +"
        } else if (col == 3) {
            m.getAmount()
        } else if (col == 4) {
            m.getCounterParty()
        } else if (col == 5) {
            m.getTransactionCode()
        } else if (col == 6) {
            m.getCommunication()
        } else ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        statements.getBusinessObjects().size()
    }

    @Override
    String getColumnName(int col) {
        columnNames[col]
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses[col]
    }

    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
    }
}