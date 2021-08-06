package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class CustomerOrdersDataModel extends SelectableTableModel<SalesOrder> {

    ArrayList<SalesOrder> salesOrders

    static int SO_COL = 0
    static int SALE_COL = 1
    static int PAY_COL = 2
    static int GAIN_COL = 3
    static int NR_OF_COL = 4

    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    CustomerOrdersDataModel(ArrayList<SalesOrder> salesOrders) {
        this.salesOrders = salesOrders
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(SO_COL, String.class)
        columnClasses.put(SALE_COL, String.class)
        columnClasses.put(PAY_COL, String.class)
        columnClasses.put(GAIN_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(SO_COL, getBundle("Accounting").getString("SO"))
        columnNames.put(SALE_COL, getBundle("Accounting").getString("SALE"))
        columnNames.put(PAY_COL, getBundle("Accounting").getString("PAYMENT"))
        columnNames.put(GAIN_COL, getBundle("Accounting").getString("GAIN"))
    }

    @Override
    SalesOrder getObject(int row, int col) {
        return null
    }

    @Override
    int getRowCount() {
        return salesOrders.size()
    }

    @Override
    int getColumnCount() {
        return NR_OF_COL
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
    Object getValueAt(int row, int col) {
        SalesOrder salesOrder = salesOrders.get(row)
        if(col == SO_COL) {
            salesOrder.toString()
        } else if (col==SALE_COL){
            salesOrder.salesTransaction?.toString()
        } else if (col==PAY_COL){
            salesOrder.paymentTransaction?.toString()
        } else if (col==GAIN_COL){
            salesOrder.gainTransaction?.toString()
        } else null
    }
}
