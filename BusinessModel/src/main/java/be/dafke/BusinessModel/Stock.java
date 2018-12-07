package be.dafke.BusinessModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Stock extends OrderItems {
    protected final HashMap<String,Order> orders = new HashMap<>(); // "PO1", PurchaseOrder ; "SO1", SalesOrder ; ...
    protected final HashMap<Article,Integer> numberInStock = new HashMap<>(); // "Coconut", 24 ; "Pala", 15 ; ...

    public void buyOrder(PurchaseOrder purchaseOrder){
        addToStock(purchaseOrder);
        addOrder(purchaseOrder);
    }

    public void addToStock(OrderItems order){
        for (OrderItem orderItem : order.getBusinessObjects()) {
            addBusinessObject(orderItem);
        }
    }

    public void sellOrder(SalesOrder salesOrder){
        removeFromStock(salesOrder);
        addOrder(salesOrder);
    }

    public void removeFromStock(OrderItems order){
        for (OrderItem orderItem : order.getBusinessObjects()) {
            removeBusinessObject(orderItem);
        }
    }

    public OrderItem addBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        Integer nr = numberInStock.getOrDefault(article, 0);
        nr+=orderItem.getNumberOfItems();
        numberInStock.put(article, nr);
        return orderItem;
    }

    public void removeBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        Integer nr = numberInStock.getOrDefault(article, 0);
        nr-=orderItem.getNumberOfItems();
        numberInStock.put(article, nr);
    }

    @Override
    public ArrayList<OrderItem> getBusinessObjects() {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for(Article article: numberInStock.keySet()) {
            Integer nrInStock = numberInStock.get(article);
            Integer itemsPerUnit = article.getItemsPerUnit();
            Integer unit = nrInStock / itemsPerUnit;
            OrderItem item = new OrderItem(unit, nrInStock, article);
            orderItems.add(item);
        }
        return orderItems;
    }

    public Collection<Order> getOrders() {
        return orders.values();
    }

    public void addOrder(Order order){
        orders.put(order.getName(), order);
    }
}
