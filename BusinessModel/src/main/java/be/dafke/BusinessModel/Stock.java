package be.dafke.BusinessModel;

import java.util.Collection;
import java.util.HashMap;

public class Stock extends OrderItems {
    private boolean removeIfEmpty = true;
    protected final HashMap<String,Order> transactions = new HashMap<>();

    public void buyOrder(PurchaseOrder purchaseOrder){
        for (OrderItem orderItem : purchaseOrder.getBusinessObjects()) {
            OrderItem newOrderItem = new OrderItem(orderItem.getNumberOfUnits(), orderItem.getNumberOfItems(), orderItem.getArticle());
            addBusinessObject(newOrderItem);
        }
        addTransaction(purchaseOrder);
    }

    public void sellOrder(SalesOrder salesOrder){
        for (OrderItem orderItem : salesOrder.getBusinessObjects()) {
            OrderItem newOrderItem = new OrderItem(orderItem.getNumberOfUnits(), orderItem.getNumberOfItems(), orderItem.getArticle());
            remove(newOrderItem, true, removeIfEmpty);
        }
        addTransaction(salesOrder);
    }

    public void payOrder(Order order){
        addTransaction(order);
    }

    public Collection<Order> getTransactions() {
        return transactions.values();
    }

    public void addTransaction(Order order){
        transactions.put(order.getName(), order);
    }
}
