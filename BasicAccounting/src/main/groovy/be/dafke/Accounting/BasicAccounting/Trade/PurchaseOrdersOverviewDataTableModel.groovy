package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.PurchaseOrders

import static java.util.ResourceBundle.getBundle 

class PurchaseOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<PurchaseOrder> {

    PurchaseOrders purchaseOrders


    PurchaseOrdersOverviewDataTableModel() {
        super()
        columnNames.put(CONTACT_COL, getBundle("Contacts").getString("SUPPLIER"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if (purchaseOrders == null) null
        PurchaseOrder purchaseOrder = getObject(row, col)
        if(purchaseOrder == null) null

        if (col == CONTACT_COL) {
            purchaseOrder.getSupplier()
        }
        if (col == PRICE_TOTAL_EXCL_COL) {
            purchaseOrder.getTotalPurchasePriceExclVat()
        }
        if (col == VAT_AMOUNT_COL) {
            purchaseOrder.getTotalPurchaseVat()
        }
        if (col == PRICE_TOTAL_INCL_COL) {
            purchaseOrder.getTotalPurchasePriceInclVat()
        }
        if (col == ORDER_NR_COL) {
            purchaseOrder.getName()
        }
        if (col == DATE_COL) {
            purchaseOrder.getDeliveryDate()
        }
        null
    }

    @Override
    int getRowCount() {
        if(purchaseOrders == null) 0
        List<PurchaseOrder> businessObjects = purchaseOrders.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    @Override
    PurchaseOrder getObject(int row, int col) {
        List<PurchaseOrder> purchaseOrders = this.purchaseOrders.getBusinessObjects()
        if(purchaseOrders == null || purchaseOrders.size() == 0) null
        purchaseOrders.get(row)
    }

    int getRow(PurchaseOrder order) {
        if(purchaseOrders == null) -1
        ArrayList<PurchaseOrder> businessObjects = purchaseOrders.getBusinessObjects()
        for(int row=0;row<businessObjects.size();row++){
            if(getObject(row,0)==order) row
        }
        -1
    }

    void setAccounting(Accounting accounting) {
        purchaseOrders = accounting.getPurchaseOrders()
        fireTableDataChanged()
    }
}