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
        if (salesOrders == null) return null
        SalesOrder salesOrder = getObject row, col
        if(salesOrder == null) return null
        if (col == CONTACT_COL) return salesOrder.customer
        if (col == PRICE_TOTAL_EXCL_COL) return salesOrder.totalSalesPriceExclVat
        if (col == VAT_AMOUNT_COL) return salesOrder.totalSalesVat
        if (col == PRICE_TOTAL_INCL_COL) return salesOrder.totalSalesPriceInclVat
        if (col == ORDER_NR_COL) return salesOrder.name
        if (col == DATE_COL) return salesOrder.deliveryDate
        null
    }

    @Override
    int getRowCount() {
        if(salesOrders == null) return 0
        List<SalesOrder> businessObjects = filter?salesOrders.getBusinessObjects(filter):salesOrders.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) return 0
        businessObjects.size()
    }

    @Override
    SalesOrder getObject(int row, int col) {
        List<SalesOrder> businessObjects = filter?salesOrders.getBusinessObjects(filter):salesOrders.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) return null
        businessObjects.get(row)
    }

    void setAccounting(Accounting accounting) {
        salesOrders = accounting.salesOrders
        fireTableDataChanged()
    }

    void setFilter(Predicate<SalesOrder> filter) {
        this.filter = filter
    }
}
