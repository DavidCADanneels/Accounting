package be.dafke.Accounting.BusinessModel

import java.math.RoundingMode

class Order extends OrderItems {
    private Contact customer, supplier
    private Transaction paymentTransaction
    private Integer id
    private String deliveryDate = null
    private String description
    private boolean creditNote

    void setArticles(Articles articles){
        articles.getBusinessObjects().forEach({ article ->
            addBusinessObject(new OrderItem(0, article, this))
        })
    }

    BigDecimal getTotalStockValue() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getStockValue()).setScale(2, RoundingMode.HALF_DOWN)
//            total = total.add(orderItem.getStockValue())
        }
        // FIXME: set scale for each addition or only at the end?
//        total.setScale(2, RoundingMode.HALF_DOWN)
        total
    }

    Contact getCustomer() {
        customer
    }

    void setCustomer(Contact customer) {
        this.customer = customer
    }

    Contact getSupplier() {
        supplier
    }

    void setSupplier(Contact supplier) {
        this.supplier = supplier
    }

    void removeEmptyOrderItems() {
        getBusinessObjects().forEach({ orderItem ->
            int numberOfUnits = orderItem.getNumberOfUnits()
            int numberOfItems = orderItem.getNumberOfItems()
            if (numberOfUnits == 0 && numberOfItems == 0) {
                remove(orderItem, false, true)
            }
        })
    }

    Transaction getPaymentTransaction() {
        paymentTransaction
    }

    void setPaymentTransaction(Transaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction
    }

    Integer getId() {
        id
    }

    void setId(Integer id) {
        this.id = id
    }

    void setName(String name){
        super.setName(name)
    }

    String getDeliveryDate() {
        deliveryDate
    }

    void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate
    }

    void setDescription(String description) {
        this.description = description
    }

    String getDescription() {
        description
    }

    boolean isCreditNote() {
        creditNote
    }

    void setCreditNote(boolean creditNote) {
        this.creditNote = creditNote
    }
}