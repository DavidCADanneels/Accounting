package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModelDao.Session


class UnbookedSupplierInvoicesTableModel extends SupplierInvoicesTableModel {

    @Override
    List<PurchaseOrder> getPurchaseOrders(){
        if(Session?.activeAccounting?.purchaseOrders) {
            return Session.activeAccounting.purchaseOrders.getBusinessObjects({ order ->
                order.purchaseTransaction == null
            })
        } else {
            return []
        }
    }

    @Override
    int getRowCount() {
        return getPurchaseOrders().size()
    }
}
