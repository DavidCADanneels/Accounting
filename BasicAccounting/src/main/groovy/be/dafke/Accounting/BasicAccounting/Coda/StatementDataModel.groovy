package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.CounterParty
import be.dafke.Accounting.BusinessModel.Statement
import be.dafke.Accounting.BusinessModel.Statements
import be.dafke.Utils.Utils

import javax.swing.table.AbstractTableModel

class StatementDataModel extends AbstractTableModel {
    final String[] columnNames = [ "Name", "Date", "D/C", "Amount", "CounterParty",
        "TransactionCode", "Communication" ]
    final Class[] columnClasses = [ String.class, String.class, String.class, BigDecimal.class,
        CounterParty.class, String.class, String.class ]
    final Statements statements

    StatementDataModel(Statements statements) {
        this.statements = statements
    }

    // DE GET METHODEN
    // ===============
    Object getValueAt(int row, int col) {
        Statement m = (Statement)statements.businessObjects.get(row)
        if (col == 0) return m.name
        if (col == 1) return Utils.toString(m.date)
        if (col == 2) return (m.debit) ? "(D) -" : "(C) +"
        if (col == 3) return m.amount
        if (col == 4) return m.getCounterParty()
        if (col == 5) return m.getTransactionCode()
        if (col == 6) return m.getCommunication()
        return ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        statements.businessObjects.size()
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