package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModelDao.Session


class UnpayedSupplierInvoicesTableModel extends SupplierInvoicesTableModel {

    @Override
    List<PurchaseOrder> getPurchaseOrders(){
        if(Session?.activeAccounting?.purchaseOrders) {
            return Session.activeAccounting.purchaseOrders.getBusinessObjects({ order ->
                order.purchaseTransaction != null && (order.paymentTransactions.isEmpty() || order.amountLeftToPay != BigDecimal.ZERO)
            })
        } else return []
    }

    @Override
    int getRowCount() {
        return getPurchaseOrders().size()
    }
}
