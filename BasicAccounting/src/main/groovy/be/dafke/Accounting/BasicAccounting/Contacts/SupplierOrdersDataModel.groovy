package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTableModel

class SupplierOrdersDataModel extends SelectableTableModel<PurchaseOrder> {

    ArrayList<PurchaseOrder> purchaseOrders

    SupplierOrdersDataModel(ArrayList<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders
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
        return 3
    }

    @Override
    Object getValueAt(int row, int col) {
        PurchaseOrder purchaseOrder = purchaseOrders.get(row)
        if(col == 0) {
            purchaseOrder.toString()
        } else if (col==1){
            purchaseOrder.purchaseTransaction?.toString()
        } else if (col==2){
            purchaseOrder.paymentTransaction?.toString()
        } else null
    }
}
