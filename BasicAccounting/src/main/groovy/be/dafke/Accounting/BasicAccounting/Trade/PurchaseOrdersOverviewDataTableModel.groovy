package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.PurchaseOrders
import be.dafke.Accounting.BusinessModelDao.Session

import static java.util.ResourceBundle.getBundle

class PurchaseOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<PurchaseOrder> {

    PurchaseOrdersOverviewDataTableModel() {
        super()
        columnNames.put(CONTACT_COL, getBundle("Contacts").getString("SUPPLIER"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        PurchaseOrders purchaseOrders = Session.activeAccounting.purchaseOrders
        if (purchaseOrders == null) return null
        PurchaseOrder purchaseOrder = getObject(row, col)
        if(purchaseOrder == null) return null
        if (col == CONTACT_COL) return purchaseOrder.supplier
        if (col == PRICE_TOTAL_EXCL_COL) return purchaseOrder.totalPurchasePriceExclVat
        if (col == VAT_AMOUNT_COL) return purchaseOrder.totalPurchaseVat
        if (col == PRICE_TOTAL_INCL_COL) return purchaseOrder.totalPurchasePriceInclVat
        if (col == INVOICE_PATH_COL) {
            if(purchaseOrder?.invoicePath != null) {
                String path = purchaseOrder.invoicePath
                int index = path.lastIndexOf('\\')
                return path.substring(index+1)
            }
            return null
        }
        if (col == ORDER_NR_COL) return purchaseOrder.name
        if (col == DATE_COL) return purchaseOrder.deliveryDate
        null
    }

    @Override
    int getRowCount() {
        Session.activeAccounting.purchaseOrders?.businessObjects?.size()?:0
    }

    @Override
    PurchaseOrder getObject(int row, int col) {
        List<PurchaseOrder> purchaseOrders = Session.activeAccounting.purchaseOrders.businessObjects
        if(purchaseOrders == null || purchaseOrders.size() == 0) return null
        purchaseOrders.get(row)
    }

    int getRow(PurchaseOrder order) {
        PurchaseOrders purchaseOrders = Session.activeAccounting.purchaseOrders
        if(purchaseOrders == null) return -1
        ArrayList<PurchaseOrder> businessObjects = purchaseOrders.businessObjects
        for(int row=0;row<businessObjects.size();row++){
            if(getObject(row,0)==order) return row
        }
        -1
    }
}