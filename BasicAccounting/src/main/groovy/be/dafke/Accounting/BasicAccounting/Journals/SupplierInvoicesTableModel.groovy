package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

abstract class SupplierInvoicesTableModel extends SelectableTableModel<PurchaseOrder> {

    static int ORDER_NR_COL = 0
    static int CONTACT_NAME_COL = 1
    static int TOTAL_AMOUNT_COL = 2
    static int LEFT_AMOUNT_COL = 3
    static int NR_OF_COL = 4

    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    SupplierInvoicesTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(ORDER_NR_COL, String.class)
        columnClasses.put(CONTACT_NAME_COL, Contact.class)
        columnClasses.put(TOTAL_AMOUNT_COL, BigDecimal.class)
        columnClasses.put(LEFT_AMOUNT_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("PURCHASE_ORDER_NR"))
        columnNames.put(CONTACT_NAME_COL, getBundle("Accounting").getString("SUPPLIER"))
        columnNames.put(TOTAL_AMOUNT_COL, getBundle("Accounting").getString("TOTAL_AMOUNT"))
        columnNames.put(LEFT_AMOUNT_COL, getBundle("Accounting").getString("LEFT_AMOUNT"))
    }

    abstract List<PurchaseOrder> getPurchaseOrders()

    @Override
    PurchaseOrder getObject(int row, int col) {
        Session.activeAccounting.purchaseOrders?getPurchaseOrders().get(row):null
    }

    @Override
    int getRowCount() {
        return Session.activeAccounting?.purchaseOrders?getPurchaseOrders().size():0
    }

    @Override
    int getColumnCount() {
        return NR_OF_COL
    }

    @Override
    Object getValueAt(int row, int col) {
        PurchaseOrder purchaseOrder = getObject(row, col)
        if(purchaseOrder == null) return null
        if(col == CONTACT_NAME_COL) {
            purchaseOrder.supplier.name
        } else if (col == ORDER_NR_COL){
            purchaseOrder.name
        } else if (col == TOTAL_AMOUNT_COL){
            purchaseOrder.getTotalPurchasePriceInclVat()
        } else if (col == LEFT_AMOUNT_COL){
            purchaseOrder.amountLeftToPay
        } else null
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
}
