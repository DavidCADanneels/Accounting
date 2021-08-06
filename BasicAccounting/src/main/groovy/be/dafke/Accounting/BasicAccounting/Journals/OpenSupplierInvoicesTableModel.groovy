package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class OpenSupplierInvoicesTableModel extends SelectableTableModel<PurchaseOrder> {

    static int ORDER_NR_COL = 0
    static int CONTACT_NAME_COL = 1
    static int AMOUNT_COL = 2
    static int NR_OF_COL = 3

    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    OpenSupplierInvoicesTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(ORDER_NR_COL, String.class)
        columnClasses.put(CONTACT_NAME_COL, Contact.class)
        columnClasses.put(AMOUNT_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("PURCHASE_ORDER_NR"))
        columnNames.put(CONTACT_NAME_COL, getBundle("Accounting").getString("SUPPLIER"))
        columnNames.put(AMOUNT_COL, getBundle("Accounting").getString("AMOUNT"))
    }

    static List<PurchaseOrder> getPurchaseOrders(){
        Session.activeAccounting.purchaseOrders.getBusinessObjects({ order ->
            order.purchaseTransaction != null && order.paymentTransaction == null
        })
    }

    @Override
    PurchaseOrder getObject(int row, int col) {
        Session.activeAccounting.purchaseOrders?getPurchaseOrders().get(row):null
    }

    @Override
    int getRowCount() {
        return Session.activeAccounting.purchaseOrders?getPurchaseOrders().size():0
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
        } else if (col == AMOUNT_COL){
            purchaseOrder.getTotalPurchasePriceInclVat()
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
