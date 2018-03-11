package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Order extends OrderItems {
    private Articles articles;
    private Contact customer, supplier;
    private boolean delivered, payed;
    private boolean placed;

    public Order(Articles articles) {
        super();
        this.articles = articles;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumber();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchaseVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumber();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchaseVat(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumber();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePriceWithVat(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public enum OrderType{
        PURCHASE, SALE;
    }

    public void setItem(OrderItem orderItem){
        Article article = orderItem.getArticle();
        int totalNumber = orderItem.getNumber();
        if(totalNumber<=0){
            stock.remove(article);
        } else {
            stock.put(article, totalNumber);
        }
    }

    public OrderItem getBusinessObject(String name){
        Article article = articles.getBusinessObject(name);
        return getBusinessObject(article);
    }

    public Contact getCustomer() {
        return customer;
    }

    public void setCustomer(Contact customer) {
        this.customer = customer;
    }

    public Contact getSupplier() {
        return supplier;
    }

    public void setSupplier(Contact supplier) {
        this.supplier = supplier;
    }
}
