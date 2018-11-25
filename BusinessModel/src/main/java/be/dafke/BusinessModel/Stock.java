package be.dafke.BusinessModel;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Stock extends OrderItems {
    private boolean removeIfEmpty = true;
    protected final HashMap<String,Order> orders = new HashMap<>(); // "PO1", PurchaseOrder ; "SO1", SalesOrder ; ...
    protected final HashMap<Article,Integer> stock = new HashMap<>(); // "Coconut", 24 ; "Pala", 15 ; ...

    public void buyOrder(PurchaseOrder purchaseOrder){
        for (OrderItem orderItem : purchaseOrder.getBusinessObjects()) {
            addBusinessObject(orderItem);
        }
        addOrder(purchaseOrder);
    }

    public void sellOrder(SalesOrder salesOrder){
        for (OrderItem orderItem : salesOrder.getBusinessObjects()) {
            removeBusinessObject(orderItem);
        }
        addOrder(salesOrder);
    }

    public OrderItem addBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        Integer nr = stock.getOrDefault(article, 0);
        nr+=orderItem.getNumberOfItems();
        stock.put(article, nr);
        return orderItem;
    }

    public void removeBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        Integer nr = stock.getOrDefault(article, 0);
        nr-=orderItem.getNumberOfItems();
        stock.put(article, nr);
    }

    @Override
    public ArrayList<OrderItem> getBusinessObjects() {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for(Article article:stock.keySet()) {
            Integer nrInStock = stock.get(article);
            Integer itemsPerUnit = article.getItemsPerUnit();
            Integer unit = nrInStock / itemsPerUnit;
            OrderItem item = new OrderItem(unit, nrInStock, article);
            orderItems.add(item);
        }
        return orderItems;
    }

    public void payOrder(Order order){
        addOrder(order);
    }

    public Collection<Order> getOrders() {
        return orders.values();
    }

    public void addOrder(Order order){
        orders.put(order.getName(), order);
    }
}
