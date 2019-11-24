package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class PurchaseOrders extends BusinessCollection<PurchaseOrder> {

    private int id = 0

    PurchaseOrder addBusinessObject(PurchaseOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if(order.getId()==null) {
            order.setId(id)
        }
        order.setName(Utils.toIDString("PO", order.getId(), 3))
        order.addPurchaseOrderToArticles()
        super.addBusinessObject(order)
    }

    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }
}
