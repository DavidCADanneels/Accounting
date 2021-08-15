package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModelDao.Session


class UnpayedSupplierInvoicesTableModel extends SupplierInvoicesTableModel {

    @Override
    List<PurchaseOrder> getPurchaseOrders(){
        Session.activeAccounting.purchaseOrders.getBusinessObjects({ order ->
            order.purchaseTransaction != null && order.paymentTransaction == null
        })
    }
}
