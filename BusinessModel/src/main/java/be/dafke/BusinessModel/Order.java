package be.dafke.BusinessModel;

import java.util.Calendar;

public class Order extends OrderItems {
//    private Articles articles;
    private Contact customer, supplier;
    private boolean delivered, payed;
    private boolean placed;
    private Transaction paymentTransaction;
    private Integer id;
    private String date = null;
    private String description;

    public Order() {
        super();
    }

    public void setArticles(Articles articles){
//        this.articles = articles;
        articles.getBusinessObjects().forEach( article -> {
            addBusinessObject(new OrderItem(0,0,article));
        });
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

    public void removeEmptyOrderItems() {
        getBusinessObjects().forEach(orderItem -> {
            int numberOfUnits = orderItem.getNumberOfUnits();
            int numberOfItems = orderItem.getNumberOfItems();
            if (numberOfUnits==0 && numberOfItems==0) {
                remove(orderItem, false, true);
            }
        });
    }

    public Transaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(Transaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
