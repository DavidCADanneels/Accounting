package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class StockOrders extends BusinessCollection<StockOrder> {

    int id = 0

    StockOrder addBusinessObject(StockOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if(order.id==null) {
            order.id = id
        }
        order.setName(Utils.toIDString("ST", order.id, 3))
        order.addStockOrderToArticles()
        super.addBusinessObject(order)
    }

    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }
}
