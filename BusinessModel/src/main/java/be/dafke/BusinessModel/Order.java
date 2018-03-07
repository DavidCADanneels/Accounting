package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Order extends StockItems{
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
        for (StockItem stockItem : getBusinessObjects()) {
            Article article = stockItem.getArticle();
            int number = stockItem.getNumber();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchaseVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (StockItem stockItem : getBusinessObjects()) {
            Article article = stockItem.getArticle();
            int number = stockItem.getNumber();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchaseVat(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (StockItem stockItem : getBusinessObjects()) {
            Article article = stockItem.getArticle();
            int number = stockItem.getNumber();
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

    public void setItem(StockItem stockItem){
        Article article = stockItem.getArticle();
        int totalNumber = stockItem.getNumber();
        if(totalNumber<=0){
            stock.remove(article);
        } else {
            stock.put(article, totalNumber);
        }
    }

    public StockItem getBusinessObject(String name){
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
