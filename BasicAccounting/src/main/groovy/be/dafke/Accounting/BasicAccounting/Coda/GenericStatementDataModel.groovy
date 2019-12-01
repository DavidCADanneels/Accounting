package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Utils.Utils

import javax.swing.table.AbstractTableModel

class GenericStatementDataModel extends AbstractTableModel {
    private final String[] columnNames = [ "Name", "Date", "D/C", "Amount", "Old CounterParty",
        "New CounterParty", "TransactionCode", "Communication" ]
    private final Class[] columnClasses = [ String.class, Calendar.class, String.class, BigDecimal.class,
        CounterParty.class, TmpCounterParty.class, String.class, String.class ]
    private Statement singleStatement
    private SearchOptions searchOptions
    private Statements statements

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
        if (col == 0) {
            m.getName()
        } else if (col == 1) {
            Utils.toString(m.getDate())
        } else if (col == 2) {
            (m.isDebit()) ? "D" : "C"
        } else if (col == 3) {
            m.getAmount()
        } else if (col == 4) {
            m.getCounterParty()
        } else if (col == 5) {
            m.getTmpCounterParty()
        } else if (col == 6) {
            m.getTransactionCode()
        } else if (col == 7) {
            m.getCommunication()
        } else ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        getAllStatements().size()
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