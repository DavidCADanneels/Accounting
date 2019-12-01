package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.Accounting.BusinessModel.SalesOrders

import java.util.function.Predicate

import static java.util.ResourceBundle.getBundle

class SalesOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<SalesOrder> {

    SalesOrders salesOrders
    Predicate<SalesOrder> filter

    SalesOrdersOverviewDataTableModel(){
        super()
        columnNames.put(CONTACT_COL, getBundle("Contacts").getString("CUSTOMER"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if (salesOrders == null) null
        SalesOrder salesOrder = getObject(row, col)
        if(salesOrder == null) null

        if (col == CONTACT_COL) {
            salesOrder.getCustomer()
        }
        if (col == PRICE_TOTAL_EXCL_COL) {
            salesOrder.getTotalSalesPriceExclVat()
        }
        if (col == VAT_AMOUNT_COL) {
            salesOrder.getTotalSalesVat()
        }
        if (col == PRICE_TOTAL_INCL_COL) {
            salesOrder.getTotalSalesPriceInclVat()
        }
        if (col == ORDER_NR_COL) {
            salesOrder.getName()
        }
        if (col == DATE_COL) {
            salesOrder.getDeliveryDate()
        }
        null
    }

    @Override
    int getRowCount() {
        if(salesOrders == null) 0
        List<SalesOrder> businessObjects = filter==null?salesOrders.getBusinessObjects():salesOrders.getBusinessObjects(filter)
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    @Override
    SalesOrder getObject(int row, int col) {
        List<SalesOrder> businessObjects = filter==null?salesOrders.getBusinessObjects():salesOrders.getBusinessObjects(filter)
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects.get(row)
    }

    void setAccounting(Accounting accounting) {
        salesOrders = accounting.getSalesOrders()
        fireTableDataChanged()
    }

    void setFilter(Predicate<SalesOrder> filter) {
        this.filter = filter
    }
}
