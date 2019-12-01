package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class SalesOrders extends BusinessCollection<SalesOrder> {

    int id = 0

    static SalesOrder mergeOrders(ArrayList<SalesOrder> ordersToAdd) {
        SalesOrder salesOrder = new SalesOrder()
        for (SalesOrder orderToAdd:ordersToAdd) {
            ArrayList<OrderItem> orderItemsToAdd = orderToAdd.businessObjects
            for (OrderItem orderitemToAdd : orderItemsToAdd) {
                String name = orderitemToAdd.name
                BigDecimal salesPriceForItem = orderitemToAdd.salesPriceForItem
                int numberOfItems = orderitemToAdd.numberOfItems
                Article article = orderitemToAdd.article
                OrderItem newItem = new OrderItem(numberOfItems, article, name+" @("+salesPriceForItem+")", null)
                newItem.setSalesPriceForItem(salesPriceForItem)
                salesOrder.addBusinessObject(newItem)
            }
        }
        salesOrder
    }

    SalesOrder addBusinessObject(SalesOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if (order.id==null) {
            order.id = id
        }
        order.setName(Utils.toIDString("SO", order.id, 3))
        order.addSalesOrderToArticles()
        super.addBusinessObject(order)
    }

    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }

}
