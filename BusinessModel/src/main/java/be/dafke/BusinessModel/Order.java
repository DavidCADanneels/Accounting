package be.dafke.BusinessModel;

public class Order extends OrderItems {
//    private Articles articles;
    private Contact customer, supplier;
    private boolean delivered, payed;
    private boolean placed;

    public Order(Articles articles) {
        super();
//        setArticles(articles);
//    }
//
//    private void setArticles(Articles articles){
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
}
