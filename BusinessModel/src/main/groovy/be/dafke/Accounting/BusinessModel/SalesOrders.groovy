package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class SalesOrders extends BusinessCollection<SalesOrder> {

    private int id = 0

    static SalesOrder mergeOrders(ArrayList<SalesOrder> ordersToAdd) {
        SalesOrder salesOrder = new SalesOrder()
        for (SalesOrder orderToAdd:ordersToAdd) {
            ArrayList<OrderItem> orderItemsToAdd = orderToAdd.getBusinessObjects()
            for (OrderItem orderitemToAdd : orderItemsToAdd) {
                String name = orderitemToAdd.getName()
                BigDecimal salesPriceForItem = orderitemToAdd.getSalesPriceForItem()
                int numberOfItems = orderitemToAdd.getNumberOfItems()
                Article article = orderitemToAdd.getArticle()
                OrderItem newItem = new OrderItem(numberOfItems, article, name+" @("+salesPriceForItem+")", null)
                newItem.setSalesPriceForItem(salesPriceForItem)
                salesOrder.addBusinessObject(newItem)
            }
        }
        salesOrder
    }

    SalesOrder addBusinessObject(SalesOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if (order.getId()==null) {
            order.setId(id)
        }
        order.setName(Utils.toIDString("SO", order.getId(), 3))
        order.addSalesOrderToArticles()
        super.addBusinessObject(order)
    }

    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }

}
