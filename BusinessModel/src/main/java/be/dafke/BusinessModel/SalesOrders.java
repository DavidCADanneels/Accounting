package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SalesOrders extends BusinessCollection<SalesOrder>{

    private int id = 0;

    public static SalesOrder mergeOrders(ArrayList<SalesOrder> ordersToAdd) {
        SalesOrder salesOrder = new SalesOrder();
        for (SalesOrder orderToAdd:ordersToAdd) {
            ArrayList<OrderItem> orderItemsToAdd = orderToAdd.getBusinessObjects();
            for (OrderItem orderitemToAdd : orderItemsToAdd) {
                String name = orderitemToAdd.getName();
                BigDecimal salesPriceForItem = orderitemToAdd.getSalesPriceForItem();
                int numberOfItems = orderitemToAdd.getNumberOfItems();
                Article article = orderitemToAdd.getArticle();
                OrderItem newItem = new OrderItem(numberOfItems, article, name+" @("+salesPriceForItem+")", null);
                newItem.setSalesPriceForItem(salesPriceForItem);
                salesOrder.addBusinessObject(newItem);
            }
        }
        return salesOrder;
    }

    public SalesOrder addBusinessObject(SalesOrder order) throws EmptyNameException, DuplicateNameException {
        id++;
        if (order.getId()==null) {
            order.setId(id);
        }
        order.setName(Utils.toIDString("SO", order.getId(), 3));
        order.addSalesOrderToArticles();
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }

}
