package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class PurchaseOrders extends BusinessCollection<PurchaseOrder> {

    int id = 0

    PurchaseOrder addBusinessObject(PurchaseOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if(order.id==null) {
            order.id = id
        }
        order.setName(Utils.toIDString("PO", order.id, 3))
        order.addPurchaseOrderToArticles()
        super.addBusinessObject(order)
    }

    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }
}
