package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Order extends OrderItems {
    private Contact customer, supplier;
    private Transaction paymentTransaction;
    private Integer id;
    private String deliveryDate = null;
    private String description;
    private boolean creditNote;

    public void setArticles(Articles articles){
        articles.getBusinessObjects().forEach( article -> {
            addBusinessObject(new OrderItem(0,0,article, this));
        });
    }

    public BigDecimal getTotalStockValue() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getStockValue()).setScale(2, RoundingMode.HALF_DOWN);
//            total = total.add(orderItem.getStockValue());
        }
        // FIXME: set scale for each addition or only at the end?
//        return total.setScale(2, RoundingMode.HALF_DOWN);
        return total;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name){
        super.setName(name);
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCreditNote() {
        return creditNote;
    }

    public void setCreditNote(boolean creditNote) {
        this.creditNote = creditNote;
    }
}