package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class PromoOrders extends BusinessCollection<PromoOrder> {

    int id = 0

    PromoOrder addBusinessObject(PromoOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if(order.id==null) {
            order.id = id
        }
        order.setName(Utils.toIDString("PR", order.id, 3))
        order.addPromoOrderToArticles()
        super.addBusinessObject(order)
    }

    static PromoOrder mergeOrders(ArrayList<PromoOrder> ordersToAdd) {
        PromoOrder promoOrder = new PromoOrder()
        for (PromoOrder orderToAdd:ordersToAdd) {
            ArrayList<OrderItem> orderItemsToAdd = orderToAdd.businessObjects
            for (OrderItem orderitemToAdd : orderItemsToAdd) {
                String name = orderitemToAdd.name
                BigDecimal purchasePriceForItem = orderitemToAdd.getPurchasePriceForItem()
                int numberOfItems = orderitemToAdd.numberOfItems
                Article article = orderitemToAdd.article
                OrderItem newItem = new OrderItem(numberOfItems, article, name+" @("+purchasePriceForItem+")", null)
                newItem.setSalesPriceForItem(purchasePriceForItem)
                promoOrder.addBusinessObject(newItem)
            }
        }
        promoOrder
    }


    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }
}
