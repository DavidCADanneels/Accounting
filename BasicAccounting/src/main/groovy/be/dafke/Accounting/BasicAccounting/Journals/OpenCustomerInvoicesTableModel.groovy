package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class OpenCustomerInvoicesTableModel extends SelectableTableModel<SalesOrder> {

    static int ORDER_NR_COL = 0
    static int CONTACT_NAME_COL = 1
    static int AMOUNT_COL = 2
    static int NR_OF_COL = 3

    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    OpenCustomerInvoicesTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(ORDER_NR_COL, String.class)
        columnClasses.put(CONTACT_NAME_COL, Contact.class)
        columnClasses.put(AMOUNT_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("SALES_ORDER_NR"))
        columnNames.put(CONTACT_NAME_COL, getBundle("Accounting").getString("CUSTOMER"))
        columnNames.put(AMOUNT_COL, getBundle("Accounting").getString("AMOUNT"))
    }

    static List<SalesOrder> getSalesOrders(){
        Session.activeAccounting.salesOrders.getBusinessObjects({ order ->
            order.salesTransaction != null && order.paymentTransaction == null
        })
    }

    @Override
    SalesOrder getObject(int row, int col) {
        Session.activeAccounting.salesOrders?getSalesOrders().get(row):null
    }

    @Override
    int getRowCount() {
        return Session.activeAccounting.salesOrders?getSalesOrders().size():0
    }

    @Override
    int getColumnCount() {
        return NR_OF_COL
    }

    @Override
    Object getValueAt(int row, int col) {
        SalesOrder salesOrder = getObject(row, col)
        if(salesOrder == null) return null
        if(col == CONTACT_NAME_COL) {
            salesOrder.customer.name
        } else if (col == ORDER_NR_COL){
            salesOrder.name
        } else if (col == AMOUNT_COL){
            salesOrder.getTotalSalesPriceInclVat()
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
