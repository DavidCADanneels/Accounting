package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Utils.Utils

import javax.swing.table.AbstractTableModel

class GenericStatementDataModel extends AbstractTableModel {
    final String[] columnNames = [ "Name", "Date", "D/C", "Amount", "Old CounterParty",
        "New CounterParty", "TransactionCode", "Communication" ]
    final Class[] columnClasses = [ String.class, Calendar.class, String.class, BigDecimal.class,
        CounterParty.class, TmpCounterParty.class, String.class, String.class ]
    Statement singleStatement
    SearchOptions searchOptions
    Statements statements

    GenericStatementDataModel(SearchOptions searchOptions, Statements statements) {
        this.statements = statements
        this.searchOptions = searchOptions
    }

    void setSingleStatement(Statement statement) {
        singleStatement = statement
        fireTableDataChanged()
    }

    // DE GET METHODEN
    // ===============
    Object getValueAt(int row, int col) {
        Statement m = getAllStatements().get(row)
        if (col == 0) return m.name
        if (col == 1) return Utils.toString(m.date)
        if (col == 2) return (m.debit) ? "D" : "C"
        if (col == 3) return m.amount
        if (col == 4) return m.getCounterParty()
        if (col == 5) return m.getTmpCounterParty()
        if (col == 6) return m.getTransactionCode()
        if (col == 7) return m.getCommunication()
        return  ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        allStatements.size()
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

    ArrayList<Statement> getAllStatements() {
        if (singleStatement != null) {
            ArrayList<Statement> result = new ArrayList<Statement>()
            result.add(singleStatement)
            result
        }
        statements.getStatements(searchOptions)
    }
}