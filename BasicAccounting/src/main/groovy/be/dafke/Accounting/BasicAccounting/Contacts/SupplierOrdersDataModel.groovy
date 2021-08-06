package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class SupplierOrdersDataModel extends SelectableTableModel<PurchaseOrder> {

    ArrayList<PurchaseOrder> purchaseOrders

    static int PO_COL = 0
    static int PURCHASE_COL = 1
    static int PAY_COL = 2
    static int NR_OF_COL = 3

    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    SupplierOrdersDataModel(ArrayList<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(PO_COL, String.class)
        columnClasses.put(PURCHASE_COL, String.class)
        columnClasses.put(PAY_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(PO_COL, getBundle("Accounting").getString("PO"))
        columnNames.put(PURCHASE_COL, getBundle("Accounting").getString("PURCHASE"))
        columnNames.put(PAY_COL, getBundle("Accounting").getString("PAYMENT"))
    }


    @Override
    PurchaseOrder getObject(int row, int col) {
        return null
    }

    @Override
    int getRowCount() {
        return purchaseOrders.size()
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
        PurchaseOrder purchaseOrder = purchaseOrders.get(row)
        if(col == PO_COL) {
            purchaseOrder.toString()
        } else if (col==PURCHASE_COL){
            purchaseOrder.purchaseTransaction?.toString()
        } else if (col==PAY_COL){
            purchaseOrder.paymentTransaction?.toString()
        } else null
    }
}
