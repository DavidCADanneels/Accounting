package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

abstract class OrdersOverviewDataTableModel <T> extends SelectableTableModel<T> {
    static int ORDER_NR_COL = 0
    static int DATE_COL = 1
    static int CONTACT_COL = 2
    static int PRICE_TOTAL_EXCL_COL = 3
    static int VAT_AMOUNT_COL = 4
    static int PRICE_TOTAL_INCL_COL = 5
    static int NR_OF_COL = 6
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()

    OrdersOverviewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    protected void setColumnClasses() {
        columnClasses.put(ORDER_NR_COL, String.class)
        columnClasses.put(DATE_COL, String.class)
        columnClasses.put(CONTACT_COL, Contact.class)
        columnClasses.put(PRICE_TOTAL_EXCL_COL, BigDecimal.class)
        columnClasses.put(VAT_AMOUNT_COL, BigDecimal.class)
        columnClasses.put(PRICE_TOTAL_INCL_COL, BigDecimal.class)
    }

    protected void setColumnNames() {
        columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"))
        columnNames.put(DATE_COL, getBundle("Accounting").getString("DELIVERY_DATE"))
        columnNames.put(CONTACT_COL, getBundle("Contacts").getString("CONTACT"))
        columnNames.put(PRICE_TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"))
        columnNames.put(VAT_AMOUNT_COL, getBundle("Accounting").getString("TOTAL_VAT"))
        columnNames.put(PRICE_TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"))
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        0
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // No editable cells !
    }

    @Override
    abstract T getObject(int row, int col)
}

