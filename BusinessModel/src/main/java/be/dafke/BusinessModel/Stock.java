package be.dafke.BusinessModel;

import java.util.*;

public class Stock extends OrderItems {
    private HashSet<Article> articles = new HashSet<>();

    public void buyOrder(PurchaseOrder purchaseOrder){
        addToStock(purchaseOrder);
    }

    public void addToStock(OrderItems order){
        for (OrderItem orderItem : order.getBusinessObjects()) {
            addBusinessObject(orderItem);
        }
    }

    public void sellOrder(SalesOrder salesOrder){
        removeFromStock(salesOrder);
    }


    public void removeFromStock(OrderItems order){
        for (OrderItem orderItem : order.getBusinessObjects()) {
            removeBusinessObject(orderItem);
        }
    }

    public OrderItem addBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        articles.add(article);
        return orderItem;
    }

    public void removeBusinessObject(OrderItem orderItem) {
//        Article article = orderItem.getArticle();
//        Integer nrInStock = article.getNrInStock();
//        if(nrInStock == 0){
//            articles.remove(article);
//        }
    }

    @Override
    public ArrayList<OrderItem> getBusinessObjects() {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for(Article article: articles) {
            Integer nrInStock = article.getNrInStock();
            Integer itemsPerUnit = article.getItemsPerUnit();
            Integer unit = nrInStock / itemsPerUnit;
            OrderItem item = new OrderItem(unit, nrInStock, article, null);
            orderItems.add(item);
        }
        return orderItems;
    }
}
