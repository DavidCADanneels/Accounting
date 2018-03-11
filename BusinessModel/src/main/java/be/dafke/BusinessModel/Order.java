package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Order extends OrderItems {
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

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public abstract void setOrderItem(OrderItem orderItem);

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
