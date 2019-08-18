package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PromoOrders extends BusinessCollection<PromoOrder>{

    private int id = 0;

    public PromoOrder addBusinessObject(PromoOrder order) throws EmptyNameException, DuplicateNameException {
        id++;
        if(order.getId()==null) {
            order.setId(id);
        }
        order.setName(Utils.toIDString("PR", order.getId(), 3));
        order.addPromoOrderToArticles();
        return super.addBusinessObject(order);
    }

    public static PromoOrder mergeOrders(ArrayList<PromoOrder> ordersToAdd) {
        PromoOrder promoOrder = new PromoOrder();
        for (PromoOrder orderToAdd:ordersToAdd) {
            ArrayList<OrderItem> orderItemsToAdd = orderToAdd.getBusinessObjects();
            for (OrderItem orderitemToAdd : orderItemsToAdd) {
                String name = orderitemToAdd.getName();
                BigDecimal purchasePriceForItem = orderitemToAdd.getPurchasePriceForItem();
                int numberOfItems = orderitemToAdd.getNumberOfItems();
                Article article = orderitemToAdd.getArticle();
                OrderItem newItem = new OrderItem(numberOfItems, article, name+" @("+purchasePriceForItem+")", null);
                newItem.setSalesPriceForItem(purchasePriceForItem);
                promoOrder.addBusinessObject(newItem);
            }
        }
        return promoOrder;
    }


    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
